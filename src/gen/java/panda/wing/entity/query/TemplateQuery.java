package panda.wing.entity.query;

import panda.dao.query.ComparableCondition;
import panda.dao.query.GenericQuery;
import panda.dao.query.StringCondition;
import panda.wing.entity.Template;
import panda.wing.entity.query.SUQuery;

public class TemplateQuery extends SUQuery<Template, TemplateQuery> {
	/**
	 * Constructor
	 */
	public TemplateQuery() {
		super(Template.class);
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public TemplateQuery(GenericQuery<Template> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<TemplateQuery, Long> id() {
		return new ComparableCondition<TemplateQuery, Long>(this, Template.ID);
	}

	/**
	 * @return condition of name
	 */
	public StringCondition<TemplateQuery> name() {
		return new StringCondition<TemplateQuery>(this, Template.NAME);
	}

	/**
	 * @return condition of language
	 */
	public StringCondition<TemplateQuery> language() {
		return new StringCondition<TemplateQuery>(this, Template.LANGUAGE);
	}

	/**
	 * @return condition of country
	 */
	public StringCondition<TemplateQuery> country() {
		return new StringCondition<TemplateQuery>(this, Template.COUNTRY);
	}

	/**
	 * @return condition of source
	 */
	public StringCondition<TemplateQuery> source() {
		return new StringCondition<TemplateQuery>(this, Template.SOURCE);
	}

}

