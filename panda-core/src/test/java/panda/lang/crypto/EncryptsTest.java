package panda.lang.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLServerSocketFactory;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Randoms;
import panda.lang.codec.binary.Hex;
import panda.lang.time.StopWatch;

/**
 * test class for Encrypts
 */
public class EncryptsTest extends TestCase {
	private void encdec(String text, String trans) {
		String enc = Encrypts.encrypt(text, Encrypts.DEFAULT_KEY, trans);
		String dec = Encrypts.decrypt(enc, Encrypts.DEFAULT_KEY, trans);

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

	@Test
	public void testGetAvailableCiphers() {
		SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

		String[] defaultCiphers = ssf.getDefaultCipherSuites();
		String[] availableCiphers = ssf.getSupportedCipherSuites();

		TreeMap<String, Boolean> ciphers = new TreeMap<String, Boolean>();

		for (int i = 0; i < availableCiphers.length; ++i)
			ciphers.put(availableCiphers[i], Boolean.FALSE);

		for (int i = 0; i < defaultCiphers.length; ++i)
			ciphers.put(defaultCiphers[i], Boolean.TRUE);

		System.out.println("Default\tCipher");
		for (Iterator i = ciphers.entrySet().iterator(); i.hasNext();) {
			Map.Entry cipher = (Map.Entry)i.next();

			if (Boolean.TRUE.equals(cipher.getValue()))
				System.out.print('*');
			else
				System.out.print(' ');

			System.out.print('\t');
			System.out.println(cipher.getKey());
		}
	}
	
	@Test
	public void testPKI() throws Exception {
		testPKI("RSA", "RSA");
		testPKI("RSA", "RSA/ECB/PKCS1Padding");
	}
	
	private void testPKI(String keya, String algorithm) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keya);
		keyPairGenerator.initialize(2048);
		KeyPair pair = keyPairGenerator.genKeyPair();
		String text = "this is a sample test.";
		
		byte[] ed = Encrypts.encrypt(text.getBytes(), pair.getPrivate(), algorithm);
		byte[] dd = Encrypts.decrypt(ed, pair.getPublic(), algorithm);
		
		System.out.println("PKI " + algorithm + ": " + Hex.encodeHexString(ed));
		Assert.assertEquals(new String(dd), text);
	}
}
