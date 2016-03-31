package panda.mvc.filepool;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.vfs.FilePool;
import panda.vfs.local.LocalFilePool;

@IocBean(type=FilePool.class)
public class MvcLocalFilePool extends LocalFilePool {
	public MvcLocalFilePool() {
	}

	/**
	 * @param path the path to set
	 */
	@IocInject(value=MvcConstants.FILEPOOL_LOCAL_PATH, required=false)
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param expires the expires to set
	 */
	@IocInject(value=MvcConstants.FILEPOOL_EXPIRES, required=false)
	public void setExpires(long expires) {
		this.expires = expires;
	}
	
	
}
