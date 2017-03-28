package panda.lang.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Represents a generic type {@code T}. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 *
 * <p>For example, to create a type literal for {@code List<String>}, you can
 * create an empty anonymous inner class:
 *
 * <p>
 * <code>TypeToken<List<String>> list = new TypeToken<List<String>>() {};</code>
 *
 * <p>This syntax cannot be used to create type literals that have wildcard
 * parameters, such as {@code Class<?>} or {@code List<? extends CharSequence>}.
 * 
 * @param <T> the type
 */
public class TypeToken<T> {
	final Class<? super T> rawType;
	final Type type;
	final int hashCode;

	/**
	 * Constructs a new type literal. Derives represented class from type parameter.
	 * <p>
	 * Clients create an empty anonymous subclass. Doing so embeds the type parameter in the
	 * anonymous class's type hierarchy so we can reconstitute it at runtime despite erasure.
	 */
	@SuppressWarnings("unchecked")
	protected TypeToken() {
		this.type = getSuperclassTypeParameter(getClass());
		this.rawType = (Class<? super T>)Types.getRawType(type);
		this.hashCode = type.hashCode();
	}

	/**
	 * Unsafe. Constructs a type literal manually.
	 */
	@SuppressWarnings("unchecked")
	TypeToken(Type type) {
		this.type = Types.canonicalize(type);
		this.rawType = (Class<? super T>)Types.getRawType(this.type);
		this.hashCode = this.type.hashCode();
	}

	/**
	 * Returns the type from super class's type parameter in {@link Types#canonicalize canonical
	 * form}.
	 */
	static Type getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType)superclass;
		return Types.canonicalize(parameterized.getActualTypeArguments()[0]);
	}

	/**
	 * @return the raw (non-generic) type for this type.
	 */
	public final Class<? super T> getRawType() {
		return rawType;
	}

	/**
	 * @return the underlying {@code Type} instance.
	 */
	public final Type getType() {
		return type;
	}

	@Override
	public final int hashCode() {
		return this.hashCode;
	}

	@Override
	public final boolean equals(Object o) {
		return o instanceof TypeToken<?> && Types.equals(type, ((TypeToken<?>)o).type);
	}

	@Override
	public final String toString() {
		return Types.toString(type);
	}

	/**
	 * Gets type literal for the given {@code Type} instance.
	 * 
	 * @param type the type
	 * @return the TypeToken
	 */
	public static TypeToken<?> get(Type type) {
		return new TypeToken<Object>(type);
	}

	/**
	 * Gets type literal for the given {@code Class} instance.
	 * 
	 * @param type the type
	 * @return the TypeToken
	 */
	public static <T> TypeToken<T> get(Class<T> type) {
		return new TypeToken<T>(type);
	}
}
