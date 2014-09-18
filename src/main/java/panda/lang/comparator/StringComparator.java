package panda.lang.comparator;

import java.util.Comparator;

import panda.lang.Objects;


/**
 * string comparator for string
 * @author yf.frank.wang@gmail.com
 */
public class StringComparator implements Comparator<String> {

	private boolean ignoreCase;
	
	/**
	 * 
	 */
	public StringComparator() {
	}

	/**
	 * @param ignoreCase set the ignoreCase
	 */
	public StringComparator(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	/**
	 * @return the ignoreCase
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * @param ignoreCase the ignoreCase to set
	 */
	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	/**
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String o1, String o2) {
		return ignoreCase ? o1.compareToIgnoreCase(o2) : o1.compareTo(o2);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
			.append("ignoreCase", ignoreCase)
			.toString();
	}
}
