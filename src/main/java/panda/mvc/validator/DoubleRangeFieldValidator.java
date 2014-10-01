package panda.mvc.validator;

public class DoubleRangeFieldValidator extends AbstractRangeValidator {

	private Double max = null;
	private Double min = null;

	/**
	 * @return the max
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Double max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Double min) {
		this.min = min;
	}

	protected Comparable getMaxComparatorValue() {
		return max;
	}

	protected Comparable getMinComparatorValue() {
		return min;
	}
}
