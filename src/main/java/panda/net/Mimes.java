package panda.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import panda.lang.Charsets;
import panda.lang.Strings;
import panda.lang.codec.DecoderException;
import panda.lang.codec.EncoderException;
import panda.lang.codec.binary.Base64;
import panda.lang.codec.net.BCodec;
import panda.lang.codec.net.QCodec;

/**
 * Utility class to decode MIME texts.
 */
public final class Mimes {

	/**
	 * The {@code US-ASCII} charset identifier constant.
	 */
	private static final String US_ASCII_CHARSET = "US-ASCII";

	/**
	 * The marker to indicate text is encoded with BASE64 algorithm.
	 */
	private static final String BASE64_ENCODING_MARKER = "B";

	/**
	 * The marker to indicate text is encoded with QuotedPrintable algorithm.
	 */
	private static final String QUOTEDPRINTABLE_ENCODING_MARKER = "Q";

	/**
	 * If the text contains any encoded tokens, those tokens will be marked with "=?".
	 */
	private static final String ENCODED_TOKEN_MARKER = "=?";

	/**
	 * If the text contains any encoded tokens, those tokens will terminate with "=?".
	 */
	private static final String ENCODED_TOKEN_FINISHER = "?=";

	/**
	 * The linear whitespace chars sequence.
	 */
	private static final String LINEAR_WHITESPACE = " \t\r\n";

	/**
	 * Hidden constructor, this class must not be instantiated.
	 */
	private Mimes() {
		// do nothing
	}

	//------------------------------------------------------------
	/**
	 * Mappings between MIME and Java charset.
	 */
	private static final Map<String, String> JAVA2MIME = new HashMap<String, String>(40);

	/**
	 * Mappings between MIME and Java charset.
	 */
	private static final Map<String, String> MIME2JAVA = new HashMap<String, String>(10);

	static {
		JAVA2MIME.put("8859_1", "ISO-8859-1");
		JAVA2MIME.put("iso8859_1", "ISO-8859-1");
		JAVA2MIME.put("iso8859-1", "ISO-8859-1");

		JAVA2MIME.put("8859_2", "ISO-8859-2");
		JAVA2MIME.put("iso8859_2", "ISO-8859-2");
		JAVA2MIME.put("iso8859-2", "ISO-8859-2");

		JAVA2MIME.put("8859_3", "ISO-8859-3");
		JAVA2MIME.put("iso8859_3", "ISO-8859-3");
		JAVA2MIME.put("iso8859-3", "ISO-8859-3");

		JAVA2MIME.put("8859_4", "ISO-8859-4");
		JAVA2MIME.put("iso8859_4", "ISO-8859-4");
		JAVA2MIME.put("iso8859-4", "ISO-8859-4");

		JAVA2MIME.put("8859_5", "ISO-8859-5");
		JAVA2MIME.put("iso8859_5", "ISO-8859-5");
		JAVA2MIME.put("iso8859-5", "ISO-8859-5");

		JAVA2MIME.put("8859_6", "ISO-8859-6");
		JAVA2MIME.put("iso8859_6", "ISO-8859-6");
		JAVA2MIME.put("iso8859-6", "ISO-8859-6");

		JAVA2MIME.put("8859_7", "ISO-8859-7");
		JAVA2MIME.put("iso8859_7", "ISO-8859-7");
		JAVA2MIME.put("iso8859-7", "ISO-8859-7");

		JAVA2MIME.put("8859_8", "ISO-8859-8");
		JAVA2MIME.put("iso8859_8", "ISO-8859-8");
		JAVA2MIME.put("iso8859-8", "ISO-8859-8");

		JAVA2MIME.put("8859_9", "ISO-8859-9");
		JAVA2MIME.put("iso8859_9", "ISO-8859-9");
		JAVA2MIME.put("iso8859-9", "ISO-8859-9");

		JAVA2MIME.put("sjis", "Shift_JIS");
		JAVA2MIME.put("jis", "ISO-2022-JP");
		JAVA2MIME.put("iso2022jp", "ISO-2022-JP");
		JAVA2MIME.put("euc_jp", "euc-jp");
		JAVA2MIME.put("koi8_r", "koi8-r");
		JAVA2MIME.put("euc_cn", "euc-cn");
		JAVA2MIME.put("euc_tw", "euc-tw");
		JAVA2MIME.put("euc_kr", "euc-kr");

		MIME2JAVA.put("iso-2022-cn", "ISO2022CN");
		MIME2JAVA.put("iso-2022-kr", "ISO2022KR");
		MIME2JAVA.put("utf-8", "UTF8");
		MIME2JAVA.put("utf8", "UTF8");
		MIME2JAVA.put("ja_jp.iso2022-7", "ISO2022JP");
		MIME2JAVA.put("ja_jp.eucjp", "EUCJIS");
		MIME2JAVA.put("euc-kr", "KSC5601");
		MIME2JAVA.put("euckr", "KSC5601");
		MIME2JAVA.put("us-ascii", "ISO-8859-1");
		MIME2JAVA.put("x-us-ascii", "ISO-8859-1");
	}

