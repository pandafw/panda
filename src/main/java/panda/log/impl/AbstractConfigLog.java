package panda.log.impl;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import panda.lang.ClassLoaders;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;


public abstract class AbstractConfigLog extends AbstractLog {
	public static final String CONFIG = "log.properties";

	private static int rootLvl = -1;
	private static Map<String, Integer> levels;
	
	protected AbstractConfigLog(String name) {
		level = getLogLevel(name);
	}
	
	protected int parseLevel(String level) {
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

	protected void LogInfo(String msg) {
		System.out.println(msg);
	}

	protected void LogError(String msg) {
		System.err.println(msg);
	}
	
	protected void init() {
		if (rootLvl >= Log.LEVEL_TRACE) {
			return;
		}
		
		synchronized (getClass()) {
			if (rootLvl >= Log.LEVEL_TRACE) {
				return;
			}

			rootLvl = Log.LEVEL_TRACE;
			
			InputStream is = ClassLoaders.getResourceAsStream(CONFIG);
			if (is == null) {
				LogInfo(CONFIG + " not found, set default log level to TRACE");
				return;
			}
			
			Properties props = new Properties();
			try {
				props.load(is);
				
				levels = new HashMap<String, Integer>();
				for (Entry<Object, Object> en : props.entrySet()) {
					int lvl = parseLevel(en.getValue().toString());
					if ("*".equals(en.getKey())) {
						rootLvl = lvl;
					}
					else {
						levels.put(en.getKey().toString(), lvl);
					}
				}
			}
			catch (IOException e) {
				LogError(e.getMessage());
			}
		}
	}
	
	protected int getLogLevel(String name) {
		init();
		
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

	@Override
	protected void log(int level, Object msg, Throwable tx) {
		switch (level) {
		case LEVEL_FATAL:
			fatal(msg, tx);
			break;
		case LEVEL_ERROR:
			error(msg, tx);
			break;
		case LEVEL_WARN:
			warn(msg, tx);
			break;
		case LEVEL_INFO:
			info(msg, tx);
			break;
		case LEVEL_DEBUG:
			debug(msg, tx);
			break;
		case LEVEL_TRACE:
			trace(msg, tx);
			break;
		}
	}
}
