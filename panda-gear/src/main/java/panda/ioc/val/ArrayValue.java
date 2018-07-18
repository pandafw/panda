package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocValue;
import panda.lang.Objects;

public class ArrayValue implements ValueProxy {

	private ValueProxy[] values;

	public ArrayValue(IocMaking ing, IocValue[] array) {
		values = new ValueProxy[array.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = ing.makeValueProxy(array[i]);
		}
	}

	@Override
	public Object get(IocMaking ing) {
		Object[] re = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			re[i] = values[i].get(ing);
		}
		return re;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder().append(values).toString();
	}
}
