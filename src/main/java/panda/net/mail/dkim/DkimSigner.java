package panda.net.mail.dkim;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;
import panda.lang.crypto.KeyPairs;
import panda.net.Mimes;
import panda.net.smtp.SMTPHeader;


/**
 * Main class providing a signature according to DKIM RFC 4871.
 */
public class DkimSigner {
	private static final String[] MIMIMUM_HEADERS_TO_SIGN = new String[] {
		SMTPHeader.FROM,
		SMTPHeader.TO,
		SMTPHeader.SUBJECT
	};

	private static final String[] DEFAULT_HEADERS_TO_SIGN = new String[] {
		SMTPHeader.FROM,
		SMTPHeader.TO,
		SMTPHeader.SUBJECT,
		SMTPHeader.CONTENT_DISPOSITION,
		SMTPHeader.CONTENT_ID,
		SMTPHeader.CONTENT_TYPE,
		SMTPHeader.CONTENT_TRANSFER_ENCODING,
		SMTPHeader.CC,
		SMTPHeader.DATE,
		SMTPHeader.REPLY_TO,
		SMTPHeader.IN_REPLY_TO,
		SMTPHeader.SENDER,
		SMTPHeader.LIST_SUBSCRIBE,
		SMTPHeader.LIST_POST,
		SMTPHeader.LIST_OWNER,
		SMTPHeader.LIST_ID,
		SMTPHeader.LIST_ARCHIVE,
		SMTPHeader.LIST_HELP,
		SMTPHeader.LIST_UNSUBSCRIBE,
		SMTPHeader.MIME_VERSION,
		SMTPHeader.MESSAGE_ID,
		SMTPHeader.RESENT_FROM,
		SMTPHeader.RESENT_TO,
		SMTPHeader.RESENT_CC,
		SMTPHeader.RESENT_DATE,
		SMTPHeader.RESENT_MESSAGE_ID,
		SMTPHeader.RESENT_SENDER,
	};

	private static final Set<String> minHeadersToSign = Collections.caseInsensitiveSet(Arrays.asList(MIMIMUM_HEADERS_TO_SIGN));
	private final Set<String> headersToSign = Collections.caseInsensitiveSet(Arrays.asList(DEFAULT_HEADERS_TO_SIGN));

	private SigningAlgorithm signingAlgorithm = SigningAlgorithm.SHA256_WITH_RSA;
	private Signature signature;
	private MessageDigest messageDigest;
	private String domain;
	private String selector;
	private String identity;
	private int bodyLength;
	private boolean zParam;
	private Canonicalization headerCanonicalization = Canonicalization.RELAXED;
	private Canonicalization bodyCanonicalization = Canonicalization.SIMPLE;
	private PrivateKey privateKey;

	/**
	 * Created a new {@code DkimSigner} for the given signing domain and
	 * selector with the given {@link PrivateKey}.
	 * 
	 * @param domain
	 *            The signing domain to be used.
	 * @param selector
	 *            The selector to be used.
	 * @param privateKey
	 *            The {@link PrivateKey} to be used to sign.
	 * @throws DkimException
	 *             If the given signing domain is invalid.
	 */
	public DkimSigner(String domain, String selector, PrivateKey privateKey) throws DkimException {
		initDkimSigner(domain, selector, privateKey);
	}

