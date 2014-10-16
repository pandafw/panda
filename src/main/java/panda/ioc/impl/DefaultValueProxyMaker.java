package panda.ioc.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.IocException;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.ValueProxyMaker;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.ioc.val.ArrayValue;
import panda.ioc.val.CollectionValue;
import panda.ioc.val.ElValue;
import panda.ioc.val.EnvValue;
import panda.ioc.val.FileValue;
import panda.ioc.val.InnerValue;
import panda.ioc.val.IocBeanNameValue;
import panda.ioc.val.IocContextValue;
import panda.ioc.val.IocSelfValue;
import panda.ioc.val.MapValue;
import panda.ioc.val.ReferValue;
import panda.ioc.val.StaticValue;
import panda.ioc.val.SysPropValue;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Strings;

public class DefaultValueProxyMaker implements ValueProxyMaker {

	@SuppressWarnings("unchecked")
	public ValueProxy make(IocMaking ing, IocValue iv) {
		String type = iv.getType();
		Object value = iv.getValue();

		// Null
		if (IocValue.TYPE_NULL.equals(type) || null == value) {
			return StaticValue.NULL;
		}
		
		// String, Number, .....
		if (IocValue.TYPE_NORMAL.equals(type) || null == type) {
			// Array
			if (value.getClass().isArray()) {
				Object[] vs = (Object[])value;
				IocValue[] tmp = new IocValue[vs.length];
				for (int i = 0; i < tmp.length; i++) {
					tmp[i] = (IocValue)vs[i];
				}
				return new ArrayValue(ing, tmp);
			}
		
			// Map
			if (value instanceof Map<?, ?>) {
				return new MapValue(ing, (Map<String, IocValue>)value,
					(Class<? extends Map<String, Object>>)value.getClass());
			}
			
			// Collection
			if (value instanceof Collection<?>) {
				return new CollectionValue(ing, (Collection<IocValue>)value,
					(Class<? extends Collection<Object>>)value.getClass());
			}
			
			// Inner Object
			if (value instanceof IocObject) {
				return new InnerValue((IocObject)value);
			}
			
			return new StaticValue(value);
		}
		
		// Refer
		if (IocValue.TYPE_REF.equals(type)) {
			if (value instanceof Class) {
				if (Ioc.class.equals(value)) {
					return IocSelfValue.i();
				}
				if (IocContext.class.equals(value)) {
					return IocContextValue.i();
				}
				return new ReferValue((Class<?>)value, iv.isRequired());
			}

			String s = value.toString();
			if (Strings.isEmpty(s)) {
				throw new IocException("Empty ref ioc value");
			}

			String ls = s.toLowerCase();
			// $ioc
			if (ValueProxyMaker.IOC.equals(ls) || Ioc.class.getName().equals(s)) {
				return IocSelfValue.i();
			}
			
			// ioc context
			if (ValueProxyMaker.ICTX.equals(ls) || IocContext.class.getName().equals(s)) {
				return IocContextValue.i();
			}

			// ioc bean name
			if (ValueProxyMaker.IBN.equals(ls)) {
				return IocBeanNameValue.i();
			}
			return new ReferValue(s, iv.isRequired());
		}

		// EL
		if (IocValue.TYPE_EL.equals(type)) {
			return new ElValue(value.toString());
		}
		
		// File
		if (IocValue.TYPE_FILE.equals(type)) {
			return new FileValue(value.toString());
		}
		
		// Env
		if (IocValue.TYPE_ENV.equals(type)) {
			return new EnvValue(value.toString());
		}
		
		// System Properties
		if (IocValue.TYPE_SYS.equals(type)) {
			return new SysPropValue(value.toString());
		}
		
		// Inner
		if (IocValue.TYPE_INNER.equals(type)) {
			return new InnerValue((IocObject)value);
		}
		
		// JNDI
		if (IocValue.TYPE_JNDI.equals(type)) {
			// for android compile
			return (ValueProxy)Classes.born(InnerValue.class.getPackage().toString() + ".JndiValue", value.toString());
		}
		return null;
	}

	public Set<String> supportedTypes() {
		return Arrays.toSet(
			IocValue.TYPE_EL, 
			IocValue.TYPE_ENV, 
			IocValue.TYPE_FILE,
			IocValue.TYPE_JNDI,
			IocValue.TYPE_REF, 
			IocValue.TYPE_SYS
			);
	}

}
