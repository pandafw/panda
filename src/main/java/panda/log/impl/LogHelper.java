package panda.log.impl;

import panda.log.Log;
import panda.log.LogAdapter;

public class LogHelper {
	private static Log log = new ConsoleLog("LOG");
	
	public static Log getLogger(LogAdapter adapter, String name) {
		Log log;
		try {
			log = adapter.getLogger(name);
		}
		catch (Throwable e) {
			log =  new ConsoleLog(name);
			log.error("Failed to getLogger(" + adapter.getClass() + ", " + name + ")");
		}
		return log;
	}
	
	public static void error(String msg) {
		log.equals(msg);
	}
}
