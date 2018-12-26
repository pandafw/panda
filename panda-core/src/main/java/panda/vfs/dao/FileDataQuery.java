package panda.vfs.dao;

import panda.dao.query.ComparableCondition;
import panda.dao.query.EntityQuery;
import panda.dao.query.ObjectCondition;

/**
 */
public class FileDataQuery extends EntityQuery<DaoFileData, FileDataQuery> {
	/**
	 * Constructor
	 */
	public FileDataQuery() {
		super(DaoFileData.class);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of fid
	 */
	public ComparableCondition<FileDataQuery, String> fid() {
		return new ComparableCondition<FileDataQuery, String>(this, DaoFileData.FID);
	}

	/**
	 * @return condition of bno
	 */
	public ComparableCondition<FileDataQuery, Integer> bno() {
		return new ComparableCondition<FileDataQuery, Integer>(this, DaoFileData.BNO);
	}

	/**
	 * @return condition of size
	 */
	public ComparableCondition<FileDataQuery, Integer> size() {
		return new ComparableCondition<FileDataQuery, Integer>(this, DaoFileData.SIZE);
	}

	/**
	 * @return condition of data
	 */
	public ObjectCondition<FileDataQuery> data() {
		return new ObjectCondition<FileDataQuery>(this, DaoFileData.DATA);
	}
}

