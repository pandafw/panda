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
	public static final String DEFAULT_CIPHER = Encrypts.Blowfish;
	public static final String DEFAULT_KEY = "== Panda Java ==";
	public static final byte[] DEFAULT_KEY_BYTES = DEFAULT_KEY.getBytes();

	//------------------------------------------------------------------------------
	public static String trimKey(String key) {
		return Strings.center(key, 16, '-');
	}
	
	public static byte[] keyBytes(String key) {
		return Strings.getBytesUtf8(key);
	}
	
	public static String encrypt(String text) {
		return encrypt(text, null, DEFAULT_CIPHER);
	}

	public static String decrypt(String text) {
		return decrypt(text, null, DEFAULT_CIPHER);
	}
	
	public static String encrypt(String text, String key) {
		return encrypt(text, key, DEFAULT_CIPHER);
	}

	public static String decrypt(String text, String key) {
		return decrypt(text, key, DEFAULT_CIPHER);
	}
	
	public static String encrypt(String text, String key, String cipher) {
		byte[] t = Strings.getBytesUtf8(text);
		byte[] k = key == null ? DEFAULT_KEY_BYTES : keyBytes(key);
		byte[] encrypted = encrypt(t, k, cipher);
		return Strings.newStringUtf8(Base64.encodeBase64(encrypted, false, false));
	}

	public static String decrypt(String text, String key, String cipher) {
		byte[] t = Base64.decodeBase64(text);
		byte[] k = key == null ? DEFAULT_KEY_BYTES : keyBytes(key);
		byte[] decrypted = decrypt(t, k, cipher);
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
	
	public static byte[] encrypt(byte[] text, byte[] key, String cipher) {
		try {
			SecretKeySpec sksSpec = new SecretKeySpec(key, cipher);
			Cipher c = Cipher.getInstance(cipher);
			c.init(Cipher.ENCRYPT_MODE, sksSpec);
			byte[] encrypted = c.doFinal(text);
			return encrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] encrypt(byte[] text, SecretKey key, String cipher) {
		try {
			Cipher c = Cipher.getInstance(cipher);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = c.doFinal(text);
			return encrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] decrypt(byte[] data, byte[] key, String cipher) {
		try {
			SecretKeySpec sksSpec = new SecretKeySpec(key, cipher);
			Cipher c = Cipher.getInstance(cipher);
			c.init(Cipher.DECRYPT_MODE, sksSpec);
			byte[] decrypted = c.doFinal(data);
			return decrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] decrypt(byte[] data, SecretKey key, String cipher) {
		try {
			Cipher c = Cipher.getInstance(cipher);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = c.doFinal(data);
			return decrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
