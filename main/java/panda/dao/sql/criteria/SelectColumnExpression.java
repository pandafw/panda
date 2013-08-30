package panda.dao.sql.criteria;

import panda.lang.Strings;

/**
 * SelectColumnExpression
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
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
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
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
		SelectColumnExpression other = (SelectColumnExpression) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		}
		else if (!column.equals(other.column))
			return false;
		if (alias == null) {
			if (other.alias != null)
				return false;
		}
		else if (!alias.equals(other.alias))
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
		sb.append("alias: ").append(alias);
		sb.append(" }");
		
		return sb.toString();
	}

}
