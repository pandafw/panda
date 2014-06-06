package panda.log;

import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public enum LogLevel {
	TRACE, 
	DEBUG,
	INFO,
	WARN,
	ERROR,
	FATAL;
	
	public static LogLevel parse(String level) {
		if (Strings.isEmpty(level)) {
			return TRACE;
		}
		
		switch (level.charAt(0)) {
		case 'd':
		case 'D':
			return DEBUG;
		case 'i':
		case 'I':
			return INFO;
		case 'w':
		case 'W':
			return WARN;
		case 'e':
		case 'E':
			return ERROR;
		case 'f':
		case 'F':
			return FATAL;
		case 't':
		case 'T':
		default:
			return TRACE;
		}
	}
}
