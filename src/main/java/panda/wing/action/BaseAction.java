package panda.wing.action;

import panda.mvc.util.PermissionProvider;
import panda.wing.mvc.AbstractDaoAction;

/**
 */
public class BaseAction extends AbstractDaoAction implements PermissionProvider {

	/**
	 * Constructor
	 */
	public BaseAction() {
	}

	/**
	 * @return the assist
	 */
	protected BaseActionAssist assist() {
		return (BaseActionAssist)super.getAssist();
	}

	/**
	 * @return the consts
	 */
	protected BaseActionConsts consts() {
		return (BaseActionConsts)super.getConsts();
	}

	/**
	 * hasPermission
	 * @param path path
	 * @return true if action has access permit
	 */
	@Override
	public boolean hasPermission(String path) {
		return assist().hasPermission(path);
	}

	/**
	 * hasPermission
	 * @param path path
	 * @return true if action has access permit
	 */
	@Override
	public boolean hasDataPermission(Object data, String path) {
		return assist().hasDataPermission(data, path);
	}
}
