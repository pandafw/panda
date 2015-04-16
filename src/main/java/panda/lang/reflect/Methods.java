package panda.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.collection.MultiKey;

/**
 * utility class for Method
 * 
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("unchecked")
public class Methods {
	/**
	 * @param method method
	 * @param index parameter index
	 * @return generic parameter type or class
	 */
	public static Type getParameterType(Method method, int index) {
		Type type = method.getGenericParameterTypes()[index];
		if (type == null) {
			type = method.getParameterTypes()[index];
		}
		return type;
	}
	
	/**
	 * Gets all methods of the given class and its parents (if any).
	 */
	public static Method[] getAllMethods(Class<?> cls) {
		final List<Method> all = getAllMethodsList(cls);
		return all.toArray(new Method[all.size()]);
	}

	/**
	 * Gets all methods of the given class and its parents (if any).
	 */
	public static List<Method> getAllMethodsList(Class<?> cls) {
		List<Method> list = new ArrayList<Method>();
		while (null != cls) {
			Method[] ms = cls.getDeclaredMethods();
			for (Method m : ms) {
				list.add(m);
			}
			cls = cls.getSuperclass();
		}
		return list;
	}

	/**
	 * call getDeclaredMethods(xx, null)
	 */
	public static Method[] getDeclaredMethods(Class<?> cls) {
		return getDeclaredMethods(cls, null);
	}

	/**
	 * call getDeclaredMethods(xx, Object.class)
	 */
	public static Method[] getDeclaredMethodsWithoutTop(Class<?> cls) {
		return getDeclaredMethods(cls, Object.class);
	}

	/**
	 * Gets declared methods of the given class and its parents until top class.
	 * Discard duplicate method of parents.
	 */
	public static Method[] getDeclaredMethods(Class<?> cls, Class<?> top) {
		Map<MultiKey, Method> map = new LinkedHashMap<MultiKey, Method>();
		List<Object> mi = new ArrayList<Object>();
		while (cls != top) {
			Method[] ms = cls.getDeclaredMethods();
			for (Method m : ms) {
				mi.clear();
				mi.add(m.getName());
				Collections.addAll(mi, m.getParameterTypes());
				
				MultiKey key = new MultiKey(mi.toArray());
				if (!map.containsKey(key)) {
					map.put(key, m);
				}
			}
			cls = cls.getSuperclass();
		}
		return map.values().toArray(new Method[map.size()]);
	}

	/**
	 * @param ann annotation
	 * @return methods
	 */
	public static <A extends Annotation> List<Method> getAnnotationMethods(Class<?> cls, Class<A> ann) {
		List<Method> methods = new ArrayList<Method>();
		for (Method m : getDeclaredMethods(cls)) {
			if (m.isAnnotationPresent(ann)) {
				methods.add(m);
			}
		}
		return methods;
	}

	/**
	 * <p>
	 * Invoke a named method.
	 * </p>
	 * 
	 * @param object object
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 */
	public static Object safeCall(Object object, String methodName) {
		try {
			return invokeMethod(object, methodName);
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than <code>invokeExactMethod()</code>. It
	 * loops through all methods with names that match and then executes the first it finds with
	 * compatable parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 */
	public static Object safeCall(Object object, String methodName, Object arg) {
		try {
			return invokeMethod(object, methodName, arg);
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeExactMethod(Object object,String methodName,Object [] args)}. It loops through
	 * all methods with names that match and then executes the first it finds with compatable
	 * parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object [] args,Class[] parameterTypes)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 */
	public static Object safeCall(Object object, String methodName, Object[] args) {
		try {
			return invokeMethod(object, methodName, args);
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeExactMethod(Object object,String methodName,Object [] args,Class[] parameterTypes)}
	 * . It loops through all methods with names that match and then executes the first it finds
	 * with compatable parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param parameterTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 */
	public static Object safeCall(Object object, String methodName, Object[] args, Class[] parameterTypes) {
		try {
			return invokeMethod(object, methodName, args, parameterTypes);
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Invoke a named method.
	 * </p>
	 * 
	 * @param object object
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeMethod(Object object, String methodName) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return invokeMethod(object, methodName, Arrays.EMPTY_OBJECT_ARRAY);
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than <code>invokeExactMethod()</code>. It
	 * loops through all methods with names that match and then executes the first it finds with
	 * compatable parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeMethod(Object object, String methodName, Object arg) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		return invokeMethod(object, methodName, args);
	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeExactMethod(Object object,String methodName,Object [] args)}. It loops through
	 * all methods with names that match and then executes the first it finds with compatable
	 * parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object [] args,Class[] parameterTypes)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeMethod(Object object, String methodName, Object[] args) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		args = Arrays.nullToEmpty(args);
		final Class<?>[] parameterTypes = Classes.toClass(args);
		return invokeMethod(object, methodName, args, parameterTypes);

	}

	/**
	 * <p>
	 * Invoke a named method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeExactMethod(Object object,String methodName,Object [] args,Class[] parameterTypes)}
	 * . It loops through all methods with names that match and then executes the first it finds
	 * with compatable parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param parameterTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeMethod(Object object, String methodName, Object[] args, Class[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		parameterTypes = Arrays.nullToEmpty(parameterTypes);
		args = Arrays.nullToEmpty(args);
		final Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
		if (method == null) {
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: "
					+ object.getClass().getName());
		}
		return method.invoke(object, args);
	}

	/**
	 * <p>
	 * Invoke a method whose parameter type matches exactly the object type.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeExactMethod(Object object,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactMethod(Object object, String methodName) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return invokeExactMethod(object, methodName, null);
	}

	/**
	 * <p>
	 * Invoke a method whose parameter type matches exactly the object type.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeExactMethod(Object object,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactMethod(Object object, String methodName, Object arg) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {

		Object[] args = { arg };
		return invokeExactMethod(object, methodName, args);

	}

	/**
	 * <p>
	 * Invoke a method whose parameter types match exactly the object types.
	 * </p>
	 * <p>
	 * This uses reflection to invoke the method obtained from a call to
	 * <code>getAccessibleMethod()</code>.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactMethod(Object object, String methodName, Object[] args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		args = Arrays.nullToEmpty(args);
		final Class<?>[] parameterTypes = Classes.toClass(args);
		return invokeExactMethod(object, methodName, args, parameterTypes);

	}

	/**
	 * <p>
	 * Invoke a method whose parameter types match exactly the parameter types given.
	 * </p>
	 * <p>
	 * This uses reflection to invoke the method obtained from a call to
	 * <code>getAccessibleMethod()</code>.
	 * </p>
	 * 
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param parameterTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		args = Arrays.nullToEmpty(args);
		parameterTypes = Arrays.nullToEmpty(parameterTypes);

		Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
		if (method == null) {
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: "
					+ object.getClass().getName());
		}
		return method.invoke(object, args);

	}

	/**
	 * <p>
	 * Invoke a static method whose parameter types match exactly the parameter types given.
	 * </p>
	 * <p>
	 * This uses reflection to invoke the method obtained from a call to
	 * {@link #getAccessibleMethod(Class, String, Class[])}.
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param parameterTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactStaticMethod(Class cls, String methodName, Object[] args,
			Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		args = Arrays.nullToEmpty(args);
		parameterTypes = Arrays.nullToEmpty(parameterTypes);

		Method method = getAccessibleMethod(cls, methodName, parameterTypes);
		if (method == null) {
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: "
					+ cls.getName());
		}
		return method.invoke(null, args);

	}

	/**
	 * <p>
	 * Invoke a named static method.
	 * </p>
	 * 
	 * @param method the method such as java.util.Calendar@getInstance
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 * @throws ClassNotFoundException if the class is not found
	 */
	public static Object invokeStaticMethod(String method) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {
		String[] ss = method.split("@");
		Class cls = Classes.getClass(ss[0]);
		return invokeStaticMethod(cls, ss[1]);
	}

	/**
	 * <p>
	 * Invoke a named static method.
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeStaticMethod(Class cls, String methodName) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return invokeStaticMethod(cls, methodName, new Object[0]);
	}

	/**
	 * <p>
	 * Invoke a named static method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeExactMethod(Object, String, Object[], Class[])}. It loops through all methods
	 * with names that match and then executes the first it finds with compatable parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeStaticMethod(Class cls,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeStaticMethod(Class cls, String methodName, Object arg)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		return invokeStaticMethod(cls, methodName, args);
	}

	/**
	 * <p>
	 * Invoke a named static method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeExactMethod(Object object,String methodName,Object [] args)}. It loops through
	 * all methods with names that match and then executes the first it finds with compatable
	 * parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeStaticMethod(Class cls,String methodName,Object [] args,Class[] parameterTypes)}
	 * .
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeStaticMethod(Class cls, String methodName, Object[] args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		args = Arrays.nullToEmpty(args);
		Class[] parameterTypes = Classes.toClass(args);
		return invokeStaticMethod(cls, methodName, args, parameterTypes);
	}

	/**
	 * <p>
	 * Invoke a named static method whose parameter type matches the object type.
	 * </p>
	 * <p>
	 * The behaviour of this method is less deterministic than
	 * {@link #invokeExactStaticMethod(Class cls,String methodName,Object [] args,Class[] parameterTypes)}
	 * . It loops through all methods with names that match and then executes the first it finds
	 * with compatable parameters.
	 * </p>
	 * <p>
	 * This method supports calls to methods taking primitive parameters via passing in wrapping
	 * classes. So, for example, a <code>Boolean</code> class would match a <code>boolean</code>
	 * primitive.
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param parameterTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeStaticMethod(Class cls, String methodName, Object[] args, final Class[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		args = Arrays.nullToEmpty(args);

		Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
		if (method == null) {
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: "
					+ cls.getName());
		}
		return method.invoke(null, args);
	}

	/**
	 * <p>
	 * Invoke a static method whose parameter type matches exactly the object type.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeExactStaticMethod(Class cls,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactStaticMethod(Class cls, String methodName)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeExactStaticMethod(cls, methodName, null, null);
	}

	/**
	 * <p>
	 * Invoke a static method whose parameter type matches exactly the object type.
	 * </p>
	 * <p>
	 * This is a convenient wrapper for
	 * {@link #invokeExactStaticMethod(Class cls,String methodName,Object [] args)}.
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactStaticMethod(Class cls, String methodName, Object arg)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		return invokeExactStaticMethod(cls, methodName, args);
	}

	/**
	 * <p>
	 * Invoke a static method whose parameter types match exactly the object types.
	 * </p>
	 * <p>
	 * This uses reflection to invoke the method obtained from a call to
	 * {@link #getAccessibleMethod(Class, String, Class[])}.
	 * </p>
	 * 
	 * @param cls invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactStaticMethod(Class cls, String methodName, Object[] args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		args = Arrays.nullToEmpty(args);
		final Class<?>[] parameterTypes = Classes.toClass(args);
		return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
	}

	/**
	 * <p>
	 * Return an accessible method (that is, one that can be invoked via reflection) with given name
	 * and parameters. If no such method can be found, return <code>null</code>. This is just a
	 * convenient wrapper for {@link #getAccessibleMethod(Method method)}.
	 * </p>
	 * 
	 * @param cls get method from this class
	 * @param methodName get method with this name
	 * @param parameterTypes with these parameters types
	 * @return The accessible method
	 */
	public static Method getAccessibleMethod(final Class cls, final String methodName, final Class ... parameterTypes) {
		try {
			return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
		}
		catch (final NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Return an accessible method (that is, one that can be invoked via reflection) that implements
	 * the specified Method. If no such method can be found, return <code>null</code>.
	 * </p>
	 * 
	 * @param method The method that we wish to call
	 * @return The accessible method
	 */
	public static Method getAccessibleMethod(Method method) {
		if (!Members.isAccessible(method)) {
			return null;
		}
		// If the declaring class is public, we are done
		final Class<?> cls = method.getDeclaringClass();
		if (Modifier.isPublic(cls.getModifiers())) {
			return method;
		}
		final String methodName = method.getName();
		final Class<?>[] parameterTypes = method.getParameterTypes();

		// Check the implemented interfaces and subinterfaces
		method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);

		// Check the superclass chain
		if (method == null) {
			method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
		}
		return method;
	}

	// -------------------------------------------------------- Private Methods

	/**
	 * <p>
	 * Return an accessible method (that is, one that can be invoked via reflection) by scanning
	 * through the superclasses. If no such method can be found, return <code>null</code>.
	 * </p>
	 * 
	 * @param cls Class to be checked
	 * @param methodName Method name of the method we wish to call
	 * @param parameterTypes The parameter type signatures
	 */
	private static Method getAccessibleMethodFromSuperclass(Class cls, String methodName, Class[] parameterTypes) {

		Class parentClazz = cls.getSuperclass();
		while (parentClazz != null) {
			if (Modifier.isPublic(parentClazz.getModifiers())) {
				try {
					return parentClazz.getMethod(methodName, parameterTypes);
				}
				catch (NoSuchMethodException e) {
					return null;
				}
			}
			parentClazz = parentClazz.getSuperclass();
		}
		return null;
	}

	/**
	 * <p>
	 * Return an accessible method (that is, one that can be invoked via reflection) that implements
	 * the specified method, by scanning through all implemented interfaces and subinterfaces. If no
	 * such method can be found, return <code>null</code>.
	 * </p>
	 * <p>
	 * There isn't any good reason why this method must be private. It is because there doesn't seem
	 * any reason why other classes should call this rather than the higher level methods.
	 * </p>
	 * 
	 * @param cls Parent class for the interfaces to be checked
	 * @param methodName Method name of the method we wish to call
	 * @param parameterTypes The parameter type signatures
	 */
	private static Method getAccessibleMethodFromInterfaceNest(Class cls, String methodName, Class[] parameterTypes) {
		// Search up the superclass chain
		for (; cls != null; cls = cls.getSuperclass()) {

			// Check the implemented interfaces of the parent class
			Class[] interfaces = cls.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {

				// Is this interface public?
				if (!Modifier.isPublic(interfaces[i].getModifiers())) {
					continue;
				}

				// Does the method exist on this interface?
				try {
					return interfaces[i].getDeclaredMethod(methodName, parameterTypes);
				}
				catch (NoSuchMethodException e) {
					/*
					 * Swallow, if no method is found after the loop then this method returns null.
					 */
				}

				// Recursively check our parent interfaces
				Method method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
				if (method != null) {
					return method;
				}

			}
		}

		// We did not find anything
		return null;
	}

	public static Method getMatchingAccessibleMethod(Class cls, final String methodName,
			final Object... args) {
		Class<?>[] pts = Classes.toClass(args);
		return getMatchingAccessibleMethod(cls, methodName, pts);
	}
	
	/**
	 * <p>
	 * Find an accessible method that matches the given name and has compatible parameters.
	 * Compatible parameters mean that every method parameter is assignable from the given
	 * parameters. In other words, it finds a method with the given name that will take the
	 * parameters given.
	 * <p>
	 * <p>
	 * This method is slightly undeterminstic since it loops through methods names and return the
	 * first matching method.
	 * </p>
	 * <p>
	 * This method is used by
	 * {@link #invokeMethod(Object object,String methodName,Object [] args,Class[] parameterTypes)}.
	 * <p>
	 * This method can match primitive parameter by passing in wrapper classes. For example, a
	 * <code>Boolean</code> will match a primitive <code>boolean</code> parameter.
	 * 
	 * @param cls find method in this class
	 * @param methodName find method with this name
	 * @param parameterTypes find method with compatible parameters
	 * @return The accessible method
	 */
	public static Method getMatchingAccessibleMethod(Class cls, final String methodName,
			final Class<?>... parameterTypes) {
		try {
			final Method method = cls.getMethod(methodName, parameterTypes);
			Members.setAccessibleWorkaround(method);
			return method;
		}
		catch (final NoSuchMethodException e) { // NOPMD - Swallow the exception
		}
		// search through all methods
		Method bestMatch = null;
		final Method[] methods = cls.getMethods();
		for (final Method method : methods) {
			// compare name and parameters
			if (method.getName().equals(methodName)
					&& Classes.isAssignable(parameterTypes, method.getParameterTypes(), true)) {
				// get accessible version of method
				final Method accessibleMethod = getAccessibleMethod(method);
				if (accessibleMethod != null
						&& (bestMatch == null || Members.compareParameterTypes(
							accessibleMethod.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0)) {
					bestMatch = accessibleMethod;
				}
			}
		}
		if (bestMatch != null) {
			Members.setAccessibleWorkaround(bestMatch);
		}
		return bestMatch;
	}

	public static Method getMatchingAccessibleMethod(Class cls, final String methodName,
			final int parameterLength) {

		// search through all methods
		Method match = null;
		final Method[] methods = cls.getMethods();
		for (final Method method : methods) {
			// compare name and parameters
			if (method.getName().equals(methodName)
					&& parameterLength == method.getParameterTypes().length) {
				// get accessible version of method
				match = getAccessibleMethod(method);
				if (match != null) {
					break;
				}
			}
		}

		if (match != null) {
			Members.setAccessibleWorkaround(match);
		}
		return match;
	}
}
