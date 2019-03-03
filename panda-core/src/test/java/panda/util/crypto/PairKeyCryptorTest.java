package panda.util.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.junit.Assert;
import org.junit.Test;

import panda.log.Log;
import panda.log.Logs;

public class PairKeyCryptorTest {
	private static final Log log = Logs.getLog(PairKeyCryptorTest.class);
	
	@Test
	public void testRsa() throws Exception {
		testPKI("RSA", "RSA");
	}
	
	@Test
	public void testRsaEcb() throws Exception {
		testPKI("RSA", "RSA/ECB/PKCS1Padding");
	}
	
	private void testPKI(String keya, String algorithm) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keya);
		keyPairGenerator.initialize(2048);
		KeyPair pair = keyPairGenerator.genKeyPair();
		String text = "this is a sample test.";
		
		Cryptor pc = new Cryptor(algorithm, pair.getPublic(), pair.getPrivate());
		String ed = pc.encrypt(text);
		String dd = pc.decrypt(ed);
		
		log.debug("PKI " + algorithm + ": " + ed);
		Assert.assertEquals(dd, text);
	}
}
