package panda.cast.castor;

import panda.cast.CastContext;
import panda.cast.Castor;
import panda.lang.Iterators;

/**
 * @author yf.frank.wang@gmail.com
 */
public class IterableCastor extends Castor<Object, Iterable> {
	public IterableCastor() {
		super(Object.class, Iterable.class);
	}

	@Override
	protected Iterable castValue(Object value, CastContext context) {
		if (Iterators.isIterable(value)) {
			return Iterators.asIterable(value);
		}
		
		return castError(value, context);
	}
}
