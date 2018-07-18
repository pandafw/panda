package panda.ioc.val;

import java.util.ArrayList;
import java.util.Collection;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Objects;

public class CollectionValue implements ValueProxy {

	private Class<? extends Collection<Object>> type;

	private ValueProxy[] values;

	@SuppressWarnings("unchecked")
	public CollectionValue(IocMaking ing, Collection<IocValue> col, Class<? extends Collection<Object>> type) {
		this.type = (Class<? extends Collection<Object>>)(null == type ? ArrayList.class : type);
		
		int i = 0;
		values = new ValueProxy[col.size()];
		for (IocValue iv : col) {
			values[i++] = ing.makeValueProxy(iv);
		}
	}

	@Override
	public Object get(IocMaking ing) {
		Collection<Object> re = Classes.born(type);
		for (ValueProxy vp : values) {
			re.add(vp.get(ing));
		}
		return re;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("type", type)
				.append("values", values)
				.toString();
	}
}
