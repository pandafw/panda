package panda.net.mail.dkim;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import panda.codec.binary.Base64;
import panda.io.Streams;
import panda.lang.crypto.Keys;

public final class DomainKeyTest {

	private static final Map<String, DomainKey> CACHE = new HashMap<String, DomainKey>();

	private static final long DEFAULT_CACHE_TTL = 2 * 60 * 60 * 1000;

	private static long cacheTtl = DEFAULT_CACHE_TTL;

	private DomainKeyTest() {
	}

	public static void main(String[] args) {
		try {
			DomainKey dkey = getDomainKey(args[0], args[1]);
			
			System.out.println("=========== DOMAIN KEY ==============");
			System.out.println(dkey);
			
			PrivateKey pkey = Keys.getPrivateKey(Streams.toByteArray(new File(args[2])), Keys.RSA);
			System.out.println("=========== PRIVATE KEY ==============");
			System.out.println(Base64.encodeBase64ChunkedString(pkey.getEncoded()));
			
			check(dkey, "test@" + args[0], pkey);
		}
		catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("DomainKeyTest <domain> <selector> <private key file>");
		}
	}
	
	/**
	 * Returns the configured TTL (time to live) for retrieved {@link DomainKey}
	 * s.
	 * 
	 * @return The configured TTL for retrieved {@link DomainKey}s.
	 */
	public static synchronized long getCacheTtl() {
		return cacheTtl;
	}

	/**
	 * Sets the TTL (time to live) for retrieved {@link DomainKey}s.
	 * 
	 * @param cacheTtl
	 *            The TTL for retrieved {@link DomainKey}s.
	 */
	public static synchronized void setCacheTtl(long cacheTtl) {
		if (cacheTtl < 0) {
			cacheTtl = DEFAULT_CACHE_TTL;
		}
		DomainKeyTest.cacheTtl = cacheTtl;
	}

	/**
	 * Retrieves the {@link DomainKey} for the given signing domain and
	 * selector.
	 * 
	 * @param signingDomain
	 *            The signing domain.
	 * @param selector
	 *            The selector.
	 * @return The retrieved {@link DomainKey}.
	 * @throws DkimException
	 *             If the domain key couldn't be retrieved or if either the
	 *             version, key type or service type given in the tags is
	 *             incompatible to this library ('DKIM1', 'RSA' and 'email'
	 *             respectively).
	 */
	public static synchronized DomainKey getDomainKey(String signingDomain, String selector) throws DkimException {
		return getDomainKey(getRecordName(signingDomain, selector));
	}

	private static synchronized DomainKey getDomainKey(String recordName) throws DkimException {
		DomainKey domainKey = CACHE.get(recordName);
		if (null != domainKey) {
			if (0 == cacheTtl || domainKey.getTimestamp() + cacheTtl > System.currentTimeMillis()) {
				return domainKey;
			}
		}
		domainKey = fetchDomainKey(recordName);
		CACHE.put(recordName, domainKey);
		return domainKey;
	}

	private static DomainKey fetchDomainKey(String recordName) throws DkimException {
		return new DomainKey(getTags(recordName));
	}

	/**
	 * Retrieves the tags of a domain key for the given signing domain and
	 * selector.
	 * 
	 * @param signingDomain
	 *            The signing domain.
	 * @param selector
	 *            The selector.
	 * @return The retrieved tags.
	 * @throws DkimException
	 *             If the domain key couldn't be retrieved.
	 */
	public static Map<Character, String> getTags(String signingDomain, String selector) throws DkimException {
		return getTags(getRecordName(signingDomain, selector));
	}

	private static Map<Character, String> getTags(String recordName) throws DkimException {
		Map<Character, String> tags = new HashMap<Character, String>();
		for (String tag : getValue(recordName).split(";")) {
			try {
				tag = tag.trim();
				tags.put(tag.charAt(0), tag.substring(2));
			} catch (IndexOutOfBoundsException e) {
				throw new DkimException("The tag " + tag + " in RR " + recordName + " couldn't be decoded.", e);
			}
		}
		return tags;
	}

	/**
	 * Retrieves the raw domain key for the given signing domain and selector.
	 * 
	 * @param signingDomain
	 *            The signing domain.
	 * @param selector
	 *            The selector.
	 * @return The raw domain key.
	 * @throws DkimException
	 *             If the domain key couldn't be retrieved.
	 */
	public static String getValue(String signingDomain, String selector) throws DkimException {
		return getValue(getRecordName(signingDomain, selector));
	}

	private static String getValue(String recordName) throws DkimException {
		try {
			DirContext dnsContext = new InitialDirContext(getEnvironment());
			Attributes attributes = dnsContext.getAttributes(recordName, new String[] { "TXT" });
			Attribute txtRecord = attributes.get("txt");

			if (txtRecord == null) {
				throw new DkimException("There is no TXT record available for " + recordName);
			}

			String value = (String) txtRecord.get();
			if (null == value) {
				throw new DkimException("Value of RR " + recordName + " couldn't be retrieved");
			}
			return value;

		} catch (NamingException ne) {
			throw new DkimException("Selector lookup failed", ne);
		}
	}

	private static String getRecordName(String signingDomain, String selector) {
		return selector + "._domainkey." + signingDomain;
	}

	private static Hashtable<String, String> getEnvironment() {
		Hashtable<String, String> environment = new Hashtable<String, String>();
		environment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		return environment;
	}

	private static final String RSA_MODE = "RSA/ECB/NoPadding";


	/**
	 * Checks, whether this {@code DomainKey} fits to the given identity and
	 * {@link RSAPrivateKey}.
	 * 
	 * @param dkey the dkim key
	 * @param identity
	 *            The identity.
	 * @param privateKey
	 *            The {@link RSAPrivateKey}.
	 * @throws DkimException
	 *             If either the {@link DomainKey#getGranularity() granularity}
	 *             of this {@code DomainKey} doesn't match the given identity or
	 *             the {@link DomainKey#getPublicKey() public key} of this
	 *             {@code DomainKey} doesn't belong to the given
	 *             {@link RSAPrivateKey}.
	 */
	public static void check(DomainKey dkey, String identity, PrivateKey privateKey) throws DkimException {
		String localPart = null == identity ? "" : identity.substring(0, identity.indexOf('@'));
		if (!dkey.getGranularity().matcher(localPart).matches()) {
			throw new DkimException("Incompatible identity (" + identity + ") for granularity g="
					+ dkey.getTagValue('g') + " ");
		}

		try {
			// prepare cipher and message
			Cipher cipher = Cipher.getInstance(RSA_MODE);
			byte[] originalMessage = new byte[dkey.getPublicKey().getModulus().bitLength() / Byte.SIZE];
			for (int i = 0, n = originalMessage.length; i < n; i++) {
				originalMessage[i] = (byte) i;
			}

			// encrypt original message
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptedMessage = cipher.doFinal(originalMessage);

			// decrypt encrypted message
			cipher.init(Cipher.DECRYPT_MODE, dkey.getPublicKey());
			byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

			if (!Arrays.equals(originalMessage, decryptedMessage)) {
				throw new DkimException("Incompatible private key for public key p=" + dkey.getTagValue('p') + " ");
			}

		} catch (NoSuchAlgorithmException e) {
			throw new DkimException("No JCE provider supports " + RSA_MODE + " ciphers.", e);
		} catch (NoSuchPaddingException e) {
			throw new DkimException("No JCE provider supports " + RSA_MODE + " ciphers.", e);
		} catch (InvalidKeyException e) {
			throw new DkimException("Performing RSA cryptography failed.", e);
		} catch (IllegalBlockSizeException e) {
			throw new DkimException("Performing RSA cryptography failed.", e);
		} catch (BadPaddingException e) {
			throw new DkimException("Performing RSA cryptography failed.", e);
		}
	}
}
