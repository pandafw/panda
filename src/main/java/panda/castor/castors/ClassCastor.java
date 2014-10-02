package panda.castor.castors;

import panda.castor.CastContext;
import panda.lang.Classes;

/**
 * @author yf.frank.wang@gmail.com
 */
public class ClassCastor extends AbstractObjectCastor<Class> {
	public ClassCastor() {
		super(Class.class);
	}

	@Override
	protected Class castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			try {
				return Classes.getClass(value.toString());
			}
			catch (ClassNotFoundException e) {
				return castError(value, context, e);
			}
		}
		return castError(value, context);
	}
}
