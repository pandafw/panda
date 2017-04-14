package panda.roid.log.log4j;

import android.util.Log;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import panda.log.log4j.AbstractAppender;

public class LogCatAppender extends AbstractAppender {

	private String tag = "PANDA";
	
	public LogCatAppender() {
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Override
	protected void subAppend(final LoggingEvent le) {
		String msg = formatLogEvent(le);
		Throwable ex = (layout.ignoresThrowable() && le.getThrowableInformation() != null) ? le.getThrowableInformation().getThrowable() : null;
		
		switch (le.getLevel().toInt()) {
		case Level.TRACE_INT:
			if (ex != null) {
				Log.v(tag, msg, ex);
			}
			else {
				Log.v(tag, msg);
			}
			break;
		case Level.DEBUG_INT:
			if (ex != null) {
				Log.d(tag, msg, ex);
			}
			else {
				Log.d(tag, msg);
			}
			break;
		case Level.INFO_INT:
			if (ex != null) {
				Log.i(tag, msg, ex);
			}
			else {
				Log.i(tag, msg);
			}
			break;
		case Level.WARN_INT:
			if (ex != null) {
				Log.w(tag, msg, ex);
			}
			else {
				Log.w(tag, msg);
			}
			break;
		case Level.ERROR_INT:
			if (ex != null) {
				Log.e(tag, msg, ex);
			}
			else {
				Log.e(tag, msg);
			}
			break;
		case Level.FATAL_INT:
			if (ex != null) {
				Log.wtf(tag, msg, ex);
			}
			else {
				Log.wtf(tag, msg);
			}
			break;
		}
	}
}
