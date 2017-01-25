package panda.mvc;

import panda.mvc.impl.ActionInvoker;

/**
 * URL Mapping
 */
public interface UrlMapping {

	/**
	 * add a url mapping
	 * 
	 * @param acm action chain maker
	 * @param ac action config
	 * @param mc MVC config
	 */
	void add(ActionChainMaker acm, ActionConfig ac, MvcConfig mc);

	/**
	 * get action invoker by path
	 * 
	 * @param ac action context
	 * @return action invoker
	 */
	ActionInvoker getActionInvoker(ActionContext ac);

	/**
	 * find action config by path
	 * 
	 * @param path path
	 * @return action config
	 */
	ActionConfig getActionConfig(String path);
}
