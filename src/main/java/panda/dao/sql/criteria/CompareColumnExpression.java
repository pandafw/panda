package panda.dao.sql.criteria;

import panda.lang.Objects;


/**
 * @author yf.frank.wang@gmail.com
 */
public class CompareColumnExpression extends SimpleExpression {

	protected String compareColumn;

	/**
	 * @return the compareColumn
	 */
	public String getCompareColumn() {
		return compareColumn;
	}

	/**
	 * @param compareColumn the compareColumn to set
	 */
	public void setCompareColumn(String compareColumn) {
		checkColumn(compareColumn);
		this.compareColumn = compareColumn;
	}

	/**
	 * Constructor
	 * @param column			column
	 * @param condition			condition
	 * @param compareColumn		compareColumn
	 */
	public CompareColumnExpression(String column, String condition, String compareColumn) {
		super(column, condition);
		setCompareColumn(compareColumn);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(compareColumn)
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

		CompareColumnExpression rhs = (CompareColumnExpression) obj;
		return Objects.equalsBuilder()
				.appendSuper(super.equals(rhs))
				.append(compareColumn, rhs.compareColumn)
				.isEquals();
	}


	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.appendSuper(super.toString())
				.append("compareColumn", compareColumn)
				.toString();
	}
}
