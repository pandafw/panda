package panda.dao.sql.criteria;

import panda.dao.SimpleQueryParameter;
import panda.dao.criteria.Conditions;
import panda.dao.criteria.Orders;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlQueryParameter extends SimpleQueryParameter {
	/**
	 * Constructor
	 */
	public SqlQueryParameter() {
		super();
	}

	/**
	 * Constructor
	 * @param qp query parameter
	 */
	public SqlQueryParameter(SqlQueryParameter qp) {
		super(qp);
	}

	/**
	 * @return conditions
	 */
	public Conditions getConditions() {
		if (conditions == null) {
			conditions = new SqlConditions();
		}
		return conditions;
	}

	/**
	 * @return conditions
	 */
	public SqlConditions getSqlConditions() {
		return (SqlConditions)getConditions();
	}

	/**
	 * @return orders
	 */
	public Orders getOrders() {
		if (orders == null) {
			orders = new SqlOrders();
		}
		return orders;
	}

	/**
	 * @return orders
	 */
	public SqlOrders getSqlOrders() {
		return (SqlOrders)getOrders();
	}

	/**
	 * clear
	 */
	public void clear() {
		if (orders != null) {
			((SqlOrders)orders).clear();
		}
		if (conditions != null) {
			((SqlConditions)conditions).clear();
		}
		if (excludes != null) {
			excludes.clear();
		}
		start = null;
		limit = null;
	}
}
