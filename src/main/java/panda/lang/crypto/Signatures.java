package panda.lang.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import panda.lang.Exceptions;

/**
 * https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature
 */
public class Signatures {
	public final static String NONE_WITH_RSA        = "NONEwithRSA";
	public final static String MD2_WITH_RSA         = "MD2withRSA";
	public final static String MD5_WITH_RSA         = "MD5withRSA";
	public final static String SHA1_WITH_RSA        = "SHA1withRSA";
	public final static String SHA256_WITH_RSA      = "SHA256withRSA";
	public final static String SHA384_WITH_RSA      = "SHA384withRSA";
	public final static String SHA512_WITH_RSA      = "SHA512withRSA";
	public final static String NONE_WITH_DSA        = "NONEwithDSA";
	public final static String SHA1_WITH_DSA        = "SHA1withDSA";
	public final static String NONE_WITH_ECDSA      = "NONEwithECDSA";
	public final static String SHA1_WITH_ECDSA      = "SHA1withECDSA";
	public final static String SHA256_WITH_ECDSA    = "SHA256withECDSA";
	public final static String SHA384_WITH_ECDSA    = "SHA384withECDSA";
	public final static String SHA512_WITH_ECDSA    = "SHA512withECDSA";

	public static byte[] sign(byte[] data, PrivateKey key, String algorithm) {
		try {
			Signature sig = Signature.getInstance(algorithm);
			sig.initSign(key);
			sig.update(data);
			return sig.sign();
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static boolean verify(byte[] signature, byte[] data, PublicKey key, String algorithm) {
		try {
			Signature sig = Signature.getInstance(algorithm);
			sig.initVerify(key);
			sig.update(data);
			return sig.verify(signature);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
