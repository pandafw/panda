package panda.dao.sql.criteria;

import java.io.Serializable;

import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class SqlFromClause implements Cloneable, Serializable {

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((table == null) ? 0 : table.hashCode());
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
		SqlFromClause other = (SqlFromClause) obj;
		if (table == null) {
			if (other.table != null)
				return false;
		}
		else if (!table.equals(other.table))
			return false;
		return true;
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
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("table: ").append(table);
		sb.append(" }");

		return sb.toString();
	}
}
