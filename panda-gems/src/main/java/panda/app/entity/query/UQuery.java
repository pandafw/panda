package panda.app.entity.query;

import java.util.Date;

import panda.app.entity.UBean;
import panda.dao.entity.Entity;
import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.DataQuery;

@SuppressWarnings("unchecked")
public class UQuery<T extends UBean, Q extends UQuery> extends EntityQuery<T, Q> {
	/**
	 * Constructor
	 * @param type table type
	 */
	public UQuery(Class<T> type) {
		super(type);
	}

	/**
	 * constructor
	 * @param entity query entity
	 */
	public UQuery(Entity<T> entity) {
		super(entity);
	}

	/**
	 * @param query the query to set
	 */
	public UQuery(DataQuery<T> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of updatedAt
	 */
	public ComparableCondition<Q, Date> updatedAt() {
		return new ComparableCondition(this, UBean.UPDATED_AT);
	}

	/**
	 * @return condition of updatedBy
	 */
	public ComparableCondition<Q, Number> updatedBy() {
		return new ComparableCondition(this, UBean.UPDATED_BY);
	}

	/**
	 * @return condition of updatedByName
	 */
	public ComparableCondition<Q, String> updatedByName() {
		return new ComparableCondition(this, UBean.UPDATED_BY_NAME);
	}
}

