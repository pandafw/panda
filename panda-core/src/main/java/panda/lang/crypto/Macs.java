package panda.lang.crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.codec.binary.Base64;
import panda.lang.codec.binary.Hex;


public class Macs {
	public static final String HMAC_MD5 = "HmacMD5";
	public static final String HMAC_SHA1 = "HmacSHA1";
	public static final String HMAC_SHA256 = "HmacSHA256";
	public static final String HMAC_SHA512 = "HmacSHA512";

	//----------------------------------------------------------------------
	// Digest
	//
	public static Mac mac(String algorithm) {
		try {
			return Mac.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Mac sha256() {
		return mac(HMAC_SHA256);
	}

	public static Mac sha512() {
		return mac(HMAC_SHA512);
	}

	public static byte[] digest(String algorithm, String secret, String data) {
		try {
			return digest(algorithm, secret, data.getBytes(Charsets.UTF_8));
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	public static byte[] digest(String algorithm, byte[] secret, String data) {
		try {
			return digest(algorithm, secret, data.getBytes(Charsets.UTF_8));
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	public static byte[] digest(String algorithm, String secret, byte[] data) {
		try {
			return digest(algorithm, secret.getBytes(Charsets.UTF_8), data);
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] digest(String algorithm, byte[] secret, byte[] data) {
		Mac mac = mac(algorithm);
		SecretKeySpec sks = new SecretKeySpec(secret, algorithm);
		
		try {
			mac.init(sks);
		}
		catch (InvalidKeyException e) {
			throw new IllegalArgumentException(e);
		}

		byte[] sig = mac.doFinal(data);
		return sig;
	}

	public static String digestHex(String algorithm, String secret, String data) {
		return Hex.encodeHexString(digest(algorithm, secret, data));
	}
	
	public static String digestHex(String algorithm, String secret, byte[] data) {
		return Hex.encodeHexString(digest(algorithm, secret, data));
	}

	public static String digestHex(String algorithm, byte[] secret, byte[] data) {
		return Hex.encodeHexString(digest(algorithm, secret, data));
	}

	public static String digestBase64(String algorithm, String secret, String data) {
		return Base64.encodeBase64String(digest(algorithm, secret, data));
	}
	
	public static String digestBase64(String algorithm, String secret, byte[] data) {
		return Base64.encodeBase64String(digest(algorithm, secret, data));
	}
	
	public static String digestBase64(String algorithm, byte[] secret, byte[] data) {
		return Base64.encodeBase64String(digest(algorithm, secret, data));
	}
	
	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-256 digest
	 */
	public static byte[] sha256(String secret, byte[] data) {
		return digest(HMAC_SHA256, secret, data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-256 digest
	 * @throws IOException On error reading from the stream
	 */
	public static byte[] sha256(String secret, String data) throws IOException {
		return digest(HMAC_SHA256, secret, data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-256 digest as a hex string
	 */
	public static String sha256Hex(String secret, byte[] data) {
		return digestHex(HMAC_SHA256, secret, data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-256 digest as a hex string
	 */
	public static String sha256Hex(String secret, String data) {
		return digestHex(HMAC_SHA256, secret, data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a base64 string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-256 digest as a base64 string
	 */
	public static String sha256Base64(String secret, byte[] data) {
		return digestBase64(HMAC_SHA256, secret, data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a base64 string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-256 digest as a base64 string
	 */
	public static String sha256Base64(String secret, String data) {
		return digestBase64(HMAC_SHA256, secret, data);
	}

	
	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-512 digest
	 */
	public static byte[] sha512(String secret, byte[] data) {
		return digest(HMAC_SHA512, secret, data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-512 digest
	 * @throws IOException On error reading from the stream
	 */
	public static byte[] sha512(String secret, String data) throws IOException {
		return digest(HMAC_SHA512, secret, data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-512 digest as a hex string
	 */
	public static String sha512Hex(String secret, byte[] data) {
		return digestHex(HMAC_SHA512, secret, data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-512 digest as a hex string
	 */
	public static String sha512Hex(String secret, String data) {
		return digestHex(HMAC_SHA512, secret, data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a base64 string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-512 digest as a base64 string
	 */
	public static String sha512Base64(String secret, byte[] data) {
		return digestBase64(HMAC_SHA512, secret, data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a base64 string.
	 * 
	 * @param secret the secret string
	 * @param data Data to digest
	 * @return SHA-512 digest as a base64 string
	 */
	public static String sha512Base64(String secret, String data) {
		return digestBase64(HMAC_SHA512, secret, data);
	}
}
