package panda.dao;


/**
 * @author yf.frank.wang@gmail.com
 */
public interface DaoSessionAware {

	/**
	 * @param daoSession the daoSession to set
	 */
	void setDaoSession(DaoSession daoSession);
}
