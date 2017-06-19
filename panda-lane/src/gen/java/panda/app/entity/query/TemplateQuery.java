package panda.app.entity.query;

import panda.app.entity.Template;
import panda.app.entity.query.SUQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.GenericQuery;
import panda.dao.query.StringCondition;

public class TemplateQuery extends SUQuery<Template, TemplateQuery> {
	/**
	 * Constructor
	 */
	public TemplateQuery() {
		super(Entities.i().getEntity(Template.class));
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

