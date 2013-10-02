package panda.dao.sql.criteria;

import panda.lang.Objects;


/**
 * @author yf.frank.wang@gmail.com
 */
public class CompareRangeExpression extends SimpleExpression {

	protected Object minValue;
	protected Object maxValue;

	/**
	 * @return the minValue
	 */
	public Object getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(Object minValue) {
		checkMinValue(minValue);
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public Object getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(Object maxValue) {
		checkMaxValue(maxValue);
		this.maxValue = maxValue;
	}

	/**
	 * Constructor
	 * @param column		column
	 * @param condition		condition
	 * @param minValue		minValue
	 * @param maxValue		maxValue
	 */
	public CompareRangeExpression(String column, String condition, Object minValue, Object maxValue) {
		super(column, condition);
		setMinValue(minValue);
		setMaxValue(maxValue);
	}

	/**
	 * checkMinValue
	 * @param value value
	 */ 
	protected void checkMinValue(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("minimum value for [" + column + "] cannot be null");
		}
	}

	/**
	 * checkMaxValue
	 * @param value value
	 */ 
	protected void checkMaxValue(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("maximum value for [" + column + "] cannot be null");
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(minValue)
				.append(maxValue)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;

		CompareRangeExpression rhs = (CompareRangeExpression) obj;
		return Objects.equalsBuilder()
				.appendSuper(super.equals(rhs))
				.append(minValue, rhs.minValue)
				.append(maxValue, rhs.maxValue)
				.isEquals();
	}


	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.appendSuper(super.toString())
				.append("minValue", minValue)
				.append("maxValue", maxValue)
				.toString();
	}

}
