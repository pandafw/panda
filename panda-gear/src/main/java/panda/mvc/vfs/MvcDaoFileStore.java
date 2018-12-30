package panda.mvc.vfs;

import panda.dao.DaoClient;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.vfs.FileStore;
import panda.vfs.dao.DaoFileStore;

@IocBean(type=FileStore.class)
public class MvcDaoFileStore extends DaoFileStore {
	
	@IocInject
	@Override
	public void setDaoClient(DaoClient daoClient) {
		super.setDaoClient(daoClient);
	}

	/**
	 * @param blockSize the blockSize to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILESTORE_DAO_BLOCK_SIZE, required=false)
	public void setBlockSize(int blockSize) {
		super.setBlockSize(blockSize);
	}
}
