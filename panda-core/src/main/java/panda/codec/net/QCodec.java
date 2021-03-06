package panda.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;

import panda.codec.DecoderException;
import panda.codec.EncoderException;
import panda.codec.StringDecoder;
import panda.codec.StringEncoder;
import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Charsets;

/**
 * Similar to the Quoted-Printable content-transfer-encoding defined in <a
 * href="http://www.ietf.org/rfc/rfc1521.txt">RFC 1521</a> and designed to allow text containing
 * mostly ASCII characters to be decipherable on an ASCII terminal without decoding.
 * <p>
 * <a href="http://www.ietf.org/rfc/rfc1522.txt">RFC 1522</a> describes techniques to allow the
 * encoding of non-ASCII text in various portions of a RFC 822 [2] message header, in a manner which
 * is unlikely to confuse existing message handling software.
 * <p>
 * This class is conditionally thread-safe. The instance field {@link #encodeBlanks} is mutable
 * {@link #setEncodeBlanks(boolean)} but is not volatile, and accesses are not synchronised. If an
 * instance of the class is shared between threads, the caller needs to ensure that suitable
 * synchronisation is used to ensure safe publication of the value between threads, and must not
 * invoke {@link #setEncodeBlanks(boolean)} after initial setup.
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc1522.txt">MIME (Multipurpose Internet Mail Extensions)
 *      Part Two: Message Header Extensions for Non-ASCII Text</a>
 */
