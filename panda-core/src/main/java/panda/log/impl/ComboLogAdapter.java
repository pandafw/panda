package panda.log.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import panda.lang.Classes;
import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogEvent;
import panda.log.LogLevel;
import panda.log.LogLog;


public class ComboLogAdapter extends AbstractLogAdapter {
	private Map<String, LogAdapter> adapters = new LinkedHashMap<String, LogAdapter>();

	@Override
	public void init(String name, Properties props) {
		super.init(name, props);
		
		Map<String, LogAdapter> as = new LinkedHashMap<String, LogAdapter>(adapters.size());
		for (Entry<String, LogAdapter> en : adapters.entrySet()) {
			String an = en.getKey();
			LogAdapter adapter = en.getValue();
			try {
				adapter.init(an, props);
				adapter.getLogger("panda");
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
		if (name.startsWith("adapter.")) {
			name = name.substring("adapter.".length());
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

	public Log getLogger(String name) {
		List<Log> logs = new ArrayList<Log>(adapters.size());
		for (LogAdapter a : adapters.values()) {
			try {
				logs.add(a.getLogger(name));
			}
			catch (Throwable e) {
				LogLog.error("Failed to getLogger(" + a.getClass() + ", " + name + ")");
			}
		}

		if (logs.isEmpty()) {
			return new ConsoleLog(name, threshold);
		}
		return new ComboLog(name, threshold, logs.toArray(new Log[logs.size()]));
	}

	private static class ComboLog extends AbstractLog {
		private Log[] logs;
		
		ComboLog(String name, LogLevel level,  Log[] logs) {
			super(name, level);
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
