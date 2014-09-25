package panda.mvc.util;

import java.util.Map;

import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;
import panda.servlet.HttpSessionMap;


/**
 * SessionStateProvider
 */
public class SessionStateProvider implements StateProvider {
	private Map<String, Object> session;
	private String prefix;
	
	/**
	 * Constructor
	 */
	public SessionStateProvider() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param prefix prefix
	 */
	public SessionStateProvider(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * @param context the context to set
	 */
	@IocInject
	public void setActionContext(ActionContext context) {
		this.session = new HttpSessionMap(context.getRequest());
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * get key name
	 * @param name state name
	 * @return key name
	 */
	private String getKey(String name) {
		return prefix == null ? name : prefix + name;
	}

	private Map<String, Object> getSession() {
		return session;
	}

	/**
	 * Save state
	 * @param name state name
	 * @param value state value
	 */
	public void saveState(String name, Object value) {
		getSession().put(getKey(name), value);
	}
	
	/**
	 * Load state
	 * @param name state name
	 * @return state value 
	 */
	public Object loadState(String name) {
		return getSession().get(getKey(name));
	}
}
