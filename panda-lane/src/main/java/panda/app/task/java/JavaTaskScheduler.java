package panda.app.task.java;

import java.util.List;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.app.task.ActionTaskSubmitter;
import panda.app.task.CronActionTask;
import panda.app.util.AppSettings;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.task.CronTrigger;
import panda.task.TaskScheduler;
import panda.task.ThreadPoolTaskScheduler;

@IocBean(type=TaskScheduler.class, create="initialize", depose="shutdown")
public class JavaTaskScheduler extends ThreadPoolTaskScheduler {
	private static final Log log = Logs.getLog(JavaTaskScheduler.class);

	@IocInject(required=false)
	protected Ioc ioc;
	
	@IocInject
	protected AppSettings settings;
	
	@IocInject(value=MVC.SCHEDULER_ENABLE, required=false)
	protected boolean enable;

	@IocInject(value=MVC.SCHEDULER_CRONS, required=false)
	protected List<CronActionTask> crons;

	@IocInject(value=MVC.SCHEDULER_NAME, required=false)
	public void setName(String name) {
		super.setName(name);
	}

	@IocInject(value=MVC.SCHEDULER_POOL_SIZE, required=false)
	public void setPoolSize(int poolSize) {
		super.setPoolSize(poolSize);
	}
	
	public JavaTaskScheduler() {
		setName("scheduler");
	}
	
	public void initialize() {
		enable = settings.getPropertyAsBoolean(SET.SCHEDULER_ENABLE, enable);

		if (enable) {
			log.info("Starting " + getClass().getName() + " ...");
			
			super.initialize();
			
			addCronTask();
		}
	}
	
	private void addCronTask() {
		if (Collections.isEmpty(crons)) {
			return;
		}
		
		for (CronActionTask cat : crons) {
			ActionTaskSubmitter ats = newActionTaskSubmitter(cat);

			if (Strings.isNotEmpty(cat.getCron())) {
				log.info("Add cron task (" + cat.getCron() + "): " + cat.getAction());
				CronTrigger ct = new CronTrigger(cat.getCron());
				schedule(ats, ct);
				continue;
			}

			if (cat.getFixedDelay() > 0) {
				log.info("Add fixedDelay task (" + cat.getInitialDelay() + ", " + cat.getFixedDelay() + "): " + cat.getAction());
				scheduleWithFixedDelay(ats, cat.getInitialDelay(), cat.getFixedDelay());
				continue;
			}

			if (cat.getFixedRate() > 0) {
				log.info("Add fixedRate task (" + cat.getInitialDelay() + ", " + cat.getFixedRate() + "): " + cat.getAction());
				scheduleAtFixedRate(ats, cat.getInitialDelay(), cat.getFixedRate());
				continue;
			}
			
			if (cat.getDelay() > 0) {
				log.info("Add delay task (" + cat.getDelay() + "): " + cat.getAction());
				schedule(ats, cat.getDelay());
				continue;
			}
		}
	}

	private ActionTaskSubmitter newActionTaskSubmitter(CronActionTask cat) {
		ActionTaskSubmitter ats = Mvcs.born(ioc, ActionTaskSubmitter.class);
		ats.setTask(cat);
		return ats;
	}

}
