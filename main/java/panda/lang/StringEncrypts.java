package panda.lang;

import panda.lang.codec.binary.Base64;

/**
 * @author yf.frank.wang@gmail.com
 */
public class StringEncrypts {

	public static String DEFAULT_TRANSFORM = Encrypts.ENCRYPT_Blowfish;

	public static String DEFAULT_KEY = "panda";

	public static String encrypt(String text) {
		return encrypt(text, DEFAULT_KEY, DEFAULT_TRANSFORM);
	}

	public static String decrypt(String text) {
		return decrypt(text, DEFAULT_KEY, DEFAULT_TRANSFORM);
	}
	
	public static String encrypt(String text, String key) {
		return encrypt(text, key, DEFAULT_TRANSFORM);
	}

	public static String decrypt(String text, String key) {
		return decrypt(text, key, DEFAULT_TRANSFORM);
	}
	
	public static String encrypt(String text, String key, String transform) {
		byte[] t = Strings.getBytesUtf8(text);
		byte[] k = Strings.getBytesUtf8(key);
		byte[] encrypted = Encrypts.encrypt(t, k, transform);
		return Strings.newStringUtf8(Base64.encodeBase64(encrypted, false, false));
	}

	public static String decrypt(String text, String key, String transform) {
		byte[] t = Base64.decodeBase64(text);
		byte[] k = Strings.getBytesUtf8(key);
		byte[] decrypted = Encrypts.decrypt(t, k, transform);
		return Strings.newStringUtf8(decrypted);
	}
}
