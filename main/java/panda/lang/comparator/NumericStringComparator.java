package panda.lang.comparator;

import java.math.BigDecimal;
import java.util.Comparator;

import panda.lang.Objects;

/**
 * number comparator for string
 * @author yf.frank.wang@gmail.coms
 */
public class NumericStringComparator implements Comparator<String> {
	
	private int radix;

	/**
	 * constructor
	 * radix = 10
	 */
	public NumericStringComparator() {
		this(10);
	}

	/**
	 * constructor
	 * @param radix radix
	 */
	public NumericStringComparator(int radix) {
		this.radix = radix;
	}
	
	/**
	 * @return radix
	 */
	public int getRadix() {
		return radix;
	}

	/**
	 * @param radix set radix
	 */
	public void setRadix(int radix) {
		this.radix = radix;
	}

	/**
	 * @param o1 string1
	 * @param o2 string2
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 */
	public int compare(String o1, String o2) {
		BigDecimal n1, n2;
		
		try {
			n1 = new BigDecimal(o1);
		}
		catch (NumberFormatException e) {
			return -1;
		}

		try {
			n2 = new BigDecimal(o2);
		}
		catch (NumberFormatException e) {
			return 1;
		}

		return n1.compareTo(n2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this).append("radix", radix).toString();
	}
	
}
