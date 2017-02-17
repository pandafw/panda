package panda.wing.action.work;

import java.io.PrintWriter;
import java.util.Date;

import panda.el.ElTemplate;
import panda.io.MimeType;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.StringEscapes;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.servlet.HttpServletSupport;
import panda.wing.action.AbstractAction;


public abstract class GenericWorkAction extends AbstractAction {
	protected static final char PREFIX = '%';
	protected static final char L_DEBUG = 'd';
	protected static final char L_INFO = 'i';
	protected static final char L_WARN = 'w';
	protected static final char L_ERROR = 'e';
	protected static final char L_SUCCESS = 's';
	
	public static class Events {
		protected ElTemplate onStart;
		protected ElTemplate onStatus;
		protected ElTemplate onFinish;

		/**
		 * @param onStart the onStart to set
		 */
		public void setOnStart(String onStart) {
			this.onStart = new ElTemplate(onStart, PREFIX);
		}

		/**
		 * @param onStatus the onStatus to set
		 */
		public void setOnStatus(String onStatus) {
			this.onStatus = new ElTemplate(onStatus, PREFIX);;
		}

		/**
		 * @param onFinish the onFinish to set
		 */
		public void setOnFinish(String onFinish) {
			this.onFinish = new ElTemplate(onFinish, PREFIX);;
		}
	}

	public static class Status {
		public long count;
		public long total;
	
		/** date */
		public Date time;
		/** last level */
		public char level;
		/** last message */
		public String status;

		/**
		 * @return the time
		 */
		public String getDate() {
			return time == null ? null : DateTimes.timestampFormat().format(time);
		}
		
		public String getMsg() {
			return StringEscapes.escapeJavaScript(status);
		}
	}
	
	protected Log log = Logs.getLog(getClass());
	protected Events events = new Events();
	protected Status status = new Status();
	
	protected boolean silent;

	/**
	 * 
	 */
	public GenericWorkAction() {
		events = new Events();
		status = newStatus();
	}

	/**
	 * @return the silent
	 */
	public boolean isSilent() {
		return silent;
	}

	/**
	 * @param silent the silent to set
	 */
	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	protected Status newStatus() {
		return new Status();
	}

	public Status getStatus() {
		return status;
	}
	
	protected void init(Events es) {
		if (es != null) {
			events = es;
		}
		if (!silent) {
			HttpServletSupport hss = new HttpServletSupport(getRequest(), getResponse());
			hss.setMaxAge(0);
			hss.setCharset(Charsets.UTF_8);
			hss.setContentType(MimeType.TEXT_HTML);
			try {
				hss.writeResponseHeader();
			}
			catch (Exception e) {
				throw Exceptions.wrapThrow(e);
			}
		}
	}

	protected void updateStatus(char level, String msg) {
		status.time = DateTimes.getDate();
		status.level = level; 
		status.status = msg;
	}
	
	protected void referStatus(GenericWorkAction awa) {
		if (awa == this || awa == null) {
			return;
		}
		
		this.status = awa.status;
	}
	
	protected String formatStatus(char level, String msg) {
		if (status.count == 0) {
			return ("> [" + level + "] - " + msg);
		}
		if (status.total == 0) {
			return ("> " + status.count + " [" + level + "] - " + msg);
		}

		return ("> " + status.count + '/' + status.total + " [" + level + "] - " + msg);
	}

	protected void printStart(String msg) {
		updateStatus(L_INFO, msg);

		String s = ">>> " + msg;
		logStatus(L_INFO, s);
		
		if (silent) {
			return;
		}
		
		if (events.onStart == null) {
			printLine(s);
		}
		else {
			printScript(events.onStart.evaluate(status));
		}
	}
	
	protected void printFinish(String msg) {
		updateStatus(L_INFO, msg);

		String s = "<<< " + msg;
		logStatus(L_INFO, s);
		
		if (silent) {
			return;
		}

		if (events.onFinish == null) {
			printLine(s);
		}
		else {
			printScript(events.onFinish.evaluate(status));
		}
	}
	
	protected void printDebug(String msg) {
		printStatus(L_DEBUG, msg);
	}
	
	protected void printInfo(String msg) {
		printStatus(L_INFO, msg);
	}
	
	protected void printWarning(String msg) {
		printStatus(L_WARN, msg);
	}
	
	protected void printError(String msg) {
		printStatus(L_ERROR, msg);
	}
	
	protected void printSuccess(String msg) {
		printStatus(L_SUCCESS, msg);
	}
	
	private void logStatus(char level, String s) {
		switch (level) {
		case L_INFO:
		case L_SUCCESS:
			log.info(s);
			break;
		case L_WARN:
			log.warn(s);
			break;
		case L_ERROR:
			log.error(s);
			break;
		case L_DEBUG:
			log.debug(s);
			break;
		default:
			log.trace(s);
			break;
		}
	}
	
	private void printLine(String msg) {
		flushPrint(msg + Streams.LINE_SEPARATOR);
	}

	protected void printStatus(char level, String msg) {
		updateStatus(level, msg);

		String s = formatStatus(level, msg);
		logStatus(level, s);

		if (silent) {
			return;
		}
		
		if (events.onStatus == null) {
			printLine(s);
		}
		else {
			printScript(events.onStatus.evaluate(status));
		}
	}
	
	private void printScript(String script) {
		flushPrint("<script>" + script + "</script>"); 
	}
	
	private void flushPrint(String msg) {
		try {
			PrintWriter pw = getResponse().getWriter();
			pw.write(msg);
			pw.flush();
		}
		catch (Exception e) {
			log.warn("Failed to write: " + msg, e);
		}
	}
	
	//-------------------------------------------------
	public abstract Object stop();
	
	public abstract Object status();
	

}
