package panda.mvc.filepool;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.vfs.FilePool;
import panda.vfs.local.LocalFilePool;

@IocBean(type=FilePool.class)
public class MvcLocalFilePool extends LocalFilePool {
	/**
	 * @param path the path to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILEPOOL_LOCAL_PATH, required=false)
	public void setPath(String path) {
		super.setPath(path);
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
