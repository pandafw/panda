package panda.ioc.loader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import panda.io.Streams;
import panda.ioc.IocException;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocParam;
import panda.ioc.meta.IocValue;
import panda.lang.Arrays;
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
				Set<String> as = new LinkedHashSet<String>();
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
		if (!Strings.isEmpty(iocBean.scope())) {
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
		if (Arrays.isNotEmpty(args)) {
			IocValue[] ivs = new IocValue[args.length];
			for (int i = 0; i < ivs.length; i++) {
				ivs[i] = convert(null, args[i]);
			}
			iocObject.setArgs(ivs);
		}
	}
	
	protected void setIocEvents(IocObject iocObject, IocBean iocBean) {
		// Events
		IocEventSet ies = new IocEventSet();
		ies.setCreate(iocBean.create());
		ies.setDepose(iocBean.depose());
		ies.setFetch(iocBean.fetch());
		if (ies.isNotEmpty()) {
			iocObject.setEvents(ies);
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

			IocValue iv;
			if (Arrays.isEmpty(inject.value())) {
				iv = new IocValue(IocValue.KIND_REF);
				if (Arrays.isEmpty(inject.type())) {
					iv.setValue(field.getType());
				}
				else {
					if (inject.type().length != 1) {
						throw new IocException("Multiple types defined at @IocInject field '" + field.getName() + "' of " + clazz);
					}
					iv.setValue(inject.type()[0]);
				}
			}
			else {
				if (inject.value().length != 1) {
					throw new IocException("Multiple values defined at @IocInject field '" + field.getName() + "' of " + clazz);
				}
				iv = convert(field.getType(), inject.value()[0]);
			}
			iv.setType(field.getType());
			iv.setRequired(inject.required());
			
			IocParam ip = new IocParam(iv);
			ip.setInjector(new FieldInjector(field));

			iocObject.addField(field.getName(), ip);
		}

		Collection<Method> methods = Methods.getAnnotationMethods(clazz, IocInject.class);
		for (Method method : methods) {
			IocInject inject = method.getAnnotation(IocInject.class);
			
			int m = method.getModifiers();
			if (Modifier.isAbstract(m)) {
				throw new IocException("Illegal @IocInject on abstract method '" + method.getName() + "' defined in " + clazz);
			}
			if (Modifier.isStatic(m)) {
				throw new IocException("Illegal @IocInject on static method '" + method.getName() + "' defined in " + clazz);
			}
			
			if (method.getParameterTypes().length < 1) {
				throw new IocException("Illegal @IocInject on non parameters method '" + method.getName() + "' defined in " + clazz);
			}

			String name = method.getName();
			if (name.startsWith("set") && name.length() > 3 && method.getParameterTypes().length == 1) {
				name = Strings.uncapitalize(name.substring(3));
			}
			
			if (iocObject.hasField(name)) {
				throw new IocException("Duplicate @IocInject method '" + method.getName() + "' defined in " + clazz);
			}

			IocParam ip = new IocParam();
			Class<?>[] pts = method.getParameterTypes();
			Class<?>[] its = pts;
			if (Arrays.isNotEmpty(inject.type())) {
				if (inject.type().length != pts.length) {
					throw new IocException("Incorrect @IocInject method '" + method.getName() + "' defined in " + clazz + ", paramater count does not equals @IocInject.type");
				}
				its = inject.type();
			}

			if (Arrays.isNotEmpty(inject.value())) {
				if (inject.value().length != pts.length) {
					throw new IocException("Incorrect @IocInject method '" + method.getName() + "' defined in " + clazz + ", paramater count does not equals @IocInject.type");
				}

				IocValue[] ivs = new IocValue[pts.length];
				for (int i = 0; i < ivs.length; i++) {
					ivs[i] = convert(pts[i], inject.value()[i]);
					ivs[i].setRequired(inject.required());
				}
				ip.setValues(ivs);
			}
			else {
				IocValue[] ivs = new IocValue[its.length];
				for (int i = 0; i < its.length; i++) {
					ivs[i] = new IocValue(IocValue.KIND_REF, pts[i], its[i], inject.required());
				}
				ip.setValues(ivs);
			}
			ip.setInjector(new MethodInjector(method));

			iocObject.addField(name, ip);
		}
	}
	
	protected void setIocFields(IocObject iocObject, Class<?> clazz, String[] fields) {
		if (fields != null && fields.length > 0) {
			for (String field : fields) {
				String desp = null;

				// name:description
				int c = field.indexOf(':');
				if (c > 0) {
					field = field.substring(0, c);
					desp = field.substring(c + 1);
				}
				
				if (iocObject.hasField(field)) {
					throw new IocException("Duplicate @IocInject field '" + field + "' defined in " + clazz);
				}

				try {
					Field f = Fields.getField(clazz, field, true);

					IocValue iv;
					if (Strings.isEmpty(desp)) {
						iv = new IocValue(IocValue.KIND_REF, f.getType(), f.getType());
					}
					else {
						iv = convert(f.getType(), desp);
					}

					IocParam ip = new IocParam(iv);
					ip.setInjector(new FieldInjector(f));
					iocObject.addField(field, ip);
				}
				catch (Exception e) {
					throw new IocException("Illegal @IocInject field '" + field + "' defined in " + clazz, e);
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
	
	protected IocValue convert(Type type, String s) {
		return Loaders.convert(IocValue.KIND_REF, type, s);
	}

}
