package panda.dao;

import java.util.Map;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface DaoSession {
	/**
	 * @return Data Access Client
	 */
	DaoClient getDaoClient();
	
	/**
	 * @return model meta data map
	 */
	Map<String, ModelMetaData> getMetaDataMap();
	
	/**
	 * @param name model name
	 * @return model meta data
	 */
	ModelMetaData getMetaData(String name);

	/**
	 * @param name model name
	 * @return modelDao
	 */
	ModelDAO getModelDAO(String name);
	
	/**
	 * close session
	 */
	void close();
	
	/**
	 * @return true if session is closed
	 */
	boolean isClosed();
	
	/**
	 * commit
	 * @throws DaoException if a data access error occurs
	 */
	void commit() throws DaoException;

	/**
	 * rollback
	 * @throws DaoException if a data access error occurs
	 */
	void rollback() throws DaoException;
}
