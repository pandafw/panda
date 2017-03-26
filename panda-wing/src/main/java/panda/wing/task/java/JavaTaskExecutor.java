package panda.wing.task.java;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskExecutor;
import panda.task.ThreadPoolTaskExecutor;
import panda.wing.AppConstants;

@IocBean(type=TaskExecutor.class, create="initialize", depose="shutdown")
public class JavaTaskExecutor extends ThreadPoolTaskExecutor {
	private static final Log log = Logs.getLog(JavaTaskExecutor.class);

	@IocInject(value=AppConstants.EXECUTOR_ENABLE, required=false)
	protected boolean enable;

	@IocInject(value=AppConstants.EXECUTOR_NAME, required=false)
	public void setName(String name) {
		super.setName(name);
	}

	@IocInject(value=AppConstants.EXECUTOR_CORE_POOL_SIZE, required=false)
	public void setCorePoolSize(int corePoolSize) {
		super.setCorePoolSize(corePoolSize);
	}
	
	@IocInject(value=AppConstants.EXECUTOR_MAX_POOL_SIZE, required=false)
	public void setMaxPoolSize(int maxPoolSize) {
		super.setMaxPoolSize(maxPoolSize);
	}
	
	public JavaTaskExecutor() {
		setName("executor");
	}
	
	public void initialize() {
		if (enable) {
			log.info("Starting " + getClass().getName() + " ...");
			
			super.initialize();
		}
	}
}
