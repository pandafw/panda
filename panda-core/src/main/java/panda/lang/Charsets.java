package panda.lang;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import panda.lang.chardet.LangHint;
import panda.lang.chardet.nsDetector;

/**
 * Character encoding names required of every implementation of the Java platform.
 *
 * From the Java documentation <a
 * href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>:
 * <p>
 * <cite>Every implementation of the Java platform is required to support the following character encodings. Consult the
 * release documentation for your implementation to see if any other encodings are supported. Consult the release
 * documentation for your implementation to see if any other encodings are supported.</cite>
 * </p>
 *
 * <ul>
 * <li><code>US-ASCII</code><br/>
 * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set.</li>
 * <li><code>ISO-8859-1</code><br/>
 * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1.</li>
 * <li><code>UTF-8</code><br/>
 * Eight-bit Unicode Transformation Format.</li>
 * <li><code>UTF-16BE</code><br/>
 * Sixteen-bit Unicode Transformation Format, big-endian byte order.</li>
 * <li><code>UTF-16LE</code><br/>
 * Sixteen-bit Unicode Transformation Format, little-endian byte order.</li>
 * <li><code>UTF-16</code><br/>
 * Sixteen-bit Unicode Transformation Format, byte order specified by a mandatory initial byte-order mark (either order
 * accepted on input, big-endian used on output.)</li>
 * </ul>
 *
 * This perhaps would best belong in the [lang] project. Even if a similar interface is defined in [lang], it is not
 * foreseen that [codec] would be made to depend on [lang].
 *
 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
 *
 */
public class Charsets {
	/**
	 * CharEncodingISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1. </p>
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * </p>
	 * 
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/**
	 * <p>
	 * Seven-bit ASCII, also known as ISO646-US, also known as the Basic Latin block of the Unicode
	 * character set.
	 * </p>
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * </p>
	 * 
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final String US_ASCII = "US-ASCII";

	/**
	 * <p>
	 * Sixteen-bit Unicode Transformation Format, The byte order specified by a mandatory initial
	 * byte-order mark (either order accepted on input, big-endian used on output)
	 * </p>
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * </p>
	 * 
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final String UTF_16 = "UTF-16";

	/**
	 * <p>
	 * Sixteen-bit Unicode Transformation Format, big-endian byte order.
	 * </p>
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * </p>
	 * 
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final String UTF_16BE = "UTF-16BE";

	/**
	 * <p>
	 * Sixteen-bit Unicode Transformation Format, little-endian byte order.
	 * </p>
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * </p>
	 * 
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final String UTF_16LE = "UTF-16LE";

	public static final String UTF_32BE = "UTF-32BE";
	public static final String UTF_32LE = "UTF-32LE";
	
	/**
	 * <p>
	 * Eight-bit Unicode Transformation Format.
	 * </p>
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * </p>
	 * 
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final String UTF_8 = "UTF-8";

	/**
	 * CharEncodingISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1.
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a
	 *      href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final Charset CS_ISO_8859_1 = Charset.forName(ISO_8859_1);

	/**
	 * Seven-bit ASCII, also known as ISO646-US, also known as the Basic Latin block of the Unicode
	 * character set.
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a
	 *      href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final Charset CS_US_ASCII = Charset.forName(US_ASCII);

	/**
	 * Sixteen-bit Unicode Transformation Format, The byte order specified by a mandatory initial
	 * byte-order mark (either order accepted on input, big-endian used on output)
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a
	 *      href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final Charset CS_UTF_16 = Charset.forName(UTF_16);

	/**
	 * Sixteen-bit Unicode Transformation Format, big-endian byte order.
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a
	 *      href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final Charset CS_UTF_16BE = Charset.forName(UTF_16BE);

	/**
	 * Sixteen-bit Unicode Transformation Format, little-endian byte order.
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a
	 *      href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final Charset CS_UTF_16LE = Charset.forName(UTF_16LE);

	public static final Charset CS_UTF_32BE = Charset.forName(UTF_32BE);
	public static final Charset CS_UTF_32LE = Charset.forName(UTF_32LE);

	/**
	 * Eight-bit Unicode Transformation Format.
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a
	 *      href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 */
	public static final Charset CS_UTF_8 = Charset.forName(UTF_8);

	/**
	 * Returns the given Charset or the default Charset if the given Charset is null.
	 * 
	 * @param charset A charset or null.
	 * @return the given Charset or the default Charset if the given Charset is null
	 */
	public static Charset toCharset(final Charset charset) {
		return charset == null ? Charset.defaultCharset() : charset;
	}

	/**
	 * Returns a Charset for the named charset. If the name is null, return the default Charset.
	 * 
	 * @param charset The name of the requested charset, may be null.
	 * @return a Charset for the named charset
	 * @throws java.nio.charset.UnsupportedCharsetException If the named charset is unavailable
	 */
	public static Charset toCharset(final String charset) {
		return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
	}

