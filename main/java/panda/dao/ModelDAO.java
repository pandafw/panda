package panda.dao;


/**
 * @param <T> model type
 * @param <E> model example type
 * @author yf.frank.wang@gmail.com
 */
public interface ModelDAO<T, E extends QueryParameter> extends EntityDAO<T, E> {

	/**
	 * @return the dataAccessClient
	 */
	DaoClient getDataAccessClient();

	/**
	 * @return the dataAccessSession
	 */
	DaoSession getDataAccessSession();

	/**
	 * @param dataAccessSession the dataAccessSession to set
	 */
	void setDataAccessSession(DaoSession dataAccessSession);
}
