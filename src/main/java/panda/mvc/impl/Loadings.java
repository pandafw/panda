package panda.mvc.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionInfo;
import panda.mvc.annotation.Adapt;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Chain;
import panda.mvc.annotation.Encoding;
import panda.mvc.annotation.Fail;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.Ok;
import panda.net.http.HttpMethod;

public abstract class Loadings {

	private static final Log log = Logs.getLog(Loadings.class);

	public static ActionInfo createInfo(Class<?> type) {
		ActionInfo ai = new ActionInfo();
		evalEncoding(ai, type.getAnnotation(Encoding.class));
		evalHttpAdaptor(ai, type.getAnnotation(Adapt.class));
		evalOk(ai, type.getAnnotation(Ok.class));
		evalFail(ai, type.getAnnotation(Fail.class));
		evalAt(ai, type.getAnnotation(At.class), null);
		evalActionChainMaker(ai, type.getAnnotation(Chain.class));
		evalAction(ai, type);
		return ai;
	}

	public static ActionInfo createInfo(Method method) {
		ActionInfo ai = new ActionInfo();
		evalEncoding(ai, method.getAnnotation(Encoding.class));
		evalHttpAdaptor(ai, method.getAnnotation(Adapt.class));
		evalOk(ai, method.getAnnotation(Ok.class));
		evalFail(ai, method.getAnnotation(Fail.class));
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
				ai.setPaths(Arrays.toArray("/" + def.toLowerCase()));
			}

			if (at.method() != null && at.method().length > 0) {
				for (HttpMethod m : at.method()) {
					ai.getHttpMethods().add(m);
				}
			}
		}
	}

	public static void evalFail(ActionInfo ai, Fail fail) {
		if (null != fail) {
			ai.setFailView(fail.value());
		}
	}

	public static void evalOk(ActionInfo ai, Ok ok) {
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

	public static void evalEncoding(ActionInfo ai, Encoding encoding) {
		if (null == encoding) {
			ai.setInputEncoding(Charsets.UTF_8);
			ai.setOutputEncoding(Charsets.UTF_8);
		}
		else {
			ai.setInputEncoding(Strings.defaultString(encoding.input(), Charsets.UTF_8));
			ai.setOutputEncoding(Strings.defaultString(encoding.output(), Charsets.UTF_8));
		}
	}

	private static boolean isAction(Class<?> cls) {
		int cm = cls.getModifiers();
		if (!Modifier.isPublic(cm) || Modifier.isAbstract(cm) || Modifier.isInterface(cm)) {
			return false;
		}
		
		for (Method m : cls.getMethods()) {
			if (m.isAnnotationPresent(At.class)) {
				return true;
			}
		}
		
		return false;
	}

}
