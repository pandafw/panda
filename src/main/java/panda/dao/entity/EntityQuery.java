package panda.dao.entity;

import panda.dao.criteria.QueryWrapper;
import panda.dao.criteria.Query;


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
}

