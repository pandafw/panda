package panda.mvc.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.spec.SecretKeySpec;

import panda.io.Settings;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.crypto.Cryptor;
import panda.lang.crypto.Keys;
import panda.mvc.MvcConstants;

@IocBean(type=Cryptor.class)
public class MvcCryptor extends Cryptor {
	public static final String DEFAULT_ALGORITHM = "AES";
	public static final String DEFAULT_KEYPHRASE = "== Panda Java ==";

	@IocInject
	protected Settings settings;
	
	public MvcCryptor() {
		super(DEFAULT_ALGORITHM, DEFAULT_KEYPHRASE);
	}

	@IocInject(value=MvcConstants.CRYPTO_ALGORITHM, required=false)
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
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
	
	@IocInject(value=MvcConstants.CRYPTO_KEY_SECRET, required=false)
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
	@IocInject(value=MvcConstants.CRYPTO_KEY_ENCODE, required=false)
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
	@IocInject(value=MvcConstants.CRYPTO_KEY_DECODE, required=false)
	public void setDecodeKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String data = getKeyContent(key);
		this.decodeKey = Keys.parseKey(data, algorithm);
	}
}
