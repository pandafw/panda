package panda.log.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.LogAdapter;
import panda.log.LogLevel;


public abstract class AbstractLogAdapter implements LogAdapter {
	private LogLevel rootLvl = LogLevel.TRACE;

	private Map<String, LogLevel> levels;

	@Override
	public void init(Properties props) {
		rootLvl = LogLevel.TRACE;
		
		String self = getClass().getName() + ".";
		
		levels = new HashMap<String, LogLevel>();
		for (Entry<Object, Object> en : props.entrySet()) {
			String key = en.getKey().toString();
			if (key.startsWith("log.")) {
				key = key.substring(4);
				LogLevel lvl = LogLevel.parse(en.getValue().toString());
				if ("*".equals(key)) {
					rootLvl = lvl;
				}
				else {
					levels.put(key, lvl);
				}
			}
			else if (key.startsWith(self)) {
				key = key.substring(self.length());
				setProperty(key, en.getValue().toString());
			}
		}
	}
	
	protected void setProperty(String name, String value) {
	}
	
	protected LogLevel getLogLevel(String name) {
		if (Collections.isNotEmpty(levels) && Strings.isNotEmpty(name)) {
			String key = "";
			for (String k : levels.keySet()) {
				if (name.startsWith(k)) {
					if (k.length() > key.length()) {
						key = k;
					}
				}
			}

			LogLevel lvl = levels.get(key);
			if (lvl != null) {
				return lvl;
			}
		}
		return rootLvl;
	}
}
