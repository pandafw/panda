package panda.ioc.impl;

import java.util.Collection;
import java.util.Map;

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

public class DefaultValueProxyMaker implements ValueProxyMaker {

	@SuppressWarnings("unchecked")
	public ValueProxy make(IocMaking ing, IocValue iv) {
		String type = iv.getType();
		Object value = iv.getValue();

		// Null
		if (IocValue.TYPE_NULL.equals(type) || null == value) {
			return new StaticValue(null);
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
				return new ReferValue((Class<?>)value);
			}

			String s = value.toString();
			if (null != s) {
				String renm = s.toLowerCase();
				// $ioc
				if ("$ioc".equals(renm)) {
					return IocSelfValue.i();
				}

				// $name
				if ("$name".equals(renm)) {
					return IocBeanNameValue.i();
				}
				
				// $context
				if ("$context".equals(renm)) {
					return IocContextValue.i();
				}
			}
			return new ReferValue(s);
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

	public String[] supportedTypes() {
		return Arrays.toArray(
			IocValue.TYPE_REF, 
			IocValue.TYPE_EL, 
			IocValue.TYPE_ENV, 
			IocValue.TYPE_FILE,
			IocValue.TYPE_SYS,
			IocValue.TYPE_JNDI);
	}

}
