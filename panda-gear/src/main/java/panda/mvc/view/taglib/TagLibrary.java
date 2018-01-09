package panda.mvc.view.taglib;

import java.util.List;

import panda.mvc.ActionContext;

/**
 * Tag library interface
 */
public interface TagLibrary {
	/**
	 * Gets a Java object that contains getters for the tag library's Freemarker models. Called once
	 * per Freemarker template processing.
	 * @param ac action context
	 * @return freemarker models
	 */
	Object getModels(ActionContext ac);

	/**
	 * Gets a list of Velocity directive classes for the tag library. Called once on framework
	 * startup when initializing Velocity.
	 * 
	 * @return A list of Velocity directive classes
	 */
	public List<Class> getDirectiveClasses();
}
