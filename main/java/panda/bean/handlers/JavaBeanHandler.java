package panda.bean.handlers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.Beans;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public class JavaBeanHandler<T> extends AbstractJavaBeanHandler<T> {

	private static class PropertyAccessor {
		String name;
		Type type;
		Method getter;
		Method setter;
		
		PropertyAccessor(Type beanType, PropertyDescriptor pd) {
			name = pd.getName();
			getter = pd.getReadMethod();
			setter = pd.getWriteMethod();
			type = Types.getPropertyType(pd);
		}
	}
	
	private Map<String, PropertyAccessor> accessors;
	private String[] readPropertyNames;
	private String[] writePropertyNames;

	/**
	 * Constructor
	 * @param factory bean handler factory
	 * @param type bean type
	 */
	public JavaBeanHandler(Beans factory, Type type) {
		super(factory, type);

		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(Types.getRawType(type));
		}
		catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}

		accessors = new HashMap<String, PropertyAccessor>();
		for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
			accessors.put(pd.getName(), new PropertyAccessor(type, pd));
			try {
				if (Character.isUpperCase(pd.getName().charAt(0))) {
					String pn = pd.getName().substring(0, 1).toLowerCase()
							+ pd.getName().substring(1);
					PropertyDescriptor pd2 = new PropertyDescriptor(pn, pd.getReadMethod(), pd.getWriteMethod());
					accessors.put(pd.getName(), new PropertyAccessor(type, pd2));
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		List<String> readPropertyNames = new ArrayList<String>();
		List<String> writePropertyNames = new ArrayList<String>();
		for (Entry<String, PropertyAccessor> en : accessors.entrySet()) {
			String pn = en.getKey();
			PropertyAccessor pa = en.getValue();
			
			if (pa.getter != null) {
				readPropertyNames.add(pn);
			}
			
			if (pa.setter != null) {
				writePropertyNames.add(pn);
			}
		}
		this.readPropertyNames = readPropertyNames.toArray(new String[readPropertyNames.size()]);
		this.writePropertyNames = writePropertyNames.toArray(new String[writePropertyNames.size()]);
	}

	/**
	 * get read property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	public String[] getReadPropertyNames(T beanObject) {
		return readPropertyNames;
	}

	/**
	 * get write property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(T beanObject) {
		return writePropertyNames;
	}

	/**
	 * is the property readable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T beanObject, String propertyName) {
		PropertyAccessor pa = accessors.get(propertyName);
		return (pa != null && pa.getter != null);
	}

	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T beanObject, String propertyName) {
		PropertyAccessor pa = accessors.get(propertyName);
		return (pa != null && pa.setter != null);
	}
	
	/**
	 * get simple property type
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public Type getPropertyType(T beanObject, String propertyName) {
		return getPropertyAccessor(propertyName).type;
	}

	/**
	 * get simple property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @return value
	 */
	public Object getPropertyValue(T beanObject, String propertyName) {
		PropertyAccessor pa = getPropertyAccessor(propertyName);
		Method getter = getReadMethod(pa);
		try {
			return getter.invoke(beanObject);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * set simple property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @param value value
	 */
	public void setPropertyValue(T beanObject, String propertyName, Object value) {
		PropertyAccessor pa = getPropertyAccessor(propertyName);
		Method setter = getWriteMethod(pa);
		try {
			setter.invoke(beanObject, value);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * getPropertyAccessor
	 * 
	 * @param propertyName property name
	 * @return PropertyAccessor
	 */
	private PropertyAccessor getPropertyAccessor(String propertyName) {
		PropertyAccessor pa = accessors.get(propertyName);
		if (pa == null) {
			throw noSuchPropertyException(propertyName);
		}
		return pa;
	}
	
	/**
	 * getReadMethod
	 * @param propertyAccessor PropertyAccessor
	 * @return read method
	 * @throws RuntimeException if read method is null 
	 */
	private Method getReadMethod(PropertyAccessor propertyAccessor) {
		if (propertyAccessor.getter == null) {
			throw noGetterMethodException(propertyAccessor.name);
		}
		return propertyAccessor.getter;
	}
	
	/**
	 * getWriteMethod
	 * @param propertyAccessor PropertyAccessor
	 * @return write method
	 * @throws RuntimeException if write method is null 
	 */
	private Method getWriteMethod(PropertyAccessor propertyAccessor) {
		if (propertyAccessor.setter == null) {
			throw noSetterMethodException(propertyAccessor.name);
		}
		return propertyAccessor.setter;
	}
}
