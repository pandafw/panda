package panda.log.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import panda.lang.ClassLoaders;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.LogAdapter;


public class ComboLogAdapter extends AbstractLogAdapter {
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
	}
	

	public Log getLogger(String name) {
		List<Log> logs = new ArrayList<Log>(adapters.size());
		for (LogAdapter a : adapters) {
			try {
				logs.add(a.getLogger(name));
			}
			catch (Throwable e) {
				LogHelper.error("Failed to getLogger(" + a.getClass() + ", " + name + ")");
			}
		}

		if (logs.isEmpty()) {
			return new ConsoleLog(name);
		}
		return new ComboLog(logs.toArray(new Log[logs.size()]));
	}

	/**
	 * Console log to System.out and System.err
	 */
	private static class ComboLog implements Log {
		private Log[] logs;
		
		ComboLog(Log[] logs) {
			this.logs = logs;
		}

		@Override
		public boolean isTraceEnabled() {
			return logs[0].isTraceEnabled();
		}

		@Override
		public boolean isInfoEnabled() {
			return logs[0].isInfoEnabled();
		}

		@Override
		public boolean isDebugEnabled() {
			return logs[0].isDebugEnabled();
		}

		@Override
		public boolean isWarnEnabled() {
			return logs[0].isWarnEnabled();
		}

		@Override
		public boolean isErrorEnabled() {
			return logs[0].isErrorEnabled();
		}

		@Override
		public boolean isFatalEnabled() {
			return logs[0].isFatalEnabled();
		}

		@Override
		public void trace(Object message) {
			for (Log g : logs) {
				g.trace(message);
			}
		}

		@Override
		public void trace(Object message, Throwable t) {
			for (Log g : logs) {
				g.trace(message, t);
			}
		}

		@Override
		public void tracef(String fmt, Object... args) {
			for (Log g : logs) {
				g.tracef(fmt, args);
			}
		}

		@Override
		public void debug(Object message) {
			for (Log g : logs) {
				g.debug(message);
			}
		}

		@Override
		public void debug(Object message, Throwable t) {
			for (Log g : logs) {
				g.debug(message, t);
			}
		}

		@Override
		public void debugf(String fmt, Object... args) {
			for (Log g : logs) {
				g.debugf(fmt, args);
			}
		}

		@Override
		public void info(Object message) {
			for (Log g : logs) {
				g.info(message);
			}
		}

		@Override
		public void info(Object message, Throwable t) {
			for (Log g : logs) {
				g.info(message, t);
			}
		}

		@Override
		public void infof(String fmt, Object... args) {
			for (Log g : logs) {
				g.infof(fmt, args);
			}
		}

		@Override
		public void warn(Object message) {
			for (Log g : logs) {
				g.warn(message);
			}
		}

		@Override
		public void warn(Object message, Throwable t) {
			for (Log g : logs) {
				g.warn(message, t);
			}
		}

		@Override
		public void warnf(String fmt, Object... args) {
			for (Log g : logs) {
				g.warnf(fmt, args);
			}
		}

		@Override
		public void error(Object message) {
			for (Log g : logs) {
				g.error(message);
			}
		}

		@Override
		public void error(Object message, Throwable t) {
			for (Log g : logs) {
				g.error(message, t);
			}
		}

		@Override
		public void errorf(String fmt, Object... args) {
			for (Log g : logs) {
				g.errorf(fmt, args);
			}
		}

		@Override
		public void fatal(Object message) {
			for (Log g : logs) {
				g.fatal(message);
			}
		}

		@Override
		public void fatal(Object message, Throwable t) {
			for (Log g : logs) {
				g.fatal(message, t);
			}
		}

		@Override
		public void fatalf(String fmt, Object... args) {
			for (Log g : logs) {
				g.fatalf(fmt, args);
			}
		}

	}
}
