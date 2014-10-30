package panda.mvc.view.taglib;

import java.util.List;

/**
 * Provides Velocity implementation classes for a tag library
 */
public interface TagLibraryDirectiveProvider {

	/**
	 * Gets a list of Velocity directive classes for the tag library. Called once on framework
	 * startup when initializing Velocity.
	 * 
	 * @return A list of Velocity directive classes
	 */
	public List<Class> getDirectiveClasses();

}
