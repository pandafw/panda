package panda.lang;

import panda.lang.escape.AggregateTranslator;
import panda.lang.escape.CharSequenceTranslator;
import panda.lang.escape.CsvEscaper;
import panda.lang.escape.CsvUnescaper;
import panda.lang.escape.EntityArrays;
import panda.lang.escape.JavaUnicodeEscaper;
import panda.lang.escape.LookupTranslator;
import panda.lang.escape.NumericEntityUnescaper;
import panda.lang.escape.OctalUnescaper;
import panda.lang.escape.UnicodeUnescaper;
import panda.lang.html.HTMLEntities;

import java.io.IOException;

/**
 * utility class for string escape
 * 
 */
public abstract class StringEscapes {
	/* ESCAPE TRANSLATORS */

	/**
	 * Translator object for escaping regular expression. While {@link #escapeRegex(CharSequence)} is the expected method
	 * of use, this object allows the Java escaping functionality to be used as the foundation for a
	 * custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_REGEX = new LookupTranslator(
		new String[][] {
			{ "[", "\\[" },
			{ "]", "\\]" },
			{ "\\", "\\\\" },
			{ "^", "\\^" },
			{ "$", "\\$" },
			{ ".", "\\." },
			{ "|", "\\|" },
			{ "?", "\\?" },
			{ "*", "\\*" },
			{ "+", "\\+" },
			{ "(", "\\(" },
			{ ")", "\\)" }
		});

	/**
	 * Translator object for escaping Java. While {@link #escapeJava(CharSequence)} is the expected method
	 * of use, this object allows the Java escaping functionality to be used as the foundation for a
	 * custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_JAVA = new AggregateTranslator(
			new LookupTranslator(new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" }, }), 
			new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), 
			JavaUnicodeEscaper.outsideOf(32, 0x7f)
		);

	/**
	 * Translator object for escaping EcmaScript/JavaScript. While {@link #escapeEcmaScript(CharSequence)}
	 * is the expected method of use, this object allows the EcmaScript escaping functionality to be
	 * used as the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = new AggregateTranslator(
			new LookupTranslator(new String[][] { { "'", "\\'" }, { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), 
			new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), 
			JavaUnicodeEscaper.outsideOf(32, 0x7f)
		);

	/**
	 * Translator object for escaping Json. While {@link #escapeJson(CharSequence)} is the expected method
	 * of use, this object allows the Json escaping functionality to be used as the foundation for a
	 * custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_JSON = new AggregateTranslator(
			new LookupTranslator(new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), 
			new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), 
			JavaUnicodeEscaper.between(0x80, 0xFF)
		);

	/**
	 * Translator object for escaping XML. While {@link #escapeXml(CharSequence)} is the expected method
	 * of use, this object allows the XML escaping functionality to be used as the foundation for a
	 * custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_XML = new LookupTranslator(HTMLEntities.XML_ESCAPE);

	/**
	 * Translator object for escaping HTML version 3.0. While {@link #escapeHtml3(CharSequence)} is the
	 * expected method of use, this object allows the HTML escaping functionality to be used as the
	 * foundation for a custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_HTML3 = new LookupTranslator(HTMLEntities.HTML3_ESCAPE);

	/**
	 * Translator object for escaping HTML version 4.0. While {@link #escapeHtml4(CharSequence)} is the
	 * expected method of use, this object allows the HTML escaping functionality to be used as the
	 * foundation for a custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_HTML4 = new LookupTranslator(HTMLEntities.HTML4_ESCAPE);

	/**
	 * Translator object for escaping HTML version 4.0. While {@link #escapeHtml4(CharSequence)} is the
	 * expected method of use, this object allows the HTML escaping functionality to be used as the
	 * foundation for a custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_HTML = ESCAPE_HTML4;

	/**
	 * Translator object for escaping HTML version 4.0. While {@link #escapeHtml4(CharSequence)} is the
	 * expected method of use, this object allows the HTML escaping functionality to be used as the
	 * foundation for a custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_PHTML = new AggregateTranslator(
		ESCAPE_HTML, 
		new LookupTranslator(new String[][] { { "\t", "&nbsp;&nbsp;" },
			{ "\n", "<br/>" }, { "\r", "" } }));

	/**
	 * Translator object for escaping individual Comma Separated Values. While
	 * {@link #escapeCsv(CharSequence)} is the expected method of use, this object allows the CSV escaping
	 * functionality to be used as the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator ESCAPE_CSV = new CsvEscaper();

	/* UNESCAPE TRANSLATORS */

