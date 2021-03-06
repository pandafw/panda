package panda.ioc.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.bind.json.JsonObject;
import panda.cast.CastException;
import panda.cast.Castors;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocParam;
import panda.ioc.meta.IocValue;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Iterators;
import panda.lang.Strings;

/**
 * 从一个 Map 对象中读取配置信息，支持 Parent
 */
public class MapIocLoader extends AbstractIocLoader {

	protected MapIocLoader() {
	}

	public MapIocLoader(String json) {
		Map<String, Object> m = JsonObject.fromJson(json);
		initialize(m);
	}
	
	public MapIocLoader(Map<String, Object> map) {
		initialize(map);
	}
	
	protected void initialize(Map<String, Object> map) {
		for (Entry<String, Object> en : map.entrySet()) {
			String key = en.getKey();
			if (!beans.containsKey(key)) {
				IocObject io = parseBean(map, key, en.getValue());
				beans.put(key, io);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected IocObject parseBean(Map<String, Object> map, String name, Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Illegal null define: " + name);
		}
		
		if (obj instanceof Map) {
			Map<String, Object> m = (Map)obj;
			
			// If has parent
			Object p = m.get("parent");
			if (p != null) {
				checkParents(map, name);
				IocObject parent = beans.get((String)p);
				if (parent == null) {
					parent = parseBean(map, (String)p, map.get(p));
				}
				
				// create new map without parent
				Map<String, Object> newMap = new HashMap<String, Object>();
				for (Entry<String, Object> en : m.entrySet()) {
					if ("parent".equals(en.getKey())) {
						continue;
					}
					newMap.put(en.getKey(), en.getValue());
				}
				
				// Create self IocObject
				IocObject self = map2iobj(newMap);
	
				// Merge with parent
				return Loaders.mergeWith(self, parent);
			}
			return map2iobj(m);
		}
		
		if (obj instanceof Collection) {
			return col2iobj((Collection)obj);
		}
		
		if (obj.getClass().isArray()) {
			return array2iobj((Object[])obj);
		}

		IocObject io = new IocObject();
		io.setValue(obj);
		io.setType(obj.getClass());
		return io;
	}

	/**
	 * 检查继承关系,如果发现循环继承,或其他错误的继承关系,则抛出IllegalArgumentException
	 * 
	 * @param name beanId
	 */
	@SuppressWarnings("unchecked")
	private void checkParents(Map<String, Object> map, String name) {
		ArrayList<String> list = new ArrayList<String>();
		String cp = name;
		while (cp != null) {
			list.add(cp);
			Object obj = map.get(cp);
			if (obj instanceof Map) {
				cp = (String)((Map<String, Object>)obj).get("parent");
			}
			if (list.contains(cp)) {
				throw new IllegalArgumentException("Cycled parent (id: " + name + ", parent: " + cp + ")");
			}
		}
	}
	
	private static final Set<String> OBJFIELDS = Arrays.toSet("type", "scope", "singleton", "fields", "args", "events", "factory");
	private boolean isIocObject(Map<String, ?> map) {
		for (Entry<String, ?> en : map.entrySet()) {
			if (!OBJFIELDS.contains(en.getKey())) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private IocObject map2iobj(Map<String, Object> map) {
		final IocObject iobj = new IocObject();
		if (isIocObject(map)) {
			Object v = map.get("type");
			// type
			try {
				String typeName = (String)v;
				if (!Strings.isEmpty(typeName)) {
					iobj.setType(Classes.getClass(typeName, false));
				}
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Wrong type: " + v, e);
			}
			
			// singleton
			try {
				v = map.get("singleton");
				if (v != null) {
					boolean b = (Boolean)Castors.scast(v, boolean.class);
					iobj.setSingleton(b);
				}
			}
			catch (CastException e) {
				throw new IllegalArgumentException("Wrong singleton: " + v, e);
			}

			// scope
			v = map.get("scope");
			if (v != null) {
				iobj.setScope(v.toString());
			}
			
			// events
			try {
				v = map.get("events");
				if (v != null) {
					IocEventSet ies = Castors.i().cast(v, IocEventSet.class);
					if (ies != null && ies.isNotEmpty()) {
						iobj.setEvents(ies);
					}
				}
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Wrong events: " + v, e);
			}
			
			// args
			try {
				v = map.get("args");
				if (v != null) {
					List<IocValue> ivs = new ArrayList<IocValue>();
					for (Object o : Iterators.asIterable(v)) {
						ivs.add(object2value(o));
					}
					iobj.setArgs(ivs.toArray(new IocValue[ivs.size()]));
				}
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Wrong args: " + v, e);
			}
			
			// fields
			try {
				v = map.get("fields");
				if (v != null) {
					Map<String, Object> fields = (Map<String, Object>)v;
					for (Entry<String, Object> en : fields.entrySet()) {
						iobj.addField(en.getKey(), object2param(en.getValue()));
					}
				}
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Wrong fields: " + v, e);
			}
			
			// factory方法
			v = map.get("factory");
			if (v != null && Strings.isNotEmpty(v.toString())) {
				iobj.setFactory(v.toString());
			}
		}
		else {
			for (Entry<String, Object> en : map.entrySet()) {
				iobj.addField(en.getKey(), object2param(en.getValue()));
			}
		}
		return iobj;
	}

	private IocObject array2iobj(Object[] array) {
		final IocObject iobj = new IocObject();
		for (int i = 0; i < array.length; i++) {
			iobj.addField(String.valueOf(i), object2param(array[i]));
		}
		return iobj;
	}

	private IocObject col2iobj(Collection<?> list) {
		final IocObject iobj = new IocObject();
		int i = 0;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			iobj.addField(String.valueOf(i), object2param(it.next()));
			i++;
		}
		return iobj;
	}
	
	private IocParam object2param(Object obj) {
		return new IocParam(object2value(obj));
	}
	
	@SuppressWarnings("unchecked")
	private IocValue object2value(Object obj) {
		// Null
		if (obj == null) {
			return new IocValue(IocValue.KIND_NULL);
		}
		
		// IocValue
		if (obj instanceof IocValue) {
			return (IocValue)obj;
		}
		
		if (obj instanceof String) {
			return convert((String)obj);
		}

		// Map
		if (obj instanceof Map<?, ?>) {
			IocValue iv = new IocValue(IocValue.KIND_RAW);
			Map<String, Object> map = (Map<String, Object>)obj;

			// Inner
			if (map.size() > 0 && isIocObject(map)) {
				iv.setKind(IocValue.KIND_INNER);
				iv.setValue(map2iobj(map));
				return iv;
			}

			// Normal map
			Map<String, IocValue> newmap = new HashMap<String, IocValue>();
			for (Entry<String, Object> en : map.entrySet()) {
				IocValue v = object2value(en.getValue());
				newmap.put(en.getKey(), v);
			}
			iv.setValue(newmap);
			return iv;
		}
		
		// Array
		if (obj.getClass().isArray()) {
			IocValue iv = new IocValue(IocValue.KIND_RAW);
			Object[] array = (Object[])obj;
			IocValue[] ivs = new IocValue[array.length];
			for (int i = 0; i < ivs.length; i++) {
				ivs[i] = object2value(array[i]);
			}
			iv.setValue(ivs);
			return iv;
		}
		
		// Collection
		if (obj instanceof Collection<?>) {
			IocValue iv = new IocValue(IocValue.KIND_RAW);
			Collection<IocValue> values = new ArrayList<IocValue>(((Collection)obj).size());
			Iterator<?> it = ((Collection<?>)obj).iterator();
			while (it.hasNext()) {
				Object o = it.next();
				IocValue v = object2value(o);
				values.add(v);
			}
			iv.setValue(values);
			return iv;
		}
		
		// Normal
		return new IocValue(IocValue.KIND_RAW, null, obj);
	}

	protected IocValue convert(String value) {
		return Loaders.convert(IocValue.KIND_RAW, null, value);
	}
}
