package panda.dao.sql.criteria;


import java.util.ArrayList;
import java.util.List;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlOrders implements Orders {

	private List<OrderExpression> expressions;

	/**
	 * constructor
	 */
	public SqlOrders() {
		expressions = new ArrayList<OrderExpression>();
	}

	/**
	 * @return expressionList
	 */
	public List<OrderExpression> getExpressions() {
		return expressions;
	}

	/**
	 * @param expressionList the expressionList to set
	 */
	public void setExpressions(List<OrderExpression> expressionList) {
		this.expressions = expressionList;
	}
	
	/**
	 * isEmpty
	 * @return true/false
	 */
	public boolean isEmpty() {
		return expressions.isEmpty();
	}
	
	/**
	 * clear
	 */
	public void clear() {
		expressions.clear();
	}


	/**
	 * addOrder
	 * @param column		column
	 * @return this
	 */
	public SqlOrders addOrder(String column) {
		if (Strings.isNotEmpty(column)) {
			expressions.add(new OrderExpression(column));
		}
		return this;
	}

	/**
	 * addOrder
	 * @param column		column
	 * @param direction		direction
	 * @return this
	 */
	public SqlOrders addOrder(String column, String direction) {
		if (Strings.isNotEmpty(column)) {
			expressions.add(new OrderExpression(column, direction));
		}
		return this;
	}

	/**
	 * addOrderAsc
	 * @param column column
	 * @return this
	 */
	public SqlOrders addOrderAsc(String column) {
		return addOrder(column, ASC);
	}

	/**
	 * addOrderDesc
	 * @param column column
	 * @return this
	 */
	public SqlOrders addOrderDesc(String column) {
		return addOrder(column, DESC);
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

		SqlOrders rhs = (SqlOrders) obj;
		return Objects.equalsBuilder().append(expressions, rhs.expressions).isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this).append("expressions", expressions).toString();
	}
}
