package panda.wing.action.work;

import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Objects;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;


public abstract class AbstractSyncWorkAction extends AbstractWorkAction {
	private static final Log log = Logs.getLog(AbstractSyncWorkAction.class);
	
	protected static final Object lock = new Object();
	
	public static class SyncStatus extends Status {
		public boolean stop = true;
		public boolean running = false;

		/**
		 * @return the progress
		 */
		public String getProgress() {
			return String.valueOf(count);
		}

	}

	protected Status newStatus() {
		return new SyncStatus();
	}

	protected SyncStatus getStatus() {
		return (SyncStatus)status;
	}
	
	protected boolean isStop() {
		return getStatus().stop;
	}
	
	/**
	 * @return the action key
	 */
	protected String getActionKey() {
		return getClass().getName();
	}
	
	/**
	 * get singleton instance
	 * @return instance
	 */
	protected AbstractSyncWorkAction getSelf() {
		synchronized (lock) {
			return (AbstractSyncWorkAction)getApp().get(getActionKey());
		}
	}

	/**
	 * set singleton instance
	 * @param instance instance
	 */
	protected void setSelf(AbstractSyncWorkAction instance) {
		synchronized (lock) {
			if (instance == null) {
				getApp().remove(getActionKey());
			}
			else {
				getApp().put(getActionKey(), instance);
			}
		}
	}
	
	@At("")
	@Ok("ftl:/panda/wing/action/work/SyncWork.ftl")
	public void input() {
	}

	@At
	@Ok(View.JSON)
	@Override
	public Object stop() {
		AbstractSyncWorkAction aswa = getSelf();
		if (aswa != null) {
			referStatus(aswa);
			aswa.doStop();
		}
		return status;
	}
	
	protected void doStop() {
		getStatus().stop = true;
		
		// wait for stop (max: 60s)
		int count = 0;
		while (getStatus().running && count < 600) {
			Objects.safeSleep(100);
			count++;
		}
	}
	
	@At
	@Ok(View.JSON)
	@Override
	public Object status() {
		AbstractSyncWorkAction aswa = getSelf();
		if (aswa != null) {
			referStatus(aswa);
		}
		return status;
	}

	@At
	@Ok(View.VOID)
	public Object start(@Param("e.*") Events es) {
		init(es);

		AbstractSyncWorkAction aswa = getSelf();
		if (aswa == null) {
			setSelf(this);
			StopWatch sw = new StopWatch();
			try {
				getStatus().count = 0;
				getStatus().stop = false;
				getStatus().running = true;
				printStart(textStart());
				doExecute();
			}
			catch (Throwable e) {
				log.error("execute", e);
				printError(getStackTrace(e));
			}
			finally {
				getStatus().running = false;
				setSelf(null);
				printFinish(textSucceed(sw));
			}
		}
		else {
			doRunning();
		}
		return status;
	}

	protected String getStackTrace(Throwable e) {
		return Exceptions.getStackTrace(e);
	}
	
	protected void doRunning() {
		printInfo(getClass().getSimpleName() + " is running ...");
	}

	protected abstract void doExecute() throws Exception;
	
	//-------------------------------------------------------
	protected String textStart() {
		return getText("text-start", "");
	}
	
	protected String textSucceed(StopWatch sw) {
		String msg = status.count + " items processed, took " + sw + ".";
		msg = getText("text-finish", msg, Arrays.toMap(
			new String[][] { { "count", String.valueOf(status.count) }, { "msg", sw.toString() }}));
		return msg;
	}
	
	protected String textNotRunning() {
		return getText("text-not-running", "Not Running");
	}
	
}
