package panda.dao.sql.criteria;


/**
 * CompareRangeExpression
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
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
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((minValue == null) ? 0 : minValue.hashCode());
		result = prime * result + ((maxValue == null) ? 0 : maxValue.hashCode());
		return result;
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
		CompareRangeExpression other = (CompareRangeExpression) obj;
		if (minValue == null) {
			if (other.minValue != null)
				return false;
		}
		else if (!minValue.equals(other.minValue))
			return false;
		if (maxValue == null) {
			if (other.maxValue != null)
				return false;
		}
		else if (!maxValue.equals(other.maxValue))
			return false;
		return true;
	}


	/**
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append(super.toString());
		sb.append(", ");
		sb.append("minValue: ").append(minValue);
		sb.append(", ");
		sb.append("maxValue: ").append(maxValue);
		sb.append(" }");
		
		return sb.toString();
	}

}
