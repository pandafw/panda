package panda.lang.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import panda.lang.Exceptions;

public class KeyPairs {

	public final static String DH = "DH"; //DiffieHellman
	public final static String DSA = "DSA";
	public final static String RSA = "RSA";
	public final static String EC = "EC";

	public static KeyPair create(String provider, String algorithm, int keysize) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, provider);
			keyPairGenerator.initialize(keysize);
			KeyPair pair = keyPairGenerator.genKeyPair();
			return pair;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static KeyPair create(String algorithm, int keysize) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
			keyPairGenerator.initialize(keysize);
			KeyPair pair = keyPairGenerator.genKeyPair();
			return pair;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
