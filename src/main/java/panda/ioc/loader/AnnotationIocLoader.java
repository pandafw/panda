package panda.ioc.loader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import panda.bind.json.Jsons;
import panda.ioc.IocException;
import panda.ioc.IocLoader;
import panda.ioc.IocLoading;
import panda.ioc.ObjectLoadException;
import panda.ioc.annotation.Inject;
import panda.ioc.annotation.Bean;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocField;
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
		for (String packageZ : packages) {
			for (Class<?> classZ : Classes.scan(packageZ)) {
				addClass(classZ);
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

	private void addClass(Class<?> clazz) {
		if (clazz.isInterface() || clazz.isMemberClass() || clazz.isEnum() || clazz.isAnnotation()
				|| clazz.isAnonymousClass()) {
			return;
		}
		
		int modify = clazz.getModifiers();
		if (Modifier.isAbstract(modify) || (!Modifier.isPublic(modify))) {
			return;
		}
		
		Bean iocBean = clazz.getAnnotation(Bean.class);
		if (iocBean != null) {
			if (log.isDebugEnabled()) {
				log.debugf("Found a Class with Ioc-Annotation : %s", clazz);
			}
			
			// 采用 @IocBean->name
			String beanName = getBeanName(clazz, iocBean);

			if (map.containsKey(beanName)) {
				throw new IocException("Duplicate beanName=%s, by %s !!  Have been define by %s !!",
					beanName, clazz, map.get(beanName).getClass());
			}
			
			IocObject iocObject = new IocObject();
			iocObject.setType(clazz);
			map.put(beanName, iocObject);

			iocObject.setSingleton(iocBean.singleton());
			if (!Strings.isBlank(iocBean.scope())) {
				iocObject.setScope(iocBean.scope());
			}

			// 看看构造函数都需要什么函数
			String[] args = iocBean.args();
			if (null != args && args.length > 0) {
				for (String value : args) {
					iocObject.addArg(convert(value));
				}
			}
			
			// 设置Events
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

			// 处理字段(以@Inject方式,位于字段)
			List<String> fieldList = new ArrayList<String>();
			Field[] fields = Fields.getAnnotationFields(clazz, Inject.class);
			for (Field field : fields) {
				Inject inject = field.getAnnotation(Inject.class);
				IocField iocField = new IocField();
				iocField.setName(field.getName());
	
				IocValue iocValue;
				if (Strings.isBlank(inject.value())) {
					iocValue = new IocValue();
					iocValue.setType(IocValue.TYPE_REF);
					iocValue.setValue(field.getName());
				}
				else {
					iocValue = convert(inject.value());
				}
				
				iocField.setValue(iocValue);
				iocObject.addField(iocField);
				fieldList.add(iocField.getName());
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
				Inject inject = method.getAnnotation(Inject.class);
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
					IocField iocField = new IocField();
					iocField.setName(Strings.uncapitalize(methodName.substring(3)));
					if (fieldList.contains(iocField.getName())) {
						throw duplicateField(clazz, iocField.getName());
					}
					
					IocValue iocValue;
					if (Strings.isBlank(inject.value())) {
						iocValue = new IocValue();
						iocValue.setType(IocValue.TYPE_REF);
						iocValue.setValue(Strings.uncapitalize(methodName.substring(3)));
					}
					else {
						iocValue = convert(inject.value());
					}

					iocField.setValue(iocValue);
					iocObject.addField(iocField);
					fieldList.add(iocField.getName());
				}
			}
			
			// 处理字段(以@IocBean.field方式)
			String[] flds = iocBean.fields();
			if (flds != null && flds.length > 0) {
				for (String fieldInfo : flds) {
					if (fieldList.contains(fieldInfo)) {
						throw duplicateField(clazz, fieldInfo);
					}
					
					IocField iocField = new IocField();
					if (fieldInfo.contains(":")) { // dao:jndi:dataSource/jdbc形式
						String[] datas = fieldInfo.split(":", 2);
						// 完整形式, 与@Inject完全一致了
						iocField.setName(datas[0]);
						iocField.setValue(convert(datas[1]));
						iocObject.addField(iocField);
					}
					else {
						// 基本形式, 引用与自身同名的bean
						iocField.setName(fieldInfo);
						IocValue iocValue = new IocValue();
						iocValue.setType(IocValue.TYPE_REF);
						iocValue.setValue(fieldInfo);
						iocField.setValue(iocValue);
						iocObject.addField(iocField);
					}
					fieldList.add(iocField.getName());
				}
			}

			// 处理工厂方法
			if (Strings.isNotBlank(iocBean.factory())) {
				iocObject.setFactory(beanName);
			}
		}
		else {
			// 这里只是检查一下@Inject,要避免抛出异常.
			try {
				if (log.isWarnEnabled()) {
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						if (field.getAnnotation(Inject.class) != null) {
							log.warnf("class(%s) don't has @IocBean, but field(%s) has @Inject! Miss @IocBean ??",
								clazz.getName(), field.getName());
							break;
						}
					}
				}
			}
			catch (Exception e) {
				// 无需处理.
			}
		}
	}

	public static String getBeanName(Class<?> cls) {
		return getBeanName(cls, cls.getAnnotation(Bean.class));
	}
	
	public static String getBeanName(Class<?> cls, Bean iocBean) {
		String bn = iocBean.name();
		if (Strings.isBlank(bn)) {
			bn = iocBean.value();
			if (Strings.isBlank(bn)) {
				bn = Strings.uncapitalize(cls.getSimpleName());
			}
		}
		return bn;
	}
	
	protected IocValue convert(String value) {
		IocValue iocValue = new IocValue();
		if (value.contains(":")) {
			iocValue.setType(value.substring(0, value.indexOf(':')));
			iocValue.setValue(value.substring(value.indexOf(':') + 1));
		}
		else {
			iocValue.setValue(value); // TODO 是否应该改为默认refer呢?
		}
		return iocValue;
	}

	public String[] getName() {
		return map.keySet().toArray(new String[map.size()]);
	}

	public boolean has(String name) {
		return map.containsKey(name);
	}

	public IocObject load(IocLoading loading, String name) throws ObjectLoadException {
		if (has(name))
			return map.get(name);
		throw new ObjectLoadException("Object '" + name + "' without define!");
	}

	private static final IocException duplicateField(Class<?> classZ, String name) {
		return Exceptions.makeThrow(IocException.class, "Duplicate filed defined! Class=%s,FileName=%s", classZ, name);
	}

	public String toString() {
		return "/*AnnotationIocLoader*/\n" + Jsons.toJson(map);
	}
}
