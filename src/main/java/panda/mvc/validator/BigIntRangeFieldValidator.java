package panda.mvc.validator;

import java.math.BigInteger;

public class BigIntRangeFieldValidator extends AbstractRangeValidator {

	private BigInteger max = null;
	private BigInteger min = null;

	/**
	 * @return the max
	 */
	public BigInteger getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(BigInteger max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public BigInteger getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(BigInteger min) {
		this.min = min;
	}

	protected Comparable getMaxComparatorValue() {
		return max;
	}

	protected Comparable getMinComparatorValue() {
		return min;
	}
}
