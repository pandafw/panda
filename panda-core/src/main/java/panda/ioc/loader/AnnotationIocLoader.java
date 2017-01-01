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
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.FieldInjector;
import panda.lang.reflect.Fields;
import panda.lang.reflect.MethodInjector;
import panda.lang.reflect.Methods;
import panda.log.Log;
import panda.log.Logs;

/**
 * 基于注解的Ioc配置
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
						+ Streams.LINE_SEPARATOR + " - "
						+ Strings.join(as, Streams.LINE_SEPARATOR + " - "));
			}
		}
		
		if (beans.size() > 0) {
			if (log.isInfoEnabled()) {
				log.info("Found " + beans.size() + " bean classes:" 
						+ Streams.LINE_SEPARATOR + " - "
						+ Strings.join(new TreeSet<String>(beans.keySet()), Streams.LINE_SEPARATOR + " - "));
			}
		}
		else {
			log.warn("NONE Annotation-Class found!\nCheck your configure for packages:" 
					+ Streams.LINE_SEPARATOR + " - "
					+ Strings.join(args, Streams.LINE_SEPARATOR + " - "));
		}
	}
	
	protected void addClass(Class<?> clazz) {
		if (log.isTraceEnabled()) {
			log.trace("Check " + clazz);
		}
		
		if (clazz.isInterface() || clazz.isMemberClass() || clazz.isEnum() || clazz.isAnnotation()
				|| clazz.isAnonymousClass()) {
			return;
		}
		
		int modify = clazz.getModifiers();
		if (Modifier.isAbstract(modify) || (!Modifier.isPublic(modify))) {
			return;
		}
		
		if (!isBeanAnnotated(clazz)) {
			checkInject(clazz);
			return;
		}

		String beanName = getBeanName(clazz);
		if (beans.containsKey(beanName)) {
			throw new IocException(String.format("Duplicate beanName=%s, by %s !!  Have been define by %s !!",
				beanName, clazz, beans.get(beanName).getType()));
		}

		if (log.isDebugEnabled()) {
			log.debug("Found a @IocBean of " + clazz);
		}
		
		try {
			IocObject iocObject = createIocObject(clazz);
			if (iocObject != null) {
				beans.put(beanName, iocObject);
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
		if (Strings.isNotBlank(iocBean.factory())) {
			iocObject.setFactory(iocBean.factory());
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
					sb.append(Streams.LINE_SEPARATOR)
					  .append(" - ")
					  .append(f.getName())
					  .append(" of ")
					  .append(f.getDeclaringClass());
				}

				Collection<Method> methods = Methods.getAnnotationMethods(clazz, IocInject.class);
				for (Method m : methods) {
					sb.append(Streams.LINE_SEPARATOR)
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
			if (inject == null) {
				continue;
			}
			
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
			for (String fieldInfo : fields) {
				if (iocObject.hasField(fieldInfo)) {
					throw duplicateField(clazz, fieldInfo);
				}
				
				if (Strings.contains(fieldInfo, ':')) { // dao:jndi:dataSource/jdbc形式
					String[] datas = Strings.split(fieldInfo, ':');
					// 完整形式, 与@Inject完全一致了
					iocObject.addField(datas[0], convert(datas[1]));
				}
				else {
					// 基本形式, 引用与自身同名的bean
					IocValue iocValue = new IocValue(IocValue.TYPE_REF);
					iocValue.setValue(fieldInfo);
					iocObject.addField(fieldInfo, iocValue);
				}
			}
		}
	}
	
	protected boolean isBeanAnnotated(Class<?> cls) {
		return cls.getAnnotation(IocBean.class) != null;
	}
	
	protected String getBeanName(Class<?> cls) {
		return getBeanName(cls, cls.getAnnotation(IocBean.class));
	}
	
	public static String getBeanName(Class<?> cls, IocBean iocBean) {
		if (iocBean == null) {
			return cls.getName();
		}
		
		String bn = iocBean.name();
		if (Strings.isBlank(bn)) {
			bn = iocBean.value();
			if (Strings.isBlank(bn)) {
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
	
	private static final IocException duplicateField(Class<?> classZ, String name) {
		return Exceptions.makeThrow(IocException.class, "Duplicate filed defined! Class=%s,FileName=%s", classZ, name);
	}

	private IocValue convert(String value) {
		return Loaders.convert(value, IocValue.TYPE_REF);
	}

}