package panda.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.lang.Collections;

/**
 * AbstractModelDAO
 * @param <T> model type
 * @param <E> model example type
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractModelDAO<T, E extends QueryParameter> implements ModelDAO<T, E> {
	private DaoSession dataAccessSession;
	
	/**
	 * Constructor
	 */
	public AbstractModelDAO() {
	}
	
	/**
	 * Constructor
	 *
	 * @param dataAccessSession the dataAccessSession to set
	 */
	public AbstractModelDAO(DaoSession dataAccessSession) {
		setDataAccessSession(dataAccessSession);
	}

	/**
	 * @return the dataAccessClient
	 */
	public DaoClient getDataAccessClient() {
		return dataAccessSession.getDaoClient();
	}

	/**
	 * @return the dataAccessSession
	 */
	public DaoSession getDataAccessSession() {
		return dataAccessSession;
	}

	/**
	 * @param dataAccessSession the dataAccessSession to set
	 */
	public void setDataAccessSession(DaoSession dataAccessSession) {
		this.dataAccessSession = dataAccessSession;
	}
	
	/**
	 * selectOneByExample
	 * 
	 * @param example E
	 * @return T 
	 * @throws DaoException if a data access error occurs
	 */ 
	public T selectOne(E example) throws DaoException {
		example.setStart(0);
		example.setLimit(1);
		List<T> list = selectList(example);
		if (Collections.isNotEmpty(list)) {
			return list.get(0);
		}
		else {
			return null;
		}
	}

	/**
	 * selectAll
	 * 
	 * @return list of T 
	 * @throws DaoException if a data access error occurs
	 */ 
	public List<T> selectListAll() throws DaoException {
		return selectList(createExample());
	}

	/**
	 * convert list to map
	 * @param list list of T
	 * @param keyProp The property to be used as the key in the Map.
	 * @return map of T 
	 */
	protected Map<Object, T> convertListToMap(List<T> list, String keyProp) {
		Map<Object, T> map = new LinkedHashMap<Object, T>();
		for (T data : list) {
			try {
				Object key = Beans.getBean(data, keyProp);
				map.put(key, data);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return map;
	}
	
	/**
	 * selectMapByExample
	 * 
	 * @param example E
	 * @param keyProp The property to be used as the key in the Map.
	 * @return map of T
	 * @throws DaoException if a data access error occurs
	 */ 
	public Map<?, T> selectMap(E example, String keyProp) throws DaoException {
		List<T> list = selectList(example);
		return convertListToMap(list, keyProp);
	}
	
	/**
	 * selectMapAll
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @return map of T
	 * @throws DaoException if a data access error occurs
	 */ 
	public Map<?, T> selectMapAll(String keyProp) throws DaoException {
		List<T> list = selectList(createExample());
		return convertListToMap(list, keyProp);
	}

	/**
	 * selectMapByExample
	 * 
	 * @param example E
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return map
	 * @throws DaoException if a data access error occurs
	 */ 
	public Map<?, ?> selectPropMap(E example, String keyProp, String valProp) throws DaoException {
		List<T> list = selectList(example);
		return convertListToMap(list, keyProp, valProp);
	}

	/**
	 * selectMapAll
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return map
	 * @throws DaoException if a data access error occurs
	 */ 
	public Map<?, ?> selectPropMap(String keyProp, String valProp) throws DaoException {
		List<T> list = selectList(createExample());
		return convertListToMap(list, keyProp, valProp);
	}

	/**
	 * convert list to map
	 * @param list list of T
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return map
	 */
	protected Map<Object, Object> convertListToMap(List<T> list, String keyProp, String valProp) {
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		for (T data : list) {
			try {
				Object key = Beans.getBean(data, keyProp);
				Object val = Beans.getBean(data, valProp);
				map.put(key, val);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return map;
	}

	/**
	 * deleteAll
	 * 
	 * @return count of deleted records
	 * @throws DaoException if a data access error occurs
	 */ 
	public int deleteAll() throws DaoException {
		return deleteByExample(createExample());
	}
}
