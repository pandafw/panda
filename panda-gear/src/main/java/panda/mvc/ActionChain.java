package panda.mvc;

/**
 * Mvc action process chain
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
