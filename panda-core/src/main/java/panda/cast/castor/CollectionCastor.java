package panda.cast.castor;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

import panda.cast.AbstractCastor;
import panda.cast.CastContext;
import panda.cast.Castor;
import panda.lang.Iterators;
import panda.lang.reflect.Types;

/**
 * 
 *
 * @param <T> target type
 */
public class CollectionCastor<T extends Collection<?>> extends AbstractCastor<Object, T> {
	protected Type toElementType;
	
	public CollectionCastor(Type fromType, Type toType) {
		super(fromType, toType);
		
		if (!Types.isAssignable(toType, Collection.class)) {
			throw new IllegalArgumentException("The argument is not a collection type: " + toType);
		}

		this.toElementType = Types.getCollectionElementType(toType);
	}

	@Override
	protected boolean isAssignable(Object value) {
		if (super.isAssignable(value)) {
			if (isObjectType(toElementType)) {
				return true;
			}

			Collection<?> c = (Collection<?>)value;
			if (c.isEmpty()) {
				return true;
			}

			boolean assignable = true;
			Iterator<?> it = c.iterator();
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected T castValue(Object value, CastContext context) {
		Collection coll = createTarget();
		if (value.getClass().isArray()) {
			Type fType = value.getClass().getComponentType();
			Castor castor = getCastor(context, fType, toElementType);
			int size = Array.getLength(value);

			for (int i = 0; i < size; i++) {
				Object v = Array.get(value, i);
				v = castChild(context, castor, i, v);
				coll.add(v);
			}
		}
		else if (Iterators.isIterable(value)) {
			Type fType = getFromComponentType();
			Castor castor = getCastor(context, fType, toElementType);

			int i = 0;
			Iterator it = Iterators.asIterator(value);
			while (it.hasNext()) {
				Object v = it.next();
				v = castChild(context, castor, i++, v);
				coll.add(v);
			}
		}
		else {
			Castor castor = getCastor(context, value.getClass(), toElementType);
			value = castor.cast(value, context);
			coll.add(value);
		}
		return (T)coll; 
	}
}
