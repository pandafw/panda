package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.lang.Objects;

public class StaticValue implements ValueProxy {

	public static final StaticValue NULL = new StaticValue(null);
	
	private Object obj;

	public StaticValue(Object obj) {
		this.obj = obj;
	}

	public Object get(IocMaking ing) {
		return obj;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("obj", obj)
				.toString();
	}
}
