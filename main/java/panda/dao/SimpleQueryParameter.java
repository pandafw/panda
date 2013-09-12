package panda.dao;

import java.util.HashMap;
import java.util.Map;

import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleQueryParameter implements QueryParameter {
	protected Map<String, Boolean> excludes;
	protected Conditions conditions;
	protected Orders orders;
	protected Integer start;
	protected Integer limit;

	/**
	 * constructor
	 */
	protected SimpleQueryParameter() {
	}

	/**
	 * constructor
	 * 
	 * @param qp queryParameter
	 */
	protected SimpleQueryParameter(SimpleQueryParameter qp) {
		orders = qp.orders;
		conditions = qp.conditions;
		excludes = qp.excludes;
		start = qp.start;
		limit = qp.limit;
	}

	/**
	 * @return the excludes
	 */
	public Map<String, Boolean> getExcludes() {
		if (excludes == null) {
			excludes = new HashMap<String, Boolean>();
		}
		return excludes;
	}

	/**
	 * @param excludes the excludes to set
	 */
	public void setExcludes(Map<String, Boolean> excludes) {
		this.excludes = excludes;
	}

	/**
	 * @return true if the excludes is not empty
	 */
	public boolean isHasExcludes() {
		return excludes != null && !excludes.isEmpty();
	}

	/**
	 * @param column exclude column
	 * @return this
	 */
	public SimpleQueryParameter addExclude(String column) {
		getExcludes().put(column, true);
		return this;
	}

	/**
	 * @param column exclude column
	 * @return this
	 */
	public SimpleQueryParameter removeExclude(String column) {
		getExcludes().remove(column);
		return this;
	}

	/**
	 * clearExcludes
	 * 
	 * @return this
	 */
	public SimpleQueryParameter clearExcludes() {
		getExcludes().clear();
		return this;
	}

	/**
	 * @return conditions
	 */
	public Conditions getConditions() {
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return orders
	 */
	public Orders getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	/**
	 * @return the start
	 */
	public Integer getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Integer start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(excludes)
				.append(conditions)
				.append(orders)
				.append(start)
				.append(limit)
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
		
		SimpleQueryParameter rhs = (SimpleQueryParameter) obj;
		return Objects.equalsBuilder()
				.append(excludes, rhs.excludes)
				.append(conditions, rhs.conditions)
				.append(orders, rhs.orders)
				.append(start, rhs.start)
				.append(limit, rhs.limit)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("excludes", excludes)
				.append("conditions", conditions)
				.append("orders", orders)
				.append("start", start)
				.append("limit", limit)
				.toString();
	}

}