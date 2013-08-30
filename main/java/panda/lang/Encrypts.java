package panda.lang;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import panda.lang.codec.binary.Hex;


/**
 * @author yf.frank.wang@gmail.com
 */
public class Encrypts {
	public static String DIGEST_MD2 = "md2";
	public static String DIGEST_MD5 = "md5";
	public static String DIGEST_SHA1 = "sha-1";
	public static String DIGEST_SHA256 = "sha-256";
	public static String DIGEST_SHA384 = "sha-384";
	public static String DIGEST_SHA512 = "sha-512";
	
	public static String ENCRYPT_AES = "AES";
	public static String ENCRYPT_AESWrap = "AESWrap";
	public static String ENCRYPT_AESFOUR = "AESFOUR";
	public static String ENCRYPT_Blowfish = "Blowfish";
	public static String ENCRYPT_DES = "DES";
	public static String ENCRYPT_DESede = "DESede";
	public static String ENCRYPT_DESedeWrap = "DESedeWrap";
	public static String ENCRYPT_ECIES = "ECIES";
	public static String ENCRYPT_RC2 = "RC2";
	public static String ENCRYPT_RC4 = "RC4";
	public static String ENCRYPT_RC5 = "RC5";
	public static String ENCRYPT_RSA = "RSA";
	
	private static final int STREAM_BUFFER_LENGTH = 1024;

	/**
	 * @param digest the digest
	 * @param encryption the encryption
	 * @return "PBEWith" + digest + "And" + encryption
	 */
	public static String pbeWith(String digest, String encryption) {
		return "PBEWith" + digest + "And" + encryption;
	}
	
