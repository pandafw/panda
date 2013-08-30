package panda.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import panda.bean.handlers.ArrayBeanHandler;
import panda.bean.handlers.AtomicBeanHandler;
import panda.bean.handlers.IterableBeanHandler;
import panda.bean.handlers.JavaBeanHandler;
import panda.bean.handlers.ListBeanHandler;
import panda.bean.handlers.MapBeanHandler;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class Beans {
	/**
	 * instance
	 */
	private static Beans me;

	static {
		try {
			me = (Beans)Classes.newInstance(
					Beans.class.getPackage().getName().toString()
					+ ".FastBeans");
		}
		catch (Throwable e) {
			me = new Beans();
		}
	}

	/**
	 * @return instance
	 */
	public static Beans me() {
		return me;
	}

	/**
	 * @return instance
	 */
	public static Beans getMe() {
		return me;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setMe(Beans instance) {
		Beans.me = instance;
	}

	// ------------------------------------------------------------------------
	public final static String[] RESERVED_PROPERTY_NAMES = { "class", "declaringClass", "metaClass" };
	public final static Class<?>[] RESERVED_PROPERTY_TYPES = { Class.class };
	
	public static boolean isReservedProperty(String propertyName) {
		return Arrays.contains(RESERVED_PROPERTY_NAMES, propertyName);
	}
	
	public static boolean isReservedProperty(Type propertyType) {
		return Arrays.contains(RESERVED_PROPERTY_TYPES, propertyType);
	}

	private static Class<?>[] ATOMIC_TYPES = {
		CharSequence.class,
		Date.class,
		Calendar.class,
		Number.class
	};
	
	public static boolean isAtomicType(Class<?> type) {
		if (type.isEnum()) {
			return true;
		}
		if (Classes.isPrimitiveOrWrapper(type)) {
			return true;
		}
		
		for (Class<?> c : ATOMIC_TYPES) {
			if (c.isAssignableFrom(type)) {
				return true;
			}
		}

		return false;
	}


	/**
	 * get bean name from method.
	 * the method must like getXXX, setXXX, isXXX
	 * 
	 * @param method method
	 * @return bean name if the method is a getter or setter
	 */
	public static String getBeanName(Method method) {
		String name = method.getName();

		if (name.startsWith("get") && method.getParameterTypes().length == 0) {
			name = Strings.uncapitalize(name.substring(3));
		}
		else if (name.startsWith("is") && Classes.isBoolean(method.getReturnType())
				&& method.getParameterTypes().length == 0) {
			name = Strings.uncapitalize(name.substring(2));
		}
		else if (name.startsWith("set") && method.getParameterTypes().length == 1) {
			name = Strings.uncapitalize(name.substring(3));
		}
		return null;
	}

	protected static void assertBeanAndName(Object bean, String name) {
		if (bean == null) {
			throw new IllegalArgumentException("argument bean is null.");
		}
		if (name == null) {
			throw new IllegalArgumentException("argument name is null.");
		}
	}
	
	/**
	 * <p>
	 * Return the value of the property of the specified name,
	 * for the specified bean, with no type conversions.
	 * </p>
	 * 
	 * @param bean Bean whose property is to be extracted
	 * @param name the property to be extracted
	 * @return the property value
	 * 
	 * @exception IllegalArgumentException if <code>bean</code> or
	 *                <code>name</code> is null
	 *                throws an exception
	 */
	@SuppressWarnings("unchecked")
	public static Object getBean(Object bean, String name) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = me().getBeanHandler(bean.getClass());

		return bh.getBeanValue(bean, name);
	}

	/**
	 * <p>
	 * set the value of the property of the specified name.
	 * for the specified bean, with no type conversions.
	 * </p>
	 * 
	 * @param bean Bean whose property is to be set
	 * @param name the property name 
	 * @param value the property value to be set
	 * 
	 * @exception IllegalArgumentException if <code>bean</code> or
	 *                <code>name</code> is null
	 *                throws an exception
	 */
	@SuppressWarnings("unchecked")
	public static void setBean(Object bean, String name, Object value) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = me().getBeanHandler(bean.getClass());

		bh.setBeanValue(bean, name, value);
	}

	/**
	 * <p>
	 * Return the value of the property of the specified name,
	 * for the specified bean, with no type conversions.
	 * </p>
	 * 
	 * @param bean Bean whose property is to be extracted
	 * @param name the property to be extracted
	 * @return the property value
	 * 
	 * @exception IllegalArgumentException if <code>bean</code> or
	 *                <code>name</code> is null
	 *                throws an exception
	 */
	@SuppressWarnings("unchecked")
	public static Object getProperty(Object bean, String name) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = me().getBeanHandler(bean.getClass());

		return bh.getPropertyValue(bean, name);
	}

	/**
	 * <p>
	 * set the value of the property of the specified name.
	 * for the specified bean, with no type conversions.
	 * </p>
	 * 
	 * @param bean Bean whose property is to be set
	 * @param name the property name 
	 * @param value the property value to be set
	 * 
	 * @exception IllegalArgumentException if <code>bean</code> or
	 *                <code>name</code> is null
	 *                throws an exception
	 */
	@SuppressWarnings("unchecked")
	public static void setProperty(Object bean, String name, Object value) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = me().getBeanHandler(bean.getClass());

		bh.setPropertyValue(bean, name, value);
	}

	// ------------------------------------------------------------------------
	/**
	 * handler map
	 */
	protected Map<Type, BeanHandler> handlers = new ConcurrentHashMap<Type, BeanHandler>();

	/**
	 * prepareBeanHandler
	 * 
	 * @param type class type
	 */
	public void prepareBeanHandler(Type type) {
		getBeanHandler(type);
	}
	
	/**
	 * prepareBeanHandler
	 * 
	 * @param types class type array
	 */
	public void prepareBeanHandler(Type[] types) {
		for (Type type : types) {
			prepareBeanHandler(type);
		}
	}

	/**
	 * prepareBeanHandler
	 * 
	 * @param types class type collection
	 */
	public void prepareBeanHandler(Collection<Type> types) {
		for (Type type : types) {
			prepareBeanHandler(type);
		}
	}

	/**
	 * Register (add) a bean handler for a class
	 * 
	 * @param type - the class
	 * @param handler - the handler instance
	 */
	public void register(Type type, BeanHandler handler) {
		handlers.put(type, handler);
	}
	
	/**
	 * Unregister (remove) a bean handler for a class
	 * 
	 * @param type - the class
	 */
	public void unregister(Type type) {
		handlers.remove(type);
	}
	
	/**
	 * clear bean handlers
	 */
	public void clear() {
		handlers.clear();
	}

	/**
	 * getBeanHandler
	 * @param type bean type
	 * @return BeanHandler
	 */
	public <T> BeanHandler<T> getBeanHandler(Class<T> type) {
		return getBeanHandler((Type)type);
	}
	
	/**
	 * getBeanHandler
	 * @param type bean type
	 * @return BeanHandler
	 */
	@SuppressWarnings("unchecked")
	public <T> BeanHandler<T> getBeanHandler(Type type) {
		if (type == null) {
			return new AtomicBeanHandler(Object.class); 
		}

		BeanHandler<T> handler = handlers.get(type);
		if (handler == null) {
			if (Types.isArrayType(type)) {
				handler = new ArrayBeanHandler(this, type);
			}
			else if (Types.isAssignable(type, Map.class)) {
				handler = new MapBeanHandler(this, type);
			}
			else if (Types.isAssignable(type, List.class)) {
				handler = new ListBeanHandler(this, type);
			}
			else if (Types.isAssignable(type, Iterable.class)) {
				handler = new IterableBeanHandler(this, type);
			}
			else if (isAtomicJavaType(type)) {
				handler = new AtomicBeanHandler(type); 
			}
			else {
				handler = createJavaBeanHandler(type);
				register(type, handler);
			}
		}
		return handler;
	}

	/**
	 * is primitive java type
	 * @param type class type
	 * @return true if the type is a simple java type
	 */
	protected boolean isAtomicJavaType(Type type) {
		Class clazz = Types.getRawType(type);
		return Beans.isAtomicType(clazz);
	}
	
	/**
	 * create java bean handler
	 * @param type bean type
	 * @return BeanHandler
	 */
	protected BeanHandler createJavaBeanHandler(Type type) {
		return new JavaBeanHandler(this, type);
	}
}
