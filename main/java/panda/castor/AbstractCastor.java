package panda.castor;

import java.lang.reflect.Type;
import java.util.Map;

import panda.lang.CycleDetectStrategy;
import panda.lang.Objects;
import panda.lang.Types;
import panda.lang.builder.ToStringBuilder;
import panda.lang.collection.KeyValue;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <S> the source class type
 * @param <T> the target class type
 */
public abstract class AbstractCastor<S, T> implements Castor<S, T> {
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
		T coll;
		try {
			coll = Types.newInstance(Types.getDefaultImplType(toType));
		}
		catch (InstantiationException e) {
			throw new CastException(e);
		}
		catch (IllegalAccessException e) {
			throw new CastException(e);
		}
		return coll;
	}

	protected boolean isObjectType(Type type) {
		return type == null || Object.class.equals(Types.getRawType(type));
	}

	/**
	 * {@inheritDoc}
	 */
	public T cast(S value) {
		return cast(value, new CastContext());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public T cast(S value, CastContext context) {
		try {
			if (value == null) {
				return defaultValue();
			}
	
			if (isAssignable(value)) {
				return (T)value;
			}
	
			return convertValue(value, context);
		}
		catch (Throwable e) {
			throw wrapError(context, e);
		}
	}
	
	protected T defaultValue() {
		return null;
	}
	
	protected boolean isAssignable(Object value) {
		return Types.isAssignable(value.getClass(), toType);
	}
	
	@SuppressWarnings("unchecked")
	protected T convertValue(S value, CastContext context) {
		// throw CastException
		return (T)value;
	}

	protected CastException wrapError(CastContext context, Throwable e) {
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