	/**
	 * Translate a MIME standard character set name into the Java equivalent.
	 * 
	 * @param charset The MIME standard name.
	 * @return The Java equivalent for this name.
	 */
	private static String javaCharset(String charset) {
		// nothing in, nothing out.
		if (charset == null) {
			return null;
		}

		String mappedCharset = MIME2JAVA.get(charset.toLowerCase(Locale.ENGLISH));
		// if there is no mapping, then the original name is used. Many of the MIME character set
		// names map directly back into Java. The reverse isn't necessarily true.
		if (mappedCharset == null) {
			return charset;
		}
		return mappedCharset;
	}

	/**
	 * Convert a java charset into its MIME charset name.
	 * <p>
	 * Note that a future version of JDK (post 1.2) might provide this functionality, in which case,
	 * we may deprecate this method then.
	 * 
	 * @param charset the JDK charset
	 * @return the MIME/IANA equivalent. If a mapping is not possible, the passed in charset itself
	 *         is returned.
	 */
	public static String mimeCharset(String charset) {
		// nothing in, nothing out.
		if (charset == null) {
			return charset;
		}

		String alias = JAVA2MIME.get(charset.toLowerCase(Locale.ENGLISH));
		return alias == null ? charset : alias;
	}

	//------------------------------------------------------------
	private static final int ALL_ASCII = 1;
	private static final int MOSTLY_ASCII = 2;
	private static final int MOSTLY_NONASCII = 3;

	/**
	 * Check if the given string contains non US-ASCII characters.
	 * 
	 * @param s string
	 * @return ALL_ASCII if all characters in the string belong to the US-ASCII charset.
	 *         MOSTLY_ASCII if more than half of the available characters are US-ASCII characters.
	 *         Else MOSTLY_NONASCII.
	 */
	private static int checkAscii(String s) {
		int ascii = 0, non_ascii = 0;
		int l = s.length();

		for (int i = 0; i < l; i++) {
			if (nonascii((int)s.charAt(i))) // non-ascii
				non_ascii++;
			else
				ascii++;
		}

		if (non_ascii == 0)
			return ALL_ASCII;
		if (ascii > non_ascii)
			return MOSTLY_ASCII;

		return MOSTLY_NONASCII;
	}

	private static final boolean nonascii(int b) {
		return b >= 0177 || (b < 040 && b != '\r' && b != '\n' && b != '\t');
	}

	public static String detectEncoding(String string) {
		int ascii = checkAscii(string);
		if (ascii == ALL_ASCII) {
			return "";
		}

		if (ascii != MOSTLY_NONASCII) {
			return "Quoted-Printable";
		}
		return "Base64";
	}
	
