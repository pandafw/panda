package panda.lang.comparator;

import java.util.Comparator;

import panda.bean.Beans;
import panda.lang.Asserts;
import panda.lang.Objects;



/**
 * comparator for property
 * @author yf.frank.wang@gmail.com
 */
public class PropertyComparator implements Comparator<Object> {
	protected String propertyName;
	
	/**
	 * @param propertyName property name
	 */
	public PropertyComparator(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		Asserts.notEmpty(propertyName);
		this.propertyName = propertyName;
	}

	/**
	 * @param o1 object1
	 * @param o2 object2
     * @return a negative integer, zero, or a positive integer as the
     * 	       first argument is less than, equal to, or greater than the
     *	       second.
	 */
	@SuppressWarnings("unchecked")
	public int compare(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1 == o2) {
			return 0;
		}
		Object v1 = null;
		try {
			v1 = Beans.getProperty(o1, propertyName);
		}
		catch (Exception e) {
		}

		Object v2 = null;
		try {
			v2 = Beans.getProperty(o2, propertyName);
		}
		catch (Exception e) {
		}
		
		if (v1 == null) {
			return -1;
		}
		if (v2 == null) {
			return 1;
		}
		
		if (v1 instanceof Comparable && v1.getClass().equals(v2.getClass())) {
			return ((Comparable)v1).compareTo(v2);
		}
		else {
			return v1.toString().compareTo(v2.toString());
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this).append("propertyName", propertyName).toString();
	}
}
