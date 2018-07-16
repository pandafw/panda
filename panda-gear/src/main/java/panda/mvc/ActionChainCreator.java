package panda.mvc;

public interface ActionChainCreator {

	/**
	 * create action chain
	 * 
	 * @param am  action meta
	 * @return action chain
	 */
	ActionChain create(ActionConfig am);

}
