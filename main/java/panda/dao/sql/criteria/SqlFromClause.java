package panda.dao.sql.criteria;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlFromClause {

	private String table;

	/**
	 * constructor
	 */
	public SqlFromClause() {
	}

	/**
	 * @return table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		checkTable(table);
		this.table = table;
	}

	/**
	 * checkTable
	 * 
	 * @param table table
	 */
	protected void checkTable(String table) {
		if (Strings.isBlank(table)) {
			throw new IllegalArgumentException("table cannot be blank string");
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder().append(table).toHashCode();
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

		SqlFromClause rhs = (SqlFromClause) obj;
		return Objects.equalsBuilder().append(table, rhs.table).isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("table", table)
				.toString();
	}
}
