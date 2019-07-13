package panda.gems.pages.entity.query;

import java.util.Date;
import panda.app.entity.query.SCUQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.StringCondition;
import panda.gems.pages.entity.Page;

public class PageQuery extends SCUQuery<Page, PageQuery> {
	/**
	 * Constructor
	 */
	public PageQuery() {
		super(Entities.i().getEntity(Page.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public PageQuery(DataQuery<Page> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<PageQuery, Long> id() {
		return new ComparableCondition<PageQuery, Long>(this, Page.ID);
	}

	/**
	 * @return condition of slug
	 */
	public StringCondition<PageQuery> slug() {
		return new StringCondition<PageQuery>(this, Page.SLUG);
	}

	/**
	 * @return condition of title
	 */
	public StringCondition<PageQuery> title() {
		return new StringCondition<PageQuery>(this, Page.TITLE);
	}

	/**
	 * @return condition of tag
	 */
	public StringCondition<PageQuery> tag() {
		return new StringCondition<PageQuery>(this, Page.TAG);
	}

	/**
	 * @return condition of publishDate
	 */
	public ComparableCondition<PageQuery, Date> publishDate() {
		return new ComparableCondition<PageQuery, Date>(this, Page.PUBLISH_DATE);
	}

	/**
	 * @return condition of thumbnail
	 */
	public StringCondition<PageQuery> thumbnail() {
		return new StringCondition<PageQuery>(this, Page.THUMBNAIL);
	}

	/**
	 * @return condition of content
	 */
	public StringCondition<PageQuery> content() {
		return new StringCondition<PageQuery>(this, Page.CONTENT);
	}


}

