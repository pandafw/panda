package panda.cast.castor;

import java.lang.reflect.Type;

import panda.cast.AbstractCastor;
import panda.cast.CastContext;
import panda.lang.Objects;
import panda.lang.reflect.Types;

/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class AnyObjectCastor<T> extends AbstractCastor<Object, T> {
	public AnyObjectCastor(Type toType) {
		super(Object.class, toType);
	}

	/**
	 * cast value
	 * @param context context
	 * @param value value
	 * @return casted value
	 */
	@SuppressWarnings("unchecked")
	public T cast(Object value, CastContext context) {
		if (log.isTraceEnabled()) {
			log.trace("cast " + context.toPath() + ": " 
					+ (value == null ? "NULL" : value.getClass().toString())
					+ " -> " + Types.getRawType(toType).toString());
		}
		
		try {
			if (value == null) {
				return defaultValue();
			}
	
			if (isAssignable(value)) {
				return (T)value;
			}

			value = prepare(value);
			if (isAssignable(value)) {
				return (T)value;
			}

			return castValue(value, context);
		}
		catch (Throwable e) {
			throw wrapError(e, context);
		}
	}
	

	/**
	 * cast value to the provided target object,
	 * NOTE: if the target is not a mutable object, a new object will be returned.
	 * @param value value
	 * @param target target object
	 * @param context context
	 * @return casted value
	 */
	public T castTo(Object value, T target, CastContext context) {
		if (log.isTraceEnabled()) {
			log.trace("castTo " + context.toPath() + ": " 
					+ (value == null ? "NULL" : value.getClass().toString())
					+ " -> " + Objects.identityToString(target));
		}
		
		try {
			if (value == null) {
				return defaultValue();
			}

			value = prepare(value);
			
			return castValueTo(value, target, context);
		}
		catch (Throwable e) {
			throw wrapError(e, context);
		}
	}

	/**
	 * @param value value
	 * @return prepared value
	 */
	protected Object prepare(Object value) {
		return value;
	}
}
