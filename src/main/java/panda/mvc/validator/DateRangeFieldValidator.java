package panda.mvc.validator;

import java.util.Date;

public class DateRangeFieldValidator extends AbstractRangeValidator {

	private Date max;
	private Date min;

	/**
	 * @return the max
	 */
	public Date getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Date max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Date getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Date min) {
		this.min = min;
	}

	protected Comparable getMaxComparatorValue() {
		return max;
	}

	protected Comparable getMinComparatorValue() {
		return min;
	}
}
