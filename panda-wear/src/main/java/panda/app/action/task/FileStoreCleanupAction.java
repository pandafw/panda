package panda.app.action.task;

import java.util.Date;
import java.util.List;

import panda.app.action.work.GenericSyncWorkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.lang.time.DateTimes;
import panda.mvc.MvcConstants;
import panda.mvc.adaptor.ejector.MultiPartParamEjector;
import panda.mvc.annotation.At;
import panda.vfs.FileItem;
import panda.vfs.FileStore;


/**
 * delete upload temporary files
 */
@At("${!!task_path|||'/task'}/filestorecleanup")
@Auth({ AUTH.LOCAL, AUTH.TOKEN, AUTH.SUPER })
public class FileStoreCleanupAction extends GenericSyncWorkAction {
	/**
	 * the temporary directory of upload file.
	 */
	@IocInject(value=MvcConstants.FILE_UPLOAD_TMPDIR, required=false)
	private String tmpdir = MultiPartParamEjector.DEFAULT_TMPDIR;
	
	/**
	 * seconds since last modified. (default: 1 day) 
	 */
	@IocInject(value=MvcConstants.FILE_UPLOAD_MAXAGE, required=false)
	private int maxage = DateTimes.SEC_DAY;

	@IocInject
	protected FileStore fileStore;

	@Override
	protected void doExecute() {
		try {
			Date before = DateTimes.addSeconds(DateTimes.getDate(), - maxage);
			
			List<FileItem> fis = fileStore.listFiles(tmpdir + '/', before);
			status.total = fis.size();
			for (FileItem fi : fis) {
				fi.delete();
				status.count++;
				printInfo("Delete " + fi.getName());
			}
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
