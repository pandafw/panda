package panda.ioc.loader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import panda.io.Streams;
import panda.ioc.IocException;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.reflect.FieldInjector;
import panda.lang.reflect.Fields;
import panda.lang.reflect.MethodInjector;
import panda.lang.reflect.Methods;
import panda.log.Log;
import panda.log.Logs;

/**
 * Annotation Ioc Loader
 */
public class AnnotationIocLoader extends AbstractIocLoader {

	private static final Log log = Logs.getLog(AnnotationIocLoader.class);

	protected AnnotationIocLoader() {
	}

	public AnnotationIocLoader(String... args) {
		this((Object[])args);
	}
	
	public AnnotationIocLoader(Object... args) {
		init(Arrays.asList(args));
	}
	
	public AnnotationIocLoader(Collection<Object> args) {
		init(args);
	}
	
	protected void init(Collection<Object> args) {
		if (Collections.isNotEmpty(args)) {
			for (Object a : args) {
				if (a instanceof Class) {
					addClass((Class<?>)a);
				}
				else {
					for (Class<?> cls : Classes.scan(a.toString())) {
						addClass(cls);
					}
				}
			}

			if (log.isInfoEnabled()) {
				TreeSet<String> as = new TreeSet<String>();
				for (Object a : args) {
					as.add(String.valueOf(a));
				}
				log.info("Successfully scan/add " + args.size() + " args:" 
						+ Streams.EOL + " - "
						+ Strings.join(as, Streams.EOL + " - "));
			}
		}
		
		if (beans.size() > 0) {
			if (log.isInfoEnabled()) {
				log.info("Found " + beans.size() + " bean classes:" 
						+ Streams.EOL + " - "
						+ Strings.join(new TreeSet<String>(beans.keySet()), Streams.EOL + " - "));
			}
		}
		else {
			log.warn("NONE Annotation-Class found!\nCheck your configure for packages:" 
					+ Streams.EOL + " - "
					+ Strings.join(args, Streams.EOL + " - "));
		}
	}
	
	protected void addClass(Class<?> clazz) {
		if (log.isTraceEnabled()) {
			log.trace("Check " + clazz);
		}
		
		if (clazz.isMemberClass() || clazz.isEnum() || clazz.isAnnotation() || clazz.isAnonymousClass()) {
			return;
		}
		
		int modify = clazz.getModifiers();
		if (!Modifier.isPublic(modify)) {
			return;
		}
		
		if (isNeedCheckInject(clazz)) {
			checkInject(clazz);
			return;
		}

		String beanName = getBeanName(clazz);

		if (log.isDebugEnabled()) {
			log.debug("Found a @IocBean of " + clazz);
		}
		
		try {
			IocObject iocObject = createIocObject(clazz);
			if (iocObject != null) {
				IocObject old = beans.put(beanName, iocObject);
				if (old != null) {
					if (log.isWarnEnabled()) {
						log.warn("IocBean(" + beanName + ") is replaced by " + clazz);
					}
				}
			}
		}
		catch (SecurityException e) {
			log.warn("Failed to createIocObject(" + clazz + "): " + e.getMessage());
		}
		catch (NoClassDefFoundError e) {
			log.warn("Failed to createIocObject(" + clazz + "): " + e.getMessage());
		}
	}
	
