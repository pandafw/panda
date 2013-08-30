package panda.bean.handlers;

import java.lang.reflect.Type;

import panda.bean.Beans;
import panda.lang.Strings;
import panda.lang.Types;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class AbstractArrayBeanHandler<T> extends AbstractJavaBeanHandler<T> {
	/**
	 * Constructor
	 * @param factory bean handler factory
	 * @param type bean type
	 */
	public AbstractArrayBeanHandler(Beans factory, Type type) {
		super(factory, type);
	}
	
	/**
	 * get read property names
	 * @param array bean object (can be null)
	 * @return property names
	 */
	public String[] getReadPropertyNames(T array) {
		if (array == null) {
			return Strings.EMPTY_ARRAY;
		}
		
		String[] pns = new String[getSize(array)];
		
		for (int i = 0; i < pns.length; i++) {
			pns[i] = String.valueOf(i);
		}

		return pns;
	}

	/**
	 * get write property names
	 * @param array bean object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(T array) {
		return getReadPropertyNames(array);
	}

	/**
	 * is the property readable
	 * @param array bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T array, String propertyName) {
		if (array == null) {
			return false;
		}
		
		int index = toIndex(propertyName);
		return index < getSize(array);
	}

	/**
	 * is the property writable
	 * @param array bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T array, String propertyName) {
		return canReadProperty(array, propertyName);
	}
	
	protected int toIndex(String index) {
		try {
			int idx = Integer.parseInt(index);
			if (idx < 0) {
				throw new IllegalArgumentException("[" + index + "] is not a valid index number.");
			}
			return idx;
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("[" + index + "] is not a valid index number.");
		}
	}

	protected Type getElementType() {
		return Types.getArrayComponentType(type);
	}
	
	public Type getPropertyType(T array, String propertyName) {
		if (array == null) {
			return getElementType();
		}
		Object val = getPropertyValue(array, propertyName);
		return val == null ? getElementType() : val.getClass();
	}
	
	public Object getPropertyValue(T array, String propertyName) {
		int index = toIndex(propertyName);
		return getElement(array, index);
	}
	
	public void setPropertyValue(T array, String propertyName, Object propertyValue) {
		int index = toIndex(propertyName);
		setElement(array, index, propertyValue);
	}

	protected abstract int getSize(T array);
	
	protected abstract Object getElement(T array, int index);
	
	protected abstract void setElement(T array, int index, Object value);
}
