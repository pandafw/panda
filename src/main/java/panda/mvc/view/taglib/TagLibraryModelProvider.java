package panda.mvc.view.taglib;

import panda.mvc.ActionContext;

/**
 * Provides Freemarker implementation classes for a tag library
 */
public interface TagLibraryModelProvider {

	/**
	 * Gets a Java object that contains getters for the tag library's Freemarker models. Called once
	 * per Freemarker template processing.
	 */
	Object getModels(ActionContext context);

}
