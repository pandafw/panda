package panda.cast.castor;

import java.util.Iterator;

import panda.cast.CastContext;
import panda.cast.Castor;
import panda.lang.Iterators;

/**
 * @author yf.frank.wang@gmail.com
 */
public class IteratorCastor extends Castor<Object, Iterator> {
	public IteratorCastor() {
		super(Object.class, Iterator.class);
	}

	@Override
	protected Iterator castValue(Object value, CastContext context) {
		if (Iterators.isIterable(value)) {
			return Iterators.asIterator(value);
		}
		
		return castError(value, context);
	}
}
