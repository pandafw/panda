package panda.cast.castor;

import java.lang.reflect.Type;

import panda.cast.CastContext;
import panda.cast.Castor;

/**
 * @author yf.frank.wang@gmail.com
 */
public class DirectCastor<S, T> extends Castor<S, T> {
	public DirectCastor(Type fromType, Type toType) {
		super(fromType, toType);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T castValue(Object value, CastContext context) {
		return (T)value;
	}

}
