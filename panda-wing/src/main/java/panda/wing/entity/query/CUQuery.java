package panda.wing.entity.query;

import java.util.Date;

import panda.dao.entity.Entity;
import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.GenericQuery;
import panda.wing.entity.CUBean;

@SuppressWarnings("unchecked")
public class CUQuery<T extends CUBean, Q extends CUQuery> extends EntityQuery<T, Q> {
	/**
	 * Constructor
	 * @param type table type
	 */
	public CUQuery(Class<T> type) {
		super(type);
	}

	/**
	 * constructor
	 * @param entity query entity
	 */
	public CUQuery(Entity<T> entity) {
		super(entity);
	}

	/**
	 * @param query the query to set
	 */
	public CUQuery(GenericQuery<T> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of cusnm
	 */
	public ComparableCondition<Q, String> cusnm() {
		return new ComparableCondition(this, CUBean.CUSNM);
	}

	/**
	 * @return condition of ctime
	 */
	public ComparableCondition<Q, Date> ctime() {
		return new ComparableCondition(this, CUBean.CTIME);
	}

	/**
	 * @return condition of uusid
	 */
	public ComparableCondition<Q, Number> uusid() {
		return new ComparableCondition(this, CUBean.UUSID);
	}

	/**
	 * @return condition of uusnm
	 */
	public ComparableCondition<Q, String> uusnm() {
		return new ComparableCondition(this, CUBean.UUSNM);
	}

	/**
	 * @return condition of utime
	 */
	public ComparableCondition<Q, Date> utime() {
		return new ComparableCondition(this, CUBean.UTIME);
	}
}
