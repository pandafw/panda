package panda.ioc.bean;

import java.lang.reflect.Type;
import java.util.Set;

import panda.bean.Beans;
import panda.bean.handler.AbstractJavaBeanHandler;
import panda.ioc.IocConstants;
import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Strings;

/**
 * 
 * @param <T> class type
 */
public class IocProxyBeanHandler<T extends IocProxy> extends AbstractJavaBeanHandler<T> {
	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public IocProxyBeanHandler(Beans beans, Type type) {
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
		
		Set<String> ns = ioc.getNames();
		return ns.toArray(new String[ns.size()]);
	}

	/**
	 * is the property readable
	 * @param ioc bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T ioc, String propertyName) {
		if (IocConstants.IOC_SELF.equals(propertyName)
				|| IocConstants.IOC_CONTEXT.equals(propertyName)) {
			return true;
		}
		
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
	 * @param ioc bean object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(T ioc) {
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
		if (IocConstants.IOC_SELF.equals(propertyName)) {
			return ioc.getIoc();
		}
		if (IocConstants.IOC_CONTEXT.equals(propertyName)) {
			return ioc.getContext();
		}
		
		return ioc.get(Object.class, propertyName);
	}
	
	public boolean setPropertyValue(T ioc, String propertyName, Object propertyValue) {
		throw Exceptions.unsupported("Ioc does not support set()");
	}
	
	/**
	 * get bean type
	 * @param ioc bean object (can be null)
	 * @param beanName bean name
	 * @return bean type
	 */
	@Override
	public Type getBeanType(T ioc, String beanName) {
		if (ioc != null) {
			Object val = getBeanValue(ioc, beanName);
			if (val != null) {
				return val.getClass();
			}
		}
		return super.getBeanType(ioc, beanName);
	}

	/**
	 * get bean value 
	 * @param ioc bean object
	 * @param beanName bean name
	 * @return bean value
	 */
	@Override
	public Object getBeanValue(T ioc, String beanName) {
		if (canReadProperty(ioc, beanName)) {
			return getPropertyValue(ioc, beanName);
		}
		return super.getBeanValue(ioc, beanName);
	}

	/**
	 * set property value 
	 * @param ioc bean object
	 * @param beanName bean name
	 * @param value value
	 */
	@Override
	public boolean setBeanValue(T ioc, String beanName, Object value) {
		return false;
	}
}
