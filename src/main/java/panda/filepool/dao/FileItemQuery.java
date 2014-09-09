package panda.filepool.dao;

import java.util.Date;

import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.StringCondition;
import panda.filepool.FileItem;

/**
 */
public class FileItemQuery extends EntityQuery<FileItem, FileItemQuery> {
	/**
	 * Constructor
	 */
	public FileItemQuery() {
		super(FileItem.class);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<FileItemQuery, Long> id() {
		return new ComparableCondition<FileItemQuery, Long>(this, FileItem.ID);
	}

	/**
	 * @return condition of name
	 */
	public StringCondition<FileItemQuery> name() {
		return new StringCondition<FileItemQuery>(this, FileItem.NAME);
	}

	/**
	 * @return condition of size
	 */
	public ComparableCondition<FileItemQuery, Long> size() {
		return new ComparableCondition<FileItemQuery, Long>(this, FileItem.SIZE);
	}

	/**
	 * @return condition of date
	 */
	public ComparableCondition<FileItemQuery, Date> date() {
		return new ComparableCondition<FileItemQuery, Date>(this, FileItem.DATE);
	}
}

