package panda.bean.handler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.Beans;
import panda.bean.PropertyAccessor;
import panda.lang.reflect.Types;

/**
 * 
 * @param <T> class type
 */
public class JavaBeanHandler<T> extends AbstractJavaBeanHandler<T> {

	private Map<String, PropertyAccessor> accessors;
	private String[] readPropertyNames;
	private String[] writePropertyNames;

	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public JavaBeanHandler(Beans beans, Type type) {
		super(beans, type);

		Class clazz = Types.getRawType(type);
		accessors = Beans.getPropertyAccessors(clazz);
		
		List<String> readPropertyNames = new ArrayList<String>();
		List<String> writePropertyNames = new ArrayList<String>();
		for (Entry<String, PropertyAccessor> en : accessors.entrySet()) {
			String pn = en.getKey();
			PropertyAccessor pa = en.getValue();
			
			if (pa.getGetter() != null) {
				readPropertyNames.add(pn);
			}
			
			if (pa.getSetter() != null) {
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
		return (pa != null && pa.getGetter() != null);
	}

	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T beanObject, String propertyName) {
		PropertyAccessor pa = accessors.get(propertyName);
		return (pa != null && pa.getSetter() != null);
	}
	
	/**
	 * get simple property type
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public Type getPropertyType(T beanObject, String propertyName) {
		PropertyAccessor pa = accessors.get(propertyName);
		return pa == null ? null : pa.getType();
	}

	/**
	 * get simple property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @return value
	 */
	public Object getPropertyValue(T beanObject, String propertyName) {
		PropertyAccessor pa = accessors.get(propertyName);
		if (pa == null) {
			return null;
		}
		
		return pa.getValue(beanObject);
	}

	/**
	 * set simple property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @param value value
	 */
	public boolean setPropertyValue(T beanObject, String propertyName, Object value) {
		PropertyAccessor pa = accessors.get(propertyName);
		if (pa == null) {
			return false;
		}

		return pa.setValue(beanObject, value);
	}
}
