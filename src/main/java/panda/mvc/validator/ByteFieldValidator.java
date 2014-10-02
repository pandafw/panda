package panda.mvc.validator;

public class ByteFieldValidator extends AbstractRangeValidator {

	private Byte max = null;
	private Byte min = null;

	/**
	 * @return the max
	 */
	public Byte getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Byte max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Byte getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Byte min) {
		this.min = min;
	}

	protected Comparable getMaxComparatorValue() {
		return max;
	}

	protected Comparable getMinComparatorValue() {
		return min;
	}
}
