package panda.ioc.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map.Entry;

import panda.ioc.IocEventTrigger;
import panda.ioc.IocException;
import panda.ioc.IocMaking;
import panda.ioc.ObjectMaker;
import panda.ioc.ObjectProxy;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.ioc.weaver.DefaultWeaver;
import panda.ioc.weaver.IocFieldInjector;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.ConstructorCreator;
import panda.lang.reflect.Constructors;
import panda.lang.reflect.MethodCreator;
import panda.lang.reflect.Methods;
import panda.log.Log;
import panda.log.Logs;

/**
 * 在这里，需要考虑 AOP
 * 
 */
public class DefaultObjectMaker implements ObjectMaker {

	private static final Log log = Logs.getLog(DefaultObjectMaker.class);

	@SuppressWarnings("unchecked")
	public ObjectProxy make(IocMaking ing, IocObject iobj) {
		// 获取 Mirror， AOP 将在这个方法中进行
		Class<?> mirror = ing.getMirrors().getMirror(iobj.getType(), ing.getName());

		// 获取配置的对象事件集合
		IocEventSet iocEventSet = iobj.getEvents();

		// 建立对象代理，并保存在上下文环境中 只有对象为 singleton
		// 并且有一个非 null 的名称的时候才会保存
		// 就是说，所有内部对象，将会随这其所附属的对象来保存，而自己不会单独保存
		ObjectProxy op = new ObjectProxy();
		if (iobj.isSingleton() && null != ing.getName()) {
			ing.getIoc().getContext().save(iobj.getScope(), ing.getName(), op);
		}

		// 为对象代理设置触发事件
		if (null != iobj.getEvents()) {
			op.setFetch(createTrigger(mirror, iocEventSet.getFetch()));
			op.setDepose(createTrigger(mirror, iocEventSet.getDepose()));
		}

		try {
			// 准备对象的编织方式
			DefaultWeaver dw = new DefaultWeaver();
			op.setWeaver(dw);

			// 为编织器设置事件触发器：创建时
			if (null != iobj.getEvents()) {
				dw.setOnCreate(createTrigger(mirror, iocEventSet.getCreate()));
			}

			// 构造函数参数
			ValueProxy[] vps = new ValueProxy[iobj.getArgs().length];
			for (int i = 0; i < vps.length; i++) {
				vps[i] = ing.makeValueProxy(iobj.getArgs()[i]);
			}
			dw.setArgs(vps);

			// 先获取一遍，根据这个数组来获得构造函数
			Object[] args = new Object[vps.length];
			for (int i = 0; i < args.length; i++) {
				args[i] = vps[i].get(ing);
			}

			// 缓存构造函数
			if (iobj.getFactory() != null) {
				// factory这属性, 格式应该是 类名@方法名
				String[] tmp = iobj.getFactory().split("@", 2);
				Method m = Methods.getMatchingAccessibleMethod(mirror, tmp[0], args);
				if (m == null) {
					m = Methods.getMatchingAccessibleMethod(mirror, tmp[0], args.length);
					if (m == null) {
						throw new IocException("Failed to find factory method of '" + ing.getName() + "': " + Arrays.toString(args));
					}
				}
				dw.setArgTypes(m.getGenericParameterTypes());
				dw.setCreator(new MethodCreator<Object>(m));
			}
			else {
				Constructor c = Constructors.getConstructor(mirror, args);
				if (c == null) {
					c = Constructors.getConstructor(mirror, args.length);
					if (c == null) {
						throw new IocException("Failed to find constructor of '" + ing.getName() + "': " + Arrays.toString(args));
					}
				}
				dw.setArgTypes(c.getGenericParameterTypes());
				dw.setCreator(new ConstructorCreator(c));
			}

			// 如果这个对象是容器中的单例，那么就可以生成实例了
			// 这一步非常重要，它解除了字段互相引用的问题
			Object obj = null;
			if (iobj.isSingleton()) {
				obj = dw.born(ing);
				op.setObj(obj);
			}

			// 获得每个字段的注入方式
			IocFieldInjector[] fields = new IocFieldInjector[iobj.getFields().size()];
			int i = 0;
			for (Entry<String, IocValue> en : iobj.getFields().entrySet()) {
				try {
					ValueProxy vp = ing.makeValueProxy(en.getValue());
					fields[i++] = IocFieldInjector.create(mirror, en.getKey(), vp);
				}
				catch (Exception e) {
					throw Exceptions.wrapThrow(e, "Failed to eval Injector for field: '%s'", en.getKey());
				}
			}
			dw.setFields(fields);

			// 如果是单例对象，前面已经生成实例了，在这里需要填充一下它的字段
			if (null != obj) {
				dw.fill(ing, obj);
			}
			
			// 对象创建完毕，如果有 create 事件，调用它
			dw.onCreate(obj);
		}
		// 当异常发生，从 context 里移除 ObjectProxy
		catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn(String.format("IobObj: \n%s", iobj.toString()), e);
			}
			
			ing.getIoc().getContext().remove(iobj.getScope(), ing.getName());
			throw new IocException("Failed to create ioc bean: " + ing.getName(), e);
		}

		// 返回
		return op;
	}

	@SuppressWarnings({ "unchecked" })
	private static IocEventTrigger<Object> createTrigger(Class<?> mirror, String str) {
		if (Strings.isBlank(str)) {
			return null;
		}
		
		if (str.contains(".")) {
			return (IocEventTrigger<Object>)Classes.born(str);
		}
		
		try {
			return new MethodEventTrigger(mirror.getMethod(str));
		}
		catch (NoSuchMethodException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

}
