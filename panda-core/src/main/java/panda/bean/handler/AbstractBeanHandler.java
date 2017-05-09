package panda.bean.handler;

import java.lang.reflect.Type;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.lang.Strings;
import panda.lang.reflect.Types;

/**
 * 
 * @param <T> class type
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBeanHandler<T> extends AbstractPropertyHandler<T> implements BeanHandler<T> {
	protected Beans beans;
	protected Type type;

	/**
	 * Constructor
	 * @param beans bean handler beans
	 * @param type class type
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
		if (Types.isImmutableType(type)) {
			return null;
		}
		return beans.getBeanHandler(type);
	}
	
	/**
	 * x[1].y -> x.1.y
	 * x(1).y -> x.1.y
	 * 
	 * @param beanName bean name
	 * @return normalized bean name
	 */
	protected String normalizeBeanName(String beanName) {
		return Strings.replaceChars(beanName, ":[()]", "...");
	}
	
	/**
	 * get bean type
	 * @param beanName bean name
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
		beanName = normalizeBeanName(beanName);
		
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return getPropertyType(beanObject, beanName);
		} 

		String itemName = beanName.substring(0, dot);
		String nestName = beanName.substring(dot + 1);

		Type itemType = getPropertyType(beanObject, itemName); 
		if (itemType == null) {
			return null;
		}

		Object itemValue = null;
		if (beanObject != null) {
			itemValue = getPropertyValue(beanObject, itemName);
		}
		
		BeanHandler bh = getBeanHandler(itemType);
		if (bh == null) {
			return null;
		}
		return bh.getBeanType(itemValue, nestName);
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
		beanName = normalizeBeanName(beanName);
		
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return canReadProperty(beanObject, beanName);
		} 

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
		if (bh == null) {
			return false;
		}
		return bh.canReadBean(itemValue, nestName);
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
		beanName = normalizeBeanName(beanName);
		
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return canWriteProperty(beanObject, beanName);
		}

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
		if (bh == null) {
			return false;
		}
		return bh.canWriteBean(itemValue, nestName);
	}

	/**
	 * get bean value 
	 * @param beanObject bean object
	 * @param beanName property name
	 * @return bean value
	 */
	public Object getBeanValue(T beanObject, String beanName) {
		beanName = normalizeBeanName(beanName);
		
		int dot = beanName.indexOf(".");
		if (dot < 0) {
			return getPropertyValue(beanObject, beanName);
		}

		String itemName = beanName.substring(0, dot);

		Object itemValue = getPropertyValue(beanObject, itemName);
		if (itemValue == null) {
			return null;
		} 

		String nestName = beanName.substring(dot + 1);
		BeanHandler bh = getBeanHandler(itemValue.getClass());
		if (bh == null) {
			return null;
		}
		return bh.getBeanValue(itemValue, nestName);
	}

	/**
	 * set bean value 
	 * @param beanObject bean object
	 * @param beanName property name
	 * @param value value
	 */
	public boolean setBeanValue(T beanObject, String beanName, Object value) {
		beanName = normalizeBeanName(beanName);
		
		int index = beanName.indexOf(".");
		if (index < 0) {
			return setPropertyValue(beanObject, beanName, value);
		} 

		String itemName = beanName.substring(0, index);
		Object itemValue = getPropertyValue(beanObject, itemName);

		Type itemType = getPropertyType(beanObject, itemName);
		if (itemType == null) {
			return false;
		}

		BeanHandler bh = getBeanHandler(itemType);
		if (bh == null) {
			return false;
		}

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
