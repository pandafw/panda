package panda.filepool.dao;

import java.util.Date;

import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.StringCondition;

/**
 */
public class FileItemQuery extends EntityQuery<DaoFileItem, FileItemQuery> {
	/**
	 * Constructor
	 */
	public FileItemQuery() {
		super(DaoFileItem.class);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<FileItemQuery, Long> id() {
		return new ComparableCondition<FileItemQuery, Long>(this, DaoFileItem.ID);
	}

	/**
	 * @return condition of name
	 */
	public StringCondition<FileItemQuery> name() {
		return new StringCondition<FileItemQuery>(this, DaoFileItem.NAME);
	}

	/**
	 * @return condition of size
	 */
	public ComparableCondition<FileItemQuery, Long> size() {
		return new ComparableCondition<FileItemQuery, Long>(this, DaoFileItem.SIZE);
	}

	/**
	 * @return condition of date
	 */
	public ComparableCondition<FileItemQuery, Date> date() {
		return new ComparableCondition<FileItemQuery, Date>(this, DaoFileItem.DATE);
	}
}

