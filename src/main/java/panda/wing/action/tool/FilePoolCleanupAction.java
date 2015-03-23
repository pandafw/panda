package panda.wing.action.tool;

import panda.filepool.FilePool;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.mvc.annotation.At;
import panda.wing.action.work.AbstractSyncWorkAction;
import panda.wing.auth.Auth;


/**
 * delete upload temporary files
 */
@At("/admin/task/filecleanup")
@Auth({"~local", "sysadmin"})
public class FilePoolCleanupAction extends AbstractSyncWorkAction {
	@IocInject
	protected FilePool filePool;
	
	@Override
	protected void doExecute() {
		try {
			filePool.clean();
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
