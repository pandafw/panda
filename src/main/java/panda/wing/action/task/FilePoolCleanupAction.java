package panda.wing.action.task;

import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.mvc.annotation.At;
import panda.vfs.FilePool;
import panda.wing.action.work.GenericSyncWorkAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;


/**
 * delete upload temporary files
 */
@At("/task/filecleanup")
@Auth({ AUTH.LOCAL, AUTH.SUPER })
public class FilePoolCleanupAction extends GenericSyncWorkAction {
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
