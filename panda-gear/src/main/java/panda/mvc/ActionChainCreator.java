package panda.mvc;

public interface ActionChainCreator {

	/**
	 * create action chain
	 * 
	 * @param mc Mvc configuration
	 * @param am  action meta
	 * @return action chain
	 */
	ActionChain create(MvcConfig mc, ActionConfig am);

}
