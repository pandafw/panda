package panda.net.mail.dkim;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import panda.codec.binary.Base64;
import panda.codec.binary.Hex;

/**
 * A {@code DomainKey} holds the information about a domain key.
 */
public final class DomainKey {

	private static final String DKIM_VERSION = "DKIM1";

	private static final String RSA_KEY_TYPE = "rsa";

	private static final String EMAIL_SERVICE_TYPE = "email";

	private final long timestamp;

	private final Pattern granularity;

	private final Set<String> serviceTypes;

	private final RSAPublicKey publicKey;

	private final Map<Character, String> tags;

	/**
	 * Creates a new {@code DomainKey} from the given tags.
	 * 
	 * @param tags
	 *            The tags to be used.
	 * @throws DkimException
	 *             If either the version, key type or service type given in the
	 *             tags is incompatible to this library ('DKIM1', 'RSA' and
	 *             'email' respectively).
	 */
	public DomainKey(Map<Character, String> tags) throws DkimException {
		timestamp = System.currentTimeMillis();
		this.tags = Collections.unmodifiableMap(tags);

		// version
		if (!(DKIM_VERSION.equals(getTagValue('v', DKIM_VERSION)))) {
			throw new DkimException("Incompatible version v=" + getTagValue('v') + ".");
		}

		// granularity
		granularity = getGranularityPattern(getTagValue('g', "*"));

		// key type
		if (!(RSA_KEY_TYPE.equals(getTagValue('k', RSA_KEY_TYPE)))) {
			throw new DkimException("Incompatible key type k=" + getTagValue('k') + ".");
		}

		// service type
		serviceTypes = getServiceTypes(getTagValue('s', "*"));
		if (!(serviceTypes.contains("*") || serviceTypes.contains(EMAIL_SERVICE_TYPE))) {
			throw new DkimException("Incompatible service type s=" + getTagValue('s') + ".");
		}

		String privateKeyTagValue = getTagValue('p');
		if (null == privateKeyTagValue) {
			throw new DkimException("No public key available.");
		}

		publicKey = getPublicKey(privateKeyTagValue);
	}

	private Set<String> getServiceTypes(String serviceTypesTagValue) {
		Set<String> serviceTypes = new HashSet<String>();
		StringTokenizer tokenizer = new StringTokenizer(serviceTypesTagValue, ":", false);
		while (tokenizer.hasMoreElements()) {
			serviceTypes.add(tokenizer.nextToken().trim());
		}
		return serviceTypes;
	}

	public String getTagValue(char tag) {
		return getTagValue(tag, null);
	}

	private String getTagValue(char tag, String fallback) {
		String tagValue = tags.get(tag);
		return null == tagValue ? fallback : tagValue;
	}

	private RSAPublicKey getPublicKey(String privateKeyTagValue) throws DkimException {
		try {
			System.out.println(privateKeyTagValue);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] bs = Base64.decodeBase64(privateKeyTagValue);
			System.out.println(Hex.encodeHexString(bs));
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bs);
			return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		}
		catch (NoSuchAlgorithmException nsae) {
			throw new DkimException("RSA algorithm not found by JVM");
		}
		catch (InvalidKeySpecException e) {
			throw new DkimException("The public key " + privateKeyTagValue + " couldn't be decoded.");
		}
	}

	private Pattern getGranularityPattern(String granularity) {
		StringTokenizer tokenizer = new StringTokenizer(granularity, "*", true);
		StringBuffer pattern = new StringBuffer();
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken();
			if ("*".equals(token)) {
				pattern.append(".*");
			} else {
				pattern.append(Pattern.quote(token));
			}
		}
		return Pattern.compile(pattern.toString());
	}

	/**
	 * Returns the construction time of this {@code DomainKey} as a timestamp.
	 * 
	 * @return The construction time of this {@code DomainKey} as a timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns a {@link Pattern} that matches the granularity of this
	 * {@code DomainKey}, as described in the 'g' tag.
	 * 
	 * @return A {@link Pattern} that matches the granularity of this
	 *         {@code DomainKey}.
	 */
	public Pattern getGranularity() {
		return granularity;
	}

	/**
	 * Returns the set of service types supported by this {@code DomainKey}, as
	 * described in the 's' tag.
	 * 
	 * @return The set of service types supported by this {@code DomainKey}.
	 */
	public Set<String> getServiceTypes() {
		return serviceTypes;
	}

	/**
	 * Returns the set of public key of this {@code DomainKey}, as provided by
	 * the 'p' tag.
	 * 
	 * @return The set of public key of this {@code DomainKey}.
	 */
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * Returns the {@link Collections#unmodifiableMap(Map) unmodifiable} map of
	 * tags, this {@code DomainKey} was constructed from.
	 * 
	 * @return The map of tags, this {@code DomainKey} was constructed from.
	 */
	public Map<Character, String> getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return "Entry [timestamp=" + timestamp + ", tags=" + tags + "]";
	}
}