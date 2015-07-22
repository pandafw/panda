package panda.mvc.view.tag.ui;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.collection.KeyValue;

/**
 * ListUIBean is the standard superclass of all list handling components.
 * <p/>
 * <!-- START SNIPPET: javadoc --> Note that the listkey and listvalue attribute will default to
 * "key" and "value" respectively only when the list attribute is evaluated to a Map or its
 * decendant. Everything else will result in listkey and listvalue to be null and not used. <!-- END
 * SNIPPET: javadoc -->
 */
public abstract class ListUIBean extends InputUIBean {
	protected Object list;
	protected int listSize = 0;

	@Override
	public void evaluateParams() {
		super.evaluateParams();
		
		if (list == null) {
			return;
		}

		if (list instanceof Collection) {
			listSize = ((Collection)list).size();
		}
		else if (list instanceof Map) {
			listSize = ((Map)list).size();
		}
		else if (list.getClass().isArray()) {
			listSize = Array.getLength(list);
		}
		else {
			throw new IllegalArgumentException("The list value is not a Collection/Map/Array object: " + (list instanceof String ? list : list.getClass()));
		}
	}

	public boolean contains(Object obj1, Object obj2) {
		return Objects.contains(obj1, obj2);
	}

	public boolean isEmptyList() {
		return listSize == 0;
	}

	public boolean isNotEmptyList() {
		return listSize > 0;
	}
	
	/**
	 * @return the listSize
	 */
	public int getListSize() {
		return listSize;
	}

	/**
	 * @param listSize the listSize to set
	 */
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	/**
	 * @return the list
	 */
	public Object getList() {
		return list;
	}

	/**
	 * Iterable source to populate from. If the list is a Map (key, value), the Map key will become
	 * the option 'value' parameter and the Map value will become the option body.
	 */
	public void setList(Object list) {
		this.list = list;
	}

	public Iterator<KeyValue> asIterator() {
		if (!Iterators.isIterable(list)) {
			return null;
		}
		
		return new KeyValueIterator(list);
	}

	public Iterable<KeyValue> asIterable() {
		if (list == null) {
			return null;
		}
		
		return new KeyValueIterator(list);
	}

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
}
