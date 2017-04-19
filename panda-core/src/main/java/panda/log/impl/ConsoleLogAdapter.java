package panda.log.impl;

import panda.log.Log;


public class ConsoleLogAdapter extends AbstractLogAdapter {
	public Log getLogger(String name) {
		return new ConsoleLog(name, threshold);
	}
}
