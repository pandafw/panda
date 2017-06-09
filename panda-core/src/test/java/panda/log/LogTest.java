package panda.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogTest {

	public static void main(String[] args) {
		Log log = Logs.getLog(LogTest.class);
		
		log.debug("d");
		log.info("i");
		log.warn("w");
		log.error("e", new Exception("ex"));
		
		Logger logger = Logger.getLogger(LogTest.class.getName());
		logger.fine("de");
		logger.info("in");
		logger.warning("wn");
		logger.log(Level.SEVERE, "er", new Exception("ex"));
		
		
		Log log2 = Logs.getLog("panda.log.test.A");
		log2.debug("this is a");
	}

}
