package panda.dao.sql.criteria;

import panda.dao.sql.SqlUtils;
import panda.lang.Strings;

/**
 * OrderBy Expression
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class OrderExpression extends AbstractExpression {

	protected String column;
	protected String direction = "";

	/**
	 * Constructor
	 * 
	 * @param column column
	 */
	public OrderExpression(String column) {
		setColumn(column);
	}
	
	/**
	 * Constructor
	 * 
	 * @param column column
	 * @param direction direction
	 */
	public OrderExpression(String column, String direction) {
		setColumn(column);
		setDirection(direction);
	}
	
	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		if (Strings.isNotBlank(direction)) {
			if (direction.compareToIgnoreCase(ASC) != 0 
					&& direction.compareToIgnoreCase(DESC) != 0) {
				throw new IllegalArgumentException("direction for [" + column + "] is not [" + ASC
						+ "] or [" + DESC + "]");
			}
			this.direction = direction;
		}
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
		if (Strings.isBlank(column)) {
			throw new IllegalArgumentException("column cannot be blank string");
		}
		if (SqlUtils.isIllegalFieldName(column)) {
			throw new IllegalArgumentException("column [" + column + "] cannot contain any non-word character");
		}
		this.column = column;
	}

	/**
	 * Clone
	 * @throws CloneNotSupportedException if clone not supported
	 * @return Clone Object
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderExpression other = (OrderExpression) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		}
		else if (!column.equals(other.column))
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		}
		else if (!direction.equals(other.direction))
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
		sb.append("direction: ").append(direction);
		sb.append(" }");

		return sb.toString();
	}
}
