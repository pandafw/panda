package panda.util.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.junit.Assert;
import org.junit.Test;

import panda.util.crypto.Cryptor;

public class PairKeyCryptorTest {
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
		
		System.out.println("PKI " + algorithm + ": " + ed);
		Assert.assertEquals(dd, text);
	}
}
