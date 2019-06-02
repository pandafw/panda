package panda.app.entity.query;

import panda.app.entity.Resource;
import panda.app.entity.query.SUQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.StringCondition;

public class ResourceQuery extends SUQuery<Resource, ResourceQuery> {
	/**
	 * Constructor
	 */
	public ResourceQuery() {
		super(Entities.i().getEntity(Resource.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public ResourceQuery(DataQuery<Resource> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<ResourceQuery, Long> id() {
		return new ComparableCondition<ResourceQuery, Long>(this, Resource.ID);
	}

	/**
	 * @return condition of clazz
	 */
	public StringCondition<ResourceQuery> clazz() {
		return new StringCondition<ResourceQuery>(this, Resource.CLAZZ);
	}

	/**
	 * @return condition of locale
	 */
	public StringCondition<ResourceQuery> locale() {
		return new StringCondition<ResourceQuery>(this, Resource.LOCALE);
	}

	/**
	 * @return condition of source
	 */
	public StringCondition<ResourceQuery> source() {
		return new StringCondition<ResourceQuery>(this, Resource.SOURCE);
	}


}

