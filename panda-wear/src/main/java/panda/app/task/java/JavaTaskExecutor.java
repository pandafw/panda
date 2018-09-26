package panda.app.task.java;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.app.util.AppSettings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskExecutor;
import panda.task.ThreadPoolTaskExecutor;

@IocBean(type=TaskExecutor.class, create="initialize", depose="shutdown")
public class JavaTaskExecutor extends ThreadPoolTaskExecutor {
	private static final Log log = Logs.getLog(JavaTaskExecutor.class);

	@IocInject
	protected AppSettings settings;

	@IocInject(value=MVC.EXECUTOR_ENABLE, required=false)
	protected boolean enable;

	@IocInject(value=MVC.EXECUTOR_NAME, required=false)
	public void setName(String name) {
		super.setName(name);
	}

	@IocInject(value=MVC.EXECUTOR_CORE_POOL_SIZE, required=false)
	public void setCorePoolSize(int corePoolSize) {
		super.setCorePoolSize(corePoolSize);
	}
	
	@IocInject(value=MVC.EXECUTOR_MAX_POOL_SIZE, required=false)
	public void setMaxPoolSize(int maxPoolSize) {
		super.setMaxPoolSize(maxPoolSize);
	}
	
	public JavaTaskExecutor() {
		setName("executor");
	}
	
	public void initialize() {
		enable = settings.getPropertyAsBoolean(SET.EXECUTOR_ENABLE, enable);

		if (enable) {
			log.info("Starting " + getClass().getName() + " ...");
			
			super.initialize();
		}
	}
}