	/**
	 * Translator object for unescaping escaped Java. While {@link #unescapeJava(CharSequence)} is the
	 * expected method of use, this object allows the Java unescaping functionality to be used as
	 * the foundation for a custom translator.
	 */
	// TODO: throw "illegal character: \92" as an Exception if a \ on the end of the Java (as per
	// the compiler)?
	public static final CharSequenceTranslator UNESCAPE_JAVA = new AggregateTranslator(
		new OctalUnescaper(), // .between('\1', '\377'),
		new UnicodeUnescaper(), 
		new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), 
		new LookupTranslator(
			new String[][] { { "\\\\", "\\" }, { "\\\"", "\"" }, { "\\'", "'" }, { "\\", "" } }));

	/**
	 * Translator object for unescaping escaped EcmaScript. While
	 * {@link #unescapeEcmaScript(CharSequence)} is the expected method of use, this object allows the
	 * EcmaScript unescaping functionality to be used as the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;

	/**
	 * Translator object for unescaping escaped Json. While {@link #unescapeJson(CharSequence)} is the
	 * expected method of use, this object allows the Json unescaping functionality to be used as
	 * the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;

	/**
	 * Translator object for unescaping escaped HTML 3.0. While {@link #unescapeHtml3(CharSequence)} is
	 * the expected method of use, this object allows the HTML unescaping functionality to be used
	 * as the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator UNESCAPE_HTML3 = new AggregateTranslator(
			new LookupTranslator(HTMLEntities.HTML3_UNESCAPE),
			new NumericEntityUnescaper()
		);

	/**
	 * Translator object for unescaping escaped HTML 4.0. While {@link #unescapeHtml4(CharSequence)} is
	 * the expected method of use, this object allows the HTML unescaping functionality to be used
	 * as the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator UNESCAPE_HTML4 = new AggregateTranslator(
			new LookupTranslator(HTMLEntities.HTML4_UNESCAPE),
			new NumericEntityUnescaper()
		);

	/**
	 * Translator object for unescaping escaped HTML 4.0. While {@link #unescapeHtml4(CharSequence)} is
	 * the expected method of use, this object allows the HTML unescaping functionality to be used
	 * as the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator UNESCAPE_HTML = UNESCAPE_HTML4;
	
	/**
	 * Translator object for unescaping escaped XML. While {@link #unescapeXml(CharSequence)} is the
	 * expected method of use, this object allows the XML unescaping functionality to be used as the
	 * foundation for a custom translator.
	 */
	public static final CharSequenceTranslator UNESCAPE_XML = new AggregateTranslator(
			new LookupTranslator(HTMLEntities.XML_UNESCAPE),
			new NumericEntityUnescaper()
		);

	/**
	 * Translator object for unescaping escaped Comma Separated Value entries. While
	 * {@link #unescapeCsv(CharSequence)} is the expected method of use, this object allows the CSV
	 * unescaping functionality to be used as the foundation for a custom translator.
	 */
	public static final CharSequenceTranslator UNESCAPE_CSV = new CsvUnescaper();

	/* Helper functions */

	// Regex
	// --------------------------------------------------------------------------
	/**
	 * <p>
	 * Escapes the characters in a {@code String} using Regular Expression String rules.
	 * </p>
	 * 
	 * @param input String to escape values in, may be null
	 * @return String with escaped values, {@code null} if null string input
	 */
	public static final String escapeRegex(final CharSequence input) {
		return ESCAPE_REGEX.translate(input);
	}

	// Java and JavaScript
	// --------------------------------------------------------------------------
	/**
	 * <p>
	 * Escapes the characters in a {@code String} using Java String rules.
	 * </p>
	 * <p>
	 * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.)
	 * </p>
	 * <p>
	 * So a tab becomes the characters {@code '\\'} and {@code 't'}.
	 * </p>
	 * <p>
	 * The only difference between Java strings and JavaScript strings is that in JavaScript, a
	 * single quote and forward-slash (/) are escaped.
	 * </p>
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * input string: He didn't say, "Stop!"
	 * output string: He didn't say, \"Stop!\"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param input String to escape values in, may be null
	 * @return String with escaped values, {@code null} if null string input
	 */
	public static final String escapeJava(final CharSequence input) {
		return ESCAPE_JAVA.translate(input);
	}

	public static final void escapeJava(final CharSequence input, final Appendable writer) throws IOException {
		ESCAPE_JAVA.translate(input, writer);
	}

	public static final void escapeJava(final CharSequence input, final int start, final Appendable writer) throws IOException {
		ESCAPE_JAVA.translate(input, start, writer);
	}

	public static final void escapeJava(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		ESCAPE_JAVA.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Escapes the characters in a {@code String} using EcmaScript String rules.
	 * </p>
	 * <p>
	 * Escapes any values it finds into their EcmaScript String form. Deals correctly with quotes
	 * and control-chars (tab, backslash, cr, ff, etc.)
	 * </p>
	 * <p>
	 * So a tab becomes the characters {@code '\\'} and {@code 't'}.
	 * </p>
	 * <p>
	 * The only difference between Java strings and EcmaScript strings is that in EcmaScript, a
	 * single quote and forward-slash (/) are escaped.
	 * </p>
	 * <p>
	 * Note that EcmaScript is best known by the JavaScript and ActionScript dialects.
	 * </p>
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * input string: He didn't say, "Stop!"
	 * output string: He didn\'t say, \"Stop!\"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param input String to escape values in, may be null
	 * @return String with escaped values, {@code null} if null string input
	 */
	public static final String escapeEcmaScript(final CharSequence input) {
		return ESCAPE_ECMASCRIPT.translate(input);
	}

	public static final void escapeEcmaScript(final CharSequence input, final Appendable writer) throws IOException {
		ESCAPE_ECMASCRIPT.translate(input, writer);
	}

	public static final void escapeEcmaScript(final CharSequence input, final int start, final Appendable writer) throws IOException {
		ESCAPE_ECMASCRIPT.translate(input, start, writer);
	}

	public static final void escapeEcmaScript(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		ESCAPE_ECMASCRIPT.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Escapes the characters in a {@code String} using Json String rules.
	 * </p>
	 * <p>
	 * Escapes any values it finds into their Json String form. Deals correctly with quotes and
	 * control-chars (tab, backslash, cr, ff, etc.)
	 * </p>
	 * <p>
	 * So a tab becomes the characters {@code '\\'} and {@code 't'}.
	 * </p>
	 * <p>
	 * The only difference between Java strings and Json strings is that in Json, forward-slash (/)
	 * is escaped.
	 * </p>
	 * <p>
	 * See http://www.ietf.org/rfc/rfc4627.txt for further details.
	 * </p>
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * input string: He didn't say, "Stop!"
	 * output string: He didn't say, \"Stop!\"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param input String to escape values in, may be null
	 * @return String with escaped values, {@code null} if null string input
	 */
	public static final String escapeJson(final CharSequence input) {
		return ESCAPE_JSON.translate(input);
	}

	public static final void escapeJson(final CharSequence input, final Appendable writer) throws IOException {
		ESCAPE_JSON.translate(input, writer);
	}

	public static final void escapeJson(final CharSequence input, final int start, final Appendable writer) throws IOException {
		ESCAPE_JSON.translate(input, start, writer);
	}

	public static final void escapeJson(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		ESCAPE_JSON.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Unescapes any Java literals found in the {@code String}. For example, it will turn a sequence
	 * of {@code '\'} and {@code 'n'} into a newline character, unless the {@code '\'} is preceded
	 * by another {@code '\'}.
	 * </p>
	 * 
	 * @param input the {@code String} to unescape, may be null
	 * @return a new unescaped {@code String}, {@code null} if null string input
	 */
	public static final String unescapeJava(final CharSequence input) {
		return UNESCAPE_JAVA.translate(input);
	}

	public static final void unescapeJava(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_JAVA.translate(input, writer);
	}

	public static final void unescapeJava(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_JAVA.translate(input, start, writer);
	}

	public static final void unescapeJava(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_JAVA.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Unescapes any EcmaScript literals found in the {@code String}.
	 * </p>
	 * <p>
	 * For example, it will turn a sequence of {@code '\'} and {@code 'n'} into a newline character,
	 * unless the {@code '\'} is preceded by another {@code '\'}.
	 * </p>
	 * 
	 * @see #unescapeJava(CharSequence)
	 * @param input the {@code String} to unescape, may be null
	 * @return A new unescaped {@code String}, {@code null} if null string input
	 */
	public static final String unescapeEcmaScript(final CharSequence input) {
		return UNESCAPE_ECMASCRIPT.translate(input);
	}

	public static final void unescapeEcmaScript(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_ECMASCRIPT.translate(input, writer);
	}

	public static final void unescapeEcmaScript(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_ECMASCRIPT.translate(input, start, writer);
	}

	public static final void unescapeEcmaScript(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_ECMASCRIPT.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Unescapes any Json literals found in the {@code String}.
	 * </p>
	 * <p>
	 * For example, it will turn a sequence of {@code '\'} and {@code 'n'} into a newline character,
	 * unless the {@code '\'} is preceded by another {@code '\'}.
	 * </p>
	 * 
	 * @see #unescapeJava(CharSequence)
	 * @param input the {@code String} to unescape, may be null
	 * @return A new unescaped {@code String}, {@code null} if null string input
	 */
	public static final String unescapeJson(final CharSequence input) {
		return UNESCAPE_JSON.translate(input);
	}

	public static final void unescapeJson(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_JSON.translate(input, writer);
	}

	public static final void unescapeJson(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_JSON.translate(input, start, writer);
	}

	public static final void unescapeJson(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_JSON.translate(input, start, end, writer);
	}

	// HTML and XML
	// --------------------------------------------------------------------------
	/**
	 * <p>
	 * Escapes the characters in a {@code String} using HTML entities.
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * <code>"bread" & "butter"</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>.
	 * </p>
	 * <p>
	 * Supports all known HTML 4.0 entities, including funky accents. Note that the commonly used
	 * apostrophe escape character (&amp;apos;) is not a legal entity and so is not supported).
	 * </p>
	 * 
	 * @param input the {@code String} to escape, may be null
	 * @return a new escaped {@code String}, {@code null} if null string input
	 * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO
	 *      Entities</a>
	 * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO
	 *      Latin-1</a>
	 * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity
	 *      references</a>
	 * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character
	 *      References</a>
	 * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code
	 *      positions</a>
	 */
	public static final String escapeHtml4(final CharSequence input) {
		return ESCAPE_HTML4.translate(input);
	}

	public static final void escapeHtml4(final CharSequence input, final Appendable writer) throws IOException {
		ESCAPE_HTML4.translate(input, writer);
	}

	public static final void escapeHtml4(final CharSequence input, final int start, final Appendable writer) throws IOException {
		ESCAPE_HTML4.translate(input, start, writer);
	}

	public static final void escapeHtml4(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		ESCAPE_HTML4.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Escapes the characters in a {@code String} using HTML entities.
	 * </p>
	 * <p>
	 * Supports only the HTML 3.0 entities.
	 * </p>
	 * 
	 * @param input the {@code String} to escape, may be null
	 * @return a new escaped {@code String}, {@code null} if null string input
	 */
	public static final String escapeHtml3(final CharSequence input) {
		return ESCAPE_HTML3.translate(input);
	}

	public static final void escapeHtml3(final CharSequence input, final Appendable writer) throws IOException {
		ESCAPE_HTML3.translate(input, writer);
	}

	public static final void escapeHtml3(final CharSequence input, final int start, final Appendable writer) throws IOException {
		ESCAPE_HTML3.translate(input, start, writer);
	}

	public static final void escapeHtml3(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		ESCAPE_HTML3.translate(input, start, end, writer);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Unescapes a string containing entity escapes to a string containing the actual Unicode
	 * characters corresponding to the escapes. Supports HTML 4.0 entities.
	 * </p>
	 * <p>
	 * For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;" will become
	 * "&lt;Fran&ccedil;ais&gt;"
	 * </p>
	 * <p>
	 * If an entity is unrecognized, it is left alone, and inserted verbatim into the result string.
	 * e.g. "&amp;gt;&amp;zzzz;x" will become "&gt;&amp;zzzz;x".
	 * </p>
	 * 
	 * @param input the {@code String} to unescape, may be null
	 * @return a new unescaped {@code String}, {@code null} if null string input
	 */
	public static final String unescapeHtml(final CharSequence input) {
		return UNESCAPE_HTML.translate(input);
	}

	public static final void unescapeHtml(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_HTML.translate(input, writer);
	}

	public static final void unescapeHtml(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_HTML.translate(input, start, writer);
	}

	public static final void unescapeHtml(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_HTML.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Unescapes a string containing entity escapes to a string containing the actual Unicode
	 * characters corresponding to the escapes. Supports HTML 4.0 entities.
	 * </p>
	 * <p>
	 * For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;" will become
	 * "&lt;Fran&ccedil;ais&gt;"
	 * </p>
	 * <p>
	 * If an entity is unrecognized, it is left alone, and inserted verbatim into the result string.
	 * e.g. "&amp;gt;&amp;zzzz;x" will become "&gt;&amp;zzzz;x".
	 * </p>
	 * 
	 * @param input the {@code String} to unescape, may be null
	 * @return a new unescaped {@code String}, {@code null} if null string input
	 */
	public static final String unescapeHtml4(final CharSequence input) {
		return UNESCAPE_HTML4.translate(input);
	}

	public static final void unescapeHtml4(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_HTML4.translate(input, writer);
	}

	public static final void unescapeHtml4(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_HTML4.translate(input, start, writer);
	}

	public static final void unescapeHtml4(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_HTML4.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Unescapes a string containing entity escapes to a string containing the actual Unicode
	 * characters corresponding to the escapes. Supports only HTML 3.0 entities.
	 * </p>
	 * 
	 * @param input the {@code String} to unescape, may be null
	 * @return a new unescaped {@code String}, {@code null} if null string input
	 */
	public static final String unescapeHtml3(final CharSequence input) {
		return UNESCAPE_HTML3.translate(input);
	}

	public static final void unescapeHtml3(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_HTML3.translate(input, writer);
	}

	public static final void unescapeHtml3(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_HTML3.translate(input, start, writer);
	}

	public static final void unescapeHtml3(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_HTML3.translate(input, start, end, writer);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Escapes the characters in a {@code String} using XML entities.
	 * </p>
	 * <p>
	 * For example: <tt>"bread" & "butter"</tt> =>
	 * <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
	 * </p>
	 * <p>
	 * Supports only the five basic XML entities (gt, lt, quot, amp, apos). Does not support DTDs or
	 * external entities.
	 * </p>
	 * <p>
	 * Note that Unicode characters greater than 0x7f are as of 3.0, no longer escaped. If you still
	 * wish this functionality, you can achieve it via the following:
	 * {@code StringEscapeUtils.ESCAPE_XML.with( NumericEntityEscaper.between(0x7f, Integer.MAX_VALUE) );}
	 * </p>
	 * 
	 * @param input the {@code String} to escape, may be null
	 * @return a new escaped {@code String}, {@code null} if null string input
	 * @see #unescapeXml(java.lang.CharSequence)
	 */
	public static final String escapeXml(final CharSequence input) {
		return ESCAPE_XML.translate(input);
	}

	public static final void escapeXml(final CharSequence input, final Appendable writer) throws IOException {
		ESCAPE_XML.translate(input, writer);
	}

	public static final void escapeXml(final CharSequence input, final int start, final Appendable writer) throws IOException {
		ESCAPE_XML.translate(input, start, writer);
	}

	public static final void escapeXml(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		ESCAPE_XML.translate(input, start, end, writer);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Unescapes a string containing XML entity escapes to a string containing the actual Unicode
	 * characters corresponding to the escapes.
	 * </p>
	 * <p>
	 * Supports only the five basic XML entities (gt, lt, quot, amp, apos). Does not support DTDs or
	 * external entities.
	 * </p>
	 * <p>
	 * Note that numerical \\u Unicode codes are unescaped to their respective Unicode characters.
	 * This may change in future releases.
	 * </p>
	 * 
	 * @param input the {@code String} to unescape, may be null
	 * @return a new unescaped {@code String}, {@code null} if null string input
	 * @see #escapeXml(CharSequence)
	 */
	public static final String unescapeXml(final CharSequence input) {
		return UNESCAPE_XML.translate(input);
	}

	public static final void unescapeXml(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_XML.translate(input, writer);
	}

	public static final void unescapeXml(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_XML.translate(input, start, writer);
	}

	public static final void unescapeXml(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_XML.translate(input, start, end, writer);
	}

	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Returns a {@code String} value for a CSV column enclosed in double quotes, if required.
	 * </p>
	 * <p>
	 * If the value contains a comma, newline or double quote, then the String value is returned
	 * enclosed in double quotes.
	 * </p>
	 * </p>
	 * <p>
	 * Any double quote characters in the value are escaped with another double quote.
	 * </p>
	 * <p>
	 * If the value does not contain a comma, newline or double quote, then the String value is
	 * returned unchanged.
	 * </p>
	 * </p> see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and <a
	 * href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
	 * 
	 * @param input the input CSV column String, may be null
	 * @return the input String, enclosed in double quotes if the value contains a comma, newline or
	 *         double quote, {@code null} if null string input
	 */
	public static final String escapeCsv(final CharSequence input) {
		return ESCAPE_CSV.translate(input);
	}

	public static final void escapeCsv(final CharSequence input, final Appendable writer) throws IOException {
		ESCAPE_CSV.translate(input, writer);
	}

	public static final void escapeCsv(final CharSequence input, final int start, final Appendable writer) throws IOException {
		ESCAPE_CSV.translate(input, start, writer);
	}

	public static final void escapeCsv(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		ESCAPE_CSV.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Returns a {@code String} value for an unescaped CSV column.
	 * </p>
	 * <p>
	 * If the value is enclosed in double quotes, and contains a comma, newline or double quote,
	 * then quotes are removed.
	 * </p>
	 * <p>
	 * Any double quote escaped characters (a pair of double quotes) are unescaped to just one
	 * double quote.
	 * </p>
	 * <p>
	 * If the value is not enclosed in double quotes, or is and does not contain a comma, newline or
	 * double quote, then the String value is returned unchanged.
	 * </p>
	 * </p> see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and <a
	 * href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
	 * 
	 * @param input the input CSV column String, may be null
	 * @return the input String, with enclosing double quotes removed and embedded double quotes
	 *         unescaped, {@code null} if null string input
	 */
	public static final String unescapeCsv(CharSequence input) {
		return UNESCAPE_CSV.translate(input);
	}

	public static final void unescapeCsv(final CharSequence input, final Appendable writer) throws IOException {
		UNESCAPE_CSV.translate(input, writer);
	}

	public static final void unescapeCsv(final CharSequence input, final int start, final Appendable writer) throws IOException {
		UNESCAPE_CSV.translate(input, start, writer);
	}

	public static final void unescapeCsv(final CharSequence input, final int start, final int end, final Appendable writer) throws IOException {
		UNESCAPE_CSV.translate(input, start, end, writer);
	}

	/**
	 * escapeJavaScript
	 * 
	 * @param str string
	 * @return escaped string
	 */
	public static String escapeJavaScript(CharSequence str) {
		return escapeEcmaScript(str);
	}

	public static void escapeJavaScript(CharSequence str, Appendable writer) throws IOException {
		escapeEcmaScript(str, writer);
	}

	public static void escapeJavaScript(CharSequence str, final int start, Appendable writer) throws IOException {
		escapeEcmaScript(str, start, writer);
	}

	public static void escapeJavaScript(CharSequence str, final int start, final int end, Appendable writer) throws IOException {
		escapeEcmaScript(str, start, end, writer);
	}

	public static String escapeHtml(CharSequence input) {
		return ESCAPE_HTML.translate(input);
	}

	public static void escapeHtml(CharSequence input, Appendable writer) throws IOException {
		ESCAPE_HTML.translate(input, writer);
	}

	public static void escapeHtml(CharSequence input, final int start, Appendable writer) throws IOException {
		ESCAPE_HTML.translate(input, start, writer);
	}

	public static void escapeHtml(CharSequence input, final int start, final int end, Appendable writer) throws IOException {
		ESCAPE_HTML.translate(input, start, end, writer);
	}

	/**
	 * <p>
	 * Escapes the characters in a <code>String</code> using HTML entities.
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * <code>"bread" & "butter"</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>.
	 * </p>
	 * <p>
	 * Supports all known HTML 4.0 entities, including funky accents. Note that the commonly used
	 * apostrophe escape character (&amp;apos;) is not a legal entity and so is not supported).
	 * </p>
	 * 
	 * @param str the <code>String</code> to escape, may be null
	 * @return a new escaped <code>String</code>, <code>null</code> if null string input
	 */
	public static String escapePhtml(CharSequence str) {
		return ESCAPE_PHTML.translate(str);
	}

	public static void escapePhtml(CharSequence str, Appendable writer) throws IOException {
		ESCAPE_PHTML.translate(str, writer);
	}

	public static void escapePhtml(CharSequence str, final int start, Appendable writer) throws IOException {
		ESCAPE_PHTML.translate(str, start, writer);
	}

	public static void escapePhtml(CharSequence str, final int start, final int end, Appendable writer) throws IOException {
		ESCAPE_PHTML.translate(str, start, end, writer);
	}

	/**
	 * <p>
	 * Escapes all occurrences of a character in a String with another.
	 * </p>
	 * <p>
	 * A {@code null} string input returns {@code null}. An empty ("") string input returns an empty
	 * string.
	 * </p>
	 * 
	 * <pre>
	 * escapeChars(null, *)           = null
	 * escapeChars("", *)             = ""
	 * escapeChars("abcba", 'b')      = "a\\bc\\ba"
	 * escapeChars("abcba", 'z')      = "abcba"
	 * </pre>
	 * 
	 * @param str String to replace characters in, may be null
	 * @param searchChars the character to search for, may be null
	 * @return modified String, {@code null} if null string input
	 */
	public static String escapeChars(final CharSequence str, final CharSequence searchChars) {
		return escapeChars(str, searchChars, '\\');
	}
	
	/**
	 * <p>
	 * Escapes all occurrences of a character in a String with another.
	 * </p>
	 * <p>
	 * A {@code null} string input returns {@code null}. An empty ("") string input returns an empty
	 * string.
	 * </p>
	 * 
	 * <pre>
	 * escapeChars(null, *, *)        = null
	 * escapeChars("", *, *)          = ""
	 * escapeChars("abcba", 'b', '\\')= "a\\bc\\ba"
	 * escapeChars("abcba", 'z', '\\')= "abcba"
	 * </pre>
	 * 
	 * @param str String to replace characters in, may be null
	 * @param searchChars the character to search for, may be null
	 * @param escapeChar a character for escape, may be zero
	 * @return modified String, {@code null} if null string input
	 */
	public static String escapeChars(final CharSequence str, final CharSequence searchChars, final char escapeChar) {
		if (str == null) {
			return null;
		}
		if (Strings.isEmpty(str) || Strings.isEmpty(searchChars)) {
			return str.toString();
		}

		boolean modified = false;
		final int strLength = str.length();
		final StringBuilder buf = new StringBuilder(strLength);
		for (int i = 0; i < strLength; i++) {
			final char ch = str.charAt(i);
			final int index = CharSequences.indexOf(searchChars, ch, 0);
			if (index >= 0) {
				modified = true;
				if (escapeChar > 0) {
					buf.append(escapeChar);
				}
			}
			buf.append(ch);
		}
		if (modified) {
			return buf.toString();
		}
		return str.toString();
	}

}
