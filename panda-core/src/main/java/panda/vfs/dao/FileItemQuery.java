package panda.vfs.dao;

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
	 * @return condition of name
	 */
	public StringCondition<FileItemQuery> name() {
		return new StringCondition<FileItemQuery>(this, DaoFileItem.NAME);
	}

	/**
	 * @return condition of size
	 */
	public ComparableCondition<FileItemQuery, Integer> size() {
		return new ComparableCondition<FileItemQuery, Integer>(this, DaoFileItem.SIZE);
	}

	/**
	 * @return condition of date
	 */
	public ComparableCondition<FileItemQuery, Date> date() {
		return new ComparableCondition<FileItemQuery, Date>(this, DaoFileItem.DATE);
	}
}

