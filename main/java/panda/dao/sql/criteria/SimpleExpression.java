package panda.dao.sql.criteria;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleExpression extends AbstractExpression {

	protected String column;
	protected String operator;
	
	/**
	 * Constructor
	 * @param column	column
	 * @param operator	operator
	 */
	public SimpleExpression(String column, String operator) {
		setColumn(column);
		setOperator(operator);
	}
	
	/**
	 * checkColumn
	 * @param column column
	 */
	protected void checkColumn(String column) {
		if (Strings.isBlank(column)) {
			throw new IllegalArgumentException("column cannot be blank string");
		}
	}

	/**
	 * checkOperator
	 * @param operator operator
	 */ 
	protected void checkOperator(String operator) {
		if (Strings.isBlank(operator)) {
			throw new IllegalArgumentException("operator for [" + column + "] cannot be blank string");
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
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		checkColumn(column);
		this.column = column;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(column)
				.append(operator)
				.hashCode();
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
		
		SimpleExpression rhs = (SimpleExpression) obj;
		return Objects.equalsBuilder()
				.append(column, rhs.column)
				.append(operator, rhs.operator)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("column", column)
				.append("operator", operator)
				.toString();
	}

}