	//------------------------------------------------------------
	/**
	 * Decode a string of text obtained from a mail header into its proper form. The text generally
	 * will consist of a string of tokens, some of which may be encoded using base64 encoding.
	 * 
	 * @param text The text to decode.
	 * @return The decoded text string.
	 * @throws UnsupportedEncodingException if the detected encoding in the input text is not
	 *             supported.
	 */
	public static String decodeText(String text) throws UnsupportedEncodingException {
		// if the text contains any encoded tokens, those tokens will be marked with "=?". If the
		// source string doesn't contain that sequent, no decoding is required.
		if (text.indexOf(ENCODED_TOKEN_MARKER) < 0) {
			return text;
		}

		int offset = 0;
		int endOffset = text.length();

		int startWhiteSpace = -1;
		int endWhiteSpace = -1;

		StringBuilder decodedText = new StringBuilder(text.length());

		boolean previousTokenEncoded = false;

		while (offset < endOffset) {
			char ch = text.charAt(offset);

			// is this a whitespace character?
			if (LINEAR_WHITESPACE.indexOf(ch) != -1) { // whitespace found
				startWhiteSpace = offset;
				while (offset < endOffset) {
					// step over the white space characters.
					ch = text.charAt(offset);
					if (LINEAR_WHITESPACE.indexOf(ch) != -1) { // whitespace found
						offset++;
					}
					else {
						// record the location of the first non lwsp and drop down to process the
						// token characters.
						endWhiteSpace = offset;
						break;
					}
				}
			}
			else {
				// we have a word token. We need to scan over the word and then try to parse it.
				int wordStart = offset;

				while (offset < endOffset) {
					// step over the non white space characters.
					ch = text.charAt(offset);
					if (LINEAR_WHITESPACE.indexOf(ch) == -1) { // not white space
						offset++;
					}
					else {
						break;
					}

					// NB: Trailing whitespace on these header strings will just be discarded.
				}
				// pull out the word token.
				String word = text.substring(wordStart, offset);
				// is the token encoded? decode the word
				if (word.startsWith(ENCODED_TOKEN_MARKER)) {
					try {
						// if this gives a parsing failure, treat it like a non-encoded word.
						String decodedWord = decodeWord(word);

						// are any whitespace characters significant? Append 'em if we've got 'em.
						if (!previousTokenEncoded && startWhiteSpace != -1) {
							decodedText.append(text.substring(startWhiteSpace, endWhiteSpace));
							startWhiteSpace = -1;
						}
						// this is definitely a decoded token.
						previousTokenEncoded = true;
						// and add this to the text.
						decodedText.append(decodedWord);
						// we continue parsing from here...we allow parsing errors to fall through
						// and get handled as normal text.
						continue;

					}
					catch (ParseException e) {
						// just ignore it, skip to next word
					}
				}
				// this is a normal token, so it doesn't matter what the previous token was. Add the
				// white space
				// if we have it.
				if (startWhiteSpace != -1) {
					decodedText.append(text.substring(startWhiteSpace, endWhiteSpace));
					startWhiteSpace = -1;
				}
				// this is not a decoded token.
				previousTokenEncoded = false;
				decodedText.append(word);
			}
		}

		return decodedText.toString();
	}

