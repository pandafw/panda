package panda.lang.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;

public class KeyPairs {
	public final static String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
	public final static String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
	public final static String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
	public final static String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";
	public final static String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
	public final static String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";

	//------------------------------------------------------------------------
	// Algorithm
	//------------------------------------------------------------------------
	public final static String DH = "DH"; //DiffieHellman
	public final static String DSA = "DSA";
	public final static String RSA = "RSA";
	public final static String EC = "EC";

	public static KeyPair create(String provider, String algorithm, int keysize) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, provider);
			keyPairGenerator.initialize(keysize);
			KeyPair pair = keyPairGenerator.genKeyPair();
			return pair;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static KeyPair create(String algorithm, int keysize) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
			keyPairGenerator.initialize(keysize);
			KeyPair pair = keyPairGenerator.genKeyPair();
			return pair;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static PrivateKey getRSAPrivateKey(byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String encoded = Strings.strip(Strings.remove(Strings.remove(new String(data), BEGIN_PRIVATE_KEY), END_PRIVATE_KEY));
		byte[] decoded = Base64.decodeBase64(encoded);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
		KeyFactory kf = KeyFactory.getInstance(RSA);
		return kf.generatePrivate(spec);
	}
	
	
	public static PrivateKey getRSAPrivateKey(InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] data = Streams.toByteArray(is);
		return getRSAPrivateKey(data);
	}

	public static PublicKey getRSAPublicKey(byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String encoded = Strings.strip(Strings.remove(Strings.remove(new String(data), BEGIN_PUBLIC_KEY), END_PUBLIC_KEY));

		byte[] decoded = Base64.decodeBase64(encoded);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
		KeyFactory kf = KeyFactory.getInstance(RSA);
		return kf.generatePublic(spec);
	}
	
	public static PublicKey getRSAPublicKey(InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] data = Streams.toByteArray(is);
		return getRSAPublicKey(data);
	}

	public static void toPem(PrivateKey key, Appendable out) throws IOException {
		out.append(BEGIN_PRIVATE_KEY);
		out.append(Strings.CRLF);
		out.append(Base64.encodeBase64ChunkedString(key.getEncoded()));
		out.append(END_PRIVATE_KEY);
		out.append(Strings.CRLF);
	}
	
	public static String toPem(PrivateKey key) {
		StringBuilder out = new StringBuilder();
		try {
			toPem(key, out);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return out.toString();
	}

	public static void toPem(PublicKey key, Appendable out) throws IOException {
		out.append(BEGIN_PUBLIC_KEY);
		out.append(Strings.CRLF);
		out.append(Base64.encodeBase64ChunkedString(key.getEncoded()));
		out.append(END_PUBLIC_KEY);
		out.append(Strings.CRLF);
	}
	
	public static String toPem(PublicKey key) {
		StringBuilder out = new StringBuilder();
		try {
			toPem(key, out);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return out.toString();
	}
}
