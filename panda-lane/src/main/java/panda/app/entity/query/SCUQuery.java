package panda.app.entity.query;

import java.util.Date;

import panda.app.entity.SCUBean;
import panda.dao.entity.Entity;
import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.DataQuery;

@SuppressWarnings("unchecked")
public class SCUQuery<T extends SCUBean, Q extends SCUQuery> extends EntityQuery<T, Q> {
	/**
	 * Constructor
	 * @param type table type
	 */
	public SCUQuery(Class<T> type) {
		super(type);
	}

	/**
	 * constructor
	 * @param entity query entity
	 */
	public SCUQuery(Entity<T> entity) {
		super(entity);
	}

	/**
	 * @param query the query to set
	 */
	public SCUQuery(DataQuery<T> query) {
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
	 * @return condition of cusid
	 */
	public ComparableCondition<Q, Number> cusid() {
		return new ComparableCondition(this, SCUBean.CUSID);
	}

	/**
	 * @return condition of cusnm
	 */
	public ComparableCondition<Q, String> cusnm() {
		return new ComparableCondition(this, SCUBean.CUSNM);
	}

	/**
	 * @return condition of ctime
	 */
	public ComparableCondition<Q, Date> ctime() {
		return new ComparableCondition(this, SCUBean.CTIME);
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