	/**
	 * Parse a string using the RFC 2047 rules for an "encoded-word" type. This encoding has the
	 * syntax: encoded-word = "=?" charset "?" encoding "?" encoded-text "?="
	 * 
	 * @param word The possibly encoded word value.
	 * @return The decoded word.
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	private static String decodeWord(String word) throws ParseException, UnsupportedEncodingException {
		// encoded words start with the characters "=?". If this not an encoded word, we throw a
		// ParseException for the caller.

		if (!word.startsWith(ENCODED_TOKEN_MARKER)) {
			throw new ParseException("Invalid RFC 2047 encoded-word: " + word, 0);
		}

		int charsetPos = word.indexOf('?', 2);
		if (charsetPos == -1) {
			throw new ParseException("Missing charset in RFC 2047 encoded-word: " + word, 2);
		}

		// pull out the character set information (this is the MIME name at this point).
		String charset = word.substring(2, charsetPos).toLowerCase();

		// now pull out the encoding token the same way.
		int encodingPos = word.indexOf('?', charsetPos + 1);
		if (encodingPos == -1) {
			throw new ParseException("Missing encoding in RFC 2047 encoded-word: " + word, charsetPos + 1);
		}

		String encoding = word.substring(charsetPos + 1, encodingPos);

		// and finally the encoded text.
		int encodedTextPos = word.indexOf(ENCODED_TOKEN_FINISHER, encodingPos + 1);
		if (encodedTextPos == -1) {
			throw new ParseException("Missing encoded text in RFC 2047 encoded-word: " + word, encodingPos + 1);
		}

		String encodedText = word.substring(encodingPos + 1, encodedTextPos);

		// seems a bit silly to encode a null string, but easy to deal with.
		if (encodedText.length() == 0) {
			return "";
		}

		try {
			byte[] encodedData = encodedText.getBytes(US_ASCII_CHARSET);

			// get the decoded byte data and convert into a string.
			byte[] decodedData;

			// Base64 encoded?
			if (encoding.equals(BASE64_ENCODING_MARKER)) {
				decodedData = Base64.decodeBase64(encodedData);
			}
			else if (encoding.equals(QUOTEDPRINTABLE_ENCODING_MARKER)) { // maybe quoted printable.
				decodedData = decodeQuoted(encodedData);
			}
			else {
				throw new UnsupportedEncodingException("Unknown RFC 2047 encoding: " + encoding);
			}
			return new String(decodedData, javaCharset(charset));
		}
		catch (IOException e) {
			throw new UnsupportedEncodingException("Invalid RFC 2047 encoding");
		}
	}

	/**
	 * Decode the encoded byte data writing it to the given output stream.
	 * 
	 * @param data The array of byte data to decode.
	 * @return the number of bytes produced.
	 * @exception IOException
	 */
	public static byte[] decodeQuoted(byte[] data) throws IOException {
		try {
			return QCodec.decodeQuotedPrintable(data);
		}
		catch (DecoderException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	//------------------------------------------------------------
	/*
	 * The following two properties allow disabling the fold() and unfold() methods and reverting to
	 * the previous behavior. They should never need to be changed and are here only because of my
	 * paranoid concern with compatibility.
	 */
	private static final boolean foldEncodedWords = false;

	/**
	 * Encode a RFC 822 "text" token into mail-safe form as per RFC 2047.
	 * <p>
	 * The given Unicode string is examined for non US-ASCII characters. If the string contains only
	 * US-ASCII characters, it is returned as-is. If the string contains non US-ASCII characters, it
	 * is first character-encoded using the platform's default charset, then transfer-encoded using
	 * either the B or Q encoding. The resulting bytes are then returned as a Unicode string
	 * containing only ASCII characters.
	 * <p>
	 * Note that this method should be used to encode only "unstructured" RFC 822 headers.
	 * <p>
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * 
	 * @param text Unicode string
	 * @return Unicode string containing only US-ASCII characters
	 * @exception UnsupportedEncodingException if the encoding fails
	 */
	public static String encodeText(String text) throws UnsupportedEncodingException {
		return encodeText(text, null, null);
	}

	/**
	 * Encode a RFC 822 "text" token into mail-safe form as per RFC 2047.
	 * <p>
	 * The given Unicode string is examined for non US-ASCII characters. If the string contains only
	 * US-ASCII characters, it is returned as-is. If the string contains non US-ASCII characters, it
	 * is first character-encoded using the platform's default charset, then transfer-encoded using
	 * either the B or Q encoding. The resulting bytes are then returned as a Unicode string
	 * containing only ASCII characters.
	 * <p>
	 * Note that this method should be used to encode only "unstructured" RFC 822 headers.
	 * <p>
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * 
	 * @param text Unicode string
	 * @param charset the charset. If this parameter is null, the platform's default chatset is
	 *            used.
	 * @return Unicode string containing only US-ASCII characters
	 * @exception UnsupportedEncodingException if the encoding fails
	 */
	public static String encodeText(String text, String charset) throws UnsupportedEncodingException {
		return encodeText(text, charset, null);
	}

	/**
	 * Encode a RFC 822 "text" token into mail-safe form as per RFC 2047.
	 * <p>
	 * The given Unicode string is examined for non US-ASCII characters. If the string contains only
	 * US-ASCII characters, it is returned as-is. If the string contains non US-ASCII characters, it
	 * is first character-encoded using the specified charset, then transfer-encoded using either
	 * the B or Q encoding. The resulting bytes are then returned as a Unicode string containing
	 * only ASCII characters.
	 * <p>
	 * Note that this method should be used to encode only "unstructured" RFC 822 headers.
	 * 
	 * @param text the header value
	 * @param charset the charset. If this parameter is null, the platform's default chatset is
	 *            used.
	 * @param encoding the encoding to be used. Currently supported values are "B" and "Q". If this
	 *            parameter is null, then the "Q" encoding is used if most of characters to be
	 *            encoded are in the ASCII charset, otherwise "B" encoding is used.
	 * @return Unicode string containing only US-ASCII characters
	 */
	public static String encodeText(String text, String charset, String encoding) throws UnsupportedEncodingException {
		return encodeWord(text, charset, encoding, false);
	}

	/**
	 * Encode a RFC 822 "word" token into mail-safe form as per RFC 2047.
	 * <p>
	 * The given Unicode string is examined for non US-ASCII characters. If the string contains only
	 * US-ASCII characters, it is returned as-is. If the string contains non US-ASCII characters, it
	 * is first character-encoded using the platform's default charset, then transfer-encoded using
	 * either the B or Q encoding. The resulting bytes are then returned as a Unicode string
	 * containing only ASCII characters.
	 * <p>
	 * This method is meant to be used when creating RFC 822 "phrases". The InternetAddress class,
	 * for example, uses this to encode it's 'phrase' component.
	 * 
	 * @param word Unicode string
	 * @return Array of Unicode strings containing only US-ASCII characters.
	 * @exception UnsupportedEncodingException if the encoding fails
	 */
	public static String encodeWord(String word) throws UnsupportedEncodingException {
		return encodeWord(word, null, null);
	}

	/**
	 * Encode a RFC 822 "word" token into mail-safe form as per RFC 2047.
	 * <p>
	 * The given Unicode string is examined for non US-ASCII characters. If the string contains only
	 * US-ASCII characters, it is returned as-is. If the string contains non US-ASCII characters, it
	 * is first character-encoded using the platform's default charset, then transfer-encoded using
	 * either the B or Q encoding. The resulting bytes are then returned as a Unicode string
	 * containing only ASCII characters.
	 * <p>
	 * This method is meant to be used when creating RFC 822 "phrases". The InternetAddress class,
	 * for example, uses this to encode it's 'phrase' component.
	 * 
	 * @param word Unicode string
	 * @return Array of Unicode strings containing only US-ASCII characters.
	 * @exception UnsupportedEncodingException if the encoding fails
	 */
	public static String encodeWord(String word, String charset) throws UnsupportedEncodingException {
		return encodeWord(word, charset, null);
	}

	/**
	 * Encode a RFC 822 "word" token into mail-safe form as per RFC 2047.
	 * <p>
	 * The given Unicode string is examined for non US-ASCII characters. If the string contains only
	 * US-ASCII characters, it is returned as-is. If the string contains non US-ASCII characters, it
	 * is first character-encoded using the specified charset, then transfer-encoded using either
	 * the B or Q encoding. The resulting bytes are then returned as a Unicode string containing
	 * only ASCII characters.
	 * <p>
	 * 
	 * @param word Unicode string
	 * @param charset the MIME charset
	 * @param encoding the encoding to be used. Currently supported values are "B" and "Q". If this
	 *            parameter is null, then the "Q" encoding is used if most of characters to be
	 *            encoded are in the ASCII charset, otherwise "B" encoding is used.
	 * @return Unicode string containing only US-ASCII characters
	 * @exception UnsupportedEncodingException if the encoding fails
	 */
	public static String encodeWord(String word, String charset, String encoding) throws UnsupportedEncodingException {
		return encodeWord(word, charset, encoding, true);
	}

	/*
	 * Encode the given string. The parameter 'encodingWord' should be true if a RFC 822 "word"
	 * token is being encoded and false if a RFC 822 "text" token is being encoded. This is because
	 * the "Q" encoding defined in RFC 2047 has more restrictions when encoding "word" tokens.
	 * (Sigh)
	 */
	private static String encodeWord(String string, String charset, String encoding, boolean encodingWord) throws UnsupportedEncodingException {
		// If no transfer-encoding is specified, figure one out.
		if (Strings.isEmpty(encoding)) {
			encoding = detectEncoding(string);
			if (Strings.isEmpty(encoding)) {
				// the 'string' does not need encoding, just return it.
				return string;
			}
		}

		encoding = encoding.substring(0, 1);

		boolean b64;
		if (encoding.equalsIgnoreCase("B")) {
			b64 = true;
		}
		else if (encoding.equalsIgnoreCase("Q")) {
			b64 = false;
		}
		else {
			// the 'string' does not need encoding, just return it.
			return string;
		}

		// Else, apply the specified charset conversion.
		String jcharset;
		if (charset == null) { // use default charset
			jcharset = Charsets.UTF_8;
			charset = Charsets.UTF_8;
		}
		else {
			// MIME charset -> java charset
			jcharset = javaCharset(charset);
		}

		
		StringBuilder outb = new StringBuilder(); // the output buffer
		doEncode(string, b64, jcharset,
		// As per RFC 2047, size of an encoded string should not
		// exceed 75 bytes.
		// 7 = size of "=?", '?', 'B'/'Q', '?', "?="
			75 - 7 - charset.length(), // the available space
			"=?" + charset + "?" + encoding + "?", // prefix
			true, encodingWord, outb);

		return outb.toString();
	}

	private static void doEncode(String string, boolean b64, String jcharset, int avail, String prefix, boolean first,
			boolean encodingWord, StringBuilder buf) throws UnsupportedEncodingException {

		// First find out what the length of the encoded version of
		// 'string' would be.
		byte[] bytes = string.getBytes(jcharset);
		int len;
		if (b64) // "B" encoding
			len = BCodec.encodedLength(bytes);
		else
			// "Q"
			len = QCodec.encodedLength(bytes, encodingWord);

		int size;
		if ((len > avail) && ((size = string.length()) > 1)) {
			// If the length is greater than 'avail', split 'string'
			// into two and recurse.
			doEncode(string.substring(0, size / 2), b64, jcharset, avail, prefix, first, encodingWord, buf);
			doEncode(string.substring(size / 2, size), b64, jcharset, avail, prefix, false, encodingWord, buf);
		}
		else {
			byte[] encodedBytes = null; // the encoded stuff

			try { // do the encoding
				if (b64) // "B" encoding
					encodedBytes = BCodec.encodeBytes(bytes);
				else
					// "Q" encoding
					encodedBytes = QCodec.encodeBytes(bytes, true);
			}
			catch (EncoderException ex) {
			}

			// Now write out the encoded (all ASCII) bytes into our
			// StringBuilder
			if (!first) // not the first line of this sequence
				if (foldEncodedWords)
					buf.append("\r\n "); // start a continuation line
				else
					buf.append(" "); // line will be folded later

			buf.append(prefix);
			for (int i = 0; i < encodedBytes.length; i++)
				buf.append((char)encodedBytes[i]);
			buf.append("?="); // terminate the current sequence
		}
	}

	/**
	 * A utility method to quote a word, if the word contains any characters from the specified
	 * 'specials' list.
	 * <p>
	 * The <code>HeaderTokenizer</code> class defines two special sets of delimiters - MIME and RFC
	 * 822.
	 * <p>
	 * This method is typically used during the generation of RFC 822 and MIME header fields.
	 * 
	 * @param word word to be quoted
	 * @param specials the set of special characters
	 * @return the possibly quoted word
	 * @see javax.mail.internet.HeaderTokenizer#MIME
	 * @see javax.mail.internet.HeaderTokenizer#RFC822
	 */
	public static String quote(String word, String specials) {
		int len = word.length();
		if (len == 0)
			return "\"\""; // an empty string is handled specially

		/*
		 * Look for any "bad" characters, Escape and quote the entire string if necessary.
		 */
		boolean needQuoting = false;
		for (int i = 0; i < len; i++) {
			char c = word.charAt(i);
			if (c == '"' || c == '\\' || c == '\r' || c == '\n') {
				// need to escape them and then quote the whole string
				StringBuilder sb = new StringBuilder(len + 3);
				sb.append('"');
				sb.append(word.substring(0, i));
				int lastc = 0;
				for (int j = i; j < len; j++) {
					char cc = word.charAt(j);
					if ((cc == '"') || (cc == '\\') || (cc == '\r') || (cc == '\n'))
						if (cc == '\n' && lastc == '\r')
							; // do nothing, CR was already escaped
						else
							sb.append('\\'); // Escape the character
					sb.append(cc);
					lastc = cc;
				}
				sb.append('"');
				return sb.toString();
			}
			else if (c < 040 || c >= 0177 || specials.indexOf(c) >= 0)
				// These characters cause the string to be quoted
				needQuoting = true;
		}

		if (needQuoting) {
			StringBuilder sb = new StringBuilder(len + 2);
			sb.append('"').append(word).append('"');
			return sb.toString();
		}
		else
			return word;
	}
}
