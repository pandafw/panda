package panda.app.task.gae;

import panda.app.AppConstants;
import panda.app.constant.SET;
import panda.app.util.AppSettings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskExecutor;

@IocBean(type=TaskExecutor.class, create="initialize")
public class GaeTaskExecutor implements TaskExecutor {
	private static final Log log = Logs.getLog(GaeTaskExecutor.class);

	@IocInject
	protected AppSettings settings;

	@IocInject(value=AppConstants.EXECUTOR_ENABLE, required=false)
	protected boolean enable;

	public void initialize() {
		enable = settings.getPropertyAsBoolean(SET.EXECUTOR_ENABLE, enable);

		if (enable) {
			log.info("Starting " + GaeTaskExecutor.class.getName() + " ...");
		}
	}

	@Override
	public void execute(Runnable task) {
		// TODO Auto-generated method stub
	}
}
