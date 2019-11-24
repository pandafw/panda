package panda.util.crypto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import panda.codec.binary.Base64;
import panda.io.Files;
import panda.lang.Exceptions;
import panda.lang.Strings;

public class Cryptor {
	protected String algorithm;
	protected Key encodeKey;
	protected Key decodeKey;
	protected IvParameterSpec ivParam;

	public Cryptor(String algorithm) {
		this.algorithm = algorithm;
	}

	public Cryptor(String algorithm, String secretKey) {
		this.algorithm = algorithm;
		setSecretKey(secretKey);
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

	/**
	 * @param secretKey secret key to set
	 */
	public void setSecretKey(byte[] secretKey) {
		String keya = Strings.substringBefore(algorithm, '/');
		SecretKeySpec skeys = Keys.secretKeySpec(secretKey, keya);
		this.encodeKey = skeys;
		this.decodeKey = skeys;
	}

	/**
	 * @param secretKey secret key to set
	 */
	public void setSecretKey(String secretKey) {
		byte[] data = Strings.getBytesUtf8(secretKey);
		setSecretKey(data);
	}

	/**
	 * @param secretKey secret key file to set
	 * @throws IOException 
	 */
	public void setSecretKey(File secretKey) throws IOException {
		byte[] data = Files.readToBytes(secretKey);
		setSecretKey(data);
	}

	/**
	 * @param key the encode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void setEncodeKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.encodeKey = Keys.parseKey(key, algorithm);
	}

	/**
	 * @param key the decode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void setDecodeKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.decodeKey = Keys.parseKey(key, algorithm);
	}

	/**
	 * @param key the encode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public void setEncodeKey(File key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		this.encodeKey = Keys.parseKey(key, algorithm);
	}

	/**
	 * @param key the decode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException
	 */
	public void setDecodeKey(File key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		this.decodeKey = Keys.parseKey(key, algorithm);
	}

	/**
	 * @return the ivParam
	 */
	public IvParameterSpec getIvParameterSpec() {
		return ivParam;
	}

	/**
	 * @return the ivParam
	 */
	public String getIvParamString() {
		if (ivParam == null) {
			return null;
		}
		return Strings.newStringUtf8(ivParam.getIV());
	}

	/**
	 * @param ivParam the ivParam to set
	 */
	public void setIvParam(byte[] ivParam) {
		this.ivParam = new IvParameterSpec(ivParam);
	}

	/**
	 * @param ivParam the ivParam to set
	 */
	public void setIvParam(String ivParam) {
		byte[] ivs = Strings.getBytesUtf8(ivParam);
		this.ivParam = new IvParameterSpec(ivs);
	}

	//----------------------------------------------------------
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
			Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, encodeKey);
			byte[] encrypted = cipher.doFinal(data);
			return encrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public byte[] decrypt(byte[] data) {
		try {
			Cipher cipher = initCipher(Cipher.DECRYPT_MODE, decodeKey);
			byte[] decrypted = cipher.doFinal(data);
			return decrypted;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public CipherOutputStream encrypt(OutputStream os) {
		try {
			Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, encodeKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			return cos;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public CipherInputStream decrypt(InputStream is) {
		try {
			Cipher cipher = initCipher(Cipher.DECRYPT_MODE, decodeKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			return cis;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	protected Cipher initCipher(int mode, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance(algorithm);
		if (ivParam == null) {
			cipher.init(mode, key);
		}
		else {
			cipher.init(mode, key, ivParam);
		}
		return cipher;
	}
}
