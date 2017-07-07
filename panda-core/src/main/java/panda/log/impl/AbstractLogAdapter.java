package panda.log.impl;

import java.util.Map.Entry;
import java.util.Properties;

import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogFormat;
import panda.log.LogFormat.SimpleLogFormat;
import panda.log.LogLevel;
import panda.log.Logs;


public abstract class AbstractLogAdapter implements LogAdapter {
	/** adapter name */
	protected String name;

	protected LogLevel threshold;
	protected LogFormat format = LogFormat.DEFAULT;
	
	@Override
	public void init(String name, Properties props) {
		this.name = name;
		String prefix = "logger." + name + ".";
		setProperties(prefix, props);
	}
	
	@Override
	public Log getLog(String name) {
		if (Logs.isLoggerEnabled(this.name, name)) {
			return getLogger(name);
		}
		return NopLog.INSTANCE;
	}

	protected abstract Log getLogger(String name);
	
	protected void setProperties(String prefix, Properties props) {
		for (Entry<Object, Object> en : props.entrySet()) {
			String key = en.getKey().toString();
			if (key.startsWith(prefix)) {
				key = key.substring(prefix.length());
				setProperty(key, en.getValue().toString());
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
