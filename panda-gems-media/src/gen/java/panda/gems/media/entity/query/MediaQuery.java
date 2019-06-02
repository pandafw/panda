package panda.gems.media.entity.query;

import panda.app.entity.query.CUQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.StringCondition;
import panda.gems.media.entity.Media;

public class MediaQuery extends CUQuery<Media, MediaQuery> {
	/**
	 * Constructor
	 */
	public MediaQuery() {
		super(Entities.i().getEntity(Media.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public MediaQuery(DataQuery<Media> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<MediaQuery, Long> id() {
		return new ComparableCondition<MediaQuery, Long>(this, Media.ID);
	}

	/**
	 * @return condition of slug
	 */
	public StringCondition<MediaQuery> slug() {
		return new StringCondition<MediaQuery>(this, Media.SLUG);
	}

	/**
	 * @return condition of tag
	 */
	public StringCondition<MediaQuery> tag() {
		return new StringCondition<MediaQuery>(this, Media.TAG);
	}

	/**
	 * @return condition of name
	 */
	public StringCondition<MediaQuery> name() {
		return new StringCondition<MediaQuery>(this, Media.NAME);
	}

	/**
	 * @return condition of size
	 */
	public ComparableCondition<MediaQuery, Integer> size() {
		return new ComparableCondition<MediaQuery, Integer>(this, Media.SIZE);
	}

	/**
	 * @return condition of width
	 */
	public ComparableCondition<MediaQuery, Integer> width() {
		return new ComparableCondition<MediaQuery, Integer>(this, Media.WIDTH);
	}

	/**
	 * @return condition of height
	 */
	public ComparableCondition<MediaQuery, Integer> height() {
		return new ComparableCondition<MediaQuery, Integer>(this, Media.HEIGHT);
	}


}

