package panda.mvc.impl;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.Panda;
import panda.bind.json.Jsons;
import panda.ioc.Ioc;
import panda.ioc.impl.DefaultIoc;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Systems;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionInfo;
import panda.mvc.IocProvider;
import panda.mvc.Loading;
import panda.mvc.LoadingException;
import panda.mvc.MvcConfig;
import panda.mvc.Setup;
import panda.mvc.UrlMapping;
import panda.mvc.ViewMaker;
import panda.mvc.annotation.At;
import panda.mvc.annotation.ChainBy;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.UrlMappingBy;
import panda.mvc.annotation.ViewBy;
import panda.mvc.config.AbstractMvcConfig;
import panda.mvc.ioc.provider.AnnotationIocProvider;

public class MvcLoading implements Loading {

	private static final Log log = Logs.getLog(MvcLoading.class);

	public UrlMapping load(AbstractMvcConfig config) {
		if (log.isInfoEnabled()) {
			log.infof("Panda Version : %s ", Panda.VERSION);
			log.infof("Panda.Mvc [%s] is initializing ...", config.getAppName());
		}

		if (log.isDebugEnabled()) {
			log.debug("Web Container Information:");
			log.debugf(" - Default Charset : %s", Charsets.defaultEncoding());
			log.debugf(" - Current Path    : %s", new File(".").getAbsolutePath());
			log.debugf(" - Java Version    : %s", Systems.JAVA_VERSION);
			log.debugf(" - File separator  : %s", Systems.FILE_SEPARATOR);
			log.debugf(" - Timezone        : %s", Systems.USER_TIMEZONE);
			log.debugf(" - OS              : %s %s", Systems.OS_NAME, Systems.OS_ARCH);
			log.debugf(" - Server Info     : %s", config.getServletContext().getServerInfo());
			log.debugf(" - Servlet API     : %d.%d", 
				config.getServletContext().getMajorVersion(), config.getServletContext().getMinorVersion());

			if (config.getServletContext().getMajorVersion() > 2 || config.getServletContext().getMinorVersion() > 4) {
				log.debugf(" - ContextPath     : %s", config.getServletContext().getContextPath());
			}
		}

		/*
		 * 准备返回值
		 */
		UrlMapping mapping;

		/*
		 * 准备计时
		 */
		StopWatch sw = new StopWatch();
		try {
			/*
			 * 创建上下文
			 */
			createLoadingContext(config);

			/*
			 * 检查 Ioc 容器并创建和保存它
			 */
			createIoc(config);

			/*
			 * 组装UrlMapping
			 */
			mapping = evalUrlMapping(config);

			/*
			 * 执行用户自定义 Setup
			 */
			evalSetup(config, true);
		}
		catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Error happend during start service!", e);
			}
			throw new LoadingException(e);
		}

		// ~ Done ^_^
		sw.stop();
		if (log.isInfoEnabled()) {
			log.infof("Panda.Mvc [%s] is up in %s", config.getAppName(), sw.toString());
		}
		
		return mapping;

	}

	protected UrlMapping evalUrlMapping(AbstractMvcConfig config) throws Exception {
		// 准备 UrlMapping
		UrlMapping mapping = createUrlMapping(config);
		if (log.isInfoEnabled()) {
			log.infof("Build URL mapping by %s ...", mapping.getClass().getName());
		}

		Class<?> mainModule = config.getMainModule();
		
		// 创建视图工厂
		ViewMaker viewMaker = createViewMaker(config);

		// 创建动作链工厂
		ActionChainMaker chainMaker = createChainMaker(config);

		// 创建主模块的配置信息
		ActionInfo mainInfo = Loadings.createInfo(mainModule);

		// 准备要加载的模块列表
		Collection<Class<?>> actions = Loadings.scanModules(mainModule);

		if (actions.isEmpty()) {
			if (log.isWarnEnabled()) {
				log.warn("No action class found!!!");
			}
			return mapping;
		}

		int atMethods = 0;

		// 分析所有的子模块
		for (Class<?> action : actions) {
			ActionInfo actionInfo = Loadings.createInfo(action).mergeWith(mainInfo);
			for (Method method : action.getMethods()) {
				// public 并且声明了 @At 的函数，才是入口函数
				if (Modifier.isPublic(method.getModifiers())  && method.isAnnotationPresent(At.class)) {
					// 增加到映射中
					ActionInfo info = Loadings.createInfo(method).mergeWith(actionInfo);
					info.setViewMaker(viewMaker);
					mapping.add(chainMaker, info, config);
					atMethods++;
				}
			}
		}

		if (atMethods == 0) {
			if (log.isWarnEnabled()) {
				log.warn("None @At found in any modules class!!");
			}
		}
		else {
			log.infof("Found %d module methods", atMethods);
		}

		return mapping;
	}

	protected void createLoadingContext(AbstractMvcConfig config) {
		// 构建一个上下文对象，方便子类获取更多的环境信息
		// 同时，所有 Filter 和 Adaptor 都可以用 ${app.root} 来填充自己
		Map<String, Object> context = new HashMap<String, Object>();

		String appRoot = config.getAppRoot();
		context.put("app.root", appRoot);

		if (log.isDebugEnabled()) {
			log.debugf(">> app.root = %s", appRoot);
		}

		// 载入环境变量
		for (Entry<String, String> entry : System.getenv().entrySet()) {
			context.put("env." + entry.getKey(), entry.getValue());
		}
		
		// 载入系统变量
		for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
			context.put("sys." + entry.getKey(), entry.getValue());
		}

		if (log.isTraceEnabled()) {
			log.tracef(">>\nCONTEXT %s", Jsons.toJson(context, true));
		}
		config.setLoadingContext(context);
	}

	protected UrlMapping createUrlMapping(MvcConfig config) throws Exception {
		UrlMappingBy umb = config.getMainModule().getAnnotation(UrlMappingBy.class);
		if (umb != null) {
			return Loadings.evalObj(config, umb.value(), umb.args());
		}
		return new UrlMappingImpl();
	}

	protected ActionChainMaker createChainMaker(MvcConfig config) {
		Class<?> mainModule = config.getMainModule();
		ChainBy ann = mainModule.getAnnotation(ChainBy.class);
		ActionChainMaker maker;
		
		if (null == ann) {
			maker = new DefaultActionChainMaker();
		}
		else {
			maker = Loadings.evalObj(config, ann.type(), ann.args());
		}
		
		if (log.isDebugEnabled()) {
			log.debugf("@ChainBy(%s)", maker.getClass().getName());
		}
		return maker;
	}
	
	protected ViewMaker createViewMaker(MvcConfig config) {
		Class<?> mainModule = config.getMainModule();
		ViewBy ann = mainModule.getAnnotation(ViewBy.class);
		ViewMaker maker;
		
		if (null == ann) {
			maker = new DefaultViewMaker();
		}
		else {
			maker = Loadings.evalObj(config, ann.type(), ann.args());
		}
		
		if (log.isDebugEnabled()) {
			log.debugf("@ViewBy(%s)", maker.getClass().getName());
		}
		return maker;
	}

	protected void evalSetup(MvcConfig config, boolean init) throws Exception {
		Ioc ioc = config.getIoc();
		if (ioc != null && ioc.has(Setup.class)) {
			Setup setup = ioc.get(Setup.class);
			if (init) {
				setup.init(config);
			}
			else {
				setup.destroy(config);
			}
		}
	}

	protected Ioc createIoc(AbstractMvcConfig config) throws Exception {
		Class<?> mainModule = config.getMainModule();

		Class<? extends IocProvider> ip = null;
		String[] args = null;
		
		IocBy ib = mainModule.getAnnotation(IocBy.class);
		if (ib == null) {
			log.warn("@IocBy is not defined for " + mainModule + ", use default ioc setting");
			ip = AnnotationIocProvider.class;
		}
		else {
			ip = ib.type();
			args = ib.args();
		}
		
		if (log.isDebugEnabled()) {
			log.debugf("@IocBy(type=%s, args=%s)", ip.getName(), Jsons.toJson(args));
		}
		
		Ioc ioc = Classes.born(ip).create(config, args);
		if (ioc instanceof DefaultIoc) {
			((DefaultIoc)ioc).addValueProxyMaker(new ServletValueProxyMaker(config.getServletContext()));
		}

		config.setIoc(ioc);
		return ioc;
	}

	public void depose(MvcConfig config) {
		if (log.isInfoEnabled()) {
			log.infof("Panda.Mvc [%s] is deposing ...", config.getAppName());
		}
		
		StopWatch sw = new StopWatch();

		// Firstly, upload the user customized destroy
		try {
			evalSetup(config, false);
		}
		catch (Exception e) {
			throw new LoadingException(e);
		}
		finally {
			// If the application has Ioc, depose it
			Ioc ioc = config.getIoc();
			if (null != ioc) {
				ioc.depose();
			}
		}

		// Done, print info
		sw.stop();
		if (log.isInfoEnabled()) {
			log.infof("Panda.Mvc [%s] is down in %s", config.getAppName(), sw.toString());
		}
	}
}
