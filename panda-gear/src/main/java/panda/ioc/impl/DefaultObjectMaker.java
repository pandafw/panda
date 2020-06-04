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
import panda.ioc.meta.IocParam;
import panda.ioc.wea.BeanMethodCreator;
import panda.ioc.wea.ELCreator;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.lang.reflect.ArrayCreator;
import panda.lang.reflect.ConstructorCreator;
import panda.lang.reflect.Constructors;
import panda.lang.reflect.MethodCreator;
import panda.lang.reflect.Methods;
import panda.log.Log;
import panda.log.Logs;

import reactor.core.Exceptions;

/**
 * 在这里，需要考虑 AOP
 * 
 */
public class DefaultObjectMaker implements ObjectMaker {

	private static final Log log = Logs.getLog(DefaultObjectMaker.class);

	public ObjectProxy makeSingleton(IocMaking im, IocObject iobj) {
		// get Mirror class for AOP
		Class<?> mirror = im.getMirrors().getMirror(im.getIoc(), iobj.getType(), im.getName());

		try {
			SingletonObjectProxy sop = new SingletonObjectProxy();
			
			// save singleton object to context
			// !! important: avoid reference loop
			if (im.getName() != null) {
				if (!im.getIoc().getContext().save(iobj.getScope(), im.getName(), sop)) {
					throw new IocException("Failed to save '" + im.getName() + "' to " + iobj.getScope() + " IocContext");
				}
			}

			// set event handlers
			if (iobj.getEvents() != null) {
				sop.setFetch(createTrigger(mirror, iobj.getEvents().getFetch()));
				sop.setDepose(createTrigger(mirror, iobj.getEvents().getDepose()));
			}

			if (iobj.getValue() != null) {
				sop.setObject(iobj.getValue());
				return sop;
			}

			ObjectWeaver ow = makeWeaver(mirror, im, iobj);

			// create singleton object
			// !! important: avoid reference loop
			Object obj = ow.born(im);

			// set object to proxy
			// !! important: avoid reference loop
			sop.setObject(obj);

			// inject fields
			ow.fill(im, obj);
			
			// trigger create event
			ow.onCreate(obj);

			return sop;
		}
		catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to make singleton object proxy for " + iobj);
			}
			
			// remove ObjectProxy from context
			if (im.getName() != null) {
				im.getIoc().getContext().remove(iobj.getScope(), im.getName());
			}
			
