package panda.wing.entity.query;

import java.util.Date;

import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.GenericQuery;
import panda.wing.entity.CBean;

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
	 * @param query the query to set
	 */
	public CQuery(GenericQuery<T> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of cusid
	 */
	public ComparableCondition<Q, Number> cusid() {
		return new ComparableCondition(this, CBean.CUSID);
	}

	/**
	 * @return condition of cusnm
	 */
	public ComparableCondition<Q, String> cusnm() {
		return new ComparableCondition(this, CBean.CUSNM);
	}

	/**
	 * @return condition of ctime
	 */
	public ComparableCondition<Q, Date> ctime() {
		return new ComparableCondition(this, CBean.CTIME);
	}
}

