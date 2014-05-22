package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocValue;

public class ArrayValue implements ValueProxy {

	private ValueProxy[] values;

	public ArrayValue(IocMaking ing, IocValue[] array) {
		values = new ValueProxy[array.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = ing.makeValueProxy(array[i]);
		}
	}

	public Object get(IocMaking ing) {
		Object[] re = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			re[i] = values[i].get(ing);
		}
		return re;
	}

}
