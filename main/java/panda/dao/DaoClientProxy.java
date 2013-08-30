package panda.dao;

import java.util.Map;


/**
 * @author yf.frank.wang@gmail.com
 */
public class DaoClientProxy implements DaoClient {
	
	private static DaoClient dataAccessClient;
	
	/**
	 * @return the dataAccessClient
	 */
	public static DaoClient getDataAccessClient() {
		return dataAccessClient;
	}

	/**
	 * @param dataAccessClient the dataAccessClient to set
	 */
	public static void setDataAccessClient(DaoClient dataAccessClient) {
		DaoClientProxy.dataAccessClient = dataAccessClient;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return dataAccessClient.getName();
	}
	
	/**
	 * @return model meta data map
	 */
	public Map<String, ModelMetaData> getMetaDataMap() {
		return dataAccessClient.getMetaDataMap();
	}
	
	/**
	 * @param name model name
	 * @return model meta data
	 */
	public ModelMetaData getMetaData(String name) {
		return dataAccessClient.getMetaData(name);
	}

	/**
	 * @param name model name
	 * @return modelDao
	 */
	public ModelDAO getModelDAO(String name, DaoSession session) {
		return dataAccessClient.getModelDAO(name, session);
	}

	/**
	 * @return An DataAccessSession instance.
	 */
	public DaoSession openSession() throws DaoException {
		return dataAccessClient.openSession();
	}

	/**
	 * @param autoCommit auto commit
	 * @return An DataAccessSession instance.
	 */
	public DaoSession openSession(boolean autoCommit) throws DaoException {
		return dataAccessClient.openSession(autoCommit);
	}
}
