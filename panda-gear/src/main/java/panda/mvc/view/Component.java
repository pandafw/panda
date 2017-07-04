package panda.mvc.view;

import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;

public class Component {

	@IocInject
	protected ActionContext context;

	public Object findValue(String expr) {
		return Mvcs.findValue(context, expr);
	}
	
	public Object findValue(String expr, Object arg) {
		return Mvcs.findValue(context, expr, arg);
	}

	public String castString(Object o) {
		if (o == null) {
			return null;
		}
		return Mvcs.castString(context, o);
	}

	public String castString(Object o, String format) {
		if (o == null) {
			return null;
		}
		return Mvcs.castString(context, o, format);
	}

	public String findString(String expr) {
		Object o = findValue(expr);
		return castString(o);
	}
	
	public String findString(String expr, Object arg) {
		Object o = findValue(expr, arg);
		return castString(o);
	}
	
	public <T> T newComponent(Class<T> cls) {
		return context.getIoc().get(cls);
	}
}
