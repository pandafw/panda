package panda.dao.sql.criteria;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SelectColumnExpression extends AbstractExpression {
	
	protected String column;
	protected String alias;
	
	/**
	 * Constructor
	 * @param column	column
	 */
	public SelectColumnExpression(String column) {
		setColumn(column);
	}
	
	/**
	 * Constructor
	 * @param column		column
	 * @param alias			alias
	 */
	public SelectColumnExpression(String column, String alias) {
		setColumn(column);
		setAlias(alias);
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
	 * @return columnAlias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param columnAlias the columnAlias to set
	 */
	public void setAlias(String columnAlias) {
		this.alias = columnAlias;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(column)
				.append(alias)
				.toHashCode();
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

		SelectColumnExpression rhs = (SelectColumnExpression) obj;
		return Objects.equalsBuilder()
				.append(column, rhs.column)
				.append(alias, rhs.alias)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("column", column)
				.append("alias", alias)
				.toString();
	}

}
