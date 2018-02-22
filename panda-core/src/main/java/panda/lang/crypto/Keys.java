package panda.lang.crypto;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.spec.SecretKeySpec;

import panda.codec.binary.Base64;
import panda.lang.Exceptions;
import panda.lang.Strings;

public class Keys {
	public final static String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
	public final static String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
	public final static String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
	public final static String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";
	public final static String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
	public final static String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";

	//------------------------------------------------------------------------
	// Algorithm
	//------------------------------------------------------------------------
	public final static String DH = "DH"; //DiffieHellman
	public final static String DSA = "DSA";
	public final static String RSA = "RSA";
	public final static String EC = "EC";

	public static SecretKeySpec secretKeySpec(String key, String algorithm) {
		return secretKeySpec(Strings.getBytesUtf8(key), algorithm);
	}

	public static SecretKeySpec secretKeySpec(byte[] key, String algorithm) {
		return new SecretKeySpec(key, algorithm);
	}
	
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

	/**
	 * get Private Key from raw data
	 *
	 * @param data raw data
	 * @param algorithm algorithm
	 * @return the PrivateKey
	 * @throws NoSuchAlgorithmException if no Provider supports a KeyFactorySpi implementation for
	 *             the specified algorithm.
	 * @throws InvalidKeySpecException if the given key specification is inappropriate for this key
	 *             factory to produce a public key.
	 */
	public static PrivateKey getPrivateKey(byte[] data, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(data);
		KeyFactory kf = KeyFactory.getInstance(algorithm);
		return kf.generatePrivate(spec);
	}

	/**
	 * get Private Key from base64 string
	 *
	 * @param data base64 string
	 * @param algorithm algorithm
	 * @return private key
	 * @throws NoSuchAlgorithmException if no Provider supports a KeyFactorySpi implementation for
	 *             the specified algorithm.
	 * @throws InvalidKeySpecException if the given key specification is inappropriate for this key
	 *             factory to produce a public key.
	 */
	public static PrivateKey getPrivateKey(String data, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String encoded = Strings.strip(Strings.remove(Strings.remove(data, BEGIN_PRIVATE_KEY), END_PRIVATE_KEY));
		byte[] decoded = Base64.decodeBase64(encoded);
		return getPrivateKey(decoded, algorithm);
	}

	/**
	 * get Public Key from raw data
	 *
	 * @param data raw data
	 * @param algorithm algorithm
	 * @return public key
	 * @throws NoSuchAlgorithmException if no Provider supports a KeyFactorySpi implementation for
	 *             the specified algorithm.
	 * @throws InvalidKeySpecException if the given key specification is inappropriate for this key
	 *             factory to produce a public key.
	 */
	public static PublicKey getPublicKey(byte[] data, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
		KeyFactory kf = KeyFactory.getInstance(algorithm);
		return kf.generatePublic(spec);
	}

	/**
	 * get RSA Private Key from base64 string
	 *
	 * @param data base64 string
	 * @param algorithm algorithm
	 * @return public key
	 * @throws NoSuchAlgorithmException if no Provider supports a KeyFactorySpi implementation for
	 *             the specified algorithm.
	 * @throws InvalidKeySpecException if the given key specification is inappropriate for this key
	 *             factory to produce a public key.
	 */
	public static PublicKey getPublicKey(String data, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String encoded = Strings.strip(Strings.remove(Strings.remove(data, BEGIN_PUBLIC_KEY), END_PUBLIC_KEY));
		byte[] decoded = Base64.decodeBase64(encoded);
		return getPublicKey(decoded, algorithm);
	}

	/**
	 * parse key from string
	 * @param data key data string
	 * @param algorithm algorithm
	 * @return key
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static Key parseKey(String data, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (Strings.startsWith(data, BEGIN_PUBLIC_KEY)) {
			return getPublicKey(data, algorithm);
		}
		if (Strings.startsWith(data, BEGIN_PRIVATE_KEY)) {
			return getPrivateKey(data, algorithm);
		}
		throw new IllegalArgumentException("Invalid Key: " + data);
	}

	/**
	 * print pem form key
	 * @param key private key
	 * @param out output
	 * @throws IOException if an IO error occurred
	 */
	public static void toPem(PrivateKey key, Appendable out) throws IOException {
		out.append(BEGIN_PRIVATE_KEY);
		out.append(Strings.CRLF);
		out.append(Base64.encodeBase64ChunkedString(key.getEncoded()));
		out.append(END_PRIVATE_KEY);
		out.append(Strings.CRLF);
	}
	
	/**
	 * print pem form key
	 * @param key private key
	 * @return pem string
	 * @throws IOException if an IO error occurred
	 */
	public static String toPem(PrivateKey key) {
		StringBuilder out = new StringBuilder();
		try {
			toPem(key, out);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return out.toString();
	}


	/**
	 * print pem form key
	 * @param key public key
	 * @param out output
	 * @throws IOException if an IO error occurred
	 */
	public static void toPem(PublicKey key, Appendable out) throws IOException {
		out.append(BEGIN_PUBLIC_KEY);
		out.append(Strings.CRLF);
		out.append(Base64.encodeBase64ChunkedString(key.getEncoded()));
		out.append(END_PUBLIC_KEY);
		out.append(Strings.CRLF);
	}
	

	/**
	 * print pem form key
	 * @param key public key
	 * @return pem string
	 * @throws IOException if an IO error occurred
	 */
	public static String toPem(PublicKey key) {
		StringBuilder out = new StringBuilder();
		try {
			toPem(key, out);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return out.toString();
	}
}