	/**
	 * Created a new {@code DkimSigner} for the given signing domain and
	 * selector with the given DER encoded RSA private Key.
	 * 
	 * @param domain
	 *            The signing domain to be used.
	 * @param selector
	 *            The selector to be used.
	 * @param key
	 *            A byte array that yields the DER encoded RSA private
	 *            key to be used. 
	 * 
	 * @throws NoSuchAlgorithmException
	 *             If the RSA algorithm is not supported by the current JVM.
	 * @throws InvalidKeySpecException
	 *             If the content of the given {@link InputStream} couldn't be
	 *             interpreted as an RSA private key.
	 * @throws DkimException
	 *             If the given signing domain is invalid.
	 */
	public DkimSigner(String domain, String selector, byte[] key) throws 
			NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey privKey = KeyPairs.getRSAPrivateKey(key);
		initDkimSigner(domain, selector, privKey);
	}

	/**
	 * Created a new {@code DkimSigner} for the given signing domain and
	 * selector with the given DER encoded RSA private Key.
	 * 
	 * @param domain
	 *            The signing domain to be used.
	 * @param selector
	 *            The selector to be used.
	 * @param key
	 *            A string that yields the PEM format DER encoded RSA private
	 *            key to be used. 
	 * 
	 * @throws NoSuchAlgorithmException
	 *             If the RSA algorithm is not supported by the current JVM.
	 * @throws InvalidKeySpecException
	 *             If the content of the given {@link InputStream} couldn't be
	 *             interpreted as an RSA private key.
	 * @throws DkimException
	 *             If the given signing domain is invalid.
	 */
	public DkimSigner(String domain, String selector, String key) throws 
			NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey privKey = KeyPairs.getRSAPrivateKey(key);
		initDkimSigner(domain, selector, privKey);
	}

	private void initDkimSigner(String domain, String selector, PrivateKey privkey) throws DkimException {
		this.domain = domain;
		this.selector = selector.trim();
		this.privateKey = privkey;
		this.setSigningAlgorithm(this.signingAlgorithm);
	}

	/**
	 * Returns the configured identity parameter.
	 * 
	 * @return The configured identity parameter.
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Sets the identity parameter to be used.
	 * 
	 * @param identity
	 *            The identity to be used.
	 * @throws DkimException
	 *             If the given identity parameter doesn't belong to the signing
	 *             domain of this {@code DkimSigner} or an subdomain thereof.
	 */
	public void setIdentity(String identity) throws DkimException {
		if (null != identity) {
			identity = identity.trim();
			if (!(identity.endsWith("@" + domain) || identity.endsWith("." + domain))) {
				throw new DkimException("The domain part of " + identity + " has to be " + domain
						+ " or a subdomain thereof");
			}
		}
		this.identity = identity;
	}

	/**
	 * Returns the configured {@link Canonicalization} to be used for the body.
	 * 
	 * @return The configured {@link Canonicalization} to be used for the body.
	 */
	public Canonicalization getBodyCanonicalization() {
		return bodyCanonicalization;
	}

	/**
	 * Sets the {@link Canonicalization} to be used for the body.
	 * 
	 * @param canonicalization
	 *            The {@link Canonicalization} to be used for the body.
	 */
	public void setBodyCanonicalization(Canonicalization canonicalization) {
		this.bodyCanonicalization = canonicalization;
	}

	/**
	 * Returns the configured {@link Canonicalization} to be used for the
	 * headers.
	 * 
	 * @return The configured {@link Canonicalization} to be used for the
	 *         headers.
	 */
	public Canonicalization getHeaderCanonicalization() {
		return headerCanonicalization;
	}

	/**
	 * Sets the {@link Canonicalization} to be used for the headers.
	 * 
	 * @param canonicalization
	 *            The {@link Canonicalization} to be used for the headers.
	 */
	public void setHeaderCanonicalization(Canonicalization canonicalization) {
		this.headerCanonicalization = canonicalization;
	}

	/**
	 * Adds a header to the set of headers that will be included in the
	 * signature, if present.
	 * 
	 * @param header
	 *            The name of the header.
	 */
	public void addHeaderToSign(String header) {
		if (null != header && 0 != header.length()) {
			headersToSign.add(header);
		}
	}

	/**
	 * Removes a header from the set of headers that will be included in the
	 * signature, unless it is one of the required headers ('From', 'To',
	 * 'Subject').
	 * 
	 * @param header
	 *            The name of the header.
	 */
	public void removeHeaderToSign(String header) {
		if (Strings.isNotEmpty(header) && !minHeadersToSign.contains(header)) {
			headersToSign.remove(header);
		}
	}

	/**
	 * Returns the configured length parameter.
	 * 
	 * @return The configured length parameter.
	 */
	public int getBodyLength() {
		return bodyLength;
	}

	/**
	 * Sets the length parameter to be used.
	 * 
	 * @param bodyLength
	 *            The length parameter to be used.
	 */
	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}

	/**
	 * Returns the configured z parameter.
	 * 
	 * @return The configured z parameter.
	 */
	public boolean isZParam() {
		return zParam;
	}

	/**
	 * Sets the z parameter to be used.
	 * 
	 * @param zParam
	 *            The z parameter to be used.
	 */
	public void setZParam(boolean zParam) {
		this.zParam = zParam;
	}

	/**
	 * Returns the configured {@link SigningAlgorithm}.
	 * 
	 * @return The configured {@link SigningAlgorithm}.
	 */
	public SigningAlgorithm getSigningAlgorithm() {
		return signingAlgorithm;
	}

	/**
	 * Sets the {@link SigningAlgorithm} to be used.
	 * 
	 * @param signingAlgorithm
	 *            The {@link SigningAlgorithm} to be used.
	 * 
	 * @throws DkimException
	 *             If either the signing algorithm or the hashing algorithm is
	 *             not supported by the current JVM or the {@link Signature}
	 *             couldn't be initialized.
	 */
	public void setSigningAlgorithm(SigningAlgorithm signingAlgorithm) throws DkimException {

		try {
			messageDigest = MessageDigest.getInstance(signingAlgorithm.getHashNotation());
		}
		catch (NoSuchAlgorithmException e) {
			throw new DkimException("The hashing algorithm " + signingAlgorithm.getHashNotation()
					+ " is not known by the JVM", e);
		}

		try {
			signature = Signature.getInstance(signingAlgorithm.getJavaNotation());
		}
		catch (NoSuchAlgorithmException e) {
			throw new DkimException("The signing algorithm " + signingAlgorithm.getJavaNotation()
					+ " is not known by the JVM", e);
		}

		try {
			signature.initSign(privateKey);
		}
		catch (InvalidKeyException e) {
			throw new DkimException("The provided private key is invalid", e);
		}

		this.signingAlgorithm = signingAlgorithm;
	}

	public String sign(SMTPHeader header, String body) throws DkimException {
		Map<String, String> dkimSignature = new LinkedHashMap<String, String>();
		dkimSignature.put("v", "1");
		dkimSignature.put("a", this.signingAlgorithm.getRfc4871Notation());
		dkimSignature.put("q", "dns/txt");
		dkimSignature.put("c", getHeaderCanonicalization().getType() + "/" + getBodyCanonicalization().getType());
		dkimSignature.put("t", ((long) new Date().getTime() / 1000) + "");
		dkimSignature.put("s", this.selector);
		dkimSignature.put("d", this.domain);

		// set identity inside signature
		if (identity != null) {
			dkimSignature.put("i", quotedPrintable(identity));
		}

		// process header
		Set<String> assureHeaders = Collections.caseInsensitiveSet(Arrays.asList(MIMIMUM_HEADERS_TO_SIGN));

		// intersect defaultHeadersToSign with available headers
		StringBuilder headerList = new StringBuilder();
		StringBuilder headerContent = new StringBuilder();
		StringBuilder zParamString = new StringBuilder();

		for (Map.Entry<String, Object> en : header.entrySet()) {
			String key = en.getKey();
			if (headersToSign.contains(key)) {
				String headerValue = header.getValue(key);
				headerList.append(key).append(":");
				headerContent.append(headerCanonicalization.canonicalizeHeader(key, headerValue));
				headerContent.append("\r\n");
				assureHeaders.remove(key);
				if (zParam) {
					zParamString.append(key);
					zParamString.append(":");
					zParamString.append(quotedPrintable(headerValue.trim()).replace("|", "=7C"));
					zParamString.append("|");
				}
			}
		}

		if (!assureHeaders.isEmpty()) {
			throw new DkimException("Could not find the header fields " + concatList(assureHeaders, ", ")
					+ " for signing");
		}

		dkimSignature.put("h", headerList.substring(0, headerList.length() - 1));
		if (zParam) {
			String zParamTemp = zParamString.toString();
			dkimSignature.put("z", zParamTemp.substring(0, zParamTemp.length() - 1));
		}

		// process body
		if (bodyLength > 0 && bodyLength <= body.length()) {
			dkimSignature.put("l", Integer.toString(bodyLength));
			body = bodyCanonicalization.canonicalizeBody(body.substring(0, bodyLength));
		}
		else {
			body = bodyCanonicalization.canonicalizeBody(body);
		}

		// calculate and encode body hash
		dkimSignature.put("bh", Base64.encodeBase64String(messageDigest.digest(body.getBytes())));

		// create signature
		String serializedSignature = serializeDkimSignature(dkimSignature);

		byte[] signedSignature;
		try {
			headerContent.append(headerCanonicalization.canonicalizeHeader(SMTPHeader.DKIM_SIGNATUR, serializedSignature));
			signature.update(headerContent.toString().getBytes());
			signedSignature = signature.sign();
		}
		catch (SignatureException se) {
			throw new DkimException("The signing operation by Java security failed", se);
		}

		return SMTPHeader.DKIM_SIGNATUR + ": " + serializedSignature + Base64.encodeBase64String(signedSignature);
	}

	private String serializeDkimSignature(Map<String, String> dkimSignature) {
		StringBuilder sb = new StringBuilder();

		for (Entry<String, String> en : dkimSignature.entrySet()) {
			sb.append(en.getKey())
				.append('=')
				.append(en.getValue())
				.append("; ");
		}

		sb.append("b=");

		return sb.toString();
	}

	private static String concatList(Set<String> assureHeaders, String separator) {
		StringBuilder buffer = new StringBuilder();
		for (String string : assureHeaders) {
			buffer.append(string);
			buffer.append(separator);
		}
		return buffer.substring(0, buffer.length() - separator.length());
	}

	private static String quotedPrintable(String s) {
		try {
			String encoded = Mimes.encodeWord(s);
			encoded = encoded.replaceAll(";", "=3B");
			encoded = encoded.replaceAll(" ", "=20");

			return encoded;
		} 
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
