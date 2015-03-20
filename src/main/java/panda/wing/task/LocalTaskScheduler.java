package panda.wing.task;

import java.util.List;

import javax.servlet.ServletContext;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.task.CronTrigger;
import panda.task.TaskScheduler;
import panda.task.ThreadPoolTaskScheduler;

@IocBean(type=TaskScheduler.class, create="initialize", depose="shutdown")
public class LocalTaskScheduler extends ThreadPoolTaskScheduler {
	private static final Log log = Logs.getLog(LocalTaskScheduler.class);

	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Settings settings;

	@IocInject(value=MvcConstants.CRONS, required=false)
	protected List<CronEntry> crons;
	
	public void initialize() {
		if (settings.getPropertyAsBoolean("scheduler.enable")) {
			log.info("Starting scheduler ...");
			
			setName(settings.getProperty("scheduler.name", "scheduler"));
			setPoolSize(settings.getPropertyAsInt("scheduler.poolSize", 1));
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
				CronTrigger ct = new CronTrigger(ce.getCron());

				log.info("Add cron task (" + ce.getCron() + "): " + at.getUrl());
				schedule(at, ct);
				continue;
			}

			if (ce.getFixedDelay() > 0) {
				log.info("Add fixedDelay task (" + ce.getInitialDelay() + ", " + ce.getFixedDelay() + "): " + at.getUrl());
				scheduleWithFixedDelay(at, ce.getInitialDelay(), ce.getFixedDelay());
			}

			if (ce.getFixedRate() > 0) {
				log.info("Add fixedRate task (" + ce.getInitialDelay() + ", " + ce.getFixedDelay() + "): " + at.getUrl());
				scheduleAtFixedRate(at, ce.getInitialDelay(), ce.getFixedDelay());
			}
			
			if (ce.getDelay() > 0) {
				log.info("Add delay task (" + ce.getDelay() + "): " + at.getUrl());
				schedule(at, ce.getDelay());
			}
		}
	}

	private ActionTask newActionTask(String url) {
		if (url.startsWith("/")) {
			String scheme = settings.getProperty("task.scheme", "http");
			String server = settings.getProperty("task.server", "localhost");
			int port = settings.getPropertyAsInt("task.port", 80);
			url = scheme + "://" + server + ':' + port + servlet.getContextPath() + url;
		}
		return new ActionTask(url);
	}

}
