package panda.mvc.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionInfo;
import panda.mvc.annotation.Adapt;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Chain;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;
import panda.net.http.HttpMethod;

public abstract class Loadings {

	private static final Log log = Logs.getLog(Loadings.class);

	public static ActionInfo createInfo(Class<?> type) {
		ActionInfo ai = new ActionInfo();
		evalHttpAdaptor(ai, type.getAnnotation(Adapt.class));
		evalOkView(ai, type.getAnnotation(Ok.class));
		evalErrorView(ai, type.getAnnotation(Err.class));
		evalFatalView(ai, type.getAnnotation(Fatal.class));
		evalAt(ai, type.getAnnotation(At.class), null);
		evalActionChainMaker(ai, type.getAnnotation(Chain.class));
		evalAction(ai, type);
		return ai;
	}

	public static ActionInfo createInfo(Method method) {
		ActionInfo ai = new ActionInfo();
		evalHttpAdaptor(ai, method.getAnnotation(Adapt.class));
		evalOkView(ai, method.getAnnotation(Ok.class));
		evalErrorView(ai, method.getAnnotation(Err.class));
		evalFatalView(ai, method.getAnnotation(Fatal.class));
		evalAt(ai, method.getAnnotation(At.class), method.getName());
		evalActionChainMaker(ai, method.getAnnotation(Chain.class));
		ai.setMethod(method);
		return ai;
	}

	public static Set<Class<?>> scanModules(Class<?> mainModule) {
		Set<Class<?>> actions = new HashSet<Class<?>>();

		Modules ann = mainModule.getAnnotation(Modules.class);
		if (ann == null) {
			addAction(actions, mainModule, true);
			return actions;
		}

		for (Class<?> action : ann.value()) {
			addAction(actions, action, true);
		}

		// scan packages
		Set<String> packages = new HashSet<String>();

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

		return actions;
	}

	private static void addAction(Set<Class<?>> actions, Class<?> action, boolean warn) {
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
	
	public static void evalActionChainMaker(ActionInfo ai, Chain cb) {
		if (null != cb) {
			ai.setChainName(cb.value());
		}
	}

	private static void evalAt(ActionInfo ai, At at, String def) {
		if (null != at) {
			if (at.value() != null && at.value().length > 0) {
				ai.setPaths(at.value());
			}
			else if (def != null) {
				ai.setPaths(Arrays.toArray(def.toLowerCase()));
			}

			if (at.method() != null && at.method().length > 0) {
				for (HttpMethod m : at.method()) {
					ai.getHttpMethods().add(m);
				}
			}
		}
	}

	public static void evalFatalView(ActionInfo ai, Fatal fatal) {
		if (null != fatal) {
			ai.setFatalView(fatal.value());
		}
	}

	public static void evalErrorView(ActionInfo ai, Err error) {
		if (null != error) {
			ai.setErrorView(error.value());
		}
	}

	public static void evalOkView(ActionInfo ai, Ok ok) {
		if (null != ok) {
			ai.setOkView(ok.value());
		}
	}

	public static void evalAction(ActionInfo ai, Class<?> type) {
		ai.setActionType(type);
	}

	public static void evalHttpAdaptor(ActionInfo ai, Adapt ab) {
		if (null != ab) {
			ai.setAdaptor(ab.type());
		}
	}

	private static boolean isAction(Class<?> cls) {
		try {
			int cm = cls.getModifiers();
			if (!Modifier.isPublic(cm) || Modifier.isAbstract(cm) || Modifier.isInterface(cm)) {
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