public class QCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
	private static String WORD_SPECIALS = "=_?\"#$%&'(),.:;<>@[\\]^`{|}~";
	private static String TEXT_SPECIALS = "=_?";

	/**
	 * The default charset used for string decoding and encoding.
	 */
	private final Charset charset;

	/**
	 * BitSet of printable characters as defined in RFC 1522.
	 */
	private static final BitSet PRINTABLE_CHARS = new BitSet(256);
	// Static initializer for printable chars collection
	static {
		// alpha characters
		PRINTABLE_CHARS.set(' ');
		PRINTABLE_CHARS.set('!');
		PRINTABLE_CHARS.set('"');
		PRINTABLE_CHARS.set('#');
		PRINTABLE_CHARS.set('$');
		PRINTABLE_CHARS.set('%');
		PRINTABLE_CHARS.set('&');
		PRINTABLE_CHARS.set('\'');
		PRINTABLE_CHARS.set('(');
		PRINTABLE_CHARS.set(')');
		PRINTABLE_CHARS.set('*');
		PRINTABLE_CHARS.set('+');
		PRINTABLE_CHARS.set(',');
		PRINTABLE_CHARS.set('-');
		PRINTABLE_CHARS.set('.');
		PRINTABLE_CHARS.set('/');
		for (int i = '0'; i <= '9'; i++) {
			PRINTABLE_CHARS.set(i);
		}
		PRINTABLE_CHARS.set(':');
		PRINTABLE_CHARS.set(';');
		PRINTABLE_CHARS.set('<');
		PRINTABLE_CHARS.set('>');
		PRINTABLE_CHARS.set('@');
		for (int i = 'A'; i <= 'Z'; i++) {
			PRINTABLE_CHARS.set(i);
		}
		PRINTABLE_CHARS.set('[');
		PRINTABLE_CHARS.set('\\');
		PRINTABLE_CHARS.set(']');
		PRINTABLE_CHARS.set('^');
		PRINTABLE_CHARS.set('`');
		for (int i = 'a'; i <= 'z'; i++) {
			PRINTABLE_CHARS.set(i);
		}
		PRINTABLE_CHARS.set('{');
		PRINTABLE_CHARS.set('|');
		PRINTABLE_CHARS.set('}');
		PRINTABLE_CHARS.set('~');
	}

	private static final byte BLANK = ' ';

	private static final byte UNDERSCORE = '_';

	private static final byte ESCAPE_CHAR = '=';

	private static final byte CR = 13;

	private static final byte LF = 10;

	private boolean encodeBlanks = false;

	/**
	 * Default constructor.
	 */
	public QCodec() {
		this(Charsets.UTF_8);
	}

	/**
	 * Constructor which allows for the selection of a default charset.
	 * 
	 * @param charset the default string charset to use.
	 * @see <a
	 *      href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public QCodec(final Charset charset) {
		super();
		this.charset = charset;
	}

	/**
	 * Constructor which allows for the selection of a default charset.
	 * 
	 * @param charsetName the charset to use.
	 * @throws java.nio.charset.UnsupportedCharsetException If the named charset is unavailable
	 * @see <a
	 *      href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public QCodec(final String charsetName) {
		this(Charset.forName(charsetName));
	}

	@Override
	protected String getEncoding() {
		return "Q";
	}

	@Override
	protected byte[] doEncoding(final byte[] bytes) {
		return encodeBytes(bytes, encodeBlanks);
	}

	@Override
	protected byte[] doDecoding(final byte[] bytes) throws DecoderException {
		return decodeQuotedPrintable(bytes);
	}

	/**
	 * Encodes a string into its quoted-printable form using the specified charset. Unsafe
	 * characters are escaped.
	 * 
	 * @param str string to convert to quoted-printable form
	 * @param charset the charset for str
	 * @return quoted-printable string
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	public String encode(final String str, final Charset charset) throws EncoderException {
		if (str == null) {
			return null;
		}
		return encodeText(str, charset);
	}

	/**
	 * Encodes a string into its quoted-printable form using the specified charset. Unsafe
	 * characters are escaped.
	 * 
	 * @param str string to convert to quoted-printable form
	 * @param charset the charset for str
	 * @return quoted-printable string
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	public String encode(final String str, final String charset) throws EncoderException {
		if (str == null) {
			return null;
		}
		try {
			return encodeText(str, charset);
		}
		catch (final UnsupportedEncodingException e) {
			throw new EncoderException(e.getMessage(), e);
		}
	}

	/**
	 * Encodes a string into its quoted-printable form using the default charset. Unsafe characters
	 * are escaped.
	 * 
	 * @param str string to convert to quoted-printable form
	 * @return quoted-printable string
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	@Override
	public String encode(final String str) throws EncoderException {
		if (str == null) {
			return null;
		}
		return encode(str, getCharset());
	}

	/**
	 * Decodes a quoted-printable string into its original form. Escaped characters are converted
	 * back to their original representation.
	 * 
	 * @param str quoted-printable string to convert into its original form
	 * @return original string
	 * @throws DecoderException A decoder exception is thrown if a failure condition is encountered
	 *             during the decode process.
	 */
	@Override
	public String decode(final String str) throws DecoderException {
		if (str == null) {
			return null;
		}
		try {
			return decodeText(str);
		}
		catch (final UnsupportedEncodingException e) {
			throw new DecoderException(e.getMessage(), e);
		}
	}

	/**
	 * Encodes an object into its quoted-printable form using the default charset. Unsafe characters
	 * are escaped.
	 * 
	 * @param obj object to convert to quoted-printable form
	 * @return quoted-printable object
	 * @throws EncoderException thrown if a failure condition is encountered during the encoding
	 *             process.
	 */
	@Override
	public Object encode(final Object obj) throws EncoderException {
		if (obj == null) {
			return null;
		}
		else if (obj instanceof String) {
			return encode((String)obj);
		}
		else {
			throw new EncoderException("Objects of type " + obj.getClass().getName()
					+ " cannot be encoded using Q codec");
		}
	}

	/**
	 * Decodes a quoted-printable object into its original form. Escaped characters are converted
	 * back to their original representation.
	 * 
	 * @param obj quoted-printable object to convert into its original form
	 * @return original object
	 * @throws DecoderException Thrown if the argument is not a <code>String</code>. Thrown if a
	 *             failure condition is encountered during the decode process.
	 */
	@Override
	public Object decode(final Object obj) throws DecoderException {
		if (obj == null) {
			return null;
		}
		else if (obj instanceof String) {
			return decode((String)obj);
		}
		else {
			throw new DecoderException("Objects of type " + obj.getClass().getName()
					+ " cannot be decoded using Q codec");
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

	/**
	 * Tests if optional transformation of SPACE characters is to be used
	 * 
	 * @return <code>true</code> if SPACE characters are to be transformed, <code>false</code>
	 *         otherwise
	 */
	public boolean isEncodeBlanks() {
		return this.encodeBlanks;
	}

	/**
	 * Defines whether optional transformation of SPACE characters is to be used
	 * 
	 * @param b <code>true</code> if SPACE characters are to be transformed, <code>false</code>
	 *            otherwise
	 */
	public void setEncodeBlanks(final boolean b) {
		this.encodeBlanks = b;
	}

	//-------------------------------------------------------------
	/**
	 * @param b the bytes
	 * @param encodingWord weather to encode word
	 * @return the length of the encoded version of this byte array.
	 */
	public static int encodedLength(byte[] b, boolean encodingWord) {
		int len = 0;
		String specials = encodingWord ? WORD_SPECIALS : TEXT_SPECIALS;
		for (int i = 0; i < b.length; i++) {
			int c = b[i] & 0xff; // Mask off MSB
			if (c < 040 || c >= 0177 || specials.indexOf(c) >= 0)
				// needs encoding
				len += 3; // Q-encoding is 1 -> 3 conversion
			else
				len++;
		}
		return len;
	}

	/**
	 * Decodes an array quoted-printable characters into an array of original bytes. Escaped
	 * characters are converted back to their original representation.
	 * <p>
	 * This function fully implements the quoted-printable encoding specification (rule #1 through
	 * rule #5) as defined in RFC 1521.
	 * 
	 * @param bytes array of quoted-printable characters
	 * @return array of original bytes
	 * @throws DecoderException Thrown if quoted-printable decoding is unsuccessful
	 */
	public static final byte[] decodeQuotedPrintable(final byte[] bytes) throws DecoderException {
		if (bytes == null) {
			return null;
		}
		
		@SuppressWarnings("resource")
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int len = bytes.length;
		for (int i = 0; i < len; i++) {
			final int b = bytes[i];
			if (b == UNDERSCORE) {
				buffer.write(BLANK);
			}
			else if (b == ESCAPE_CHAR) {
				if (i + 2 >= len) {
					throw new DecoderException("Invalid quoted printable encoding; truncated escape sequence");
				}

				byte b1 = bytes[++i];
				byte b2 = bytes[++i];

				// we've found an encoded carriage return. The next char needs to be a newline
				if (b1 == CR) {
					if (b2 != LF) {
						throw new DecoderException("Invalid quoted printable encoding; CR must be followed by LF");
					}
					// this was a soft linebreak inserted by the encoding. We just toss this away on decode.
					continue;
				}

				final int u = Utils.digit16(b1);
				final int l = Utils.digit16(b2);
				buffer.write((char)((u << 4) + l));
			}
			else {
				buffer.write(b);
			}
		}
		return buffer.toByteArray();
	}

	/**
	 * Encodes an array of bytes using the defined encoding scheme.
	 * 
	 * @param bytes Data to be encoded
	 * @param encodeBlanks weather to encode blanks
	 * @return A byte array containing the encoded data
	 */
	public static byte[] encodeBytes(byte[] bytes, boolean encodeBlanks) {
		if (bytes == null) {
			return null;
		}
		final byte[] data = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
		if (encodeBlanks) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] == BLANK) {
					data[i] = UNDERSCORE;
				}
			}
		}
		return data;
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
		return decodeQuotedPrintable(bytes);
	}
}
