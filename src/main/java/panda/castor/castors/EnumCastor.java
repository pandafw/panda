package panda.castor.castors;

import panda.castor.CastContext;

/**
 * @author yf.frank.wang@gmail.com
 */
public class EnumCastor extends AbstractObjectCastor<Enum> {
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
				throw wrapError(e, context);
			}
		}
		throw castError(value, context);
	}
}
