package panda.dao;

import java.util.Map;

import panda.lang.Arrays;
import panda.lang.Classes;


/**
 * @param <T> model type
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractModelMetaData<T> implements ModelMetaData<T> {
	protected Object getPropertyItem(Map<String, Object[]> properties, String propertyName, int i) {
		Object[] objs = properties.get(propertyName);
		return objs == null ? null : objs[i];
	}
	

	public Class[] getPropertyParameterTypes(String propertyName) {
		return Classes.EMPTY_CLASS_ARRAY;
	}
	public String[] getReadPropertyNames(T data) {
		return getPropertyNames();
	}

	public String[] getWritePropertyNames(T data) {
		return getPropertyNames();
	}

	public String[] getReadPropertyNames() {
		return getPropertyNames();
	}

	public String[] getWritePropertyNames() {
		return getPropertyNames();
	}

	public boolean canReadProperty(String propertyName) {
		return Arrays.contains(getPropertyNames(), propertyName);
	}

	public boolean canWriteProperty(String propertyName) {
		return Arrays.contains(getPropertyNames(), propertyName);
	}

	public boolean canReadProperty(T data, String propertyName) {
		return Arrays.contains(getPropertyNames(), propertyName);
	}

	public boolean canWriteProperty(T data, String propertyName) {
		return Arrays.contains(getPropertyNames(), propertyName);
	}

	protected RuntimeException illegalPropertyException(String propertyName) {
		return new IllegalArgumentException("Illegal property name: " + propertyName);
	}
}
