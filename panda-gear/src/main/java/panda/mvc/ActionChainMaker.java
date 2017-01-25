package panda.mvc;

public interface ActionChainMaker {

	/**
	 * create action chain
	 * 
	 * @param mc Mvc configuration
	 * @param am  action meta
	 * @return action chain
	 */
	ActionChain eval(MvcConfig mc, ActionConfig am);

}
