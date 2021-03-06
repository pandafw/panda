package panda.bean.handler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.lang.Strings;
import panda.lang.reflect.Types;

/**
 * 
 * @param <T> class type
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapBeanHandler<T extends Map> extends AbstractJavaBeanHandler<T> {
	private Type elementType;
	
	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public MapBeanHandler(Beans beans, Type type) {
		super(beans, type);

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

		List<String> ns = new ArrayList<String>(map.size());
		for (Object o : map.keySet()) {
			if (o != null) {
				String k = o.toString();
				if (Strings.isNotEmpty(k)) {
					ns.add(k);
				}
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

	public Object getPropertyValue(T map, String propertyName) {
		return map.get(propertyName);
	}
	
	public boolean setPropertyValue(T map, String propertyName, Object propertyValue) {
		map.put(propertyName, propertyValue);
		return true;
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
		}
		Type t = super.getBeanType(map, beanName);
		return t == null ? elementType : t;
	}

	/**
	 * get bean value 
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
	public boolean setBeanValue(T map, String beanName, Object value) {
		if (map.containsKey(beanName)) {
			map.put(beanName, value);
			return true;
		}

		return super.setBeanValue(map, beanName, value);
	}
}
