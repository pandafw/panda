package panda.wing.task.gae;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import javax.servlet.ServletContext;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.task.TaskScheduler;
import panda.task.Trigger;
import panda.wing.constant.SC;
import panda.wing.task.CronEntry;

@IocBean(type=TaskScheduler.class, create="initialize", depose="shutdown")
public class GaeTaskScheduler implements TaskScheduler {
	private static final Log log = Logs.getLog(GaeTaskScheduler.class);

	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Settings settings;

	@IocInject(value=MvcConstants.CRONS, required=false)
	protected List<CronEntry> crons;
	
	public void initialize() {
		if (settings.getPropertyAsBoolean(SC.SCHEDULER_ENABLE)) {
			log.info("Starting " + GaeTaskScheduler.class.getName() + " ...");
			
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
		
		for (CronEntry ce : crons) {
			if (Strings.isNotEmpty(ce.getCron())) {
				log.info("Add cron task (" + ce.getCron() + "): " + ce.getUrl());
				schedule(ce.getUrl(), ce.getCron());
				continue;
			}

			if (ce.getFixedDelay() > 0) {
				log.info("Add fixedDelay task (" + ce.getInitialDelay() + ", " + ce.getFixedDelay() + "): " + ce.getUrl());
				scheduleWithFixedDelay(ce.getUrl(), ce.getInitialDelay(), ce.getFixedDelay());
			}

			if (ce.getFixedRate() > 0) {
				log.info("Add fixedRate task (" + ce.getInitialDelay() + ", " + ce.getFixedRate() + "): " + ce.getUrl());
				scheduleAtFixedRate(ce.getUrl(), ce.getInitialDelay(), ce.getFixedRate());
			}
			
			if (ce.getDelay() > 0) {
				log.info("Add delay task (" + ce.getDelay() + "): " + ce.getUrl());
				schedule(ce.getUrl(), ce.getDelay());
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
