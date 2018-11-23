package panda.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import panda.Panda;
import panda.bind.json.Jsons;
import panda.io.Files;
import panda.io.Streams;
import panda.ioc.Ioc;
import panda.ioc.bean.IocProxy;
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
import panda.mvc.annotation.AdaptBy;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Chain;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.To;
import panda.mvc.ioc.provider.DefaultIocProvider;

public class MvcLoader {
	private static final Log log = Logs.getLog(MvcLoader.class);

	private static final String DEFAULT_ACTION_METHOD_SEPARATORS = ".:";

	private MvcConfig mvcConfig;
	private Ioc ioc;
	private IocProxy iocp;
	private ActionMapping actionMapping;
	private MvcHandler actionHandler;
	private String separators;
	
	public MvcLoader(MvcConfig config) {
		this.mvcConfig = config;
		
		if (log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Panda.Mvc [%s] is initializing ... ", config.getMainModule().getName())).append(Streams.EOL);
			sb.append(String.format(" - Panda Version   : %s ", Panda.VERSION)).append(Streams.EOL);
			sb.append(String.format(" - OS              : %s %s", Systems.OS_NAME, Systems.OS_ARCH)).append(Streams.EOL);
			sb.append(String.format(" - Java            : %s", Systems.JAVA_VERSION)).append(Streams.EOL);
			sb.append(String.format(" - Charset         : %s", Charsets.defaultEncoding())).append(Streams.EOL);
			sb.append(String.format(" - Timezone        : %s", Systems.USER_TIMEZONE)).append(Streams.EOL);
			sb.append(String.format(" - User Directory  : %s", Systems.getUserDir())).append(Streams.EOL);
			sb.append(String.format(" - Temp Directory  : %s", Files.getTempDirectoryPath())).append(Streams.EOL);
			sb.append(String.format(" - File Separator  : %s", Systems.FILE_SEPARATOR)).append(Streams.EOL);
			sb.append(String.format(" - Server Info     : %s", config.getServletContext().getServerInfo())).append(Streams.EOL);
			sb.append(String.format(" - Servlet API     : %d.%d", 
				config.getServletContext().getMajorVersion(), 
				config.getServletContext().getMinorVersion()))
				.append(Streams.EOL);
			sb.append(String.format(" - Context Name    : %s", config.getServletContext().getServletContextName())).append(Streams.EOL);

			if (config.getServletContext().getMajorVersion() > 2 || config.getServletContext().getMinorVersion() > 4) {
				sb.append(String.format(" - Context Path    : %s", config.getServletContext().getContextPath())).append(Streams.EOL);
			}
			sb.append(String.format(" - Web Directory   : %s", config.getServletContext().getRealPath("/"))).append(Streams.EOL);
			log.info(sb.toString());
		}

		StopWatch sw = new StopWatch();
		try {
			ioc = createIoc(config);
			iocp = new IocProxy(ioc);

			separators = ioc.getIfExists(String.class, MvcConstants.MVC_ACTION_METHOD_SEPARATORS);
			if (separators == null) {
				separators = DEFAULT_ACTION_METHOD_SEPARATORS;
			}
			
			actionMapping = evalActionMapping(config.getMainModule());

			actionHandler = new MvcHandler(this);

			evalSetup(true);
		}
		catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Error happend during start service!", e);
			}
			throw Exceptions.wrapThrow(e);
		}
		sw.stop();

		if (log.isInfoEnabled()) {
			log.infof("Panda.Mvc [%s] is up in %s", config.getMainModule().getName(), sw.toString());
		}
	}

	/**
	 * @return the mvcConfig
	 */
	public MvcConfig getMvcConfig() {
		return mvcConfig;
	}

	/**
	 * @return the ioc
	 */
	public Ioc getIoc() {
		return ioc;
	}

	/**
	 * @return the actionMapping
	 */
	public ActionMapping getActionMapping() {
		return actionMapping;
	}

	/**
	 * @return the actionHandler
	 */
	public MvcHandler getActionHandler() {
		return actionHandler;
	}


	protected ActionMapping evalActionMapping(Class<?> mainModule) {
		ActionMapping mapping = ioc.get(ActionMapping.class);
		if (log.isInfoEnabled()) {
			log.infof("Build " + ActionMapping.class.getName() + " by %s ...", mapping.getClass().getName());
		}

		ActionChainCreator acc = ioc.get(ActionChainCreator.class);

		// create action info for mail module
		ActionConfig mainCfg = createActionConfig(mainModule);

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
			// merge with main module
			ActionConfig clsCfg = createActionConfig(action).mergeWith(mainCfg);

			for (Method method : action.getMethods()) {
				// public & not synthetic/bridge (ignore generic type method for super class) & @At is declared
				if (Modifier.isPublic(method.getModifiers()) 
						&& !method.isSynthetic() 
						&& !method.isBridge()
						&& method.isAnnotationPresent(At.class)) {
					// merge with action
					ActionConfig acfg = createActionConfig(method).mergeWith(clsCfg);

					// add to mapping
					mapping.add(acc, acfg);
					atMethods++;
				}
			}
		}

		if (atMethods == 0) {
			if (log.isWarnEnabled()) {
				log.warn("None @At found in any module class!!");
			}
		}
		else {
			if (log.isInfoEnabled()) {
				log.info("Found " + atMethods + " module methods");
				log.info(ActionMapping.class.getName() + ": " + mapping);
			}
		}

		return mapping;
	}

	protected void evalSetup(boolean init) {
		Setup setup = ioc.getIfExists(Setup.class);
		if (setup != null) {
			if (init) {
				setup.initialize();
			}
			else {
				setup.destroy();
			}
		}
	}

	protected Ioc createIoc(MvcConfig config) {
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

		return ioc;
	}

	public void depose() {
		if (log.isInfoEnabled()) {
			log.infof("Panda.Mvc [%s] is deposing ... ", mvcConfig.getMainModule().getName());
		}
		
		StopWatch sw = new StopWatch();

		// Firstly, upload the user customized destroy
		try {
			evalSetup(false);
		}
		finally {
			ioc.depose();
		}

		// Done, print info
		sw.stop();
		if (log.isInfoEnabled()) {
			log.infof("Panda.Mvc [%s] is down in %s", mvcConfig.getMainModule().getName(), sw.toString());
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

		// add classes
		for (Class<?> action : ann.value()) {
			addAction(actions, action, true);
		}

		Set<String> packages = new LinkedHashSet<String>();
		if (ann.scan()) {
			// add default main package
			packages.add(mainModule.getPackage().getName());
		}

		if (Arrays.isNotEmpty(ann.packages())) {
			// add annotation packages
			for (String pkg : ann.packages()) {
				if (Strings.isNotEmpty(pkg)) {
					packages.add(pkg);
				}
			}
		}

		// scan packages
		if (Collections.isNotEmpty(packages)) {
			for (String pkg : packages) {
				if (log.isDebugEnabled()) {
					log.debug(" > scan " + pkg);
				}

				List<Class<?>> clss = Classes.scan(pkg);
				for (Class<?> cls : clss) {
					addAction(actions, cls, true);
				}
			}
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
	
	protected ActionConfig createActionConfig(Class<?> type) {
		ActionConfig ac = new ActionConfig();
		evalHttpAdaptor(ac, type.getAnnotation(AdaptBy.class));
		evalViews(ac, type.getAnnotation(To.class));
		evalAt(ac, type.getAnnotation(At.class), null);
		evalActionChainMaker(ac, type.getAnnotation(Chain.class));
		evalAction(ac, type);
		return ac;
	}

	protected ActionConfig createActionConfig(Method method) {
		ActionConfig am = new ActionConfig();
		evalHttpAdaptor(am, method.getAnnotation(AdaptBy.class));
		evalViews(am, method.getAnnotation(To.class));
		evalAt(am, method.getAnnotation(At.class), method);
		evalActionChainMaker(am, method.getAnnotation(Chain.class));
		am.setActionMethod(method);
		return am;
	}

	protected void evalActionChainMaker(ActionConfig ac, Chain cb) {
		if (cb != null) {
			ac.setChainName(cb.value());
		}
	}

	protected void evalAt(ActionConfig ac, At at, Method method) {
		if (at == null) {
			return;
		}

		if (Arrays.isNotEmpty(at.value())) {
			String[] ps = new String[at.value().length];
			for (int i = 0; i < ps.length; i++) {
				String a = at.value()[i];
				a = Strings.isEmpty(a) ? a : Mvcs.translate(a, iocp);
				ps[i] = a.length() > 1 ? Strings.stripEnd(a, '/') : a;
			}
			ac.setPaths(ps);
		}
		else if (method != null) {
			String mn = method.getName();
			List<String> ps = new ArrayList<String>();
			ps.add(mn);
			
			if (Strings.isNotEmpty(separators) && Strings.contains(mn, '_')) {
				for (int i = 0; i < separators.length(); i++) {
					ps.add(Strings.replaceChars(mn, '_', separators.charAt(i)));
				}
			}
			ac.setPaths(ps.toArray(new String[ps.size()]));
		}

		if (Arrays.isNotEmpty(at.method())) {
			ac.setAtMethods(at.method());
		}
	}

	protected void evalViews(ActionConfig am, To vm) {
		if (vm == null) {
			return;
		}
		if (Strings.isNotEmpty(vm.value())) {
			am.setDefaultView(vm.value());
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
