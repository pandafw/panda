package panda.dao.sql.criteria;

import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class CompareValueExpression extends SimpleExpression {

	protected Object compareValue;

	/**
	 * @return the compareValue
	 */
	public Object getCompareValue() {
		return compareValue;
	}

	/**
	 * @param compareValue the compareValue to set
	 */
	public void setCompareValue(Object compareValue) {
		checkCompareValue(compareValue);
		this.compareValue = compareValue;
	}
	
	/**
	 * Constructor
	 * @param column		column
	 * @param condition		condition
	 * @param value			value
	 */
	public CompareValueExpression(String column, String condition, Object value) {
		super(column, condition);
		setCompareValue(value);
	}
	
	/**
	 * checkCompareValue
	 * @param compareValue compareValue
	 */ 
	protected void checkCompareValue(Object compareValue) {
		if (compareValue == null) {
			throw new IllegalArgumentException("compareValue for [" + column + "] cannot be null");
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(compareValue)
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

		CompareValueExpression rhs = (CompareValueExpression) obj;
		return Objects.equalsBuilder()
				.appendSuper(super.equals(rhs))
				.append(compareValue, rhs.compareValue)
				.isEquals();
	}


	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.appendSuper(super.toString())
				.append("compareValue", compareValue)
				.toString();
	}

}
