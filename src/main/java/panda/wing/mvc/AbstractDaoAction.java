package panda.wing.mvc;

import panda.dao.DaoClient;
import panda.ioc.annotation.IocInject;

public class AbstractDaoAction extends AbstractAction {

	/*------------------------------------------------------------
	 * DaoClientAware Implements
	 *------------------------------------------------------------*/
	@IocInject
	protected DaoClient daoClient;

	/**
	 * @return the daoClient
	 */
	public DaoClient getDaoClient() {
		return daoClient;
	}
}
