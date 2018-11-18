package panda.app.entity.query;

import panda.app.entity.Media;
import panda.app.entity.query.UQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.ObjectCondition;
import panda.dao.query.StringCondition;

public class MediaQuery extends UQuery<Media, MediaQuery> {
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
	 * @return condition of kind
	 */
	public StringCondition<MediaQuery> kind() {
		return new StringCondition<MediaQuery>(this, Media.KIND);
	}

	/**
	 * @return condition of mediaName
	 */
	public StringCondition<MediaQuery> mediaName() {
		return new StringCondition<MediaQuery>(this, Media.MEDIA_NAME);
	}

	/**
	 * @return condition of mediaData
	 */
	public ObjectCondition<MediaQuery> mediaData() {
		return new ObjectCondition<MediaQuery>(this, Media.MEDIA_DATA);
	}

	/**
	 * @return condition of mediaSize
	 */
	public ComparableCondition<MediaQuery, Integer> mediaSize() {
		return new ComparableCondition<MediaQuery, Integer>(this, Media.MEDIA_SIZE);
	}

	/**
	 * @return condition of mediaWidth
	 */
	public ComparableCondition<MediaQuery, Integer> mediaWidth() {
		return new ComparableCondition<MediaQuery, Integer>(this, Media.MEDIA_WIDTH);
	}

	/**
	 * @return condition of mediaHeight
	 */
	public ComparableCondition<MediaQuery, Integer> mediaHeight() {
		return new ComparableCondition<MediaQuery, Integer>(this, Media.MEDIA_HEIGHT);
	}


}

