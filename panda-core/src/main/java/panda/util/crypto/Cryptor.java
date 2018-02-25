package panda.util.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import panda.codec.binary.Base64;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Strings;

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

	/**
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @return the encodeKey
	 */
	public Key getEncodeKey() {
		return encodeKey;
	}

	/**
	 * @param encodeKey the encodeKey to set
	 */
	public void setEncodeKey(Key encodeKey) {
		this.encodeKey = encodeKey;
	}

	/**
	 * @return the decodeKey
	 */
	public Key getDecodeKey() {
		return decodeKey;
	}

	/**
	 * @param decodeKey the decodeKey to set
	 */
	public void setDecodeKey(Key decodeKey) {
		this.decodeKey = decodeKey;
	}

	protected String getKeyContent(String src) throws IOException {
		try {
			URI uri = new URI(src);
			if (Strings.isNotEmpty(uri.getScheme())) {
				byte[] data = Streams.toByteArray(uri);
				return Strings.newStringUtf8(data);
			}
		}
		catch (URISyntaxException e) {
			// skip
		}
		return src;
	}

	public void setSecretKey(String key) throws IOException {
		String data = getKeyContent(key);
		SecretKeySpec skeys = Keys.secretKeySpec(data, algorithm);
		this.encodeKey = skeys;
		this.decodeKey = skeys;
	}

	/**
	 * @param key the encode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public void setEncodeKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String data = getKeyContent(key);
		this.encodeKey = Keys.parseKey(data, algorithm);
	}

	/**
	 * @param key the decode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException
	 */
	public void setDecodeKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String data = getKeyContent(key);
		this.decodeKey = Keys.parseKey(data, algorithm);
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
