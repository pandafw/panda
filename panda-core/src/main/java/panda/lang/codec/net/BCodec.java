package panda.lang.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import panda.lang.Charsets;
import panda.lang.codec.DecoderException;
import panda.lang.codec.EncoderException;
import panda.lang.codec.StringDecoder;
import panda.lang.codec.StringEncoder;
import panda.lang.codec.binary.Base64;

/**
 * Identical to the Base64 encoding defined by <a href="http://www.ietf.org/rfc/rfc1521.txt">RFC
 * 1521</a> and allows a character set to be specified.
 * <p>
 * <a href="http://www.ietf.org/rfc/rfc1522.txt">RFC 1522</a> describes techniques to allow the
 * encoding of non-ASCII text in various portions of a RFC 822 [2] message header, in a manner which
 * is unlikely to confuse existing message handling software.
 * <p>
 * This class is immutable and thread-safe.
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc1522.txt">MIME (Multipurpose Internet Mail Extensions)
 *      Part Two: Message Header Extensions for Non-ASCII Text</a>
 */
public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
	/**
	 * The default charset used for string decoding and encoding.
	 */
	private final Charset charset;

	/**
	 * Default constructor.
	 */
	public BCodec() {
		this(Charsets.UTF_8);
	}

	/**
	 * Constructor which allows for the selection of a default charset
	 * 
	 * @param charset the default string charset to use.
	 * @see <a
	 *      href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public BCodec(final Charset charset) {
		this.charset = charset;
	}

	/**
	 * Constructor which allows for the selection of a default charset
	 * 
	 * @param charsetName the default charset to use.
	 * @throws java.nio.charset.UnsupportedCharsetException If the named charset is unavailable
	 * @see <a
	 *      href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public BCodec(final String charsetName) {
		this(Charset.forName(charsetName));
	}

	@Override
	protected String getEncoding() {
		return "B";
	}

	@Override
	protected byte[] doEncoding(final byte[] bytes) {
		return encodeBytes(bytes);
	}

	@Override
	protected byte[] doDecoding(final byte[] bytes) {
		return decodeBytes(bytes);
	}

	/**
	 * Encodes a string into its Base64 form using the specified charset. Unsafe characters are
	 * escaped.
	 * 
	 * @param value string to convert to Base64 form
	 * @param charset the charset for <code>value</code>
	 * @return Base64 string
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	public String encode(final String value, final Charset charset) throws EncoderException {
		if (value == null) {
			return null;
		}
		return encodeText(value, charset);
	}

	/**
	 * Encodes a string into its Base64 form using the specified charset. Unsafe characters are
	 * escaped.
	 * 
	 * @param value string to convert to Base64 form
	 * @param charset the charset for <code>value</code>
	 * @return Base64 string
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	public String encode(final String value, final String charset) throws EncoderException {
		if (value == null) {
			return null;
		}
		try {
			return this.encodeText(value, charset);
		}
		catch (final UnsupportedEncodingException e) {
			throw new EncoderException(e.getMessage(), e);
		}
	}

	/**
	 * Encodes a string into its Base64 form using the default charset. Unsafe characters are
	 * escaped.
	 * 
	 * @param value string to convert to Base64 form
	 * @return Base64 string
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	@Override
	public String encode(final String value) throws EncoderException {
		if (value == null) {
			return null;
		}
		return encode(value, this.getCharset());
	}

	/**
	 * Decodes a Base64 string into its original form. Escaped characters are converted back to
	 * their original representation.
	 * 
	 * @param value Base64 string to convert into its original form
	 * @return original string
	 * @throws DecoderException A decoder exception is thrown if a failure condition is encountered
	 *             during the decode process.
	 */
	@Override
	public String decode(final String value) throws DecoderException {
		if (value == null) {
			return null;
		}
		try {
			return this.decodeText(value);
		}
		catch (final UnsupportedEncodingException e) {
			throw new DecoderException(e.getMessage(), e);
		}
	}

	/**
	 * Encodes an object into its Base64 form using the default charset. Unsafe characters are
	 * escaped.
	 * 
	 * @param value object to convert to Base64 form
	 * @return Base64 object
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	@Override
	public Object encode(final Object value) throws EncoderException {
		if (value == null) {
			return null;
		}
		else if (value instanceof String) {
			return encode((String)value);
		}
		else {
			throw new EncoderException("Objects of type " + value.getClass().getName()
					+ " cannot be encoded using BCodec");
		}
	}

	/**
	 * Decodes a Base64 object into its original form. Escaped characters are converted back to
	 * their original representation.
	 * 
	 * @param value Base64 object to convert into its original form
	 * @return original object
	 * @throws DecoderException Thrown if the argument is not a <code>String</code>. Thrown if a
	 *             failure condition is encountered during the decode process.
	 */
	@Override
	public Object decode(final Object value) throws DecoderException {
		if (value == null) {
			return null;
		}
		else if (value instanceof String) {
			return decode((String)value);
		}
		else {
			throw new DecoderException("Objects of type " + value.getClass().getName()
					+ " cannot be decoded using BCodec");
		}
	}

	/**
	 * Gets the default charset name used for string decoding and encoding.
	 * 
	 * @return the default charset name
	 */
	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * Gets the default charset name used for string decoding and encoding.
	 * 
	 * @return the default charset name
	 */
	public String getDefaultCharset() {
		return this.charset.name();
	}

	//-------------------------------------------------------------
	/**
	 * Returns the length of the encoded version of this byte array.
	 */
	public static int encodedLength(byte[] b) {
		return Base64.encodedLength(b);
	}

	/**
	 * Encodes an array of bytes using the defined encoding scheme.
	 * 
	 * @param bytes Data to be encoded
	 * @return A byte array containing the encoded data
	 */
	public static byte[] encodeBytes(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return Base64.encodeBase64(bytes);
	}

	/**
	 * Decodes an array of bytes using the defined encoding scheme.
	 * 
	 * @param bytes Data to be decoded
	 * @return a byte array that contains decoded data
	 * @throws DecoderException A decoder exception is thrown if a Decoder encounters a failure
	 *             condition during the decode process.
	 */
	public static byte[] decodeBytes(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return Base64.decodeBase64(bytes);
	}
}
