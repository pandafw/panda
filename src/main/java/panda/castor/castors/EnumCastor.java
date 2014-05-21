package panda.castor.castors;

import panda.castor.CastContext;
import panda.castor.Castor;

/**
 * @author yf.frank.wang@gmail.com
 */
public class EnumCastor extends Castor<Object, Enum> {
	public EnumCastor(Class<?> toType) {
		super(Object.class, toType);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Enum castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			try {
				return Enum.valueOf((Class)getToType(), value.toString());
			}
			catch (IllegalArgumentException e) {
				throw wrapError(e, context);
			}
		}
		throw castError(value, context);
	}
}
