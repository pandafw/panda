package panda.cast.castor;

import panda.cast.CastContext;
import panda.lang.Classes;

/**
 */
public class ClassCastor extends AnySingleCastor<Class> {
	public ClassCastor() {
		super(Class.class);
	}

	@Override
	protected Class castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			String s = value.toString();
			if (s.length() < 1) {
				return defaultValue();
			}

			try {
				return Classes.getClass(s);
			}
			catch (ClassNotFoundException e) {
				return castError(value, context, e);
			}
		}
		return castError(value, context);
	}
}
