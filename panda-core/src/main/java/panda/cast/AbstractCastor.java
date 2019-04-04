package panda.cast;

import java.lang.reflect.Type;
import java.util.Map;

import panda.lang.CycleDetectStrategy;
import panda.lang.Exceptions;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.collection.KeyValue;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;


/**
 * Castor interface 
 *
 * @param <S> source type
 * @param <T> target type
 * 
 */
public abstract class AbstractCastor<S, T> implements Castor<S, T> {
	protected final static Log log = Logs.getLog(AbstractCastor.class);

	protected final Type fromType;
	protected final Type toType;
	
	public AbstractCastor(Type fromType, Type toType) {
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
		catch (Throwable e) {
			throw new CastException(e);
		}
	}

	protected boolean isObjectType(Type type) {
		return type == null || Object.class.equals(Types.getRawType(type));
	}

	/**
	 * cast value
	 * @param context context
	 * @param value value
	 * @return casted value
	 */
	@Override
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
	
	/**
	 * cast value to the provided target object,
	 * NOTE: if the target is not a mutable object, a new object will be returned.
	 * @param value value
	 * @param target target object
	 * @return casted value
	 */
	@Override
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
	@Override
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
		return Types.isAssignable(value.getClass(), toType, false);
	}
	
	/**
	 * cast value
	 * @param value source value (not null)
	 * @param context context
	 * @return casted value
	 */
	protected abstract T castValue(S value, CastContext context);

	/**
	 * cast value to target
	 * @param value source value (not null)
	 * @param target target value
	 * @param context context
	 * @return casted value
	 */
	protected T castValueTo(S value, T target, CastContext context) {
		throw Exceptions.unsupported("CastValueTo(" + (value == null ? "NULL" : value.getClass())
			+ ", " + (target == null ? "NULL" : target.getClass()) + ") is not supported");
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

	protected String castErrorMsg(Object value, CastContext context) {
		return context.toPath() + ": Failed to cast " 
				+ (value == null ? "null" : value.getClass()) 
				+ (value instanceof CharSequence ? ("[" + Strings.left((CharSequence)value, 20) + "]") : "")
				+ " -> "
				+ Types.getRawType(toType);
	}
	
	protected T castError(Object value, CastContext context, Throwable e) {
		if (context.isSkipCastError()) {
			context.addError(context.toName(), value);
			return defaultValue();
		}
		throw new CastException(castErrorMsg(value, context), e);
	}
	
	protected T castError(Object value, CastContext context) {
		if (context.isSkipCastError()) {
			context.addError(context.toName(), value);
			return defaultValue();
		}
		throw new CastException(castErrorMsg(value, context));
	}
	
	protected <TS, TT> Castor<TS, TT> getCastor(CastContext context, Type fromType, Type toType) {
		return context.getCastors().getCastor(fromType, toType);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		return Objects.toStringBuilder()
				.append("fromType", fromType)
				.append("toType", toType)
				.toString();
	}
	
}
