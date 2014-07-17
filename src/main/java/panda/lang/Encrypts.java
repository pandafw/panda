package panda.lang;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import panda.lang.codec.binary.Base64;


/**
 * @author yf.frank.wang@gmail.com
 */
public class Encrypts {
	public static final String AES = "AES";
	public static final String AESWrap = "AESWrap";
	public static final String AESFOUR = "AESFOUR";
	public static final String Blowfish = "Blowfish";
	public static final String DES = "DES";
	public static final String DESede = "DESede";
	public static final String DESedeWrap = "DESedeWrap";
	public static final String ECIES = "ECIES";
	public static final String RC2 = "RC2";
	public static final String RC4 = "RC4";
	public static final String RC5 = "RC5";
	public static final String RSA = "RSA";
	
	//------------------------------------------------------------------------------
	private static final String DEFAULT_TRANSFORM = Encrypts.Blowfish;

	private static final byte[] DEFAULT_KEY_BYTES = "== Panda Java ==".getBytes();

	public static String encrypt(String text) {
		return encrypt(text, null, DEFAULT_TRANSFORM);
	}

	public static String decrypt(String text) {
		return decrypt(text, null, DEFAULT_TRANSFORM);
	}
	
	public static String encrypt(String text, String key) {
		return encrypt(text, key, DEFAULT_TRANSFORM);
	}

	public static String decrypt(String text, String key) {
		return decrypt(text, key, DEFAULT_TRANSFORM);
	}
	
	public static String encrypt(String text, String key, String transform) {
		byte[] t = Strings.getBytesUtf8(text);
		byte[] k = key == null ? DEFAULT_KEY_BYTES : Strings.getBytesUtf8(key);
		byte[] encrypted = encrypt(t, k, transform);
		return Strings.newStringUtf8(Base64.encodeBase64(encrypted, false, false));
	}

	public static String decrypt(String text, String key, String transform) {
		byte[] t = Base64.decodeBase64(text);
		byte[] k = key == null ? DEFAULT_KEY_BYTES : Strings.getBytesUtf8(key);
		byte[] decrypted = decrypt(t, k, transform);
		return Strings.newStringUtf8(decrypted);
	}

	//------------------------------------------------------------------------------
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
}
