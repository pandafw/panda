package panda.dao;

import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;


/**
 * @author yf.frank.wang@gmail.com
 */
public class DaoClientProxy extends DaoClient {
	
	public DaoClientProxy() {
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
		DaoClientProxy.daoClient = daoClient;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return daoClient.getName();
	}

	/**
	 * @return datebase meta
	 */
	@Override
	public DatabaseMeta getDatabaseMeta() {
		return daoClient.getDatabaseMeta();
	}

	/**
	 * @param type record type
	 * @return the entity
	 */
	@Override
	public <T> Entity<T> getEntity(Class<T> type) {
		return daoClient.getEntity(type);
	}

	/**
	 * @param type record type
	 * @param param argument used for dynamic table
	 * @return the entity
	 */
	@Override
	public <T> EntityDao<T> getEntityDao(Class<T> type, Object param) {
		return daoClient.getEntityDao(type, param);
	}
	
	/**
	 * @return An DataAccessSession instance.
	 */
	@Override
	public Dao getDao() {
		return daoClient.getDao();
	}
}
