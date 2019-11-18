package panda.lang;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.lang.collection.KeyValue;

/**
 * utility class for Iterator. 
 */
@SuppressWarnings("rawtypes")
public abstract class Iterators {
	/**
	 * Determine whether a given object can be made into an <code>Iterator</code>
	 * 
	 * @param object the object to check
	 * @return <code>true</code> if the object can be converted to an iterator and
	 *         <code>false</code> otherwise
	 */
	public static boolean isIterable(Object object) {
		if (object == null) {
			return false;
		}

		if (object instanceof Iterable) {
			return true;
		}
		else if (object.getClass().isArray()) {
			return true;
		}
		else if (object instanceof Enumeration) {
			return true;
		}
		else if (object instanceof Iterator) {
			return true;
		}
		else {
			return false;
		}
	}

	// Iterator for enumerations
	public static class EnumerationIterator<E> implements Iterator<E>, Iterable<E> {
		Enumeration<E> enumeration;

		public EnumerationIterator(Enumeration<E> aEnum) {
			enumeration = aEnum;
		}

		@Override
		public boolean hasNext() {
			return enumeration.hasMoreElements();
		}

		@Override
		public E next() {
			return enumeration.nextElement();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Remove is not supported in EnumerationIterator.");
		}

		@Override
		public Iterator<E> iterator() {
			return this;
		}
	}

	// Iterator for Array
	public static class ArrayIterator implements Iterator, Iterable {
		Object array;
		int index;
		int size;

		public ArrayIterator(Object array) {
			this.array = array;
			index = 0;
			size = Array.getLength(array);
		}

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public Object next() {
			return Array.get(array, index++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Remove is not supported in ArrayIterator.");
		}

		@Override
		public Iterator iterator() {
			return this;
		}
	}
	
	public static class SingleIterator<T> implements Iterator<T>, Iterable<T> {
		T obj;
		
		public SingleIterator(T obj) {
			this.obj = obj;
		}
		
		@Override
		public boolean hasNext() {
			return obj != null;
		}

		@Override
		public T next() {
			T o = obj;
			obj = null;
			return o;
		}

		@Override
		public void remove() {
			throw Exceptions.unsupported("Remove unsupported on SingleIterator");
		}

		@Override
		public Iterator<T> iterator() {
			return this;
		}
	}

	public static class IteratorIterable implements Iterable {
		private final Iterator iterator;
		
		public IteratorIterable(Iterator iterator) {
			this.iterator = iterator;
		}

		public Iterator iterator() {
			return iterator;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Iterator asIterator(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Iterator) {
			return (Iterator)value;
		}

		if (value instanceof Object[]) {
			return Arrays.asList((Object[])value).iterator();
		}

		if (value instanceof Iterable) {
			return ((Iterable)value).iterator();
		}

		if (value.getClass().isArray()) {
			return new ArrayIterator(value);
		}
		
		if (value instanceof Enumeration) {
			return new EnumerationIterator((Enumeration)value);
		}
		
		return new SingleIterator(value);
	}

	@SuppressWarnings("unchecked")
	public static Iterable asIterable(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Iterable) {
			return (Iterable)value;
		}

		if (value instanceof Iterator) {
			return new IteratorIterable((Iterator)value);
		}

		if (value instanceof Object[]) {
			return Arrays.asList((Object[])value);
		}

		if (value.getClass().isArray()) {
			return new ArrayIterator(value);
		}
		
		if (value instanceof Enumeration) {
			return new EnumerationIterator((Enumeration)value);
		}

		return new SingleIterator(value);
	}
	
	//----------------------------------------------------------
	public static class KeyValueIterator implements Iterator<KeyValue>, Iterable<KeyValue> {
		Iterator it;

		public KeyValueIterator(Object list) {
			if (list instanceof Map) {
				it = ((Map)list).entrySet().iterator();
			}
			else {
				it = Iterators.asIterator(list);
			}
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		@SuppressWarnings("unchecked")
		public KeyValue next() {
			Object o = it.next();
			if (o instanceof Entry) {
				return new KeyValue(((Entry)o).getKey(), ((Entry)o).getValue());
			}
			return new KeyValue(o, o);
		}

		public void remove() {
			throw new UnsupportedOperationException("Remove is not supported.");
		}

		public Iterator<KeyValue> iterator() {
			return this;
		}
	}

	public static Iterator<KeyValue> asKeyValueIterator(Object value) {
		if (!Iterators.isIterable(value)) {
			return null;
		}
		
		return new KeyValueIterator(value);
	}

	public static Iterable<KeyValue> asKeyValueIterable(Object value) {
		if (value == null) {
			return null;
		}
		
		return new KeyValueIterator(value);
	}

	public static <T> List<T> toList(Iterator<T> it) {
		if (it == null) {
			return null;
		}
		
		List<T> c = new ArrayList<T>();
		while (it.hasNext()) {
			c.add(it.next());
		}
		return c;
	}

	public static <T> Set<T> toSet(Iterator<T> it) {
		if (it == null) {
			return null;
		}
		
		Set<T> c = new HashSet<T>();
		while (it.hasNext()) {
			c.add(it.next());
		}
		return c;
	}

	public static <T> Set<T> toLinkedSet(Iterator<T> it) {
		if (it == null) {
			return null;
		}
		
		Set<T> c = new LinkedHashSet<T>();
		while (it.hasNext()) {
			c.add(it.next());
		}
		return c;
	}
}
