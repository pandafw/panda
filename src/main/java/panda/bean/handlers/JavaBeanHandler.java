package panda.bean.handlers;

import panda.bean.Beans;
import panda.bean.PropertyAccessor;
import panda.lang.Exceptions;
import panda.lang.Types;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author yf.frank.wang@gmail.com
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
			
			if (pa.getField() != null) {
				readPropertyNames.add(pn);
				writePropertyNames.add(pn);
				continue;
			}

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
		return (pa != null && (pa.getField() != null || pa.getGetter() != null));
	}

	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T beanObject, String propertyName) {
		PropertyAccessor pa = accessors.get(propertyName);
		return (pa != null && (pa.getField() != null || pa.getSetter() != null));
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
		
		try {
			if (pa.getField() != null) {
				return pa.getField().get(beanObject);
			}
			else if (pa.getGetter() != null) {
				return pa.getGetter().invoke(beanObject);
			}
			return null;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
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

		try {
			if (pa.getField() != null) {
				pa.getField().set(beanObject, value);
				return true;
			}
			
			if (pa.getSetter() != null) {
				pa.getSetter().invoke(beanObject, value);
				return true;
			}
			
			return false;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
