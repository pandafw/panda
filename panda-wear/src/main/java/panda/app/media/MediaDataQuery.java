package panda.app.media;

import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.EntityQuery;
import panda.dao.query.ObjectCondition;

public class MediaDataQuery extends EntityQuery<MediaData, MediaDataQuery> {
	/**
	 * Constructor
	 */
	public MediaDataQuery() {
		super(Entities.i().getEntity(MediaData.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public MediaDataQuery(DataQuery<MediaData> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of mid
	 */
	public ComparableCondition<MediaDataQuery, Long> mid() {
		return new ComparableCondition<MediaDataQuery, Long>(this, MediaData.MID);
	}

	/**
	 * @return condition of msz
	 */
	public ComparableCondition<MediaDataQuery, Integer> msz() {
		return new ComparableCondition<MediaDataQuery, Integer>(this, MediaData.MSZ);
	}

	/**
	 * @return condition of size
	 */
	public ComparableCondition<MediaDataQuery, Integer> size() {
		return new ComparableCondition<MediaDataQuery, Integer>(this, MediaData.SIZE);
	}

	/**
	 * @return condition of data
	 */
	public ObjectCondition<MediaDataQuery> data() {
		return new ObjectCondition<MediaDataQuery>(this, MediaData.DATA);
	}


}

