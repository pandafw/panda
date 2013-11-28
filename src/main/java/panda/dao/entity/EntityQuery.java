package panda.dao.entity;

import panda.dao.criteria.Order;
import panda.dao.criteria.Query;
import panda.dao.criteria.QueryWrapper;


@SuppressWarnings("unchecked")
public class EntityQuery<T extends EntityQuery> extends QueryWrapper {
	/**
	 * Constructor
	 */
	public EntityQuery() {
		super();
	}

	/**
	 * @param query the query to set
	 */
	public EntityQuery(Query query) {
		super(query);
	}

	//----------------------------------------------------------------------
	public T reset() {
		query.reset();
		return (T)this;
	}
	
	//----------------------------------------------------------------------
	// conjunction
	//----------------------------------------------------------------------
	/**
	 * append begin paren
	 * @return this
	 */
	public T begin() {
		getQuery().begin();
		return (T)this;
	}

	/**
	 * append end paren
	 * @return this
	 */
	public T end() {
		getQuery().end();
		return (T)this;
	}

	/**
	 * append AND
	 * @return this
	 */
	public T and() {
		getQuery().and();
		return (T)this;
	}

	/**
	 * append OR
	 * @return this
	 */
	public T or() {
		getQuery().or();
		return (T)this;
	}

	/**
	 * set start
	 * @return this
	 */
	public T start(int start) {
		getQuery().start(start);
		return (T)this;
	}

	/**
	 * set limit
	 * @return this
	 */
	public T limit(int limit) {
		getQuery().limit(limit);
		return (T)this;
	}

	/**
	 * add ascend order
	 * @param column column
	 * @return this
	 */
	public T orderBy(String column) {
		getQuery().orderBy(column);
		return (T)this;
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public T orderBy(String name, Order order) {
		getQuery().orderBy(name, order);
		return (T)this;
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public T orderBy(String name, String order) {
		getQuery().orderBy(name, order);
		return (T)this;
	}

	/**
	 * add order
	 * @param name name
	 * @param ascend direction
	 * @return this
	 */
	public T orderBy(String name, boolean ascend) {
		getQuery().orderBy(name, ascend);
		return (T)this;
	}

	/**
	 * add ascend order
	 * @param name		name
	 * @return this
	 */
	public T orderByAsc(String name) {
		getQuery().orderByAsc(name);
		return (T)this;
	}

	/**
	 * add descend order
	 * @param name		name
	 * @return this
	 */
	public T orderByDesc(String name) {
		getQuery().orderByDesc(name);
		return (T)this;
	}
}

