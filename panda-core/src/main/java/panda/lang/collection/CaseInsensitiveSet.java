package panda.lang.collection;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a <code>Set</code> that ignores the case of the
 * keys.
 * @param <E> the type of keys maintained by this set
 * 
 */
public class CaseInsensitiveSet<E> implements Set<E> {
	private transient Map<Object, E> map;

	public CaseInsensitiveSet() {
		map = new HashMap<Object, E>();
	}

	public CaseInsensitiveSet(Collection<? extends E> c) {
		map = new HashMap<Object, E>(Math.max((int) (c.size()/.75f) + 1, 16));
		addAll(c);
	}

	protected Object toCompareKey(Object key) {
		return key instanceof String ? ((String)key).toLowerCase() : key; 
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(toCompareKey(o));
	}

	@Override
	public Iterator<E> iterator() {
		return map.values().iterator();
	}

	@Override
	public Object[] toArray() {
		return map.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return map.values().toArray(a);
	}

	@Override
	public boolean add(E e) {
		Object ck = toCompareKey(e);
		if (map.containsKey(ck)) {
			return false;
		}
		map.put(ck, e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		Object ck = toCompareKey(o);
		if (map.containsKey(ck)) {
			map.remove(ck);
			return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object e : c) {
			if (!contains(e)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean modified = false;
		for (E e : c) {
			if (add(e)) {
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;

		Set<Object> s = new CaseInsensitiveSet<Object>(c);

		Iterator<?> it = iterator();
		while (it.hasNext()) {
			if (!s.contains(it.next())) {
				it.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		
		for (Object o : c) {
			if (remove(o)) {
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Set))
			return false;

		Collection<?> c = (Collection<?>)o;
		if (c.size() != size())
			return false;
		
		try {
			return containsAll(c);
		}
		catch (ClassCastException unused) {
			return false;
		}
		catch (NullPointerException unused) {
			return false;
		}
	}

	/**
	 * @see AbstractSet#hashCode()
	 */ 
	@Override
	public int hashCode() {
		int h = 0;
		Iterator<E> i = map.values().iterator();
		while (i.hasNext()) {
			E obj = i.next();
			if (obj != null)
				h += obj.hashCode();
		}
		return h;
	}

	@Override
	public String toString() {
		return map.values().toString();
	}
}
