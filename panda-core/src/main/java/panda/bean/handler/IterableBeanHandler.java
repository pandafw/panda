package panda.bean.handler;

import java.lang.reflect.Type;
import java.util.Iterator;

import panda.bean.Beans;
import panda.lang.Strings;
import panda.lang.reflect.Types;

/**
 * 
 * @param <T> class type
 */
public class IterableBeanHandler<T extends Iterable> extends AbstractArrayBeanHandler<T> {
	protected Type elementType;
	
	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public IterableBeanHandler(Beans beans, Type type) {
		super(beans, type);
		
		elementType = Types.getCollectionElementType(type);
	}
	
	/**
	 * get write property names
	 * @param array bean object (can be null)
	 * @return property names
	 */
	@Override
	public String[] getWritePropertyNames(T array) {
		return Strings.EMPTY_ARRAY;
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

	@Override
	protected Type getElementType() {
		return elementType;
	}

	@Override
	protected int getSize(T list) {
		Iterator it = list.iterator();
		int n = 0;
		for ( ; it.hasNext(); it.next(), n++) {}
		return n;
	}
	
	@Override
	protected Object getElement(T list, int index) {
		Iterator it = list.iterator();
		int n = 0;
		for ( ; it.hasNext() && n < index; it.next(), n++) {}
		if (n != index || !it.hasNext()) {
			//this.noSuchPropertyException(String.valueOf(index));
			return null;
		}
		return it.next();
	}
	
	@Override
	protected boolean setElement(T list, int index, Object value) {
		return false;
	}
}
