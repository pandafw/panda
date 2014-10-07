package panda.mvc.validation.validator;

import java.util.Date;

public class DateFieldValidator extends AbstractRangeFieldValidator<Date> {

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
}
