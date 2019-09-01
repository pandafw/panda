package panda.mvc.util;

public interface ActionPreprocessor {
	/**
	 * pre-process before action method invoke
	 * @return false to stop the action
	 */
	boolean preprocess();
}
