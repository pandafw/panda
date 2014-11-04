package panda.mvc.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import javax.servlet.ServletContext;

import panda.Panda;
import panda.bind.json.Jsons;
import panda.io.Files;
import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Systems;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.IocProvider;
import panda.mvc.Loading;
import panda.mvc.MvcConfig;
import panda.mvc.Setup;
import panda.mvc.UrlMapping;
import panda.mvc.ViewMaker;
import panda.mvc.annotation.At;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.config.AbstractMvcConfig;
import panda.mvc.ioc.provider.DefaultIocProvider;

public class DefaultMvcLoading implements Loading {

	private static final Log log = Logs.getLog(DefaultMvcLoading.class);

	public UrlMapping load(AbstractMvcConfig config) {
		if (log.isInfoEnabled()) {
			log.infof("Panda Version : %s ", Panda.VERSION);
			log.infof("Panda.Mvc [%s] is initializing ...", config.getAppName());
		}

		if (log.isDebugEnabled()) {
			log.debug("Web Container Information:");
			log.debugf(" - OS              : %s %s", Systems.OS_NAME, Systems.OS_ARCH);
			log.debugf(" - Java            : %s", Systems.JAVA_VERSION);
			log.debugf(" - Charset         : %s", Charsets.defaultEncoding());
			log.debugf(" - Timezone        : %s", Systems.USER_TIMEZONE);
			log.debugf(" - User Directory  : %s", Systems.getUserDir());
			log.debugf(" - Temp Directory  : %s", Files.getTempDirectoryPath());
			log.debugf(" - File Separator  : %s", Systems.FILE_SEPARATOR);
			log.debugf(" - Server Info     : %s", config.getServletContext().getServerInfo());
			log.debugf(" - Servlet API     : %d.%d", 
				config.getServletContext().getMajorVersion(), config.getServletContext().getMinorVersion());

			if (config.getServletContext().getMajorVersion() > 2 || config.getServletContext().getMinorVersion() > 4) {
				log.debugf(" - ContextPath     : %s", config.getServletContext().getContextPath());
			}
			log.debugf(" - Web Directory  : %s", config.getServletContext().getRealPath("/"));
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
			setContextClass(config);
			
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
			throw Exceptions.wrapThrow(e);
		}

		// Done
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
		createViewMaker(config);

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

	protected UrlMapping createUrlMapping(MvcConfig config) throws Exception {
		UrlMapping  um = config.getIoc().getIfExists(UrlMapping.class);
		if (um == null) {
			um = new DefaultUrlMapping();
		}

		if (log.isDebugEnabled()) {
			log.debug("Use UrlMapping: " +  um.getClass());
		}
		return um;
	}

	protected ActionChainMaker createChainMaker(MvcConfig config) {
		ActionChainMaker maker = config.getIoc().getIfExists(ActionChainMaker.class);
		if (maker == null) {
			maker = new DefaultActionChainMaker();
		}

		if (log.isDebugEnabled()) {
			log.debug("Use ActionChainMaker: " +  maker.getClass());
		}
		return maker;
	}
	
	protected ViewMaker createViewMaker(MvcConfig config) {
		ViewMaker maker = config.getIoc().getIfExists(ViewMaker.class);
		if (maker == null) {
			maker = new DefaultViewMaker();
		}

		if (log.isDebugEnabled()) {
			log.debug("Use ViewMaker: " +  maker.getClass());
		}
		return maker;
	}

	protected void evalSetup(MvcConfig config, boolean init) {
		Ioc ioc = config.getIoc();
		if (ioc != null && ioc.has(Setup.class)) {
			Setup setup = ioc.get(Setup.class);
			if (init) {
				setup.init();
			}
			else {
				setup.destroy();
			}
		}
	}

	protected void setContextClass(AbstractMvcConfig config) {
		Class<?> mm = config.getMainModule();
		Modules ms = mm.getAnnotation(Modules.class);
		config.setContextClass(ms == null ? ActionContext.class : ms.context());
	}
	
	protected void createIoc(AbstractMvcConfig config) {
		Class<?> mainModule = config.getMainModule();

		Class<? extends IocProvider> ip = null;
		String[] args = null;
		
		IocBy ib = mainModule.getAnnotation(IocBy.class);
		if (ib == null) {
			log.warn("@IocBy is not defined for " + mainModule + ", use default ioc setting");
			ip = DefaultIocProvider.class;
		}
		else {
			ip = ib.type();
			args = ib.args();
		}
		
		if (log.isDebugEnabled()) {
			log.debugf("@IocBy(type=%s, args=%s)", ip.getName(), Jsons.toJson(args));
		}
		
		Ioc ioc = Classes.born(ip).create(config, args);
		IocContext ictx = ioc.getContext();
		
		// save default beans
		ictx.save(Scope.APP, MvcConfig.class.getName(), new ObjectProxy(config));
		ictx.save(Scope.APP, ServletContext.class.getName(), new ObjectProxy(config.getServletContext()));
		ictx.save(Scope.APP, "$servlet", new ObjectProxy(config.getServletContext()));

		config.setIoc(ioc);
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
