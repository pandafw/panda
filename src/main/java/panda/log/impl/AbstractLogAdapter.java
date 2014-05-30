package panda.log.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.LogAdapter;


public abstract class AbstractLogAdapter implements LogAdapter {
	private int rootLvl = Log.LEVEL_TRACE;

	private Map<String, Integer> levels;

	@Override
	public void init(Properties props) {
		rootLvl = Log.LEVEL_TRACE;
		
		levels = new HashMap<String, Integer>();
		for (Entry<Object, Object> en : props.entrySet()) {
			String key = en.getKey().toString();
			if (key.startsWith("log.")) {
				key = key.substring(4);
				int lvl = parseLevel(en.getValue().toString());
				if ("*".equals(key)) {
					rootLvl = lvl;
				}
				else {
					levels.put(key, lvl);
				}
			}
		}
	}
	
	private int parseLevel(String level) {
		if ("debug".equalsIgnoreCase(level)) {
			return Log.LEVEL_DEBUG;
		}
		if ("info".equalsIgnoreCase(level)) {
			return Log.LEVEL_INFO;
		}
		if ("warn".equalsIgnoreCase(level)) {
			return Log.LEVEL_WARN;
		}
		if ("error".equalsIgnoreCase(level)) {
			return Log.LEVEL_ERROR;
		}
		if ("fatal".equalsIgnoreCase(level)) {
			return Log.LEVEL_FATAL;
		}
		return Log.LEVEL_TRACE;
	}

	protected void LogLogInfo(String msg) {
		System.out.println(msg);
	}

	protected void LogLogError(String msg) {
		System.err.println(msg);
	}
	
	protected int getLogLevel(String name) {
		if (Collections.isNotEmpty(levels) && Strings.isNotEmpty(name)) {
			String key = "";
			for (String k : levels.keySet()) {
				if (name.startsWith(k)) {
					if (k.length() > key.length()) {
						key = k;
					}
				}
			}

			Integer lvl = levels.get(key);
			if (lvl != null) {
				return lvl;
			}
		}
		return rootLvl;
	}
}
