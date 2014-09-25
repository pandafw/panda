package panda.mvc.util;


/**
 * PermissionValidationAware
 */
public interface PermissionValidator {
	/**
	 * hasPermission
	 * @param action action
	 * @return true if action has access permission
	 */
	boolean hasPermission(String action);

	/**
	 * hasPermission
	 * @param namespace namespace
	 * @param action action
	 * @return true if action has access permission
	 */
	boolean hasPermission(String namespace, String action);

	/**
	 * hasDataPermission
	 * @param data data
	 * @param action action
	 * @return true if action has access permission
	 */
	boolean hasDataPermission(Object data, String action);

	/**
	 * hasDataPermission
	 * @param data data
	 * @param namespace namespace
	 * @param action action
	 * @return true if action has access permission
	 */
	boolean hasDataPermission(Object data, String namespace, String action);

}
