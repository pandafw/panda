package panda.cast.castor;

import panda.cast.CastContext;
import panda.lang.Classes;

/**
 * @author yf.frank.wang@gmail.com
 */
public class ClassCastor extends AnySingleCastor<Class> {
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
