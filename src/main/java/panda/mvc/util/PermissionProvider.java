package panda.mvc.util;


public interface PermissionProvider {
	/**
	 * hasPermission
	 * @param action action
	 * @return true if action has access permission
	 */
	boolean hasPermission(String action);

	/**
	 * hasDataPermission
	 * @param data data
	 * @param action action
	 * @return true if action has access permission
	 */
	boolean hasDataPermission(Object data, String action);

}
