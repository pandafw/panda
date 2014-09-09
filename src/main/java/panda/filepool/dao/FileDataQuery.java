package panda.filepool.dao;

import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.ObjectCondition;

/**
 */
public class FileDataQuery extends EntityQuery<FileData, FileDataQuery> {
	/**
	 * Constructor
	 */
	public FileDataQuery() {
		super(FileData.class);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of fid
	 */
	public ComparableCondition<FileDataQuery, Long> fid() {
		return new ComparableCondition<FileDataQuery, Long>(this, FileData.FID);
	}

	/**
	 * @return condition of bno
	 */
	public ComparableCondition<FileDataQuery, Integer> bno() {
		return new ComparableCondition<FileDataQuery, Integer>(this, FileData.BNO);
	}

	/**
	 * @return condition of size
	 */
	public ComparableCondition<FileDataQuery, Integer> size() {
		return new ComparableCondition<FileDataQuery, Integer>(this, FileData.SIZE);
	}

	/**
	 * @return condition of data
	 */
	public ObjectCondition<FileDataQuery> data() {
		return new ObjectCondition<FileDataQuery>(this, FileData.DATA);
	}
}

