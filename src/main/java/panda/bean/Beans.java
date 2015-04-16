package panda.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import panda.bean.handler.ArrayBeanHandler;
import panda.bean.handler.CollectionBeanHandler;
import panda.bean.handler.IocProxyBeanHandler;
import panda.bean.handler.IterableBeanHandler;
import panda.bean.handler.JavaBeanHandler;
import panda.bean.handler.ListBeanHandler;
import panda.bean.handler.MapBeanHandler;
import panda.ioc.IocProxy;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.lang.reflect.Fields;
import panda.lang.reflect.Methods;
import panda.lang.reflect.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class Beans {
	private static Map<Class<?>, Map<String, PropertyAccessor>> accessors = new HashMap<Class<?>, Map<String, PropertyAccessor>>();
	
	/**
	 * instance
	 */
	private static Beans i;

	static {
		try {
			i = (Beans)Classes.newInstance(Beans.class.getPackage().getName() + ".FastBeans");
		}
		catch (Throwable e) {
			i = new Beans();
		}
	}

	/**
	 * @return singleton instance
	 */
	public static Beans i() {
		return i;
	}

	/**
	 * @return instance
	 */
	public static Beans getInstance() {
		return i;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(Beans instance) {
		Beans.i = instance;
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
		
		BeanHandler bh = i().getBeanHandler(bean.getClass());

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
	public static boolean setBean(Object bean, String name, Object value) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = i().getBeanHandler(bean.getClass());

		return bh.setBeanValue(bean, name, value);
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
		
		BeanHandler bh = i().getBeanHandler(bean.getClass());

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
	public static boolean setProperty(Object bean, String name, Object value) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = i().getBeanHandler(bean.getClass());

		return bh.setPropertyValue(bean, name, value);
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
			throw new NullPointerException("type is null");
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
			else if (Types.isAssignable(type, Collection.class)) {
				handler = new CollectionBeanHandler(this, type);
			}
			else if (Types.isAssignable(type, Iterable.class)) {
				handler = new IterableBeanHandler(this, type);
			}
			else if (Types.isAssignable(type, IocProxy.class)) {
				handler = new IocProxyBeanHandler(this, type);
				register(type, handler);
			}
			else if (Types.isImmutableType(type)) {
				throw new IllegalArgumentException("Illegal bean type: " + type);
			}
			else {
				handler = createJavaBeanHandler(type);
				register(type, handler);
			}
		}
		return handler;
	}

	/**
	 * create java bean handler
	 * @param type bean type
	 * @return BeanHandler
	 */
	protected BeanHandler createJavaBeanHandler(Type type) {
		return new JavaBeanHandler(this, type);
	}
	
	private static void setField(Map<String, PropertyAccessor> accessors, Field field) {
		String name = field.getName();

		Type type = Fields.getFieldType(field);

		if (!field.isAccessible()) {
			field.setAccessible(true);
		}

		PropertyAccessor pa = new PropertyAccessor();
		pa.getter = field;
		if (!Modifier.isFinal(field.getModifiers())) {
			pa.setter = field;
		}
		pa.type = type;
		accessors.put(name, pa);
	}
	
	private static void setGetter(Map<String, PropertyAccessor> accessors, String name, Method getter) {
		Type type = getter.getGenericReturnType();
		if (type == null) {
			type = getter.getReturnType();
		}

		PropertyAccessor pa = accessors.get(name);
		if (pa == null) {
			if (!getter.isAccessible()) {
				getter.setAccessible(true);
			}
			pa = new PropertyAccessor();
			pa.getter = getter;
			pa.type = type;
			accessors.put(name, pa);
		}
		else if (pa.getter == null) {
			if (Types.getRawType(type) == Types.getRawType(pa.type)) {
				if (!getter.isAccessible()) {
					getter.setAccessible(true);
				}
				pa.getter = getter;
			}
		}
	}
	
	private static void setSetter(Map<String, PropertyAccessor> accessors, String name, Method setter) {
		Type type = Methods.getParameterType(setter, 0);
		
		PropertyAccessor pa = accessors.get(name);
		if (pa == null) {
			if (!setter.isAccessible()) {
				setter.setAccessible(true);
			}
			pa = new PropertyAccessor();
			pa.setter = setter;
			pa.type = type;
			accessors.put(name, pa);
		}
		else if (pa.setter == null) {
			if (Types.getRawType(type) == Types.getRawType(pa.type)) {
				if (!setter.isAccessible()) {
					setter.setAccessible(true);
				}
				setter.setAccessible(true);
				pa.setter = setter;
			}
		}
	}
	
	/**
	 * get property accessor map according to the specified class
	 * @param clazz class
	 * @return accessor map
	 */
	public static Map<String, PropertyAccessor> getPropertyAccessors(Class<?> clazz) {
		Map<String, PropertyAccessor> pas = accessors.get(clazz);
		if (pas != null) {
			return pas;
		}

		synchronized(accessors) {
			pas = accessors.get(clazz);
			if (pas != null) {
				return pas;
			}
			pas = resolvePropertyAccessors(clazz);
			accessors.put(clazz, pas);
			return pas;
		}
	}

	public static Map<String, PropertyAccessor> resolvePropertyAccessors(Class<?> clazz) {
		Map<String, PropertyAccessor> pas = new TreeMap<String, PropertyAccessor>();

		Field[] fields = clazz.getFields();
		for (Field f : fields) {
			int m = f.getModifiers();
			if (f.isSynthetic() || Modifier.isStatic(m)) {
				continue;
			}
			setField(pas, f);
		}
		
		Method[] methods = clazz.getMethods();

		// is
		for (Method m : methods) {
			if (Modifier.isStatic(m.getModifiers())) {
				continue;
			}
			
			if (boolean.class != m.getReturnType()) {
				continue;
			}

			Type[] pts = m.getGenericParameterTypes();
			if (pts != null && pts.length != 0) {
				continue;
			}

			if (!m.getName().startsWith("is")) {
				continue;
			}
			
			String n = m.getName().substring(2);
			if (n.isEmpty() || Character.isLowerCase(n.charAt(0))) {
				continue;
			}

			n = Character.toLowerCase(n.charAt(0)) + n.substring(1);
			setGetter(pas, n, m);
		}
		
		// getter
		for (Method m : methods) {
			if (Modifier.isStatic(m.getModifiers())) {
				continue;
			}
			
			Type[] pts = m.getGenericParameterTypes();
			if (pts != null && pts.length != 0) {
				continue;
			}

			if (!m.getName().startsWith("get")) {
				continue;
			}
			
			String n = m.getName().substring(3);
			if (n.isEmpty() || Character.isLowerCase(n.charAt(0))) {
				continue;
			}

			n = Character.toLowerCase(n.charAt(0)) + n.substring(1);
			setGetter(pas, n, m);
		}
		
		// setter
		for (Method m : methods) {
			if (Modifier.isStatic(m.getModifiers())) {
				continue;
			}
			
			Type[] pts = m.getGenericParameterTypes();
			if (pts == null || pts.length != 1) {
				continue;
			}

			if (!m.getName().startsWith("set")) {
				continue;
			}

			String n = m.getName().substring(3);
			if (n.isEmpty() || Character.isLowerCase(n.charAt(0))) {
				continue;
			}
			
			n = Character.toLowerCase(n.charAt(0)) + n.substring(1);
			setSetter(pas, n, m);
		}
		
		return pas;
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
	public Object getBeanValue(Object bean, String name) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = getBeanHandler(bean.getClass());

		return bh.getBeanValue(bean, name);
	}

	@SuppressWarnings("unchecked")
	public Type getBeanType(Object bean, String name) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = getBeanHandler(bean.getClass());

		return bh.getBeanType(bean, name);
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
	public boolean setBeanValue(Object bean, String name, Object value) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = getBeanHandler(bean.getClass());

		return bh.setBeanValue(bean, name, value);
	}

	@SuppressWarnings("unchecked")
	public Type getPropertyType(Object bean, String name) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = getBeanHandler(bean.getClass());

		return bh.getPropertyType(bean, name);
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
	public Object getPropertyValue(Object bean, String name) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = getBeanHandler(bean.getClass());

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
	public boolean setPropertyValue(Object bean, String name, Object value) {
		assertBeanAndName(bean, name);
		
		BeanHandler bh = getBeanHandler(bean.getClass());

		return bh.setPropertyValue(bean, name, value);
	}

	@SuppressWarnings("unchecked")
	public void copyProperties(Object des, Object src, String ... props) {
		if (src == null || des == null || props == null || props.length == 0) {
			return;
		}
		
		BeanHandler sbh = getBeanHandler(src.getClass());
		BeanHandler dbh = getBeanHandler(des.getClass());
		for (String p : props) {
			Object v = sbh.getPropertyValue(src, p);
			dbh.setPropertyValue(des, p, v);
		}
	}

	@SuppressWarnings("unchecked")
	public void copyNotNullProperties(Object des, Object src, String ... props) {
		if (src == null || des == null || props == null || props.length == 0) {
			return;
		}
		
		BeanHandler sbh = getBeanHandler(src.getClass());
		BeanHandler dbh = getBeanHandler(des.getClass());
		for (String p : props) {
			Object v = sbh.getPropertyValue(src, p);
			if (v != null) {
				dbh.setPropertyValue(des, p, v);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void copyBeans(Object des, Object src, String ... props) {
		if (src == null || des == null || props == null || props.length == 0) {
			return;
		}
		
		BeanHandler sbh = getBeanHandler(src.getClass());
		BeanHandler dbh = getBeanHandler(des.getClass());
		for (String p : props) {
			Object v = sbh.getBeanValue(src, p);
			dbh.setBeanValue(des, p, v);
		}
	}

	@SuppressWarnings("unchecked")
	public void copyNotNullBeans(Object des, Object src, String ... props) {
		if (src == null || des == null || props == null || props.length == 0) {
			return;
		}
		
		BeanHandler sbh = getBeanHandler(src.getClass());
		BeanHandler dbh = getBeanHandler(des.getClass());
		for (String p : props) {
			Object v = sbh.getBeanValue(src, p);
			if (v != null) {
				dbh.setBeanValue(des, p, v);
			}
		}
	}
}
