package panda.wing.action.task;

import panda.filepool.FilePool;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.mvc.annotation.At;
import panda.wing.action.work.AbstractSyncWorkAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;


/**
 * delete upload temporary files
 */
@At("/task/filecleanup")
@Auth({ AUTH.LOCAL, AUTH.SYSADMIN })
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
