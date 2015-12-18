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
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
@SuppressWarnings("unchecked")
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
	 * @param map bean object (can be null)
	 * @param propertyName property name
	 * @return true if has property
	 */
	public boolean hasProperty(T map, String propertyName) {
		return canReadProperty(map, propertyName);
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
}
