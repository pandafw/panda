package panda.lang;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import panda.lang.time.StopWatch;

/**
 * test class for Encrypts
 */
public class EncryptsTest extends TestCase {
	private void encdec(String text, String trans) {
		String enc = Encrypts.encrypt(text, trans);
		String dec = Encrypts.decrypt(enc, trans);

		assertEquals(text, dec);
	}

	public void testBlowfish() {
		encdec("panda", Encrypts.Blowfish);
	}

	public void testDES() throws Exception {
		encdec("1234567", Encrypts.DES);
	}

	public void testAES() {
		encdec("1234567890123456", Encrypts.AES);
	}
	
	private void speedTest(List<String> samples, String trans) {
		final int CNT = samples.size();
		final int MAX = 1;
		StopWatch sw = new StopWatch();
		for (int j = 0; j < MAX; j++) {
			for (int i = 0; i < CNT; i++) {
				encdec(samples.get(i), trans);
			}
		}
		System.out.println(trans + ": " + sw);
	}

	public void testSpeed() {
		final int CNT = 100;

		List<String> samples = new ArrayList<String>();
		for (int i = 0; i < CNT; i++) {
			samples.add(Randoms.randString(11));
		}

		speedTest(samples, Encrypts.AES);
		speedTest(samples, Encrypts.AES);
		speedTest(samples, Encrypts.AESWrap);
		speedTest(samples, Encrypts.AESFOUR);
		speedTest(samples, Encrypts.Blowfish);
		speedTest(samples, Encrypts.DES);
		speedTest(samples, Encrypts.DESede);
		speedTest(samples, Encrypts.DESedeWrap);
		speedTest(samples, Encrypts.ECIES);
		speedTest(samples, Encrypts.RC2);
		speedTest(samples, Encrypts.RC4);
		speedTest(samples, Encrypts.RC5);
		speedTest(samples, Encrypts.RSA);
	}
}
