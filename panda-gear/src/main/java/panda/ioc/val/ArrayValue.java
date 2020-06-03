package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocValue;
import panda.lang.Strings;

public class ArrayValue implements ValueProxy {

	private ValueProxy[] values;

	public ArrayValue(IocMaking im, IocValue[] array) {
		values = new ValueProxy[array.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = im.makeValueProxy(array[i]);
		}
	}

	@Override
	public Object get(IocMaking im) {
		Object[] re = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			re[i] = values[i].get(im);
		}
		return re;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + Strings.join(values, ", ");
	}
}
