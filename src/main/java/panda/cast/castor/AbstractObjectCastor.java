package panda.cast.castor;

import java.lang.reflect.Type;
import java.util.Iterator;

import panda.cast.CastContext;
import panda.cast.Castor;
import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.reflect.Types;

/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractObjectCastor<T> extends Castor<Object, T> {
	public AbstractObjectCastor(Type toType) {
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

			Object one = getOne(value);
			if (one != null) {
				return cast(one, context);
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

			Object one = getOne(value);
			if (one != null) {
				return castTo(one, target, context);
			}
			
			return castValueTo(value, target, context);
		}
		catch (Throwable e) {
			throw wrapError(e, context);
		}
	}

	/**
	 * @param value value
	 * @return one value of the array[1]
	 */
	protected Object getOne(Object value) {
		if (Iterators.isIterable(value)) {
			Iterator it = Iterators.asIterator(value);
			if (it.hasNext()) {
				value = it.next();
				if (!it.hasNext()) {
					return value;
				}
			}
		}
		return null;
	}
}
