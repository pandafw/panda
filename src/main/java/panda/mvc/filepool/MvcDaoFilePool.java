package panda.mvc.filepool;

import panda.dao.DaoClient;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.vfs.FilePool;
import panda.vfs.dao.DaoFilePool;

@IocBean(type=FilePool.class)
public class MvcDaoFilePool extends DaoFilePool {
	/**
	 * @param daoClient the daoClient to set
	 */
	@IocInject
	public void setDaoClient(DaoClient daoClient) {
		this.daoClient = daoClient;
	}

	/**
	 * @param blockSize the blockSize to set
	 */
	@IocInject(value=MvcConstants.FILEPOOL_DAO_BLOCK_SIZE, required=false)
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * @param expires the expires to set
	 */
	@IocInject(value=MvcConstants.FILEPOOL_EXPIRES, required=false)
	public void setExpires(long expires) {
		this.expires = expires;
	}
}
