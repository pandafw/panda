package panda.wing.entity.query;

import java.util.Date;

import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.GenericQuery;
import panda.wing.entity.SCUBean;
import panda.wing.entity.SUBean;

@SuppressWarnings("unchecked")
public class SUQuery<T extends SUBean, Q extends SUQuery> extends EntityQuery<T, Q> {
	/**
	 * Constructor
	 * @param type table type
	 */
	public SUQuery(Class<T> type) {
		super(type);
	}

	/**
	 * @param query the query to set
	 */
	public SUQuery(GenericQuery<T> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of status
	 */
	public ComparableCondition<Q, Character> status() {
		return new ComparableCondition(this, SCUBean.STATUS);
	}

	/**
	 * @return condition of uusid
	 */
	public ComparableCondition<Q, Number> uusid() {
		return new ComparableCondition(this, SCUBean.UUSID);
	}

	/**
	 * @return condition of uusnm
	 */
	public ComparableCondition<Q, String> uusnm() {
		return new ComparableCondition(this, SCUBean.UUSNM);
	}

	/**
	 * @return condition of utime
	 */
	public ComparableCondition<Q, Date> utime() {
		return new ComparableCondition(this, SCUBean.UTIME);
	}
}

