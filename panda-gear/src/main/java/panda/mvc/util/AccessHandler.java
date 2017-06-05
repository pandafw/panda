package panda.mvc.util;


public interface AccessHandler {
	/**
	 * canDataAccess
	 * @param action action
	 * @return true if the action can access
	 */
	boolean canAccess(String action);

	/**
	 * canAccessData
	 * @param action action
	 * @param data data
	 * @return true if action can access the data
	 */
	boolean canAccessData(String action, Object data);

}
