package panda.dao.sql.criteria;

import panda.lang.Strings;

/**
 * SimpleExpression
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
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
		SimpleExpression other = (SimpleExpression) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		}
		else if (!column.equals(other.column))
			return false;
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
		sb.append("column: ").append(column);
		sb.append(", ");
		sb.append("operator: ").append(operator);
		sb.append(" }");

		return sb.toString();
	}

}
