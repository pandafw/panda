package panda.aop.asm.test;

import panda.log.Log;
import panda.log.Logs;

public class ZZZ {
	private static final Log log = Logs.getLog(ZZZ.class);
	
	public ZZZ(String name) {
	}

	public void p(@AX AX x) {
		log.debug("==========================================================");
	}
}
