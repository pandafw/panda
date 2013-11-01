package panda.dao.criteria.condition;

import panda.dao.criteria.Query;


/**
 * @author yf.frank.wang@gmail.com
 */
public class ConditionQuery {
	protected Query query;

	/**
	 * Constructor
	 */
	public ConditionQuery() {
		query = new Query();
	}
	
	/**
	 * @param query the query to set
	 */
	public ConditionQuery(Query query) {
		this.query = query;
	}

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return query;
	}
	
}

