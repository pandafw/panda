package panda.dao.sql.criteria;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * CollectionExpression
 */
@SuppressWarnings("serial")
public class CompareCollectionExpression extends SimpleExpression {
	
	protected Object compareValues;

	/**
	 * @return the compareValues
	 */
	public Object getCompareValues() {
		return compareValues;
	}

	/**
	 * @param compareValues the compareValues to set
	 */
	public void setCompareValues(Object compareValues) {
		checkCompareValues(compareValues);
		this.compareValues = compareValues;
	}

	/**
	 * Constructor
	 * @param column		column
	 * @param condition		condition
	 * @param compareValues	compareValues
	 */
	public CompareCollectionExpression(String column, String condition, Object[] compareValues) {
		super(column, condition);
		setCompareValues(compareValues);
	}

	/**
	 * Constructor
	 * @param column		column
	 * @param condition		condition
	 * @param compareValues	compareValues
	 */
	public CompareCollectionExpression(String column, String condition, Collection compareValues) {
		super(column, condition);
		setCompareValues(compareValues);
	}
	
	/**
	 * checkCompareValues
	 * @param compareValues compareValues
	 */ 
	protected void checkCompareValues(Object compareValues) {
		if (compareValues == null 
				|| (compareValues.getClass().isArray() && Array.getLength(compareValues) == 0)
				|| (compareValues instanceof Collection && ((Collection)compareValues).size() == 0)) {
			throw new IllegalArgumentException("compareValues for [" + column + "] cannot be null or empty");
		}
	}
	
	/**
	 * checkCompareValues
	 * @param compareValues compareValues
	 */ 
	protected void checkCompareValues(Collection compareValues) {
		if (compareValues == null || compareValues.size() == 0) {
			throw new IllegalArgumentException("compareValues for [" + column + "] cannot be null or empty");
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((compareValues == null) ? 0 : compareValues.hashCode());
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
		CompareCollectionExpression other = (CompareCollectionExpression) obj;
		if (compareValues == null) {
			if (other.compareValues != null)
				return false;
		}
		else if (!compareValues.equals(other.compareValues))
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
		sb.append("compareValues: ").append(compareValues);
		sb.append(" }");

		return sb.toString();
	}

}
