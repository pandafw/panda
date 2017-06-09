package panda.log.impl;

import panda.log.Log;


public class ConsoleLogAdapter extends AbstractLogAdapter {
	@Override
	protected Log getLogger(String name) {
		return new ConsoleLog(name, threshold, format);
	}
}
