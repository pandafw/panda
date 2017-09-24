package panda.lang.crypto;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;

public class Cryptor {
	protected String algorithm;
	protected Key encodeKey;
	protected Key decodeKey;

	public Cryptor(String algorithm, String secretKey) {
		this.algorithm = algorithm;
		SecretKeySpec skeys = Keys.secretKeySpec(secretKey, algorithm);
		this.encodeKey = skeys;
		this.decodeKey = skeys;
	}

	public Cryptor(String algorithm, Key encodeKey, Key decodeKey) {
		this.algorithm = algorithm;
		this.encodeKey = encodeKey;
		this.decodeKey = decodeKey;
	}

	public String encrypt(String text) {
		byte[] t = Strings.getBytesUtf8(text);
		byte[] encrypted = encrypt(t);
		return Strings.newStringUtf8(Base64.encodeBase64(encrypted, false, false));
	}

	public String decrypt(String text) {
		byte[] t = Base64.decodeBase64(text);
		byte[] decrypted = decrypt(t);
		return Strings.newStringUtf8(decrypted);
	}

	public byte[] encrypt(byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, encodeKey);
			byte[] encrypted = cipher.doFinal(data);
			return encrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public byte[] decrypt(byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, decodeKey);
			byte[] decrypted = cipher.doFinal(data);
			return decrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public CipherOutputStream encrypt(OutputStream os) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, encodeKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			return cos;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public CipherInputStream decrypt(InputStream is) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, decodeKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			return cis;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
