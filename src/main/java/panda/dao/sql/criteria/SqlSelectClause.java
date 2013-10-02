package panda.dao.sql.criteria;


import java.util.ArrayList;
import java.util.List;

import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlSelectClause {

	private List<SelectColumnExpression> expressions;

	/**
	 * @return expressionList
	 */
	public List<SelectColumnExpression> getExpressions() {
		return expressions;
	}

	/**
	 * @param expressions the expressions to set
	 */
	public void setExpressions(List<SelectColumnExpression> expressions) {
		this.expressions = expressions;
	}

	/**
	 * constructor
	 */
	public SqlSelectClause() {
		expressions = new ArrayList<SelectColumnExpression>();
	}

	/**
	 * clear
	 */
	public void clear() {
		expressions.clear();
	}

	/**
	 * addSelectColumn
	 * 
	 * @param column column
	 */
	public void addSelectColumn(String column) {
		expressions.add(new SelectColumnExpression(column));
	}

	/**
	 * addSelectColumn
	 * 
	 * @param column column
	 * @param columnAlias columnAlias
	 */
	public void addSelectColumn(String column, String columnAlias) {
		expressions.add(new SelectColumnExpression(column, columnAlias));
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder().append(expressions).toHashCode();
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

		SqlSelectClause rhs = (SqlSelectClause) obj;
		return Objects.equalsBuilder()
				.append(expressions, rhs.expressions)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("expressions", expressions)
				.toString();
	}

}
