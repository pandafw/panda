package panda.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * utility class for access private field or method
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("unchecked")
public class PrivateAccessors {

	/** An empty class array */
	private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

	/** An empty object array */
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	/**
	 * Make the given field accessible, explicitly setting it accessible if necessary. The
	 * <code>setAccessible(true)</code> method is only called when actually necessary, to avoid
	 * unnecessary conflicts with a JVM SecurityManager (if active).
	 * 
	 * @param field the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void setAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * Make the given method accessible, explicitly setting it accessible if necessary. The
	 * <code>setAccessible(true)</code> method is only called when actually necessary, to avoid
	 * unnecessary conflicts with a JVM SecurityManager (if active).
	 * 
	 * @param method the method to make accessible
	 * @see java.lang.reflect.Method#setAccessible
	 */
	public static void setAccessible(Method method) {
		if (!Modifier.isPublic(method.getModifiers())
				|| !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}

	/**
	 * Make the given constructor accessible, explicitly setting it accessible if necessary. The
	 * <code>setAccessible(true)</code> constructor is only called when actually necessary, to avoid
	 * unnecessary conflicts with a JVM SecurityManager (if active).
	 * 
	 * @param constructor the constructor to make accessible
	 * @see java.lang.reflect.Method#setAccessible
	 */
	public static void setAccessible(Constructor constructor) {
		if (!Modifier.isPublic(constructor.getModifiers())) {
			constructor.setAccessible(true);
		}
	}

	/**
	 * new instance by the className
	 * @param className class name
	 * @return object instance
	 * @throws ClassNotFoundException if class not found
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 * @throws NoSuchMethodException if NoSuchMethodException occurs
	 * @throws SecurityException if SecurityException occurs
	 */
	public static Object newInstance(String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException {
		return newInstance(Classes.getClass(className));
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
	 * @throws NoSuchMethodException if NoSuchMethodException occurs
	 * @throws SecurityException if SecurityException occurs
	 */
	public static Object newInstance(String className, Object argValue, Class argType)
			throws InstantiationException, IllegalAccessException, InvocationTargetException,
			ClassNotFoundException, SecurityException, NoSuchMethodException {
		return newInstance(Classes.getClass(className), new Object[] { argValue }, new Class[] { argType });
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
	 * @throws NoSuchMethodException if NoSuchMethodException occurs
	 * @throws SecurityException if SecurityException occurs
	 */
	public static Object newInstance(String className, Object[] argValues, Class[] argTypes)
			throws InstantiationException, IllegalAccessException, InvocationTargetException,
			ClassNotFoundException, SecurityException, NoSuchMethodException {
		return newInstance(Classes.getClass(className), argValues, argTypes);
	}

	/**
	 * new instance by the clazz 
	 * @param clazz class
	 * @return object instance
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 * @throws NoSuchMethodException if NoSuchMethodException occurs
	 * @throws SecurityException if SecurityException occurs
	 */
	public static Object newInstance(Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		return newInstance(clazz, EMPTY_OBJECT_ARRAY, EMPTY_CLASS_ARRAY);
	}

	/**
	 * new instance by the clazz 
	 * @param clazz class
	 * @param argValue constructor argument value
	 * @param argType constructor argument type
	 * @return class instance
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 * @throws NoSuchMethodException if NoSuchMethodException occurs
	 * @throws SecurityException if SecurityException occurs
	 */
	public static Object newInstance(Class clazz, Object argValue, Class argType)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		return newInstance(clazz, new Object[] { argValue }, new Class[] { argType });
	}

	/**
	 * new instance by the clazz
	 * @param clazz class
	 * @return object instance
	 * @param argValues constructor arguments value
	 * @param argTypes constructor arguments type
	 * @throws InstantiationException if InstantiationException occurs 
	 * @throws IllegalAccessException if IllegalAccessException occurs 
	 * @throws InvocationTargetException if InvocationTargetException occurs
	 * @throws NoSuchMethodException if NoSuchMethodException occurs
	 * @throws SecurityException if SecurityException occurs
	 */
	public static Object newInstance(Class clazz, Object[] argValues, Class[] argTypes)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Constructor constructor = clazz.getDeclaredConstructor(argTypes);
		setAccessible(constructor);
		return constructor.newInstance(argValues);
	}

	/**
	 * get field of the specified clazz, if not found search clazz.getSuperClass().
	 * 
	 * @param clazz class
	 * @param fieldName field name
	 * @return accessible field
	 * @throws Exception if an error occurs
	 */
	public static Field getField(Class clazz, String fieldName) throws Exception {
		Exception te = new NoSuchFieldException(clazz.getClass() + ":" + fieldName);
		do {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				setAccessible(field);
				return field;
			}
			catch (Exception e) {
				te = e;
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);

		throw te;
	}
	
	/**
	 * get field of the specified object.
	 * 
	 * @param object object
	 * @param fieldName field name
	 * @return field
	 * @throws Exception if an error occurs
	 * @see #getField(Class, String)
	 */
	public static Field getField(Object object, String fieldName) throws Exception {
		return getField(object.getClass(), fieldName);
	}

	/**
	 * get field value
	 * 
	 * @param clazz class
	 * @param object object
	 * @param fieldName field name
	 * @return field field value
	 * @throws Exception  if an error occurs
	 */
	public static Object getExactFieldValue(Class clazz, Object object, String fieldName) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		setAccessible(field);
		return field.get(object);
	}

	/**
	 * get field value
	 * 
	 * @param object object
	 * @param fieldName field name
	 * @return field field value
	 * @throws Exception  if an error occurs
	 */
	public static Object getFieldValue(Object object, String fieldName) throws Exception {
		return getField(object, fieldName).get(object);
	}

	/**
	 * set field value
	 * 
	 * @param object object
	 * @param fieldName field name
	 * @param value field value
	 * @throws Exception  if an error occurs
	 */
	public static void setFieldValue(Object object, String fieldName, Object value)
			throws Exception {
		getField(object, fieldName).set(object, value);
	}

	/**
	 * get static field value
	 * 
	 * @param clazz class
	 * @param fieldName field name
	 * @return field field value  if an error occurs
	 * @throws Exception  if an error occurs
	 */
	public static Object getStaticFieldValue(Class clazz, String fieldName) throws Exception {
		return getField(clazz, fieldName).get(null);
	}

	/**
	 * set static field value
	 * 
	 * @param clazz class
	 * @param fieldName field name
	 * @param value field value
	 * @throws Exception  if an error occurs
	 */
	public static void setStaticFieldValue(Class clazz, String fieldName, Object value)
			throws Exception {
		getField(clazz, fieldName).set(null, value);
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than <code>invokeExactMethod()</code>. It
	 * loops through all methods with names that match and then executes the first it finds with
	 * compatable arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeMethod(Object object, String methodName) throws Exception {
		return invokeMethod(object, methodName, null);
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than <code>invokeExactMethod()</code>. It
	 * loops through all methods with names that match and then executes the first it finds with
	 * compatable arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeMethod(Object object, String methodName, Object arg)
			throws Exception {

		Object[] args = { arg };
		return invokeMethod(object, methodName, args);
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeMethod(Object object,String methodName,Object [] args)} . It loops through
	 * all methods with names that match and then executes the first it finds with compatable
	 * arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object [] args,Class[] argTypes)}
	 * .
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeMethod(Object object, String methodName, Object[] args)
			throws Exception {

		if (args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}
		int arguments = args.length;
		Class[] argTypes = new Class[arguments];
		for (int i = 0; i < arguments; i++) {
			argTypes[i] = args[i].getClass();
		}
		return invokeMethod(object, methodName, args, argTypes);

	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeMethod(Object object,String methodName,Object [] args,Class[] argTypes)}
	 * . It loops through all methods with names that match and then executes the first it finds
	 * with compatable arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param argTypes match these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeMethod(Object object, String methodName, Object[] args,
			Class[] argTypes) throws Exception {

		if (argTypes == null) {
			argTypes = EMPTY_CLASS_ARRAY;
		}
		if (args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}

		return getMethod(object, methodName, argTypes).invoke(object, args);
	}

	/**
	 * <p>
	 * Invoke a named static method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeMethod(Object, String, Object[], Class[])}. It loops through all methods
	 * with names that match and then executes the first it finds with compatable arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeStaticMethod(Class objectClass,String methodName,Object [] args)} .
	 * </p>
	 * 
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeStaticMethod(Class objectClass, String methodName) throws Exception {
		return invokeStaticMethod(objectClass, methodName, null);
	}

	/**
	 * <p>
	 * Invoke a named static method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeMethod(Object, String, Object[], Class[])}. It loops through all methods
	 * with names that match and then executes the first it finds with compatable arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeStaticMethod(Class objectClass,String methodName,Object [] args)} .
	 * </p>
	 * 
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeStaticMethod(Class objectClass, String methodName, Object arg)
			throws Exception {

		Object[] args = { arg };
		return invokeStaticMethod(objectClass, methodName, args);
	}

	/**
	 * <p>
	 * Invoke a named static method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeMethod(Object object,String methodName,Object [] args)} . It loops through
	 * all methods with names that match and then executes the first it finds with compatable
	 * arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeStaticMethod(Class objectClass,String methodName,Object [] args,Class[] argTypes)}
	 * .
	 * </p>
	 * 
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeStaticMethod(Class objectClass, String methodName, Object[] args)
			throws Exception {

		if (args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}
		int arguments = args.length;
		Class[] argTypes = new Class[arguments];
		for (int i = 0; i < arguments; i++) {
			argTypes[i] = args[i].getClass();
		}
		return invokeStaticMethod(objectClass, methodName, args, argTypes);
	}

	/**
	 * <p>
	 * Invoke a named static method whose parameter type matches the object type.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeStaticMethod(Class objectClass,String methodName,Object [] args,Class[] argTypes)}
	 * . It loops through all methods with names that match and then executes the first it finds
	 * with compatable arguments.
	 * </p>
	 * 
	 * <p>
	 * This method supports calls to methods taking primitive arguments via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * 
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param argTypes match these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * 
	 * @throws Exception if an error occurs
	 */
	public static Object invokeStaticMethod(Class objectClass, String methodName, Object[] args,
			Class[] argTypes) throws Exception {

		if (argTypes == null) {
			argTypes = EMPTY_CLASS_ARRAY;
		}
		if (args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}

		return getMethod(objectClass, methodName, argTypes).invoke(null, args);
	}

	/**
	 * <p>
	 * Find an accessible method that matches the given name and has compatible arguments.
	 * Compatible arguments mean that every method parameter is assignable from the given
	 * arguments. In other words, it finds a method with the given name that will take the
	 * arguments given.
	 * <p>
	 * 
	 * <p>
	 * This method is slightly undeterminstic since it loops through clazz.getSuperClass() 
	 * and return the first matching method.
	 * </p>
	 * 
	 * <p>
	 * This method is used by
	 * {@link #invokeMethod(Object object,String methodName,Object [] args,Class[] argTypes)}.
	 * 
	 * <p>
	 * This method can match primitive parameter by passing in wrapper classes. For example, a
	 * <code>Boolean</code> will match a primitive <code>boolean</code> parameter.
	 * 
	 * @param clazz find method in this class
	 * @param methodName find method with this name
	 * @param argTypes find method with compatible arguments
	 * @return The accessible method
	 * @throws Exception if an error occurs
	 */
	public static Method getMethod(Class clazz, String methodName, Class[] argTypes) throws Exception {
		Exception te;
		do {
			try {
				Method method = clazz.getDeclaredMethod(methodName, argTypes);
				setAccessible(method);
				return method;
			}
			catch (Exception e) {
				te = e;
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);

		throw te;
	}

	/**
	 * get method 
	 * 
	 * @param object find method in this object.getClass()
	 * @param methodName find method with this name
	 * @param argTypes find method with compatible arguments
	 * @return The accessible method
	 * @throws Exception if an error occurs
	 * @see #getMethod(Class, String, Class[])
	 */
	public static Method getMethod(Object object, String methodName, Class[] argTypes) throws Exception {
		return getMethod(object.getClass(), methodName, argTypes);
	}

}
