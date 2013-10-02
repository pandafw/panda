package panda.dao.criteria;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import panda.lang.Asserts;
import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Query {
	protected Set<String> excludes;
	protected Conditions conditions;
	protected Orders orders;
	protected int start;
	protected int limit;
	protected Map<String, Object> params;

	/**
	 * constructor
	 */
	public Query() {
	}

	/**
	 * constructor
	 * 
	 * @param qp query
	 */
	public Query(Query qp) {
		orders = qp.orders;
		conditions = qp.conditions;
		excludes = qp.excludes;
		start = qp.start;
		limit = qp.limit;
	}

	/**
	 * @return the excludes
	 */
	public Set<String> getExcludes() {
		if (excludes == null) {
			excludes = new HashSet<String>();
		}
		return excludes;
	}

	/**
	 * @param excludes the excludes to set
	 */
	public void setExcludes(Set<String> excludes) {
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
	public Query addExclude(String column) {
		getExcludes().add(column);
		return this;
	}

	/**
	 * @param column exclude column
	 * @return this
	 */
	public Query removeExclude(String column) {
		getExcludes().remove(column);
		return this;
	}

	/**
	 * clearExcludes
	 * 
	 * @return this
	 */
	public Query clearExcludes() {
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
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		Asserts.isTrue(start >= 0, "The start must >= 0");
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		Asserts.isTrue(limit >= 0, "The limit must >= 0");
		this.limit = limit;
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
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
				.append(params)
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
		
		Query rhs = (Query) obj;
		return Objects.equalsBuilder()
				.append(excludes, rhs.excludes)
				.append(conditions, rhs.conditions)
				.append(orders, rhs.orders)
				.append(start, rhs.start)
				.append(limit, rhs.limit)
				.append(params, rhs.params)
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
				.append("params", params)
				.toString();
	}
}