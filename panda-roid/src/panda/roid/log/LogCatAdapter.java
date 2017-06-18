package panda.roid.log;

import org.apache.log4j.helpers.LogLog;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;
import panda.log.impl.AbstractLog;
import panda.log.impl.AbstractLogAdapter;
import panda.roid.Androids;


public class LogCatAdapter extends AbstractLogAdapter {
	public static String TAG_NAME = "PANDA";
	
	protected void write(LogEvent event) {
		String msg = format.format(event);
		if (event.getLevel() == LogLevel.FATAL) {
			android.util.Log.wtf(tag, msg, event.getError());
		}
		else if (event.getLevel() == LogLevel.ERROR) {
			android.util.Log.e(tag, msg, event.getError());
		}
		else if (event.getLevel() == LogLevel.WARN) {
			android.util.Log.w(tag, msg, event.getError());
		}
		else if (event.getLevel() == LogLevel.INFO) {
			android.util.Log.i(tag, msg, event.getError());
		}
		else if (event.getLevel() == LogLevel.DEBUG) {
			android.util.Log.d(tag, msg, event.getError());
		}
		else if (event.getLevel() == LogLevel.TRACE) {
			android.util.Log.v(tag, msg, event.getError());
		}
	}
	

	//------------------------------------------------
	private String tag = TAG_NAME;
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		if (Strings.isEmpty(tag)) {
			LogLog.error("Empty TAG");
			return;
		}
		this.tag = Strings.left(tag, Androids.LOGTAG_MAXLENGTH);
	}

	@Override
	protected Log getLogger(String name) {
		return new LogCatLog(this, name);
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("tag".equalsIgnoreCase(name)) {
			setTag(value);
		}
		else {
			super.setProperty(name, value);
		}
	}
	
	/**
	 * LogCat
	 */
	private static class LogCatLog extends AbstractLog {
		private LogCatAdapter adapter;
		
		protected LogCatLog(LogCatAdapter adapter, String name) {
			super(name, adapter.threshold);
			this.adapter = adapter;
		}

		@Override
		protected void write(LogEvent event) {
			adapter.write(event);
		}
	}
}
