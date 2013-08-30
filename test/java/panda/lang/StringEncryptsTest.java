package panda.lang;

import panda.lang.Encrypts;
import panda.lang.StringEncrypts;
import junit.framework.TestCase;

/**
 * test class for StringEncrypts
 */
public class StringEncryptsTest extends TestCase {
	private void encdec(String key, String trans) throws Exception {
		String text = "trustme";

		String enc = StringEncrypts.encrypt(text, key, trans);
		String dec = StringEncrypts.decrypt(enc, key, trans);
		
		assertEquals(text, dec);
	}

	public void testBlowfish() throws Exception {
		encdec("panda", Encrypts.ENCRYPT_Blowfish);
	}

//	public void testDES() throws Exception {
//		encdec("1234567", "DES");
//	}

	public void testAES() throws Exception {
		encdec("1234567890123456", Encrypts.ENCRYPT_AES);
	}
}
