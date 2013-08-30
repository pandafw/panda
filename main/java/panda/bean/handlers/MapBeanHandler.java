package panda.bean.handlers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.lang.Strings;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
@SuppressWarnings("unchecked")
public class MapBeanHandler<T extends Map> extends AbstractJavaBeanHandler<T> {
	private Type elementType;
	
	/**
	 * Constructor
	 * @param factory bean handler factory
	 * @param type bean type
	 */
	public MapBeanHandler(Beans factory, Type type) {
		super(factory, type);

		elementType = Types.getMapKeyAndValueTypes(type)[1];
	}
	
	/**
	 * get read property names
	 * @param map bean object (can be null)
	 * @return property names
	 */
	public String[] getReadPropertyNames(T map) {
		if (map == null) {
			return Strings.EMPTY_ARRAY;
		}

		List<String> ns = new ArrayList<String>();
		for (Object o : map.keySet()) {
			if (o != null) {
				ns.add(o.toString());
			}
		}
		return ns.toArray(new String[ns.size()]);
	}

	/**
	 * is the property readable
	 * @param map bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T map, String propertyName) {
		if (map == null) {
			return false;
		}
		
		return map.containsKey(propertyName);
	}

	/**
	 * is the property writable
	 * @param array bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T array, String propertyName) {
		return true;
	}

	/**
	 * get write property names
	 * @param map bean object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(T map) {
		return getReadPropertyNames(map);
	}

	@Override
	public Type getPropertyType(String propertyName) {
		return elementType;
	}
	
	public Type getPropertyType(T map, String propertyName) {
		if (map == null) {
			return elementType;
		}
		Object val = getPropertyValue(map, propertyName);
		return val == null ? elementType : val.getClass();
	}
	
	/**
	 * get bean type
	 * @param map bean object (can be null)
	 * @param beanName bean name
	 * @return bean type
	 */
	@Override
	public Type getBeanType(T map, String beanName) {
		if (map != null) {
			Object val = getBeanValue(map, beanName);
			if (val != null) {
				return val.getClass();
			}
			return super.getBeanType(map, beanName);
		}
		return elementType;
	}

	public Object getPropertyValue(T map, String propertyName) {
		return map.get(propertyName);
	}
	
	public void setPropertyValue(T map, String propertyName, Object propertyValue) {
		map.put(propertyName, propertyValue);
	}

	/**
	 * get property value 
	 * @param map bean object
	 * @param beanName bean name
	 * @return bean value
	 */
	@Override
	public Object getBeanValue(T map, String beanName) {
		if (map.containsKey(beanName)) {
			return map.get(beanName);
		}
		return super.getBeanValue(map, beanName);
	}

	/**
	 * set property value 
	 * @param map bean object
	 * @param beanName bean name
	 * @param value value
	 */
	@Override
	public void setBeanValue(T map, String beanName, Object value) {
		if (map.containsKey(beanName)) {
			map.put(beanName, value);
			return;
		}

		super.setBeanValue(map, beanName, value);
	}
}
