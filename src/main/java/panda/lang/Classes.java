package panda.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import panda.io.FileNames;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.reflect.Constructors;
import panda.log.Log;
import panda.log.Logs;

/**
 * ulitity class for Class
 * 
 * @author yf.frank.wang@gmail.com
 */
public abstract class Classes {
	public final static Class[] EMPTY_CLASS_ARRAY = new Class[0];

	public final static String PACKAGE_INFO_CLASS = "package-info.class";

	/**
	 * <p>
	 * The package separator character: <code>'&#x2e;' == {@value}</code>.
	 * </p>
	 */
	public static final char PACKAGE_SEPARATOR_CHAR = '.';

	/**
	 * <p>
	 * The package separator String: {@code "&#x2e;"}.
	 * </p>
	 */
	public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

	/**
	 * <p>
	 * The inner class separator character: <code>'$' == {@value}</code>.
	 * </p>
	 */
	public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

	/**
	 * <p>
	 * The inner class separator String: {@code "$"}.
	 * </p>
	 */
	public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

	/** Suffix for array class names: "[]" */
	public final static String ARRAY_SUFFIX = "[]";

	/** The CGLIB class separator character "$$" */
	public final static String CGLIB_CLASS_SEPARATOR = "$$";

	/** The ".class" file suffix */
	public final static String CLASS_FILE_SUFFIX = ".class";

	/**
	 * Map with primitive wrapper type as key and corresponding primitive type as value, for
	 * example: Integer.class -> int.class.
	 */
	private final static Map<Class<?>, Class<?>> wrapper2PrimitiveTypeMap = new HashMap<Class<?>, Class<?>>(
		9);

	/**
	 * Map with primitive type as key and corresponding primitive wrapper type as value, for
	 * example: int.class -> Integer.class.
	 */
	private final static Map<Class<?>, Class<?>> primitive2WrapperTypeMap = new HashMap<Class<?>, Class<?>>(
		9);

	static {
		wrapper2PrimitiveTypeMap.put(Boolean.class, Boolean.TYPE);
		wrapper2PrimitiveTypeMap.put(Byte.class, Byte.TYPE);
		wrapper2PrimitiveTypeMap.put(Character.class, Character.TYPE);
		wrapper2PrimitiveTypeMap.put(Double.class, Double.TYPE);
		wrapper2PrimitiveTypeMap.put(Float.class, Float.TYPE);
		wrapper2PrimitiveTypeMap.put(Integer.class, Integer.TYPE);
		wrapper2PrimitiveTypeMap.put(Long.class, Long.TYPE);
		wrapper2PrimitiveTypeMap.put(Short.class, Short.TYPE);
		wrapper2PrimitiveTypeMap.put(Void.class, Void.TYPE);

		primitive2WrapperTypeMap.put(Boolean.TYPE, Boolean.class);
		primitive2WrapperTypeMap.put(Byte.TYPE, Byte.class);
		primitive2WrapperTypeMap.put(Character.TYPE, Character.class);
		primitive2WrapperTypeMap.put(Double.TYPE, Double.class);
		primitive2WrapperTypeMap.put(Float.TYPE, Float.class);
		primitive2WrapperTypeMap.put(Integer.TYPE, Integer.class);
		primitive2WrapperTypeMap.put(Long.TYPE, Long.class);
		primitive2WrapperTypeMap.put(Short.TYPE, Short.class);
		primitive2WrapperTypeMap.put(Void.TYPE, Void.class);
	}

	/**
	 * Maps a primitive class name to its corresponding abbreviation used in array class names.
	 */
	private static final Map<String, String> abbreviationMap = new HashMap<String, String>();

	/**
	 * Maps an abbreviation used in array class names to corresponding primitive class name.
	 */
	private static final Map<String, String> reverseAbbreviationMap = new HashMap<String, String>();

	/**
	 * Add primitive type abbreviation to maps of abbreviations.
	 * 
	 * @param primitive Canonical name of primitive type
	 * @param abbreviation Corresponding abbreviation of primitive type
	 */
	private static void addAbbreviation(String primitive, String abbreviation) {
		abbreviationMap.put(primitive, abbreviation);
		reverseAbbreviationMap.put(abbreviation, primitive);
	}

	/**
	 * Feed abbreviation maps
	 */
	static {
		addAbbreviation("int", "I");
		addAbbreviation("boolean", "Z");
		addAbbreviation("float", "F");
		addAbbreviation("long", "J");
		addAbbreviation("short", "S");
		addAbbreviation("byte", "B");
		addAbbreviation("double", "D");
		addAbbreviation("char", "C");
	}

	/**
	 * immutable types
	 */
	private static Class<?>[] IMMUTABLE_TYPES = {
		CharSequence.class,
		Date.class,
		Calendar.class,
		Number.class
	};
	
