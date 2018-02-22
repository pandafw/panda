package panda.util.crypto;

import org.junit.Test;

import panda.util.crypto.Macs;

/**
 * test class for Digests
 */
public class MacsTest {

	@Test
	public void testHmacs() {
		String secret = "aa";
		String data = "bb";
		
		System.out.println("256Hex:    " + Macs.sha256Hex(secret, data));
		System.out.println("256Base64: " + Macs.sha256Base64(secret, data));
		System.out.println("512Hex:    " + Macs.sha512Hex(secret, data));
		System.out.println("512Base64: " + Macs.sha512Base64(secret, data));
	}
}
