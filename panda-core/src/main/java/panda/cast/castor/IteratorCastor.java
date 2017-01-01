package panda.cast.castor;

import java.util.Iterator;

import panda.cast.AbstractCastor;
import panda.cast.CastContext;
import panda.lang.Iterators;

/**
 */
public class IteratorCastor extends AbstractCastor<Object, Iterator> {
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
