package panda.bean.handlers;

import java.lang.reflect.Type;

import panda.bean.Beans;
import panda.ioc.Ioc;
import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Strings;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public class IocBeanHandler<T extends Ioc> extends AbstractJavaBeanHandler<T> {
	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public IocBeanHandler(Beans beans, Type type) {
		super(beans, type);
	}
	
	/**
	 * get read property names
	 * @param ioc bean object (can be null)
	 * @return property names
	 */
	public String[] getReadPropertyNames(T ioc) {
		if (ioc == null) {
			return Strings.EMPTY_ARRAY;
		}
		
		return ioc.getNames();
	}

	/**
	 * is the property readable
	 * @param ioc bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T ioc, String propertyName) {
		if (ioc == null) {
			return false;
		}
		
		return ioc.has(propertyName);
	}

	/**
	 * is the property writable
	 * @param array bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T array, String propertyName) {
		return false;
	}

	/**
	 * get write property names
	 * @param map bean object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(T map) {
		return Arrays.EMPTY_STRING_ARRAY;
	}

	@Override
	public Type getPropertyType(String propertyName) {
		return null;
	}
	
	public Type getPropertyType(T ioc, String propertyName) {
		if (ioc == null) {
			return null;
		}
		
		Object val = getPropertyValue(ioc, propertyName);
		return val == null ? null : val.getClass();
	}
	
	public Object getPropertyValue(T ioc, String propertyName) {
		return ioc.get(Object.class, propertyName);
	}
	
	public boolean setPropertyValue(T map, String propertyName, Object propertyValue) {
		throw Exceptions.unsupported("Ioc does not support set()");
	}
}
