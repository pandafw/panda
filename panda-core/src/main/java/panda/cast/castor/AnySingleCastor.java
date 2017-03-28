package panda.cast.castor;

import java.lang.reflect.Type;
import java.util.Iterator;

import panda.lang.Iterators;

/**
 * @param <T> target type
 */
public abstract class AnySingleCastor<T> extends AnyObjectCastor<T> {
	public AnySingleCastor(Type toType) {
		super(toType);
	}

	/**
	 * @param value value
	 * @return one value of the array[1]
	 */
	protected Object prepare(Object value) {
		if (value instanceof byte[] || value instanceof char[]) {
			return value;
		}

		if (Iterators.isIterable(value)) {
			Iterator it = Iterators.asIterator(value);
			if (it.hasNext()) {
				Object v = it.next();
				if (!it.hasNext()) {
					return v;
				}
			}
		}
		return value;
	}
}
