package panda.app.entity.query;

import panda.app.entity.Property;
import panda.app.entity.query.SUQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.StringCondition;

public class PropertyQuery extends SUQuery<Property, PropertyQuery> {
	/**
	 * Constructor
	 */
	public PropertyQuery() {
		super(Entities.i().getEntity(Property.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public PropertyQuery(DataQuery<Property> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<PropertyQuery, Long> id() {
		return new ComparableCondition<PropertyQuery, Long>(this, Property.ID);
	}

	/**
	 * @return condition of clazz
	 */
	public StringCondition<PropertyQuery> clazz() {
		return new StringCondition<PropertyQuery>(this, Property.CLAZZ);
	}

	/**
	 * @return condition of locale
	 */
	public StringCondition<PropertyQuery> locale() {
		return new StringCondition<PropertyQuery>(this, Property.LOCALE);
	}

	/**
	 * @return condition of name
	 */
	public StringCondition<PropertyQuery> name() {
		return new StringCondition<PropertyQuery>(this, Property.NAME);
	}

	/**
	 * @return condition of value
	 */
	public StringCondition<PropertyQuery> value() {
		return new StringCondition<PropertyQuery>(this, Property.VALUE);
	}

	/**
	 * @return condition of memo
	 */
	public StringCondition<PropertyQuery> memo() {
		return new StringCondition<PropertyQuery>(this, Property.MEMO);
	}


}

