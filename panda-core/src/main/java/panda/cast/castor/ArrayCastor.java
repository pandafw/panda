package panda.cast.castor;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import panda.cast.AbstractCastor;
import panda.cast.CastContext;
import panda.cast.Castor;
import panda.lang.Arrays;
import panda.lang.Iterators;
import panda.lang.reflect.Types;

/**
 * 
 *
 * @param <S> source type
 * @param <T> target type
 */
public class ArrayCastor<S, T> extends AbstractCastor<S, T> {
	private Type toComponentType;
	
	public ArrayCastor(Type fromType, Type toType) {
		super(fromType, toType);
		
		if (!Types.isArrayType(toType)) {
			throw new IllegalArgumentException("The argument is not a array type: " + toType);
		}
		toComponentType = Types.getArrayComponentType(toType);
	}

	private Type getFromComponentType() {
		Type cType = Types.getArrayElementType(fromType);
		return cType == null ? Object.class : cType;
	}

	private Object createArray(T target, int size) {
		if (target != null
				&& size == Array.getLength(target) 
				&& Types.isAssignable(toComponentType, target.getClass().getComponentType())) {
			return target;
		}
		
		return Arrays.newInstance(toComponentType, size);
	}
	
	@Override
	protected T castValue(S value, CastContext context) {
		return castValueTo(value, null, context);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected T castValueTo(S value, T target, CastContext context) {
		if (value.getClass().isArray()) {
			Type fType = value.getClass().getComponentType();
			Castor conv = getCastor(context, fType, toComponentType);

			int size = Array.getLength(value);
			Object array = createArray(target, size);
			for (int i = 0; i < size; i++) {
				Object v = Array.get(value, i);
				v = castChild(context, conv, i, v);
				Array.set(array, i, v);
			}
			return (T)array; 
		}
		
		if (Iterators.isIterable(value)) {
			Type fType = getFromComponentType();
			Castor conv = getCastor(context, fType, toComponentType);

			int i = 0;
			List<Object> list = new ArrayList<Object>();
			Iterator it = Iterators.asIterator(value);
			while (it.hasNext()) {
				Object v = it.next();
				list.add(castChild(context, conv, i++, v));
			}

			Object array = createArray(target, list.size());
			int index = 0;
			for (Object v : list) {
				Array.set(array, index++, v);
			}
			return (T)array; 
		}

		Object array = createArray(target, 1);
		Castor conv = getCastor(context, value.getClass(), toComponentType);
		Object cv = conv.cast(value, context);
		Array.set(array, 0, cv);
		return (T)array;
	}
}
