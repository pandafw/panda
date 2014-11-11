package panda.wing;

import panda.bean.Beans;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.Setup;
import panda.task.TaskExecutor;
import panda.task.TaskScheduler;


@IocBean(type=Setup.class)
public class AppSetup implements Setup {
	@IocInject(required=false)
	protected Beans beans = Beans.i();
	
	@IocInject(required=false)
	protected TaskScheduler scheduler;
	
	@IocInject(required=false)
	protected TaskExecutor executor;
	
	/**
	 * initialize
	 */
	public void initialize() {
		
	}

	/**
	 * destroy
	 */
	public void destroy() {
		beans.clear();
	}
}
