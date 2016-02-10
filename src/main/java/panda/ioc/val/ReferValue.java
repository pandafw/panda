package panda.ioc.val;

import panda.ioc.Ioc;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.util.Pair;

public class ReferValue implements ValueProxy {
	public static ValueProxy IOC_SELF = new ValueProxy() {
		public Object get(IocMaking ing) {
			return ing.getIoc();
		}
	};

	public static ValueProxy IOC_BEAN_NAME = new ValueProxy() {
		public Object get(IocMaking ing) {
			return ing.getName();
		}
	};

	public static ValueProxy IOC_CONTEXT = new ValueProxy() {
		public Object get(IocMaking ing) {
			return ing.getIoc().getContext();
		}
	};

	private String name;
	private Class<?> type;
	private boolean required = true;

	public static Pair<Class<?>> parse(String name) {
		String _name = null;
		Class<?> type = null;
		int pos = name.indexOf(':');
		if (pos < 0) {
			_name = Strings.trim(name);
		}
		else {
			_name = Strings.trim(name.substring(0, pos));
			String typeName = Strings.trim(name.substring(pos + 1));
			type = Classes.load(typeName, false);
		}
		return new Pair<Class<?>>(_name, type);
	}

	public ReferValue(String expr, boolean required) {
		Pair<Class<?>> p = parse(expr);
		this.name = p.getName();
		this.type = p.getValue();
		this.required = required;
	}

	public ReferValue(Class<?> type, boolean required) {
		this.type = type;
		this.required = required;
	}

	public ReferValue(String expr, Class<?> type, boolean required) {
		Pair<Class<?>> p = parse(expr);
		this.name = p.getName();
		this.type = p.getValue() == null ? type : p.getValue();
		this.required = required;
	}

	public Object get(IocMaking ing) {
		Ioc ioc = ing.getIoc();
		if (required) {
			return ioc.get(type, name);
		}
		
		if (ioc.has(type, name)) {
			return ioc.get(type, name);
		}
		
		return UNDEFINED;
	}

}
