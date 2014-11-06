package panda.wing.action;

import panda.dao.DaoClient;
import panda.ioc.annotation.IocInject;
import panda.mvc.util.ActionSupport;
import panda.mvc.util.PermissionProvider;

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
	protected BaseActionAssist assist() {
		return (BaseActionAssist)super.getAssist();
	}

	/**
	 * @return the consts
	 */
	protected BaseActionConsts consts() {
		return (BaseActionConsts)super.getConsts();
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
