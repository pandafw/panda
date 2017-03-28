package panda.lang.reflect;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Exceptions;

/**
 * Static methods for working with types.
 * 
 */
public abstract class Types {
	public static final Type[] EMPTY_TYPE_ARRAY = new Type[] {};

	/**
	 * Return the castable class name of the given class, usually wrap
	 * the class name: "int" -> "Integer".
	 * @param type the class
	 * @return the castable class name
	 */
	public static String getCastableClassName(Type type) {
		if (type instanceof Class) {
			return Classes.getCastableClassName((Class)type);
		}
		
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType)type;

			// I'm not exactly sure why getRawType() returns Type instead of Class.
			// Neal isn't either but suspects some pathological case related
			// to nested classes exists.
			Type rawType = parameterizedType.getRawType();
			Type[] typeArguments = parameterizedType.getActualTypeArguments();
			StringBuilder sb = new StringBuilder(30 * (typeArguments.length + 1));
			sb.append(getCastableClassName(rawType));

			if (typeArguments.length == 0) {
				return sb.toString();
			}

			sb.append("<").append(getCastableClassName(typeArguments[0]));
			for (int i = 1; i < typeArguments.length; i++) {
				sb.append(", ").append(getCastableClassName(typeArguments[i]));
			}
			return sb.append(">").toString();
		}

		return Classes.getCanonicalClassName(type.toString());
	}

	public static Type getDefaultImplType(Type type) {
		if (isAbstractType(type)) {
			if (type instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType)type;
				Type rawType = getDefaultImplType(pt.getRawType());
				type = paramTypeOfOwner(pt.getOwnerType(), rawType, pt.getActualTypeArguments());
			}
			else {
				if (Types.isAssignable(type, List.class)) {
					type = ArrayList.class;
				}
				else if (Types.isAssignable(type, Map.class)) {
					type = LinkedHashMap.class;
				}
				else if (Types.isAssignable(type, Set.class)) {
					type = LinkedHashSet.class;
				}
			}
		}
		return type;
	}
	
	/**
	 * get declared field type
	 * @param owner owner type
	 * @param name field name
	 * @return field type
	 */
	public static Type getDeclaredFieldType(Type owner, String name) {
		return getDeclaredFieldType(owner, name, null);
	}
	
	/**
	 * get declared field type
	 * @param owner owner type
	 * @param name field name
	 * @param deft default type 
	 * @return field type
	 */
	public static Type getDeclaredFieldType(Type owner, String name, Type deft) {
		Type type = deft;
		try {
			Class beanClazz = Types.getRawType(owner);
			Type gtype = beanClazz.getDeclaredField(name).getGenericType();
			if (deft == null || Types.isAssignable(deft, gtype)) {
				type = gtype;
			}
		}
		catch (SecurityException e) {
		}
		catch (NoSuchFieldException e) {
		}
		return type;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Type type) throws InstantiationException, IllegalAccessException {
		return (T)getRawType(type).newInstance();
	}

	/**
	 * new instance by the type 
	 * @param type type
	 * @return object instance
	 */
	public static <T> T born(Type type) {
		try {
			return newInstance(type);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to create instance of " + type, e);
		}
	}
	
	/**
	 * 获取一个类的泛型参数数组，如果这个类没有泛型参数，返回 null
	 * @param cls the class
	 * @return the type array
	 */
	public static Type[] getDeclaredGenericTypeParams(Class<?> cls) {
		if (cls == null)
			return null;

		// 看看父类
		Type sc = cls.getGenericSuperclass();
		if (null != sc && sc instanceof ParameterizedType)
			return ((ParameterizedType)sc).getActualTypeArguments();

		// 看看接口
		Type[] interfaces = cls.getGenericInterfaces();
		for (Type inf : interfaces) {
			if (inf instanceof ParameterizedType) {
				return ((ParameterizedType)inf).getActualTypeArguments();
			}
		}
		return getDeclaredGenericTypeParams(cls.getSuperclass());
	}
	
	/**
	 * 获取一个类的某个一个泛型参数
	 * 
	 * @param klass 类
	 * @param index 参数下标 （从 0 开始）
	 * @return 泛型参数类型
	 */
