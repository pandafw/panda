package panda.app.action;

import panda.app.util.AppActionAssist;
import panda.app.util.AppActionConsts;
import panda.mvc.util.AccessHandler;
import panda.mvc.util.ActionSupport;

public abstract class AbstractAction extends ActionSupport implements AccessHandler {
	/**
	 * @return the assist
	 */
	protected AppActionAssist assist() {
		return (AppActionAssist)super.getAssist();
	}

	/**
	 * @return the consts
	 */
	protected AppActionConsts consts() {
		return (AppActionConsts)super.getConsts();
	}

	/*------------------------------------------------------------
	 * PermissionProvider
	 *------------------------------------------------------------*/
	/**
	 * can access
	 * @param action action
	 * @return true if action can access
	 */
	@Override
	public boolean canAccess(String action) {
		return assist().canAccess(action);
	}

	/**
	 * canAccessData
	 * @param action action
	 * @param data data
	 * @return true if action can access the data
	 */
	@Override
	public boolean canAccessData(String action, Object data) {
		return assist().canAccessData(action, data);
	}
}
