package panda.dao.sql.criteria;

import panda.dao.sql.SqlUtils;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(column)
				.append(direction)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		OrderExpression rhs = (OrderExpression) obj;
		return Objects.equalsBuilder()
				.append(column, rhs.column)
				.append(direction, rhs.direction)
				.isEquals();
	}
	
	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("column", column)
				.append("direction", direction)
				.toString();
	}
}
