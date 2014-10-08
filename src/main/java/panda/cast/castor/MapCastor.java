package panda.cast.castor;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.bean.BeanHandler;
import panda.cast.CastContext;
import panda.cast.Castor;
import panda.cast.Castors;
import panda.lang.reflect.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> target type
 */
public class MapCastor<T extends Map<?,?>> extends AnyJsonCastor<T> {
	private Castors castors;
	private Type keyType;
	private Type valType;
	private Castor keyCastor;
	private Castor valCastor;
	
	public MapCastor(Type toType, Castors castors) {
		super(toType);
		
		if (!Types.isAssignable(toType, Map.class)) {
			throw new IllegalArgumentException("The argument is not a map type: " + toType);
		}

		this.castors = castors;
		
		Type[] ts = Types.getMapKeyAndValueTypes(toType);
		this.keyType = ts[0];
		this.valType = ts[1];
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean isAssignable(Object value) {
		if (super.isAssignable(value)) {
			if (isObjectType(keyType) && isObjectType(valType)) {
				return true;
			}

			Map m = (Map)value;
			if (m.isEmpty()) {
				return true;
			}

			boolean assignable = true;
			for (Entry en : (Set<Entry>)m.entrySet()) {
				Object key = en.getKey();
				if (key != null && !Types.isAssignable(key.getClass(), keyType)) {
					assignable = false;
					break;
				}

				Object val = en.getValue();
				if (val != null && !Types.isAssignable(val.getClass(), valType)) {
					assignable = false;
					break;
				}
			}
			return assignable;
		}
		return false;
	}

	@Override
	protected T castValue(Object value, CastContext context) {
		T map = createTarget();
		return castValueTo(value, map, context);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected T castValueTo(Object value, T target, CastContext context) {
		if (keyCastor == null) {
			keyCastor = castors.getCastor(keyType);
		}
		if (valCastor == null) {
			valCastor = castors.getCastor(valType);
		}
		
		Map map = target;
		map.clear();
		if (value.getClass().isArray()) {
			int size = Array.getLength(value);

			for (int i = 0; i < size; i++) {
				Object v = Array.get(value, i);
				if (v instanceof Entry) {
					Map.Entry kv = castChild(context, keyCastor, valCastor, String.valueOf(i), ((Entry)v).getKey(), ((Entry)v).getValue());
					if (kv != null) {
						map.put(kv.getKey(), kv.getValue());
					}
				}
				else {
					Map.Entry kv = castChild(context, keyCastor, valCastor, String.valueOf(i), v, null);
					if (kv != null) {
						map.put(kv.getKey(), kv.getValue());
					}
				}
			}
		}
		else if (value instanceof Map) {
			int i = 0;
			for (Entry en : (Set<Entry>)((Map)value).entrySet()) {
				Map.Entry kv = castChild(context, keyCastor, valCastor, String.valueOf(i++), en.getKey(), en.getValue());
				if (kv != null) {
					map.put(kv.getKey(), kv.getValue());
				}
			}
		}
		else if (value instanceof Iterable) {
			Iterator it = ((Iterable)value).iterator();
			for (int i = 0; it.hasNext(); i++) {
				Object v = it.next();
				if (v instanceof Entry) {
					Map.Entry kv = castChild(context, keyCastor, valCastor, String.valueOf(i), ((Entry)v).getKey(), ((Entry)v).getValue());
					if (kv != null) {
						map.put(kv.getKey(), kv.getValue());
					}
				}
				else {
					Map.Entry kv = castChild(context, keyCastor, valCastor, String.valueOf(i), v, null);
					if (kv != null) {
						map.put(kv.getKey(), kv.getValue());
					}
				}
			}
		}
		else {
			BeanHandler bh = castors.getBeanHandler(value.getClass());
			for (String key : bh.getReadPropertyNames()) {
				Object val = bh.getPropertyValue(value, key);
				Map.Entry kv = castChild(context, keyCastor, valCastor, key, key, val);
				if (kv != null) {
					map.put(kv.getKey(), kv.getValue());
				}
			}
		}
		return (T)map; 
	}
}
