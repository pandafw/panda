package panda.wing.entity.query;

import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.GenericQuery;
import panda.wing.entity.SBean;

@SuppressWarnings("unchecked")
public class SQuery<T extends SBean, Q extends SQuery> extends EntityQuery<T, Q> {
	/**
	 * Constructor
	 * @param type table type
	 */
	public SQuery(Class<T> type) {
		super(type);
	}

	/**
	 * @param query the query to set
	 */
	public SQuery(GenericQuery<T> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of status
	 */
	public ComparableCondition<Q, Character> status() {
		return new ComparableCondition(this, SBean.STATUS);
	}

}

