package panda.castor.castors;

import panda.castor.CastContext;
import panda.castor.Castor;
import panda.lang.Classes;

/**
 * @author yf.frank.wang@gmail.com
 */
public class ClassCastor extends Castor<Object, Class> {
	public ClassCastor() {
		super(Object.class, Class.class);
	}

	@Override
	protected Class castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			try {
				return Classes.getClass(value.toString());
			}
			catch (ClassNotFoundException e) {
				throw wrapError(e, context);
			}
		}
		throw castError(value, context);
	}
}
