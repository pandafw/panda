package panda.mvc.view.taglib;

import java.util.List;

import panda.mvc.ActionContext;

/**
 * Provides Freemarker implementation classes for a tag library
 */
public interface TagLibrary {
	/**
	 * Gets a Java object that contains getters for the tag library's Freemarker models. Called once
	 * per Freemarker template processing.
	 */
	Object getModels(ActionContext context);

	/**
	 * Gets a list of Velocity directive classes for the tag library. Called once on framework
	 * startup when initializing Velocity.
	 * 
	 * @return A list of Velocity directive classes
	 */
	public List<Class> getDirectiveClasses();
}
