package panda.dao.sql.criteria;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
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
		return Objects.hashCodeBuilder()
				.append(operator)
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

		ConjunctionExpression rhs = (ConjunctionExpression) obj;
		return Objects.equalsBuilder()
				.append(operator, rhs.operator)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("operator", operator)
				.toString();
	}
}
