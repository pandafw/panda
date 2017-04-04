package panda.mvc;

import java.lang.reflect.Method;

import panda.mvc.impl.ActionInvoker;

/**
 * Action Mapping
 */
public interface ActionMapping {

	/**
	 * add a mapping
	 * 
	 * @param acm action chain maker
	 * @param acfg action config
	 * @param mcfg MVC config
	 */
	void add(ActionChainMaker acm, ActionConfig acfg, MvcConfig mcfg);

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

	/**
	 * find action config by method
	 * 
	 * @param method method
	 * @return action config
	 */
	ActionConfig getActionConfig(Method method);
}