	// Short class name
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the class name minus the package name for an {@code Object}.
	 * </p>
	 * 
	 * @param object the class to get the short name for, may be null
	 * @param valueIfNull the value to return if null
	 * @return the class name of the object without the package name, or the null value
	 */
	public static String getShortClassName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getShortClassName(object.getClass());
	}

	/**
	 * <p>
	 * Gets the class name minus the package name from a {@code Class}.
	 * </p>
	 * <p>
	 * Consider using the Java 5 API {@link Class#getSimpleName()} instead. The one known difference
	 * is that this code will return {@code "Map.Entry"} while the {@code java.lang.Class} variant
	 * will simply return {@code "Entry"}.
	 * </p>
	 * 
	 * @param cls the class to get the short name for.
	 * @return the class name without the package name or an empty string
	 */
	public static String getShortClassName(Class<?> cls) {
		if (cls == null) {
			return Strings.EMPTY;
		}
		return getShortClassName(cls.getName());
	}

	/**
	 * <p>
	 * Gets the class name minus the package name from a String.
	 * </p>
	 * <p>
	 * The string passed in is assumed to be a class name - it is not checked.
	 * </p>
	 * <p>
	 * Note that this method differs from Class.getSimpleName() in that this will return
	 * {@code "Map.Entry"} whilst the {@code java.lang.Class} variant will simply return
	 * {@code "Entry"}.
	 * </p>
	 * 
	 * @param className the className to get the short name for
	 * @return the class name of the class without the package name or an empty string
	 */
	public static String getShortClassName(String className) {
		if (className == null) {
			return Strings.EMPTY;
		}
		if (className.length() == 0) {
			return Strings.EMPTY;
		}

		StringBuilder arrayPrefix = new StringBuilder();

		// Handle array encoding
		if (className.startsWith("[")) {
			while (className.charAt(0) == '[') {
				className = className.substring(1);
				arrayPrefix.append("[]");
			}
			// Strip Object type encoding
			if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
				className = className.substring(1, className.length() - 1);
			}
		}

		if (reverseAbbreviationMap.containsKey(className)) {
			className = reverseAbbreviationMap.get(className);
		}

		int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR,
			lastDotIdx == -1 ? 0 : lastDotIdx + 1);
		String out = className.substring(lastDotIdx + 1);
		if (innerIdx != -1) {
			out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
		}
		return out + arrayPrefix;
	}

	/**
	 * <p>
	 * Null-safe version of <code>aClass.getSimpleName()</code>
	 * </p>
	 * 
	 * @param cls the class for which to get the simple name.
	 * @return the simple class name.
	 * @see Class#getSimpleName()
	 */
	public static String getSimpleName(Class<?> cls) {
		if (cls == null) {
			return Strings.EMPTY;
		}
		return cls.getSimpleName();
	}

	/**
	 * <p>
	 * Null-safe version of <code>aClass.getSimpleName()</code>
	 * </p>
	 * 
	 * @param object the object for which to get the simple class name.
	 * @param valueIfNull the value to return if <code>object</code> is <code>null</code>
	 * @return the simple class name.
	 * @see Class#getSimpleName()
	 */
	public static String getSimpleName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getSimpleName(object.getClass());
	}

	// Package name
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the package name of an {@code Object}.
	 * </p>
	 * 
	 * @param object the class to get the package name for, may be null
	 * @param valueIfNull the value to return if null
	 * @return the package name of the object, or the null value
	 */
	public static String getPackageName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getPackageName(object.getClass());
	}

	/**
	 * <p>
	 * Gets the package name of a {@code Class}.
	 * </p>
	 * 
	 * @param cls the class to get the package name for, may be {@code null}.
	 * @return the package name or an empty string
	 */
	public static String getPackageName(Class<?> cls) {
		if (cls == null) {
			return Strings.EMPTY;
		}
		return getPackageName(cls.getName());
	}

	/**
	 * <p>
	 * Gets the package name from a {@code String}.
	 * </p>
	 * <p>
	 * The string passed in is assumed to be a class name - it is not checked.
	 * </p>
	 * <p>
	 * If the class is unpackaged, return an empty string.
	 * </p>
	 * 
	 * @param className the className to get the package name for, may be {@code null}
	 * @return the package name or an empty string
	 */
	public static String getPackageName(String className) {
		if (className == null || className.length() == 0) {
			return Strings.EMPTY;
		}

		// Strip array encoding
		while (className.charAt(0) == '[') {
			className = className.substring(1);
		}
		// Strip Object type encoding
		if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
			className = className.substring(1);
		}

		int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		if (i == -1) {
			return Strings.EMPTY;
		}
		return className.substring(0, i);
	}

	// Superclasses/Superinterfaces
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets a {@code List} of superclasses for the given class.
	 * </p>
	 * 
	 * @param cls the class to look up, may be {@code null}
	 * @return the {@code List} of superclasses in order going up from this one {@code null} if null
	 *         input
	 */
	public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
		if (cls == null) {
			return null;
		}
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> superclass = cls.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return classes;
	}

	/**
	 * <p>
	 * Gets a {@code List} of all interfaces implemented by the given class and its superclasses.
	 * </p>
	 * <p>
	 * The order is determined by looking through each interface in turn as declared in the source
	 * file and following its hierarchy up. Then each superclass is considered in the same way.
	 * Later duplicates are ignored, so the order is maintained.
	 * </p>
	 * 
	 * @param cls the class to look up, may be {@code null}
	 * @return the {@code List} of interfaces in order, {@code null} if null input
	 */
	public static List<Class<?>> getAllInterfaces(Class<?> cls) {
		if (cls == null) {
			return null;
		}

		LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
		getAllInterfaces(cls, interfacesFound);

		return new ArrayList<Class<?>>(interfacesFound);
	}

	/**
	 * Get the interfaces for the specified class.
	 * 
	 * @param cls the class to look up, may be {@code null}
	 * @param interfacesFound the {@code Set} of interfaces for the class
	 */
	private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
		while (cls != null) {
			Class<?>[] interfaces = cls.getInterfaces();

			for (Class<?> i : interfaces) {
				if (interfacesFound.add(i)) {
					getAllInterfaces(i, interfacesFound);
				}
			}

			cls = cls.getSuperclass();
		}
	}

	// Convert list
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Given a {@code List} of class names, this method converts them into classes.
	 * </p>
	 * <p>
	 * A new {@code List} is returned. If the class name cannot be found, {@code null} is stored in
	 * the {@code List}. If the class name in the {@code List} is {@code null}, {@code null} is
	 * stored in the output {@code List}.
	 * </p>
	 * 
	 * @param classNames the classNames to change
	 * @return a {@code List} of Class objects corresponding to the class names, {@code null} if
	 *         null input
	 * @throws ClassCastException if classNames contains a non String entry
	 */
	public static List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
		if (classNames == null) {
			return null;
		}
		List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
		for (String className : classNames) {
			try {
				classes.add(Class.forName(className));
			}
			catch (Exception ex) {
				classes.add(null);
			}
		}
		return classes;
	}

	/**
	 * <p>
	 * Given a {@code List} of {@code Class} objects, this method converts them into class names.
	 * </p>
	 * <p>
	 * A new {@code List} is returned. {@code null} objects will be copied into the returned list as
	 * {@code null}.
	 * </p>
	 * 
	 * @param classes the classes to change
	 * @return a {@code List} of class names corresponding to the Class objects, {@code null} if
	 *         null input
	 * @throws ClassCastException if {@code classes} contains a non-{@code Class} entry
	 */
	public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
		if (classes == null) {
			return null;
		}
		List<String> classNames = new ArrayList<String>(classes.size());
		for (Class<?> cls : classes) {
			if (cls == null) {
				classNames.add(null);
			}
			else {
				classNames.add(cls.getName());
			}
		}
		return classNames;
	}

	// Is assignable
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Classes can be assigned to another array of Classes.
	 * </p>
	 * <p>
	 * This method calls {@link #isAssignable(Class, Class) isAssignable} for each Class pair in the
	 * input arrays. It can be used to check if a set of arguments (the first parameter) are
	 * suitably compatible with a set of method parameter types (the second parameter).
	 * </p>
	 * <p>
	 * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this method takes into
	 * account widenings of primitive classes and {@code null}s.
	 * </p>
	 * <p>
	 * Primitive widenings allow an int to be assigned to a {@code long}, {@code float} or
	 * {@code double}. This method returns the correct result for these cases.
	 * </p>
	 * <p>
	 * {@code Null} may be assigned to any reference type. This method will return {@code true} if
	 * {@code null} is passed in and the toClass is non-primitive.
	 * </p>
	 * <p>
	 * Specifically, this method tests whether the type represented by the specified {@code Class}
	 * parameter can be converted to the type represented by this {@code Class} object via an
	 * identity conversion widening primitive or widening reference conversion. See
	 * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
	 * sections 5.1.1, 5.1.2 and 5.1.4 for details.
	 * </p>
	 * <p>
	 * <strong>Since Lang 3.0,</strong> this method will default behavior for calculating
	 * assignability between primitive and wrapper types <em>corresponding
	 * to the running Java version</em>; i.e. autoboxing will be the default behavior in VMs running
	 * Java versions >= 1.5.
	 * </p>
	 * 
	 * @param classArray the array of Classes to check, may be {@code null}
	 * @param toClassArray the array of Classes to try to assign into, may be {@code null}
	 * @return {@code true} if assignment possible
	 */
	public static boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray) {
		return isAssignable(classArray, toClassArray, true);
	}

	/**
	 * <p>
	 * Checks if an array of Classes can be assigned to another array of Classes.
	 * </p>
	 * <p>
	 * This method calls {@link #isAssignable(Class, Class) isAssignable} for each Class pair in the
	 * input arrays. It can be used to check if a set of arguments (the first parameter) are
	 * suitably compatible with a set of method parameter types (the second parameter).
	 * </p>
	 * <p>
	 * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this method takes into
	 * account widenings of primitive classes and {@code null}s.
	 * </p>
	 * <p>
	 * Primitive widenings allow an int to be assigned to a {@code long}, {@code float} or
	 * {@code double}. This method returns the correct result for these cases.
	 * </p>
	 * <p>
	 * {@code Null} may be assigned to any reference type. This method will return {@code true} if
	 * {@code null} is passed in and the toClass is non-primitive.
	 * </p>
	 * <p>
	 * Specifically, this method tests whether the type represented by the specified {@code Class}
	 * parameter can be converted to the type represented by this {@code Class} object via an
	 * identity conversion widening primitive or widening reference conversion. See
	 * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
	 * sections 5.1.1, 5.1.2 and 5.1.4 for details.
	 * </p>
	 * 
	 * @param classArray the array of Classes to check, may be {@code null}
	 * @param toClassArray the array of Classes to try to assign into, may be {@code null}
	 * @param autoboxing whether to use implicit autoboxing/unboxing between primitives and wrappers
	 * @return {@code true} if assignment possible
	 */
	public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray,
			boolean autoboxing) {
		if (Arrays.isSameLength(classArray, toClassArray) == false) {
			return false;
		}
		if (classArray == null) {
			classArray = Arrays.EMPTY_CLASS_ARRAY;
		}
		if (toClassArray == null) {
			toClassArray = Arrays.EMPTY_CLASS_ARRAY;
		}
		for (int i = 0; i < classArray.length; i++) {
			if (isAssignable(classArray[i], toClassArray[i], autoboxing) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns whether the given {@code type} is a primitive or primitive wrapper ({@link Boolean},
	 * {@link Byte}, {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link Double}
	 * , {@link Float}).
	 * 
	 * @param type The class to query or null.
	 * @return true if the given {@code type} is a primitive or primitive wrapper ({@link Boolean},
	 *         {@link Byte}, {@link Character}, {@link Short}, {@link Integer}, {@link Long},
	 *         {@link Double}, {@link Float}).
	 */
	public static boolean isPrimitiveOrWrapper(Class<?> type) {
		if (type == null) {
			return false;
		}
		return type.isPrimitive() || isPrimitiveWrapper(type);
	}

	/**
	 * Returns whether the given {@code type} is a primitive wrapper ({@link Boolean}, {@link Byte},
	 * {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link Double},
	 * {@link Float}).
	 * 
	 * @param type The class to query or null.
	 * @return true if the given {@code type} is a primitive wrapper ({@link Boolean}, {@link Byte},
	 *         {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link Double},
	 *         {@link Float}).
	 */
	public static boolean isPrimitiveWrapper(Class<?> type) {
		return wrapper2PrimitiveTypeMap.containsKey(type);
	}

	public static boolean isPrimitiveType(Class<?> type) {
		return type != null && type.isPrimitive();
	}

	/**
	 * <p>
	 * Checks if one {@code Class} can be assigned to a variable of another {@code Class}.
	 * </p>
	 * <p>
	 * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this method takes into
	 * account widenings of primitive classes and {@code null}s.
	 * </p>
	 * <p>
	 * Primitive widenings allow an int to be assigned to a long, float or double. This method
	 * returns the correct result for these cases.
	 * </p>
	 * <p>
	 * {@code Null} may be assigned to any reference type. This method will return {@code true} if
	 * {@code null} is passed in and the toClass is non-primitive.
	 * </p>
	 * <p>
	 * Specifically, this method tests whether the type represented by the specified {@code Class}
	 * parameter can be converted to the type represented by this {@code Class} object via an
	 * identity conversion widening primitive or widening reference conversion. See
	 * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
	 * sections 5.1.1, 5.1.2 and 5.1.4 for details.
	 * </p>
	 * <p>
	 * <strong>Since Lang 3.0,</strong> this method will default behavior for calculating
	 * assignability between primitive and wrapper types <em>corresponding
	 * to the running Java version</em>; i.e. autoboxing will be the default behavior in VMs running
	 * Java versions >= 1.5.
	 * </p>
	 * 
	 * @param cls the Class to check, may be null
	 * @param toClass the Class to try to assign into, returns false if null
	 * @return {@code true} if assignment possible
	 */
	public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
		return isAssignable(cls, toClass, true);
	}

	/**
	 * <p>
	 * Checks if one {@code Class} can be assigned to a variable of another {@code Class}.
	 * </p>
	 * <p>
	 * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this method takes into
	 * account widenings of primitive classes and {@code null}s.
	 * </p>
	 * <p>
	 * Primitive widenings allow an int to be assigned to a long, float or double. This method
	 * returns the correct result for these cases.
	 * </p>
	 * <p>
	 * {@code Null} may be assigned to any reference type. This method will return {@code true} if
	 * {@code null} is passed in and the toClass is non-primitive.
	 * </p>
	 * <p>
	 * Specifically, this method tests whether the type represented by the specified {@code Class}
	 * parameter can be converted to the type represented by this {@code Class} object via an
	 * identity conversion widening primitive or widening reference conversion. See
	 * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
	 * sections 5.1.1, 5.1.2 and 5.1.4 for details.
	 * </p>
	 * 
	 * @param cls the Class to check, may be null
	 * @param toClass the Class to try to assign into, returns false if null
	 * @param autoboxing whether to use implicit autoboxing/unboxing between primitives and wrappers
	 * @return {@code true} if assignment possible
	 */
	public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
		// have to check for null, as isAssignableFrom doesn't
		if (cls == null) {
			return toClass == null || !toClass.isPrimitive();
		}
		
		if (toClass == null) {
			return false;
		}

		// autoboxing:
		if (autoboxing) {
			if (cls.isPrimitive() && !toClass.isPrimitive()) {
				cls = primitiveToWrapper(cls);
				if (cls == null) {
					return false;
				}
			}
			if (toClass.isPrimitive() && !cls.isPrimitive()) {
				cls = wrapperToPrimitive(cls);
				if (cls == null) {
					return false;
				}
			}
		}
		if (cls.equals(toClass)) {
			return true;
		}
		if (cls.isPrimitive()) {
			if (toClass.isPrimitive() == false) {
				return false;
			}
			if (Integer.TYPE.equals(cls)) {
				return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass)
						|| Double.TYPE.equals(toClass);
			}
			if (Long.TYPE.equals(cls)) {
				return Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
			}
			if (Boolean.TYPE.equals(cls)) {
				return false;
			}
			if (Double.TYPE.equals(cls)) {
				return false;
			}
			if (Float.TYPE.equals(cls)) {
				return Double.TYPE.equals(toClass);
			}
			if (Character.TYPE.equals(cls)) {
				return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass)
						|| Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
			}
			if (Short.TYPE.equals(cls)) {
				return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass)
						|| Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
			}
			if (Byte.TYPE.equals(cls)) {
				return Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass)
						|| Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass)
						|| Double.TYPE.equals(toClass);
			}
			// should never get here
			return false;
		}
		return toClass.isAssignableFrom(cls);
	}

	/**
	 * <p>
	 * Converts the specified primitive Class object to its corresponding wrapper Class object.
	 * </p>
	 * <p>
	 * NOTE: From v2.2, this method handles {@code Void.TYPE}, returning {@code Void.TYPE}.
	 * </p>
	 * 
	 * @param cls the class to convert, may be null
	 * @return the wrapper class for {@code cls} or {@code cls} if {@code cls} is not a primitive.
	 *         {@code null} if null input.
	 */
	public static Class<?> primitiveToWrapper(Class<?> cls) {
		Class<?> convertedClass = cls;
		if (cls != null && cls.isPrimitive()) {
			convertedClass = primitive2WrapperTypeMap.get(cls);
		}
		return convertedClass;
	}

	/**
	 * <p>
	 * Converts the specified array of primitive Class objects to an array of its corresponding
	 * wrapper Class objects.
	 * </p>
	 * 
	 * @param classes the class array to convert, may be null or empty
	 * @return an array which contains for each given class, the wrapper class or the original class
	 *         if class is not a primitive. {@code null} if null input. Empty array if an empty
	 *         array passed in.
	 */
	public static Class<?>[] primitivesToWrappers(Class<?>... classes) {
		if (classes == null) {
			return null;
		}

		if (classes.length == 0) {
			return classes;
		}

		Class<?>[] convertedClasses = new Class[classes.length];
		for (int i = 0; i < classes.length; i++) {
			convertedClasses[i] = primitiveToWrapper(classes[i]);
		}
		return convertedClasses;
	}

	/**
	 * <p>
	 * Converts the specified wrapper class to its corresponding primitive class.
	 * </p>
	 * <p>
	 * This method is the counter part of {@code primitiveToWrapper()}. If the passed in class is a
	 * wrapper class for a primitive type, this primitive type will be returned (e.g.
	 * {@code Integer.TYPE} for {@code Integer.class}). For other classes, or if the parameter is
	 * <b>null</b>, the return value is <b>null</b>.
	 * </p>
	 * 
	 * @param cls the class to convert, may be <b>null</b>
	 * @return the corresponding primitive type if {@code cls} is a wrapper class, <b>null</b>
	 *         otherwise
	 * @see #primitiveToWrapper(Class)
	 */
	public static Class<?> wrapperToPrimitive(Class<?> cls) {
		return wrapper2PrimitiveTypeMap.get(cls);
	}

	/**
	 * <p>
	 * Converts the specified array of wrapper Class objects to an array of its corresponding
	 * primitive Class objects.
	 * </p>
	 * <p>
	 * This method invokes {@code wrapperToPrimitive()} for each element of the passed in array.
	 * </p>
	 * 
	 * @param classes the class array to convert, may be null or empty
	 * @return an array which contains for each given class, the primitive class or <b>null</b> if
	 *         the original class is not a wrapper class. {@code null} if null input. Empty array if
	 *         an empty array passed in.
	 * @see #wrapperToPrimitive(Class)
	 */
	public static Class<?>[] wrappersToPrimitives(Class<?>... classes) {
		if (classes == null) {
			return null;
		}

		if (classes.length == 0) {
			return classes;
		}

		Class<?>[] convertedClasses = new Class[classes.length];
		for (int i = 0; i < classes.length; i++) {
			convertedClasses[i] = wrapperToPrimitive(classes[i]);
		}
		return convertedClasses;
	}

	// Inner class
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Is the specified class an inner class or static nested class.
	 * </p>
	 * 
	 * @param cls the class to check, may be null
	 * @return {@code true} if the class is an inner or static nested class, false if not or
	 *         {@code null}
	 */
	public static boolean isInnerClass(Class<?> cls) {
		return cls != null && cls.getEnclosingClass() != null;
	}

	// Class loading
	// ----------------------------------------------------------------------
	/**
	 * Returns the class represented by {@code className} using the current thread's context class
	 * loader. This implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "
	 * {@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param className the class name
	 * @return the class represented by {@code className} using the current thread's context class
	 *         loader, null if the class is not found
	 */
	public static Class<?> findClass(String className) {
		try {
			return getClass(className);
		}
		catch (Error e) {
			return null;
		}
		catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Returns the class represented by {@code className} using the {@code classLoader}. This
	 * implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "
	 * {@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param classLoader the class loader to use to load the class
	 * @param className the class name
	 * @param initialize whether the class must be initialized
	 * @return the class represented by {@code className} using the {@code classLoader}
	 * @throws RuntimeException if the class is not found
	 */
	public static Class<?> load(ClassLoader classLoader, String className, boolean initialize) {
		try {
			return Classes.getClass(classLoader, className, initialize);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * Returns the (initialized) class represented by {@code className} using the
	 * {@code classLoader}. This implementation supports the syntaxes "{@code java.util.Map.Entry[]}
	 * ", "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param classLoader the class loader to use to load the class
	 * @param className the class name
	 * @return the class represented by {@code className} using the {@code classLoader}
	 * @throws RuntimeException if the class is not found
	 */
	public static Class<?> load(ClassLoader classLoader, String className) {
		try {
			return Classes.getClass(classLoader, className);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * Returns the (initialized) class represented by {@code className} using the current thread's
	 * context class loader. This implementation supports the syntaxes "{@code java.util.Map.Entry[]}
	 * ", "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param className the class name
	 * @return the class represented by {@code className} using the current thread's context class
	 *         loader
	 * @throws RuntimeException if the class is not found
	 */
	public static Class<?> load(String className) {
		try {
			return Classes.getClass(className);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * Returns the class represented by {@code className} using the current thread's context class
	 * loader. This implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "
	 * {@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param className the class name
	 * @param initialize whether the class must be initialized
	 * @return the class represented by {@code className} using the current thread's context class
	 *         loader
	 * @throws RuntimeException if the class is not found
	 */
	public static Class<?> load(String className, boolean initialize) {
		try {
			return Classes.getClass(className, initialize);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	// Class loading
	// ----------------------------------------------------------------------
	/**
	 * Returns the class represented by {@code className} using the {@code classLoader}. This
	 * implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "
	 * {@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param classLoader the class loader to use to load the class
	 * @param className the class name
	 * @param initialize whether the class must be initialized
	 * @return the class represented by {@code className} using the {@code classLoader}
	 * @throws ClassNotFoundException if the class is not found
	 */
	public static Class<?> getClass(ClassLoader classLoader, String className, boolean initialize)
			throws ClassNotFoundException {
		try {
			Class<?> cls;
			if (abbreviationMap.containsKey(className)) {
				String clsName = "[" + abbreviationMap.get(className);
				cls = Class.forName(clsName, initialize, classLoader).getComponentType();
			}
			else {
				cls = Class.forName(toCanonicalName(className), initialize, classLoader);
			}
			return cls;
		}
		catch (ClassNotFoundException ex) {
			// allow path separators (.) as inner class name separators
			int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);

			if (lastDotIndex != -1) {
				try {
					return getClass(classLoader, className.substring(0, lastDotIndex)
							+ INNER_CLASS_SEPARATOR_CHAR + className.substring(lastDotIndex + 1),
						initialize);
				}
				catch (ClassNotFoundException ex2) { // NOPMD
					// ignore exception
				}
			}

			throw ex;
		}
	}

	/**
	 * Returns the (initialized) class represented by {@code className} using the
	 * {@code classLoader}. This implementation supports the syntaxes "{@code java.util.Map.Entry[]}
	 * ", "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param classLoader the class loader to use to load the class
	 * @param className the class name
	 * @return the class represented by {@code className} using the {@code classLoader}
	 * @throws ClassNotFoundException if the class is not found
	 */
	public static Class<?> getClass(ClassLoader classLoader, String className)
			throws ClassNotFoundException {
		return getClass(classLoader, className, true);
	}

	/**
	 * Returns the (initialized) class represented by {@code className} using the current thread's
	 * context class loader. This implementation supports the syntaxes "{@code java.util.Map.Entry[]}
	 * ", "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param className the class name
	 * @return the class represented by {@code className} using the current thread's context class
	 *         loader
	 * @throws ClassNotFoundException if the class is not found
	 */
	public static Class<?> getClass(String className) throws ClassNotFoundException {
		return getClass(className, true);
	}

	/**
	 * Returns the class represented by {@code className} using the current thread's context class
	 * loader. This implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "
	 * {@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}", and "
	 * {@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param className the class name
	 * @param initialize whether the class must be initialized
	 * @return the class represented by {@code className} using the current thread's context class
	 *         loader
	 * @throws ClassNotFoundException if the class is not found
	 */
	public static Class<?> getClass(String className, boolean initialize)
			throws ClassNotFoundException {
		return getClass(ClassLoaders.getClassLoader(), className, initialize);
	}
	
	// Public method
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Returns the desired Method much like {@code Class.getMethod}, however it ensures that the
	 * returned Method is from a public class or interface and not from an anonymous inner class.
	 * This means that the Method is invokable and doesn't fall foul of Java bug <a
	 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4071957">4071957</a>).
	 * <code><pre>Set set = Collections.unmodifiableSet(...);
	 *  Method method = ClassUtils.getPublicMethod(set.getClass(), "isEmpty",  new Class[0]);
	 *  Object result = method.invoke(set, new Object[]);</pre></code>
	 * </p>
	 * 
	 * @param cls the class to check, not null
	 * @param methodName the name of the method
	 * @param parameterTypes the list of parameters
	 * @return the method
	 * @throws NullPointerException if the class is null
	 * @throws SecurityException if a a security violation occured
	 * @throws NoSuchMethodException if the method is not found in the given class or if the
	 *             metothod doen't conform with the requirements
	 */
	public static Method getPublicMethod(Class<?> cls, String methodName,
			Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {

		Method declaredMethod = cls.getMethod(methodName, parameterTypes);
		if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
			return declaredMethod;
		}

		List<Class<?>> candidateClasses = new ArrayList<Class<?>>();
		candidateClasses.addAll(getAllInterfaces(cls));
		candidateClasses.addAll(getAllSuperclasses(cls));

		for (Class<?> candidateClass : candidateClasses) {
			if (!Modifier.isPublic(candidateClass.getModifiers())) {
				continue;
			}
			Method candidateMethod;
			try {
				candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
			}
			catch (NoSuchMethodException ex) {
				continue;
			}
			if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
				return candidateMethod;
			}
		}

		throw new NoSuchMethodException("Can't find a public method for " + methodName + " "
				+ Arrays.toString(parameterTypes));
	}

	// ----------------------------------------------------------------------
	/**
	 * Converts a class name to a JLS style class name.
	 * 
	 * @param className the class name
	 * @return the converted name
	 */
	private static String toCanonicalName(String className) {
		className = Strings.deleteWhitespace(className);
		if (className == null) {
			throw new NullPointerException("className must not be null.");
		}
		else if (className.endsWith("[]")) {
			StringBuilder classNameBuffer = new StringBuilder();
			while (className.endsWith("[]")) {
				className = className.substring(0, className.length() - 2);
				classNameBuffer.append("[");
			}
			String abbreviation = abbreviationMap.get(className);
			if (abbreviation != null) {
				classNameBuffer.append(abbreviation);
			}
			else {
				classNameBuffer.append("L").append(className).append(";");
			}
			className = classNameBuffer.toString();
		}
		return className;
	}

	/**
	 * <p>
	 * Converts an array of {@code Object} in to an array of {@code Class} objects. If any of these
	 * objects is null, a null element will be inserted into the array.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array an {@code Object} array
	 * @return a {@code Class} array, {@code null} if null array input
	 */
	public static Class<?>[] toClass(Object... array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return Arrays.EMPTY_CLASS_ARRAY;
		}
		Class<?>[] classes = new Class[array.length];
		for (int i = 0; i < array.length; i++) {
			classes[i] = array[i] == null ? null : array[i].getClass();
		}
		return classes;
	}

	// Short canonical name
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the canonical name minus the package name for an {@code Object}.
	 * </p>
	 * 
	 * @param object the class to get the short name for, may be null
	 * @param valueIfNull the value to return if null
	 * @return the canonical name of the object without the package name, or the null value
	 */
	public static String getShortCanonicalName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getShortCanonicalName(object.getClass().getName());
	}

	/**
	 * <p>
	 * Gets the canonical name minus the package name from a {@code Class}.
	 * </p>
	 * 
	 * @param cls the class to get the short name for.
	 * @return the canonical name without the package name or an empty string
	 */
	public static String getShortCanonicalName(Class<?> cls) {
		if (cls == null) {
			return Strings.EMPTY;
		}
		return getShortCanonicalName(cls.getName());
	}

	/**
	 * <p>
	 * Gets the canonical name minus the package name from a String.
	 * </p>
	 * <p>
	 * The string passed in is assumed to be a canonical name - it is not checked.
	 * </p>
	 * 
	 * @param canonicalName the class name to get the short name for
	 * @return the canonical name of the class without the package name or an empty string
	 */
	public static String getShortCanonicalName(String canonicalName) {
		return Classes.getShortClassName(getCanonicalName(canonicalName));
	}

	// Package name
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the package name from the canonical name of an {@code Object}.
	 * </p>
	 * 
	 * @param object the class to get the package name for, may be null
	 * @param valueIfNull the value to return if null
	 * @return the package name of the object, or the null value
	 */
	public static String getPackageCanonicalName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getPackageCanonicalName(object.getClass().getName());
	}

	/**
	 * <p>
	 * Gets the package name from the canonical name of a {@code Class}.
	 * </p>
	 * 
	 * @param cls the class to get the package name for, may be {@code null}.
	 * @return the package name or an empty string
	 */
	public static String getPackageCanonicalName(Class<?> cls) {
		if (cls == null) {
			return Strings.EMPTY;
		}
		return getPackageCanonicalName(cls.getName());
	}

	/**
	 * <p>
	 * Gets the package name from the canonical name.
	 * </p>
	 * <p>
	 * The string passed in is assumed to be a canonical name - it is not checked.
	 * </p>
	 * <p>
	 * If the class is unpackaged, return an empty string.
	 * </p>
	 * 
	 * @param canonicalName the canonical name to get the package name for, may be {@code null}
	 * @return the package name or an empty string
	 */
	public static String getPackageCanonicalName(String canonicalName) {
		return Classes.getPackageName(getCanonicalName(canonicalName));
	}

	/**
	 * <p>
	 * Converts a given name of class into canonical format. If name of class is not a name of array
	 * class it returns unchanged name.
	 * </p>
	 * <p>
	 * Example:
	 * <ul>
	 * <li>{@code getCanonicalName("[I") = "int[]"}</li>
	 * <li>{@code getCanonicalName("[Ljava.lang.String;") = "java.lang.String[]"}</li>
	 * <li>{@code getCanonicalName("java.lang.String") = "java.lang.String"}</li>
	 * </ul>
	 * </p>
	 * 
	 * @param className the name of class
	 * @return canonical form of class name
	 */
	private static String getCanonicalName(String className) {
		className = Strings.deleteWhitespace(className);
		if (className == null) {
			return null;
		}
		else {
			int dim = 0;
			while (className.startsWith("[")) {
				dim++;
				className = className.substring(1);
			}
			if (dim < 1) {
				return className;
			}
			else {
				if (className.startsWith("L")) {
					className = className.substring(1,
						className.endsWith(";") ? className.length() - 1 : className.length());
				}
				else {
					if (className.length() > 0) {
						className = reverseAbbreviationMap.get(className.substring(0, 1));
					}
				}
				StringBuilder canonicalClassNameBuffer = new StringBuilder(className);
				for (int i = 0; i < dim; i++) {
					canonicalClassNameBuffer.append("[]");
				}
				return canonicalClassNameBuffer.toString();
			}
		}
	}

	/**
	 * new instance by the class 
	 * @param cls class
	 * @return object instance
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 */
	public static <T> T newInstance(Class<T> cls) throws InstantiationException, IllegalAccessException {
		return cls.newInstance();
	}

	/**
	 * new instance by the className
	 * @param className class name
	 * @return object instance
	 * @throws ClassNotFoundException if class not found
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 */
	public static Object newInstance(String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		return newInstance(getClass(className));
	}

	/**
	 * new instance by the className 
	 * @param className class name
	 * @param argValue constructor argument value
	 * @return class instance
	 * @throws ClassNotFoundException if ClassNotFoundException occurs
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 */
	public static Object newInstance(String className, Object argValue)
			throws InstantiationException, IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {
		return newInstance(getClass(className), new Object[] { argValue }, toClass(argValue));
	}

	/**
	 * new instance by the className 
	 * @param className class name
	 * @param argValue constructor argument value
	 * @param argType constructor argument type
	 * @return class instance
	 * @throws ClassNotFoundException if ClassNotFoundException occurs
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 */
	public static Object newInstance(String className, Object argValue, Class<?> argType)
			throws InstantiationException, IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {
		return newInstance(getClass(className), new Object[] { argValue }, new Class<?>[] { argType });
	}

	/**
	 * new instance by the className
	 * @param className class name
	 * @return object instance
	 * @param argValues constructor arguments value
	 * @throws ClassNotFoundException if class not found
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 */
	public static Object newInstance(String className, Object[] argValues)
			throws InstantiationException, IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {
		Class<?>[] argTypes = toClass(argValues);
		return newInstance(getClass(className), argValues, argTypes);
	}

	/**
	 * new instance by the className
	 * @param className class name
	 * @return object instance
	 * @param argValues constructor arguments value
	 * @param argTypes constructor arguments type
	 * @throws ClassNotFoundException if class not found
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 */
	public static Object newInstance(String className, Object[] argValues, Class<?>[] argTypes)
			throws InstantiationException, IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {
		return newInstance(getClass(className), argValues, argTypes);
	}

	/**
	 * new instance by the class 
	 * @param cls class
	 * @param argValue constructor argument value
	 * @return class instance
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 */
	public static <T> T newInstance(Class<T> cls, Object argValue)
		throws InstantiationException, IllegalAccessException, InvocationTargetException {
		return newInstance(cls, new Object[] { argValue }, toClass(argValue));
	}

	/**
	 * new instance by the class 
	 * @param cls class
	 * @param argValue constructor argument value
	 * @param argType constructor argument type
	 * @return class instance
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 */
	public static <T> T newInstance(Class<T> cls, Object argValue, Class<?> argType)
		throws InstantiationException, IllegalAccessException, InvocationTargetException {
		return newInstance(cls, new Object[] { argValue }, new Class<?>[] { argType });
	}

	/**
	 * new instance by the class
	 * @param cls class
	 * @return object instance
	 * @param argValues constructor arguments value
	 * @param argTypes constructor arguments type
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 * @see Constructors#invokeConstructor(Class, Class[], Object[])
	 */
	public static <T> T newInstance(Class<T> cls, Object[] argValues, Class<?>[] argTypes)
		throws InstantiationException, IllegalAccessException, InvocationTargetException {
		return Constructors.invokeConstructor(cls, argTypes, argValues);
	}

	////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * new instance by the class 
	 * @param cls class
	 * @return object instance
	 */
	public static <T> T born(Class<T> cls) {
		try {
			return cls.newInstance();
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the className
	 * @param className class name
	 * @return object instance
	 */
	public static Object born(String className) {
		try {
			return getClass(className).newInstance();
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the className 
	 * @param className class name
	 * @param argValue constructor argument value
	 * @return class instance
	 */
	public static Object born(String className, Object argValue) {
		try {
			return newInstance(className, argValue);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the className 
	 * @param className class name
	 * @param argValue constructor argument value
	 * @param argType constructor argument type
	 * @return class instance
	 */
	public static Object born(String className, Object argValue, Class<?> argType) {
		try {
			return newInstance(className, argValue, argType);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the className
	 * @param className class name
	 * @return object instance
	 * @param argValues constructor arguments value
	 */
	public static Object born(String className, Object[] argValues) {
		try {
			return newInstance(className, argValues);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the className
	 * @param className class name
	 * @return object instance
	 * @param argValues constructor arguments value
	 * @param argTypes constructor arguments type
	 */
	public static Object born(String className, Object[] argValues, Class<?>[] argTypes) {
		try {
			return newInstance(className, argValues, argTypes);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the class 
	 * @param cls class
	 * @param argValue constructor argument value
	 * @return class instance
	 */
	public static <T> T born(Class<T> cls, Object argValue) {
		try {
			return newInstance(cls, argValue);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the class 
	 * @param cls class
	 * @param argValue constructor argument value
	 * @param argType constructor argument type
	 * @return class instance
	 */
	public static <T> T born(Class<T> cls, Object argValue, Class<?> argType) {
		try {
			return newInstance(cls, argValue, argType);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the class
	 * @param cls class
	 * @return object instance
	 * @param argValues constructor arguments value
	 */
	public static <T> T born(Class<T> cls, Object[] argValues) {
		try {
			return newInstance(cls, argValues);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * new instance by the class
	 * @param cls class
	 * @return object instance
	 * @param argValues constructor arguments value
	 * @param argTypes constructor arguments type
	 */
	public static <T> T born(Class<T> cls, Object[] argValues, Class<?>[] argTypes) {
		try {
			return newInstance(cls, argValues, argTypes);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a {@link Constructor} for the given method signature, or <tt>null</tt>
	 * if no such <tt>Constructor</tt> can be found.
	 *
	 * @param cls     the (non-<tt>null</tt>) type of {@link Object} the returned {@link Constructor} should create
	 * @param argTypes a non-<tt>null</tt> array of types describing the parameters to the {@link Constructor}.
	 * @return a {@link Constructor} for the given method signature, or <tt>null</tt>
	 *         if no such <tt>Constructor</tt> can be found.
	 * @see Constructors#getConstructor(Class, Class[])
	 */
	public static <T> Constructor<T> getConstructor(Class<T> cls, Class<?>[] argTypes) {
		return Constructors.getConstructor(cls, argTypes);
	}
	
	/**
	 * Override the thread context ClassLoader with the environment's bean ClassLoader
	 * if necessary, i.e. if the bean ClassLoader is not equivalent to the thread
	 * context ClassLoader already.
	 * @param classLoaderToUse the actual ClassLoader to use for the thread context
	 * @return the original thread context ClassLoader, or <code>null</code> if not overridden
	 */
	public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse) {
		Thread currentThread = Thread.currentThread();
		ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
		if (classLoaderToUse != null && !classLoaderToUse.equals(threadContextClassLoader)) {
			currentThread.setContextClassLoader(classLoaderToUse);
			return threadContextClassLoader;
		}
		else {
			return null;
		}
	}

	/**
	 * Determine whether the {@link Class} identified by the supplied name is present
	 * and can be loaded. Will return <code>false</code> if either the class or
	 * one of its dependencies is not present or cannot be loaded.
	 * @param className the name of the class to check
	 * @return whether the specified class is present
	 */
	public static boolean isPresent(String className) {
		return isPresent(className, ClassLoaders.getClassLoader());
	}

	/**
	 * Determine whether the {@link Class} identified by the supplied name is present
	 * and can be loaded. Will return <code>false</code> if either the class or
	 * one of its dependencies is not present or cannot be loaded.
	 * @param className the name of the class to check
	 * @param classLoader the class loader to use
	 * (may be <code>null</code>, which indicates the default class loader)
	 * @return whether the specified class is present
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			getClass(classLoader, className);
			return true;
		}
		catch (Throwable ex) {
			// Class or one of its dependencies is not present...
			return false;
		}
	}

	/**
	 * Primitive Class to Wrapper Class: int.class -> Integer.class
	 * @param cls the type of the potentially primitive class
	 * @return the primitive wrapper class
	 */
	public static Class<?> primitive2Wrapper(Class<?> cls) {
		if (cls != null && cls.isPrimitive()) {
			cls = primitive2WrapperTypeMap.get(cls);
		}
		return cls;
	}
	
	/**
	 * Wrapper Class to Primitive Class: Integer.class -> int.class
	 * @param cls the type of the potentially primitive wrapper class
	 * @return the primitive class
	 */
	public static Class<?> wrapper2Primitive(Class<?> cls) {
		Class<?> wrapper = wrapper2PrimitiveTypeMap.get(cls);
		return (wrapper == null ? cls : wrapper);
	}
	
	/**
	 * Return the user-defined class for the given instance: usually simply
	 * the class of the given instance, but the original class in case of a
	 * CGLIB-generated subclass.
	 * @param instance the instance to check
	 * @return the user-defined class
	 */
	public static Class<?> getUserClass(Object instance) {
		Asserts.notNull(instance, "Instance must not be null");
		return getUserClass(instance.getClass());
	}

	/**
	 * Return the user-defined class for the given class: usually simply the given
	 * class, but the original class in case of a CGLIB-generated subclass.
	 * @param cls the class to check
	 * @return the user-defined class
	 */
	public static Class<?> getUserClass(Class<?> cls) {
		return (cls != null && cls.getName().indexOf(CGLIB_CLASS_SEPARATOR) != -1 ?
				cls.getSuperclass() : cls);
	}

	/**
	 * Check whether the given class is cache-safe in the given context,
	 * i.e. whether it is loaded by the given ClassLoader or a parent of it.
	 * @param cls the class to analyze
	 * @param classLoader the ClassLoader to potentially cache metadata in
	 * @return true if the given class is cache-safe
	 */
	public static boolean isCacheSafe(Class<?> cls, ClassLoader classLoader) {
		Asserts.notNull(cls, "Class must not be null");
		ClassLoader target = cls.getClassLoader();
		if (target == null) {
			return false;
		}
		ClassLoader cur = classLoader;
		if (cur == target) {
			return true;
		}
		while (cur != null) {
			cur = cur.getParent();
			if (cur == target) {
				return true;
			}
		}
		return false;
	}


	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the class name minus the package name for an <code>Object</code>.
	 * </p>
	 * 
	 * @param object the class to get the short name for, may be null
	 * @param valueIfNull the value to return if null
	 * @return the class name of the object without the package name, or the null value
	 */
	public static String getSimpleClassName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getSimpleClassName(object.getClass().getName());
	}

	/**
	 * Get the class name without the qualified package name.
	 * @param className the className to get the short name for
	 * @return the class name of the class without the package name
	 * @throws IllegalArgumentException if the className is empty
	 */
	public static String getSimpleClassName(String className) {
		return getShortClassName(className);
	}

	/**
	 * Get the class name without the qualified package name.
	 * @param cls the class to get the short name for
	 * @return the class name of the class without the package name
	 */
	public static String getSimpleClassName(Class<?> cls) {
		return getSimpleClassName(getQualifiedClassName(cls));
	}

	/**
	 * Determine the name of the class file, relative to the containing
	 * package: e.g. "String.class"
	 * @param cls the class
	 * @return the file name of the ".class" file
	 */
	public static String getClassFileName(Class<?> cls) {
		Asserts.notNull(cls, "Class must not be null");
		String className = cls.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
	}

	/**
	 * Return the qualified name of the given class: usually simply
	 * the class name, but component type class name + "[]" for arrays.
	 * @param cls the class
	 * @return the qualified name of the class
	 */
	public static String getCastableClassName(String cls) {
		return cls.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
	}
	
	/**
	 * Return the qualified name of the given class: usually simply
	 * the class name, but component type class name + "[]" for arrays.
	 * @param cls the class
	 * @return the qualified name of the class
	 */
	public static String getQualifiedClassName(Class<?> cls) {
		Asserts.notNull(cls, "Class must not be null");
		if (cls.isArray()) {
			StringBuilder sb = new StringBuilder();
			while (cls.isArray()) {
				cls = cls.getComponentType();
				sb.append(Classes.ARRAY_SUFFIX);
			}
			sb.insert(0, getQualifiedClassName(cls));
			return sb.toString();
		}
		else {
			return getCastableClassName(cls.getName());
		}
	}

	/**
	 * Return the castable class name of the given class, usually wrap
	 * the class name: "int" -> "Integer".
	 * @param cls the class
	 * @return the castable class name
	 */
	public static String getCastableClassName(Class<?> cls) {
		if (cls.isArray()) {
			StringBuilder sb = new StringBuilder();
			while (cls.isArray()) {
				cls = cls.getComponentType();
				sb.append("[]");
			}
			sb.insert(0, getQualifiedClassName(cls));
			return sb.toString();
		}
		else if (cls.isPrimitive()) {
			return primitive2Wrapper(cls).getName();
		}
		else {
			return getCastableClassName(cls.getName());
		}
	}

	/**
	 * Return the qualified name of the given method, consisting of
	 * fully qualified interface/class name + "." + method name.
	 * @param method the method
	 * @return the qualified name of the method
	 */
	public static String getQualifiedMethodName(Method method) {
		Asserts.notNull(method, "Method must not be null");
		return method.getDeclaringClass().getName() + "." + method.getName();
	}


	/**
	 * Determine whether the given class has a constructor with the given signature.
	 * <p>Essentially translates <code>NoSuchMethodException</code> to "false".
	 * @param cls	the class to analyze
	 * @param paramTypes the parameter types of the method
	 * @return whether the class has a corresponding constructor
	 * @see java.lang.Class#getMethod
	 */
	public static boolean hasConstructor(Class<?> cls, Class<?>[] paramTypes) {
		return (getConstructorIfAvailable(cls, paramTypes) != null);
	}

	/**
	 * Determine whether the given class has a constructor with the given signature,
	 * and return it if available (else return <code>null</code>).
	 * <p>Essentially translates <code>NoSuchMethodException</code> to <code>null</code>.
	 * @param cls	the class to analyze
	 * @param paramTypes the parameter types of the method
	 * @return the constructor, or <code>null</code> if not found
	 * @see java.lang.Class#getConstructor
	 */
	public static Constructor getConstructorIfAvailable(Class<?> cls, Class<?>[] paramTypes) {
		Asserts.notNull(cls, "Class must not be null");
		try {
			return cls.getConstructor(paramTypes);
		}
		catch (NoSuchMethodException ex) {
			return null;
		}
	}

	/**
	 * Check if the given class represents an array of primitives,
	 * i.e. boolean, byte, char, short, int, long, float, or double.
	 * @param cls the class to check
	 * @return whether the given class is a primitive array class
	 */
	public static boolean isPrimitiveArray(Class<?> cls) {
		if (cls == null) {
			return false;
		}
		return (cls.isArray() && cls.getComponentType().isPrimitive());
	}

	/**
	 * Check if the given class represents an array of primitive wrappers,
	 * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
	 * @param cls the class to check
	 * @return whether the given class is a primitive wrapper array class
	 */
	public static boolean isPrimitiveWrapperArray(Class<?> cls) {
		if (cls == null) {
			return false;
		}
		return (cls.isArray() && isPrimitiveWrapper(cls.getComponentType()));
	}

	/**
	 * Determine if the given type is assignable from the given value,
	 * assuming setting by reflection. Considers primitive wrapper classes
	 * as assignable to the corresponding primitive types.
	 * @param cls	the target type
	 * @param value the value that should be assigned to the type
	 * @return if the type is assignable from the value
	 */
	public static boolean isAssignableValue(Class<?> cls, Object value) {
		Asserts.notNull(cls, "Class must not be null");
		return (value != null ? isAssignable(value.getClass(), cls) : !cls.isPrimitive());
	}

	/**
	 * Convert a "/"-based resource path to a "."-based fully qualified class name.
	 * @param resourcePath the resource path pointing to a class
	 * @return the corresponding fully qualified class name
	 */
	public static String resourcePathToClassName(String resourcePath) {
		return resourcePath.replace('/', '.');
	}

	/**
	 * Convert a "."-based fully qualified class name to a "/"-based resource path.
	 * @param className the fully qualified class name
	 * @return the corresponding resource path, pointing to the class
	 */
	public static String classNameToResourcePath(String className) {
		return className.replace('.', '/');
	}

	/**
	 * Return a path suitable for use with <code>ClassLoader.getResource</code>
	 * (also suitable for use with <code>Class.getResource</code> by prepending a
	 * slash ('/') to the return value. Built by taking the package of the specified
	 * class file, converting all dots ('.') to slashes ('/'), adding a trailing slash
	 * if necesssary, and concatenating the specified resource name to this.
	 * <br/>As such, this function may be used to build a path suitable for
	 * loading a resource file that is in the same package as a class file.
	 * @param cls	the Class whose package will be used as the base
	 * @param resourceName the resource name to append. A leading slash is optional.
	 * @return the built-up resource path
	 * @see java.lang.ClassLoader#getResource
	 * @see java.lang.Class#getResource
	 */
	public static String getResourcePath(Class<?> cls, String resourceName) {
		Asserts.notNull(resourceName, "Resource name must not be null");
		if (!resourceName.startsWith("/")) {
			return classPackageAsResourcePath(cls) + "/" + resourceName;
		}
		return classPackageAsResourcePath(cls) + resourceName;
	}

	/**
	 * Given an input class object, return a string which consists of the
	 * class's package name as a pathname, i.e., all dots ('.') are replaced by
	 * slashes ('/'). Neither a leading nor trailing slash is added. The result
	 * could be concatenated with a slash and the name of a resource, and fed
	 * directly to <code>ClassLoader.getResource()</code>. For it to be fed to
	 * <code>Class.getResource</code> instead, a leading slash would also have
	 * to be prepended to the returned value.
	 * @param cls the input class. A <code>null</code> value or the default
	 * (empty) package will result in an empty string ("") being returned.
	 * @return a path which represents the package name
	 * @see ClassLoader#getResource
	 * @see Class#getResource
	 */
	public static String classPackageAsResourcePath(Class<?> cls) {
		if (cls == null) {
			return "";
		}
		String className = cls.getName();
		int packageEndIndex = className.lastIndexOf('.');
		if (packageEndIndex == -1) {
			return "";
		}
		String packageName = className.substring(0, packageEndIndex);
		return packageName.replace('.', '/');
	}

	/**
	 * Build a String that consists of the names of the classes/interfaces
	 * in the given array.
	 * <p>Basically like <code>AbstractCollection.toString()</code>, but stripping
	 * the "class "/"interface " prefix before every class name.
	 * @param classes a Collection of Class objects (may be <code>null</code>)
	 * @return a String of form "[com.foo.Bar, com.foo.Baz]"
	 * @see java.util.AbstractCollection#toString()
	 */
	public static String classNamesToString(Class<?>[] classes) {
		return classNamesToString(Arrays.asList(classes));
	}

	/**
	 * Build a String that consists of the names of the classes/interfaces
	 * in the given collection.
	 * <p>Basically like <code>AbstractCollection.toString()</code>, but stripping
	 * the "class "/"interface " prefix before every class name.
	 * @param classes a Collection of Class objects (may be <code>null</code>)
	 * @return a String of form "[com.foo.Bar, com.foo.Baz]"
	 * @see java.util.AbstractCollection#toString()
	 */
	public static String classNamesToString(Collection classes) {
		if (Collections.isEmpty(classes)) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (Iterator it = classes.iterator(); it.hasNext(); ) {
			Class<?> cls = (Class<?>) it.next();
			sb.append(cls.getName());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}


	/**
	 * Return all interfaces that the given instance implements as array,
	 * including ones implemented by super classes.
	 * @param instance the instance to analyze for interfaces
	 * @return all interfaces that the given instance implements as array
	 */
	public static Class<?>[] getAllInterfaces(Object instance) {
		Asserts.notNull(instance, "Instance must not be null");
		return getAllInterfacesForClass(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as array,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param cls the class to analyse for interfaces
	 * @return all interfaces that the given object implements as array
	 */
	public static Class<?>[] getAllInterfacesForClass(Class<?> cls) {
		return getAllInterfacesForClass(cls, null);
	}

	/**
	 * Return all interfaces that the given class implements as array,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param cls the class to analyse for interfaces
	 * @param classLoader the ClassLoader that the interfaces need to be visible in
	 * (may be <code>null</code> when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as array
	 */
	public static Class<?>[] getAllInterfacesForClass(Class<?> cls, ClassLoader classLoader) {
		Asserts.notNull(cls, "Class must not be null");
		if (cls.isInterface()) {
			return new Class<?>[] {cls};
		}
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		while (cls != null) {
			for (int i = 0; i < cls.getInterfaces().length; i++) {
				Class<?> ifc = cls.getInterfaces()[i];
				if (!interfaces.contains(ifc) &&
						(classLoader == null || isVisible(ifc, classLoader))) {
					interfaces.add(ifc);
				}
			}
			cls = cls.getSuperclass();
		}
		return (Class<?>[]) interfaces.toArray(new Class<?>[interfaces.size()]);
	}

	/**
	 * Return all interfaces that the given instance implements as Set,
	 * including ones implemented by superclasses.
	 * @param instance the instance to analyse for interfaces
	 * @return all interfaces that the given instance implements as Set
	 */
	public static Set getAllInterfacesAsSet(Object instance) {
		Asserts.notNull(instance, "Instance must not be null");
		return getAllInterfacesForClassAsSet(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as Set,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param cls the class to analyse for interfaces
	 * @return all interfaces that the given object implements as Set
	 */
	public static Set getAllInterfacesForClassAsSet(Class<?> cls) {
		return getAllInterfacesForClassAsSet(cls, null);
	}

	/**
	 * Return all interfaces that the given class implements as Set,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param cls the class to analyse for interfaces
	 * @param classLoader the ClassLoader that the interfaces need to be visible in
	 * (may be <code>null</code> when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as Set
	 */
	public static Set getAllInterfacesForClassAsSet(Class<?> cls, ClassLoader classLoader) {
		Asserts.notNull(cls, "Class must not be null");
		if (cls.isInterface()) {
			return Collections.singleton(cls);
		}
		Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
		while (cls != null) {
			for (int i = 0; i < cls.getInterfaces().length; i++) {
				Class<?> ifc = cls.getInterfaces()[i];
				if (classLoader == null || isVisible(ifc, classLoader)) {
					interfaces.add(ifc);
				}
			}
			cls = cls.getSuperclass();
		}
		return interfaces;
	}

	/**
	 * Create a composite interface Class for the given interfaces,
	 * implementing the given interfaces in one single Class.
	 * <p>This implementation builds a JDK proxy class for the given interfaces.
	 * @param interfaces the interfaces to merge
	 * @param classLoader the ClassLoader to create the composite Class in
	 * @return the merged interface as Class
	 * @see java.lang.reflect.Proxy#getProxyClass
	 */
	public static Class<?> createCompositeInterface(Class[] interfaces, ClassLoader classLoader) {
		Asserts.notEmpty(interfaces, "Interfaces must not be empty");
		Asserts.notNull(classLoader, "ClassLoader must not be null");
		return Proxy.getProxyClass(classLoader, interfaces);
	}

	/**
	 * Check whether the given class is visible in the given ClassLoader.
	 * @param cls the class to check (typically an interface)
	 * @param classLoader the ClassLoader to check against (may be <code>null</code>,
	 * in which case this method will always return <code>true</code>)
	 * @return true if the given class is visible
	 */
	public static boolean isVisible(Class<?> cls, ClassLoader classLoader) {
		if (classLoader == null) {
			return true;
		}
		try {
			Class<?> actualClass = classLoader.loadClass(cls.getName());
			return (cls == actualClass);
			// Else: different interface class found...
		}
		catch (ClassNotFoundException ex) {
			// No interface class found...
			return false;
		}
	}

	/**
	 * find a annotation from this class to super class
	 * 
	 * @param <A> annotation type
	 * @param cls Class
	 * @param annType annotation type
	 * @return annotation instance
	 */
	public static <A extends Annotation> A getAnnotation(Class<?> cls, Class<A> annType) {
		A ann;
		do {
			ann = cls.getAnnotation(annType);
			cls = cls.getSuperclass();
		}
		while (null == ann && cls != null && cls != Object.class);
		return ann;
	}

	/**
	 * @return true if the class is String
	 */
	public static boolean isString(Class<?> cls) {
		return String.class.equals(cls);
	}

	/**
	 * @return true if the class is CharSequence
	 */
	public static boolean isCharSequence(Class<?> cls) {
		return isAssignable(cls, CharSequence.class);
	}

	/**
	 * @return true if the class is char or Character
	 */
	public static boolean isChar(Class<?> cls) {
		return char.class.equals(cls) || Character.class.equals(cls);
	}

	/**
	 * @return true if the class is boolean or Boolean
	 */
	public static boolean isBoolean(Class<?> cls) {
		return boolean.class.equals(cls) || Boolean.class.equals(cls);
	}

	/**
	 * @return true if the class is float or Float
	 */
	public static boolean isFloat(Class<?> cls) {
		return float.class.equals(cls) || Float.class.equals(cls);
	}

	/**
	 * @return true if the class is double or Double
	 */
	public static boolean isDouble(Class<?> cls) {
		return double.class.equals(cls) || Double.class.equals(cls);
	}

	/**
	 * @return true if the class is int or Integer
	 */
	public static boolean isInt(Class<?> cls) {
		return int.class.equals(cls) || Integer.class.equals(cls);
	}

	/**
	 * @return true if the class is float or double or BigDecimal
	 */
	public static boolean isDecimal(Class<?> cls) {
		return isFloat(cls) || isDouble(cls) || BigDecimal.class.equals(cls);
	}

	/**
	 * @return true if the class is long or Long
	 */
	public static boolean isLong(Class<?> cls) {
		return long.class.equals(cls) || Long.class.equals(cls);
	}

	/**
	 * @return true if the class is short or Short
	 */
	public static boolean isShort(Class<?> cls) {
		return short.class.equals(cls) || Short.class.equals(cls);
	}

	/**
	 * @return true if the class is byte or Byte
	 */
	public static boolean isByte(Class<?> cls) {
		return byte.class.equals(cls) || Byte.class.equals(cls);
	}

	/**
	 * @return true if the class is int, long, short, byte, BigInteger
	 */
	public static boolean isIntLike(Class<?> cls) {
		return cls != null && (isInt(cls) || isLong(cls) || isShort(cls) || isByte(cls) || BigInteger.class.equals(cls));
	}

	/**
	 * @return true if the class is a primitive number class
	 */
	public static boolean isPrimitiveNumber(Class<?> cls) {
		return cls != null && (isInt(cls) || isLong(cls) || isFloat(cls) || isDouble(cls) || isByte(cls) || isShort(cls));
	}

	/**
	 * @return true if the class is a number class
	 */
	public static boolean isNumber(Class<?> cls) {
		return cls != null && (Number.class.isAssignableFrom(cls) || isPrimitiveNumber(cls));
	}

	/**
	 * @return true if the class is a enum class
	 */
	public static boolean isEnum(Class<?> cls) {
		return cls != null && cls.isEnum();
	}

	/**
	 * @return true if the class is a interface class
	 */
	public static boolean isInterface(Class<?> cls) {
		return cls != null && cls.isInterface();
	}

	/**
	 * @return true if the class is Array, Map, Collection
	 */
	public static boolean isContainer(Class<?> cls) {
		return isArray(cls) || isCollection(cls) || isMap(cls);
	}

	/**
	 * @return true if the class is Array class
	 */
	public static boolean isArray(Class<?> cls) {
		return cls != null && cls.isArray();
	}

	/**
	 * @return true if the class is Collection class
	 */
	public static boolean isCollection(Class<?> cls) {
		return isAssignable(cls, Collection.class);
	}

	/**
	 * @return true if the class is Array class or Collection class
	 */
	public static boolean isArrayOrCollection(Class<?> cls) {
		return isArray(cls) || isCollection(cls);
	}

	/**
	 * @return true if the class is Map class
	 */
	public static boolean isMap(Class<?> cls) {
		return isAssignable(cls, Map.class);
	}

	/**
	 * @return true if the class is a Date or Calendar class
	 */
	public static boolean isDateTime(Class<?> cls) {
		return cls != null && (isAssignable(cls, Calendar.class) || isAssignable(cls, Date.class));
	}

	/**
	 * The immutable type is:
	 * enum, primitive type, primitive wrapper, all class inherit from
	 * CharSequence, Date, Calendar, Number 
	 *  
	 * @param cls class
	 * @return true if the class is a immutable type
	 */
	public static boolean isImmutable(Class<?> cls) {
		if (cls.isEnum()) {
			return true;
		}
		if (Classes.isPrimitiveOrWrapper(cls)) {
			return true;
		}
		
		for (Class<?> c : IMMUTABLE_TYPES) {
			if (c.isAssignableFrom(cls)) {
				return true;
			}
		}
		return false;
	}
	
	//----------------------------------------------------
	public static List<Class<?>> scan(String... packages) {
		if (packages == null) {
			throw new NullPointerException("packages is null!");
		}
		
		List<String> pkgs = new ArrayList<String>(packages.length);
		for (String p : packages) {
			if (Strings.isNotEmpty(p)) {
				p = Strings.replaceChars(p, '.', '/');
				if (p.charAt(0) == '/') {
					p = p.substring(1);
				}
				pkgs.add(p);
			}
		}

		if (Collections.isEmpty(pkgs)) {
			throw new IllegalArgumentException("No packages supplied!");
		}

		final Log log = Logs.getLog(Classes.class);
		
		List<Class<?>> clss = new ArrayList<Class<?>>();
		URL[] urls = ((URLClassLoader)ClassLoaders.getClassLoader()).getURLs();
		for (URL url : urls) {
			if (url.getFile().endsWith("jar")) {
				ZipInputStream zis = null;
				try {
					zis = Streams.zip(new FileInputStream(url.getFile()));
					ZipEntry ens = null;
					while (null != (ens = zis.getNextEntry())) {
						String cp = ens.getName();
						if (cp.endsWith(CLASS_FILE_SUFFIX)) {
							for (String pkg : pkgs) {
								if (cp.startsWith(pkg)) {
									String cn = Strings.replaceChars(Strings.substring(cp, 0, -6), '/', '.');
									try {
										clss.add(Classes.getClass(cn, false));
										break;
									}
									catch (ClassNotFoundException e) {
										log.warn("Failed to load class " + cp + " from " + url.getFile());
									}
								}
							}
						}
					}
				}
				catch (IOException e) {
					log.warn("Failed to open " + url.getFile());
				}
				finally {
					Streams.safeClose(zis);
				}
			}
			else {
				File dir = new File(url.getFile());
				Collection<File> files = Files.listFiles(dir, new String[] { CLASS_FILE_SUFFIX }, true);
				for (File file : files) {
					if (file.getName().equals(PACKAGE_INFO_CLASS)) {
						continue;
					}
					
					String cp = FileNames.getRelativePath(dir, file);
					for (String pkg : pkgs) {
						if (cp.startsWith(pkg)) {
							String cn = Strings.replaceChars(Strings.substring(cp, 0, -6), '/', '.');
							try {
								clss.add(Classes.getClass(cn, false));
								break;
							}
							catch (ClassNotFoundException e) {
								log.warn("Failed to load class " + file.getAbsolutePath());
							}
						}
					}
				}
			}
		}
		return clss;
	}

	/**
	 * 获取基本类型的默认值
	 * 
	 * @param cls 基本类型
	 * @return 0/false,如果传入的pClass不是基本类型的类,则返回null
	 */
	public static Object getPrimitiveDefaultValue(Class<?> cls) {
		if (int.class.equals(cls))
			return Integer.valueOf(0);
		if (long.class.equals(cls))
			return Long.valueOf(0);
		if (short.class.equals(cls))
			return Short.valueOf((short)0);
		if (float.class.equals(cls))
			return Float.valueOf(0f);
		if (double.class.equals(cls))
			return Double.valueOf(0);
		if (byte.class.equals(cls))
			return Byte.valueOf((byte)0);
		if (char.class.equals(cls))
			return Character.valueOf((char)0);
		if (boolean.class.equals(cls))
			return Boolean.FALSE;
		return null;
	}
}
