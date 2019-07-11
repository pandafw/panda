package panda.app.action.task;

import java.io.File;

import panda.app.action.work.SyncWorkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.io.Settings;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.annotation.At;


/**
 * configure log on log.properties modified
 */
@At("${!!task_path|||'/task'}/logconfigure")
@Auth({ AUTH.LOCAL, AUTH.TOKEN, AUTH.SUPER })
public class LogConfigureAction extends SyncWorkAction {
	private static final Log log = Logs.getLog(LogConfigureAction.class);
	
	@Override
	protected void doExecute() {
		String prop = Systems.getProperty(Logs.CONFIG, Logs.CONFIG);
		File file = new File(prop);
		
		if (file.exists() && file.lastModified() > Logs.getConfigureTime()) {
			log.info("Configure Logs: " + file.getAbsolutePath());
			try {
				Settings props = new Settings(prop);
				Logs.configure(props);
			}
			catch (Exception e) {
				log.error("Failed to configure logs " + file, e);
			}
		}
	}
}
