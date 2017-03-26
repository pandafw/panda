package panda.wing.task.gae;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskExecutor;
import panda.wing.AppConstants;

@IocBean(type=TaskExecutor.class, create="initialize")
public class GaeTaskExecutor implements TaskExecutor {
	private static final Log log = Logs.getLog(GaeTaskExecutor.class);

	@IocInject(value=AppConstants.EXECUTOR_ENABLE, required=false)
	protected boolean enable;

	public void initialize() {
		if (enable) {
			log.info("Starting " + GaeTaskExecutor.class.getName() + " ...");
		}
	}

	@Override
	public void execute(Runnable task) {
		// TODO Auto-generated method stub
	}
}
