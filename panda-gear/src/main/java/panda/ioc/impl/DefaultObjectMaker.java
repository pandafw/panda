package panda.ioc.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import panda.ioc.IocEventTrigger;
import panda.ioc.IocException;
import panda.ioc.IocMaking;
import panda.ioc.ObjectMaker;
import panda.ioc.ObjectProxy;
import panda.ioc.ObjectWeaver;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.ioc.wea.BeanMethodCreator;
import panda.ioc.wea.ELCreator;
import panda.lang.Arrays;
import panda.lang.Chars;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.ArrayCreator;
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

	public ObjectProxy make(IocMaking imak, IocObject iobj) {
		// 获取 Mirror， AOP 将在这个方法中进行
		Class<?> mirror = imak.getMirrors().getMirror(imak.getIoc(), iobj.getType(), imak.getName());

		try {
			if (iobj.isSingleton()) {
				SingletonObjectProxy sop = new SingletonObjectProxy();
				
				// 建立对象代理，并保存在上下文环境中 只有对象为 singleton
				// 并且有一个非 null 的名称的时候才会保存
				// 就是说，所有内部对象，将会随这其所附属的对象来保存，而自己不会单独保存
				if (imak.getName() != null) {
					if (!imak.getIoc().getContext().save(iobj.getScope(), imak.getName(), sop)) {
						throw new IocException("Failed to save '" + imak.getName() + "' to " + iobj.getScope() + " IocContext");
					}
				}
	
				// 为对象代理设置触发事件
				if (iobj.getEvents() != null) {
					sop.setFetch(createTrigger(mirror, iobj.getEvents().getFetch()));
					sop.setDepose(createTrigger(mirror, iobj.getEvents().getDepose()));
				}
	
				if (iobj.getValue() != null) {
					sop.setObject(iobj.getValue());
					return sop;
				}

				ObjectWeaver ow = makeWeaver(mirror, imak, iobj);

				// create singleton object
				Object obj = ow.born(imak);

				// set obj to proxy
				// 这一步非常重要，它解除了字段互相引用的问题
				sop.setObject(obj);

				// inject
				ow.fill(imak, obj);
				
				// 对象创建完毕，如果有 create 事件，调用它
				ow.onCreate(obj);
				
				return sop;
			}
			else {
				ObjectWeaver ow = makeWeaver(mirror, imak, iobj);

				DynamicObjectProxy dop = new DynamicObjectProxy(ow);
				
				// 为对象代理设置触发事件
				if (iobj.getEvents() != null) {
					dop.setFetch(createTrigger(mirror, iobj.getEvents().getFetch()));
				}
	
				return dop;
			}
		}
		catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("Error occurred for IocObject: " + iobj);
			}
			
			//remove ObjectProxy from context
			if (iobj.isSingleton() && imak.getName() != null) {
				imak.getIoc().getContext().remove(iobj.getScope(), imak.getName());
			}
			
			if (e instanceof IocException) {
				throw (IocException)e;
			}
			throw new IocException("Failed to create ioc bean: " + imak.getName(), e);
		}
	}
	
	public ObjectWeaver makeWeaver(Class<?> mirror, IocMaking imak, IocObject iobj) {
		ObjectWeaver ow;

		if (imak.getName() == null) {
			ow = createWeaver(mirror, imak, iobj);
		}
		else {
			ow = imak.getWeavers().get(imak.getName());
			if (ow == null) {
				synchronized (imak.getWeavers()) {
					ow = imak.getWeavers().get(imak.getName());
					if (ow == null) {
						ow = createWeaver(mirror, imak, iobj);
						imak.getWeavers().put(imak.getName(), ow);
					}
				}
			}
		}
		return ow;
	}

	private ObjectWeaver createWeaver(Class<?> mirror, IocMaking imak, IocObject iobj) {
		// 准备对象的编织方式
		DefaultObjectWeaver dw = new DefaultObjectWeaver();

		// 构造函数参数
		setWeaverCreator(dw, mirror, imak, iobj);

		// 获得每个字段的注入方式
		setWeaverFields(dw, mirror, imak, iobj);
		
		return dw;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setWeaverCreator(DefaultObjectWeaver dw, Class<?> cls, IocMaking ing, IocObject iobj) {
		// 为编织器设置事件触发器：创建时
		if (iobj.getEvents() != null) {
			dw.setOnCreate(createTrigger(cls, iobj.getEvents().getCreate()));
		}

		// 构造函数参数
		ValueProxy[] vps = new ValueProxy[iobj.getArgs().size()];
		for (int i = 0; i < vps.length; i++) {
			vps[i] = ing.makeValueProxy(iobj.getArgs().get(i));
		}
		dw.setArgs(vps);

		// 先获取一遍，根据这个数组来获得构造函数
		Object[] args = new Object[vps.length];
		for (int i = 0; i < args.length; i++) {
			args[i] = vps[i].get(ing);
		}

		// factory
		if (Strings.isNotEmpty(iobj.getFactory())) {
			String fa = iobj.getFactory();
			int c0 = fa.charAt(0);

			if (fa.length() > 3 && (c0 == Chars.DOLLAR || c0 == Chars.PERCENT) 
					&& fa.charAt(1) == Chars.BRACES_LEFT 
					&& fa.charAt(fa.length() - 1) == Chars.BRACES_RIGHT) {
				// EL
				dw.setCreator(new ELCreator(ing.getIoc(), fa.substring(2, fa.length() - 1)));
			}
			else if (c0 == Chars.SHARP) {
				// iocbean.method
				int d = fa.lastIndexOf('.');
				if (d < 1) {
					throw new IocException("Invalid factory method of '" + ing.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
				}
				String name = fa.substring(1, d);
				String method = fa.substring(d + 1);
				Object obj = ing.getIoc().get(null, name);

				Method m = Methods.getMatchingAccessibleMethod(obj.getClass(), method, args);
				if (m == null) {
					m = Methods.getMatchingAccessibleMethod(obj.getClass(), method, args.length);
					if (m == null) {
						throw new IocException("Failed to find factory method of '" + ing.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
					}
				}
				dw.setArgTypes(m.getGenericParameterTypes());
				dw.setCreator(new BeanMethodCreator<Object>(ing.getIoc(), name, m));
			}
			else {
				// static class@method
				String[] ss = fa.split("@", 2);
				if (ss.length != 2) {
					throw new IocException("Invalid factory method of '" + ing.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
				}

				Method m = Methods.getMatchingAccessibleMethod(cls, ss[0], args);
				if (m == null) {
					m = Methods.getMatchingAccessibleMethod(cls, ss[0], args.length);
					if (m == null) {
						throw new IocException("Failed to find factory method of '" + ing.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
					}
				}
				dw.setArgTypes(m.getGenericParameterTypes());
				dw.setCreator(new MethodCreator<Object>(m));
			}
		}
		else {
			if (cls.isArray()) {
				dw.setCreator(new ArrayCreator(cls, iobj.getFields().size()));
			}
			else {
				Constructor<?> c = Constructors.getConstructor(cls, args);
				if (c == null) {
					c = Constructors.getConstructor(cls, args.length);
					if (c == null) {
						throw new IocException("Failed to find constructor of '" 
								+ ing.getName() + "' " + cls + ": " + Arrays.toString(args));
					}
				}
				dw.setArgTypes(c.getGenericParameterTypes());
				dw.setCreator(new ConstructorCreator(c));
			}
		}
	}

	private void setWeaverFields(DefaultObjectWeaver weaver, Class<?> mirror, IocMaking ing, IocObject iobj) {
		// 获得每个字段的注入方式
		IocFieldInjector[] fields = new IocFieldInjector[iobj.getFields().size()];
		int i = 0;
		for (Entry<String, IocValue> en : iobj.getFields().entrySet()) {
			try {
				ValueProxy vp = ing.makeValueProxy(en.getValue());
				fields[i++] = IocFieldInjector.create(mirror, en.getKey(), en.getValue(), vp);
			}
			catch (Exception e) {
				throw Exceptions.wrapThrow(e, "Failed to eval Injector for field: '%s'", en.getKey());
			}
		}
		weaver.setFields(fields);
	}

	@SuppressWarnings({ "unchecked" })
	private IocEventTrigger<Object> createTrigger(Class<?> mirror, String str) {
		if (Strings.isEmpty(str)) {
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
