package panda.wing.task.gae;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskExecutor;
import panda.wing.constant.SC;

@IocBean(type=TaskExecutor.class, create="initialize")
public class GaeTaskExecutor implements TaskExecutor {
	private static final Log log = Logs.getLog(GaeTaskExecutor.class);

	@IocInject
	protected Settings settings;

	public void initialize() {
		if (settings.getPropertyAsBoolean(SC.EXECUTOR_ENABLE)) {
			log.info("Starting " + GaeTaskExecutor.class.getName() + " ...");
		}
	}

	@Override
	public void execute(Runnable task) {
		// TODO Auto-generated method stub
	}
}
