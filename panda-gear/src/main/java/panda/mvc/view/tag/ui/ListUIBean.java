package panda.mvc.view.tag.ui;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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

	// list style
	protected boolean listBreak;
	protected boolean listOrder;

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
	 * @param list the list object to set
	 */
	public void setList(Object list) {
		this.list = list;
	}

	public Iterator<KeyValue> asIterator() {
		return Iterators.asKeyValueIterator(list);
	}

	public Iterable<KeyValue> asIterable() {
		return Iterators.asKeyValueIterable(list);
	}

	/**
	 * @return the listBreak
	 */
	public boolean isListBreak() {
		return listBreak;
	}

	/**
	 * @param listBreak the listBreak to set
	 */
	public void setListBreak(boolean listBreak) {
		this.listBreak = listBreak;
	}

	/**
	 * @return the listOrder
	 */
	public boolean isListOrder() {
		return listOrder;
	}

	/**
	 * @param listOrder the listOrder to set
	 */
	public void setListOrder(boolean listOrder) {
		this.listOrder = listOrder;
	}
}
