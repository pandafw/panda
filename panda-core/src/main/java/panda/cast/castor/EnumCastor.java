package panda.cast.castor;

import panda.cast.CastContext;

/**
 */
public class EnumCastor extends AnySingleCastor<Enum> {
	public EnumCastor(Class<?> toType) {
		super(toType);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Enum castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			try {
				return Enum.valueOf((Class)getToType(), value.toString());
			}
			catch (IllegalArgumentException e) {
				return castError(value, context, e);
			}
		}
		return castError(value, context);
	}
}
