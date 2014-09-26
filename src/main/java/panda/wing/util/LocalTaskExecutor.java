package panda.wing.util;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskExecutor;
import panda.task.ThreadPoolTaskExecutor;

@IocBean(type=TaskExecutor.class, create="initialize", depose="shutdown")
public class LocalTaskExecutor extends ThreadPoolTaskExecutor {
	private static final Log log = Logs.getLog(LocalTaskExecutor.class);

	@IocInject
	protected Settings settings;

	public void initialize() {
		if (settings.getPropertyAsBoolean("executor.enable")) {
			log.info("Starting executor ...");
			
			setName(settings.getProperty("executor.name", "executor"));
			setCorePoolSize(settings.getPropertyAsInt("executor.corePoolSize", 1));
			setMaxPoolSize(settings.getPropertyAsInt("executor.maxPoolSize", Integer.MAX_VALUE));
			super.initialize();
		}
	}
}
