package panda.castor.castors;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

import panda.castor.AbstractCastor;
import panda.castor.CastContext;
import panda.castor.Castor;
import panda.castor.Castors;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> target type
 */
public class CollectionCastor<T extends Collection<?>> extends AbstractCastor<Object, T> {
	protected Castors castors;
	protected Type toElementType;
	
	public CollectionCastor(Type fromType, Type toType, Castors castors) {
		super(fromType, toType);
		
		if (!Types.isAssignable(toType, Collection.class)) {
			throw new IllegalArgumentException("The argument is not a collection type: " + toType);
		}

		this.castors = castors;
		this.toElementType = Types.getCollectionElementType(toType);
	}

	@Override
	protected boolean isAssignable(Object value) {
		if (super.isAssignable(value)) {
			if (isObjectType(toElementType)) {
				return true;
			}

			Collection c = (Collection)value;
			if (c.isEmpty()) {
				return true;
			}

			boolean assignable = true;
			Iterator it = c.iterator();
			while (it.hasNext()) {
				Object v = it.next();
				if (v != null && !Types.isAssignable(v.getClass(), toElementType)) {
					assignable = false;
					break;
				}
			}
			return assignable;
		}
		return false;
	}

	private Type getFromComponentType() {
		if (Types.isArrayType(fromType)) {
			return Types.getArrayComponentType(fromType);
		}
		else if (Types.isAssignable(fromType, Collection.class)) {
			return Types.getCollectionElementType(fromType);
		}
		return Object.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T convertValue(Object value, CastContext context) {
		Collection coll = createTarget();
		if (value.getClass().isArray()) {
			Type fType = value.getClass().getComponentType();
			Castor castor = castors.getCastor(fType, toElementType);
			int size = Array.getLength(value);

			for (int i = 0; i < size; i++) {
				Object v = Array.get(value, i);
				v = castChild(context, castor, i, v);
				coll.add(v);
			}
		}
		else if (value instanceof Iterable) {
			Type fType = getFromComponentType();
			Castor castor = castors.getCastor(fType, toElementType);

			int i = 0;
			Iterator it = ((Iterable)value).iterator();
			while (it.hasNext()) {
				Object v = it.next();
				v = castChild(context, castor, i++, v);
				coll.add(v);
			}
		}
		else {
			Castor castor = castors.getCastor(value.getClass(), toElementType);
			value = castChild(context, castor, 0, value);
			coll.add(value);
		}
		return (T)coll; 
	}
}
