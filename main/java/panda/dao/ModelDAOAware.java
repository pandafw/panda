package panda.dao;


/**
 * ModelDAOAware
 * @param <T> data type
 * @param <E> example type
 * @author yf.frank.wang@gmail.com
 */
public interface ModelDAOAware<T, E extends QueryParameter> {

	/**
	 * @param modelDAO the modelDAO to set
	 */
	void setModelDAO(ModelDAO<T, E> modelDAO);
}
