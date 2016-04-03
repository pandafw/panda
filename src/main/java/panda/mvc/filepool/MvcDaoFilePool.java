package panda.mvc.filepool;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.vfs.FilePool;
import panda.vfs.dao.DaoFilePool;

@IocBean(type=FilePool.class)
public class MvcDaoFilePool extends DaoFilePool {
	/**
	 * @param blockSize the blockSize to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILEPOOL_DAO_BLOCK_SIZE, required=false)
	public void setBlockSize(int blockSize) {
		super.setBlockSize(blockSize);
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILEPOOL_MAXAGE, required=false)
	public void setMaxAge(int maxAge) {
		super.setMaxAge(maxAge);
	}
}
