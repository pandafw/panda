package panda.ioc.impl;

import java.util.Collection;
import java.util.Map;

import panda.bind.json.Jsons;
import panda.ioc.Ioc;
import panda.ioc.IocConstants;
import panda.ioc.IocContext;
import panda.ioc.IocException;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.ValueProxyMaker;
import panda.ioc.meta.IocValue;
import panda.ioc.val.ArrayValue;
import panda.ioc.val.CollectionValue;
import panda.ioc.val.ElValue;
import panda.ioc.val.MapValue;
import panda.ioc.val.ReferValue;
import panda.ioc.val.StaticValue;
import panda.lang.Strings;
import panda.lang.reflect.Types;

public class DefaultValueProxyMaker implements ValueProxyMaker {

	@SuppressWarnings("unchecked")
	public ValueProxy make(IocMaking ing, IocValue iv) {
		char type = iv.getType();
		Object value = iv.getValue();

		// Null
		if (IocValue.TYPE_NULL == type || null == value) {
			return StaticValue.NULL;
		}
		
		// String, Number, .....
		if (IocValue.TYPE_NORMAL == type) {
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
			
			return new StaticValue(value);
		}
		
		// Refer
		if (IocValue.TYPE_REF == type) {
			if (value instanceof Class) {
				if (Ioc.class.equals(value)) {
					return ReferValue.IOC_SELF;
				}
				if (IocContext.class.equals(value)) {
					return ReferValue.IOC_CONTEXT;
				}
				return new ReferValue((Class<?>)value, iv.isRequired());
			}

			String s = value.toString();
			if (Strings.isEmpty(s)) {
				throw new IocException("Empty ref ioc value");
			}

			// $ioc
			if (IocConstants.IOC_SELF.equalsIgnoreCase(s) || Ioc.class.getName().equals(s)) {
				return ReferValue.IOC_SELF;
			}
			
			// ioc context
			if (IocConstants.IOC_CONTEXT.equalsIgnoreCase(s) || IocContext.class.getName().equals(s)) {
				return ReferValue.IOC_CONTEXT;
			}

			// ioc bean name
			if (IocConstants.IOC_BEAN_NAME.equalsIgnoreCase(s)) {
				return ReferValue.IOC_BEAN_NAME;
			}
			
			Class t = null;
			if (iv.getInjector() != null) {
				t = Types.getRawType(Types.getDefaultImplType(iv.getInjector().type(null)));
			}
			return new ReferValue(s, t, iv.isRequired());
		}

		// EL
		if (IocValue.TYPE_EL == type) {
			return new ElValue(value.toString());
		}
		
		// JSON
		if (IocValue.TYPE_JSON == type) {
			Object jv = Jsons.fromJson(value.toString());
			return new StaticValue(jv);
		}

		throw new IllegalArgumentException("Invalid type of IocValue: " + type);
	}

}
