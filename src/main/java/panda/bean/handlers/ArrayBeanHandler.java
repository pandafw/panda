package panda.bean.handlers;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

import panda.bean.Beans;
import panda.lang.reflect.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public class ArrayBeanHandler<T> extends AbstractArrayBeanHandler<T> {
	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public ArrayBeanHandler(Beans beans, Type type) {
		super(beans, type);

		if (!Types.isArrayType(type)) {
			throw new IllegalArgumentException(type + " is not a array type");
		}
	}

	@Override
	protected int getSize(T array) {
		return Array.getLength(array);
	}
	
	@Override
	protected Object getElement(T array, int index) {
		return Array.get(array, index);
	}
	
	@Override
	protected boolean setElement(T array, int index, Object value) {
		Array.set(array, index, value);
		return true;
	}
}
