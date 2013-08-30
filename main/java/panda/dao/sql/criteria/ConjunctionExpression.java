package panda.dao.sql.criteria;

import panda.lang.Strings;

/**
 * ConjunctionExpression
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class ConjunctionExpression extends AbstractExpression {

	protected String operator;
	
	/**
	 * Constructor
	 * @param operator	operator
	 */
	public ConjunctionExpression(String operator) {
		setOperator(operator);
	}
	
	/**
	 * checkOperator
	 * @param operator operator
	 */ 
	protected void checkOperator(String operator) {
		if (Strings.isBlank(operator)) {
			throw new IllegalArgumentException("operator cannot be blank string");
		}
		if (!operator.equalsIgnoreCase(AND) && !operator.equalsIgnoreCase(OR)) {
			throw new IllegalArgumentException("operator '" + operator + "' must be " + AND + " | " + OR);
		}
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		checkOperator(operator);
		this.operator = operator;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
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
		ConjunctionExpression other = (ConjunctionExpression) obj;
		if (operator == null) {
			if (other.operator != null)
				return false;
		}
		else if (!operator.equals(other.operator))
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
		sb.append("operator: ").append(operator);
		sb.append(" }");

		return sb.toString();
	}
}
