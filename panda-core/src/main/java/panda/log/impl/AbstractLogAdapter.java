package panda.log.impl;

import java.util.Map.Entry;
import java.util.Properties;

import panda.log.LogAdapter;
import panda.log.LogLevel;


public abstract class AbstractLogAdapter implements LogAdapter {
	protected LogLevel threshold;
	
	@Override
	public void init(Properties props) {
		String self = getClass().getName() + ".";
		
		for (Entry<Object, Object> en : props.entrySet()) {
			String key = en.getKey().toString();
			if (key.startsWith(self)) {
				key = key.substring(self.length());
				setProperty(key, en.getValue().toString());
			}
		}
	}
	
	protected void setThreshold(String threshold) {
		this.threshold = LogLevel.parse(threshold);
	}

	protected void setProperty(String name, String value) {
		if ("threshold".equalsIgnoreCase(name)) {
			setThreshold(value);
		}
	}
}
