package panda.wing.action;

import panda.dao.DaoClient;
import panda.ioc.annotation.IocInject;
import panda.mvc.util.ActionSupport;
import panda.mvc.util.PermissionProvider;
import panda.wing.util.AppActionAssist;
import panda.wing.util.AppActionConsts;

public class AbstractAction extends ActionSupport implements PermissionProvider {
	/*------------------------------------------------------------
	 * bean
	 *------------------------------------------------------------*/
	@IocInject
	protected DaoClient daoClient;

	/**
	 * @return the daoClient
	 */
	protected DaoClient getDaoClient() {
		return daoClient;
	}

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
