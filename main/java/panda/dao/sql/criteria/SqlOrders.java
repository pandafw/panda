package panda.dao.sql.criteria;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import panda.dao.Orders;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class SqlOrders implements Orders, Cloneable, Serializable {

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expressions == null) ? 0 : expressions.hashCode());
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
		SqlOrders other = (SqlOrders) obj;
		if (expressions == null) {
			if (other.expressions != null)
				return false;
		}
		else if (!expressions.equals(other.expressions))
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
		sb.append("expressions: [").append(expressions).append(" ]");
		sb.append(" }");

		return sb.toString();
	}
}
