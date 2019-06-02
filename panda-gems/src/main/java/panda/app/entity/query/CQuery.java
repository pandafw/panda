package panda.app.entity.query;

import java.util.Date;

import panda.app.entity.CBean;
import panda.dao.entity.Entity;
import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.DataQuery;

@SuppressWarnings("unchecked")
public class CQuery<T extends CBean, Q extends CQuery> extends EntityQuery<T, Q> {
	/**
	 * Constructor
	 * @param type table type
	 */
	public CQuery(Class<T> type) {
		super(type);
	}

	/**
	 * constructor
	 * @param entity query entity
	 */
	public CQuery(Entity<T> entity) {
		super(entity);
	}

	/**
	 * @param query the query to set
	 */
	public CQuery(DataQuery<T> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of createdAt
	 */
	public ComparableCondition<Q, Date> createdAt() {
		return new ComparableCondition(this, CBean.CREATED_AT);
	}

	/**
	 * @return condition of createdBy
	 */
	public ComparableCondition<Q, Number> createdBy() {
		return new ComparableCondition(this, CBean.CREATED_BY);
	}

	/**
	 * @return condition of createdByName
	 */
	public ComparableCondition<Q, String> createdByName() {
		return new ComparableCondition(this, CBean.CREATED_BY_NAME);
	}
}

