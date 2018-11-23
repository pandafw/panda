package panda.lang.comparator;

import java.util.Comparator;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.lang.Asserts;
import panda.lang.Objects;



/**
 * comparator for property
 * 
 * @param <T> the object type
 */
public class PropertyComparator<T> implements Comparator<T> {
	protected BeanHandler<T> bh;
	protected String prop;
	
	/**
	 * @param type class type
	 * @param prop property name
	 */
	public PropertyComparator(Class<T> type, String prop) {
		Asserts.notNull(type);
		Asserts.notEmpty(prop);

		this.bh = Beans.i().getBeanHandler(type);
		this.prop = prop;
	}

	/**
	 * @param o1 object1
	 * @param o2 object2
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}

		Object v1 = bh.getPropertyValue(o1, prop);
		Object v2 = bh.getPropertyValue(o2, prop);
		if (v1 == v2) {
			return 0;
		}

		if (v1 == null) {
			return -1;
		}

		if (v2 == null) {
			return 1;
		}
		
		if (v1 instanceof Comparable && v1.getClass().isAssignableFrom(v2.getClass())) {
			return ((Comparable)v1).compareTo(v2);
		}
		
		if (v2 instanceof Comparable && v2.getClass().isAssignableFrom(v1.getClass())) {
			return -((Comparable)v2).compareTo(v1);
		}

		return v1.toString().compareTo(v2.toString());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("type", bh.getType())
				.append("prop", prop)
				.toString();
	}
}
