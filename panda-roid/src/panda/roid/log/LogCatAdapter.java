package panda.roid.log;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.LogLevel;
import panda.log.impl.AbstractLog;
import panda.log.impl.AbstractLogAdapter;


public class LogCatAdapter extends AbstractLogAdapter {
	private static final int TAG_SIZE = 23;

	public static String getLogTag(String name) {
		if (name == null) {
			return "null";
		}
		
		int len = name.length();
		if (len <= TAG_SIZE) {
			return name;
		}

		if (Strings.contains(name, '.')) {
			String[] ss = Strings.split(name, '.');
			for (int i = 0; i < ss.length; i++) {
				String s = ss[i];
				if (s.length() > 1) {
					if (i == ss.length - 1) {
						if (s.length() > len - TAG_SIZE) {
							ss[i] = s.substring(0, s.length() - len + TAG_SIZE);
							len = TAG_SIZE;
						}
						else {
							ss[i] = s.charAt(0) + "*";
							len -= s.length() - 2;
						}
					}
					else {
						ss[i] = s.charAt(0) + "*";
						len -= s.length() - 2;
					}
				}
				
				if (len <= TAG_SIZE) {
					break;
				}
			}
			
			name = Strings.join(ss, '.');
			if (name.length() > TAG_SIZE) {
				name = '*' + name.substring(name.length() - TAG_SIZE + 1);
			}
		}
		else {
			name = name.substring(0, TAG_SIZE - 1) + '*';
		}

		return name;
	}

	public static void log(String tag, String cat, LogLevel level, String msg, Throwable t) {
		msg = cat + " - " + msg;
		if (level == LogLevel.FATAL) {
			android.util.Log.wtf(tag, msg, t);
		}
		else if (level == LogLevel.ERROR) {
			android.util.Log.e(tag, msg, t);
		}
		else if (level == LogLevel.WARN) {
			android.util.Log.w(tag, msg, t);
		}
		else if (level == LogLevel.INFO) {
			android.util.Log.i(tag, msg, t);
		}
		else if (level == LogLevel.DEBUG) {
			android.util.Log.d(tag, msg, t);
		}
		else if (level == LogLevel.TRACE) {
			android.util.Log.v(tag, msg, t);
		}
	}
	

	//------------------------------------------------
	private String tag = "PANDA";
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Log getLogger(String name) {
		return new LogCatLog(name);
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("tag".equalsIgnoreCase(name)) {
			setTag(value);
		}
	}
	
	/**
	 * LogCat
	 */
	private class LogCatLog extends AbstractLog {
		private String cat;
		
		LogCatLog(String name) {
			super(name);
			cat = name;
		}

		@Override
		public void log(LogLevel level, String msg, Throwable t) {
			LogCatAdapter.log(tag, cat, level, msg, t);
		}
	}
}
