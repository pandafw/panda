package panda.mvc.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import panda.Panda;
import panda.bind.json.Jsons;
import panda.io.Files;
import panda.io.Streams;
import panda.ioc.Ioc;
import panda.ioc.IocProxy;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionConfig;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.mvc.IocProvider;
import panda.mvc.Loading;
import panda.mvc.MvcConfig;
import panda.mvc.Mvcs;
import panda.mvc.Setup;
import panda.mvc.ViewMaker;
import panda.mvc.annotation.AdaptBy;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Chain;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.To;
import panda.mvc.config.AbstractMvcConfig;
import panda.mvc.ioc.provider.DefaultIocProvider;

public class DefaultMvcLoading implements Loading {

	private static final Log log = Logs.getLog(DefaultMvcLoading.class);

	public ActionMapping load(AbstractMvcConfig config) {
		if (log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Panda.Mvc [%s] is initializing ...", config.getAppName())).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - Panda Version   : %s ", Panda.VERSION)).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - OS              : %s %s", Systems.OS_NAME, Systems.OS_ARCH)).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - Java            : %s", Systems.JAVA_VERSION)).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - Charset         : %s", Charsets.defaultEncoding())).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - Timezone        : %s", Systems.USER_TIMEZONE)).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - User Directory  : %s", Systems.getUserDir())).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - Temp Directory  : %s", Files.getTempDirectoryPath())).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - File Separator  : %s", Systems.FILE_SEPARATOR)).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - Server Info     : %s", config.getServletContext().getServerInfo())).append(Streams.LINE_SEPARATOR);
			sb.append(String.format(" - Servlet API     : %d.%d", 
				config.getServletContext().getMajorVersion(), 
				config.getServletContext().getMinorVersion()))
				.append(Streams.LINE_SEPARATOR);

			if (config.getServletContext().getMajorVersion() > 2 || config.getServletContext().getMinorVersion() > 4) {
				sb.append(String.format(" - ContextPath     : %s", config.getServletContext().getContextPath())).append(Streams.LINE_SEPARATOR);
			}
			sb.append(String.format(" - Web Directory   : %s", config.getServletContext().getRealPath("/"))).append(Streams.LINE_SEPARATOR);
			log.info(sb.toString());
		}

		ActionMapping mapping;

		StopWatch sw = new StopWatch();
		try {
			setContextClass(config);
			
			createIoc(config);

			mapping = evalActionMapping(config);

			evalSetup(config, true);
		}
		catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Error happend during start service!", e);
			}
			throw Exceptions.wrapThrow(e);
		}
		sw.stop();

		if (log.isInfoEnabled()) {
			log.infof("Panda.Mvc [%s] is up in %s", config.getAppName(), sw.toString());
		}
		
		return mapping;

	}

	protected ActionMapping evalActionMapping(AbstractMvcConfig mcfg) throws Exception {
		ActionMapping mapping = createActionMapping(mcfg);
		if (log.isInfoEnabled()) {
			log.infof("Build " + ActionMapping.class.getName() + " by %s ...", mapping.getClass().getName());
		}

		Class<?> mainModule = mcfg.getMainModule();
		
		createViewMaker(mcfg);

		ActionChainMaker chainMaker = createChainMaker(mcfg);

		// create action info for mail module
		ActionConfig mainCfg = createActionConfig(mcfg.getIoc(), mainModule);

		// scan classes
		Collection<Class<?>> actions = scanModules(mainModule);
		if (actions.isEmpty()) {
			if (log.isWarnEnabled()) {
				log.warn("No action class found!!!");
			}
			return mapping;
		}

		int atMethods = 0;
		for (Class<?> action : actions) {
			ActionConfig clsCfg = createActionConfig(mcfg.getIoc(), action).mergeWith(mainCfg);
			for (Method method : action.getMethods()) {
				// public & not synthetic/bridge (ignore generic type method for super class) & @At is declared
				if (Modifier.isPublic(method.getModifiers()) 
						&& !method.isSynthetic() 
						&& !method.isBridge()
						&& method.isAnnotationPresent(At.class)) {
					// add to mapping
					ActionConfig acfg = createActionConfig(mcfg.getIoc(), method).mergeWith(clsCfg);
					mapping.add(chainMaker, acfg, mcfg);
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
			if (log.isInfoEnabled()) {
				log.infof("Found %d module methods", atMethods);
				log.infof(ActionMapping.class.getName() + ": %s", mapping.toString());
			}
		}

		return mapping;
	}

	protected ActionMapping createActionMapping(MvcConfig config) throws Exception {
		ActionMapping um = config.getIoc().getIfExists(ActionMapping.class);
		if (um == null) {
			um = new RegexActionMapping();
		}

		if (log.isDebugEnabled()) {
			log.debug("Use " + ActionMapping.class.getName() + ": " +  um.getClass());
		}
		return um;
	}

	protected ActionChainMaker createChainMaker(MvcConfig mcfg) {
		ActionChainMaker maker = mcfg.getIoc().get(ActionChainMaker.class);

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
				setup.initialize();
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
			log.debugf("@IocBy(type=%s, args=%s, mirror=%s)", ip.getName(), Jsons.toJson(args), (ib == null ? null : ib.mirror().getName()));
		}
		
		Ioc ioc = Classes.born(ip).create(config, args);

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
	
	//----------------------------------------------------------------
	protected Set<Class<?>> scanModules(Class<?> mainModule) {
		Set<Class<?>> actions = new LinkedHashSet<Class<?>>();

		Modules ann = mainModule.getAnnotation(Modules.class);
		if (ann == null) {
			// no annotation, add the specified class
			addAction(actions, mainModule, true);
			return actions;
		}

		// scan packages
		Set<String> packages = new LinkedHashSet<String>();

		if (ann.scan()) {
			// add default main package
			packages.add(mainModule.getPackage().getName());
		}

		if (Arrays.isNotEmpty(ann.packages())) {
			packages.addAll(Arrays.asList(ann.packages()));
		}

		if (Collections.isNotEmpty(packages)) {
			if (log.isDebugEnabled()) {
				log.debug(" > scan " + Arrays.toString(packages));
			}
			
			List<Class<?>> subs = Classes.scan(packages);
			for (Class<?> sub : subs) {
				addAction(actions, sub, false);
			}
		}

		// add classes
		for (Class<?> action : ann.value()) {
			addAction(actions, action, true);
		}

		return actions;
	}

	private void addAction(Set<Class<?>> actions, Class<?> action, boolean warn) {
		if (isAction(action)) {
			if (log.isDebugEnabled()) {
				log.debug(" > add " + action.getName());
			}
			actions.add(action);
		}
		else if (warn) {
			log.warn(" > ignore " +  action.getName());
		}
	}
	
	protected ActionConfig createActionConfig(Ioc ioc, Class<?> type) {
		ActionConfig ac = new ActionConfig();
		evalHttpAdaptor(ac, type.getAnnotation(AdaptBy.class));
		evalViews(ac, type.getAnnotation(To.class));
		evalAt(ioc, ac, type.getAnnotation(At.class), null);
		evalActionChainMaker(ac, type.getAnnotation(Chain.class));
		evalAction(ac, type);
		return ac;
	}

	protected ActionConfig createActionConfig(Ioc ioc, Method method) {
		ActionConfig am = new ActionConfig();
		evalHttpAdaptor(am, method.getAnnotation(AdaptBy.class));
		evalViews(am, method.getAnnotation(To.class));
		evalAt(ioc, am, method.getAnnotation(At.class), method.getName());
		evalActionChainMaker(am, method.getAnnotation(Chain.class));
		am.setActionMethod(method);
		return am;
	}

	protected void evalActionChainMaker(ActionConfig ac, Chain cb) {
		if (null != cb) {
			ac.setChainName(cb.value());
		}
	}

	protected void evalAt(Ioc ioc, ActionConfig ac, At at, String def) {
		if (at != null) {
			if (Arrays.isNotEmpty(at.value())) {
				IocProxy ip = new IocProxy(ioc);
				String[] ps = new String[at.value().length];
				for (int i = 0; i < ps.length; i++) {
					String a = at.value()[i];
					a = Strings.isEmpty(a) ? a : Mvcs.translate(a, ip);
					ps[i] = a.length() > 1 ? Strings.stripEnd(a, '/') : a;
				}
				ac.setPaths(ps);
			}
			else if (def != null) {
				ac.setPaths(Arrays.toArray(def));
			}

			if (Arrays.isNotEmpty(at.method())) {
				ac.setAtMethods(at.method());
			}
		}
	}

	protected void evalViews(ActionConfig am, To vm) {
		if (vm == null) {
			return;
		}
		if (Strings.isNotEmpty(vm.all())) {
			am.setOkView(vm.all());
			am.setErrorView(vm.all());
			am.setFatalView(vm.all());
		}
		if (Strings.isNotEmpty(vm.value())) {
			am.setOkView(vm.value());
		}
		if (Strings.isNotEmpty(vm.error())) {
			am.setErrorView(vm.error());
		}
		if (Strings.isNotEmpty(vm.fatal())) {
			am.setFatalView(vm.fatal());
		}
	}

	protected void evalAction(ActionConfig am, Class<?> type) {
		am.setActionType(type);
	}

	protected void evalHttpAdaptor(ActionConfig am, AdaptBy ab) {
		if (null != ab) {
			am.setAdaptor(ab.type());
		}
	}

	protected boolean isAction(Class<?> cls) {
		try {
			int cm = cls.getModifiers();
			if (Modifier.isAbstract(cm) || Modifier.isInterface(cm)
					|| !Modifier.isPublic(cm)
					|| !cls.isAnnotationPresent(At.class)) {
				return false;
			}

			for (Method m : cls.getMethods()) {
				if (m.isAnnotationPresent(At.class)) {
					return true;
				}
			}
		}
		catch (Throwable e) {
			// skip
		}
		return false;
	}
}
