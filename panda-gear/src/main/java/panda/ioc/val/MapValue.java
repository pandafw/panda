package panda.ioc.val;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Objects;

public class MapValue implements ValueProxy {

	private Class<? extends Map<String, Object>> type;

	private Map<String, ValueProxy> values;

	@SuppressWarnings("unchecked")
	public MapValue(IocMaking ing, Map<String, IocValue> map, Class<? extends Map<String, Object>> type) {
		this.type = (Class<? extends Map<String, Object>>)(null == type ? HashMap.class : type);
		values = new HashMap<String, ValueProxy>(map.size());
		for (Entry<String, IocValue> en : map.entrySet()) {
			String name = en.getKey();
			IocValue iv = en.getValue();
			values.put(name, ing.makeValueProxy(iv));
		}
	}

	@Override
	public Object get(IocMaking ing) {
		Map<String, Object> map = Classes.born(type);
		for (Entry<String, ValueProxy> en : values.entrySet()) {
			map.put(en.getKey(), en.getValue().get(ing));
		}
		return map;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("type", type)
				.append("values", values)
				.toString();
	}
}
