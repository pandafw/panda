package panda.util.crypto;

import org.junit.Test;

import panda.log.Log;
import panda.log.Logs;

/**
 * test class for Digests
 */
public class MacsTest {

	private static final Log log = Logs.getLog(MacsTest.class);
	
	@Test
	public void testHmacs() {
		String secret = "aa";
		String data = "bb";
		
		log.debug("256Hex:    " + Macs.sha256Hex(secret, data));
		log.debug("256Base64: " + Macs.sha256Base64(secret, data));
		log.debug("512Hex:    " + Macs.sha512Hex(secret, data));
		log.debug("512Base64: " + Macs.sha512Base64(secret, data));
	}
}
