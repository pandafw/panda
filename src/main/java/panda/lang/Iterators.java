package panda.lang;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * utility class for Iterator. 
 * @author yf.frank.wang@gmail.com
 */
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

		if (object instanceof Map) {
			return true;
		}
		else if (object instanceof Iterable) {
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
	public static class EnumerationIterator implements Iterator, Iterable {
		Enumeration enumeration;

		public EnumerationIterator(Enumeration aEnum) {
			enumeration = aEnum;
		}

		public boolean hasNext() {
			return enumeration.hasMoreElements();
		}

		public Object next() {
			return enumeration.nextElement();
		}

		public void remove() {
			throw new UnsupportedOperationException("Remove is not supported in EnumerationIterator.");
		}

		public Iterator iterator() {
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

		public boolean hasNext() {
			return index < size;
		}

		public Object next() {
			return Array.get(array, index++);
		}

		public void remove() {
			throw new UnsupportedOperationException("Remove is not supported in ArrayIterator.");
		}

		public Iterator iterator() {
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
	
	public static Iterator asIterator(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Iterator) {
			return (Iterator)value;
		}

		if (value instanceof Map) {
			value = ((Map)value).entrySet();
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
		
		return Arrays.asList(value).iterator();
	}

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

		if (value instanceof Map) {
			return ((Map)value).entrySet();
		}

		if (value.getClass().isArray()) {
			return new ArrayIterator(value);
		}
		
		if (value instanceof Enumeration) {
			return new EnumerationIterator((Enumeration)value);
		}

		return Arrays.asList(value);
	}
}
