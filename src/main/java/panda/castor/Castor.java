package panda.castor;

import java.lang.reflect.Type;
import java.util.Map;

import panda.lang.CycleDetectStrategy;
import panda.lang.Objects;
import panda.lang.Types;
import panda.lang.builder.ToStringBuilder;
import panda.lang.collection.KeyValue;
import panda.log.Log;
import panda.log.Logs;


/**
 * Castor interface 
 *
 * @param <S> source type
 * @param <T> target type
 * 
 * @author yf.frank.wang@gmail.com
 */
public class Castor<S, T> {
	protected final static Log log = Logs.getLog(Castor.class);

	protected final Type fromType;
	protected final Type toType;
	
	public Castor(Type fromType, Type toType) {
		this.fromType = fromType;
		this.toType = toType;
	}

	/**
	 * @return the from type
	 */
	protected Type getFromType() {
		return fromType;
	}

	/**
	 * @return the to type
	 */
	protected Type getToType() {
		return toType;
	}

	protected T createTarget() {
		try {
			return Types.newInstance(Types.getDefaultImplType(toType));
		}
		catch (InstantiationException e) {
			throw new CastException(e);
		}
		catch (IllegalAccessException e) {
			throw new CastException(e);
		}
	}

	protected boolean isObjectType(Type type) {
		return type == null || Object.class.equals(Types.getRawType(type));
	}

	/**
	 * cast value
	 * @param value value
	 * @return casted value
	 */
	public T cast(S value) {
		return cast(value, new CastContext());
	}
	
	/**
	 * cast value
	 * @param context context
	 * @param value value
	 * @return casted value
	 */
	@SuppressWarnings("unchecked")
	public T cast(S value, CastContext context) {
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

			return castValue(value, context);
		}
		catch (Throwable e) {
			throw wrapError(e, context);
		}
	}
	
	protected CastException castError(Object value, CastContext context) {
		return new CastException(context.toPath() 
				+ ": Failed to cast " 
				+ (value == null ? "null" : value.getClass())
				+ " -> "
				+ Types.getRawType(toType));
	}
	
	/**
	 * cast value to the provided target object,
	 * NOTE: if the target is not a mutable object, a new object will be returned.
	 * @param value value
	 * @param target target object
	 * @return casted value
	 */
	public T castTo(S value, T target) {
		return castTo(value, target, null);
	}

	/**
	 * cast value to the provided target object,
	 * NOTE: if the target is not a mutable object, a new object will be returned.
	 * @param value value
	 * @param target target object
	 * @param context context
	 * @return casted value
	 */
	public T castTo(S value, T target, CastContext context) {
		if (log.isTraceEnabled()) {
			log.trace("castTo " + context.toPath() + ": " 
					+ (value == null ? "NULL" : value.getClass().toString())
					+ " -> " + Objects.identityToString(target));
		}
		try {
			if (value == null) {
				return defaultValue();
			}
			return castValueTo(value, target, context);
		}
		catch (Throwable e) {
			throw wrapError(e, context);
		}
	}

	protected T defaultValue() {
		return null;
	}
	
	protected boolean isAssignable(Object value) {
		return Types.isAssignable(value.getClass(), toType);
	}
	
	/**
	 * cast value
	 * @param value source value (not null)
	 * @param context context
	 * @return casted value
	 */
	@SuppressWarnings("unchecked")
	protected T castValue(S value, CastContext context) {
		// throw CastException
		return (T)value;
	}

	/**
	 * cast value to target
	 * @param value source value (not null)
	 * @param target target value
	 * @param context context
	 * @return casted value
	 */
	@SuppressWarnings("unchecked")
	protected T castValueTo(S value, T target, CastContext context) {
		if (isAssignable(value)) {
			return (T)value;
		}
		
		return castValue(value, context);
	}

	protected CastException wrapError(Throwable e, CastContext context) {
		if (e instanceof CastException) {
			return (CastException)e;
		}
		return new CastException("Cast Exception at " + context.toPath() + ": " + e.getMessage(), e);
	}
	
	protected CastException cycleError(CastContext context, String name, Object value) {
		throw new CastException("Cycle object detected: " + context.toPath(name) + " : " + Objects.identityToString(value));
	}
	
	@SuppressWarnings("unchecked")
	protected Object castChild(CastContext context, Castor conv, int index, Object value) {
		if (value == null) {
			return conv.cast(value, context);
		}
		
		String name = String.valueOf(index);
		if (context.isCycled(value)) {
			switch (context.getCycleDetectStrategy()) {
			case CycleDetectStrategy.CYCLE_DETECT_NOPROP:
			case CycleDetectStrategy.CYCLE_DETECT_LENIENT:
				return null;
			default:
				throw cycleError(context, name, value);
			}
		}

		context.push(name, value);
		try {
			return conv.cast(value, context);
		}
		finally {
			context.popup();
		}
	}

	@SuppressWarnings("unchecked")
	protected Map.Entry castChild(CastContext context, Castor keyCastor, Castor valCastor, String name, Object key, Object value) {
		if (key == null) {
			key = keyCastor.cast(key, context);
		}
		else {
			if (context.isCycled(key)) {
				switch (context.getCycleDetectStrategy()) {
				case CycleDetectStrategy.CYCLE_DETECT_NOPROP:
				case CycleDetectStrategy.CYCLE_DETECT_LENIENT:
					return null;
				default:
					throw cycleError(context, name, key);
				}
			}
	
			context.push(name, key);
			try {
				key = keyCastor.cast(key, context);
			}
			finally {
				context.popup();
			}
		}
		
		if (value == null) {
			value = valCastor.cast(value, context);
		}
		else {
			if (context.isCycled(key)) {
				switch (context.getCycleDetectStrategy()) {
				case CycleDetectStrategy.CYCLE_DETECT_NOPROP:
					return null;
				case CycleDetectStrategy.CYCLE_DETECT_LENIENT:
					value = null;
					break;
				default:
					throw cycleError(context, name, value);
				}
			}

			context.push(name, value);
			try {
				value = valCastor.cast(value, context);
			}
			finally {
				context.popup();
			}
		}

		return new KeyValue(key, value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("fromType", fromType).append("toType", toType).toString();
	}
	
}
