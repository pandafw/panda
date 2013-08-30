package panda.castor.castors;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import panda.castor.AbstractCastor;
import panda.castor.CastContext;
import panda.castor.Castor;
import panda.castor.Castors;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <S> source type
 * @param <T> target type
 */
public class ArrayCastor<S, T> extends AbstractCastor<S, T> {
	private Castors castors;
	private Type toComponentType;
	
	public ArrayCastor(Type fromType, Type toType, Castors castors) {
		super(fromType, toType);
		
		this.castors = castors;

		if (!Types.isArrayType(toType)) {
			throw new IllegalArgumentException("The argument is not a array type: " + toType);
		}
		toComponentType = Types.getArrayComponentType(toType);
	}

	private Type getFromComponentType() {
		Type cType = Types.getArrayElementType(fromType);
		return cType == null ? Object.class : cType;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T convertValue(Object value, CastContext context) {
		if (value.getClass().isArray()) {
			Type fType = value.getClass().getComponentType();
			Castor conv = castors.getCastor(fType, toComponentType);

			int size = Array.getLength(value);
			Object array = Arrays.newInstance(toComponentType, size);
			for (int i = 0; i < size; i++) {
				Object v = Array.get(value, i);
				v = castChild(context, conv, i, v);
				Array.set(array, i, v);
			}
			return (T)array; 
		}
		else if (value instanceof Iterable) {
			Type fType = getFromComponentType();
			Castor conv = castors.getCastor(fType, toComponentType);

			int i = 0;
			List list = new ArrayList();
			Iterator it = ((Iterable)value).iterator();
			while (it.hasNext()) {
				Object v = it.next();
				list.add(castChild(context, conv, i++, v));
			}

			Object array = Arrays.newInstance(toComponentType, list.size());
			int index = 0;
			for (Object v : list) {
				Array.set(array, index++, v);
			}
			return (T)array; 
		}
		else {
			if (byte.class.equals(toComponentType)) {
				try {
					if (value instanceof InputStream) {
						return (T)Streams.toByteArray((InputStream)value);
					}
					if (value instanceof Reader) {
						return (T)Streams.toByteArray((Reader)value, Charsets.UTF_8);
					}
					if (value instanceof Blob) {
						return (T)((Blob)value).getBytes(0, (int)((Blob)value).length());
					}
					return (T)value.toString().getBytes(Charsets.CS_UTF_8);
				}
				catch (SQLException e) {
					Exceptions.wrapThrow(e);
				}
				catch (IOException e) {
					Exceptions.wrapThrow(e);
				}
			}
			if (char.class.equals(toComponentType)) {
				try {
					if (value instanceof InputStream) {
						return (T)Streams.toCharArray((InputStream)value, Charsets.UTF_8);
					}
					if (value instanceof Reader) {
						return (T)Streams.toCharArray((Reader)value);
					}
					if (value instanceof Clob) {
						return (T)Streams.toCharArray(((Clob)value).getCharacterStream());
					}
					return (T)value.toString().toCharArray();
				}
				catch (SQLException e) {
					Exceptions.wrapThrow(e);
				}
				catch (IOException e) {
					Exceptions.wrapThrow(e);
				}
			}

			Object array = Arrays.newInstance(toComponentType, 1);
			Castor conv = castors.getCastor(value.getClass(), toComponentType);
			Array.set(array, 0, castChild(context, conv, 0, value));
			return (T)array;
		}
	}
}
