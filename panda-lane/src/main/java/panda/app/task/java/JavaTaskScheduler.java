package panda.app.task.java;

import java.util.List;

import javax.servlet.ServletContext;

import panda.app.AppConstants;
import panda.app.task.ActionTask;
import panda.app.task.CronEntry;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.task.CronTrigger;
import panda.task.TaskScheduler;
import panda.task.ThreadPoolTaskScheduler;

@IocBean(type=TaskScheduler.class, create="initialize", depose="shutdown")
public class JavaTaskScheduler extends ThreadPoolTaskScheduler {
	private static final Log log = Logs.getLog(JavaTaskScheduler.class);

	private static final int DEFAULT_ERROR_LIMIT = 3;
	
	@IocInject(required=false)
	protected ServletContext servlet;

	@IocInject(value=AppConstants.SCHEDULER_CRONS, required=false)
	protected List<CronEntry> crons;

	@IocInject(value=AppConstants.TASK_ACTION_SCHEME, required=false)
	protected String scheme = "http://localhost:8080";
	
	@IocInject(value=AppConstants.SCHEDULER_ENABLE, required=false)
	protected boolean enable;

	@IocInject(value=AppConstants.SCHEDULER_NAME, required=false)
	public void setName(String name) {
		super.setName(name);
	}

	@IocInject(value=AppConstants.SCHEDULER_POOL_SIZE, required=false)
	public void setPoolSize(int poolSize) {
		super.setPoolSize(poolSize);
	}
	
	public JavaTaskScheduler() {
		setName("scheduler");
	}
	
	public void initialize() {
		if (enable) {
			log.info("Starting " + getClass().getName() + " ...");
			
			super.initialize();
			
			addCronTask();
		}
	}
	
	private void addCronTask() {
		if (servlet == null || Collections.isEmpty(crons)) {
			return;
		}
		
		for (CronEntry ce : crons) {
			ActionTask at = newActionTask(ce.getUrl());

			if (Strings.isNotEmpty(ce.getCron())) {
				log.info("Add cron task (" + ce.getCron() + "): " + at.getUrl());
				at.setErrorLimit(ce.getErrorLimit() > 0 ? ce.getErrorLimit() : DEFAULT_ERROR_LIMIT);
				CronTrigger ct = new CronTrigger(ce.getCron());
				schedule(at, ct);
				continue;
			}

			if (ce.getFixedDelay() > 0) {
				log.info("Add fixedDelay task (" + ce.getInitialDelay() + ", " + ce.getFixedDelay() + "): " + at.getUrl());
				at.setErrorLimit(ce.getErrorLimit() > 0 ? ce.getErrorLimit() : DEFAULT_ERROR_LIMIT);
				scheduleWithFixedDelay(at, ce.getInitialDelay(), ce.getFixedDelay());
				continue;
			}

			if (ce.getFixedRate() > 0) {
				log.info("Add fixedRate task (" + ce.getInitialDelay() + ", " + ce.getFixedRate() + "): " + at.getUrl());
				at.setErrorLimit(ce.getErrorLimit() > 0 ? ce.getErrorLimit() : DEFAULT_ERROR_LIMIT);
				scheduleAtFixedRate(at, ce.getInitialDelay(), ce.getFixedRate());
				continue;
			}
			
			if (ce.getDelay() > 0) {
				log.info("Add delay task (" + ce.getDelay() + "): " + at.getUrl());
				schedule(at, ce.getDelay());
				continue;
			}
		}
	}

	private ActionTask newActionTask(String url) {
		if (url.startsWith("/")) {
			url = scheme + servlet.getContextPath() + url;
		}
		return new ActionTask(url);
	}

}
