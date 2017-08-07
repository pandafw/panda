package panda.mvc.util;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;


/**
 * SessionStateProvider
 */
@IocBean(type=StateProvider.class, scope=Scope.REQUEST)
public class SessionStateProvider implements StateProvider {
	@IocInject
	protected ActionContext context;
	
	/**
	 * Constructor
	 */
	public SessionStateProvider() {
	}
	
	/**
	 * make session key
	 * @param name state name
	 * @return session key name (path/name)
	 */
	private String toKey(String name) {
		return context.getPath() + '/' + name;
	}

	/**
	 * Save state
	 * @param name state name
	 * @param value state value
	 * @return true if state value saved successfully
	 */
	@Override
	public boolean saveState(String name, String value) {
		String key = toKey(name);
		context.getSession().setAttribute(key, value);
		return true;
	}
	
	/**
	 * Load state
	 * @param name state name
	 * @return state value 
	 */
	@Override
	public String loadState(String name) {
		String key = toKey(name);
		return (String)context.getSession().getAttribute(key);
	}
}
