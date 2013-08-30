package panda.dao.sql.criteria;

/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
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
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((compareValue == null) ? 0 : compareValue.hashCode());
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
		CompareValueExpression other = (CompareValueExpression) obj;
		if (compareValue == null) {
			if (other.compareValue != null)
				return false;
		}
		else if (!compareValue.equals(other.compareValue))
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
		sb.append("compareValue: ").append(compareValue);
		sb.append(" }");

		return sb.toString();
	}

}
