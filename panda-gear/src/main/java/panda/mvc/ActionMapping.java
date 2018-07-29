package panda.mvc;

import java.lang.reflect.Method;

import panda.mvc.impl.ActionDispatcher;

/**
 * Action Mapping
 */
public interface ActionMapping {

	/**
	 * add a mapping
	 * 
	 * @param acc action chain creator
	 * @param acfg action config
	 */
	void add(ActionChainCreator acc, ActionConfig acfg);

	/**
	 * get action dispatcher by path
	 * 
	 * @param ac action context
	 * @return action dispatcher
	 */
	ActionDispatcher getActionDispatcher(ActionContext ac);

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
	 * @param clazz action class
	 * @param method action method
	 * @return action config
	 */
	ActionConfig getActionConfig(Class clazz, Method method);
}
