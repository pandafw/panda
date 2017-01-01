package panda.log.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import panda.lang.ClassLoaders;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogLevel;
import panda.log.LogLog;


public class ComboLogAdapter extends BaseLogAdapter {
	private List<LogAdapter> adapters = new ArrayList<LogAdapter>();

	@Override
	public void init(Properties props) {
		super.init(props);
		
		List<LogAdapter> as = new ArrayList<LogAdapter>(adapters.size());
		for (LogAdapter adapter : adapters) {
			try {
				adapter.init(props);
				adapter.getLogger("panda");
				as.add(adapter);
			}
			catch (Throwable e) {
				System.err.println("Failed to initialize " + adapter.getClass() + ": " + e.getMessage());
			}
		}
		adapters = as;
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("adapters".equalsIgnoreCase(name)) {
			String[] as = Strings.split(value);

			ClassLoader cl = ClassLoaders.getClassLoader();
			for (String a : as) {
				try {
					LogAdapter adapter = (LogAdapter)Class.forName(a, true, cl).newInstance();
					adapters.add(adapter);
				}
				catch (Throwable e) {
					System.err.println("Failed to create " + a + ": " + e.getMessage());
				}
			}
		}
		else {
			super.setProperty(name, value);
		}
	}
	

	public Log getLogger(String name) {
		List<Log> logs = new ArrayList<Log>(adapters.size());
		for (LogAdapter a : adapters) {
			try {
				logs.add(a.getLogger(name));
			}
			catch (Throwable e) {
				LogLog.error("Failed to getLogger(" + a.getClass() + ", " + name + ")");
			}
		}

		if (logs.isEmpty()) {
			return new DefaultLog(name);
		}
		return new ComboLog(this, name, logs.toArray(new Log[logs.size()]));
	}

	/**
	 * Console log to System.out and System.err
	 */
	private static class ComboLog extends BaseLog {
		private Log[] logs;
		
		ComboLog(ComboLogAdapter adapter, String name, Log[] logs) {
			super(adapter, name);
			this.logs = logs;
		}

		@Override
		public void log(LogLevel level, String msg, Throwable tx) {
			for (Log g : logs) {
				g.log(level, msg, tx);
			}
		}
	}
}