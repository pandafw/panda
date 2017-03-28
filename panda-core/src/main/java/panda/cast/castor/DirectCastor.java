package panda.cast.castor;

import panda.cast.CastContext;
import panda.cast.Castor;

/**
 *
 * @param <S> source type
 * @param <T> target type
 */
public class DirectCastor<S, T> implements Castor<S, T> {
	public static DirectCastor INSTANCE = new DirectCastor();

	public static Castor i() {
		return INSTANCE;
	}
	
	private DirectCastor() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public T cast(S value, CastContext context) {
		return (T)value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T castTo(S value, T target) {
		return (T)value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T castTo(S value, T target, CastContext context) {
		return (T)value;
	}

}
