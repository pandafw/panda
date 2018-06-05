package panda.app.action.task;

import panda.app.action.work.GenericSyncWorkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.mvc.annotation.At;
import panda.vfs.FilePool;


/**
 * delete upload temporary files
 */
@At("/task/filecleanup")
@Auth({ AUTH.LOCAL, AUTH.TOKEN, AUTH.SUPER })
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
