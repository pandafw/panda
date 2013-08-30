package panda.dao;

import java.util.Map;


/**
 * @author yf.frank.wang@gmail.com
 */
public interface DaoClient {
	/**
	 * @return the name
	 */
	String getName();
	
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
	ModelDAO getModelDAO(String name, DaoSession session);

	/**
	 * @return An DataAccessSession instance.
	 */
	DaoSession openSession() throws DaoException;

	/**
	 * @param autoCommit auto commit
	 * @return An DataAccessSession instance.
	 */
	DaoSession openSession(boolean autoCommit) throws DaoException;
}
