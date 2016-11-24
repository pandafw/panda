package panda.lang.crypto;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;

/**
 * https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public class Encrypts {
	//------------------------------------------------------------------------------
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
	/** AES/CBC/NoPadding (128) */
	public static final String AES_CBC_NOPADDING                      =  "AES/CBC/NoPadding";
	/** AES/CBC/PKCS5Padding (128) */
	public static final String AES_CBC_PKCS5PADDING                   = "AES/CBC/PKCS5Padding";
	/** AES/ECB/NoPadding (128) */
	public static final String AES_ECB_NOPADDING                      = "AES/ECB/NoPadding";
	/** AES/ECB/PKCS5Padding (128) */
	public static final String AES_ECB_PKCS5PADDING                   = "AES/ECB/PKCS5Padding";
	/** DES/CBC/NoPadding (56) */
	public static final String DES_CBC_NOPADDING                      = "DES/CBC/NoPadding";
	/** DES/CBC/PKCS5Padding (56) */
	public static final String DES_CBC_PKCS5PADDING                   = "DES/CBC/PKCS5Padding";
	/** DES/ECB/NoPadding (56) */
	public static final String DES_ECB_NOPADDING                      = "DES/ECB/NoPadding";
	/** DES/ECB/PKCS5Padding (56) */
	public static final String DES_ECB_PKCS5PADDING                   = "DES/ECB/PKCS5Padding";
	/** DESede/CBC/NoPadding (168) */
	public static final String DESEDE_CBC_NOPADDING                   = "DESede/CBC/NoPadding";
	/** DESede/CBC/PKCS5Padding (168) */
	public static final String DESEDE_CBC_PKCS5PADDING                = "DESede/CBC/PKCS5Padding";
	/** DESede/ECB/NoPadding (168) */
	public static final String DESEDE_ECB_NOPADDING                   = "DESede/ECB/NoPadding";
	/** DESede/ECB/PKCS5Padding (168) */
	public static final String DESEDE_ECB_PKCS5PADDING                = "DESede/ECB/PKCS5Padding";
	/** RSA/ECB/PKCS1Padding (1024, 2048) */
	public static final String RSA_ECB_PKCS1PADDING                   = "RSA/ECB/PKCS1Padding";
	/** RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048) */
	public static final String RSA_ECB_OAEPWITHSHA1_AND_MGF1PADDING    = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
	/** RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048) */
	public static final String RSA_ECB_OAEPWITHSHA256_AND_MGF1PADDING  = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
	
	//------------------------------------------------------------------------------
	public static final String DEFAULT_CIPHER = Encrypts.Blowfish;
	public static final String DEFAULT_KEY = "== Panda Java ==";

	//------------------------------------------------------------------------------
	public static byte[] keyBytes(String key) {
		return Strings.getBytesUtf8(key);
	}
	
	public static String encrypt(String text, String key, String algorithm) {
		byte[] t = Strings.getBytesUtf8(text);
		byte[] k = keyBytes(key);
		byte[] encrypted = encrypt(t, k, algorithm);
		return Strings.newStringUtf8(Base64.encodeBase64(encrypted, false, false));
	}

	public static String decrypt(String text, String key, String algorithm) {
		byte[] t = Base64.decodeBase64(text);
		byte[] k = keyBytes(key);
		byte[] decrypted = decrypt(t, k, algorithm);
		return Strings.newStringUtf8(decrypted);
	}

	//------------------------------------------------------------------------------
	public static byte[] encrypt(byte[] data, Key key, String algorithm) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = cipher.doFinal(data);
			return encrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static byte[] decrypt(byte[] data, Key key, String algorithm) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = cipher.doFinal(data);
			return decrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	public static byte[] encrypt(byte[] data, byte[] key, String algorithm) {
		SecretKeySpec skey = new SecretKeySpec(key, algorithm);
		return encrypt(data, skey, algorithm);
	}

	public static byte[] decrypt(byte[] data, byte[] key, String algorithm) {
		SecretKeySpec skey = new SecretKeySpec(key, algorithm);
		return decrypt(data, skey, algorithm);
	}
}
