package panda.wing.task.java;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskExecutor;
import panda.task.ThreadPoolTaskExecutor;
import panda.wing.constant.SC;

@IocBean(type=TaskExecutor.class, create="initialize", depose="shutdown")
public class JavaTaskExecutor extends ThreadPoolTaskExecutor {
	private static final Log log = Logs.getLog(JavaTaskExecutor.class);

	@IocInject
	protected Settings settings;

	public void initialize() {
		if (settings.getPropertyAsBoolean(SC.EXECUTOR_ENABLE)) {
			log.info("Starting " + JavaTaskExecutor.class.getName() + " ...");
			
			setName(settings.getProperty(SC.EXECUTOR_NAME, "executor"));
			setCorePoolSize(settings.getPropertyAsInt(SC.EXECUTOR_CORE_POOL_SIZE, 1));
			setMaxPoolSize(settings.getPropertyAsInt(SC.EXECUTOR_MAX_POOL_SIZE, Integer.MAX_VALUE));
			super.initialize();
		}
	}
}
