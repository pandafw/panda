package panda.bean.handlers;

import java.lang.reflect.Type;

import panda.bean.BeanHandler;
import panda.bean.Beans;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBeanHandler<T> implements BeanHandler<T> {
	protected Beans beans;
	protected Type type;

	/**
	 * Constructor
	 * @param beans bean handler beans
	 */
	public AbstractBeanHandler(Beans beans, Type type) {
		this.beans = beans;
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * getBeanHandler
	 * @param type bean type
	 * @return BeanHandler
	 */
	protected BeanHandler getBeanHandler(Type type) {
		return beans.getBeanHandler(type);
	}
	
	/**
	 * get read property names
	 * @return property names
	 */
	public String[] getReadPropertyNames() {
		return getReadPropertyNames(null);
	}

	/**
	 * get write property names
	 * @return property names
	 */
	public String[] getWritePropertyNames() {
		return getWritePropertyNames(null);
	}

	/**
	 * get property type
	 * @param propertyName property name
	 * @return property type
	 */
	public Type getPropertyType(String propertyName) {
		return getPropertyType(null, propertyName);
	}

	/**
	 * get bean type
	 * @param beanName property name
	 * @return bean type
	 */
	public Type getBeanType(String beanName) {
		return getBeanType(null, beanName);
	}

	/**
	 * get bean type
	 * @param beanObject bean object (can be null)
	 * @param beanName property name
	 * @return property type
	 */
	public Type getBeanType(T beanObject, String beanName) {
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return getPropertyType(beanObject, beanName);
		} 
		else {
			String itemName = beanName.substring(0, dot);
			String nestName = beanName.substring(dot + 1);

			Object itemValue = null;
			if (beanObject != null) {
				itemValue = getPropertyValue(beanObject, itemName);
			}
			
			Type itemType = getPropertyType(beanObject, itemName); 
			if (itemType == null) {
				return null;
			}

			BeanHandler bh = getBeanHandler(itemType);
			return bh.getBeanType(itemValue, nestName);
		}
	}

	/**
	 * is the property readable
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(String propertyName) {
		return canReadProperty(null, propertyName);
	}

	/**
	 * is the property readable
	 * @param beanName bean name
	 * @return bean type
	 */
	public boolean canReadBean(String beanName) {
		return canReadBean(null, beanName);
	}

	/**
	 * is the property readable
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return bean type
	 */
	public boolean canReadBean(T beanObject, String beanName) {
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return canReadProperty(beanObject, beanName);
		} 
		else {
			String itemName = beanName.substring(0, dot);
			String nestName = beanName.substring(dot + 1);

			Object itemValue = null;
			if (beanObject != null) {
				itemValue = getPropertyValue(beanObject, itemName);
			}
			
			Type itemType = getPropertyType(beanObject, itemName);
			if (itemType == null) {
				return false;
			}

			BeanHandler bh = getBeanHandler(itemType);
			return bh.canReadBean(itemValue, nestName);
		}
	}
	
	/**
	 * is the property writable
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(String propertyName) {
		return canWriteProperty(null, propertyName);
	}
	
	/**
	 * is the bean writable
	 * @param beanName property name
	 * @return bean writable
	 */
	public boolean canWriteBean(String beanName) {
		return canWriteBean(null, beanName);
	}

	/**
	 * is the bean writable
	 * @param beanObject bean object (can be null)
	 * @param beanName property name
	 * @return bean writable
	 */
	public boolean canWriteBean(T beanObject, String beanName) {
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return canWriteProperty(beanObject, beanName);
		}
		else {
			String itemName = beanName.substring(0, dot);
			String nestName = beanName.substring(dot + 1);

			Object itemValue = null;
			if (beanObject != null) {
				itemValue = getPropertyValue(beanObject, itemName);
			}
			
			Type itemType = getPropertyType(beanObject, itemName);
			if (itemType == null) {
				return false;
			}

			BeanHandler bh = getBeanHandler(itemType);
			return bh.canWriteBean(itemValue, nestName);
		}
	}

	/**
	 * get bean value 
	 * @param beanObject bean object
	 * @param beanName property name
	 * @return bean value
	 */
	public Object getBeanValue(T beanObject, String beanName) {
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return getPropertyValue(beanObject, beanName);
		}
		else {
			String itemName = beanName.substring(0, dot);

			Object itemValue = getPropertyValue(beanObject, itemName);
			if (itemValue == null) {
				return null;
			} 

			String nestName = beanName.substring(dot + 1);
			BeanHandler bh = getBeanHandler(itemValue.getClass());
			return bh.getBeanValue(itemValue, nestName);
		}
	}

	/**
	 * set bean value 
	 * @param beanObject bean object
	 * @param beanName property name
	 * @param value value
	 */
	public boolean setBeanValue(T beanObject, String beanName, Object value) {
		int index = beanName.indexOf(".");
		if (index < 0) {
			return setPropertyValue(beanObject, beanName, value);
		} 
		else {
			String itemName = beanName.substring(0, index);
			Object itemValue = getPropertyValue(beanObject, itemName);

			Type itemType = getPropertyType(beanObject, itemName);
			if (itemType == null) {
				return false;
			}

			BeanHandler bh = getBeanHandler(itemType);
			if (itemValue == null) {
				itemValue = bh.createObject(); 
				if (!setPropertyValue(beanObject, itemName, itemValue)) {
					return false;
				}
			}
			
			String nestName = beanName.substring(index + 1);
			return bh.setBeanValue(itemValue, nestName, value);
		}
	}
}
