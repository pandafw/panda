package panda.mvc.util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.util.crypto.Cryptor;

@IocBean(type=Cryptor.class)
public class MvcCryptor extends Cryptor {
	public static final String DEFAULT_ALGORITHM = "AES";
	public static final String DEFAULT_SECRETKEY = "== Panda Java ==";

	public MvcCryptor() {
		super(DEFAULT_ALGORITHM, DEFAULT_SECRETKEY);
	}

	@IocInject(value=MvcConstants.CRYPTO_ALGORITHM, required=false)
	public void setAlgorithm(String algorithm) {
		super.setAlgorithm(algorithm);
	}
	
	@IocInject(value=MvcConstants.CRYPTO_KEY_SECRET, required=false)
	public void setCryptoSecretKey(String key) throws IOException {
		File file = new File(key);
		if (Files.exists(file)) {
			setSecretKey(file);
		}
		else {
			setSecretKey(key);
		}
	}

	/**
	 * @param key the encode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	@IocInject(value=MvcConstants.CRYPTO_KEY_ENCODE, required=false)
	public void setCryptoEncodeKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		File file = new File(key);
		if (Files.exists(file)) {
			setEncodeKey(file);
		}
		else {
			setEncodeKey(key);
		}
	}

	/**
	 * @param key the decode key file or text
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException
	 */
	@IocInject(value=MvcConstants.CRYPTO_KEY_DECODE, required=false)
	public void setCryptoDecodeKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		File file = new File(key);
		if (Files.exists(file)) {
			setDecodeKey(file);
		}
		else {
			setDecodeKey(key);
		}
	}
}
