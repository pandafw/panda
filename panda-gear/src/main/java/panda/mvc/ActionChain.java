package panda.mvc;

/**
 * Mvc action processor chain
 * 
 * <p>
 * !! thread safe !!
 * 
 */
public interface ActionChain {

	ActionConfig getConfig();
	
	void doChain(ActionContext ac);

	void doNext(ActionContext ac);
}
