package panda.wing.entity.query;

import panda.dao.query.ComparableCondition;
import panda.dao.query.GenericQuery;
import panda.dao.query.StringCondition;
import panda.wing.entity.Property;
import panda.wing.entity.query.SCUQuery;

public class PropertyQuery extends SCUQuery<Property, PropertyQuery> {
	/**
	 * Constructor
	 */
	public PropertyQuery() {
		super(Property.class);
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public PropertyQuery(GenericQuery<Property> query) {
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
	 * @return condition of language
	 */
	public StringCondition<PropertyQuery> language() {
		return new StringCondition<PropertyQuery>(this, Property.LANGUAGE);
	}

	/**
	 * @return condition of country
	 */
	public StringCondition<PropertyQuery> country() {
		return new StringCondition<PropertyQuery>(this, Property.COUNTRY);
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

