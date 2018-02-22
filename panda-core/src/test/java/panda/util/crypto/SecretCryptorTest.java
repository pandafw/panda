package panda.util.crypto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLServerSocketFactory;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Randoms;
import panda.lang.time.StopWatch;
import panda.util.crypto.Ciphers;
import panda.util.crypto.Cryptor;

/**
 * test class for Encrypts
 */
public class SecretCryptorTest {
	private void encdec(String text, String algo) {
		encdec(text, null, algo);
	}

	private void encdec(String text, String key, String algo) {
		key = key == null ? "== Panda Java ==" : key;
		
		Cryptor sc = new Cryptor(algo, key);
		String enc = sc.encrypt(text);
		String dec = sc.decrypt(enc);

		Assert.assertEquals(text, dec);
	}

	@Test
	public void testBlowfish() {
		encdec("panda", Ciphers.Blowfish);
	}

	@Test
	public void testDES() throws Exception {
		encdec("1234567", "12345678", Ciphers.DES);
	}

	@Test
	public void testAES() {
		encdec("1234567890123456", Ciphers.AES);
	}
	
	private void speedTest(List<String> samples, String algo) {
		speedTest(samples, null, algo);
	}
	
	private void speedTest(List<String> samples, String key, String algo) {
		final int CNT = samples.size();
		final int MAX = 10;
		StopWatch sw = new StopWatch();
		for (int j = 0; j < MAX; j++) {
			for (int i = 0; i < CNT; i++) {
				encdec(samples.get(i), key, algo);
			}
		}
		System.out.println(algo + ": " + sw);
	}

	@Test
	public void testSpeed() {
		final int CNT = 100;

		List<String> samples = new ArrayList<String>();
		for (int i = 0; i < CNT; i++) {
			samples.add(Randoms.randString(11));
		}

		speedTest(samples, Ciphers.AES);
		speedTest(samples, Ciphers.Blowfish);
		speedTest(samples, "12345678", Ciphers.DES);
		speedTest(samples, "123456781234567812345678", Ciphers.DESede);
		speedTest(samples, Ciphers.RC2);
		speedTest(samples, Ciphers.RC4);
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
}