	public static byte[] encrypt(byte[] text, byte[] key, String transform) {
		try {
			SecretKeySpec sksSpec = new SecretKeySpec(key, transform);
			Cipher cipher = Cipher.getInstance(transform);
			cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
			byte[] encrypted = cipher.doFinal(text);
			return encrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] encrypt(byte[] text, SecretKey key, String transform) {
		try {
			Cipher cipher = Cipher.getInstance(transform);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = cipher.doFinal(text);
			return encrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] decrypt(byte[] data, byte[] key, String transform) {
		try {
			SecretKeySpec sksSpec = new SecretKeySpec(key, transform);
			Cipher cipher = Cipher.getInstance(transform);
			cipher.init(Cipher.DECRYPT_MODE, sksSpec);
			byte[] decrypted = cipher.doFinal(data);
			return decrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] decrypt(byte[] data, SecretKey key, String transform) {
		try {
			Cipher cipher = Cipher.getInstance(transform);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = cipher.doFinal(data);
			return decrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	//----------------------------------------------------------------------
	// Digest
	//
	public static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static MessageDigest md2() {
		return getDigest(DIGEST_MD2);
	}

	public static MessageDigest md5() {
		return getDigest(DIGEST_MD5);
	}

	public static MessageDigest sha1() {
		return getDigest(DIGEST_SHA1);
	}
	
	public static MessageDigest sha256() {
		return getDigest(DIGEST_SHA256);
	}
	
	public static MessageDigest sha384() {
		return getDigest(DIGEST_SHA384);
	}
	
	public static MessageDigest sha512() {
		return getDigest(DIGEST_SHA512);
	}
	
	/**
	 * Read through an InputStream and returns the digest for the data
	 * 
	 * @param digest The MessageDigest to use (e.g. MD5)
	 * @param data Data to digest
	 * @return MD5 digest
	 * @throws IOException On error reading from the stream
	 */
	private static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
		return updateDigest(digest, data).digest();
	}

	/**
	 * Updates the given {@link MessageDigest}.
	 * 
	 * @param messageDigest the {@link MessageDigest} to update
	 * @param valueToDigest the value to update the {@link MessageDigest} with
	 * @return the updated {@link MessageDigest}
	 * @since 1.7
	 */
	public static MessageDigest updateDigest(final MessageDigest messageDigest, final byte[] valueToDigest) {
		messageDigest.update(valueToDigest);
		return messageDigest;
	}

	/**
	 * Updates the given {@link MessageDigest}.
	 * 
	 * @param messageDigest the {@link MessageDigest} to update
	 * @param valueToDigest the value to update the {@link MessageDigest} with
	 * @return the updated {@link MessageDigest}
	 */
	public static MessageDigest updateDigest(final MessageDigest messageDigest, final String valueToDigest) {
		messageDigest.update(Strings.getBytesUtf8(valueToDigest));
		return messageDigest;
	}

	/**
	 * Reads through an InputStream and updates the digest for the data
	 * 
	 * @param digest The MessageDigest to use (e.g. MD5)
	 * @param data Data to digest
	 * @return digest
	 * @throws IOException On error reading from the stream
	 */
	public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) throws IOException {
		final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
		int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

		while (read > -1) {
			digest.update(buffer, 0, read);
			read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
		}

		return digest;
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 * @return MD2 digest
	 */
	public static byte[] md2(byte[] data) {
		return md2().digest(data);
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 * @return MD2 digest
	 * @throws IOException On error reading from the stream
	 */
	public static byte[] md2(InputStream data) throws IOException {
		return digest(md2(), data);
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 */
	public static byte[] md2(String data) {
		return md2(Strings.getBytesUtf8(data));
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data Data to digest
	 * @return MD2 digest as a hex string
	 */
	public static String md2Hex(byte[] data) {
		return Hex.encodeHexString(md2(data));
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data Data to digest
	 * @return MD2 digest as a hex string
	 * @throws IOException On error reading from the stream
	 */
	public static String md2Hex(InputStream data) throws IOException {
		return Hex.encodeHexString(md2(data));
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data Data to digest
	 * @return MD2 digest as a hex string
	 */
	public static String md2Hex(String data) {
		return Hex.encodeHexString(md2(data));
	}


	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 * @return MD5 digest
	 */
	public static byte[] md5(byte[] data) {
		return md5().digest(data);
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 * @return MD5 digest
	 * @throws IOException On error reading from the stream
	 */
	public static byte[] md5(InputStream data) throws IOException {
		return digest(md5(), data);
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 */
	public static byte[] md5(String data) {
		return md5(Strings.getBytesUtf8(data));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(byte[] data) {
		return Hex.encodeHexString(md5(data));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data Data to digest
	 * @return MD5 digest as a hex string
	 * @throws IOException On error reading from the stream
	 */
	public static String md5Hex(InputStream data) throws IOException {
		return Hex.encodeHexString(md5(data));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(String data) {
		return Hex.encodeHexString(md5(data));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 * @return SHA-1 digest
	 */
	public static byte[] sha1(byte[] data) {
		return sha1().digest(data);
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 * @return SHA-1 digest
	 * @throws IOException On error reading from the stream
	 */
	public static byte[] sha1(InputStream data) throws IOException {
		return digest(sha1(), data);
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
	 * 
	 * @param data Data to digest
	 * @return SHA-1 digest
	 */
	public static byte[] sha1(String data) {
		return sha1(Strings.getBytesUtf8(data));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-256 digest
	 */
	public static byte[] sha256(byte[] data) {
		return sha256().digest(data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-256 digest
	 * @throws IOException On error reading from the stream
	 * @since 1.4
	 */
	public static byte[] sha256(InputStream data) throws IOException {
		return digest(sha256(), data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-256 digest
	 */
	public static byte[] sha256(String data) {
		return sha256(Strings.getBytesUtf8(data));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-256 digest as a hex string
	 */
	public static String sha256Hex(byte[] data) {
		return Hex.encodeHexString(sha256(data));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-256 digest as a hex string
	 * @throws IOException On error reading from the stream
	 * @since 1.4
	 */
	public static String sha256Hex(InputStream data) throws IOException {
		return Hex.encodeHexString(sha256(data));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-256 digest as a hex string
	 * @since 1.4
	 */
	public static String sha256Hex(String data) {
		return Hex.encodeHexString(sha256(data));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-384 digest
	 */
	public static byte[] sha384(byte[] data) {
		return sha384().digest(data);
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-384 digest
	 * @throws IOException On error reading from the stream
	 * @since 1.4
	 */
	public static byte[] sha384(InputStream data) throws IOException {
		return digest(sha384(), data);
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-384 digest
	 */
	public static byte[] sha384(String data) {
		return sha384(Strings.getBytesUtf8(data));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-384 digest as a hex string
	 */
	public static String sha384Hex(byte[] data) {
		return Hex.encodeHexString(sha384(data));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-384 digest as a hex string
	 * @throws IOException On error reading from the stream
	 */
	public static String sha384Hex(InputStream data) throws IOException {
		return Hex.encodeHexString(sha384(data));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-384 digest as a hex string
	 */
	public static String sha384Hex(String data) {
		return Hex.encodeHexString(sha384(data));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-512 digest
	 */
	public static byte[] sha512(byte[] data) {
		return sha512().digest(data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-512 digest
	 * @throws IOException On error reading from the stream
	 */
	public static byte[] sha512(InputStream data) throws IOException {
		return digest(sha512(), data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-512 digest
	 */
	public static byte[] sha512(String data) {
		return sha512(Strings.getBytesUtf8(data));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-512 digest as a hex string
	 */
	public static String sha512Hex(byte[] data) {
		return Hex.encodeHexString(sha512(data));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-512 digest as a hex string
	 * @throws IOException On error reading from the stream
	 */
	public static String sha512Hex(InputStream data) throws IOException {
		return Hex.encodeHexString(sha512(data));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 * 
	 * @param data Data to digest
	 * @return SHA-512 digest as a hex string
	 */
	public static String sha512Hex(String data) {
		return Hex.encodeHexString(sha512(data));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a hex string.
	 * 
	 * @param data Data to digest
	 * @return SHA-1 digest as a hex string
	 */
	public static String sha1Hex(byte[] data) {
		return Hex.encodeHexString(sha1(data));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a hex string.
	 * 
	 * @param data Data to digest
	 * @return SHA-1 digest as a hex string
	 * @throws IOException On error reading from the stream
	 */
	public static String sha1Hex(InputStream data) throws IOException {
		return Hex.encodeHexString(sha1(data));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a hex string.
	 * 
	 * @param data Data to digest
	 * @return SHA-1 digest as a hex string
	 */
	public static String sha1Hex(String data) {
		return Hex.encodeHexString(sha1(data));
	}
}
