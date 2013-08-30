package panda.dao.sql.criteria;


/**
 * CompareColumnExpression
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
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
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((compareColumn == null) ? 0 : compareColumn.hashCode());
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
		CompareColumnExpression other = (CompareColumnExpression) obj;
		if (compareColumn == null) {
			if (other.compareColumn != null)
				return false;
		}
		else if (!compareColumn.equals(other.compareColumn))
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
		sb.append(super.toString());
		sb.append(", ");
		sb.append("compareColumn: ").append(compareColumn);
		sb.append(" }");

		return sb.toString();
	}
}
