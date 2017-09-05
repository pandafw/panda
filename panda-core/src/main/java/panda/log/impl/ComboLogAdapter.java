package panda.log.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.lang.Classes;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogEvent;
import panda.log.LogLevel;
import panda.log.LogLog;
import panda.log.Logs;


public class ComboLogAdapter extends AbstractLogAdapter {
	private Map<String, LogAdapter> adapters = new LinkedHashMap<String, LogAdapter>();

	@Override
	public void init(Logs logs, String name, Map<String, String> props) {
		super.init(logs, name, props);
		
		if (Strings.isEmpty(name)) {
			setProperties("panda.", props);
		}

		// initialize adapters
		Map<String, LogAdapter> as = new LinkedHashMap<String, LogAdapter>(adapters.size());
		for (Entry<String, LogAdapter> en : adapters.entrySet()) {
			String an = en.getKey();
			LogAdapter adapter = en.getValue();
			try {
				adapter.init(logs, an, props);
				adapter.getLog("panda");
				as.put(an, adapter);
			}
			catch (Throwable e) {
				LogLog.error("Failed to initialize " + adapter.getClass() + ": " + e.getMessage());
			}
		}
		adapters = as;
	}

	@Override
	protected void setProperty(String name, String value) {
		if (name.startsWith("logger.")) {
			name = name.substring("logger.".length());
			addAdapter(name, value);
		}
		else {
			super.setProperty(name, value);
		}
	}
	
	private void addAdapter(String name, String impl) {
		try {
			LogAdapter adapter = (LogAdapter)Classes.getClass(impl).newInstance();
			adapters.put(name, adapter);
		}
		catch (Throwable e) {
			LogLog.error("Failed to create " + name + ":" + impl, e);
		}
	}

	@Override
	protected Log getLogger(String name) {
		if (adapters.isEmpty()) {
			return new ConsoleLog(this.logs, name, threshold);
		}
		
		if (adapters.size() == 1) {
			for (LogAdapter a : adapters.values()) {
				try {
					return a.getLog(name);
				}
				catch (Throwable e) {
					LogLog.error("Failed to getLogger(" + a.getClass() + ", " + name + ")");
				}
			}
			return new ConsoleLog(this.logs, name, threshold);
		}
		
		boolean error = false;
		List<Log> logs = new ArrayList<Log>(adapters.size());
		for (LogAdapter a : adapters.values()) {
			try {
				Log l = a.getLog(name);
				if (!(l instanceof NopLog)) {
					logs.add(l);
				}
			}
			catch (Throwable e) {
				error = true;
				LogLog.error("Failed to getLogger(" + a.getClass() + ", " + name + ")");
			}
		}

		if (logs.isEmpty()) {
			if (error) {
				// return a console log if some error occurs
				return new ConsoleLog(this.logs, name, threshold);
			}
			return NopLog.INSTANCE;
		}

		if (logs.size() == 1) {
			return logs.get(0);
		}

		return new ComboLog(this.logs, name, threshold, logs.toArray(new Log[logs.size()]));
	}

	private static class ComboLog extends AbstractLog {
		private Log[] logs;
		
		ComboLog(Logs p, String name, LogLevel level, Log[] logs) {
			super(p, name, level);
			this.logs = logs;
		}

		@Override
		protected void write(LogEvent event) {
			for (Log g : logs) {
				g.log(event);
			}
		}
	}
}
