package panda.dao;

/**
 * @author yf.frank.wang@gmail.com
 */
public class DaoClientHolder {
	
	public DaoClientHolder() {
	}

	private static DaoClient daoClient;
	
	/**
	 * @return the daoClient
	 */
	public static DaoClient getDaoClient() {
		return daoClient;
	}

	/**
	 * @param daoClient the daoClient to set
	 */
	public static void setDaoClient(DaoClient daoClient) {
		DaoClientHolder.daoClient = daoClient;
	}

	/**
	 * @return the daoClient
	 */
	public DaoClient get() {
		return daoClient;
	}
}
