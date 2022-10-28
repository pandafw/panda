package panda.app.action.task;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import panda.app.action.work.SyncWorkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.SET;
import panda.io.FileIterator;
import panda.io.Files;
import panda.io.IOCase;
import panda.io.Settings;
import panda.io.filter.RegexFileFilter;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;


/**
 * delete temporary files
 */
@At("${!!task_path|||'/task'}/tmpfilescleanup")
@Auth({ AUTH.LOCAL, AUTH.TOKEN, AUTH.SUPER })
public class TmpFilesCleanupAction extends SyncWorkAction {
	@IocInject
	private Settings settings;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doExecute() {
		Map<String, String> ts = (Map<String, String>)settings.getPropertyAsMap(SET.TMP_FILES_CLEANUP);
		if (Collections.isEmpty(ts)) {
			return;
		}

		// temp file age, default: 1day
		long maxage = settings.getPropertyAsInt(SET.TMP_FILES_MAXAGE, DateTimes.SEC_DAY) * 1000L;

		for (Entry<String, String> en : ts.entrySet()) {
			String path = en.getKey();
			String regx = en.getValue();

			if (Strings.isEmpty(path) || Strings.isEmpty(regx)) {
				continue;
			}
			
			File file = new File(path);
			if (file.getParent() == null) {
				printWarning("Illegal to clean root directory!");
				continue;
			}

			long now = System.currentTimeMillis();
			try (FileIterator fi = Files.iterateFiles(file, false, new RegexFileFilter(regx, IOCase.INSENSITIVE))) {
				while (fi.hasNext()) {
					File f = fi.next();
					long l = f.length();
					long t = f.lastModified();

					Boolean d = null;
					if (maxage <= 0 || t + maxage < now) {
						d = f.delete();
					}
					
					String m = (d == null ? "Skipped" : (d ? "Succeeded" : "Failed")) 
							+ " to delete " 
							+ f.getPath() 
							+ " (" + Numbers.humanSize(l) + ") [" 
							+ DateTimes.isoDatetimeFormat().format(t) + "]";

					if (d == null || d) {
						printInfo(m);
						status.count++;
					}
					else {
						printWarning(m);
					}
				}
			}
			catch (Exception e) {
				printError(e.getMessage());
			}
		}
	}
}