//	@SuppressWarnings("unchecked")
//	public static <T> Class<T> getDeclaredGenericTypeParam(Class<?> klass, int index) {
//	}
	
	public static Type getDeclaredGenericTypeParam(Class<?> klass, int index) {
		Type[] types = getDeclaredGenericTypeParams(klass);
		if (index < 0 || index >= types.length) {
			throw Exceptions.makeThrow("Class type param out of range %d/%d", index, types.length);
		}

		return types[index];
	}

	/**
	 * <pre>
	 * class Test {
	 * 	List&lt;String&gt; list;
	 * 
	 * 	void test() {
	 * 		// return &quot;java.lang.String&quot;;
	 * 		Types.getGenericFieldParameterTypes(Test.class, &quot;list&quot;);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param owner owner type
	 * @param fieldName field name
	 * @return element types
	 * @throws SecurityException if an security error occurs
	 * @throws NoSuchFieldException if a field with the specified name is not found.
	 */
	public static Type[] getGenericFieldParameterTypes(Type owner, String fieldName)
			throws SecurityException, NoSuchFieldException {
		
		Class<?> clazz = getRawType(owner);
		Field field = clazz.getDeclaredField(fieldName);
		Class type = field.getType();
		if (type.isArray()) {
			return new Type[] { type.getComponentType() };
		}
		
		Type ptype = field.getGenericType();
		if (ptype instanceof ParameterizedType) {
			return ((ParameterizedType)ptype).getActualTypeArguments();
		}
		return EMPTY_TYPE_ARRAY;
	}

	/**
	 * check the type is a interface or abstract
	 * @param type the type to be checked
	 * @return <code>true</code> if <code>type</code> is a interface or abstract class
	 */
	public static boolean isAbstractType(Type type) {
		Class clazz = getRawType(type);
		return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers());
	}
	
	/**
	 * Learn whether the specified type denotes an array type.
	 * 
	 * @param type the type to be checked
	 * @return <code>true</code> if <code>type</code> is an array class or a
	 *         {@link GenericArrayType}.
	 */
	public static boolean isArrayType(Type type) {
		return type instanceof GenericArrayType || type instanceof Class<?> && ((Class<?>)type).isArray();
	}

	/**
	 * The immutable type is:
	 * enum, primitive type, primitive wrapper, all class inherit from
	 * CharSequence, Date, Calendar, Number 
	 *  
	 * @param type the type to be checked
	 * @return true if the class is a immutable type
	 */
	public static boolean isImmutableType(Type type) {
		Class clazz = getRawType(type);
		return Classes.isImmutable(clazz);
	}

	/**
	 * Determines if the specified {@code Class} object represents a
	 * primitive type.
	 *
	 * <p> There are nine predefined {@code Class} objects to represent
	 * the eight primitive types and void.  These are created by the Java
	 * Virtual Machine, and have the same names as the primitive types that
	 * they represent, namely {@code boolean}, {@code byte},
	 * {@code char}, {@code short}, {@code int},
	 * {@code long}, {@code float}, and {@code double}.
	 *
	 * <p> These objects may only be accessed via the following public static
	 * final variables, and are the only {@code Class} objects for which
	 * this method returns {@code true}.
	 * 
	 * @param type the type
	 * @return true if and only if this class represents a primitive type
	 */
	public static boolean isPrimitiveType(Type type) {
		Class clazz = getRawType(type);
		return Classes.isPrimitiveType(clazz);
	}
	
	/**
	 * Get the array component type of <code>type</code>.
	 * 
	 * @param type the type to be checked
	 * @return component type or null if type is not an array type
	 */
	public static Type getArrayComponentType(Type type) {
		if (type instanceof Class<?>) {
			Class<?> clazz = (Class<?>)type;
			return clazz.isArray() ? clazz.getComponentType() : null;
		}
		if (type instanceof GenericArrayType) {
			return ((GenericArrayType)type).getGenericComponentType();
		}
		return null;
	}

	/**
	 * <p>
	 * Checks if the subject type may be implicitly cast to the target type following the Java
	 * generics rules. If both types are {@link Class} objects, the method returns the result of
	 * {@link Classes#isAssignable(Class, Class)}.
	 * </p>
	 * 
	 * @param type the subject type to be assigned to the target type
	 * @param toType the target type
	 * @return <code>true</code> if <code>type</code> is assignable to <code>toType</code>.
	 */
	public static boolean isAssignable(Type type, Type toType) {
		return isAssignable(type, toType, null, true);
	}

	public static boolean isAssignable(Type type, Type toType, boolean autoBoxing) {
		return isAssignable(type, toType, null, autoBoxing);
	}

	/**
	 * <p>
	 * Checks if the subject type may be implicitly cast to the target type following the Java
	 * generics rules.
	 * </p>
	 * 
	 * @param type the subject type to be assigned to the target type
	 * @param toType the target type
	 * @param typeVarAssigns optional map of type variable assignments
	 * @return <code>true</code> if <code>type</code> is assignable to <code>toType</code>.
	 */
	private static boolean isAssignable(Type type, Type toType,
			Map<TypeVariable<?>, Type> typeVarAssigns, boolean autoBoxing) {
		if (toType == null || toType instanceof Class<?>) {
			return isAssignable(type, (Class<?>)toType, autoBoxing);
		}

		if (toType instanceof ParameterizedType) {
			return isAssignable(type, (ParameterizedType)toType, typeVarAssigns, autoBoxing);
		}

		if (toType instanceof GenericArrayType) {
			return isAssignable(type, (GenericArrayType)toType, typeVarAssigns, autoBoxing);
		}

		if (toType instanceof WildcardType) {
			return isAssignable(type, (WildcardType)toType, typeVarAssigns, autoBoxing);
		}

		// *
		if (toType instanceof TypeVariable<?>) {
			return isAssignable(type, (TypeVariable<?>)toType, typeVarAssigns, autoBoxing);
		}
		// */

		throw new IllegalStateException("found an unhandled type: " + toType);
	}

	/**
	 * <p>
	 * Checks if the subject type may be implicitly cast to the target class following the Java
	 * generics rules.
	 * </p>
	 * 
	 * @param type the subject type to be assigned to the target type
	 * @param toClass the target class
	 * @return true if <code>type</code> is assignable to <code>toClass</code>.
	 */
	private static boolean isAssignable(Type type, Class<?> toClass, boolean autoBoxing) {
		if (type == null) {
			// consistency with ClassUtils.isAssignable() behavior
			return toClass == null || !toClass.isPrimitive();
		}

		// only a null type can be assigned to null type which
		// would have cause the previous to return true
		if (toClass == null) {
			return false;
		}

		// all types are assignable to themselves
		if (toClass.equals(type)) {
			return true;
		}

		if (type instanceof Class<?>) {
			// just comparing two classes
			return Classes.isAssignable((Class<?>)type, toClass, autoBoxing);
		}

		if (type instanceof ParameterizedType) {
			// only have to compare the raw type to the class
			return isAssignable(getRawType((ParameterizedType)type), toClass, autoBoxing);
		}

		// *
		if (type instanceof TypeVariable<?>) {
			// if any of the bounds are assignable to the class, then the
			// type is assignable to the class.
			for (Type bound : ((TypeVariable<?>)type).getBounds()) {
				if (isAssignable(bound, toClass, autoBoxing)) {
					return true;
				}
			}

			return false;
		}

		// the only classes to which a generic array type can be assigned
		// are class Object and array classes
		if (type instanceof GenericArrayType) {
			return toClass.equals(Object.class)
					|| toClass.isArray()
					&& isAssignable(((GenericArrayType)type).getGenericComponentType(),
						toClass.getComponentType(), autoBoxing);
		}

		// wildcard types are not assignable to a class (though one would think
		// "? super Object" would be assignable to Object)
		if (type instanceof WildcardType) {
			return false;
		}

		throw new IllegalStateException("found an unhandled type: " + type);
	}

	/**
	 * <p>
	 * Checks if the subject type may be implicitly cast to the target parameterized type following
	 * the Java generics rules.
	 * </p>
	 * 
	 * @param type the subject type to be assigned to the target type
	 * @param toParameterizedType the target parameterized type
	 * @param typeVarAssigns a map with type variables
	 * @return true if <code>type</code> is assignable to <code>toType</code>.
	 */
	private static boolean isAssignable(Type type, ParameterizedType toParameterizedType,
			Map<TypeVariable<?>, Type> typeVarAssigns, boolean autoBoxing) {
		if (type == null) {
			return true;
		}

		// only a null type can be assigned to null type which
		// would have cause the previous to return true
		if (toParameterizedType == null) {
			return false;
		}

		// all types are assignable to themselves
		if (toParameterizedType.equals(type)) {
			return true;
		}

		// get the target type's raw type
		Class<?> toClass = getRawType(toParameterizedType);
		// get the subject type's type arguments including owner type arguments
		// and supertype arguments up to and including the target class.
		Map<TypeVariable<?>, Type> fromTypeVarAssigns = getTypeArguments(type, toClass, null);

		// null means the two types are not compatible
		if (fromTypeVarAssigns == null) {
			return false;
		}

		// compatible types, but there's no type arguments. this is equivalent
		// to comparing Map< ?, ? > to Map, and raw types are always assignable
		// to parameterized types.
		if (fromTypeVarAssigns.isEmpty()) {
			return true;
		}

		// get the target type's type arguments including owner type arguments
		Map<TypeVariable<?>, Type> toTypeVarAssigns = getTypeArguments(toParameterizedType,
			toClass, typeVarAssigns);

		// now to check each type argument
		for (Map.Entry<TypeVariable<?>, Type> entry : toTypeVarAssigns.entrySet()) {
			Type toTypeArg = entry.getValue();
			Type fromTypeArg = fromTypeVarAssigns.get(entry.getKey());

			// parameters must either be absent from the subject type, within
			// the bounds of the wildcard type, or be an exact match to the
			// parameters of the target type.
			if (fromTypeArg != null
					&& !toTypeArg.equals(fromTypeArg)
					&& !(toTypeArg instanceof WildcardType && isAssignable(fromTypeArg, toTypeArg,
						typeVarAssigns, autoBoxing))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>
	 * Checks if the subject type may be implicitly cast to the target generic array type following
	 * the Java generics rules.
	 * </p>
	 * 
	 * @param type the subject type to be assigned to the target type
	 * @param toGenericArrayType the target generic array type
	 * @param typeVarAssigns a map with type variables
	 * @return true if <code>type</code> is assignable to <code>toGenericArrayType</code>.
	 */
	private static boolean isAssignable(Type type, GenericArrayType toGenericArrayType,
			Map<TypeVariable<?>, Type> typeVarAssigns, boolean autoBoxing) {
		if (type == null) {
			return true;
		}

		// only a null type can be assigned to null type which
		// would have cause the previous to return true
		if (toGenericArrayType == null) {
			return false;
		}

		// all types are assignable to themselves
		if (toGenericArrayType.equals(type)) {
			return true;
		}

		Type toComponentType = toGenericArrayType.getGenericComponentType();

		if (type instanceof Class<?>) {
			Class<?> cls = (Class<?>)type;

			// compare the component types
			return cls.isArray()
					&& isAssignable(cls.getComponentType(), toComponentType, typeVarAssigns, autoBoxing);
		}

		if (type instanceof GenericArrayType) {
			// compare the component types
			return isAssignable(((GenericArrayType)type).getGenericComponentType(),
				toComponentType, typeVarAssigns, autoBoxing);
		}

		if (type instanceof WildcardType) {
			// so long as one of the upper bounds is assignable, it's good
			for (Type bound : getImplicitUpperBounds((WildcardType)type)) {
				if (isAssignable(bound, toGenericArrayType, autoBoxing)) {
					return true;
				}
			}

			return false;
		}

		if (type instanceof TypeVariable<?>) {
			// probably should remove the following logic and just return false.
			// type variables cannot specify arrays as bounds.
			for (Type bound : getImplicitBounds((TypeVariable<?>)type)) {
				if (isAssignable(bound, toGenericArrayType, autoBoxing)) {
					return true;
				}
			}

			return false;
		}

		if (type instanceof ParameterizedType) {
			// the raw type of a parameterized type is never an array or
			// generic array, otherwise the declaration would look like this:
			// Collection[]< ? extends String > collection;
			return false;
		}

		throw new IllegalStateException("found an unhandled type: " + type);
	}

	/**
	 * <p>
	 * Checks if the subject type may be implicitly cast to the target wildcard type following the
	 * Java generics rules.
	 * </p>
	 * 
	 * @param type the subject type to be assigned to the target type
	 * @param toWildcardType the target wildcard type
	 * @param typeVarAssigns a map with type variables
	 * @return true if <code>type</code> is assignable to <code>toWildcardType</code>.
	 */
	private static boolean isAssignable(Type type, WildcardType toWildcardType,
			Map<TypeVariable<?>, Type> typeVarAssigns, boolean autoBoxing) {
		if (type == null) {
			return true;
		}

		// only a null type can be assigned to null type which
		// would have cause the previous to return true
		if (toWildcardType == null) {
			return false;
		}

		// all types are assignable to themselves
		if (toWildcardType.equals(type)) {
			return true;
		}

		Type[] toUpperBounds = getImplicitUpperBounds(toWildcardType);
		Type[] toLowerBounds = getImplicitLowerBounds(toWildcardType);

		if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType)type;
			Type[] upperBounds = getImplicitUpperBounds(wildcardType);
			Type[] lowerBounds = getImplicitLowerBounds(wildcardType);

			for (Type toBound : toUpperBounds) {
				// if there are assignments for unresolved type variables,
				// now's the time to substitute them.
				toBound = substituteTypeVariables(toBound, typeVarAssigns);

				// each upper bound of the subject type has to be assignable to
				// each
				// upper bound of the target type
				for (Type bound : upperBounds) {
					if (!isAssignable(bound, toBound, typeVarAssigns, autoBoxing)) {
						return false;
					}
				}
			}

			for (Type toBound : toLowerBounds) {
				// if there are assignments for unresolved type variables,
				// now's the time to substitute them.
				toBound = substituteTypeVariables(toBound, typeVarAssigns);

				// each lower bound of the target type has to be assignable to
				// each
				// lower bound of the subject type
				for (Type bound : lowerBounds) {
					if (!isAssignable(toBound, bound, typeVarAssigns, autoBoxing)) {
						return false;
					}
				}
			}

			return true;
		}

		for (Type toBound : toUpperBounds) {
			// if there are assignments for unresolved type variables,
			// now's the time to substitute them.
			if (!isAssignable(type, substituteTypeVariables(toBound, typeVarAssigns),
				typeVarAssigns, autoBoxing)) {
				return false;
			}
		}

		for (Type toBound : toLowerBounds) {
			// if there are assignments for unresolved type variables,
			// now's the time to substitute them.
			if (!isAssignable(substituteTypeVariables(toBound, typeVarAssigns), type,
				typeVarAssigns, autoBoxing)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>
	 * Checks if the subject type may be implicitly cast to the target type variable following the
	 * Java generics rules.
	 * </p>
	 * 
	 * @param type the subject type to be assigned to the target type
	 * @param toTypeVariable the target type variable
	 * @param typeVarAssigns a map with type variables
	 * @return true if <code>type</code> is assignable to <code>toTypeVariable</code>.
	 */
	private static boolean isAssignable(Type type, TypeVariable<?> toTypeVariable,
			Map<TypeVariable<?>, Type> typeVarAssigns, boolean autoBoxing) {
		if (type == null) {
			return true;
		}

		// only a null type can be assigned to null type which
		// would have cause the previous to return true
		if (toTypeVariable == null) {
			return false;
		}

		// all types are assignable to themselves
		if (toTypeVariable.equals(type)) {
			return true;
		}

		if (type instanceof TypeVariable<?>) {
			// a type variable is assignable to another type variable, if
			// and only if the former is the latter, extends the latter, or
			// is otherwise a descendant of the latter.
			Type[] bounds = getImplicitBounds((TypeVariable<?>)type);

			for (Type bound : bounds) {
				if (isAssignable(bound, toTypeVariable, typeVarAssigns, autoBoxing)) {
					return true;
				}
			}
		}

		if (type instanceof Class<?> || type instanceof ParameterizedType
				|| type instanceof GenericArrayType || type instanceof WildcardType) {
			return false;
		}

		throw new IllegalStateException("found an unhandled type: " + type);
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param type the type to be replaced
	 * @param typeVarAssigns the map with type variables
	 * @return the replaced type
	 * @throws IllegalArgumentException if the type cannot be substituted
	 */
	private static Type substituteTypeVariables(Type type, Map<TypeVariable<?>, Type> typeVarAssigns) {
		if (type instanceof TypeVariable<?> && typeVarAssigns != null) {
			Type replacementType = typeVarAssigns.get(type);

			if (replacementType == null) {
				throw new IllegalArgumentException("missing assignment type for type variable "
						+ type);
			}

			return replacementType;
		}

		return type;
	}

	/**
	 * <p>
	 * Retrieves all the type arguments for this parameterized type including owner hierarchy
	 * arguments such as <code>
	 * Outer<K,V>.Inner<T>.DeepInner<E></code> . The arguments are returned in a {@link Map}
	 * specifying the argument type for each {@link TypeVariable}.
	 * </p>
	 * 
	 * @param type specifies the subject parameterized type from which to harvest the parameters.
	 * @return a map of the type arguments to their respective type variables.
	 */
	public static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType type) {
		return getTypeArguments(type, getRawType(type), null);
	}

	/**
	 * <p>
	 * Gets the type arguments of a class/interface based on a subtype. For instance, this method
	 * will determine that both of the parameters for the interface {@link Map} are {@link Object}
	 * for the subtype {@link java.util.Properties Properties} even though the subtype does not
	 * directly implement the <code>Map</code> interface.
	 * <p>
	 * </p>
	 * This method returns <code>null</code> if <code>type</code> is not assignable to
	 * <code>toClass</code>. It returns an empty map if none of the classes or interfaces in its
	 * inheritance hierarchy specify any type arguments. </p>
	 * <p>
	 * A side-effect of this method is that it also retrieves the type arguments for the classes and
	 * interfaces that are part of the hierarchy between <code>type</code> and <code>toClass</code>.
	 * So with the above example, this method will also determine that the type arguments for
	 * {@link java.util.Hashtable Hashtable} are also both <code>Object</code>. In cases where the
	 * interface specified by <code>toClass</code> is (indirectly) implemented more than once (e.g.
	 * where <code>toClass</code> specifies the interface {@link java.lang.Iterable Iterable} and
	 * <code>type</code> specifies a parameterized type that implements both {@link java.util.Set
	 * Set} and {@link java.util.Collection Collection}), this method will look at the inheritance
	 * hierarchy of only one of the implementations/subclasses; the first interface encountered that
	 * isn't a subinterface to one of the others in the <code>type</code> to <code>toClass</code>
	 * hierarchy.
	 * </p>
	 * 
	 * @param type the type from which to determine the type parameters of <code>toClass</code>
	 * @param toClass the class whose type parameters are to be determined based on the subtype
	 *            <code>type</code>
	 * @return a map of the type assignments for the type variables in each type in the inheritance
	 *         hierarchy from <code>type</code> to <code>toClass</code> inclusive.
	 */
	public static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass) {
		return getTypeArguments(type, toClass, null);
	}

	/**
	 * <p>
	 * Return a map of the type arguments of <code>type</code> in the context of
	 * <code>toClass</code>.
	 * </p>
	 * 
	 * @param type the type in question
	 * @param toClass the class
	 * @param subtypeVarAssigns a map with type variables
	 * @return the map with type arguments
	 */
	private static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> toClass,
			Map<TypeVariable<?>, Type> subtypeVarAssigns) {
		if (type instanceof Class<?>) {
			return getTypeArguments((Class<?>)type, toClass, subtypeVarAssigns);
		}

		if (type instanceof ParameterizedType) {
			return getTypeArguments((ParameterizedType)type, toClass, subtypeVarAssigns);
		}

		if (type instanceof GenericArrayType) {
			return getTypeArguments(((GenericArrayType)type).getGenericComponentType(),
				toClass.isArray() ? toClass.getComponentType() : toClass, subtypeVarAssigns);
		}

		// since wildcard types are not assignable to classes, should this just
		// return null?
		if (type instanceof WildcardType) {
			for (Type bound : getImplicitUpperBounds((WildcardType)type)) {
				// find the first bound that is assignable to the target class
				if (isAssignable(bound, toClass)) {
					return getTypeArguments(bound, toClass, subtypeVarAssigns);
				}
			}

			return null;
		}

		// *
		if (type instanceof TypeVariable<?>) {
			for (Type bound : getImplicitBounds((TypeVariable<?>)type)) {
				// find the first bound that is assignable to the target class
				if (isAssignable(bound, toClass)) {
					return getTypeArguments(bound, toClass, subtypeVarAssigns);
				}
			}

			return null;
		}
		// */

		throw new IllegalStateException("found an unhandled type: " + type);
	}

	/**
	 * <p>
	 * Return a map of the type arguments of a parameterized type in the context of
	 * <code>toClass</code>.
	 * </p>
	 * 
	 * @param parameterizedType the parameterized type
	 * @param toClass the class
	 * @param subtypeVarAssigns a map with type variables
	 * @return the map with type arguments
	 */
	private static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType,
			Class<?> toClass, Map<TypeVariable<?>, Type> subtypeVarAssigns) {
		Class<?> cls = getRawType(parameterizedType);

		// make sure they're assignable
		if (!isAssignable(cls, toClass)) {
			return null;
		}

		Type ownerType = parameterizedType.getOwnerType();
		Map<TypeVariable<?>, Type> typeVarAssigns;

		if (ownerType instanceof ParameterizedType) {
			// get the owner type arguments first
			ParameterizedType parameterizedOwnerType = (ParameterizedType)ownerType;
			typeVarAssigns = getTypeArguments(parameterizedOwnerType,
				getRawType(parameterizedOwnerType), subtypeVarAssigns);
		}
		else {
			// no owner, prep the type variable assignments map
			typeVarAssigns = subtypeVarAssigns == null ? new HashMap<TypeVariable<?>, Type>() : new HashMap<TypeVariable<?>, Type>(
				subtypeVarAssigns);
		}

		// get the subject parameterized type's arguments
		Type[] typeArgs = parameterizedType.getActualTypeArguments();
		// and get the corresponding type variables from the raw class
		TypeVariable<?>[] typeParams = cls.getTypeParameters();

		// map the arguments to their respective type variables
		for (int i = 0; i < typeParams.length; i++) {
			Type typeArg = typeArgs[i];
			typeVarAssigns.put(typeParams[i],
				typeVarAssigns.containsKey(typeArg) ? typeVarAssigns.get(typeArg) : typeArg);
		}

		if (toClass.equals(cls)) {
			// target class has been reached. Done.
			return typeVarAssigns;
		}

		// walk the inheritance hierarchy until the target class is reached
		return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
	}

	/**
	 * <p>
	 * Return a map of the type arguments of a class in the context of <code>toClass</code>.
	 * </p>
	 * 
	 * @param cls the class in question
	 * @param toClass the context class
	 * @param subtypeVarAssigns a map with type variables
	 * @return the map with type arguments
	 */
	private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, Class<?> toClass,
			Map<TypeVariable<?>, Type> subtypeVarAssigns) {
		// make sure they're assignable
		if (!isAssignable(cls, toClass)) {
			return null;
		}

		// can't work with primitives
		if (cls.isPrimitive()) {
			// both classes are primitives?
			if (toClass.isPrimitive()) {
				// dealing with widening here. No type arguments to be
				// harvested with these two types.
				return new HashMap<TypeVariable<?>, Type>();
			}

			// work with wrapper the wrapper class instead of the primitive
			cls = Classes.primitiveToWrapper(cls);
		}

		// create a copy of the incoming map, or an empty one if it's null
		HashMap<TypeVariable<?>, Type> typeVarAssigns = subtypeVarAssigns == null ? new HashMap<TypeVariable<?>, Type>() : new HashMap<TypeVariable<?>, Type>(
			subtypeVarAssigns);

		// no arguments for the parameters, or target class has been reached
		if (cls.getTypeParameters().length > 0 || toClass.equals(cls)) {
			return typeVarAssigns;
		}

		// walk the inheritance hierarchy until the target class is reached
		return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
	}

	/**
	 * <p>
	 * Tries to determine the type arguments of a class/interface based on a super parameterized
	 * type's type arguments. This method is the inverse of {@link #getTypeArguments(Type, Class)}
	 * which gets a class/interface's type arguments based on a subtype. It is far more limited in
	 * determining the type arguments for the subject class's type variables in that it can only
	 * determine those parameters that map from the subject {@link Class} object to the supertype.
	 * </p>
	 * <p>
	 * Example: {@link java.util.TreeSet TreeSet} sets its parameter as the parameter for
	 * {@link java.util.NavigableSet NavigableSet}, which in turn sets the parameter of
	 * {@link java.util.SortedSet}, which in turn sets the parameter of {@link Set}, which in turn
	 * sets the parameter of {@link java.util.Collection}, which in turn sets the parameter of
	 * {@link java.lang.Iterable}. Since <code>TreeSet</code>'s parameter maps (indirectly) to
	 * <code>Iterable</code>'s parameter, it will be able to determine that based on the super type
	 * <code>Iterable<? extends
	 * Map<Integer,? extends Collection<?>>></code>, the parameter of <code>TreeSet</code> is
	 * <code>? extends Map<Integer,? extends
	 * Collection<?>></code>.
	 * </p>
	 * 
	 * @param cls the class whose type parameters are to be determined
	 * @param superType the super type from which <code>cls</code>'s type arguments are to be
	 *            determined
	 * @return a map of the type assignments that could be determined for the type variables in each
	 *         type in the inheritance hierarchy from <code>type</code> to <code>toClass</code>
	 *         inclusive.
	 */
	public static Map<TypeVariable<?>, Type> determineTypeArguments(Class<?> cls,
			ParameterizedType superType) {
		Class<?> superClass = getRawType(superType);

		// compatibility check
		if (!isAssignable(cls, superClass)) {
			return null;
		}

		if (cls.equals(superClass)) {
			return getTypeArguments(superType, superClass, null);
		}

		// get the next class in the inheritance hierarchy
		Type midType = getClosestParentType(cls, superClass);

		// can only be a class or a parameterized type
		if (midType instanceof Class<?>) {
			return determineTypeArguments((Class<?>)midType, superType);
		}

		ParameterizedType midParameterizedType = (ParameterizedType)midType;
		Class<?> midClass = getRawType(midParameterizedType);
		// get the type variables of the mid class that map to the type
		// arguments of the super class
		Map<TypeVariable<?>, Type> typeVarAssigns = determineTypeArguments(midClass, superType);
		// map the arguments of the mid type to the class type variables
		mapTypeVariablesToArguments(cls, midParameterizedType, typeVarAssigns);

		return typeVarAssigns;
	}

	/**
	 * <p>
	 * Performs a mapping of type variables.
	 * </p>
	 * 
	 * @param <T> the generic type of the class in question
	 * @param cls the class in question
	 * @param parameterizedType the parameterized type
	 * @param typeVarAssigns the map to be filled
	 */
	private static <T> void mapTypeVariablesToArguments(Class<T> cls,
			ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> typeVarAssigns) {
		// capture the type variables from the owner type that have assignments
		Type ownerType = parameterizedType.getOwnerType();

		if (ownerType instanceof ParameterizedType) {
			// recursion to make sure the owner's owner type gets processed
			mapTypeVariablesToArguments(cls, (ParameterizedType)ownerType, typeVarAssigns);
		}

		// parameterizedType is a generic interface/class (or it's in the owner
		// hierarchy of said interface/class) implemented/extended by the class
		// cls. Find out which type variables of cls are type arguments of
		// parameterizedType:
		Type[] typeArgs = parameterizedType.getActualTypeArguments();

		// of the cls's type variables that are arguments of parameterizedType,
		// find out which ones can be determined from the super type's arguments
		TypeVariable<?>[] typeVars = getRawType(parameterizedType).getTypeParameters();

		// use List view of type parameters of cls so the contains() method can be used:
		List<TypeVariable<Class<T>>> typeVarList = Arrays.asList(cls.getTypeParameters());

		for (int i = 0; i < typeArgs.length; i++) {
			TypeVariable<?> typeVar = typeVars[i];
			Type typeArg = typeArgs[i];

			// argument of parameterizedType is a type variable of cls
			if (typeVarList.contains(typeArg)
			// type variable of parameterizedType has an assignment in
			// the super type.
					&& typeVarAssigns.containsKey(typeVar)) {
				// map the assignment to the cls's type variable
				typeVarAssigns.put((TypeVariable<?>)typeArg, typeVarAssigns.get(typeVar));
			}
		}
	}

	/**
	 * <p>
	 * Closest parent type? Closest to what? The closest parent type to the super class specified by
	 * <code>superClass</code>.
	 * </p>
	 * 
	 * @param cls the class in question
	 * @param superClass the super class
	 * @return the closes parent type
	 */
	private static Type getClosestParentType(Class<?> cls, Class<?> superClass) {
		// only look at the interfaces if the super class is also an interface
		if (superClass.isInterface()) {
			// get the generic interfaces of the subject class
			Type[] interfaceTypes = cls.getGenericInterfaces();
			// will hold the best generic interface match found
			Type genericInterface = null;

			// find the interface closest to the super class
			for (Type midType : interfaceTypes) {
				Class<?> midClass = null;

				if (midType instanceof ParameterizedType) {
					midClass = getRawType((ParameterizedType)midType);
				}
				else if (midType instanceof Class<?>) {
					midClass = (Class<?>)midType;
				}
				else {
					throw new IllegalStateException("Unexpected generic"
							+ " interface type found: " + midType);
				}

				// check if this interface is further up the inheritance chain
				// than the previously found match
				if (isAssignable(midClass, superClass)
						&& isAssignable(genericInterface, (Type)midClass)) {
					genericInterface = midType;
				}
			}

			// found a match?
			if (genericInterface != null) {
				return genericInterface;
			}
		}

		// none of the interfaces were descendants of the target class, so the
		// super class has to be one, instead
		return cls.getGenericSuperclass();
	}

	/**
	 * <p>
	 * Checks if the given value can be assigned to the target type following the Java generics
	 * rules.
	 * </p>
	 * 
	 * @param value the value to be checked
	 * @param type the target type
	 * @return true of <code>value</code> is an instance of <code>type</code>.
	 */
	public static boolean isInstance(Object value, Type type) {
		if (type == null) {
			return false;
		}

		return value == null ? !(type instanceof Class<?>) || !((Class<?>)type).isPrimitive() : isAssignable(
			value.getClass(), type, null, false);
	}

	/**
	 * <p>
	 * This method strips out the redundant upper bound types in type variable types and wildcard
	 * types (or it would with wildcard types if multiple upper bounds were allowed).
	 * </p>
	 * <p>
	 * Example: with the variable type declaration:
	 * 
	 * <pre>
	 * &lt;K extends java.util.Collection&lt;String&gt; &amp;
	 * java.util.List&lt;String&gt;&gt;
	 * </pre>
	 * 
	 * since <code>List</code> is a subinterface of <code>Collection</code>, this method will return
	 * the bounds as if the declaration had been:
	 * 
	 * <pre>
	 * &lt;K extends java.util.List&lt;String&gt;&gt;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param bounds an array of types representing the upper bounds of either
	 *            <code>WildcardType</code> or <code>TypeVariable</code>.
	 * @return an array containing the values from <code>bounds</code> minus the redundant types.
	 */
	public static Type[] normalizeUpperBounds(Type[] bounds) {
		// don't bother if there's only one (or none) type
		if (bounds.length < 2) {
			return bounds;
		}

		Set<Type> types = new HashSet<Type>(bounds.length);

		for (Type type1 : bounds) {
			boolean subtypeFound = false;

			for (Type type2 : bounds) {
				if (type1 != type2 && isAssignable(type2, type1, null, false)) {
					subtypeFound = true;
					break;
				}
			}

			if (!subtypeFound) {
				types.add(type1);
			}
		}

		return types.toArray(new Type[types.size()]);
	}

	/**
	 * <p>
	 * Returns an array containing the sole type of {@link Object} if
	 * {@link TypeVariable#getBounds()} returns an empty array. Otherwise, it returns the result of
	 * <code>TypeVariable.getBounds()</code> passed into {@link #normalizeUpperBounds}.
	 * </p>
	 * 
	 * @param typeVariable the subject type variable
	 * @return a non-empty array containing the bounds of the type variable.
	 */
	public static Type[] getImplicitBounds(TypeVariable<?> typeVariable) {
		Type[] bounds = typeVariable.getBounds();

		return bounds.length == 0 ? new Type[] { Object.class } : normalizeUpperBounds(bounds);
	}

	/**
	 * <p>
	 * Returns an array containing the sole value of {@link Object} if
	 * {@link WildcardType#getUpperBounds()} returns an empty array. Otherwise, it returns the
	 * result of <code>WildcardType.getUpperBounds()</code> passed into
	 * {@link #normalizeUpperBounds}.
	 * </p>
	 * 
	 * @param wildcardType the subject wildcard type
	 * @return a non-empty array containing the upper bounds of the wildcard type.
	 */
	public static Type[] getImplicitUpperBounds(WildcardType wildcardType) {
		Type[] bounds = wildcardType.getUpperBounds();

		return bounds.length == 0 ? new Type[] { Object.class } : normalizeUpperBounds(bounds);
	}

	/**
	 * <p>
	 * Returns an array containing a single value of <code>null</code> if
	 * {@link WildcardType#getLowerBounds()} returns an empty array. Otherwise, it returns the
	 * result of <code>WildcardType.getLowerBounds()</code>.
	 * </p>
	 * 
	 * @param wildcardType the subject wildcard type
	 * @return a non-empty array containing the lower bounds of the wildcard type.
	 */
	public static Type[] getImplicitLowerBounds(WildcardType wildcardType) {
		Type[] bounds = wildcardType.getLowerBounds();

		return bounds.length == 0 ? new Type[] { null } : bounds;
	}

	/**
	 * <p>
	 * Determines whether or not specified types satisfy the bounds of their mapped type variables.
	 * When a type parameter extends another (such as <code><T, S extends T></code>), uses another
	 * as a type parameter (such as <code><T, S extends Comparable<T></code>), or otherwise depends
	 * on another type variable to be specified, the dependencies must be included in
	 * <code>typeVarAssigns</code>.
	 * </p>
	 * 
	 * @param typeVarAssigns specifies the potential types to be assigned to the type variables.
	 * @return whether or not the types can be assigned to their respective type variables.
	 */
	public static boolean typesSatisfyVariables(Map<TypeVariable<?>, Type> typeVarAssigns) {
		// all types must be assignable to all the bounds of the their mapped
		// type variable.
		for (Map.Entry<TypeVariable<?>, Type> entry : typeVarAssigns.entrySet()) {
			TypeVariable<?> typeVar = entry.getKey();
			Type type = entry.getValue();

			for (Type bound : getImplicitBounds(typeVar)) {
				if (!isAssignable(type, substituteTypeVariables(bound, typeVarAssigns),
					typeVarAssigns, false)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns a new parameterized type, applying {@code typeArguments} to {@code rawType}.
	 * 
	 * @param rawType the raw type
	 * @param typeArguments the type arguments
	 * @return a {@link java.io.Serializable serializable} parameterized type.
	 */
	public static ParameterizedType paramTypeOf(Type rawType, Type... typeArguments) {
		return new ParameterizedTypeImpl(null, rawType, typeArguments);
	}

	/**
	 * Returns a new parameterized type, applying {@code typeArguments} to {@code rawType} and
	 * enclosed by {@code ownerType}.
	 * 
	 * @param ownerType the owner type
	 * @param rawType the raw type
	 * @param typeArguments the type arguments
	 * @return a {@link java.io.Serializable serializable} parameterized type.
	 */
	public static ParameterizedType paramTypeOfOwner(Type ownerType, Type rawType,
			Type... typeArguments) {
		return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
	}

	/**
	 * Returns an array type whose elements are all instances of {@code componentType}.
	 * 
	 * @param componentType the component type
	 * @return a {@link java.io.Serializable serializable} generic array type.
	 */
	public static GenericArrayType arrayTypeOf(Type componentType) {
		return new GenericArrayTypeImpl(componentType);
	}

	/**
	 * Returns a type that represents an unknown type that extends {@code bound}. For example, if
	 * {@code bound} is {@code CharSequence.class}, this returns {@code ? extends CharSequence}. If
	 * {@code bound} is {@code Object.class}, this returns {@code ?}, which is shorthand for
	 * {@code ? extends Object}.
	 * 
	 * @param bound the bound type
	 * @return the WildCardType
	 */
	public static WildcardType subTypeOf(Type bound) {
		return new WildcardTypeImpl(new Type[] { bound }, EMPTY_TYPE_ARRAY);
	}

	/**
	 * Returns a type that represents an unknown supertype of {@code bound}. For example, if
	 * {@code bound} is {@code String.class}, this returns {@code ?
	 * super String}.
	 * 
	 * @param bound the bound type
	 * @return the WildCardType
	 */
	public static WildcardType superTypeOf(Type bound) {
		return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { bound });
	}

	/**
	 * Returns a type that is functionally equal but not necessarily equal according to
	 * {@link Object#equals(Object) Object.equals()}. The returned type is
	 * {@link java.io.Serializable}.
	 * 
	 * @param type the type
	 * @return the canonicalized type
	 */
	public static Type canonicalize(Type type) {
		if (type instanceof Class) {
			Class<?> c = (Class<?>)type;
			return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;
		}
		
		if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType)type;
			return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(),
				p.getActualTypeArguments());
		}
		
		if (type instanceof GenericArrayType) {
			GenericArrayType g = (GenericArrayType)type;
			return new GenericArrayTypeImpl(g.getGenericComponentType());
		}
		
		if (type instanceof WildcardType) {
			WildcardType w = (WildcardType)type;
			return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
		}

		// type is either serializable as-is or unsupported
		return type;
	}

	public static Class<?> getRawType(Type type) {
		if (type instanceof Class<?>) {
			// type is a normal class.
			return (Class<?>)type;
		}
		else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType)type;

			// I'm not exactly sure why getRawType() returns Type instead of Class.
			// Neal isn't either but suspects some pathological case related
			// to nested classes exists.
			Type rawType = parameterizedType.getRawType();
			Asserts.isTrue(rawType instanceof Class);
			return (Class<?>)rawType;

		}
		else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType)type).getGenericComponentType();
			return Array.newInstance(getRawType(componentType), 0).getClass();
		}
		else if (type instanceof TypeVariable) {
			// we could use the variable's bounds, but that won't work if there are multiple.
			// having a raw type that's more general than necessary is okay
			return Object.class;
		}
		else if (type instanceof WildcardType) {
			return getRawType(((WildcardType)type).getUpperBounds()[0]);
		}
		else {
			String className = type == null ? "null" : type.getClass().getName();
			throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
					+ "GenericArrayType, but <" + type + "> is of type " + className);
		}
	}

	static boolean equal(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}

	/**
	 * @param a the type
	 * @param b the type
	 * @return true if {@code a} and {@code b} are equal.
	 */
	public static boolean equals(Type a, Type b) {
		if (a == b) {
			// also handles (a == null && b == null)
			return true;

		}
		else if (a instanceof Class) {
			// Class already specifies equals().
			return a.equals(b);

		}
		else if (a instanceof ParameterizedType) {
			if (!(b instanceof ParameterizedType)) {
				return false;
			}

			// TODO: save a .clone() call
			ParameterizedType pa = (ParameterizedType)a;
			ParameterizedType pb = (ParameterizedType)b;
			return equal(pa.getOwnerType(), pb.getOwnerType())
					&& pa.getRawType().equals(pb.getRawType())
					&& Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());

		}
		else if (a instanceof GenericArrayType) {
			if (!(b instanceof GenericArrayType)) {
				return false;
			}

			GenericArrayType ga = (GenericArrayType)a;
			GenericArrayType gb = (GenericArrayType)b;
			return equals(ga.getGenericComponentType(), gb.getGenericComponentType());

		}
		else if (a instanceof WildcardType) {
			if (!(b instanceof WildcardType)) {
				return false;
			}

			WildcardType wa = (WildcardType)a;
			WildcardType wb = (WildcardType)b;
			return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds())
					&& Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());

		}
		else if (a instanceof TypeVariable) {
			if (!(b instanceof TypeVariable)) {
				return false;
			}
			TypeVariable<?> va = (TypeVariable<?>)a;
			TypeVariable<?> vb = (TypeVariable<?>)b;
			return va.getGenericDeclaration() == vb.getGenericDeclaration()
					&& va.getName().equals(vb.getName());

		}
		else {
			// This isn't a type we support. Could be a generic array type, wildcard type, etc.
			return false;
		}
	}

	private static int hashCodeOrZero(Object o) {
		return o != null ? o.hashCode() : 0;
	}

	public static String toString(Type type) {
		return type instanceof Class ? ((Class<?>)type).getName() : type.toString();
	}

	/**
	 * Returns the generic supertype for {@code supertype}. For example, given a class
	 * {@code IntegerSet}, the result for when supertype is {@code Set.class} is
	 * {@code Set<Integer>} and the result when the supertype is {@code Collection.class} is
	 * {@code Collection<Integer>}.
	 */
	static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
		if (toResolve == rawType) {
			return context;
		}

		// we skip searching through interfaces if unknown is an interface
		if (toResolve.isInterface()) {
			Class<?>[] interfaces = rawType.getInterfaces();
			for (int i = 0, length = interfaces.length; i < length; i++) {
				if (interfaces[i] == toResolve) {
					return rawType.getGenericInterfaces()[i];
				}
				else if (toResolve.isAssignableFrom(interfaces[i])) {
					return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i],
						toResolve);
				}
			}
		}

		// check our supertypes
		if (!rawType.isInterface()) {
			while (rawType != Object.class) {
				Class<?> rawSupertype = rawType.getSuperclass();
				if (rawSupertype == toResolve) {
					return rawType.getGenericSuperclass();
				}
				else if (toResolve.isAssignableFrom(rawSupertype)) {
					return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype,
						toResolve);
				}
				rawType = rawSupertype;
			}
		}

		// we can't resolve this further
		return toResolve;
	}

	/**
	 * Returns the generic form of {@code supertype}. For example, if this is
	 * {@code ArrayList<String>}, this returns {@code Iterable<String>} given the input
	 * {@code Iterable.class}.
	 * 
	 * @param supertype a superclass of, or interface implemented by, this.
	 */
	static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
		Asserts.isTrue(supertype.isAssignableFrom(contextRawType));
		return resolve(context, contextRawType,
			Types.getGenericSupertype(context, contextRawType, supertype));
	}

	private static final Type[] OBJECTS = new Type[] { Object.class };

	/**
	 * Returns the element type of this collection type.
	 * 
	 * @param context the type
	 * @param contextRawType type raw type
	 * @return the collection element type
	 * @throws IllegalArgumentException if this type is not a collection.
	 */
	public static Type getCollectionElementType(Type context, Class<?> contextRawType) {
		Type collectionType = getSupertype(context, contextRawType, Collection.class);

		if (collectionType instanceof WildcardType) {
			collectionType = ((WildcardType)collectionType).getUpperBounds()[0];
		}
		if (collectionType instanceof ParameterizedType) {
			Type eType = ((ParameterizedType)collectionType).getActualTypeArguments()[0];
			if (eType instanceof TypeVariable && Arrays.equals(OBJECTS, ((TypeVariable)eType).getBounds())) {
				eType = Object.class;
			}
			return eType;
		}
		return Object.class;
	}

	public static Type getCollectionElementType(Type type) {
		TypeToken typeToken = TypeToken.get(type);
		return getCollectionElementType(typeToken);
	}
	
	public static Type getCollectionElementType(TypeToken typeToken) {
		Type type = typeToken.getType();

		Class rawType = typeToken.getRawType();
		if (!Collection.class.isAssignableFrom(rawType)) {
			return null;
		}

		return getCollectionElementType(type, rawType);
	}

	public static Type getArrayElementType(Type type) {
		if (Types.isArrayType(type)) {
			return Types.getArrayComponentType(type);
		}

		return Types.getCollectionElementType(type);
	}
	
	/**
	 * Returns a two element array containing this map's key and value types in positions 0 and 1
	 * respectively.
	 * 
	 * @param context the type
	 * @param contextRawType the raw type
	 * @return the key/value types
	 */
	public static Type[] getMapKeyAndValueTypes(Type context, Class<?> contextRawType) {
		/*
		 * Work around a problem with the declaration of java.util.Properties. That class should
		 * extend Hashtable<String, String>, but it's declared to extend Hashtable<Object, Object>.
		 */
		if (context == Properties.class) {
			return new Type[] { String.class, String.class }; // TODO: test subclasses of
																// Properties!
		}

		Type mapType = getSupertype(context, contextRawType, Map.class);
		// TODO: strip wildcards?
		if (mapType instanceof ParameterizedType) {
			ParameterizedType mapParameterizedType = (ParameterizedType)mapType;
			return mapParameterizedType.getActualTypeArguments();
		}
		return new Type[] { Object.class, Object.class };
	}

	public static Type[] getMapKeyAndValueTypes(TypeToken typeToken) {
		Type type = typeToken.getType();

		Class rawType = typeToken.getRawType();
		if (!Map.class.isAssignableFrom(rawType)) {
			return null;
		}

		Class<?> rawTypeOfSrc = getRawType(type);
		return getMapKeyAndValueTypes(type, rawTypeOfSrc);
	}

	public static Type[] getMapKeyAndValueTypes(Type type) {
		if (type instanceof ParameterizedType) {
			TypeToken typeToken = TypeToken.get(type);
			return getMapKeyAndValueTypes(typeToken);
		}
		return new Type[] { Object.class, Object.class };
	}

	public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
		// this implementation is made a little more complicated in an attempt to avoid
		// object-creation
		while (true) {
			if (toResolve instanceof TypeVariable) {
				TypeVariable<?> typeVariable = (TypeVariable<?>)toResolve;
				toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
				if (toResolve == typeVariable) {
					return toResolve;
				}

			}
			else if (toResolve instanceof Class && ((Class<?>)toResolve).isArray()) {
				Class<?> original = (Class<?>)toResolve;
				Type componentType = original.getComponentType();
				Type newComponentType = resolve(context, contextRawType, componentType);
				return componentType == newComponentType ? original : arrayTypeOf(newComponentType);

			}
			else if (toResolve instanceof GenericArrayType) {
				GenericArrayType original = (GenericArrayType)toResolve;
				Type componentType = original.getGenericComponentType();
				Type newComponentType = resolve(context, contextRawType, componentType);
				return componentType == newComponentType ? original : arrayTypeOf(newComponentType);

			}
			else if (toResolve instanceof ParameterizedType) {
				ParameterizedType original = (ParameterizedType)toResolve;
				Type ownerType = original.getOwnerType();
				Type newOwnerType = resolve(context, contextRawType, ownerType);
				boolean changed = newOwnerType != ownerType;

				Type[] args = original.getActualTypeArguments();
				for (int t = 0, length = args.length; t < length; t++) {
					Type resolvedTypeArgument = resolve(context, contextRawType, args[t]);
					if (resolvedTypeArgument != args[t]) {
						if (!changed) {
							args = args.clone();
							changed = true;
						}
						args[t] = resolvedTypeArgument;
					}
				}

				return changed ? paramTypeOfOwner(newOwnerType, original.getRawType(),
					args) : original;

			}
			else if (toResolve instanceof WildcardType) {
				WildcardType original = (WildcardType)toResolve;
				Type[] originalLowerBound = original.getLowerBounds();
				Type[] originalUpperBound = original.getUpperBounds();

				if (originalLowerBound.length == 1) {
					Type lowerBound = resolve(context, contextRawType, originalLowerBound[0]);
					if (lowerBound != originalLowerBound[0]) {
						return superTypeOf(lowerBound);
					}
				}
				else if (originalUpperBound.length == 1) {
					Type upperBound = resolve(context, contextRawType, originalUpperBound[0]);
					if (upperBound != originalUpperBound[0]) {
						return subTypeOf(upperBound);
					}
				}
				return original;

			}
			else {
				return toResolve;
			}
		}
	}

	static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
		Class<?> declaredByRaw = declaringClassOf(unknown);

		// we can't reduce this further
		if (declaredByRaw == null) {
			return unknown;
		}

		Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
		if (declaredBy instanceof ParameterizedType) {
			int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
			return ((ParameterizedType)declaredBy).getActualTypeArguments()[index];
		}

		return unknown;
	}

	private static int indexOf(Object[] array, Object toFind) {
		for (int i = 0; i < array.length; i++) {
			if (toFind.equals(array[i])) {
				return i;
			}
		}
		throw new NoSuchElementException();
	}

	/**
	 * Returns the declaring class of {@code typeVariable}, or {@code null} if it was not declared
	 * by a class.
	 */
	private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
		GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
		return genericDeclaration instanceof Class ? (Class<?>)genericDeclaration : null;
	}

	private static void checkNotPrimitive(Type type) {
		Asserts.isTrue(!(type instanceof Class<?>) || !((Class<?>)type).isPrimitive());
	}

	private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
		private final Type ownerType;
		private final Type rawType;
		private final Type[] typeArguments;

		public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
			// require an owner type if the raw type needs it
			if (rawType instanceof Class<?>) {
				Class<?> rawTypeAsClass = (Class<?>)rawType;
				Asserts.isTrue(ownerType != null || rawTypeAsClass.getEnclosingClass() == null);
				Asserts.isTrue(ownerType == null || rawTypeAsClass.getEnclosingClass() != null);
			}

			this.ownerType = ownerType == null ? null : canonicalize(ownerType);
			this.rawType = canonicalize(rawType);
			this.typeArguments = typeArguments.clone();
			for (int t = 0; t < this.typeArguments.length; t++) {
				Asserts.notNull(this.typeArguments[t]);
				checkNotPrimitive(this.typeArguments[t]);
				this.typeArguments[t] = canonicalize(this.typeArguments[t]);
			}
		}

		public Type[] getActualTypeArguments() {
			return typeArguments.clone();
		}

		public Type getRawType() {
			return rawType;
		}

		public Type getOwnerType() {
			return ownerType;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof ParameterizedType
					&& Types.equals(this, (ParameterizedType)other);
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(typeArguments) ^ rawType.hashCode() ^ hashCodeOrZero(ownerType);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(30 * (typeArguments.length + 1));
			sb.append(Types.toString(rawType));

			if (typeArguments.length == 0) {
				return sb.toString();
			}

			sb.append("<").append(Types.toString(typeArguments[0]));
			for (int i = 1; i < typeArguments.length; i++) {
				sb.append(", ").append(Types.toString(typeArguments[i]));
			}
			return sb.append(">").toString();
		}

		private static final long serialVersionUID = 0;
	}

	private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
		private final Type componentType;

		public GenericArrayTypeImpl(Type componentType) {
			this.componentType = canonicalize(componentType);
		}

		public Type getGenericComponentType() {
			return componentType;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof GenericArrayType && Types.equals(this, (GenericArrayType)o);
		}

		@Override
		public int hashCode() {
			return componentType.hashCode();
		}

		@Override
		public String toString() {
			return Types.toString(componentType) + "[]";
		}

		private static final long serialVersionUID = 0;
	}

	/**
	 * The WildcardType interface supports multiple upper bounds and multiple lower bounds. We only
	 * support what the Java 6 language needs - at most one bound. If a lower bound is set, the
	 * upper bound must be Object.class.
	 */
	private static final class WildcardTypeImpl implements WildcardType, Serializable {
		private final Type upperBound;
		private final Type lowerBound;

		public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
			Asserts.isTrue(lowerBounds.length <= 1);
			Asserts.isTrue(upperBounds.length == 1);

			if (lowerBounds.length == 1) {
				Asserts.notNull(lowerBounds[0]);
				checkNotPrimitive(lowerBounds[0]);
				Asserts.isTrue(upperBounds[0] == Object.class);
				this.lowerBound = canonicalize(lowerBounds[0]);
				this.upperBound = Object.class;

			}
			else {
				Asserts.notNull(upperBounds[0]);
				checkNotPrimitive(upperBounds[0]);
				this.lowerBound = null;
				this.upperBound = canonicalize(upperBounds[0]);
			}
		}

		public Type[] getUpperBounds() {
			return new Type[] { upperBound };
		}

		public Type[] getLowerBounds() {
			return lowerBound != null ? new Type[] { lowerBound } : EMPTY_TYPE_ARRAY;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof WildcardType && Types.equals(this, (WildcardType)other);
		}

		@Override
		public int hashCode() {
			// this equals Arrays.hashCode(getLowerBounds()) ^ Arrays.hashCode(getUpperBounds());
			return (lowerBound != null ? 31 + lowerBound.hashCode() : 1)
					^ (31 + upperBound.hashCode());
		}

		@Override
		public String toString() {
			if (lowerBound != null) {
				return "? super " + Types.toString(lowerBound);
			}
			else if (upperBound == Object.class) {
				return "?";
			}
			else {
				return "? extends " + Types.toString(upperBound);
			}
		}

		private static final long serialVersionUID = 0;
	}
}
