package panda.gems.bundle.template.entity.query;

import panda.app.entity.query.SUQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.StringCondition;
import panda.gems.bundle.template.entity.Template;

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
	public TemplateQuery(DataQuery<Template> query) {
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
	 * @return condition of locale
	 */
	public StringCondition<TemplateQuery> locale() {
		return new StringCondition<TemplateQuery>(this, Template.LOCALE);
	}

	/**
	 * @return condition of source
	 */
	public StringCondition<TemplateQuery> source() {
		return new StringCondition<TemplateQuery>(this, Template.SOURCE);
	}


}

