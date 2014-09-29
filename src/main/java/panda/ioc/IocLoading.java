package panda.ioc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import panda.castor.CastException;
import panda.castor.Castors;
import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Iterators;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class IocLoading {

	private static final Log log = Logs.getLog(IocLoading.class);

	private Set<String> supportedTypes;

	public IocLoading() {
		this.supportedTypes = new HashSet<String>(9); // count of IocValue.TYPE_XXX
	}

	/**
	 * @return the supportedTypes
	 */
	public Set<String> getSupportedTypes() {
		return supportedTypes;
	}

	/**
	 * @param supportedTypes the supportedTypes to set
	 */
	public void setSupportedTypes(Set<String> supportedTypes) {
		this.supportedTypes = supportedTypes;
	}

	private static IocLoadException E(Throwable e, String fmt, Object... args) {
		return new IocLoadException(String.format(fmt, args), e);
	}

	private static final Pattern OBJFIELDS = Pattern.compile("^(type|scope|singleton|fields|args|events|factory)$");
	public static boolean isIocObject(Map<String, ?> map) {
		for (Entry<String, ?> en : map.entrySet()) {
			if (!OBJFIELDS.matcher(en.getKey()).matches()) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public IocObject map2iobj(Map<String, Object> map) throws IocLoadException {
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
				throw E(e, "Wrong type name: '%s'", v);
			}
			
			// singleton
			try {
				v = map.get("singleton");
				if (v != null) {
					Boolean b = Castors.scast(v, Boolean.class);
					iobj.setSingleton(Boolean.TRUE.equals(b));
				}
			}
			catch (CastException e) {
				throw E(e, "Wrong singleton: '%s'", v);
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
				throw E(e, "Wrong events: '%s'", v);
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
				throw E(e, "Wrong args: '%s'", v);
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
				throw E(e, "Wrong args: '%s'", v);
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
	IocValue object2value(Object obj) {
		IocValue iv = new IocValue();
		// Null
		if (null == obj) {
			iv.setType(IocValue.TYPE_NULL);
			return iv;
		}
		// IocValue
		else if (obj instanceof IocValue) {
			return (IocValue)obj;
		}
		// Map
		else if (obj instanceof Map<?, ?>) {
			Map<String, Object> map = (Map<String, Object>)obj;
			if (map.size() == 1) {
				Entry<String, ?> en = map.entrySet().iterator().next();
				String key = en.getKey();
				// support this type or not?
				if (supportedTypes.contains(key)) {
					iv.setType(key);
					iv.setValue(en.getValue());
					return iv;
				}
			}
			// Inner
			if (map.size() > 0 && isIocObject(map)) {
				iv.setType(IocValue.TYPE_INNER);
				try {
					iv.setValue(map2iobj(map));
				}
				catch (IocLoadException e) {
					throw Exceptions.wrapThrow(e);
				}
				return iv;
			}
			// Normal map
			Map<String, IocValue> newmap = new HashMap<String, IocValue>();
			for (Entry<String, Object> en : map.entrySet()) {
				IocValue v = object2value(en.getValue());
				newmap.put(en.getKey(), v);
			}
			iv.setType(IocValue.TYPE_NORMAL);
			iv.setValue(newmap);
			return iv;
		}
		// Array
		else if (obj.getClass().isArray()) {
			Object[] array = (Object[])obj;
			IocValue[] ivs = new IocValue[array.length];
			for (int i = 0; i < ivs.length; i++) {
				ivs[i] = object2value(array[i]);
			}
			iv.setType(IocValue.TYPE_NORMAL);
			iv.setValue(ivs);
			return iv;
		}
		// Collection
		else if (obj instanceof Collection<?>) {
			try {
				Collection<IocValue> values = new ArrayList<IocValue>(((Collection)obj).size());
				Iterator<?> it = ((Collection<?>)obj).iterator();
				while (it.hasNext()) {
					Object o = it.next();
					IocValue v = object2value(o);
					values.add(v);
				}
				iv.setType(IocValue.TYPE_NORMAL);
				iv.setValue(values);
				return iv;
			}
			catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		
		// Normal
		iv.setType(IocValue.TYPE_NORMAL);
		iv.setValue(obj);
		return iv;
	}

}
