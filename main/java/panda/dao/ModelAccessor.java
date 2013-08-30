package panda.dao;

import java.util.Map;

import panda.bean.PropertyHandler;


/**
 * @param <T> data type
 * @author yf.frank.wang@gmail.com
 */
public interface ModelAccessor<T> extends PropertyHandler<T> {
	/**
	 * create data object
	 * @return data instance 
	 */
	T createObject();

	/**
	 * set data properties values from map
	 * @param data data
	 * @param map map
	 */
	void setDataProperties(T data, Map<String, Object> map);
	
	/**
	 * get data property values map
	 * @param data data
	 * @return property values map
	 */
	Map<String, Object> getDataProperties(T data);
}
