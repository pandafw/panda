package panda.ioc.loader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import panda.bind.json.Jsons;
import panda.ioc.IocException;
import panda.ioc.IocLoader;
import panda.ioc.IocLoading;
import panda.ioc.IocLoadException;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.Fields;
import panda.log.Log;
import panda.log.Logs;

/**
 * 基于注解的Ioc配置
 */
public class AnnotationIocLoader implements IocLoader {

	private static final Log log = Logs.getLog(AnnotationIocLoader.class);

	private HashMap<String, IocObject> map = new HashMap<String, IocObject>();

	public AnnotationIocLoader(String... packages) {
		for (String pkg : packages) {
			for (Class<?> cls : Classes.scan(pkg)) {
				addClass(cls);
			}
		}
		
		if (map.size() > 0) {
			if (log.isInfoEnabled()) {
				log.info("Successfully scan " + packages.length + " packages: " + Strings.join(packages, ' ') + "\n"
					+ "Found " + map.size() + " bean classes\n"
					+ Strings.join(map.keySet(), '\n'));
			}
		}
		else {
			log.warn("NONE Annotation-Class found!! Check your configure or report a bug!! packages: "
					+ Strings.join(packages, ' '));
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
		if (map.containsKey(beanName)) {
			throw new IocException("Duplicate beanName=%s, by %s !!  Have been define by %s !!",
				beanName, clazz, map.get(beanName).getClass());
		}

		if (log.isDebugEnabled()) {
			log.debug("Found a @IocBean of " + clazz);
		}
		
		IocObject iocObject = createIocObject(clazz);
		if (iocObject != null) {
			map.put(beanName, iocObject);
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
	
	protected void setIocInjects(IocObject iocObject, Class<?> clazz) {
		Field[] fields = Fields.getAnnotationFields(clazz, IocInject.class);
		for (Field field : fields) {
			IocInject inject = field.getAnnotation(IocInject.class);

			IocValue iocValue;
			if (Strings.isBlank(inject.value())) {
				iocValue = new IocValue();
				iocValue.setType(IocValue.TYPE_REF);
				iocValue.setValue(Object.class.equals(inject.type()) ? field.getType() : inject.type());
			}
			else {
				iocValue = convert(inject.value());
			}
			iocValue.setRequired(inject.required());
			
			iocObject.addField(field.getName(), iocValue);
		}

		// 处理字段(以@Inject方式,位于set方法)
		Method[] methods;
		try {
			methods = clazz.getMethods();
		}
		catch (Exception e) {
			// 如果获取失败,就忽略之
			log.infof("Fail to call getMethods() in Class=%s, miss class or Security Limit, ignore it", clazz, e);
			methods = new Method[0];
		}
		catch (NoClassDefFoundError e) {
			log.infof("Fail to call getMethods() in Class=%s, miss class or Security Limit, ignore it", clazz, e);
			methods = new Method[0];
		}
		
		for (Method method : methods) {
			IocInject inject = method.getAnnotation(IocInject.class);
			if (inject == null) {
				continue;
			}
			
			// 过滤特殊方法
			int m = method.getModifiers();
			if (Modifier.isAbstract(m) || (!Modifier.isPublic(m)) || Modifier.isStatic(m)) {
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
					iocValue = new IocValue();
					iocValue.setType(IocValue.TYPE_REF);
					iocValue.setValue(Strings.uncapitalize(methodName.substring(3)));
					iocValue.setValue(Object.class.equals(inject.type()) ? method.getParameterTypes()[0] : inject.type());
				}
				else {
					iocValue = convert(inject.value());
				}
				iocValue.setRequired(inject.required());

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
					IocValue iocValue = new IocValue();
					iocValue.setType(IocValue.TYPE_REF);
					iocValue.setValue(fieldInfo);
					iocObject.addField(fieldInfo, iocValue);
				}
			}
		}
	}

	protected void checkInject(Class<?> clazz) {
		// check @IocInject without @IocBean
		try {
			if (log.isWarnEnabled()) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					if (field.getAnnotation(IocInject.class) != null) {
						log.warnf("class(%s) don't has @IocBean, but field(%s) has @IocInject! Miss @IocBean ??",
							clazz.getName(), field.getName());
						break;
					}
				}
			}
		}
		catch (Exception e) {
			// skip.
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
	
	protected IocValue convert(String value) {
		IocValue iocValue = new IocValue();
		int colon = value.indexOf(':');
		if (colon == 0) {
			iocValue.setType(IocValue.TYPE_REF);
			iocValue.setValue(value.substring(1));
		}
		else if (colon > 0) {
			iocValue.setType(value.substring(0, colon));
			iocValue.setValue(value.substring(colon + 1));
		}
		else {
			iocValue.setValue(value);
		}
		return iocValue;
	}

	public String[] getName() {
		return map.keySet().toArray(new String[map.size()]);
	}

	public boolean has(String name) {
		return map.containsKey(name);
	}

	public IocObject load(IocLoading loading, String name) throws IocLoadException {
		return map.get(name);
	}

	private static final IocException duplicateField(Class<?> classZ, String name) {
		return Exceptions.makeThrow(IocException.class, "Duplicate filed defined! Class=%s,FileName=%s", classZ, name);
	}

	public String toString() {
		return "/*AnnotationIocLoader*/\n" + Jsons.toJson(map);
	}
}