	/**
	 * Returns a Charset for the named charset. If the name is null, return the default Charset.
	 * 
	 * @param charset The name of the requested charset, may be null.
	 * @param defCharset Default charset value
	 * @return a Charset for the named charset
	 */
	public static Charset toCharset(final String charset, Charset defCharset) {
		if (defCharset == null) {
			defCharset = Charset.defaultCharset();
		}
		if (Strings.isNotEmpty(charset)) {
			try {
				return Charset.forName(charset);
			}
			catch (Exception e) {
				//skip
			}
		}
		return defCharset;
	}

	private final static Map<String, String> charsetMap = new ConcurrentHashMap<String, String>();

	static {
		loadBuiltInCharsetMap();
	}
	
	/**
	 * Loads a preset language-to-encoding map. It assumes the usual character encodings for most
	 * languages. The previous content of the encoding map will be lost. This default map currently
	 * contains the following mappings:
	 * <table>
	 * <tr>
	 * <td>ar</td>
	 * <td>ISO-8859-6</td>
	 * </tr>
	 * <tr>
	 * <td>be</td>
	 * <td>ISO-8859-5</td>
	 * </tr>
	 * <tr>
	 * <td>bg</td>
	 * <td>ISO-8859-5</td>
	 * </tr>
	 * <tr>
	 * <td>ca</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>cs</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>da</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>de</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>el</td>
	 * <td>ISO-8859-7</td>
	 * </tr>
	 * <tr>
	 * <td>en</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>es</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>et</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>fi</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>fr</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>hr</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>hu</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>is</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>it</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>iw</td>
	 * <td>ISO-8859-8</td>
	 * </tr>
	 * <tr>
	 * <td>ja</td>
	 * <td>Shift_JIS</td>
	 * </tr>
	 * <tr>
	 * <td>ko</td>
	 * <td>EUC-KR</td>
	 * </tr>
	 * <tr>
	 * <td>lt</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>lv</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>mk</td>
	 * <td>ISO-8859-5</td>
	 * </tr>
	 * <tr>
	 * <td>nl</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>no</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>pl</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>pt</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>ro</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>ru</td>
	 * <td>ISO-8859-5</td>
	 * </tr>
	 * <tr>
	 * <td>sh</td>
	 * <td>ISO-8859-5</td>
	 * </tr>
	 * <tr>
	 * <td>sk</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>sl</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>sq</td>
	 * <td>ISO-8859-2</td>
	 * </tr>
	 * <tr>
	 * <td>sr</td>
	 * <td>ISO-8859-5</td>
	 * </tr>
	 * <tr>
	 * <td>sv</td>
	 * <td>ISO-8859-1</td>
	 * </tr>
	 * <tr>
	 * <td>tr</td>
	 * <td>ISO-8859-9</td>
	 * </tr>
	 * <tr>
	 * <td>uk</td>
	 * <td>ISO-8859-5</td>
	 * </tr>
	 * <tr>
	 * <td>zh</td>
	 * <td>GB2312</td>
	 * </tr>
	 * <tr>
	 * <td>zh_TW</td>
	 * <td>Big5</td>
	 * </tr>
	 * </table>
	 */
	public static void loadBuiltInCharsetMap() {
		charsetMap.clear();
		charsetMap.put("ar", "ISO-8859-6");
		charsetMap.put("be", "ISO-8859-5");
		charsetMap.put("bg", "ISO-8859-5");
		charsetMap.put("ca", "ISO-8859-1");
		charsetMap.put("cs", "ISO-8859-2");
		charsetMap.put("da", "ISO-8859-1");
		charsetMap.put("de", "ISO-8859-1");
		charsetMap.put("el", "ISO-8859-7");
		charsetMap.put("en", "ISO-8859-1");
		charsetMap.put("es", "ISO-8859-1");
		charsetMap.put("et", "ISO-8859-1");
		charsetMap.put("fi", "ISO-8859-1");
		charsetMap.put("fr", "ISO-8859-1");
		charsetMap.put("hr", "ISO-8859-2");
		charsetMap.put("hu", "ISO-8859-2");
		charsetMap.put("is", "ISO-8859-1");
		charsetMap.put("it", "ISO-8859-1");
		charsetMap.put("iw", "ISO-8859-8");
		charsetMap.put("ja", "Shift_JIS");
		charsetMap.put("ko", "EUC-KR");
		charsetMap.put("lt", "ISO-8859-2");
		charsetMap.put("lv", "ISO-8859-2");
		charsetMap.put("mk", "ISO-8859-5");
		charsetMap.put("nl", "ISO-8859-1");
		charsetMap.put("no", "ISO-8859-1");
		charsetMap.put("pl", "ISO-8859-2");
		charsetMap.put("pt", "ISO-8859-1");
		charsetMap.put("ro", "ISO-8859-2");
		charsetMap.put("ru", "ISO-8859-5");
		charsetMap.put("sh", "ISO-8859-5");
		charsetMap.put("sk", "ISO-8859-2");
		charsetMap.put("sl", "ISO-8859-2");
		charsetMap.put("sq", "ISO-8859-2");
		charsetMap.put("sr", "ISO-8859-5");
		charsetMap.put("sv", "ISO-8859-1");
		charsetMap.put("tr", "ISO-8859-9");
		charsetMap.put("uk", "ISO-8859-5");
		charsetMap.put("zh", "GB2312");
		charsetMap.put("zh_TW", "Big5");
	}

