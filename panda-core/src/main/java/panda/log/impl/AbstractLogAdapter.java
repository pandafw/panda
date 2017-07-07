package panda.log.impl;

import java.util.Map;
import java.util.Map.Entry;

import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogFormat;
import panda.log.LogFormat.SimpleLogFormat;
import panda.log.LogLevel;
import panda.log.Logs;


public abstract class AbstractLogAdapter implements LogAdapter {
	/** logs */
	protected Logs logs;
	
	/** adapter name */
	protected String name;

	protected LogLevel threshold;
	protected LogFormat format = LogFormat.DEFAULT;
	
	@Override
	public void init(Logs logs, String name, Map<String, String> props) {
		this.logs = logs;
		this.name = name;
		String prefix = "logger." + name + ".";
		setProperties(prefix, props);
	}
	
	@Override
	public Log getLog(String name) {
		if (logs.isLoggerEnabled(this.name, name)) {
			return getLogger(name);
		}
		return NopLog.INSTANCE;
	}

	protected abstract Log getLogger(String name);
	
	protected void setProperties(String prefix, Map<String, String> props) {
		for (Entry<String, String> en : props.entrySet()) {
			String key = en.getKey();
			if (key.startsWith(prefix)) {
				key = key.substring(prefix.length());
				setProperty(key, en.getValue());
			}
		}
	}
	
	protected void setProperty(String name, String value) {
		if ("threshold".equalsIgnoreCase(name)) {
			setThreshold(value);
		}
		else if ("format".equalsIgnoreCase(name)) {
			setFormat(value);
		}
	}
	
	protected void setThreshold(String threshold) {
		this.threshold = LogLevel.parse(threshold);
	}

	protected void setFormat(String pattern) {
		this.format = new SimpleLogFormat(pattern);
	}
}
