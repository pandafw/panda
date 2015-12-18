package panda.wing.action.work;

import java.io.PrintWriter;
import java.util.Date;

import panda.el.ElTemplate;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.servlet.HttpServletSupport;
import panda.wing.action.AbstractAction;


public abstract class GenericWorkAction extends AbstractAction {
	private static final Log log = Logs.getLog(GenericWorkAction.class);

	protected static final char PREFIX = '%';
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
			return time == null ? null : DateTimes.TIMESTAMP_FORMAT.format(time);
		}
		
		public String getMsg() {
			return StringEscapes.escapeJavaScript(status);
		}
	}
	
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
			hss.setExpiry(0);
			hss.setCharset(Charsets.UTF_8);
			hss.setContentType("text/html");
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

	protected void logStatus(String s) {
		log.debug(s);
	}
	
	protected boolean skipPrint(String s) {
		return (this.silent || Strings.isEmpty(s));
	}

	protected void printStatus(char level, String msg) {
		updateStatus(level, msg);

		if (skipPrint(msg)) {
			return;
		}

		String s = formatStatus(level, msg);
		logStatus(s);
		
		if (events.onStatus == null) {
			printLine(s);
		}
		else {
			printScript(events.onStatus.evaluate(status));
		}
	}
	
	protected void printStart(String msg) {
		updateStatus(L_INFO, msg);

		if (skipPrint(msg)) {
			return;
		}

		if (events.onStart == null) {
			printLine(">>> " + msg);
		}
		else {
			printScript(events.onStart.evaluate(status));
		}
	}
	
	protected void printFinish(String msg) {
		updateStatus(L_INFO, msg);

		if (skipPrint(msg)) {
			return;
		}

		if (events.onFinish == null) {
			printLine("<<< " + msg);
		}
		else {
			printScript(events.onFinish.evaluate(status));
		}
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
	
	protected void printLine(String msg) {
		if (skipPrint(msg)) {
			return;
		}

		flushPrint(msg + Streams.LINE_SEPARATOR);
	}

	protected void printScript(String script) {
		if (skipPrint(script)) {
			return;
		}

		flushPrint("<script>" + script + "</script>"); 
	}
	
	protected void flushPrint(String msg) {
		if (skipPrint(msg)) {
			return;
		}

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
