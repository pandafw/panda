package panda.ioc.val;

import panda.ioc.Ioc;
import panda.ioc.IocConstants;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.lang.Classes;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.collection.KeyValue;

public class ReferValue implements ValueProxy {
	public static ValueProxy IOC_SELF = new ValueProxy() {
		public Object get(IocMaking ing) {
			return ing.getIoc();
		}

		@Override
		public String toString() {
			return IocConstants.IOC_SELF;
		}
	};

	public static ValueProxy IOC_BEAN_NAME = new ValueProxy() {
		public Object get(IocMaking ing) {
			return ing.getName();
		}

		@Override
		public String toString() {
			return IocConstants.IOC_BEAN_NAME;
		}
	};

	public static ValueProxy IOC_CONTEXT = new ValueProxy() {
		public Object get(IocMaking ing) {
			return ing.getIoc().getContext();
		}

		@Override
		public String toString() {
			return IocConstants.IOC_CONTEXT;
		}
	};

	private String name;
	private Class<?> type;
	private boolean required = true;

	public static KeyValue<String, Class<?>> parse(String name) {
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
		return new KeyValue<String, Class<?>>(_name, type);
	}

	public ReferValue(String expr, boolean required) {
		KeyValue<String, Class<?>> p = parse(expr);
		this.name = p.getKey();
		this.type = p.getValue();
		this.required = required;
	}

	public ReferValue(Class<?> type, boolean required) {
		this.type = type;
		this.required = required;
	}

	public ReferValue(String expr, Class<?> type, boolean required) {
		KeyValue<String, Class<?>> p = parse(expr);
		this.name = p.getKey();
		this.type = p.getValue() == null ? type : p.getValue();
		this.required = required;
	}

	@Override
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

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("type", type)
				.append("required", required)
				.toString();
	}
}
