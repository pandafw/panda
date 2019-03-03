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
import panda.log.Log;
import panda.log.Logs;

/**
 * test class for Encrypts
 */
public class SecretCryptorTest {
	private static final Log log = Logs.getLog(SecretCryptorTest.class);
	
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
		encdec("panda", Algorithms.Blowfish);
	}

	@Test
	public void testDES() throws Exception {
		encdec("1234567", "12345678", Algorithms.DES);
	}

	@Test
	public void testAES() {
		encdec("1234567890123456", Algorithms.AES);
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
		log.debug(algo + ": " + sw);
	}

	@Test
	public void testSpeed() {
		final int CNT = 100;

		List<String> samples = new ArrayList<String>();
		for (int i = 0; i < CNT; i++) {
			samples.add(Randoms.randString(11));
		}

		speedTest(samples, Algorithms.AES);
		speedTest(samples, Algorithms.Blowfish);
		speedTest(samples, "12345678", Algorithms.DES);
		speedTest(samples, "123456781234567812345678", Algorithms.DESede);
		speedTest(samples, Algorithms.RC2);
		speedTest(samples, Algorithms.RC4);
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

		log.debug("Default\tCipher");
		for (Iterator i = ciphers.entrySet().iterator(); i.hasNext();) {
			Map.Entry cipher = (Map.Entry)i.next();

			StringBuilder sb = new StringBuilder();
			if (Boolean.TRUE.equals(cipher.getValue()))
				sb.append('*');
			else
				sb.append(' ');

			sb.append('\t').append(cipher.getKey());
			log.debug(sb.toString());
		}
	}
}