	/**
	 * Clears language-to-encoding map.
	 * 
	 * @see #loadBuiltInCharsetMap
	 * @see #setCharset
	 */
	public static void clearCharsetMap() {
		charsetMap.clear();
	}

	/**
	 * Sets the character set encoding to use for templates of a given locale.
	 * 
	 * @param locale locale
	 * @param encoding encoding
	 * @see #clearCharsetMap
	 * @see #loadBuiltInCharsetMap
	 */
	public static void setCharset(Locale locale, String encoding) {
		charsetMap.put(locale.toString(), encoding);
	}

	/**
	 * Gets the preferred character encoding for the given locale, or the default encoding if no
	 * encoding is set explicitly for the specified locale. You can associate encodings with locales
	 * using {@link #setCharset(Locale, String)} or {@link #loadBuiltInCharsetMap()}.
	 * 
	 * @param loc the locale
	 * @return the preferred character encoding for the locale.
	 */
	public static String charsetFromLocale(Locale loc) {
		// Try for a full name match (may include country and variant)
		String charset = (String)charsetMap.get(loc.toString());
		if (charset == null) {
			if (loc.getVariant().length() > 0) {
				Locale l = new Locale(loc.getLanguage(), loc.getCountry());
				charset = (String)charsetMap.get(l.toString());
				if (charset != null) {
					charsetMap.put(loc.toString(), charset);
				}
			}

			charset = (String)charsetMap.get(loc.getLanguage());
			if (charset != null) {
				charsetMap.put(loc.toString(), charset);
			}
		}
		return charset;
	}

	/**
	 * is the specified charset name a unicode charset?
	 * 
	 * @param charset charset
	 * @return true/false
	 */
	public static boolean isUnicodeCharset(String charset) {
		return Strings.startsWithIgnoreCase(charset, "UTF-");
	}

	public static boolean isSupportedCharset(String charset) {
		if (Strings.isNotEmpty(charset)) {
			return Charset.isSupported(charset);
		}
		return false;
	}
	
	public static Charset defaultCharset() {
		return Charset.defaultCharset();
	}
	
	public static Charset defaultCharset(Charset cs) {
		return cs == null ? defaultCharset() : cs;
	}
	
	public static Charset defaultCharset(Charset cs, Charset def) {
		return cs == null ? def : cs;
	}
	
	public static String defaultEncoding() {
		return Charset.defaultCharset().name();
	}

	public static String defaultEncoding(String enc) {
		return enc == null ? defaultEncoding() : enc;
	}

	public static String defaultEncoding(String enc, String def) {
		return enc == null ? def : enc;
	}

	//-----------------------------------------------------------------------------
	public static String[] detectCharsets(InputStream content) throws IOException {
		return detectCharsets(content, LangHint.ALL);
	}

	public static String[] detectCharsets(InputStream content, LangHint lang) throws IOException {
		nsDetector det = new nsDetector(lang);

		if (det.DoIt(content)) {
			return new String[] { det.getDetectedCharset() };
		}

		det.Done();

		return det.getProbableCharsets();
	}

	public static String detectCharset(InputStream content) throws IOException {
		return detectCharset(content, LangHint.ALL);
	}

	public static String detectCharset(InputStream content, LangHint lang) throws IOException {
		nsDetector det = new nsDetector(lang);

		if (det.DoIt(content)) {
			return det.getDetectedCharset();
		}

		det.Done();
		return det.getProbableCharset();
	}

	public static String[] detectCharsets(byte[] content) {
		return detectCharsets(content, LangHint.ALL);
	}

	public static String[] detectCharsets(byte[] content, LangHint lang) {
		try {
			return detectCharsets(new ByteArrayInputStream(content), lang);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static String detectCharset(byte[] content) {
		return detectCharset(content, LangHint.ALL);
	}

	public static String detectCharset(byte[] content, LangHint lang) {
		try {
			return detectCharset(new ByteArrayInputStream(content), lang);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
