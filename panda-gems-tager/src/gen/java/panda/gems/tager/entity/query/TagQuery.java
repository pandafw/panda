package panda.gems.tager.entity.query;

import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.EntityQuery;
import panda.dao.query.StringCondition;
import panda.gems.tager.entity.Tag;

public class TagQuery extends EntityQuery<Tag, TagQuery> {
	/**
	 * Constructor
	 */
	public TagQuery() {
		super(Entities.i().getEntity(Tag.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public TagQuery(DataQuery<Tag> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<TagQuery, Long> id() {
		return new ComparableCondition<TagQuery, Long>(this, Tag.ID);
	}

	/**
	 * @return condition of name
	 */
	public StringCondition<TagQuery> name() {
		return new StringCondition<TagQuery>(this, Tag.NAME);
	}

	/**
	 * @return condition of kind
	 */
	public StringCondition<TagQuery> kind() {
		return new StringCondition<TagQuery>(this, Tag.KIND);
	}

	/**
	 * @return condition of code
	 */
	public StringCondition<TagQuery> code() {
		return new StringCondition<TagQuery>(this, Tag.CODE);
	}


}

