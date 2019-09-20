package panda.gae.app.cache.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import javax.servlet.ServletContext;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.app.task.CronActionTask;
import panda.app.util.AppSettings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.task.TaskScheduler;
import panda.task.Trigger;

@IocBean(type=TaskScheduler.class, create="initialize")
public class GaeTaskScheduler implements TaskScheduler {
	private static final Log log = Logs.getLog(GaeTaskScheduler.class);

	@IocInject(required=false)
	protected ServletContext servlet;

	@IocInject
	protected AppSettings settings;
	
	@IocInject(value=MVC.SCHEDULER_ENABLE, required=false)
	protected boolean enable;

	@IocInject(value=MVC.SCHEDULER_CRONS, required=false)
	protected List<CronActionTask> crons;
	
	public void initialize() {
		enable = settings.getPropertyAsBoolean(SET.SCHEDULER_ENABLE, enable);

		if (enable) {
			log.info("Starting " + getClass().getName() + " ...");
			
			addCronTask();
		}
	}
	
	private void schedule(String url, String cron) {
		
	}
	private void schedule(String url, long delay) {
		
	}
	private void scheduleWithFixedDelay(String url, long initial, long delay) {
		
	}
	private void scheduleAtFixedRate(String url, long initial, long rate) {
		
	}

	private void addCronTask() {
		if (servlet == null || Collections.isEmpty(crons)) {
			return;
		}
		
		for (CronActionTask cat : crons) {
			if (Strings.isNotEmpty(cat.getCron())) {
				log.info("Add cron task (" + cat.getCron() + "): " + cat.getAction());
				schedule(cat.getAction(), cat.getCron());
				continue;
			}

			if (cat.getFixedDelay() > 0) {
				log.info("Add fixedDelay task (" + cat.getInitialDelay() + ", " + cat.getFixedDelay() + "): " + cat.getAction());
				scheduleWithFixedDelay(cat.getAction(), cat.getInitialDelay(), cat.getFixedDelay());
			}

			if (cat.getFixedRate() > 0) {
				log.info("Add fixedRate task (" + cat.getInitialDelay() + ", " + cat.getFixedRate() + "): " + cat.getAction());
				scheduleAtFixedRate(cat.getAction(), cat.getInitialDelay(), cat.getFixedRate());
			}
			
			if (cat.getDelay() > 0) {
				log.info("Add delay task (" + cat.getDelay() + "): " + cat.getAction());
				schedule(cat.getAction(), cat.getDelay());
			}
		}
	}

	@Override
	public void execute(Runnable task, long startTimeout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Future<?> submit(Runnable task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(Runnable task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, long delay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
		// TODO Auto-generated method stub
		return null;
	}
}
