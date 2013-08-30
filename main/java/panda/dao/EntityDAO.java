package panda.dao;

import java.util.List;
import java.util.Map;


/**
 * @param <T> entity type
 * @param <E> entity example type
 * @author yf.frank.wang@gmail.com
 */
public interface EntityDAO<T, E extends QueryParameter> {
	/**
	 * create model example
	 * @return model example
	 */
	E createExample();
	
	/**
	 * exists
	 * 
	 * @param key T
	 * @return T
	 * @throws DaoException if a data access error occurs
	 */ 
	boolean exists(T key) throws DaoException;
	
	/**
	 * count
	 * 
	 * @param example E
	 * @return count
	 * @throws DaoException if a data access error occurs
	 */ 
	int count(E example) throws DaoException;

	/**
	 * select by primary key
	 * 
	 * @param key T
	 * @return T
	 * @throws DaoException if a data access error occurs
	 */ 
	T fetch(T key) throws DaoException;

	/**
	 * selectOneByExample
	 * 
	 * @param example E
	 * @return T 
	 * @throws DaoException if a data access error occurs
	 */ 
	T selectOne(E example) throws DaoException;

	/**
	 * select by example with data handler
	 * 
	 * @param example E
	 * @param dataHandler data handler
	 * @return data count
	 * @throws DaoException if a data access error occurs
	 */ 
	int selectWithDataHandler(E example, DataHandler<T> dataHandler) throws DaoException;

	/**
	 * select by example
	 * 
	 * @param example E
	 * @return list of T 
	 * @throws DaoException if a data access error occurs
	 */ 
	List<T> selectList(E example) throws DaoException;

	/**
	 * select all
	 * 
	 * @return list of T 
	 * @throws DaoException if a data access error occurs
	 */ 
	List<T> selectListAll() throws DaoException;

	/**
	 * selectMapByExample
	 * 
	 * @param example E
	 * @param keyProp The property to be used as the key in the Map.
	 * @return map of T
	 * @throws DaoException if a data access error occurs
	 */ 
	Map<?, T> selectMap(E example, String keyProp) throws DaoException;

	/**
	 * selectMapAll
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @return map of T
	 * @throws DaoException if a data access error occurs
	 */ 
	Map<?, T> selectMapAll(String keyProp) throws DaoException;

	/**
	 * selectMapByExample
	 * 
	 * @param example E
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return map
	 * @throws DaoException if a data access error occurs
	 */ 
	Map<?, ?> selectPropMap(E example, String keyProp, String valProp) throws DaoException;

	/**
	 * selectMapAll
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return map
	 * @throws DaoException if a data access error occurs
	 */ 
	Map<?, ?> selectPropMap(String keyProp, String valProp) throws DaoException;

	/**
	 * if (exists(data)) { update(data); } else { insert(data}; }
	 * 
	 * @param data T
	 * @throws DaoException if a data access error occurs
	 */ 
	void save(T data) throws DaoException;
	
	/**
	 * insert
	 * 
	 * @param data T
	 * @throws DaoException if a data access error occurs
	 */ 
	void insert(T data) throws DaoException;

	/**
	 * delete
	 * 
	 * @param key T
	 * @return count of deleted records
	 * @throws DaoException if a data access error occurs
	 */ 
	int delete(T key) throws DaoException;

	/**
	 * deleteByExample
	 * 
	 * @param example E
	 * @return count of deleted records
	 * @throws DaoException if a data access error occurs
	 */ 
	int deleteByExample(E example) throws DaoException;

	/**
	 * deleteAll
	 * 
	 * @return count of deleted records
	 * @throws DaoException if a data access error occurs
	 */ 
	int deleteAll() throws DaoException;

	/**
	 * update
	 * 
	 * @param data T
	 * @return count of updated records
	 * @throws DaoException if a data access error occurs
	 */ 
	int update(T data) throws DaoException;

	/**
	 * updateIgnoreNull (ignore null properties)
	 * 
	 * @param data T
	 * @return count of updated records
	 * @throws DaoException if a data access error occurs
	 */ 
	int updateIgnoreNull(T data) throws DaoException;

	/**
	 * update by example
	 * 
	 * @param data T
	 * @param example E
	 * @return count of updated records
	 * @throws DaoException if a data access error occurs
	 */ 
	int update(T data, E example) throws DaoException;

	/**
	 * update by example (ignore null properties)
	 * 
	 * @param data T
	 * @param example E
	 * @return count of updated records
	 * @throws DaoException if a data access error occurs
	 */ 
	int updateIgnoreNull(T data, E example) throws DaoException;
}
