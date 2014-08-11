package panda.bean.handlers;

import java.lang.reflect.Type;
import java.util.List;

import panda.bean.Beans;
import panda.lang.reflect.Types;

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
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public ListBeanHandler(Beans beans, Type type) {
		super(beans, type);
		
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
		return list.get(index);
	}
	
	@Override
	protected boolean isValidSetIndex(T array, int index) {
		return index >= 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean setElement(T list, int index, Object value) {
		// append null elements
		if (index >= list.size()) {
			for (int i = list.size(); i <= index; i++) {
				list.add(null);
			}
		}

		list.set(index, value);
		return true;
	}
}