	protected IocObject createIocObject(Class<?> clazz) {
		IocBean iocBean = clazz.getAnnotation(IocBean.class);
		if (iocBean == null) {
			return null;
		}

		IocObject iocObject = new IocObject();
		iocObject.setType(clazz);

		iocObject.setSingleton(iocBean.singleton());
		if (!Strings.isBlank(iocBean.scope())) {
			iocObject.setScope(iocBean.scope());
		}

		setIocArgs(iocObject, iocBean.args());
		setIocEvents(iocObject, iocBean);
		setIocInjects(iocObject, clazz);
		setIocFields(iocObject, clazz, iocBean.fields());

		// factory
		if (Strings.isNotEmpty(iocBean.factory())) {
			iocObject.setFactory(iocBean.factory());
		}

		// check interface
		if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
			if (iocBean.type() != Object.class) {
				iocObject.setType(iocBean.type());
			}
			if (Strings.isEmpty(iocBean.factory())) {
				throw new IocException("Missing factory for ioc bean: " + clazz);
			}
		}
		return iocObject;
	}

	protected void setIocArgs(IocObject iocObject, String[] args) {
		if (null != args && args.length > 0) {
			for (String value : args) {
				iocObject.addArg(convert(value));
			}
		}
	}
	
	protected void setIocEvents(IocObject iocObject, IocBean iocBean) {
		// Events
		IocEventSet eventSet = new IocEventSet();
		iocObject.setEvents(eventSet);
		if (Strings.isNotBlank(iocBean.create())) {
			eventSet.setCreate(iocBean.create().trim().intern());
		}
		if (Strings.isNotBlank(iocBean.depose())) {
			eventSet.setDepose(iocBean.depose().trim().intern());
		}
		if (Strings.isNotBlank(iocBean.fetch())) {
			eventSet.setFetch(iocBean.fetch().trim().intern());
		}
	}

	protected void checkInject(Class<?> clazz) {
		// check @IocInject without @IocBean
		try {
			if (log.isWarnEnabled()) {
				StringBuilder sb = new StringBuilder();

				Collection<Field> fields = Fields.getAnnotationFields(clazz, IocInject.class);
				for (Field f : fields) {
					sb.append(Streams.EOL)
					  .append(" - ")
					  .append(f.getName())
					  .append(" of ")
					  .append(f.getDeclaringClass());
				}

				Collection<Method> methods = Methods.getAnnotationMethods(clazz, IocInject.class);
				for (Method m : methods) {
					sb.append(Streams.EOL)
					  .append(" - ")
					  .append(m.getName())
					  .append("() of ")
					  .append(m.getDeclaringClass());
				}
				
				if (sb.length() > 0) {
					sb.insert(0, String.format("class(%s) don't has @IocBean, but some fields/methods has @IocInject! Miss @IocBean ??", clazz.getName()));
					log.warn(sb.toString());
				}
			}
		}
		catch (Throwable e) {
			// skip.
		}
	}
	
	protected void setIocInjects(IocObject iocObject, Class<?> clazz) {
		Collection<Field> fields = Fields.getAnnotationFields(clazz, IocInject.class);
		for (Field field : fields) {
			IocInject inject = field.getAnnotation(IocInject.class);

			IocValue iocValue;
			if (Strings.isBlank(inject.value())) {
				iocValue = new IocValue(IocValue.TYPE_REF);
				iocValue.setValue(Object.class.equals(inject.type()) ? field.getType() : inject.type());
			}
			else {
				iocValue = convert(inject.value());
			}
			iocValue.setRequired(inject.required());
			iocValue.setInjector(new FieldInjector(field));
			
			iocObject.addField(field.getName(), iocValue);
		}

		Collection<Method> methods = Methods.getAnnotationMethods(clazz, IocInject.class);
		for (Method method : methods) {
			IocInject inject = method.getAnnotation(IocInject.class);
			
			int m = method.getModifiers();
			if (Modifier.isAbstract(m) || Modifier.isStatic(m)) {
				continue;
			}
			
			String methodName = method.getName();
			if (methodName.startsWith("set") && methodName.length() > 3 && method.getParameterTypes().length == 1) {
				String name = Strings.uncapitalize(methodName.substring(3));
				if (iocObject.hasField(name)) {
					throw duplicateField(clazz, name);
				}
				
				IocValue iocValue;
				if (Strings.isBlank(inject.value())) {
					iocValue = new IocValue(IocValue.TYPE_REF);
					iocValue.setValue(Strings.uncapitalize(methodName.substring(3)));
					iocValue.setValue(Object.class.equals(inject.type()) ? method.getParameterTypes()[0] : inject.type());
				}
				else {
					iocValue = convert(inject.value());
				}
				iocValue.setRequired(inject.required());
				iocValue.setInjector(new MethodInjector(method));

				iocObject.addField(name, iocValue);
			}
		}
	}
	
	protected void setIocFields(IocObject iocObject, Class<?> clazz, String[] fields) {
		if (fields != null && fields.length > 0) {
			for (String field : fields) {
				if (iocObject.hasField(field)) {
					throw duplicateField(clazz, field);
				}

				int c = field.indexOf(':');
				if (c > 0) {
					// name:description
					iocObject.addField(field.substring(0, c), convert(field.substring(c + 1)));
				}
				else {
					// name only (use field type)
					Field f = Fields.getField(clazz, field, true);
					IocValue iocValue = new IocValue(IocValue.TYPE_REF);
					iocValue.setValue(f.getType());
					iocObject.addField(field, iocValue);
				}
			}
		}
	}
	
	protected boolean isNeedCheckInject(Class<?> cls) {
		return !cls.isInterface() 
				&& !Modifier.isAbstract(cls.getModifiers()) 
				&& !cls.isAnnotationPresent(IocBean.class);
	}
	
	protected String getBeanName(Class<?> cls) {
		return getBeanName(cls, cls.getAnnotation(IocBean.class));
	}
	
	public static String getBeanName(Class<?> cls, IocBean iocBean) {
		if (iocBean == null) {
			return cls.getName();
		}
		
		String bn = iocBean.name();
		if (Strings.isEmpty(bn)) {
			bn = iocBean.value();
			if (Strings.isEmpty(bn)) {
				Class<?> cn = iocBean.type();
				if (cn == Object.class) {
					bn = cls.getName();
				}
				else {
					bn = cn.getName();
				}
			}
		}
		return bn;
	}
	
	private static final IocException duplicateField(Class<?> clazz, String name) {
		return new IocException("Duplicate field '" + name + "' defined in " + clazz);
	}

	protected IocValue convert(String value) {
		return Loaders.convert(value, IocValue.TYPE_REF);
	}

}
