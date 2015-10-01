package panda.ioc.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.bind.json.JsonObject;
import panda.cast.CastException;
import panda.cast.Castors;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Iterators;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

/**
 * 从一个 Map 对象中读取配置信息，支持 Parent
 */
public class MapIocLoader extends AbstractIocLoader {

	private static final Log log = Logs.getLog(MapIocLoader.class);

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
			if (null != p) {
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

		IocObject io = new IocObject();
		io.setValue(obj);
		io.setType(obj.getClass());
		return io;
	}

	/**
	 * 检查继承关系,如果发现循环继承,或其他错误的继承关系,则抛出ObjectLoadException
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
				if (!Strings.isBlank(typeName)) {
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
			if (null != v) {
				iobj.setScope(v.toString());
			}
			
			// events
			try {
				v = map.get("events");
				if (null != v) {
					IocEventSet ies = Castors.i().cast(v, IocEventSet.class);
					iobj.setEvents(ies);
				}
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Wrong events: " + v, e);
			}
			
			// args
			try {
				v = map.get("args");
				if (null != v) {
					for (Object o : Iterators.asIterable(v)) {
						iobj.addArg(object2value(o));
					}
				}
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Wrong args: " + v, e);
			}
			
			// fields
			try {
				v = map.get("fields");
				if (null != v) {
					Map<String, Object> fields = (Map<String, Object>)v;
					for (Entry<String, Object> en : fields.entrySet()) {
						iobj.addField(en.getKey(), object2value(en.getValue()));
					}
				}
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Wrong fields: " + v, e);
			}
			
			// factory方法
			v = map.get("factory");
			if (v != null && !Strings.isBlank(v.toString())) {
				iobj.setFactory(v.toString());
			}
		}
		else {
			for (Entry<String, Object> en : map.entrySet()) {
				iobj.addField(en.getKey(), object2value(en.getValue()));
			}
			if (log.isWarnEnabled()) {
				log.warn("Using *Declared* ioc-define (without type or events)!!! Please use Standard Ioc-Define!!"
						+ " Bean will define as: " + iobj);
			}
		}
		return iobj;
	}

	@SuppressWarnings("unchecked")
	private IocValue object2value(Object obj) {
		// Null
		if (null == obj) {
			return new IocValue(IocValue.TYPE_NULL);
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
			IocValue iv = new IocValue(IocValue.TYPE_NORMAL);
			Map<String, Object> map = (Map<String, Object>)obj;
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
			IocValue iv = new IocValue(IocValue.TYPE_NORMAL);
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
			IocValue iv = new IocValue(IocValue.TYPE_NORMAL);
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
		return new IocValue(IocValue.TYPE_NORMAL, obj);
	}

	private IocValue convert(String value) {
		return Loaders.convert(value, IocValue.TYPE_NORMAL);
	}
}
