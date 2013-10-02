package panda.bean.handlers;

import java.lang.reflect.Type;
import java.util.List;

import panda.bean.Beans;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public class ListBeanHandler<T extends List> extends AbstractArrayBeanHandler<T> {
	protected Type elementType;
	
	/**
	 * Constructor
	 * @param factory bean handler factory
	 * @param type bean type
	 */
	public ListBeanHandler(Beans factory, Type type) {
		super(factory, type);
		
		elementType = Types.getCollectionElementType(type);
	}
	
	@Override
	protected Type getElementType() {
		return elementType;
	}
	
	@Override
	protected int getSize(T list) {
		return list.size();
	}
	
	@Override
	protected Object getElement(T list, int index) {
		if (index < 0 || index >= getSize(list)) {
			return null;
		}
		return list.get(index);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected boolean setElement(T list, int index, Object value) {
		if (index < 0 || index >= getSize(list)) {
			return false;
		}

		list.set(index, value);
		return true;
	}
}