			if (e instanceof IocException) {
				throw (IocException)e;
			}
			throw new IocException("Failed to create singleton object proxy for " + im.getName(), Exceptions.unwrap(e));
		}
	}

	public ObjectProxy makeDynamic(IocMaking im, IocObject iobj) {
		// get Mirror class for AOP
		Class<?> mirror = im.getMirrors().getMirror(im.getIoc(), iobj.getType(), im.getName());

		try {
			ObjectWeaver ow = makeWeaver(mirror, im, iobj);

			DynamicObjectProxy dop = new DynamicObjectProxy(ow);
			
			// save dynamic object to context
			// !! important: avoid reference loop
			if (im.getName() != null) {
				if (!im.getIoc().getContext().save(iobj.getScope(), im.getName(), dop)) {
					throw new IocException("Failed to save '" + im.getName() + "' to " + iobj.getScope() + " IocContext");
				}
			}

			// set fetch event handler
			// depose event is not support for dynamic object
			if (iobj.getEvents() != null) {
				dop.setFetch(createTrigger(mirror, iobj.getEvents().getFetch()));
			}

			return dop;
		}
		catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to make dynamic object proxy for " + iobj);
			}
			
			// remove ObjectProxy from context
			if (im.getName() != null) {
				im.getIoc().getContext().remove(iobj.getScope(), im.getName());
			}
			
			if (e instanceof IocException) {
				throw (IocException)e;
			}
			throw new IocException("Failed to create dynamic object proxy for " + im.getName(), Exceptions.unwrap(e));
		}
	}

	public ObjectWeaver makeWeaver(Class<?> mirror, IocMaking im, IocObject iobj) {
		ObjectWeaver ow;

		if (im.getName() == null) {
			ow = createWeaver(mirror, im, iobj);
		}
		else {
			ow = im.getWeavers().get(im.getName());
			if (ow == null) {
				synchronized (im.getWeavers()) {
					ow = im.getWeavers().get(im.getName());
					if (ow == null) {
						ow = createWeaver(mirror, im, iobj);
						im.getWeavers().put(im.getName(), ow);
					}
				}
			}
		}
		return ow;
	}

	private ObjectWeaver createWeaver(Class<?> mirror, IocMaking im, IocObject iobj) {
		// 准备对象的编织方式
		DefaultObjectWeaver dw = new DefaultObjectWeaver();

		// 构造函数参数
		setWeaverCreator(dw, mirror, im, iobj);

		// 获得每个字段的注入方式
		setWeaverFields(dw, mirror, im, iobj);
		
		return dw;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setWeaverCreator(DefaultObjectWeaver dw, Class<?> cls, IocMaking im, IocObject iobj) {
		// 为编织器设置事件触发器：创建时
		if (iobj.getEvents() != null) {
			dw.setOnCreate(createTrigger(cls, iobj.getEvents().getCreate()));
		}

		Object[] args = Arrays.EMPTY_OBJECT_ARRAY;
		if (iobj.getArgs() != null) {
			// 构造函数参数
			ValueProxy[] vps = new ValueProxy[iobj.getArgs().length];
			for (int i = 0; i < vps.length; i++) {
				vps[i] = im.makeValueProxy(iobj.getArgs()[i]);
			}
			dw.setArgs(vps);
	
			// 先获取一遍，根据这个数组来获得构造函数
			args = new Object[vps.length];
			for (int i = 0; i < args.length; i++) {
				args[i] = vps[i].get(im);
			}
		}
		
		// factory
		if (Strings.isNotEmpty(iobj.getFactory())) {
			String fa = iobj.getFactory();
			int c0 = fa.charAt(0);

			if (fa.length() > 3 && (c0 == '$' || c0 == '%') 
					&& fa.charAt(1) == '{'
					&& fa.charAt(fa.length() - 1) == '}') {
				// EL
				dw.setCreator(new ELCreator(im.getIoc(), fa.substring(2, fa.length() - 1)));
			}
			else if (c0 == '#') {
				// iocbean.method
				int d = fa.lastIndexOf('.');
				if (d < 1) {
					throw new IocException("Invalid factory method of '" + im.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
				}
				String name = fa.substring(1, d);
				String method = fa.substring(d + 1);
				Object obj = im.getIoc().get(null, name);

				Method m = Methods.getMatchingAccessibleMethod(obj.getClass(), method, args);
				if (m == null) {
					m = Methods.getMatchingAccessibleMethod(obj.getClass(), method, args.length);
					if (m == null) {
						throw new IocException("Failed to find factory method of '" + im.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
					}
				}
				dw.setArgTypes(m.getGenericParameterTypes());
				dw.setCreator(new BeanMethodCreator<Object>(im.getIoc(), name, m));
			}
			else {
				// static class@method
				String[] ss = fa.split("@", 2);
				if (ss.length != 2) {
					throw new IocException("Invalid factory method of '" + im.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
				}

				Method m = Methods.getMatchingAccessibleMethod(cls, ss[0], args);
				if (m == null) {
					m = Methods.getMatchingAccessibleMethod(cls, ss[0], args.length);
					if (m == null) {
						throw new IocException("Failed to find factory method of '" + im.getName() + "': " + fa + '(' + Arrays.toString(args) + ')');
					}
				}
				dw.setArgTypes(m.getGenericParameterTypes());
				dw.setCreator(new MethodCreator<Object>(m));
			}
		}
		else {
			if (cls.isArray()) {
				dw.setCreator(new ArrayCreator(cls, iobj.getFields() == null ? 0 : iobj.getFields().size()));
			}
			else {
				Constructor<?> c = Constructors.getConstructor(cls, args);
				if (c == null) {
					c = Constructors.getConstructor(cls, args.length);
					if (c == null) {
						throw new IocException("Failed to find constructor of '" 
								+ im.getName() + "' " + cls + ": " + Arrays.toString(args));
					}
				}
				dw.setArgTypes(c.getGenericParameterTypes());
				dw.setCreator(new ConstructorCreator(c));
			}
		}
	}

	private void setWeaverFields(DefaultObjectWeaver weaver, Class<?> mirror, IocMaking im, IocObject iobj) {
		if (iobj.getFields() == null) {
			return;
		}
		
		// 获得每个字段的注入方式
		IocFieldInjector[] fields = new IocFieldInjector[iobj.getFields().size()];
		int i = 0;
		for (Entry<String, IocParam> en : iobj.getFields().entrySet()) {
			try {
				fields[i++] = IocFieldInjector.create(im, mirror, en.getKey(), en.getValue());
			}
			catch (Exception e) {
				throw new IocException("Failed to eval Injector for field '" + en.getKey() + "' of " + mirror, e);
			}
		}
		weaver.setFields(fields);
	}

	@SuppressWarnings({ "unchecked" })
	private IocEventTrigger<Object> createTrigger(Class<?> mirror, String str) {
		if (Strings.isEmpty(str)) {
			return null;
		}
		
		try {
			if (str.contains(".")) {
				return (IocEventTrigger<Object>)Classes.born(str);
			}
			
			return new MethodEventTrigger(mirror.getMethod(str));
		}
		catch (Exception e) {
			throw new IocException("Failed to eval EventTrigger for '" + str + "' of " + mirror, e);
		}
	}

}
