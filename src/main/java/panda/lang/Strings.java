package panda.lang;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * <p>Operations on {@link java.lang.String} that are
 * {@code null} safe.</p>
 *
 * <ul>
 *  <li><b>IsEmpty/IsBlank</b>
 *      - checks if a String contains text</li>
 *  <li><b>Trim/Strip</b>
 *      - removes leading and trailing whitespace</li>
 *  <li><b>Equals</b>
 *      - compares two strings null-safe</li>
 *  <li><b>startsWith</b>
 *      - check if a String starts with a prefix null-safe</li>
 *  <li><b>endsWith</b>
 *      - check if a String ends with a suffix null-safe</li>
 *  <li><b>IndexOf/LastIndexOf/Contains</b>
 *      - null-safe index-of checks
 *  <li><b>IndexOfAny/LastIndexOfAny/IndexOfAnyBut/LastIndexOfAnyBut</b>
 *      - index-of any of a set of Strings</li>
 *  <li><b>ContainsOnly/ContainsNone/ContainsAny</b>
 *      - does String contains only/none/any of these characters</li>
 *  <li><b>Substring/Left/Right/Mid</b>
 *      - null-safe substring extractions</li>
 *  <li><b>SubstringBefore/SubstringAfter/SubstringBetween</b>
 *      - substring extraction relative to other strings</li>
 *  <li><b>Split/Join</b>
 *      - splits a String into an array of substrings and vice versa</li>
 *  <li><b>Remove/Delete</b>
 *      - removes part of a String</li>
 *  <li><b>Replace/Overlay</b>
 *      - Searches a String and replaces one String with another</li>
 *  <li><b>Chomp/Chop</b>
 *      - removes the last part of a String</li>
 *  <li><b>AppendIfMissing</b>
 *      - appends a suffix to the end of the String if not present</li>
 *  <li><b>PrependIfMissing</b>
 *      - prepends a prefix to the start of the String if not present</li>
 *  <li><b>LeftPad/RightPad/Center/Repeat</b>
 *      - pads a String</li>
 *  <li><b>UpperCase/LowerCase/SwapCase/Capitalize/Uncapitalize</b>
 *      - changes the case of a String</li>
 *  <li><b>CountMatches</b>
 *      - counts the number of occurrences of one String in another</li>
 *  <li><b>IsAlpha/IsNumeric/IsWhitespace/IsAsciiPrintable</b>
 *      - checks the characters in a String</li>
 *  <li><b>DefaultString</b>
 *      - protects against a null input String</li>
 *  <li><b>Reverse/ReverseDelimited</b>
 *      - reverses a String</li>
 *  <li><b>Abbreviate</b>
 *      - abbreviates a string using ellipsis</li>
 *  <li><b>Difference</b>
 *      - compares Strings and reports on their differences</li>
 *  <li><b>LevenshteinDistance</b>
 *      - the number of changes needed to change one String into another</li>
 * </ul>
 *
 * <p>The {@code StringUtils} class defines certain words related to
 * String handling.</p>
 *
 * <ul>
 *  <li>null - {@code null}</li>
 *  <li>empty - a zero-length string ({@code ""})</li>
 *  <li>space - the space character ({@code ' '}, char 32)</li>
 *  <li>whitespace - the characters defined by {@link Character#isWhitespace(char)}</li>
 *  <li>trim - the characters &lt;= 32 as in {@link String#trim()}</li>
 * </ul>
 *
 * <p>{@code StringUtils} handles {@code null} input Strings quietly.
 * That is to say that a {@code null} input will return {@code null}.
 * Where a {@code boolean} or {@code int} is being returned
 * details vary by method.</p>
 *
 * <p>A side effect of the {@code null} handling is that a
 * {@code NullPointerException} should be considered a bug in
 * {@code StringUtils}.</p>
 *
 * <p>Methods in this class give sample code to explain their operation.
 * The symbol {@code *} is used to indicate any input including {@code null}.</p>
 *
 * <p>#ThreadSafe#</p>
 * @see java.lang.String
 * 
 * @author yf.frank.wang@gmail.com
 */
public class Strings {
	/**
	 * The empty String {@code ""}.
	 */
	public static final String EMPTY = "";

	/**
	 * A String for a space character.
	 */
	public static final String SPACE = " ";
	
	/**
	 * A String for a CR character.
	 */
	public static final String CR = "\r";
	
	/**
	 * A String for a LF character.
	 */
	public static final String LF = "\n";
	
	/**
	 * A String for a CRLF character.
	 */
	public static final String CRLF = "\r\n";


	/**
	 * Represents a failed index search.
	 */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * <p>
	 * The maximum size to which the padding constant(s) can expand.
	 * </p>
	 */
	private static final int PAD_LIMIT = 8192;

    /**
     * A regex pattern for recognizing blocks of whitespace characters.
     * The apparent convolutedness of the pattern serves the purpose of
     * ignoring "blocks" consisting of only a single space:  the pattern
     * is used only to normalize whitespace, condensing "blocks" down to a
     * single space, thus matching the same would likely cause a great
     * many noop replacements.
     */
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(?: \\s|[\\s&&[^ ]])\\s*");

	/**
	 * EMPTY_ARRAY = new String[0];
	 */
	public static final String[] EMPTY_ARRAY = new String[0];
	
	// Empty checks
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a CharSequence is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isEmpty(null)      = true
	 * Strings.isEmpty("")        = true
	 * Strings.isEmpty(" ")       = false
	 * Strings.isEmpty("bob")     = false
	 * Strings.isEmpty("  bob  ") = false
	 * </pre>
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the CharSequence. That
	 * functionality is available in isBlank().
	 * </p>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is empty or null
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty ("") and not null.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isNotEmpty(null)      = false
	 * Strings.isNotEmpty("")        = false
	 * Strings.isNotEmpty(" ")       = true
	 * Strings.isNotEmpty("bob")     = true
	 * Strings.isNotEmpty("  bob  ") = true
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !Strings.isEmpty(cs);
	}

	/**
	 * <p>
	 * Checks if a CharSequence is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isBlank(null)      = true
	 * Strings.isBlank("")        = true
	 * Strings.isBlank(" ")       = true
	 * Strings.isBlank("bob")     = false
	 * Strings.isBlank("  bob  ") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty (""), not null and not whitespace only.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isNotBlank(null)      = false
	 * Strings.isNotBlank("")        = false
	 * Strings.isNotBlank(" ")       = false
	 * Strings.isNotBlank("bob")     = true
	 * Strings.isNotBlank("  bob  ") = true
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null and not whitespace
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !Strings.isBlank(cs);
	}

	// Trim
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String, handling
	 * {@code null} by returning {@code null}.
	 * </p>
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and end characters
	 * &lt;= 32. To strip whitespace use {@link #strip(CharSequence)}.
	 * </p>
	 * <p>
	 * To trim your choice of characters, use the {@link #strip(CharSequence, String)} methods.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trim(null)          = null
	 * Strings.trim("")            = ""
	 * Strings.trim("     ")       = ""
	 * Strings.trim("abc")         = "abc"
	 * Strings.trim("    abc    ") = "abc"
	 * </pre>
	 * 
	 * @param str the String to be trimmed, may be null
	 * @return the trimmed string, {@code null} if null String input
	 */
	public static String trim(final String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String returning
	 * {@code null} if the String is empty ("") after the trim or if it is {@code null}.
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and end characters
	 * &lt;= 32. To strip whitespace use {@link #stripToNull(CharSequence)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trimToNull(null)          = null
	 * Strings.trimToNull("")            = null
	 * Strings.trimToNull("     ")       = null
	 * Strings.trimToNull("abc")         = "abc"
	 * Strings.trimToNull("    abc    ") = "abc"
	 * </pre>
	 * 
	 * @param str the String to be trimmed, may be null
	 * @return the trimmed String, {@code null} if only chars &lt;= 32, empty or null String input
	 */
	public static String trimToNull(final String str) {
		final String ts = trim(str);
		return isEmpty(ts) ? null : ts;
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String returning an empty
	 * String ("") if the String is empty ("") after the trim or if it is {@code null}.
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and end characters
	 * &lt;= 32. To strip whitespace use {@link #stripToEmpty(CharSequence)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trimToEmpty(null)          = ""
	 * Strings.trimToEmpty("")            = ""
	 * Strings.trimToEmpty("     ")       = ""
	 * Strings.trimToEmpty("abc")         = "abc"
	 * Strings.trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 * 
	 * @param str the String to be trimmed, may be null
	 * @return the trimmed String, or an empty String if {@code null} input
	 */
	public static String trimToEmpty(final String str) {
		return str == null ? EMPTY : str.trim();
	}

	// Stripping
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Strips whitespace from the start and end of a String.
	 * </p>
	 * <p>
	 * This is similar to {@link #trim(String)} but removes whitespace. Whitespace is defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.strip(null)     = null
	 * Strings.strip("")       = ""
	 * Strings.strip("   ")    = ""
	 * Strings.strip("abc")    = "abc"
	 * Strings.strip("  abc")  = "abc"
	 * Strings.strip("abc  ")  = "abc"
	 * Strings.strip(" abc ")  = "abc"
	 * Strings.strip(" ab c ") = "ab c"
	 * </pre>
	 * 
	 * @param str the String to remove whitespace from, may be null
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String strip(final CharSequence str) {
		return strip(str, null);
	}

	/**
	 * <p>
	 * Strips whitespace from the start and end of a String returning {@code null} if the String is
	 * empty ("") after the strip.
	 * </p>
	 * <p>
	 * This is similar to {@link #trimToNull(String)} but removes whitespace. Whitespace is defined
	 * by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripToNull(null)     = null
	 * Strings.stripToNull("")       = null
	 * Strings.stripToNull("   ")    = null
	 * Strings.stripToNull("abc")    = "abc"
	 * Strings.stripToNull("  abc")  = "abc"
	 * Strings.stripToNull("abc  ")  = "abc"
	 * Strings.stripToNull(" abc ")  = "abc"
	 * Strings.stripToNull(" ab c ") = "ab c"
	 * </pre>
	 * 
	 * @param str the String to be stripped, may be null
	 * @return the stripped String, {@code null} if whitespace, empty or null String input
	 */
	public static String stripToNull(CharSequence str) {
		if (str == null) {
			return null;
		}
		String s = strip(str, null);
		return s.isEmpty() ? null : s;
	}

	/**
	 * <p>
	 * Strips whitespace from the start and end of a String returning an empty String if
	 * {@code null} input.
	 * </p>
	 * <p>
	 * This is similar to {@link #trimToEmpty(String)} but removes whitespace. Whitespace is defined
	 * by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripToEmpty(null)     = ""
	 * Strings.stripToEmpty("")       = ""
	 * Strings.stripToEmpty("   ")    = ""
	 * Strings.stripToEmpty("abc")    = "abc"
	 * Strings.stripToEmpty("  abc")  = "abc"
	 * Strings.stripToEmpty("abc  ")  = "abc"
	 * Strings.stripToEmpty(" abc ")  = "abc"
	 * Strings.stripToEmpty(" ab c ") = "ab c"
	 * </pre>
	 * 
	 * @param str the String to be stripped, may be null
	 * @return the trimmed String, or an empty String if {@code null} input
	 */
	public static String stripToEmpty(final CharSequence str) {
		return str == null ? EMPTY : strip(str, null);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the start and end of a String. This is similar to
	 * {@link String#trim()} but allows the characters to be stripped to be controlled.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. An empty string ("") input returns the
	 * empty string.
	 * </p>
	 * <p>
	 * If the stripChars String is {@code null}, whitespace is stripped as defined by
	 * {@link Character#isWhitespace(char)}. Alternatively use {@link #strip(CharSequence)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.strip(null, *)          = null
	 * Strings.strip("", *)            = ""
	 * Strings.strip("abc", null)      = "abc"
	 * Strings.strip("  abc", null)    = "abc"
	 * Strings.strip("abc  ", null)    = "abc"
	 * Strings.strip(" abc ", null)    = "abc"
	 * Strings.strip("  abcyx", "xyz") = "  abc"
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param stripChars the characters to remove, null treated as whitespace
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String strip(CharSequence str, final String stripChars) {
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return EMPTY;
		}
		str = stripStart(str, stripChars);
		return stripEnd(str, stripChars);
	}


	/**
	 * <p>
	 * Strips whitespace from the start of a String.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. An empty string ("") input returns the
	 * empty string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripStart(null)       = null
	 * Strings.stripStart("")         = ""
	 * Strings.stripStart("abc")      = "abc"
	 * Strings.stripStart("abc")      = "abc"
	 * Strings.stripStart("  abc")    = "abc"
	 * Strings.stripStart("abc  ")    = "abc  "
	 * Strings.stripStart(" abc ")    = "abc "
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String stripStart(final CharSequence str) {
		return stripStart(str, null);
	}
	
	/**
	 * <p>
	 * Strips specified character from the start of a String.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. An empty string ("") input returns the
	 * empty string.
	 * </p>
	 * <p>
	 * If the strip char is {@code 0}, whitespace is stripped as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripStart(null, *)          = null
	 * Strings.stripStart("", *)            = ""
	 * Strings.stripStart("abc", 0)         = "abc"
	 * Strings.stripStart("  abc", 0)    = "abc"
	 * Strings.stripStart("abc  ", 0)    = "abc  "
	 * Strings.stripStart(" abc ", 0)    = "abc "
	 * Strings.stripStart("yxabc  ", 'y') = "xabc  "
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param chr the character to remove, null treated as whitespace
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String stripStart(final CharSequence str, final char chr) {
		if (str == null) {
			return null;
		}
		
		int strLen = str.length();
		if (strLen == 0) {
			return str.toString();
		}

		int start = 0;
		if (chr == 0) {
			while (start != strLen && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		}
		else {
			while (start != strLen && chr == str.charAt(start)) {
				start++;
			}
		}
		return str.subSequence(start, str.length()).toString();
	}
	
	/**
	 * <p>
	 * Strips any of a set of characters from the start of a String.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. An empty string ("") input returns the
	 * empty string.
	 * </p>
	 * <p>
	 * If the stripChars String is {@code null}, whitespace is stripped as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripStart(null, *)          = null
	 * Strings.stripStart("", *)            = ""
	 * Strings.stripStart("abc", "")        = "abc"
	 * Strings.stripStart("abc", null)      = "abc"
	 * Strings.stripStart("  abc", null)    = "abc"
	 * Strings.stripStart("abc  ", null)    = "abc  "
	 * Strings.stripStart(" abc ", null)    = "abc "
	 * Strings.stripStart("yxabc  ", "xyz") = "abc  "
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param stripChars the characters to remove, null treated as whitespace
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String stripStart(final CharSequence str, final String stripChars) {
		if (str == null) {
			return null;
		}
		
		int strLen = str.length();
		if (strLen == 0) {
			return str.toString();
		}

		int start = 0;
		if (stripChars == null) {
			while (start != strLen && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		}
		else if (stripChars.length() == 0) {
			return str.toString();
		}
		else {
			while (start != strLen && stripChars.indexOf(str.charAt(start)) != INDEX_NOT_FOUND) {
				start++;
			}
		}
		return str.subSequence(start, str.length()).toString();
	}

	/**
	 * <p>
	 * Strips whitespace from the end of a String.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. An empty string ("") input returns the
	 * empty string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripEnd(null)          = null
	 * Strings.stripEnd("")            = ""
	 * Strings.stripEnd("abc")         = "abc"
	 * Strings.stripEnd("abc")         = "abc"
	 * Strings.stripEnd("  abc")       = "  abc"
	 * Strings.stripEnd("abc  ")       = "abc"
	 * Strings.stripEnd(" abc ")       = " abc"
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param stripChars the set of characters to remove, null treated as whitespace
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String stripEnd(final CharSequence str) {
		return stripEnd(str, null);
	}
	
	/**
	 * <p>
	 * Strips specified character from the end of a String.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. An empty string ("") input returns the
	 * empty string.
	 * </p>
	 * <p>
	 * If the strip char is {@code 0}, whitespace is stripped as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripEnd(null, *)          = null
	 * Strings.stripEnd("", *)            = ""
	 * Strings.stripEnd("abc", 0)         = "abc"
	 * Strings.stripEnd("  abc", 0)    = "  abc"
	 * Strings.stripEnd("abc  ", 0)    = "abc"
	 * Strings.stripEnd(" abc ", 0)    = " abc"
	 * Strings.stripEnd("  abcyx", 'x') = "  abcy"
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param chr the character to remove, null treated as whitespace
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String stripEnd(final CharSequence str, final char chr) {
		if (str == null) {
			return null;
		}
		
		int end = str.length();
		if (end == 0) {
			return str.toString();
		}

		if (chr == 0) {
			while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		}
		else {
			while (end != 0 && chr == str.charAt(end - 1)) {
				end--;
			}
		}
		return str.subSequence(0, end).toString();
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the end of a String.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. An empty string ("") input returns the
	 * empty string.
	 * </p>
	 * <p>
	 * If the stripChars String is {@code null}, whitespace is stripped as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripEnd(null, *)          = null
	 * Strings.stripEnd("", *)            = ""
	 * Strings.stripEnd("abc", "")        = "abc"
	 * Strings.stripEnd("abc", null)      = "abc"
	 * Strings.stripEnd("  abc", null)    = "  abc"
	 * Strings.stripEnd("abc  ", null)    = "abc"
	 * Strings.stripEnd(" abc ", null)    = " abc"
	 * Strings.stripEnd("  abcyx", "xyz") = "  abc"
	 * Strings.stripEnd("120.00", ".0")   = "12"
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param stripChars the set of characters to remove, null treated as whitespace
	 * @return the stripped String, {@code null} if null String input
	 */
	public static String stripEnd(final CharSequence str, final String stripChars) {
		if (str == null) {
			return null;
		}
		
		int end = str.length();
		if (end == 0) {
			return str.toString();
		}

		if (stripChars == null) {
			while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		}
		else if (stripChars.length() == 0) {
			return str.toString();
		}
		else {
			while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != INDEX_NOT_FOUND) {
				end--;
			}
		}
		return str.subSequence(0, end).toString();
	}

	// StripAll
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Strips whitespace from the start and end of every String in an array. Whitespace is defined
	 * by {@link Character#isWhitespace(char)}.
	 * </p>
	 * <p>
	 * A new array is returned each time, except for length zero. A {@code null} array will return
	 * {@code null}. An empty array will return itself. A {@code null} array entry will be ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripAll(null)             = null
	 * Strings.stripAll([])               = []
	 * Strings.stripAll(["abc", "  abc"]) = ["abc", "abc"]
	 * Strings.stripAll(["abc  ", null])  = ["abc", null]
	 * </pre>
	 * 
	 * @param strs the array to remove whitespace from, may be null
	 * @return the stripped Strings, {@code null} if null array input
	 */
	public static String[] stripAll(final CharSequence... strs) {
		return stripAll(strs, null);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the start and end of every String in an array.
	 * </p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 * <p>
	 * A new array is returned each time, except for length zero. A {@code null} array will return
	 * {@code null}. An empty array will return itself. A {@code null} array entry will be ignored.
	 * A {@code null} stripChars will strip whitespace as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripAll(null, *)                = null
	 * Strings.stripAll([], *)                  = []
	 * Strings.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
	 * Strings.stripAll(["abc  ", null], null)  = ["abc", null]
	 * Strings.stripAll(["abc  ", null], "yz")  = ["abc  ", null]
	 * Strings.stripAll(["yabcz", null], "yz")  = ["abc", null]
	 * </pre>
	 * 
	 * @param strs the array to remove characters from, may be null
	 * @param stripChars the characters to remove, null treated as whitespace
	 * @return the stripped Strings, {@code null} if null array input
	 */
	public static String[] stripAll(final CharSequence[] strs, final String stripChars) {
		if (strs == null) {
			return null;
		}

		int strsLen = strs.length;
		if (strsLen == 0) {
			return EMPTY_ARRAY;
		}

		final String[] newArr = new String[strsLen];
		for (int i = 0; i < strsLen; i++) {
			newArr[i] = strip(strs[i], stripChars);
		}
		return newArr;
	}

	// Equals
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two CharSequences, returning {@code true} if they represent equal sequences of
	 * characters.
	 * </p>
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to
	 * be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.equals(null, null)   = true
	 * Strings.equals(null, "abc")  = false
	 * Strings.equals("abc", null)  = false
	 * Strings.equals("abc", "abc") = true
	 * Strings.equals("abc", "ABC") = false
	 * </pre>
	 * 
	 * @see java.lang.String#equals(Object)
	 * @param cs1 the first CharSequence, may be null
	 * @param cs2 the second CharSequence, may be null
	 * @return {@code true} if the CharSequences are equal, case sensitive, or both {@code null}
	 */
	public static boolean equals(CharSequence cs1, CharSequence cs2) {
		if (cs1 == cs2) {
			return true;
		}
		if (cs1 == null || cs2 == null) {
			return false;
		}
		if (cs1 instanceof String && cs2 instanceof String) {
			return cs1.equals(cs2);
		}
		return CharSequences.regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()));
	}

	/**
	 * <p>
	 * Compares two CharSequences, returning {@code true} if they are equal ignoring the case.
	 * </p>
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered
	 * equal. Comparison is case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.equalsIgnoreCase(null, null)   = true
	 * Strings.equalsIgnoreCase(null, "abc")  = false
	 * Strings.equalsIgnoreCase("abc", null)  = false
	 * Strings.equalsIgnoreCase("abc", "abc") = true
	 * Strings.equalsIgnoreCase("abc", "ABC") = true
	 * </pre>
	 * 
	 * @param str1 the first CharSequence, may be null
	 * @param str2 the second CharSequence, may be null
	 * @return {@code true} if the CharSequence are equal, case insensitive, or both {@code null}
	 */
	public static boolean equalsIgnoreCase(final CharSequence str1, final CharSequence str2) {
		if (str1 == null || str2 == null) {
			return str1 == str2;
		}
		else if (str1 == str2) {
			return true;
		}
		else if (str1.length() != str2.length()) {
			return false;
		}
		else {
			return CharSequences.regionMatches(str1, true, 0, str2, 0, Math.max(str1.length(), str2.length()));
		}
	}

	// IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the first index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(int, int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code INDEX_NOT_FOUND (-1)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOf(null, *)         = -1
	 * Strings.indexOf("", *)           = -1
	 * Strings.indexOf("aabaabaa", 'a') = 0
	 * Strings.indexOf("aabaabaa", 'b') = 2
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchChar the character to find
	 * @return the first index of the search character, -1 if no match or {@code null} string input
	 */
	public static int indexOf(final CharSequence seq, final int searchChar) {
		if (isEmpty(seq)) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.indexOf(seq, searchChar, 0);
	}

	/**
	 * <p>
	 * Finds the first index within a CharSequence from a start position, handling {@code null}.
	 * This method uses {@link String#indexOf(int, int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code (INDEX_NOT_FOUND) -1}. A
	 * negative start position is treated as zero. A start position greater than the string length
	 * returns {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOf(null, *, *)          = -1
	 * Strings.indexOf("", *, *)            = -1
	 * Strings.indexOf("aabaabaa", 'b', 0)  = 2
	 * Strings.indexOf("aabaabaa", 'b', 3)  = 5
	 * Strings.indexOf("aabaabaa", 'b', 9)  = -1
	 * Strings.indexOf("aabaabaa", 'b', -1) = 2
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchChar the character to find
	 * @param startPos the start position, negative treated as zero
	 * @return the first index of the search character (always &ge; startPos), -1 if no match or
	 *         {@code null} string input
	 */
	public static int indexOf(final CharSequence seq, final int searchChar, final int startPos) {
		if (isEmpty(seq)) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.indexOf(seq, searchChar, startPos);
	}

	/**
	 * <p>
	 * Finds the first index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(String, int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOf(null, *)          = -1
	 * Strings.indexOf(*, null)          = -1
	 * Strings.indexOf("", "")           = 0
	 * Strings.indexOf("", *)            = -1 (except when * = "")
	 * Strings.indexOf("aabaabaa", "a")  = 0
	 * Strings.indexOf("aabaabaa", "b")  = 2
	 * Strings.indexOf("aabaabaa", "ab") = 1
	 * Strings.indexOf("aabaabaa", "")   = 0
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchSeq the CharSequence to find, may be null
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string
	 *         input
	 */
	public static int indexOf(final CharSequence seq, final CharSequence searchSeq) {
		if (seq == null || searchSeq == null) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.indexOf(seq, searchSeq, 0);
	}

	/**
	 * <p>
	 * Finds the first index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(String, int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is treated as
	 * zero. An empty ("") search CharSequence always matches. A start position greater than the
	 * string length only matches an empty search CharSequence.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOf(null, *, *)          = -1
	 * Strings.indexOf(*, null, *)          = -1
	 * Strings.indexOf("", "", 0)           = 0
	 * Strings.indexOf("", *, 0)            = -1 (except when * = "")
	 * Strings.indexOf("aabaabaa", "a", 0)  = 0
	 * Strings.indexOf("aabaabaa", "b", 0)  = 2
	 * Strings.indexOf("aabaabaa", "ab", 0) = 1
	 * Strings.indexOf("aabaabaa", "b", 3)  = 5
	 * Strings.indexOf("aabaabaa", "b", 9)  = -1
	 * Strings.indexOf("aabaabaa", "b", -1) = 2
	 * Strings.indexOf("aabaabaa", "", 2)   = 2
	 * Strings.indexOf("abc", "", 9)        = 3
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchSeq the CharSequence to find, may be null
	 * @param startPos the start position, negative treated as zero
	 * @return the first index of the search CharSequence (always &ge; startPos), -1 if no match or
	 *         {@code null} string input
	 */
	public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
		if (seq == null || searchSeq == null) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.indexOf(seq, searchSeq, startPos);
	}

	/**
	 * <p>
	 * Finds the n-th index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(String)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.ordinalIndexOf(null, *, *)          = -1
	 * Strings.ordinalIndexOf(*, null, *)          = -1
	 * Strings.ordinalIndexOf("", "", *)           = 0
	 * Strings.ordinalIndexOf("aabaabaa", "a", 1)  = 0
	 * Strings.ordinalIndexOf("aabaabaa", "a", 2)  = 1
	 * Strings.ordinalIndexOf("aabaabaa", "b", 1)  = 2
	 * Strings.ordinalIndexOf("aabaabaa", "b", 2)  = 5
	 * Strings.ordinalIndexOf("aabaabaa", "ab", 1) = 1
	 * Strings.ordinalIndexOf("aabaabaa", "ab", 2) = 4
	 * Strings.ordinalIndexOf("aabaabaa", "", 1)   = 0
	 * Strings.ordinalIndexOf("aabaabaa", "", 2)   = 0
	 * </pre>
	 * <p>
	 * Note that 'head(CharSequence str, int n)' may be implemented as:
	 * </p>
	 * 
	 * <pre>
	 * str.substring(0, lastOrdinalIndexOf(str, &quot;\n&quot;, n))
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @param ordinal the n-th {@code searchStr} to find
	 * @return the n-th index of the search CharSequence, {@code -1} ({@code INDEX_NOT_FOUND}) if no
	 *         match or {@code null} string input
	 */
	public static int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal) {
		return ordinalIndexOf(str, searchStr, ordinal, false);
	}

	/**
	 * <p>
	 * Finds the n-th index within a String, handling {@code null}. This method uses
	 * {@link String#indexOf(String)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @param ordinal the n-th {@code searchStr} to find
	 * @param lastIndex true if lastOrdinalIndexOf() otherwise false if ordinalIndexOf()
	 * @return the n-th index of the search CharSequence, {@code -1} ({@code INDEX_NOT_FOUND}) if no
	 *         match or {@code null} string input
	 */
	// Shared code between ordinalIndexOf(String,String,int) and
	// lastOrdinalIndexOf(String,String,int)
	private static int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal,
			final boolean lastIndex) {
		if (str == null || searchStr == null || ordinal <= 0) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return lastIndex ? str.length() : 0;
		}
		int found = 0;
		int index = lastIndex ? str.length() : INDEX_NOT_FOUND;
		do {
			if (lastIndex) {
				index = CharSequences.lastIndexOf(str, searchStr, index - 1);
			}
			else {
				index = CharSequences.indexOf(str, searchStr, index + 1);
			}
			if (index < 0) {
				return index;
			}
			found++;
		}
		while (found < ordinal);
		return index;
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a CharSequence.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is treated as
	 * zero. An empty ("") search CharSequence always matches. A start position greater than the
	 * string length only matches an empty search CharSequence.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfIgnoreCase(null, *)          = -1
	 * Strings.indexOfIgnoreCase(*, null)          = -1
	 * Strings.indexOfIgnoreCase("", "")           = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "a")  = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "b")  = 2
	 * Strings.indexOfIgnoreCase("aabaabaa", "ab") = 1
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string
	 *         input
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a CharSequence from the specified position.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is treated as
	 * zero. An empty ("") search CharSequence always matches. A start position greater than the
	 * string length only matches an empty search CharSequence.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfIgnoreCase(null, *, *)          = -1
	 * Strings.indexOfIgnoreCase(*, null, *)          = -1
	 * Strings.indexOfIgnoreCase("", "", 0)           = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * Strings.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * Strings.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * Strings.indexOfIgnoreCase("abc", "", 9)        = 3
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @param startPos the start position, negative treated as zero
	 * @return the first index of the search CharSequence (always &ge; startPos), -1 if no match or
	 *         {@code null} string input
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return INDEX_NOT_FOUND;
		}
		if (startPos < 0) {
			startPos = 0;
		}
		final int endLimit = str.length() - searchStr.length() + 1;
		if (startPos > endLimit) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return startPos;
		}
		for (int i = startPos; i < endLimit; i++) {
			if (CharSequences.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// LastIndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the last index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastIndexOf(null, *)         = -1
	 * Strings.lastIndexOf("", *)           = -1
	 * Strings.lastIndexOf("aabaabaa", 'a') = 7
	 * Strings.lastIndexOf("aabaabaa", 'b') = 5
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchChar the character to find
	 * @return the last index of the search character, -1 if no match or {@code null} string input
	 */
	public static int lastIndexOf(final CharSequence seq, final int searchChar) {
		if (isEmpty(seq)) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.lastIndexOf(seq, searchChar, seq.length());
	}

	/**
	 * <p>
	 * Finds the last index within a CharSequence from a start position, handling {@code null}. This
	 * method uses {@link String#lastIndexOf(int, int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code -1}. A negative start position
	 * returns {@code -1}. A start position greater than the string length searches the whole
	 * string. The search starts at the startPos and works backwards; matches starting after the
	 * start position are ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastIndexOf(null, *, *)          = -1
	 * Strings.lastIndexOf("", *,  *)           = -1
	 * Strings.lastIndexOf("aabaabaa", 'b', 8)  = 5
	 * Strings.lastIndexOf("aabaabaa", 'b', 4)  = 2
	 * Strings.lastIndexOf("aabaabaa", 'b', 0)  = -1
	 * Strings.lastIndexOf("aabaabaa", 'b', 9)  = 5
	 * Strings.lastIndexOf("aabaabaa", 'b', -1) = -1
	 * Strings.lastIndexOf("aabaabaa", 'a', 0)  = 0
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchChar the character to find
	 * @param startPos the start position
	 * @return the last index of the search character (always &le; startPos), -1 if no match or
	 *         {@code null} string input
	 */
	public static int lastIndexOf(final CharSequence seq, final int searchChar, final int startPos) {
		if (isEmpty(seq)) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.lastIndexOf(seq, searchChar, startPos);
	}

	/**
	 * <p>
	 * Finds the last index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(String)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastIndexOf(null, *)          = -1
	 * Strings.lastIndexOf(*, null)          = -1
	 * Strings.lastIndexOf("", "")           = 0
	 * Strings.lastIndexOf("aabaabaa", "a")  = 7
	 * Strings.lastIndexOf("aabaabaa", "b")  = 5
	 * Strings.lastIndexOf("aabaabaa", "ab") = 4
	 * Strings.lastIndexOf("aabaabaa", "")   = 8
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchSeq the CharSequence to find, may be null
	 * @return the last index of the search String, -1 if no match or {@code null} string input
	 */
	public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq) {
		if (seq == null || searchSeq == null) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.lastIndexOf(seq, searchSeq, seq.length());
	}

	/**
	 * <p>
	 * Finds the n-th last index within a String, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(String)}.
	 * </p>
	 * <p>
	 * A {@code null} String will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastOrdinalIndexOf(null, *, *)          = -1
	 * Strings.lastOrdinalIndexOf(*, null, *)          = -1
	 * Strings.lastOrdinalIndexOf("", "", *)           = 0
	 * Strings.lastOrdinalIndexOf("aabaabaa", "a", 1)  = 7
	 * Strings.lastOrdinalIndexOf("aabaabaa", "a", 2)  = 6
	 * Strings.lastOrdinalIndexOf("aabaabaa", "b", 1)  = 5
	 * Strings.lastOrdinalIndexOf("aabaabaa", "b", 2)  = 2
	 * Strings.lastOrdinalIndexOf("aabaabaa", "ab", 1) = 4
	 * Strings.lastOrdinalIndexOf("aabaabaa", "ab", 2) = 1
	 * Strings.lastOrdinalIndexOf("aabaabaa", "", 1)   = 8
	 * Strings.lastOrdinalIndexOf("aabaabaa", "", 2)   = 8
	 * </pre>
	 * <p>
	 * Note that 'tail(CharSequence str, int n)' may be implemented as:
	 * </p>
	 * 
	 * <pre>
	 * str.substring(lastOrdinalIndexOf(str, &quot;\n&quot;, n) + 1)
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @param ordinal the n-th last {@code searchStr} to find
	 * @return the n-th last index of the search CharSequence, {@code -1} ({@code INDEX_NOT_FOUND})
	 *         if no match or {@code null} string input
	 */
	public static int lastOrdinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal) {
		return ordinalIndexOf(str, searchStr, ordinal, true);
	}

	/**
	 * <p>
	 * Finds the last index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(String, int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position returns
	 * {@code -1}. An empty ("") search CharSequence always matches unless the start position is
	 * negative. A start position greater than the string length searches the whole string. The
	 * search starts at the startPos and works backwards; matches starting after the start position
	 * are ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastIndexOf(null, *, *)          = -1
	 * Strings.lastIndexOf(*, null, *)          = -1
	 * Strings.lastIndexOf("aabaabaa", "a", 8)  = 7
	 * Strings.lastIndexOf("aabaabaa", "b", 8)  = 5
	 * Strings.lastIndexOf("aabaabaa", "ab", 8) = 4
	 * Strings.lastIndexOf("aabaabaa", "b", 9)  = 5
	 * Strings.lastIndexOf("aabaabaa", "b", -1) = -1
	 * Strings.lastIndexOf("aabaabaa", "a", 0)  = 0
	 * Strings.lastIndexOf("aabaabaa", "b", 0)  = -1
	 * Strings.lastIndexOf("aabaabaa", "b", 1)  = -1
	 * Strings.lastIndexOf("aabaabaa", "b", 2)  = 2
	 * Strings.lastIndexOf("aabaabaa", "ba", 2)  = -1
	 * Strings.lastIndexOf("aabaabaa", "ba", 2)  = 2
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchSeq the CharSequence to find, may be null
	 * @param startPos the start position, negative treated as zero
	 * @return the last index of the search CharSequence (always &le; startPos), -1 if no match or
	 *         {@code null} string input
	 */
	public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
		if (seq == null || searchSeq == null) {
			return INDEX_NOT_FOUND;
		}
		return CharSequences.lastIndexOf(seq, searchSeq, startPos);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the last index within a CharSequence.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position returns
	 * {@code -1}. An empty ("") search CharSequence always matches unless the start position is
	 * negative. A start position greater than the string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastIndexOfIgnoreCase(null, *)          = -1
	 * Strings.lastIndexOfIgnoreCase(*, null)          = -1
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string
	 *         input
	 */
	public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		if (str == null || searchStr == null) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOfIgnoreCase(str, searchStr, str.length());
	}

	/**
	 * <p>
	 * Case in-sensitive find of the last index within a CharSequence from the specified position.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position returns
	 * {@code -1}. An empty ("") search CharSequence always matches unless the start position is
	 * negative. A start position greater than the string length searches the whole string. The
	 * search starts at the startPos and works backwards; matches starting after the start position
	 * are ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastIndexOfIgnoreCase(null, *, *)          = -1
	 * Strings.lastIndexOfIgnoreCase(*, null, *)          = -1
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @param startPos the start position
	 * @return the last index of the search CharSequence (always &le; startPos), -1 if no match or
	 *         {@code null} input
	 */
	public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return INDEX_NOT_FOUND;
		}
		if (startPos > str.length() - searchStr.length()) {
			startPos = str.length() - searchStr.length();
		}
		if (startPos < 0) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return startPos;
		}

		for (int i = startPos; i >= 0; i--) {
			if (CharSequences.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// Contains
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if CharSequence contains a search character, handling {@code null}. This method uses
	 * {@link String#indexOf(int)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.contains(null, *)    = false
	 * Strings.contains("", *)      = false
	 * Strings.contains("abc", 'a') = true
	 * Strings.contains("abc", 'z') = false
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchChar the character to find
	 * @return true if the CharSequence contains the search character, false if not or {@code null}
	 *         string input
	 */
	public static boolean contains(final CharSequence seq, final int searchChar) {
		if (isEmpty(seq)) {
			return false;
		}
		return CharSequences.indexOf(seq, searchChar, 0) >= 0;
	}

	/**
	 * <p>
	 * Checks if CharSequence contains a search CharSequence, handling {@code null}. This method
	 * uses {@link String#indexOf(String)} if possible.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.contains(null, *)     = false
	 * Strings.contains(*, null)     = false
	 * Strings.contains("", "")      = true
	 * Strings.contains("abc", "")   = true
	 * Strings.contains("abc", "a")  = true
	 * Strings.contains("abc", "z")  = false
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchSeq the CharSequence to find, may be null
	 * @return true if the CharSequence contains the search CharSequence, false if not or
	 *         {@code null} string input
	 */
	public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
		if (seq == null || searchSeq == null) {
			return false;
		}
		return CharSequences.indexOf(seq, searchSeq, 0) >= 0;
	}

	/**
	 * <p>
	 * Checks if CharSequence contains a search CharSequence irrespective of case, handling
	 * {@code null}. Case-insensitivity is defined as by {@link String#equalsIgnoreCase(String)}.
	 * <p>
	 * A {@code null} CharSequence will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.contains(null, *) = false
	 * Strings.contains(*, null) = false
	 * Strings.contains("", "") = true
	 * Strings.contains("abc", "") = true
	 * Strings.contains("abc", "a") = true
	 * Strings.contains("abc", "z") = false
	 * Strings.contains("abc", "A") = true
	 * Strings.contains("abc", "Z") = false
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @return true if the CharSequence contains the search CharSequence irrespective of case or
	 *         false if not or {@code null} string input
	 */
	public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		final int len = searchStr.length();
		final int max = str.length() - len;
		for (int i = 0; i <= max; i++) {
			if (CharSequences.regionMatches(str, true, i, searchStr, 0, len)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given CharSequence contains any whitespace characters.
	 * 
	 * @param seq the CharSequence to check (may be {@code null})
	 * @return {@code true} if the CharSequence is not empty and contains at least 1 whitespace
	 *         character
	 * @see java.lang.Character#isWhitespace
	 */
	// From org.springframework.util.StringUtils, under Apache License 2.0
	public static boolean containsWhitespace(final CharSequence seq) {
		if (isEmpty(seq)) {
			return false;
		}
		final int strLen = seq.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(seq.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	// IndexOfAny chars
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Search a CharSequence to find the first index of any character in the given set of
	 * characters.
	 * </p>
	 * <p>
	 * A {@code null} String will return {@code -1}. A {@code null} or zero length search array will
	 * return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfAny(null, *)                = -1
	 * Strings.indexOfAny("", *)                  = -1
	 * Strings.indexOfAny(*, null)                = -1
	 * Strings.indexOfAny(*, [])                  = -1
	 * Strings.indexOfAny("zzabyycdxx",['z','a']) = 0
	 * Strings.indexOfAny("zzabyycdxx",['b','y']) = 3
	 * Strings.indexOfAny("aba", ['z'])           = -1
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAny(final CharSequence cs, final char... searchChars) {
		if (isEmpty(cs) || Arrays.isEmpty(searchChars)) {
			return INDEX_NOT_FOUND;
		}
		final int csLen = cs.length();
		final int csLast = csLen - 1;
		final int searchLen = searchChars.length;
		final int searchLast = searchLen - 1;
		for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
						// ch is a supplementary character
						if (searchChars[j + 1] == cs.charAt(i + 1)) {
							return i;
						}
					}
					else {
						return i;
					}
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Search a CharSequence to find the first index of any character in the given set of
	 * characters.
	 * </p>
	 * <p>
	 * A {@code null} String will return {@code -1}. A {@code null} search string will return
	 * {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfAny(null, *)            = -1
	 * Strings.indexOfAny("", *)              = -1
	 * Strings.indexOfAny(*, null)            = -1
	 * Strings.indexOfAny(*, "")              = -1
	 * Strings.indexOfAny("zzabyycdxx", "za") = 0
	 * Strings.indexOfAny("zzabyycdxx", "by") = 3
	 * Strings.indexOfAny("aba","z")          = -1
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAny(final CharSequence cs, final String searchChars) {
		if (isEmpty(cs) || isEmpty(searchChars)) {
			return INDEX_NOT_FOUND;
		}
		return indexOfAny(cs, searchChars.toCharArray());
	}

	// ContainsAny
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the CharSequence contains any character in the given set of characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} or zero length search
	 * array will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsAny(null, *)                = false
	 * Strings.containsAny("", *)                  = false
	 * Strings.containsAny(*, null)                = false
	 * Strings.containsAny(*, [])                  = false
	 * Strings.containsAny("zzabyycdxx",['z','a']) = true
	 * Strings.containsAny("zzabyycdxx",['b','y']) = true
	 * Strings.containsAny("aba", ['z'])           = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the {@code true} if any of the chars are found, {@code false} if no match or null
	 *         input
	 */
	public static boolean containsAny(final CharSequence cs, final char... searchChars) {
		if (isEmpty(cs) || Arrays.isEmpty(searchChars)) {
			return false;
		}
		final int csLength = cs.length();
		final int searchLength = searchChars.length;
		final int csLast = csLength - 1;
		final int searchLast = searchLength - 1;
		for (int i = 0; i < csLength; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLength; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return true;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return true;
						}
					}
					else {
						// ch is in the Basic Multilingual Plane
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains any character in the given set of characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} search CharSequence
	 * will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsAny(null, *)            = false
	 * Strings.containsAny("", *)              = false
	 * Strings.containsAny(*, null)            = false
	 * Strings.containsAny(*, "")              = false
	 * Strings.containsAny("zzabyycdxx", "za") = true
	 * Strings.containsAny("zzabyycdxx", "by") = true
	 * Strings.containsAny("aba","z")          = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the {@code true} if any of the chars are found, {@code false} if no match or null
	 *         input
	 */
	public static boolean containsAny(final CharSequence cs, final CharSequence searchChars) {
		if (searchChars == null) {
			return false;
		}
		return containsAny(cs, CharSequences.toCharArray(searchChars));
	}

	// IndexOfAnyBut chars
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Searches a CharSequence to find the first index of any character not in the given set of
	 * characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} or zero length search
	 * array will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfAnyBut(null, *)                              = -1
	 * Strings.indexOfAnyBut("", *)                                = -1
	 * Strings.indexOfAnyBut(*, null)                              = -1
	 * Strings.indexOfAnyBut(*, [])                                = -1
	 * Strings.indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
	 * Strings.indexOfAnyBut("aba", new char[] {'z'} )             = 0
	 * Strings.indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1
	 * 
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAnyBut(final CharSequence cs, final char... searchChars) {
		if (isEmpty(cs) || Arrays.isEmpty(searchChars)) {
			return INDEX_NOT_FOUND;
		}
		final int csLen = cs.length();
		final int csLast = csLen - 1;
		final int searchLen = searchChars.length;
		final int searchLast = searchLen - 1;
		outer: for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
						if (searchChars[j + 1] == cs.charAt(i + 1)) {
							continue outer;
						}
					}
					else {
						continue outer;
					}
				}
			}
			return i;
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Search a CharSequence to find the first index of any character not in the given set of
	 * characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} or empty search string
	 * will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfAnyBut(null, *)            = -1
	 * Strings.indexOfAnyBut("", *)              = -1
	 * Strings.indexOfAnyBut(*, null)            = -1
	 * Strings.indexOfAnyBut(*, "")              = -1
	 * Strings.indexOfAnyBut("zzabyycdxx", "za") = 3
	 * Strings.indexOfAnyBut("zzabyycdxx", "")   = -1
	 * Strings.indexOfAnyBut("aba","ab")         = -1
	 * </pre>
	 * 
	 * @param seq the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAnyBut(final CharSequence seq, final CharSequence searchChars) {
		if (isEmpty(seq) || isEmpty(searchChars)) {
			return INDEX_NOT_FOUND;
		}
		final int strLen = seq.length();
		for (int i = 0; i < strLen; i++) {
			final char ch = seq.charAt(i);
			final boolean chFound = CharSequences.indexOf(searchChars, ch, 0) >= 0;
			if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
				final char ch2 = seq.charAt(i + 1);
				if (chFound && CharSequences.indexOf(searchChars, ch2, 0) < 0) {
					return i;
				}
			}
			else {
				if (!chFound) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	// ContainsOnly
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the CharSequence contains only certain characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} valid character array
	 * will return {@code false}. An empty CharSequence (length()=0) always returns {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsOnly(null, *)       = false
	 * Strings.containsOnly(*, null)       = false
	 * Strings.containsOnly("", *)         = true
	 * Strings.containsOnly("ab", '')      = false
	 * Strings.containsOnly("abab", 'abc') = true
	 * Strings.containsOnly("ab1", 'abc')  = false
	 * Strings.containsOnly("abz", 'abc')  = false
	 * </pre>
	 * 
	 * @param cs the String to check, may be null
	 * @param valid an array of valid chars, may be null
	 * @return true if it only contains valid chars and is non-null
	 */
	public static boolean containsOnly(final CharSequence cs, final char... valid) {
		// All these pre-checks are to maintain API with an older version
		if (valid == null || cs == null) {
			return false;
		}
		if (cs.length() == 0) {
			return true;
		}
		if (valid.length == 0) {
			return false;
		}
		return indexOfAnyBut(cs, valid) == INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only certain characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} valid character String
	 * will return {@code false}. An empty String (length()=0) always returns {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsOnly(null, *)       = false
	 * Strings.containsOnly(*, null)       = false
	 * Strings.containsOnly("", *)         = true
	 * Strings.containsOnly("ab", "")      = false
	 * Strings.containsOnly("abab", "abc") = true
	 * Strings.containsOnly("ab1", "abc")  = false
	 * Strings.containsOnly("abz", "abc")  = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param validChars a String of valid chars, may be null
	 * @return true if it only contains valid chars and is non-null
	 */
	public static boolean containsOnly(final CharSequence cs, final String validChars) {
		if (cs == null || validChars == null) {
			return false;
		}
		return containsOnly(cs, validChars.toCharArray());
	}

	// ContainsNone
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks that the CharSequence does not contain certain characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code true}. A {@code null} invalid character array
	 * will return {@code true}. An empty CharSequence (length()=0) always returns true.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsNone(null, *)       = true
	 * Strings.containsNone(*, null)       = true
	 * Strings.containsNone("", *)         = true
	 * Strings.containsNone("ab", '')      = true
	 * Strings.containsNone("abab", 'xyz') = true
	 * Strings.containsNone("ab1", 'xyz')  = true
	 * Strings.containsNone("abz", 'xyz')  = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars an array of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 */
	public static boolean containsNone(final CharSequence cs, final char... searchChars) {
		if (cs == null || searchChars == null) {
			return true;
		}
		final int csLen = cs.length();
		final int csLast = csLen - 1;
		final int searchLen = searchChars.length;
		final int searchLast = searchLen - 1;
		for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return false;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return false;
						}
					}
					else {
						// ch is in the Basic Multilingual Plane
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks that the CharSequence does not contain certain characters.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code true}. A {@code null} invalid character array
	 * will return {@code true}. An empty String ("") always returns true.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsNone(null, *)       = true
	 * Strings.containsNone(*, null)       = true
	 * Strings.containsNone("", *)         = true
	 * Strings.containsNone("ab", "")      = true
	 * Strings.containsNone("abab", "xyz") = true
	 * Strings.containsNone("ab1", "xyz")  = true
	 * Strings.containsNone("abz", "xyz")  = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @param invalidChars a String of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 */
	public static boolean containsNone(final CharSequence cs, final String invalidChars) {
		if (cs == null || invalidChars == null) {
			return true;
		}
		return containsNone(cs, invalidChars.toCharArray());
	}

	// IndexOfAny strings
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Find the first index of any of a set of potential substrings.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} or zero length search
	 * array will return {@code -1}. A {@code null} search array entry will be ignored, but a search
	 * array containing "" will return {@code 0} if {@code str} is not null. This method uses
	 * {@link String#indexOf(String)} if possible.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfAny(null, *)                     = -1
	 * Strings.indexOfAny(*, null)                     = -1
	 * Strings.indexOfAny(*, [])                       = -1
	 * Strings.indexOfAny("zzabyycdxx", ["ab","cd"])   = 2
	 * Strings.indexOfAny("zzabyycdxx", ["cd","ab"])   = 2
	 * Strings.indexOfAny("zzabyycdxx", ["mn","op"])   = -1
	 * Strings.indexOfAny("zzabyycdxx", ["zab","aby"]) = 1
	 * Strings.indexOfAny("zzabyycdxx", [""])          = 0
	 * Strings.indexOfAny("", [""])                    = 0
	 * Strings.indexOfAny("", ["a"])                   = -1
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStrs the CharSequences to search for, may be null
	 * @return the first index of any of the searchStrs in str, -1 if no match
	 */
	public static int indexOfAny(final CharSequence str, final CharSequence... searchStrs) {
		if (str == null || searchStrs == null) {
			return INDEX_NOT_FOUND;
		}
		final int sz = searchStrs.length;

		// String's can't have a MAX_VALUEth index.
		int ret = Integer.MAX_VALUE;

		int tmp = 0;
		for (int i = 0; i < sz; i++) {
			final CharSequence search = searchStrs[i];
			if (search == null) {
				continue;
			}
			tmp = CharSequences.indexOf(str, search, 0);
			if (tmp == INDEX_NOT_FOUND) {
				continue;
			}

			if (tmp < ret) {
				ret = tmp;
			}
		}

		return ret == Integer.MAX_VALUE ? INDEX_NOT_FOUND : ret;
	}

	/**
	 * <p>
	 * Find the latest index of any of a set of potential substrings.
	 * </p>
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} search array will return
	 * {@code -1}. A {@code null} or zero length search array entry will be ignored, but a search
	 * array containing "" will return the length of {@code str} if {@code str} is not null. This
	 * method uses {@link String#indexOf(String)} if possible
	 * </p>
	 * 
	 * <pre>
	 * Strings.lastIndexOfAny(null, *)                   = -1
	 * Strings.lastIndexOfAny(*, null)                   = -1
	 * Strings.lastIndexOfAny(*, [])                     = -1
	 * Strings.lastIndexOfAny(*, [null])                 = -1
	 * Strings.lastIndexOfAny("zzabyycdxx", ["ab","cd"]) = 6
	 * Strings.lastIndexOfAny("zzabyycdxx", ["cd","ab"]) = 6
	 * Strings.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
	 * Strings.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
	 * Strings.lastIndexOfAny("zzabyycdxx", ["mn",""])   = 10
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param searchStrs the CharSequences to search for, may be null
	 * @return the last index of any of the CharSequences, -1 if no match
	 */
	public static int lastIndexOfAny(final CharSequence str, final CharSequence... searchStrs) {
		if (str == null || searchStrs == null) {
			return INDEX_NOT_FOUND;
		}
		final int sz = searchStrs.length;
		int ret = INDEX_NOT_FOUND;
		int tmp = 0;
		for (int i = 0; i < sz; i++) {
			final CharSequence search = searchStrs[i];
			if (search == null) {
				continue;
			}
			tmp = CharSequences.lastIndexOf(str, search, str.length());
			if (tmp > ret) {
				ret = tmp;
			}
		}
		return ret;
	}

	// Substring
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * <p>
	 * A negative start position can be used to start {@code n} characters from the end of the
	 * String.
	 * </p>
	 * <p>
	 * A {@code null} String will return {@code null}. An empty ("") String will return "".
	 * </p>
	 * 
	 * <pre>
	 * Strings.substring(null, *)   = null
	 * Strings.substring("", *)     = ""
	 * Strings.substring("abc", 0)  = "abc"
	 * Strings.substring("abc", 2)  = "c"
	 * Strings.substring("abc", 4)  = ""
	 * Strings.substring("abc", -2) = "bc"
	 * Strings.substring("abc", -4) = "abc"
	 * </pre>
	 * 
	 * @param str the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the end of the String
	 *            by this many characters
	 * @return substring from start position, {@code null} if null String input
	 */
	public static String substring(final String str, int start) {
		if (str == null) {
			return null;
		}

		// handle negatives, which means last n characters
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return EMPTY;
		}

		return str.substring(start);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * <p>
	 * A negative start position can be used to start/end {@code n} characters from the end of the
	 * String.
	 * </p>
	 * <p>
	 * The returned substring starts with the character in the {@code start} position and ends
	 * before the {@code end} position. All position counting is zero-based -- i.e., to start at the
	 * beginning of the string use {@code start = 0}. Negative start and end positions can be used
	 * to specify offsets relative to the end of the String.
	 * </p>
	 * <p>
	 * If {@code start} is not strictly to the left of {@code end}, "" is returned.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substring(null, *, *)    = null
	 * Strings.substring("", * ,  *)    = "";
	 * Strings.substring("abc", 0, 2)   = "ab"
	 * Strings.substring("abc", 2, 0)   = ""
	 * Strings.substring("abc", 2, 4)   = "c"
	 * Strings.substring("abc", 4, 6)   = ""
	 * Strings.substring("abc", 2, 2)   = ""
	 * Strings.substring("abc", -2, -1) = "b"
	 * Strings.substring("abc", -4, 2)  = "ab"
	 * </pre>
	 * 
	 * @param str the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the end of the String
	 *            by this many characters
	 * @param end the position to end at (exclusive), negative means count back from the end of the
	 *            String by this many characters
	 * @return substring from start position to end position, {@code null} if null String input
	 */
	public static String substring(final String str, int start, int end) {
		if (str == null) {
			return null;
		}

		// handle negatives
		if (end < 0) {
			end = str.length() + end; // remember end is negative
		}
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		// check length next
		if (end > str.length()) {
			end = str.length();
		}

		// if start is greater than end, return ""
		if (start > end) {
			return EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	// Left/Right/Mid
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the leftmost {@code len} characters of a String.
	 * </p>
	 * <p>
	 * If {@code len} characters are not available, or the String is {@code null}, the String will
	 * be returned without an exception. An empty String is returned if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * Strings.left(null, *)    = null
	 * Strings.left(*, -ve)     = ""
	 * Strings.left("", *)      = ""
	 * Strings.left("abc", 0)   = ""
	 * Strings.left("abc", 2)   = "ab"
	 * Strings.left("abc", 4)   = "abc"
	 * </pre>
	 * 
	 * @param str the String to get the leftmost characters from, may be null
	 * @param len the length of the required String
	 * @return the leftmost characters, {@code null} if null String input
	 */
	public static String left(final CharSequence str, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str.toString();
		}
		return str.subSequence(0, len).toString();
	}

	/**
	 * <p>
	 * Gets the rightmost {@code len} characters of a String.
	 * </p>
	 * <p>
	 * If {@code len} characters are not available, or the String is {@code null}, the String will
	 * be returned without an an exception. An empty String is returned if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * Strings.right(null, *)    = null
	 * Strings.right(*, -ve)     = ""
	 * Strings.right("", *)      = ""
	 * Strings.right("abc", 0)   = ""
	 * Strings.right("abc", 2)   = "bc"
	 * Strings.right("abc", 4)   = "abc"
	 * </pre>
	 * 
	 * @param str the String to get the rightmost characters from, may be null
	 * @param len the length of the required String
	 * @return the rightmost characters, {@code null} if null String input
	 */
	public static String right(final String str, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(str.length() - len);
	}

	/**
	 * <p>
	 * Gets {@code len} characters from the middle of a String.
	 * </p>
	 * <p>
	 * If {@code len} characters are not available, the remainder of the String will be returned
	 * without an exception. If the String is {@code null}, {@code null} will be returned. An empty
	 * String is returned if len is negative or exceeds the length of {@code str}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.mid(null, *, *)    = null
	 * Strings.mid(*, *, -ve)     = ""
	 * Strings.mid("", 0, *)      = ""
	 * Strings.mid("abc", 0, 2)   = "ab"
	 * Strings.mid("abc", 0, 4)   = "abc"
	 * Strings.mid("abc", 2, 4)   = "c"
	 * Strings.mid("abc", 4, 2)   = ""
	 * Strings.mid("abc", -2, 2)  = "ab"
	 * </pre>
	 * 
	 * @param str the String to get the characters from, may be null
	 * @param pos the position to start from, negative treated as zero
	 * @param len the length of the required String
	 * @return the middle characters, {@code null} if null String input
	 */
	public static String mid(final String str, int pos, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0 || pos > str.length()) {
			return EMPTY;
		}
		if (pos < 0) {
			pos = 0;
		}
		if (str.length() <= pos + len) {
			return str.substring(pos);
		}
		return str.substring(pos, pos + len);
	}

	// SubStringAfter/SubStringBefore
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the substring before the first occurrence of a separator. The separator is not returned.
	 * </p>
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input will return
	 * the empty string. A {@code null} separator will return the input string.
	 * </p>
	 * <p>
	 * If nothing is found, the string input is returned.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substringBefore(null, *)      = null
	 * Strings.substringBefore("", *)        = ""
	 * Strings.substringBefore("abc", "a")   = ""
	 * Strings.substringBefore("abcba", "b") = "a"
	 * Strings.substringBefore("abc", "c")   = "ab"
	 * Strings.substringBefore("abc", "d")   = "abc"
	 * Strings.substringBefore("abc", "")    = ""
	 * Strings.substringBefore("abc", null)  = "abc"
	 * </pre>
	 * 
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the first occurrence of the separator, {@code null} if null
	 *         String input
	 */
	public static String substringBefore(final String str, final String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String substringBefore(final String str, final char separator) {
		if (isEmpty(str)) {
			return str;
		}

		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Gets the substring after the first occurrence of a separator. The separator is not returned.
	 * </p>
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input will return
	 * the empty string. A {@code null} separator will return the empty string if the input string
	 * is not {@code null}.
	 * </p>
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substringAfter(null, *)      = null
	 * Strings.substringAfter("", *)        = ""
	 * Strings.substringAfter(*, null)      = ""
	 * Strings.substringAfter("abc", "a")   = "bc"
	 * Strings.substringAfter("abcba", "b") = "cba"
	 * Strings.substringAfter("abc", "c")   = ""
	 * Strings.substringAfter("abc", "d")   = ""
	 * Strings.substringAfter("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the first occurrence of the separator, {@code null} if null
	 *         String input
	 */
	public static String substringAfter(final String str, final String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	public static String substringAfter(final String str, final char separator) {
		if (isEmpty(str)) {
			return str;
		}
		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * <p>
	 * Gets the substring before the last occurrence of a separator. The separator is not returned.
	 * </p>
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input will return
	 * the empty string. An empty or {@code null} separator will return the input string.
	 * </p>
	 * <p>
	 * If nothing is found, the string input is returned.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substringBeforeLast(null, *)      = null
	 * Strings.substringBeforeLast("", *)        = ""
	 * Strings.substringBeforeLast("abcba", "b") = "abc"
	 * Strings.substringBeforeLast("abc", "c")   = "ab"
	 * Strings.substringBeforeLast("a", "a")     = ""
	 * Strings.substringBeforeLast("a", "z")     = "a"
	 * Strings.substringBeforeLast("a", null)    = "a"
	 * Strings.substringBeforeLast("a", "")      = "a"
	 * </pre>
	 * 
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the last occurrence of the separator, {@code null} if null
	 *         String input
	 */
	public static String substringBeforeLast(final String str, final String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String substringBeforeLast(final String str, final char separator) {
		if (isEmpty(str)) {
			return str;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Gets the substring after the last occurrence of a separator. The separator is not returned.
	 * </p>
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input will return
	 * the empty string. An empty or {@code null} separator will return the empty string if the
	 * input string is not {@code null}.
	 * </p>
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substringAfterLast(null, *)      = null
	 * Strings.substringAfterLast("", *)        = ""
	 * Strings.substringAfterLast(*, "")        = ""
	 * Strings.substringAfterLast(*, null)      = ""
	 * Strings.substringAfterLast("abc", "a")   = "bc"
	 * Strings.substringAfterLast("abcba", "b") = "a"
	 * Strings.substringAfterLast("abc", "c")   = ""
	 * Strings.substringAfterLast("a", "a")     = ""
	 * Strings.substringAfterLast("a", "z")     = ""
	 * </pre>
	 * 
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the last occurrence of the separator, {@code null} if null String
	 *         input
	 */
	public static String substringAfterLast(final String str, final String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return EMPTY;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	public static String substringAfterLast(final String str, final char separator) {
		if (isEmpty(str)) {
			return str;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND || pos == str.length() - 1) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	// Substring between
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the String that is nested in between two instances of the same String.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} tag returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substringBetween(null, *)            = null
	 * Strings.substringBetween("", "")             = ""
	 * Strings.substringBetween("", "tag")          = null
	 * Strings.substringBetween("tagabctag", null)  = null
	 * Strings.substringBetween("tagabctag", "")    = ""
	 * Strings.substringBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 * 
	 * @param str the String containing the substring, may be null
	 * @param tag the String before and after the substring, may be null
	 * @return the substring, {@code null} if no match
	 */
	public static String substringBetween(final String str, final String tag) {
		return substringBetween(str, tag, tag);
	}

	public static String substringBetween(final String str, final char tag) {
		return substringBetween(str, tag, tag);
	}

	/**
	 * <p>
	 * Gets the String that is nested in between two Strings. Only the first match is returned.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} open/close returns
	 * {@code null} (no match). An empty ("") open and close returns an empty string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substringBetween("wx[b]yz", "[", "]") = "b"
	 * Strings.substringBetween(null, *, *)          = null
	 * Strings.substringBetween(*, null, *)          = null
	 * Strings.substringBetween(*, *, null)          = null
	 * Strings.substringBetween("", "", "")          = ""
	 * Strings.substringBetween("", "", "]")         = null
	 * Strings.substringBetween("", "[", "]")        = null
	 * Strings.substringBetween("yabcz", "", "")     = ""
	 * Strings.substringBetween("yabcz", "y", "z")   = "abc"
	 * Strings.substringBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 * 
	 * @param str the String containing the substring, may be null
	 * @param open the String before the substring, may be null
	 * @param close the String after the substring, may be null
	 * @return the substring, {@code null} if no match
	 */
	public static String substringBetween(final String str, final String open, final String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		final int start = str.indexOf(open);
		if (start != INDEX_NOT_FOUND) {
			final int end = str.indexOf(close, start + open.length());
			if (end != INDEX_NOT_FOUND) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	public static String substringBetween(final String str, final char open, final char close) {
		if (str == null) {
			return null;
		}
		final int start = str.indexOf(open);
		if (start != INDEX_NOT_FOUND) {
			final int end = str.indexOf(close, start + 1);
			if (end != INDEX_NOT_FOUND) {
				return str.substring(start + 1, end);
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Searches a String for substrings delimited by a start and end tag, returning all matching
	 * substrings in an array.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} open/close returns
	 * {@code null} (no match). An empty ("") open/close returns {@code null} (no match).
	 * </p>
	 * 
	 * <pre>
	 * Strings.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
	 * Strings.substringsBetween(null, *, *)            = null
	 * Strings.substringsBetween(*, null, *)            = null
	 * Strings.substringsBetween(*, *, null)            = null
	 * Strings.substringsBetween("", "[", "]")          = []
	 * </pre>
	 * 
	 * @param str the String containing the substrings, null returns null, empty returns empty
	 * @param open the String identifying the start of the substring, empty returns null
	 * @param close the String identifying the end of the substring, empty returns null
	 * @return a String Array of substrings, or {@code null} if no match
	 */
	public static String[] substringsBetween(final String str, final String open, final String close) {
		if (str == null || isEmpty(open) || isEmpty(close)) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		final int closeLen = close.length();
		final int openLen = open.length();
		final List<String> list = new ArrayList<String>();
		int pos = 0;
		while (pos < strLen - closeLen) {
			int start = str.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			final int end = str.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.toArray(new String[list.size()]);
	}

	// Nested extraction
	// -----------------------------------------------------------------------

	// Splitting
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Splits the provided text into an array, using whitespace as the separator. Whitespace is
	 * defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as one separator. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.split(null)       = null
	 * Strings.split("")         = []
	 * Strings.split("abc def")  = ["abc", "def"]
	 * Strings.split("abc  def") = ["abc", "def"]
	 * Strings.split(" abc ")    = ["abc"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] split(final String str) {
		return split(str, null, -1);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator specified. This is an alternative to using
	 * StringTokenizer.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as one separator. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.split(null, *)         = null
	 * Strings.split("", *)           = []
	 * Strings.split("a.b.c", '.')    = ["a", "b", "c"]
	 * Strings.split("a..b.c", '.')   = ["a", "b", "c"]
	 * Strings.split("a:b:c", '.')    = ["a:b:c"]
	 * Strings.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @param separatorChar the character used as the delimiter
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] split(final String str, final char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separators specified. This is an alternative to using
	 * StringTokenizer.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as one separator. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
	 * whitespace.
	 * </p>
	 * 
	 * <pre>
	 * Strings.split(null, *)         = null
	 * Strings.split("", *)           = []
	 * Strings.split("abc def", null) = ["abc", "def"]
	 * Strings.split("abc def", " ")  = ["abc", "def"]
	 * Strings.split("abc  def", " ") = ["abc", "def"]
	 * Strings.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @param separatorChars the characters used as the delimiters, {@code null} splits on
	 *            whitespace
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] split(final String str, final String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array with a maximum length, separators specified.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as one separator.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
	 * whitespace.
	 * </p>
	 * <p>
	 * If more than {@code max} delimited substrings are found, the last returned string includes
	 * all characters after the first {@code max - 1} returned strings (including separator
	 * characters).
	 * </p>
	 * 
	 * <pre>
	 * Strings.split(null, *, *)            = null
	 * Strings.split("", *, *)              = []
	 * Strings.split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
	 * Strings.split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
	 * Strings.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
	 * Strings.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @param separatorChars the characters used as the delimiters, {@code null} splits on
	 *            whitespace
	 * @param max the maximum number of elements to include in the array. A zero or negative value
	 *            implies no limit
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] split(final String str, final String separatorChars, final int max) {
		return splitWorker(str, separatorChars, max, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator string specified.
	 * </p>
	 * <p>
	 * The separator(s) will not be included in the returned String array. Adjacent separators are
	 * treated as one separator.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separator splits on
	 * whitespace.
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitByWholeSeparator(null, *)               = null
	 * Strings.splitByWholeSeparator("", *)                 = []
	 * Strings.splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
	 * Strings.splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
	 * Strings.splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
	 * Strings.splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @param separator String containing the String to be used as a delimiter, {@code null} splits
	 *            on whitespace
	 * @return an array of parsed Strings, {@code null} if null String was input
	 */
	public static String[] splitByWholeSeparator(final String str, final String separator) {
		return splitByWholeSeparatorWorker(str, separator, -1, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator string specified. Returns a maximum of
	 * {@code max} substrings.
	 * </p>
	 * <p>
	 * The separator(s) will not be included in the returned String array. Adjacent separators are
	 * treated as one separator.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separator splits on
	 * whitespace.
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitByWholeSeparator(null, *, *)               = null
	 * Strings.splitByWholeSeparator("", *, *)                 = []
	 * Strings.splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
	 * Strings.splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
	 * Strings.splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
	 * Strings.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
	 * Strings.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @param separator String containing the String to be used as a delimiter, {@code null} splits
	 *            on whitespace
	 * @param max the maximum number of elements to include in the returned array. A zero or
	 *            negative value implies no limit.
	 * @return an array of parsed Strings, {@code null} if null String was input
	 */
	public static String[] splitByWholeSeparator(final String str, final String separator, final int max) {
		return splitByWholeSeparatorWorker(str, separator, max, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator string specified.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as separators for empty tokens. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separator splits on
	 * whitespace.
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitByWholeSeparatorPreserveAllTokens(null, *)               = null
	 * Strings.splitByWholeSeparatorPreserveAllTokens("", *)                 = []
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab de fg", null)      = ["ab", "de", "fg"]
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null)    = ["ab", "", "", "de", "fg"]
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @param separator String containing the String to be used as a delimiter, {@code null} splits
	 *            on whitespace
	 * @return an array of parsed Strings, {@code null} if null String was input
	 */
	public static String[] splitByWholeSeparatorPreserveAllTokens(final String str, final String separator) {
		return splitByWholeSeparatorWorker(str, separator, -1, true);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator string specified. Returns a maximum of
	 * {@code max} substrings.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as separators for empty tokens. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separator splits on
	 * whitespace.
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitByWholeSeparatorPreserveAllTokens(null, *, *)               = null
	 * Strings.splitByWholeSeparatorPreserveAllTokens("", *, *)                 = []
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab de fg", null, 0)      = ["ab", "de", "fg"]
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null, 0)    = ["ab", "", "", "de", "fg"]
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
	 * Strings.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @param separator String containing the String to be used as a delimiter, {@code null} splits
	 *            on whitespace
	 * @param max the maximum number of elements to include in the returned array. A zero or
	 *            negative value implies no limit.
	 * @return an array of parsed Strings, {@code null} if null String was input
	 */
	public static String[] splitByWholeSeparatorPreserveAllTokens(final String str, final String separator,
			final int max) {
		return splitByWholeSeparatorWorker(str, separator, max, true);
	}

	/**
	 * Performs the logic for the {@code splitByWholeSeparatorPreserveAllTokens} methods.
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separator String containing the String to be used as a delimiter, {@code null} splits
	 *            on whitespace
	 * @param max the maximum number of elements to include in the returned array. A zero or
	 *            negative value implies no limit.
	 * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token
	 *            separators; if {@code false}, adjacent separators are treated as one separator.
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitByWholeSeparatorWorker(final String str, final String separator, final int max,
			final boolean preserveAllTokens) {
		if (str == null) {
			return null;
		}

		final int len = str.length();

		if (len == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}

		if (separator == null || EMPTY.equals(separator)) {
			// Split on whitespace.
			return splitWorker(str, null, max, preserveAllTokens);
		}

		final int separatorLength = separator.length();

		final ArrayList<String> substrings = new ArrayList<String>();
		int numberOfSubstrings = 0;
		int beg = 0;
		int end = 0;
		while (end < len) {
			end = str.indexOf(separator, beg);

			if (end > -1) {
				if (end > beg) {
					numberOfSubstrings += 1;

					if (numberOfSubstrings == max) {
						end = len;
						substrings.add(str.substring(beg));
					}
					else {
						// The following is OK, because String.substring( beg, end ) excludes
						// the character at the position 'end'.
						substrings.add(str.substring(beg, end));

						// Set the starting point for the next search.
						// The following is equivalent to beg = end + (separatorLength - 1) + 1,
						// which is the right calculation:
						beg = end + separatorLength;
					}
				}
				else {
					// We found a consecutive occurrence of the separator, so skip it.
					if (preserveAllTokens) {
						numberOfSubstrings += 1;
						if (numberOfSubstrings == max) {
							end = len;
							substrings.add(str.substring(beg));
						}
						else {
							substrings.add(EMPTY);
						}
					}
					beg = end + separatorLength;
				}
			}
			else {
				// String.substring( beg ) goes from 'beg' to the end of the String.
				substrings.add(str.substring(beg));
				end = len;
			}
		}

		return substrings.toArray(new String[substrings.size()]);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Splits the provided text into an array, using whitespace as the separator, preserving all
	 * tokens, including empty tokens created by adjacent separators. This is an alternative to
	 * using StringTokenizer. Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as separators for empty tokens. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitPreserveAllTokens(null)       = null
	 * Strings.splitPreserveAllTokens("")         = []
	 * Strings.splitPreserveAllTokens("abc def")  = ["abc", "def"]
	 * Strings.splitPreserveAllTokens("abc  def") = ["abc", "", "def"]
	 * Strings.splitPreserveAllTokens(" abc ")    = ["", "abc", ""]
	 * </pre>
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] splitPreserveAllTokens(final String str) {
		return splitWorker(str, null, -1, true);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator specified, preserving all tokens, including
	 * empty tokens created by adjacent separators. This is an alternative to using StringTokenizer.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as separators for empty tokens. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitPreserveAllTokens(null, *)         = null
	 * Strings.splitPreserveAllTokens("", *)           = []
	 * Strings.splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
	 * Strings.splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
	 * Strings.splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
	 * Strings.splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
	 * Strings.splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
	 * Strings.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
	 * Strings.splitPreserveAllTokens("a b c  ", ' ')   = ["a", "b", "c", "", ""]
	 * Strings.splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
	 * Strings.splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
	 * Strings.splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
	 * </pre>
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChar the character used as the delimiter, {@code null} splits on whitespace
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] splitPreserveAllTokens(final String str, final char separatorChar) {
		return splitWorker(str, separatorChar, true);
	}

	/**
	 * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods that do
	 * not return a maximum array length.
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChar the separate character
	 * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token
	 *            separators; if {@code false}, adjacent separators are treated as one separator.
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitWorker(final String str, final char separatorChar, final boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		final int len = str.length();
		if (len == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		final List<String> list = new ArrayList<String>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			}
			lastMatch = false;
			match = true;
			i++;
		}
		if (match || preserveAllTokens && lastMatch) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separators specified, preserving all tokens,
	 * including empty tokens created by adjacent separators. This is an alternative to using
	 * StringTokenizer.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as separators for empty tokens. For more control over the split use the StrTokenizer class.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
	 * whitespace.
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitPreserveAllTokens(null, *)           = null
	 * Strings.splitPreserveAllTokens("", *)             = []
	 * Strings.splitPreserveAllTokens("abc def", null)   = ["abc", "def"]
	 * Strings.splitPreserveAllTokens("abc def", " ")    = ["abc", "def"]
	 * Strings.splitPreserveAllTokens("abc  def", " ")   = ["abc", "", def"]
	 * Strings.splitPreserveAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
	 * Strings.splitPreserveAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
	 * Strings.splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
	 * Strings.splitPreserveAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
	 * Strings.splitPreserveAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
	 * Strings.splitPreserveAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
	 * Strings.splitPreserveAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
	 * </pre>
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChars the characters used as the delimiters, {@code null} splits on
	 *            whitespace
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] splitPreserveAllTokens(final String str, final String separatorChars) {
		return splitWorker(str, separatorChars, -1, true);
	}

	/**
	 * <p>
	 * Splits the provided text into an array with a maximum length, separators specified,
	 * preserving all tokens, including empty tokens created by adjacent separators.
	 * </p>
	 * <p>
	 * The separator is not included in the returned String array. Adjacent separators are treated
	 * as separators for empty tokens. Adjacent separators are treated as one separator.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
	 * whitespace.
	 * </p>
	 * <p>
	 * If more than {@code max} delimited substrings are found, the last returned string includes
	 * all characters after the first {@code max - 1} returned strings (including separator
	 * characters).
	 * </p>
	 * 
	 * <pre>
	 * Strings.splitPreserveAllTokens(null, *, *)            = null
	 * Strings.splitPreserveAllTokens("", *, *)              = []
	 * Strings.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
	 * Strings.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
	 * Strings.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
	 * Strings.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
	 * Strings.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
	 * Strings.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
	 * Strings.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChars the characters used as the delimiters, {@code null} splits on
	 *            whitespace
	 * @param max the maximum number of elements to include in the array. A zero or negative value
	 *            implies no limit
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] splitPreserveAllTokens(final String str, final String separatorChars, final int max) {
		return splitWorker(str, separatorChars, max, true);
	}

	/**
	 * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods that
	 * return a maximum array length.
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChars the separate character
	 * @param max the maximum number of elements to include in the array. A zero or negative value
	 *            implies no limit.
	 * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token
	 *            separators; if {@code false}, adjacent separators are treated as one separator.
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitWorker(final String str, final String separatorChars, final int max,
			final boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		final int len = str.length();
		if (len == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		final List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			final char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || preserveAllTokens && lastMatch) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * <p>
	 * Splits a String by Character type as returned by {@code java.lang.Character.getType(char)}.
	 * Groups of contiguous characters of the same type are returned as complete tokens.
	 * 
	 * <pre>
	 * Strings.splitByCharacterType(null)         = null
	 * Strings.splitByCharacterType("")           = []
	 * Strings.splitByCharacterType("ab de fg")   = ["ab", " ", "de", " ", "fg"]
	 * Strings.splitByCharacterType("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
	 * Strings.splitByCharacterType("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
	 * Strings.splitByCharacterType("number5")    = ["number", "5"]
	 * Strings.splitByCharacterType("fooBar")     = ["foo", "B", "ar"]
	 * Strings.splitByCharacterType("foo200Bar")  = ["foo", "200", "B", "ar"]
	 * Strings.splitByCharacterType("ASFRules")   = ["ASFR", "ules"]
	 * </pre>
	 * 
	 * @param str the String to split, may be {@code null}
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] splitByCharacterType(final String str) {
		return splitByCharacterType(str, false);
	}

	/**
	 * <p>
	 * Splits a String by Character type as returned by {@code java.lang.Character.getType(char)}.
	 * Groups of contiguous characters of the same type are returned as complete tokens, with the
	 * following exception: the character of type {@code Character.UPPERCASE_LETTER}, if any,
	 * immediately preceding a token of type {@code Character.LOWERCASE_LETTER} will belong to the
	 * following token rather than to the preceding, if any, {@code Character.UPPERCASE_LETTER}
	 * token.
	 * 
	 * <pre>
	 * Strings.splitByCharacterTypeCamelCase(null)         = null
	 * Strings.splitByCharacterTypeCamelCase("")           = []
	 * Strings.splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
	 * Strings.splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
	 * Strings.splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
	 * Strings.splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
	 * Strings.splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
	 * Strings.splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
	 * Strings.splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
	 * </pre>
	 * 
	 * @param str the String to split, may be {@code null}
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	public static String[] splitByCharacterTypeCamelCase(final String str) {
		return splitByCharacterType(str, true);
	}

	/**
	 * <p>
	 * Splits a String by Character type as returned by {@code java.lang.Character.getType(char)}.
	 * Groups of contiguous characters of the same type are returned as complete tokens, with the
	 * following exception: if {@code camelCase} is {@code true}, the character of type
	 * {@code Character.UPPERCASE_LETTER}, if any, immediately preceding a token of type
	 * {@code Character.LOWERCASE_LETTER} will belong to the following token rather than to the
	 * preceding, if any, {@code Character.UPPERCASE_LETTER} token.
	 * 
	 * @param str the String to split, may be {@code null}
	 * @param camelCase whether to use so-called "camel-case" for letter types
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitByCharacterType(final String str, final boolean camelCase) {
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		final char[] c = str.toCharArray();
		final List<String> list = new ArrayList<String>();
		int tokenStart = 0;
		int currentType = Character.getType(c[tokenStart]);
		for (int pos = tokenStart + 1; pos < c.length; pos++) {
			final int type = Character.getType(c[pos]);
			if (type == currentType) {
				continue;
			}
			if (camelCase && type == Character.LOWERCASE_LETTER && currentType == Character.UPPERCASE_LETTER) {
				final int newTokenStart = pos - 1;
				if (newTokenStart != tokenStart) {
					list.add(new String(c, tokenStart, newTokenStart - tokenStart));
					tokenStart = newTokenStart;
				}
			}
			else {
				list.add(new String(c, tokenStart, pos - tokenStart));
				tokenStart = pos;
			}
			currentType = type;
		}
		list.add(new String(c, tokenStart, c.length - tokenStart));
		return list.toArray(new String[list.size()]);
	}

	// Joining
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No separator is added to the joined String. Null objects or empty strings within the array
	 * are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null)            = null
	 * Strings.join([])              = ""
	 * Strings.join([null])          = ""
	 * Strings.join(["a", "b", "c"]) = "abc"
	 * Strings.join([null, "", "a"]) = "a"
	 * </pre>
	 * 
	 * @param <T> the specific type of values to join together
	 * @param elements the values to join together, may be null
	 * @return the joined String, {@code null} if null array input
	 */
	public static <T> String join(final T... elements) {
		return join(elements, null);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join(["a", "b", "c"], ';')  = "a;b;c"
	 * Strings.join(["a", "b", "c"], null) = "abc"
	 * Strings.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final Object[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final long[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final int[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final short[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final byte[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final char[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final float[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final double[] array, final char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join(["a", "b", "c"], ';')  = "a;b;c"
	 * Strings.join(["a", "b", "c"], null) = "abc"
	 * Strings.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final Object[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final long[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final int[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final byte[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final short[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final char[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final double[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = null
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join([1, 2, 3], ';')  = "1;2;3"
	 * Strings.join([1, 2, 3], null) = "123"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final float[] array, final char separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the same as an
	 * empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)                = null
	 * Strings.join([], *)                  = ""
	 * Strings.join([null], *)              = ""
	 * Strings.join(["a", "b", "c"], "--")  = "a--b--c"
	 * Strings.join(["a", "b", "c"], null)  = "abc"
	 * Strings.join(["a", "b", "c"], "")    = "abc"
	 * Strings.join([null, "", "a"], ',')   = ",,a"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, {@code null} if null array input
	 */
	public static String join(final Object[] array, final String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the same as an
	 * empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *, *, *)                = null
	 * Strings.join([], *, *, *)                  = ""
	 * Strings.join([null], *, *, *)              = ""
	 * Strings.join(["a", "b", "c"], "--", 0, 3)  = "a--b--c"
	 * Strings.join(["a", "b", "c"], "--", 1, 3)  = "b--c"
	 * Strings.join(["a", "b", "c"], "--", 2, 3)  = "c"
	 * Strings.join(["a", "b", "c"], "--", 2, 2)  = ""
	 * Strings.join(["a", "b", "c"], null, 0, 3)  = "abc"
	 * Strings.join(["a", "b", "c"], "", 0, 3)    = "abc"
	 * Strings.join([null, "", "a"], ',', 0, 3)   = ",,a"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from.
	 * @param endIndex the index to stop joining from (exclusive).
	 * @return the joined String, {@code null} if null array input; or the empty string if
	 *         {@code endIndex - startIndex <= 0}. The number of joined entries is given by
	 *         {@code endIndex - startIndex}
	 * @throws ArrayIndexOutOfBoundsException ife<br/>
	 *             {@code startIndex < 0} or <br/>
	 *             {@code startIndex >= array.length()} or <br/>
	 *             {@code endIndex < 0} or <br/>
	 *             {@code endIndex > array.length()}
	 */
	public static String join(final Object[] array, String separator, final int startIndex, final int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		final int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}

		final StringBuilder buf = new StringBuilder(noOfItems * 16);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterator} into a single String containing the
	 * provided elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * iteration are represented by empty strings.
	 * </p>
	 * <p>
	 * See the examples here: {@link #join(Object[],char)}.
	 * </p>
	 * 
	 * @param iterator the {@code Iterator} of values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(final Iterator<?> iterator, final char separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return EMPTY;
		}
		final Object first = iterator.next();
		if (!iterator.hasNext()) {
			return Objects.toString(first);
		}

		// two or more elements
		final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			buf.append(separator);
			final Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}

		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterable} into a single String containing the
	 * provided elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the
	 * iteration are represented by empty strings.
	 * </p>
	 * <p>
	 * See the examples here: {@link #join(Object[],char)}.
	 * </p>
	 * 
	 * @param iterable the {@code Iterable} providing the values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(final Iterable<?> iterable, final char separator) {
		if (iterable == null) {
			return null;
		}
		return join(iterable.iterator(), separator);
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterable} into a single String containing the
	 * provided elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the same as an
	 * empty String ("").
	 * </p>
	 * <p>
	 * See the examples here: {@link #join(Object[],String)}.
	 * </p>
	 * 
	 * @param iterable the {@code Iterable} providing the values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(final Iterable<?> iterable, final String separator) {
		if (iterable == null) {
			return null;
		}
		return join(iterable.iterator(), separator);
	}

	// Delete
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Deletes all whitespaces from a String as defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.deleteWhitespace(null)         = null
	 * Strings.deleteWhitespace("")           = ""
	 * Strings.deleteWhitespace("abc")        = "abc"
	 * Strings.deleteWhitespace("   ab  c  ") = "abc"
	 * </pre>
	 * 
	 * @param str the String to delete whitespace from, may be null
	 * @return the String without whitespaces, {@code null} if null String input
	 */
	public static String deleteWhitespace(final String str) {
		if (isEmpty(str)) {
			return str;
		}
		final int sz = str.length();
		final char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}
		return new String(chs, 0, count);
	}

	// Remove
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Removes a substring only if it is at the beginning of a source string, otherwise returns the
	 * source string.
	 * </p>
	 * <p>
	 * A {@code null} source string will return {@code null}. An empty ("") source string will
	 * return the empty string. A {@code null} search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeStart(null, *)      = null
	 * Strings.removeStart("", *)        = ""
	 * Strings.removeStart(*, null)      = *
	 * Strings.removeStart("www.domain.com", "www.")   = "domain.com"
	 * Strings.removeStart("domain.com", "www.")       = "domain.com"
	 * Strings.removeStart("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeStart("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param str the source String to search, may be null
	 * @param remove the String to search for and remove, may be null
	 * @return the substring with the string removed if found, {@code null} if null String input
	 */
	public static String removeStart(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.startsWith(remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Case insensitive removal of a substring if it is at the beginning of a source string,
	 * otherwise returns the source string.
	 * </p>
	 * <p>
	 * A {@code null} source string will return {@code null}. An empty ("") source string will
	 * return the empty string. A {@code null} search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeStartIgnoreCase(null, *)      = null
	 * Strings.removeStartIgnoreCase("", *)        = ""
	 * Strings.removeStartIgnoreCase(*, null)      = *
	 * Strings.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
	 * Strings.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
	 * Strings.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
	 * Strings.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeStartIgnoreCase("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param str the source String to search, may be null
	 * @param remove the String to search for (case insensitive) and remove, may be null
	 * @return the substring with the string removed if found, {@code null} if null String input
	 */
	public static String removeStartIgnoreCase(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (startsWithIgnoreCase(str, remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Removes a substring only if it is at the end of a source string, otherwise returns the source
	 * string.
	 * </p>
	 * <p>
	 * A {@code null} source string will return {@code null}. An empty ("") source string will
	 * return the empty string. A {@code null} search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeEnd(null, *)      = null
	 * Strings.removeEnd("", *)        = ""
	 * Strings.removeEnd(*, null)      = *
	 * Strings.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
	 * Strings.removeEnd("www.domain.com", ".com")   = "www.domain"
	 * Strings.removeEnd("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeEnd("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param str the source String to search, may be null
	 * @param remove the String to search for and remove, may be null
	 * @return the substring with the string removed if found, {@code null} if null String input
	 */
	public static String removeEnd(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.endsWith(remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Case insensitive removal of a substring if it is at the end of a source string, otherwise
	 * returns the source string.
	 * </p>
	 * <p>
	 * A {@code null} source string will return {@code null}. An empty ("") source string will
	 * return the empty string. A {@code null} search string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeEndIgnoreCase(null, *)      = null
	 * Strings.removeEndIgnoreCase("", *)        = ""
	 * Strings.removeEndIgnoreCase(*, null)      = *
	 * Strings.removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
	 * Strings.removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
	 * Strings.removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeEndIgnoreCase("abc", "")    = "abc"
	 * Strings.removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
	 * Strings.removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
	 * </pre>
	 * 
	 * @param str the source String to search, may be null
	 * @param remove the String to search for (case insensitive) and remove, may be null
	 * @return the substring with the string removed if found, {@code null} if null String input
	 */
	public static String removeEndIgnoreCase(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (endsWithIgnoreCase(str, remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * <p>
	 * Removes all occurrences of a substring from within the source string.
	 * </p>
	 * <p>
	 * A {@code null} source string will return {@code null}. An empty ("") source string will
	 * return the empty string. A {@code null} remove string will return the source string. An empty
	 * ("") remove string will return the source string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.remove(null, *)        = null
	 * Strings.remove("", *)          = ""
	 * Strings.remove(*, null)        = *
	 * Strings.remove(*, "")          = *
	 * Strings.remove("queued", "ue") = "qd"
	 * Strings.remove("queued", "zz") = "queued"
	 * </pre>
	 * 
	 * @param str the source String to search, may be null
	 * @param remove the String to search for and remove, may be null
	 * @return the substring with the string removed if found, {@code null} if null String input
	 */
	public static String remove(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		return replace(str, remove, EMPTY, -1);
	}

	/**
	 * <p>
	 * Removes all occurrences of a character from within the source string.
	 * </p>
	 * <p>
	 * A {@code null} source string will return {@code null}. An empty ("") source string will
	 * return the empty string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.remove(null, *)       = null
	 * Strings.remove("", *)         = ""
	 * Strings.remove("queued", 'u') = "qeed"
	 * Strings.remove("queued", 'z') = "queued"
	 * </pre>
	 * 
	 * @param str the source String to search, may be null
	 * @param remove the char to search for and remove, may be null
	 * @return the substring with the char removed if found, {@code null} if null String input
	 */
	public static String remove(final String str, final char remove) {
		if (isEmpty(str) || str.indexOf(remove) == INDEX_NOT_FOUND) {
			return str;
		}
		final char[] chars = str.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != remove) {
				chars[pos++] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	// Replacing
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, once.
	 * </p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replaceOnce(null, *, *)        = null
	 * Strings.replaceOnce("", *, *)          = ""
	 * Strings.replaceOnce("any", null, *)    = "any"
	 * Strings.replaceOnce("any", *, null)    = "any"
	 * Strings.replaceOnce("any", "", *)      = "any"
	 * Strings.replaceOnce("aba", "a", null)  = "aba"
	 * Strings.replaceOnce("aba", "a", "")    = "ba"
	 * Strings.replaceOnce("aba", "a", "z")   = "zba"
	 * </pre>
	 * 
	 * @see #replace(String text, String searchString, String replacement, int max)
	 * @param text text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement the String to replace with, may be null
	 * @return the text with any replacements processed, {@code null} if null String input
	 */
	public static String replaceOnce(final String text, final String searchString, final String replacement) {
		return replace(text, searchString, replacement, 1);
	}

	/**
	 * Replaces each substring of the source String that matches the given regular expression with
	 * the given replacement using the {@link Pattern#DOTALL} option. DOTALL is also know as
	 * single-line mode in Perl. This call is also equivalent to:
	 * <ul>
	 * <li>{@code source.replaceAll(&quot;(?s)&quot; + regex, replacement)}</li>
	 * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement)}</li>
	 * </ul>
	 * 
	 * @param source the source string
	 * @param regex the regular expression to which this string is to be matched
	 * @param replacement the string to be substituted for each match
	 * @return The resulting {@code String}
	 * @see String#replaceAll(String, String)
	 * @see Pattern#DOTALL
	 */
	public static String replacePattern(final String source, final String regex, final String replacement) {
		return Pattern.compile(regex, Pattern.DOTALL).matcher(source).replaceAll(replacement);
	}

	/**
	 * Removes each substring of the source String that matches the given regular expression using
	 * the DOTALL option.
	 * 
	 * @param source the source string
	 * @param regex the regular expression to which this string is to be matched
	 * @return The resulting {@code String}
	 * @see String#replaceAll(String, String)
	 * @see Pattern#DOTALL
	 */
	public static String removePattern(final String source, final String regex) {
		return replacePattern(source, regex, Strings.EMPTY);
	}

	/**
	 * <p>
	 * Replaces all occurrences of a String within another String.
	 * </p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replace(null, *, *)        = null
	 * Strings.replace("", *, *)          = ""
	 * Strings.replace("any", null, *)    = "any"
	 * Strings.replace("any", *, null)    = "any"
	 * Strings.replace("any", "", *)      = "any"
	 * Strings.replace("aba", "a", null)  = "aba"
	 * Strings.replace("aba", "a", "")    = "b"
	 * Strings.replace("aba", "a", "z")   = "zbz"
	 * </pre>
	 * 
	 * @see #replace(String text, String searchString, String replacement, int max)
	 * @param text text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement the String to replace it with, may be null
	 * @return the text with any replacements processed, {@code null} if null String input
	 */
	public static String replace(final String text, final String searchString, final String replacement) {
		return replace(text, searchString, replacement, -1);
	}

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, for the first {@code max}
	 * values of the search String.
	 * </p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replace(null, *, *, *)         = null
	 * Strings.replace("", *, *, *)           = ""
	 * Strings.replace("any", null, *, *)     = "any"
	 * Strings.replace("any", *, null, *)     = "any"
	 * Strings.replace("any", "", *, *)       = "any"
	 * Strings.replace("any", *, *, 0)        = "any"
	 * Strings.replace("abaa", "a", null, -1) = "abaa"
	 * Strings.replace("abaa", "a", "", -1)   = "b"
	 * Strings.replace("abaa", "a", "z", 0)   = "abaa"
	 * Strings.replace("abaa", "a", "z", 1)   = "zbaa"
	 * Strings.replace("abaa", "a", "z", 2)   = "zbza"
	 * Strings.replace("abaa", "a", "z", -1)  = "zbzz"
	 * </pre>
	 * 
	 * @param text text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement the String to replace it with, may be null
	 * @param max maximum number of values to replace, or {@code -1} if no maximum
	 * @return the text with any replacements processed, {@code null} if null String input
	 */
	public static String replace(final String text, final String searchString, final String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == INDEX_NOT_FOUND) {
			return text;
		}
		final int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != INDEX_NOT_FOUND) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * <p>
	 * Replaces all occurrences of Strings within another String.
	 * </p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op, or if any "search string" or
	 * "string to replace" is null, that replace will be ignored. This will not repeat. For
	 * repeating replaces, call the overloaded method.
	 * </p>
	 * 
	 * <pre>
	 *  Strings.replaceEach(null, *, *)        = null
	 *  Strings.replaceEach("", *, *)          = ""
	 *  Strings.replaceEach("aba", null, null) = "aba"
	 *  Strings.replaceEach("aba", new String[0], null) = "aba"
	 *  Strings.replaceEach("aba", null, new String[0]) = "aba"
	 *  Strings.replaceEach("aba", new String[]{"a"}, null)  = "aba"
	 *  Strings.replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
	 *  Strings.replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
	 *  (example of how it does not repeat)
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
	 * </pre>
	 * 
	 * @param text text to search and replace in, no-op if null
	 * @param searchList the Strings to search for, no-op if null
	 * @param replacementList the Strings to replace them with, no-op if null
	 * @return the text with any replacements processed, {@code null} if null String input
	 * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
	 *             and/or size 0)
	 */
	public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
		return replaceEach(text, searchList, replacementList, false, 0);
	}

	/**
	 * <p>
	 * Replaces all occurrences of Strings within another String.
	 * </p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op, or if any "search string" or
	 * "string to replace" is null, that replace will be ignored.
	 * </p>
	 * 
	 * <pre>
	 *  Strings.replaceEach(null, *, *, *) = null
	 *  Strings.replaceEach("", *, *, *) = ""
	 *  Strings.replaceEach("aba", null, null, *) = "aba"
	 *  Strings.replaceEach("aba", new String[0], null, *) = "aba"
	 *  Strings.replaceEach("aba", null, new String[0], *) = "aba"
	 *  Strings.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
	 *  Strings.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
	 *  Strings.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
	 *  (example of how it repeats)
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, true) = IllegalStateException
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, false) = "dcabe"
	 * </pre>
	 * 
	 * @param text text to search and replace in, no-op if null
	 * @param searchList the Strings to search for, no-op if null
	 * @param replacementList the Strings to replace them with, no-op if null
	 * @return the text with any replacements processed, {@code null} if null String input
	 * @throws IllegalStateException if the search is repeating and there is an endless loop due to
	 *             outputs of one being inputs to another
	 * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
	 *             and/or size 0)
	 */
	public static String replaceEachRepeatedly(final String text, final String[] searchList,
			final String[] replacementList) {
		// timeToLive should be 0 if not used or nothing to replace, else it's
		// the length of the replace array
		final int timeToLive = searchList == null ? 0 : searchList.length;
		return replaceEach(text, searchList, replacementList, true, timeToLive);
	}

	/**
	 * <p>
	 * Replaces all occurrences of Strings within another String.
	 * </p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op, or if any "search string" or
	 * "string to replace" is null, that replace will be ignored.
	 * </p>
	 * 
	 * <pre>
	 *  Strings.replaceEach(null, *, *, *) = null
	 *  Strings.replaceEach("", *, *, *) = ""
	 *  Strings.replaceEach("aba", null, null, *) = "aba"
	 *  Strings.replaceEach("aba", new String[0], null, *) = "aba"
	 *  Strings.replaceEach("aba", null, new String[0], *) = "aba"
	 *  Strings.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
	 *  Strings.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
	 *  Strings.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
	 *  (example of how it repeats)
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
	 *  Strings.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalStateException
	 * </pre>
	 * 
	 * @param text text to search and replace in, no-op if null
	 * @param searchList the Strings to search for, no-op if null
	 * @param replacementList the Strings to replace them with, no-op if null
	 * @param repeat if true, then replace repeatedly until there are no more possible replacements
	 *            or timeToLive < 0
	 * @param timeToLive if less than 0 then there is a circular reference and endless loop
	 * @return the text with any replacements processed, {@code null} if null String input
	 * @throws IllegalStateException if the search is repeating and there is an endless loop due to
	 *             outputs of one being inputs to another
	 * @throws IllegalArgumentException if the lengths of the arrays are not the same (null is ok,
	 *             and/or size 0)
	 */
	private static String replaceEach(final String text, final String[] searchList, final String[] replacementList,
			final boolean repeat, final int timeToLive) {

		// mchyzer Performance note: This creates very few new objects (one major goal)
		// let me know if there are performance requests, we can create a harness to measure

		if (text == null || text.length() == 0 || searchList == null || searchList.length == 0
				|| replacementList == null || replacementList.length == 0) {
			return text;
		}

		// if recursing, this shouldn't be less than 0
		if (timeToLive < 0) {
			throw new IllegalStateException("Aborting to protect against StackOverflowError - "
					+ "output of one loop is the input of another");
		}

		final int searchLength = searchList.length;
		final int replacementLength = replacementList.length;

		// make sure lengths are ok, these need to be equal
		if (searchLength != replacementLength) {
			throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs "
					+ replacementLength);
		}

		// keep track of which still have matches
		final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

		// index on index that the match was found
		int textIndex = -1;
		int replaceIndex = -1;
		int tempIndex = -1;

		// index of replace array that will replace the search string found
		// NOTE: logic duplicated below START
		for (int i = 0; i < searchLength; i++) {
			if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
					|| replacementList[i] == null) {
				continue;
			}
			tempIndex = text.indexOf(searchList[i]);

			// see if we need to keep searching for this
			if (tempIndex == -1) {
				noMoreMatchesForReplIndex[i] = true;
			}
			else {
				if (textIndex == -1 || tempIndex < textIndex) {
					textIndex = tempIndex;
					replaceIndex = i;
				}
			}
		}
		// NOTE: logic mostly below END

		// no search strings found, we are done
		if (textIndex == -1) {
			return text;
		}

		int start = 0;

		// get a good guess on the size of the result buffer so it doesn't have to double if it goes
		// over a bit
		int increase = 0;

		// count the replacement text elements that are larger than their corresponding text being
		// replaced
		for (int i = 0; i < searchList.length; i++) {
			if (searchList[i] == null || replacementList[i] == null) {
				continue;
			}
			final int greater = replacementList[i].length() - searchList[i].length();
			if (greater > 0) {
				increase += 3 * greater; // assume 3 matches
			}
		}
		// have upper-bound at 20% increase, then let Java take over
		increase = Math.min(increase, text.length() / 5);

		final StringBuilder buf = new StringBuilder(text.length() + increase);

		while (textIndex != -1) {

			for (int i = start; i < textIndex; i++) {
				buf.append(text.charAt(i));
			}
			buf.append(replacementList[replaceIndex]);

			start = textIndex + searchList[replaceIndex].length();

			textIndex = -1;
			replaceIndex = -1;
			tempIndex = -1;
			// find the next earliest match
			// NOTE: logic mostly duplicated above START
			for (int i = 0; i < searchLength; i++) {
				if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
						|| replacementList[i] == null) {
					continue;
				}
				tempIndex = text.indexOf(searchList[i], start);

				// see if we need to keep searching for this
				if (tempIndex == -1) {
					noMoreMatchesForReplIndex[i] = true;
				}
				else {
					if (textIndex == -1 || tempIndex < textIndex) {
						textIndex = tempIndex;
						replaceIndex = i;
					}
				}
			}
			// NOTE: logic duplicated above END

		}
		final int textLength = text.length();
		for (int i = start; i < textLength; i++) {
			buf.append(text.charAt(i));
		}
		final String result = buf.toString();
		if (!repeat) {
			return result;
		}

		return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
	}

	// Replace, character based
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Replaces all occurrences of a character in a String with another. This is a null-safe version
	 * of {@link String#replace(char, char)}.
	 * </p>
	 * <p>
	 * A {@code null} string input returns {@code null}. An empty ("") string input returns an empty
	 * string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replaceChars(null, *, *)        = null
	 * Strings.replaceChars("", *, *)          = ""
	 * Strings.replaceChars("abcba", 'b', 'y') = "aycya"
	 * Strings.replaceChars("abcba", 'z', 'y') = "abcba"
	 * </pre>
	 * 
	 * @param str String to replace characters in, may be null
	 * @param searchChar the character to search for, may be null
	 * @param replaceChar the character to replace, may be null
	 * @return modified String, {@code null} if null string input
	 */
	public static String replaceChars(final String str, final char searchChar, final char replaceChar) {
		if (str == null) {
			return null;
		}
		return str.replace(searchChar, replaceChar);
	}

	/**
	 * <p>
	 * Replaces multiple characters in a String in one go. This method can also be used to delete
	 * characters.
	 * </p>
	 * <p>
	 * For example:<br />
	 * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>.
	 * </p>
	 * <p>
	 * A {@code null} string input returns {@code null}. An empty ("") string input returns an empty
	 * string. A null or empty set of search characters returns the input string.
	 * </p>
	 * <p>
	 * The length of the search characters should normally equal the length of the replace
	 * characters. If the search characters is longer, then the extra search characters are deleted.
	 * If the search characters is shorter, then the extra replace characters are ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replaceChars(null, *, *)           = null
	 * Strings.replaceChars("", *, *)             = ""
	 * Strings.replaceChars("abc", null, *)       = "abc"
	 * Strings.replaceChars("abc", "", *)         = "abc"
	 * Strings.replaceChars("abc", "b", null)     = "ac"
	 * Strings.replaceChars("abc", "b", 0)       = "ac"
	 * Strings.replaceChars("abcba", "bc", 'y')  = "ayyya"
	 * </pre>
	 * 
	 * @param str String to replace characters in, may be null
	 * @param searchChars a set of characters to search for, may be null
	 * @param replaceChar a character to replace, may be zero
	 * @return modified String, {@code null} if null string input
	 */
	public static String replaceChars(final String str, final String searchChars, final char replaceChar) {
		if (isEmpty(str) || isEmpty(searchChars)) {
			return str;
		}

		boolean modified = false;
		final int strLength = str.length();
		final StringBuilder buf = new StringBuilder(strLength);
		for (int i = 0; i < strLength; i++) {
			final char ch = str.charAt(i);
			final int index = searchChars.indexOf(ch);
			if (index >= 0) {
				modified = true;
				if (replaceChar > 0) {
					buf.append(replaceChar);
				}
			}
			else {
				buf.append(ch);
			}
		}
		if (modified) {
			return buf.toString();
		}
		return str;
	}

	/**
	 * <p>
	 * Replaces multiple characters in a String in one go. This method can also be used to delete
	 * characters.
	 * </p>
	 * <p>
	 * For example:<br />
	 * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>.
	 * </p>
	 * <p>
	 * A {@code null} string input returns {@code null}. An empty ("") string input returns an empty
	 * string. A null or empty set of search characters returns the input string.
	 * </p>
	 * <p>
	 * The length of the search characters should normally equal the length of the replace
	 * characters. If the search characters is longer, then the extra search characters are deleted.
	 * If the search characters is shorter, then the extra replace characters are ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replaceChars(null, *, *)           = null
	 * Strings.replaceChars("", *, *)             = ""
	 * Strings.replaceChars("abc", null, *)       = "abc"
	 * Strings.replaceChars("abc", "", *)         = "abc"
	 * Strings.replaceChars("abc", "b", null)     = "ac"
	 * Strings.replaceChars("abc", "b", "")       = "ac"
	 * Strings.replaceChars("abcba", "bc", "yz")  = "ayzya"
	 * Strings.replaceChars("abcba", "bc", "y")   = "ayya"
	 * Strings.replaceChars("abcba", "bc", "yzx") = "ayzya"
	 * </pre>
	 * 
	 * @param str String to replace characters in, may be null
	 * @param searchChars a set of characters to search for, may be null
	 * @param replaceChars a set of characters to replace, may be null
	 * @return modified String, {@code null} if null string input
	 */
	public static String replaceChars(final String str, final String searchChars, String replaceChars) {
		if (isEmpty(str) || isEmpty(searchChars)) {
			return str;
		}
		if (replaceChars == null) {
			replaceChars = EMPTY;
		}
		boolean modified = false;
		final int replaceCharsLength = replaceChars.length();
		final int strLength = str.length();
		final StringBuilder buf = new StringBuilder(strLength);
		for (int i = 0; i < strLength; i++) {
			final char ch = str.charAt(i);
			final int index = searchChars.indexOf(ch);
			if (index >= 0) {
				modified = true;
				if (index < replaceCharsLength) {
					buf.append(replaceChars.charAt(index));
				}
			}
			else {
				buf.append(ch);
			}
		}
		if (modified) {
			return buf.toString();
		}
		return str;
	}

	// Overlay
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Overlays part of a String with another String.
	 * </p>
	 * <p>
	 * A {@code null} string input returns {@code null}. A negative index is treated as zero. An
	 * index greater than the string length is treated as the string length. The start index is
	 * always the smaller of the two indices.
	 * </p>
	 * 
	 * <pre>
	 * Strings.overlay(null, *, *, *)            = null
	 * Strings.overlay("", "abc", 0, 0)          = "abc"
	 * Strings.overlay("abcdef", null, 2, 4)     = "abef"
	 * Strings.overlay("abcdef", "", 2, 4)       = "abef"
	 * Strings.overlay("abcdef", "", 4, 2)       = "abef"
	 * Strings.overlay("abcdef", "zzzz", 2, 4)   = "abzzzzef"
	 * Strings.overlay("abcdef", "zzzz", 4, 2)   = "abzzzzef"
	 * Strings.overlay("abcdef", "zzzz", -1, 4)  = "zzzzef"
	 * Strings.overlay("abcdef", "zzzz", 2, 8)   = "abzzzz"
	 * Strings.overlay("abcdef", "zzzz", -2, -3) = "zzzzabcdef"
	 * Strings.overlay("abcdef", "zzzz", 8, 10)  = "abcdefzzzz"
	 * </pre>
	 * 
	 * @param str the String to do overlaying in, may be null
	 * @param overlay the String to overlay, may be null
	 * @param start the position to start overlaying at
	 * @param end the position to stop overlaying before
	 * @return overlayed String, {@code null} if null String input
	 */
	public static String overlay(final String str, String overlay, int start, int end) {
		if (str == null) {
			return null;
		}
		if (overlay == null) {
			overlay = EMPTY;
		}
		final int len = str.length();
		if (start < 0) {
			start = 0;
		}
		if (start > len) {
			start = len;
		}
		if (end < 0) {
			end = 0;
		}
		if (end > len) {
			end = len;
		}
		if (start > end) {
			final int temp = start;
			start = end;
			end = temp;
		}
		return new StringBuilder(len + start - end + overlay.length() + 1).append(str.substring(0, start))
			.append(overlay).append(str.substring(end)).toString();
	}

	// Chomping
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Removes one newline from end of a String if it's there, otherwise leave it alone. A newline
	 * is &quot;{@code \n}&quot;, &quot;{@code \r}&quot;, or &quot;{@code \r\n}&quot;.
	 * </p>
	 * <p>
	 * NOTE: This method changed in 2.0. It now more closely matches Perl chomp.
	 * </p>
	 * 
	 * <pre>
	 * Strings.chomp(null)          = null
	 * Strings.chomp("")            = ""
	 * Strings.chomp("abc \r")      = "abc "
	 * Strings.chomp("abc\n")       = "abc"
	 * Strings.chomp("abc\r\n")     = "abc"
	 * Strings.chomp("abc\r\n\r\n") = "abc\r\n"
	 * Strings.chomp("abc\n\r")     = "abc\n"
	 * Strings.chomp("abc\n\rabc")  = "abc\n\rabc"
	 * Strings.chomp("\r")          = ""
	 * Strings.chomp("\n")          = ""
	 * Strings.chomp("\r\n")        = ""
	 * </pre>
	 * 
	 * @param str the String to chomp a newline from, may be null
	 * @return String without newline, {@code null} if null String input
	 */
	public static String chomp(final String str) {
		if (isEmpty(str)) {
			return str;
		}

		if (str.length() == 1) {
			final char ch = str.charAt(0);
			if (ch == Chars.CR || ch == Chars.LF) {
				return EMPTY;
			}
			return str;
		}

		int lastIdx = str.length() - 1;
		final char last = str.charAt(lastIdx);

		if (last == Chars.LF) {
			if (str.charAt(lastIdx - 1) == Chars.CR) {
				lastIdx--;
			}
		}
		else if (last != Chars.CR) {
			lastIdx++;
		}
		return str.substring(0, lastIdx);
	}

	/**
	 * <p>
	 * Removes {@code separator} from the end of {@code str} if it's there, otherwise leave it
	 * alone.
	 * </p>
	 * <p>
	 * NOTE: This method changed in version 2.0. It now more closely matches Perl chomp. For the
	 * previous behavior, use {@link #substringBeforeLast(String, String)}. This method uses
	 * {@link String#endsWith(String)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.chomp(null, *)         = null
	 * Strings.chomp("", *)           = ""
	 * Strings.chomp("foobar", "bar") = "foo"
	 * Strings.chomp("foobar", "baz") = "foobar"
	 * Strings.chomp("foo", "foo")    = ""
	 * Strings.chomp("foo ", "foo")   = "foo "
	 * Strings.chomp(" foo", "foo")   = " "
	 * Strings.chomp("foo", "foooo")  = "foo"
	 * Strings.chomp("foo", "")       = "foo"
	 * Strings.chomp("foo", null)     = "foo"
	 * </pre>
	 * 
	 * @param str the String to chomp from, may be null
	 * @param separator separator String, may be null
	 * @return String without trailing separator, {@code null} if null String input
	 * @deprecated This feature will be removed in Lang 4.0, use
	 *             {@link Strings#removeEnd(String, String)} instead
	 */
	@Deprecated
	public static String chomp(final String str, final String separator) {
		return removeEnd(str, separator);
	}

	// Chopping
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Remove the last character from a String.
	 * </p>
	 * <p>
	 * If the String ends in {@code \r\n}, then remove both of them.
	 * </p>
	 * 
	 * <pre>
	 * Strings.chop(null)          = null
	 * Strings.chop("")            = ""
	 * Strings.chop("abc \r")      = "abc "
	 * Strings.chop("abc\n")       = "abc"
	 * Strings.chop("abc\r\n")     = "abc"
	 * Strings.chop("abc")         = "ab"
	 * Strings.chop("abc\nabc")    = "abc\nab"
	 * Strings.chop("a")           = ""
	 * Strings.chop("\r")          = ""
	 * Strings.chop("\n")          = ""
	 * Strings.chop("\r\n")        = ""
	 * </pre>
	 * 
	 * @param str the String to chop last character from, may be null
	 * @return String without last character, {@code null} if null String input
	 */
	public static String chop(final String str) {
		if (str == null) {
			return null;
		}
		final int strLen = str.length();
		if (strLen < 2) {
			return EMPTY;
		}
		final int lastIdx = strLen - 1;
		final String ret = str.substring(0, lastIdx);
		final char last = str.charAt(lastIdx);
		if (last == Chars.LF && ret.charAt(lastIdx - 1) == Chars.CR) {
			return ret.substring(0, lastIdx - 1);
		}
		return ret;
	}

	// Conversion
	// -----------------------------------------------------------------------

	// Padding
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Repeat a String {@code repeat} times to form a new String.
	 * </p>
	 * 
	 * <pre>
	 * Strings.repeat(null, 2) = null
	 * Strings.repeat("", 0)   = ""
	 * Strings.repeat("", 2)   = ""
	 * Strings.repeat("a", 3)  = "aaa"
	 * Strings.repeat("ab", 2) = "abab"
	 * Strings.repeat("a", -2) = ""
	 * </pre>
	 * 
	 * @param str the String to repeat, may be null
	 * @param repeat number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated, {@code null} if null String
	 *         input
	 */
	public static String repeat(final CharSequence str, final int repeat) {
		if (str == null) {
			return null;
		}
		if (repeat <= 0) {
			return EMPTY;
		}
		final int inputLength = str.length();
		if (repeat == 1 || inputLength == 0) {
			return str.toString();
		}
		if (inputLength == 1 && repeat <= PAD_LIMIT) {
			return repeat(str.charAt(0), repeat);
		}

		final int outputLength = inputLength * repeat;
		switch (inputLength) {
		case 1:
			return repeat(str.charAt(0), repeat);
		case 2:
			final char ch0 = str.charAt(0);
			final char ch1 = str.charAt(1);
			final char[] output2 = new char[outputLength];
			for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default:
			final StringBuilder buf = new StringBuilder(outputLength);
			for (int i = 0; i < repeat; i++) {
				buf.append(str);
			}
			return buf.toString();
		}
	}

	/**
	 * <p>
	 * Repeat a String {@code repeat} times to form a new String, with a String separator injected
	 * each time.
	 * </p>
	 * 
	 * <pre>
	 * Strings.repeat(null, null, 2) = null
	 * Strings.repeat(null, "x", 2)  = null
	 * Strings.repeat("", null, 0)   = ""
	 * Strings.repeat("", "", 2)     = ""
	 * Strings.repeat("", "x", 3)    = "xxx"
	 * Strings.repeat("?", ", ", 3)  = "?, ?, ?"
	 * </pre>
	 * 
	 * @param str the String to repeat, may be null
	 * @param separator the String to inject, may be null
	 * @param repeat number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated, {@code null} if null String
	 *         input
	 */
	public static String repeat(final CharSequence str, final String separator, final int repeat) {
		if (str == null || separator == null) {
			return repeat(str, repeat);
		}
		// given that repeat(String, int) is quite optimized, better to rely on it than try and
		// splice this into it
		final String result = repeat(str.toString() + separator, repeat);
		return removeEnd(result, separator);
	}

	/**
	 * <p>
	 * Returns padding using the specified delimiter repeated to a given length.
	 * </p>
	 * 
	 * <pre>
	 * Strings.repeat('e', 0)  = ""
	 * Strings.repeat('e', 3)  = "eee"
	 * Strings.repeat('e', -2) = ""
	 * </pre>
	 * <p>
	 * Note: this method doesn't not support padding with <a
	 * href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary
	 * Characters</a> as they require a pair of {@code char}s to be represented. If you are needing
	 * to support full I18N of your applications consider using {@link #repeat(CharSequence, int)}
	 * instead.
	 * </p>
	 * 
	 * @param ch character to repeat
	 * @param repeat number of times to repeat char, negative treated as zero
	 * @return String with repeated character
	 * @see #repeat(CharSequence, int)
	 */
	public static String repeat(final char ch, final int repeat) {
		final char[] buf = new char[repeat];
		for (int i = repeat - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}

	/**
	 * <p>
	 * Right pad a String with spaces (' ').
	 * </p>
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.rightPad(null, *)   = null
	 * Strings.rightPad("", 3)     = "   "
	 * Strings.rightPad("bat", 3)  = "bat"
	 * Strings.rightPad("bat", 5)  = "bat  "
	 * Strings.rightPad("bat", 1)  = "bat"
	 * Strings.rightPad("bat", -1) = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @return right padded String or original String if no padding is necessary, {@code null} if
	 *         null String input
	 */
	public static String rightPad(final String str, final int size) {
		return rightPad(str, size, ' ');
	}

	/**
	 * <p>
	 * Right pad a String with a specified character.
	 * </p>
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.rightPad(null, *, *)     = null
	 * Strings.rightPad("", 3, 'z')     = "zzz"
	 * Strings.rightPad("bat", 3, 'z')  = "bat"
	 * Strings.rightPad("bat", 5, 'z')  = "batzz"
	 * Strings.rightPad("bat", 1, 'z')  = "bat"
	 * Strings.rightPad("bat", -1, 'z') = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @param padChar the character to pad with
	 * @return right padded String or original String if no padding is necessary, {@code null} if
	 *         null String input
	 */
	public static String rightPad(final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return rightPad(str, size, String.valueOf(padChar));
		}
		return str.concat(repeat(padChar, pads));
	}

	/**
	 * <p>
	 * Right pad a String with a specified String.
	 * </p>
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.rightPad(null, *, *)      = null
	 * Strings.rightPad("", 3, "z")      = "zzz"
	 * Strings.rightPad("bat", 3, "yz")  = "bat"
	 * Strings.rightPad("bat", 5, "yz")  = "batyz"
	 * Strings.rightPad("bat", 8, "yz")  = "batyzyzy"
	 * Strings.rightPad("bat", 1, "yz")  = "bat"
	 * Strings.rightPad("bat", -1, "yz") = "bat"
	 * Strings.rightPad("bat", 5, null)  = "bat  "
	 * Strings.rightPad("bat", 5, "")    = "bat  "
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return right padded String or original String if no padding is necessary, {@code null} if
	 *         null String input
	 */
	public static String rightPad(final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return rightPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return str.concat(padStr);
		}
		else if (pads < padLen) {
			return str.concat(padStr.substring(0, pads));
		}
		else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return str.concat(new String(padding));
		}
	}

	/**
	 * <p>
	 * Left pad a String with spaces (' ').
	 * </p>
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.leftPad(null, *)   = null
	 * Strings.leftPad("", 3)     = "   "
	 * Strings.leftPad("bat", 3)  = "bat"
	 * Strings.leftPad("bat", 5)  = "  bat"
	 * Strings.leftPad("bat", 1)  = "bat"
	 * Strings.leftPad("bat", -1) = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @return left padded String or original String if no padding is necessary, {@code null} if
	 *         null String input
	 */
	public static String leftPad(final CharSequence str, final int size) {
		return leftPad(str, size, ' ');
	}

	/**
	 * <p>
	 * Left pad a String with a specified character.
	 * </p>
	 * <p>
	 * Pad to a size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.leftPad(null, *, *)     = null
	 * Strings.leftPad("", 3, 'z')     = "zzz"
	 * Strings.leftPad("bat", 3, 'z')  = "bat"
	 * Strings.leftPad("bat", 5, 'z')  = "zzbat"
	 * Strings.leftPad("bat", 1, 'z')  = "bat"
	 * Strings.leftPad("bat", -1, 'z') = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @param padChar the character to pad with
	 * @return left padded String or original String if no padding is necessary, {@code null} if
	 *         null String input
	 */
	public static String leftPad(final CharSequence str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			return str.toString(); // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return repeat(padChar, pads).concat(str.toString());
	}

	/**
	 * <p>
	 * Left pad a String with a specified String.
	 * </p>
	 * <p>
	 * Pad to a size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.leftPad(null, *, *)      = null
	 * Strings.leftPad("", 3, "z")      = "zzz"
	 * Strings.leftPad("bat", 3, "yz")  = "bat"
	 * Strings.leftPad("bat", 5, "yz")  = "yzbat"
	 * Strings.leftPad("bat", 8, "yz")  = "yzyzybat"
	 * Strings.leftPad("bat", 1, "yz")  = "bat"
	 * Strings.leftPad("bat", -1, "yz") = "bat"
	 * Strings.leftPad("bat", 5, null)  = "  bat"
	 * Strings.leftPad("bat", 5, "")    = "  bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * @param size the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return left padded String or original String if no padding is necessary, {@code null} if
	 *         null String input
	 */
	public static String leftPad(final CharSequence str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString(); // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return padStr.concat(str.toString());
		}
		else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str.toString());
		}
		else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new StringBuilder(padding.length + str.length()).append(padding).append(str).toString();
		}
	}

	/**
	 * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 * 
	 * @param cs a CharSequence or {@code null}
	 * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 */
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	// Centering
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Centers a String in a larger String of size {@code size} using the space character (' ').
	 * <p>
	 * <p>
	 * If the size is less than the String length, the String is returned. A {@code null} String
	 * returns {@code null}. A negative size is treated as zero.
	 * </p>
	 * <p>
	 * Equivalent to {@code center(str, size, " ")}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.center(null, *)   = null
	 * Strings.center("", 4)     = "    "
	 * Strings.center("ab", -1)  = "ab"
	 * Strings.center("ab", 4)   = " ab "
	 * Strings.center("abcd", 2) = "abcd"
	 * Strings.center("a", 4)    = " a  "
	 * </pre>
	 * 
	 * @param str the String to center, may be null
	 * @param size the int size of new String, negative treated as zero
	 * @return centered String, {@code null} if null String input
	 */
	public static String center(final String str, final int size) {
		return center(str, size, ' ');
	}

	/**
	 * <p>
	 * Centers a String in a larger String of size {@code size}. Uses a supplied character as the
	 * value to pad the String with.
	 * </p>
	 * <p>
	 * If the size is less than the String length, the String is returned. A {@code null} String
	 * returns {@code null}. A negative size is treated as zero.
	 * </p>
	 * 
	 * <pre>
	 * Strings.center(null, *, *)     = null
	 * Strings.center("", 4, ' ')     = "    "
	 * Strings.center("ab", -1, ' ')  = "ab"
	 * Strings.center("ab", 4, ' ')   = " ab "
	 * Strings.center("abcd", 2, ' ') = "abcd"
	 * Strings.center("a", 4, ' ')    = " a  "
	 * Strings.center("a", 4, 'y')    = "yayy"
	 * </pre>
	 * 
	 * @param str the String to center, may be null
	 * @param size the int size of new String, negative treated as zero
	 * @param padChar the character to pad the new String with
	 * @return centered String, {@code null} if null String input
	 */
	public static String center(String str, final int size, final char padChar) {
		if (str == null || size <= 0) {
			return str;
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		str = leftPad(str, strLen + pads / 2, padChar);
		str = rightPad(str, size, padChar);
		return str;
	}

	/**
	 * <p>
	 * Centers a String in a larger String of size {@code size}. Uses a supplied String as the value
	 * to pad the String with.
	 * </p>
	 * <p>
	 * If the size is less than the String length, the String is returned. A {@code null} String
	 * returns {@code null}. A negative size is treated as zero.
	 * </p>
	 * 
	 * <pre>
	 * Strings.center(null, *, *)     = null
	 * Strings.center("", 4, " ")     = "    "
	 * Strings.center("ab", -1, " ")  = "ab"
	 * Strings.center("ab", 4, " ")   = " ab "
	 * Strings.center("abcd", 2, " ") = "abcd"
	 * Strings.center("a", 4, " ")    = " a  "
	 * Strings.center("a", 4, "yz")   = "yayz"
	 * Strings.center("abc", 7, null) = "  abc  "
	 * Strings.center("abc", 7, "")   = "  abc  "
	 * </pre>
	 * 
	 * @param str the String to center, may be null
	 * @param size the int size of new String, negative treated as zero
	 * @param padStr the String to pad the new String with, must not be null or empty
	 * @return centered String, {@code null} if null String input
	 * @throws IllegalArgumentException if padStr is {@code null} or empty
	 */
	public static String center(String str, final int size, String padStr) {
		if (str == null || size <= 0) {
			return str;
		}
		if (isEmpty(padStr)) {
			padStr = SPACE;
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		str = leftPad(str, strLen + pads / 2, padStr);
		str = rightPad(str, size, padStr);
		return str;
	}

	// Case conversion
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts a String to upper case as per {@link String#toUpperCase()}.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.upperCase(null)  = null
	 * Strings.upperCase("")    = ""
	 * Strings.upperCase("aBc") = "ABC"
	 * </pre>
	 * <p>
	 * <strong>Note:</strong> As described in the documentation for {@link String#toUpperCase()},
	 * the result of this method is affected by the current locale. For platform-independent case
	 * transformations, the method {@link #lowerCase(String, Locale)} should be used with a specific
	 * locale (e.g. {@link Locale#ENGLISH}).
	 * </p>
	 * 
	 * @param str the String to upper case, may be null
	 * @return the upper cased String, {@code null} if null String input
	 */
	public static String upperCase(final String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	/**
	 * <p>
	 * Converts a String to upper case as per {@link String#toUpperCase(Locale)}.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.upperCase(null, Locale.ENGLISH)  = null
	 * Strings.upperCase("", Locale.ENGLISH)    = ""
	 * Strings.upperCase("aBc", Locale.ENGLISH) = "ABC"
	 * </pre>
	 * 
	 * @param str the String to upper case, may be null
	 * @param locale the locale that defines the case transformation rules, must not be null
	 * @return the upper cased String, {@code null} if null String input
	 */
	public static String upperCase(final String str, final Locale locale) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase(locale);
	}

	/**
	 * <p>
	 * Converts a String to lower case as per {@link String#toLowerCase()}.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lowerCase(null)  = null
	 * Strings.lowerCase("")    = ""
	 * Strings.lowerCase("aBc") = "abc"
	 * </pre>
	 * <p>
	 * <strong>Note:</strong> As described in the documentation for {@link String#toLowerCase()},
	 * the result of this method is affected by the current locale. For platform-independent case
	 * transformations, the method {@link #lowerCase(String, Locale)} should be used with a specific
	 * locale (e.g. {@link Locale#ENGLISH}).
	 * </p>
	 * 
	 * @param str the String to lower case, may be null
	 * @return the lower cased String, {@code null} if null String input
	 */
	public static String lowerCase(final String str) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase();
	}

	/**
	 * <p>
	 * Converts a String to lower case as per {@link String#toLowerCase(Locale)}.
	 * </p>
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lowerCase(null, Locale.ENGLISH)  = null
	 * Strings.lowerCase("", Locale.ENGLISH)    = ""
	 * Strings.lowerCase("aBc", Locale.ENGLISH) = "abc"
	 * </pre>
	 * 
	 * @param str the String to lower case, may be null
	 * @param locale the locale that defines the case transformation rules, must not be null
	 * @return the lower cased String, {@code null} if null String input
	 */
	public static String lowerCase(final String str, final Locale locale) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase(locale);
	}

	/**
	 * <p>
	 * Capitalizes a String changing the first letter to title case as per
	 * {@link Character#toTitleCase(char)}. No other letters are changed.
	 * </p>
	 * <p>
	 * For a word based algorithm, see {@link panda.lang.Texts#capitalize(String)}. A {@code null}
	 * input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.capitalize(null)  = null
	 * Strings.capitalize("")    = ""
	 * Strings.capitalize("cat") = "Cat"
	 * Strings.capitalize("cAt") = "CAt"
	 * </pre>
	 * 
	 * @param str the String to capitalize, may be null
	 * @return the capitalized String, {@code null} if null String input
	 * @see Texts#capitalize(String)
	 * @see #uncapitalize(String)
	 */
	public static String capitalize(final String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		char firstChar = str.charAt(0);
		if (Character.isTitleCase(firstChar)) {
			// already capitalized
			return str;
		}

		return new StringBuilder(strLen).append(Character.toTitleCase(firstChar)).append(str.substring(1)).toString();
	}

	/**
	 * <p>
	 * Uncapitalizes a String changing the first letter to title case as per
	 * {@link Character#toLowerCase(char)}. No other letters are changed.
	 * </p>
	 * <p>
	 * For a word based algorithm, see {@link Texts#uncapitalize(String)}. A {@code null} input
	 * String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.uncapitalize(null)  = null
	 * Strings.uncapitalize("")    = ""
	 * Strings.uncapitalize("Cat") = "cat"
	 * Strings.uncapitalize("CAT") = "cAT"
	 * </pre>
	 * 
	 * @param str the String to uncapitalize, may be null
	 * @return the uncapitalized String, {@code null} if null String input
	 * @see Texts#uncapitalize(String)
	 * @see #capitalize(String)
	 */
	public static String uncapitalize(final String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		char firstChar = str.charAt(0);
		if (Character.isLowerCase(firstChar)) {
			// already uncapitalized
			return str;
		}

		return new StringBuilder(strLen).append(Character.toLowerCase(firstChar)).append(str.substring(1)).toString();
	}

	/**
	 * <p>
	 * Swaps the case of a String changing upper and title case to lower case, and lower case to
	 * upper case.
	 * </p>
	 * <ul>
	 * <li>Upper case character converts to Lower case</li>
	 * <li>Title case character converts to Lower case</li>
	 * <li>Lower case character converts to Upper case</li>
	 * </ul>
	 * <p>
	 * For a word based algorithm, see {@link Texts#swapCase(String)}. A {@code null} input String
	 * returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.swapCase(null)                 = null
	 * Strings.swapCase("")                   = ""
	 * Strings.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 * 
	 * @param str the String to swap case, may be null
	 * @return the changed String, {@code null} if null String input
	 */
	public static String swapCase(final String str) {
		if (Strings.isEmpty(str)) {
			return str;
		}

		final char[] buffer = str.toCharArray();

		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (Character.isUpperCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			}
			else if (Character.isTitleCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			}
			else if (Character.isLowerCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			}
		}
		return new String(buffer);
	}

	// Count matches
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Counts how many times the substring appears in the larger string.
	 * </p>
	 * <p>
	 * A {@code null} or empty ("") String input returns {@code 0}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.countMatches(null, *)       = 0
	 * Strings.countMatches("", *)         = 0
	 * Strings.countMatches("abba", null)  = 0
	 * Strings.countMatches("abba", "")    = 0
	 * Strings.countMatches("abba", "a")   = 2
	 * Strings.countMatches("abba", "ab")  = 1
	 * Strings.countMatches("abba", "xxx") = 0
	 * </pre>
	 * 
	 * @param str the CharSequence to check, may be null
	 * @param sub the substring to count, may be null
	 * @return the number of occurrences, 0 if either CharSequence is {@code null}
	 */
	public static int countMatches(final CharSequence str, final CharSequence sub) {
		if (isEmpty(str) || isEmpty(sub)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = CharSequences.indexOf(str, sub, idx)) != INDEX_NOT_FOUND) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	// Character Tests
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode letters.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isAlpha(null)   = false
	 * Strings.isAlpha("")     = false
	 * Strings.isAlpha("  ")   = false
	 * Strings.isAlpha("abc")  = true
	 * Strings.isAlpha("ab2c") = false
	 * Strings.isAlpha("ab-c") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains letters, and is non-null
	 */
	public static boolean isAlpha(final CharSequence cs) {
		if (cs == null || cs.length() == 0) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetter(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode letters and space (' ').
	 * </p>
	 * <p>
	 * {@code null} will return {@code false} An empty CharSequence (length()=0) will return
	 * {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isAlphaSpace(null)   = false
	 * Strings.isAlphaSpace("")     = true
	 * Strings.isAlphaSpace("  ")   = true
	 * Strings.isAlphaSpace("abc")  = true
	 * Strings.isAlphaSpace("ab c") = true
	 * Strings.isAlphaSpace("ab2c") = false
	 * Strings.isAlphaSpace("ab-c") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains letters and space, and is non-null
	 */
	public static boolean isAlphaSpace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetter(cs.charAt(i)) == false && cs.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode letters or digits.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isAlphanumeric(null)   = false
	 * Strings.isAlphanumeric("")     = false
	 * Strings.isAlphanumeric("  ")   = false
	 * Strings.isAlphanumeric("abc")  = true
	 * Strings.isAlphanumeric("ab c") = false
	 * Strings.isAlphanumeric("ab2c") = true
	 * Strings.isAlphanumeric("ab-c") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains letters or digits, and is non-null
	 */
	public static boolean isAlphanumeric(final CharSequence cs) {
		if (cs == null || cs.length() == 0) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetterOrDigit(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode letters, digits or space ({@code ' '}).
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isAlphanumericSpace(null)   = false
	 * Strings.isAlphanumericSpace("")     = true
	 * Strings.isAlphanumericSpace("  ")   = true
	 * Strings.isAlphanumericSpace("abc")  = true
	 * Strings.isAlphanumericSpace("ab c") = true
	 * Strings.isAlphanumericSpace("ab2c") = true
	 * Strings.isAlphanumericSpace("ab-c") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains letters, digits or space, and is non-null
	 */
	public static boolean isAlphanumericSpace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetterOrDigit(cs.charAt(i)) == false && cs.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only ASCII printable characters.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isAsciiPrintable(null)     = false
	 * Strings.isAsciiPrintable("")       = true
	 * Strings.isAsciiPrintable(" ")      = true
	 * Strings.isAsciiPrintable("Ceki")   = true
	 * Strings.isAsciiPrintable("ab2c")   = true
	 * Strings.isAsciiPrintable("!ab-c~") = true
	 * Strings.isAsciiPrintable("\u0020") = true
	 * Strings.isAsciiPrintable("\u0021") = true
	 * Strings.isAsciiPrintable("\u007e") = true
	 * Strings.isAsciiPrintable("\u007f") = false
	 * Strings.isAsciiPrintable("Ceki G\u00fclc\u00fc") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if every character is in the range 32 thru 126
	 */
	public static boolean isAsciiPrintable(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Chars.isAsciiPrintable(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode digits. A decimal point is not a Unicode
	 * digit and returns false.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code false}.
	 * </p>
	 * <p>
	 * Note that the method does not allow for a leading sign, either positive or negative. Also, if
	 * a String passes the numeric test, it may still generate a NumberFormatException when parsed
	 * by Integer.parseInt or Long.parseLong, e.g. if the value is outside the range for int or long
	 * respectively.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isNumeric(null)   = false
	 * Strings.isNumeric("")     = false
	 * Strings.isNumeric("  ")   = false
	 * Strings.isNumeric("123")  = true
	 * Strings.isNumeric("12 3") = false
	 * Strings.isNumeric("ab2c") = false
	 * Strings.isNumeric("12-3") = false
	 * Strings.isNumeric("12.3") = false
	 * Strings.isNumeric("-123") = false
	 * Strings.isNumeric("+123") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains digits, and is non-null
	 */
	public static boolean isNumeric(final CharSequence cs) {
		if (cs == null || cs.length() == 0) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode digits or space ({@code ' '}). A decimal
	 * point is not a Unicode digit and returns false.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isNumericSpace(null)   = false
	 * Strings.isNumericSpace("")     = true
	 * Strings.isNumericSpace("  ")   = true
	 * Strings.isNumericSpace("123")  = true
	 * Strings.isNumericSpace("12 3") = true
	 * Strings.isNumericSpace("ab2c") = false
	 * Strings.isNumericSpace("12-3") = false
	 * Strings.isNumericSpace("12.3") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains digits or space, and is non-null
	 */
	public static boolean isNumericSpace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(cs.charAt(i)) == false && cs.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only whitespace.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isWhitespace(null)   = false
	 * Strings.isWhitespace("")     = true
	 * Strings.isWhitespace("  ")   = true
	 * Strings.isWhitespace("abc")  = false
	 * Strings.isWhitespace("ab2c") = false
	 * Strings.isWhitespace("ab-c") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains whitespace, and is non-null
	 */
	public static boolean isWhitespace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only lowercase characters.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return
	 * {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isAllLowerCase(null)   = false
	 * Strings.isAllLowerCase("")     = false
	 * Strings.isAllLowerCase("  ")   = false
	 * Strings.isAllLowerCase("abc")  = true
	 * Strings.isAllLowerCase("abC") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains lowercase characters, and is non-null
	 */
	public static boolean isAllLowerCase(final CharSequence cs) {
		if (cs == null || isEmpty(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLowerCase(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only uppercase characters.
	 * </p>
	 * <p>
	 * {@code null} will return {@code false}. An empty String (length()=0) will return
	 * {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isAllUpperCase(null)   = false
	 * Strings.isAllUpperCase("")     = false
	 * Strings.isAllUpperCase("  ")   = false
	 * Strings.isAllUpperCase("ABC")  = true
	 * Strings.isAllUpperCase("aBC") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains uppercase characters, and is non-null
	 */
	public static boolean isAllUpperCase(final CharSequence cs) {
		if (cs == null || isEmpty(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isUpperCase(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	// Defaults
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, an empty String ("").
	 * </p>
	 * 
	 * <pre>
	 * Strings.defaultString(null)  = ""
	 * Strings.defaultString("")    = ""
	 * Strings.defaultString("bat") = "bat"
	 * </pre>
	 * 
	 * @see Objects#toString(Object)
	 * @see String#valueOf(Object)
	 * @param str the String to check, may be null
	 * @return the passed in String, or the empty String if it was {@code null}
	 */
	public static String defaultString(Object str) {
		return str == null ? EMPTY : str.toString();
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, the value of
	 * {@code defaultStr}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.defaultString(null, "NULL")  = "NULL"
	 * Strings.defaultString("", "NULL")    = ""
	 * Strings.defaultString("bat", "NULL") = "bat"
	 * </pre>
	 * 
	 * @see Objects#toString(Object,String)
	 * @see String#valueOf(Object)
	 * @param str the String to check, may be null
	 * @param defaultStr the default String to return if the input is {@code null}, may be null
	 * @return the passed in String, or the default if it was {@code null}
	 */
	public static String defaultString(Object str, String defaultStr) {
		return str == null ? defaultStr : str.toString();
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, an empty String ("").
	 * </p>
	 * 
	 * <pre>
	 * Strings.defaultString(null)  = ""
	 * Strings.defaultString("")    = ""
	 * Strings.defaultString("bat") = "bat"
	 * </pre>
	 * 
	 * @see Objects#toString(Object)
	 * @see String#valueOf(Object)
	 * @param str the String to check, may be null
	 * @return the passed in String, or the empty String if it was {@code null}
	 */
	public static String defaultString(final String str) {
		return str == null ? EMPTY : str;
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, the value of
	 * {@code defaultStr}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.defaultString(null, "NULL")  = "NULL"
	 * Strings.defaultString("", "NULL")    = ""
	 * Strings.defaultString("bat", "NULL") = "bat"
	 * </pre>
	 * 
	 * @see Objects#toString(Object,String)
	 * @see String#valueOf(Object)
	 * @param str the String to check, may be null
	 * @param defaultStr the default String to return if the input is {@code null}, may be null
	 * @return the passed in String, or the default if it was {@code null}
	 */
	public static String defaultString(final String str, final String defaultStr) {
		return str == null ? defaultStr : str;
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is whitespace, empty ("")
	 * or {@code null}, the value of {@code defaultStr}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.defaultIfBlank(null, "NULL")  = "NULL"
	 * Strings.defaultIfBlank("", "NULL")    = "NULL"
	 * Strings.defaultIfBlank(" ", "NULL")   = "NULL"
	 * Strings.defaultIfBlank("bat", "NULL") = "bat"
	 * Strings.defaultIfBlank("", null)      = null
	 * </pre>
	 * 
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultStr the default CharSequence to return if the input is whitespace, empty ("")
	 *            or {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 * @see Strings#defaultString(String, String)
	 */
	public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
		return Strings.isBlank(str) ? defaultStr : str;
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is empty or {@code null},
	 * the value of {@code defaultStr}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.defaultIfEmpty(null, "NULL")  = "NULL"
	 * Strings.defaultIfEmpty("", "NULL")    = "NULL"
	 * Strings.defaultIfEmpty(" ", "NULL")   = " "
	 * Strings.defaultIfEmpty("bat", "NULL") = "bat"
	 * Strings.defaultIfEmpty("", null)      = null
	 * </pre>
	 * 
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultStr the default CharSequence to return if the input is empty ("") or
	 *            {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 * @see Strings#defaultString(String, String)
	 */
	public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
		return Strings.isEmpty(str) ? defaultStr : str;
	}

	// Reversing
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Reverses a String as per {@link StringBuilder#reverse()}.
	 * </p>
	 * <p>
	 * A {@code null} String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.reverse(null)  = null
	 * Strings.reverse("")    = ""
	 * Strings.reverse("bat") = "tab"
	 * </pre>
	 * 
	 * @param str the String to reverse, may be null
	 * @return the reversed String, {@code null} if null String input
	 */
	public static String reverse(final String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}

	/**
	 * <p>
	 * Reverses a String that is delimited by a specific character.
	 * </p>
	 * <p>
	 * The Strings between the delimiters are not reversed. Thus java.lang.String becomes
	 * String.lang.java (if the delimiter is {@code '.'}).
	 * </p>
	 * 
	 * <pre>
	 * Strings.reverseDelimited(null, *)      = null
	 * Strings.reverseDelimited("", *)        = ""
	 * Strings.reverseDelimited("a.b.c", 'x') = "a.b.c"
	 * Strings.reverseDelimited("a.b.c", ".") = "c.b.a"
	 * </pre>
	 * 
	 * @param str the String to reverse, may be null
	 * @param separatorChar the separator character to use
	 * @return the reversed String, {@code null} if null String input
	 */
	public static String reverseDelimited(final String str, final char separatorChar) {
		if (str == null) {
			return null;
		}
		// could implement manually, but simple way is to reuse other,
		// probably slower, methods.
		final String[] strs = split(str, separatorChar);
		Arrays.reverse(strs);
		return join(strs, separatorChar);
	}

	// Abbreviating
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Abbreviates a String using ellipses. This will turn "Now is the time for all good men" into
	 * "Now is the time for..."
	 * </p>
	 * <p>
	 * Specifically:
	 * <ul>
	 * <li>If {@code str} is less than {@code maxWidth} characters long, return it.</li>
	 * <li>Else abbreviate it to {@code (substring(str, 0, max-3) + "...")}.</li>
	 * <li>If {@code maxWidth} is less than {@code 4}, throw an {@code IllegalArgumentException}.</li>
	 * <li>In no case will it return a String of length greater than {@code maxWidth}.</li>
	 * </ul>
	 * </p>
	 * 
	 * <pre>
	 * Strings.abbreviate(null, *)      = null
	 * Strings.abbreviate("", 4)        = ""
	 * Strings.abbreviate("abcdefg", 6) = "abc..."
	 * Strings.abbreviate("abcdefg", 7) = "abcdefg"
	 * Strings.abbreviate("abcdefg", 8) = "abcdefg"
	 * Strings.abbreviate("abcdefg", 4) = "a..."
	 * Strings.abbreviate("abcdefg", 3) = IllegalArgumentException
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @param maxWidth maximum length of result String, must be at least 4
	 * @return abbreviated String, {@code null} if null String input
	 * @throws IllegalArgumentException if the width is too small
	 */
	public static String abbreviate(final String str, final int maxWidth) {
		return abbreviate(str, 0, maxWidth);
	}

	/**
	 * <p>
	 * Abbreviates a String using ellipses. This will turn "Now is the time for all good men" into
	 * "...is the time for..."
	 * </p>
	 * <p>
	 * Works like {@code abbreviate(String, int)}, but allows you to specify a "left edge" offset.
	 * Note that this left edge is not necessarily going to be the leftmost character in the result,
	 * or the first character following the ellipses, but it will appear somewhere in the result.
	 * <p>
	 * In no case will it return a String of length greater than {@code maxWidth}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.abbreviate(null, *, *)                = null
	 * Strings.abbreviate("", 0, 4)                  = ""
	 * Strings.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
	 * Strings.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
	 * Strings.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
	 * Strings.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
	 * Strings.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
	 * Strings.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
	 * Strings.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
	 * Strings.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
	 * Strings.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
	 * Strings.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
	 * Strings.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @param offset left edge of source String
	 * @param maxWidth maximum length of result String, must be at least 4
	 * @return abbreviated String, {@code null} if null String input
	 * @throws IllegalArgumentException if the width is too small
	 */
	public static String abbreviate(final String str, int offset, final int maxWidth) {
		if (str == null) {
			return null;
		}
		if (maxWidth < 4) {
			throw new IllegalArgumentException("Minimum abbreviation width is 4");
		}
		if (str.length() <= maxWidth) {
			return str;
		}
		if (offset > str.length()) {
			offset = str.length();
		}
		if (str.length() - offset < maxWidth - 3) {
			offset = str.length() - (maxWidth - 3);
		}
		final String abrevMarker = "...";
		if (offset <= 4) {
			return str.substring(0, maxWidth - 3) + abrevMarker;
		}
		if (maxWidth < 7) {
			throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
		}
		if (offset + maxWidth - 3 < str.length()) {
			return abrevMarker + abbreviate(str.substring(offset), maxWidth - 3);
		}
		return abrevMarker + str.substring(str.length() - (maxWidth - 3));
	}

	/**
	 * <p>
	 * Abbreviates a String to the length passed, replacing the middle characters with the supplied
	 * replacement String.
	 * </p>
	 * <p>
	 * This abbreviation only occurs if the following criteria is met:
	 * <ul>
	 * <li>Neither the String for abbreviation nor the replacement String are null or empty</li>
	 * <li>The length to truncate to is less than the length of the supplied String</li>
	 * <li>The length to truncate to is greater than 0</li>
	 * <li>The abbreviated String will have enough room for the length supplied replacement String
	 * and the first and last characters of the supplied String for abbreviation</li>
	 * </ul>
	 * Otherwise, the returned String will be the same as the supplied String for abbreviation.
	 * </p>
	 * 
	 * <pre>
	 * Strings.abbreviateMiddle(null, null, 0)      = null
	 * Strings.abbreviateMiddle("abc", null, 0)      = "abc"
	 * Strings.abbreviateMiddle("abc", ".", 0)      = "abc"
	 * Strings.abbreviateMiddle("abc", ".", 3)      = "abc"
	 * Strings.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
	 * </pre>
	 * 
	 * @param str the String to abbreviate, may be null
	 * @param middle the String to replace the middle characters with, may be null
	 * @param length the length to abbreviate {@code str} to.
	 * @return the abbreviated String if the above criteria is met, or the original String supplied
	 *         for abbreviation.
	 */
	public static String abbreviateMiddle(final String str, final String middle, final int length) {
		if (isEmpty(str) || isEmpty(middle)) {
			return str;
		}

		if (length >= str.length() || length < middle.length() + 2) {
			return str;
		}

		final int targetSting = length - middle.length();
		final int startOffset = targetSting / 2 + targetSting % 2;
		final int endOffset = str.length() - targetSting / 2;

		final StringBuilder builder = new StringBuilder(length);
		builder.append(str.substring(0, startOffset));
		builder.append(middle);
		builder.append(str.substring(endOffset));

		return builder.toString();
	}

	// Difference
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two Strings, and returns the portion where they differ. More precisely, return the
	 * remainder of the second String, starting from where it's different from the first. This means
	 * that the difference between "abc" and "ab" is the empty String and not "c".
	 * </p>
	 * <p>
	 * For example, {@code difference("i am a machine", "i am a robot") -> "robot"}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.difference(null, null) = null
	 * Strings.difference("", "") = ""
	 * Strings.difference("", "abc") = "abc"
	 * Strings.difference("abc", "") = ""
	 * Strings.difference("abc", "abc") = ""
	 * Strings.difference("abc", "ab") = ""
	 * Strings.difference("ab", "abxyz") = "xyz"
	 * Strings.difference("abcde", "abxyz") = "xyz"
	 * Strings.difference("abcde", "xyz") = "xyz"
	 * </pre>
	 * 
	 * @param str1 the first String, may be null
	 * @param str2 the second String, may be null
	 * @return the portion of str2 where it differs from str1; returns the empty String if they are
	 *         equal
	 * @see #indexOfDifference(CharSequence,CharSequence)
	 */
	public static String difference(final String str1, final String str2) {
		if (str1 == null) {
			return str2;
		}
		if (str2 == null) {
			return str1;
		}
		final int at = indexOfDifference(str1, str2);
		if (at == INDEX_NOT_FOUND) {
			return EMPTY;
		}
		return str2.substring(at);
	}

	/**
	 * <p>
	 * Compares two CharSequences, and returns the index at which the CharSequences begin to differ.
	 * </p>
	 * <p>
	 * For example, {@code indexOfDifference("i am a machine", "i am a robot") -> 7}
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfDifference(null, null) = -1
	 * Strings.indexOfDifference("", "") = -1
	 * Strings.indexOfDifference("", "abc") = 0
	 * Strings.indexOfDifference("abc", "") = 0
	 * Strings.indexOfDifference("abc", "abc") = -1
	 * Strings.indexOfDifference("ab", "abxyz") = 2
	 * Strings.indexOfDifference("abcde", "abxyz") = 2
	 * Strings.indexOfDifference("abcde", "xyz") = 0
	 * </pre>
	 * 
	 * @param cs1 the first CharSequence, may be null
	 * @param cs2 the second CharSequence, may be null
	 * @return the index where cs1 and cs2 begin to differ; -1 if they are equal
	 */
	public static int indexOfDifference(final CharSequence cs1, final CharSequence cs2) {
		if (cs1 == cs2) {
			return INDEX_NOT_FOUND;
		}
		if (cs1 == null || cs2 == null) {
			return 0;
		}
		int i;
		for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
			if (cs1.charAt(i) != cs2.charAt(i)) {
				break;
			}
		}
		if (i < cs2.length() || i < cs1.length()) {
			return i;
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Compares all CharSequences in an array and returns the index at which the CharSequences begin
	 * to differ.
	 * </p>
	 * <p>
	 * For example,
	 * <code>indexOfDifference(new String[] {"i am a machine", "i am a robot"}) -> 7</code>
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfDifference(null) = -1
	 * Strings.indexOfDifference(new String[] {}) = -1
	 * Strings.indexOfDifference(new String[] {"abc"}) = -1
	 * Strings.indexOfDifference(new String[] {null, null}) = -1
	 * Strings.indexOfDifference(new String[] {"", ""}) = -1
	 * Strings.indexOfDifference(new String[] {"", null}) = 0
	 * Strings.indexOfDifference(new String[] {"abc", null, null}) = 0
	 * Strings.indexOfDifference(new String[] {null, null, "abc"}) = 0
	 * Strings.indexOfDifference(new String[] {"", "abc"}) = 0
	 * Strings.indexOfDifference(new String[] {"abc", ""}) = 0
	 * Strings.indexOfDifference(new String[] {"abc", "abc"}) = -1
	 * Strings.indexOfDifference(new String[] {"abc", "a"}) = 1
	 * Strings.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
	 * Strings.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
	 * Strings.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
	 * Strings.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
	 * Strings.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
	 * </pre>
	 * 
	 * @param css array of CharSequences, entries may be null
	 * @return the index where the strings begin to differ; -1 if they are all equal
	 */
	public static int indexOfDifference(final CharSequence... css) {
		if (css == null || css.length <= 1) {
			return INDEX_NOT_FOUND;
		}
		boolean anyStringNull = false;
		boolean allStringsNull = true;
		final int arrayLen = css.length;
		int shortestStrLen = Integer.MAX_VALUE;
		int longestStrLen = 0;

		// find the min and max string lengths; this avoids checking to make
		// sure we are not exceeding the length of the string each time through
		// the bottom loop.
		for (int i = 0; i < arrayLen; i++) {
			if (css[i] == null) {
				anyStringNull = true;
				shortestStrLen = 0;
			}
			else {
				allStringsNull = false;
				shortestStrLen = Math.min(css[i].length(), shortestStrLen);
				longestStrLen = Math.max(css[i].length(), longestStrLen);
			}
		}

		// handle lists containing all nulls or all empty strings
		if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
			return INDEX_NOT_FOUND;
		}

		// handle lists containing some nulls or some empty strings
		if (shortestStrLen == 0) {
			return 0;
		}

		// find the position with the first difference across all strings
		int firstDiff = -1;
		for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
			final char comparisonChar = css[0].charAt(stringPos);
			for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
				if (css[arrayPos].charAt(stringPos) != comparisonChar) {
					firstDiff = stringPos;
					break;
				}
			}
			if (firstDiff != -1) {
				break;
			}
		}

		if (firstDiff == -1 && shortestStrLen != longestStrLen) {
			// we compared all of the characters up to the length of the
			// shortest string and didn't find a match, but the string lengths
			// vary, so return the length of the shortest string.
			return shortestStrLen;
		}
		return firstDiff;
	}

	/**
	 * <p>
	 * Compares all Strings in an array and returns the initial sequence of characters that is
	 * common to all of them.
	 * </p>
	 * <p>
	 * For example,
	 * <code>getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -> "i am a "</code>
	 * </p>
	 * 
	 * <pre>
	 * Strings.getCommonPrefix(null) = ""
	 * Strings.getCommonPrefix(new String[] {}) = ""
	 * Strings.getCommonPrefix(new String[] {"abc"}) = "abc"
	 * Strings.getCommonPrefix(new String[] {null, null}) = ""
	 * Strings.getCommonPrefix(new String[] {"", ""}) = ""
	 * Strings.getCommonPrefix(new String[] {"", null}) = ""
	 * Strings.getCommonPrefix(new String[] {"abc", null, null}) = ""
	 * Strings.getCommonPrefix(new String[] {null, null, "abc"}) = ""
	 * Strings.getCommonPrefix(new String[] {"", "abc"}) = ""
	 * Strings.getCommonPrefix(new String[] {"abc", ""}) = ""
	 * Strings.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
	 * Strings.getCommonPrefix(new String[] {"abc", "a"}) = "a"
	 * Strings.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
	 * Strings.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
	 * Strings.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
	 * Strings.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
	 * Strings.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
	 * </pre>
	 * 
	 * @param strs array of String objects, entries may be null
	 * @return the initial sequence of characters that are common to all Strings in the array; empty
	 *         String if the array is null, the elements are all null or if there is no common
	 *         prefix.
	 */
	public static String getCommonPrefix(final String... strs) {
		if (strs == null || strs.length == 0) {
			return EMPTY;
		}
		final int smallestIndexOfDiff = indexOfDifference(strs);
		if (smallestIndexOfDiff == INDEX_NOT_FOUND) {
			// all strings were identical
			if (strs[0] == null) {
				return EMPTY;
			}
			return strs[0];
		}
		else if (smallestIndexOfDiff == 0) {
			// there were no common initial characters
			return EMPTY;
		}
		else {
			// we found a common initial character sequence
			return strs[0].substring(0, smallestIndexOfDiff);
		}
	}

	// Misc
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Find the Levenshtein distance between two Strings.
	 * </p>
	 * <p>
	 * This is the number of changes needed to change one String into another, where each change is
	 * a single character modification (deletion, insertion or substitution).
	 * </p>
	 * <p>
	 * The previous implementation of the Levenshtein distance algorithm was from <a
	 * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
	 * </p>
	 * <p>
	 * Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError which
	 * can occur when my Java implementation is used with very large strings.<br>
	 * This implementation of the Levenshtein distance algorithm is from <a
	 * href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a>
	 * </p>
	 * 
	 * <pre>
	 * Strings.getLevenshteinDistance(null, *)             = IllegalArgumentException
	 * Strings.getLevenshteinDistance(*, null)             = IllegalArgumentException
	 * Strings.getLevenshteinDistance("","")               = 0
	 * Strings.getLevenshteinDistance("","a")              = 1
	 * Strings.getLevenshteinDistance("aaapppp", "")       = 7
	 * Strings.getLevenshteinDistance("frog", "fog")       = 1
	 * Strings.getLevenshteinDistance("fly", "ant")        = 3
	 * Strings.getLevenshteinDistance("elephant", "hippo") = 7
	 * Strings.getLevenshteinDistance("hippo", "elephant") = 7
	 * Strings.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
	 * Strings.getLevenshteinDistance("hello", "hallo")    = 1
	 * </pre>
	 * 
	 * @param s the first String, must not be null
	 * @param t the second String, must not be null
	 * @return result distance
	 * @throws IllegalArgumentException if either String input {@code null}
	 */
	public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		/*
		 * The difference between this impl. and the previous is that, rather than creating and
		 * retaining a matrix of size s.length() + 1 by t.length() + 1, we maintain two
		 * single-dimensional arrays of length s.length() + 1. The first, d, is the 'current
		 * working' distance array that maintains the newest distance cost counts as we iterate
		 * through the characters of String s. Each time we increment the index of String t we are
		 * comparing, d is copied to p, the second int[]. Doing so allows us to retain the previous
		 * cost counts as required by the algorithm (taking the minimum of the cost count to the
		 * left, up one, and diagonally up and to the left of the current cost count being
		 * calculated). (Note that the arrays aren't really copied anymore, just switched...this is
		 * clearly much better than cloning an array or doing a System.arraycopy() each time through
		 * the outer loop.) Effectively, the difference between the two implementations is this one
		 * does not cause an out of memory condition when calculating the LD over two very large
		 * strings.
		 */

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		}
		else if (m == 0) {
			return n;
		}

		if (n > m) {
			// swap the input strings to consume less memory
			final CharSequence tmp = s;
			s = t;
			t = tmp;
			n = m;
			m = t.length();
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}

	/**
	 * <p>
	 * Find the Levenshtein distance between two Strings if it's less than or equal to a given
	 * threshold.
	 * </p>
	 * <p>
	 * This is the number of changes needed to change one String into another, where each change is
	 * a single character modification (deletion, insertion or substitution).
	 * </p>
	 * <p>
	 * This implementation follows from Algorithms on Strings, Trees and Sequences by Dan Gusfield
	 * and Chas Emerick's implementation of the Levenshtein distance algorithm from <a
	 * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a>
	 * </p>
	 * 
	 * <pre>
	 * Strings.getLevenshteinDistance(null, *, *)             = IllegalArgumentException
	 * Strings.getLevenshteinDistance(*, null, *)             = IllegalArgumentException
	 * Strings.getLevenshteinDistance(*, *, -1)               = IllegalArgumentException
	 * Strings.getLevenshteinDistance("","", 0)               = 0
	 * Strings.getLevenshteinDistance("aaapppp", "", 8)       = 7
	 * Strings.getLevenshteinDistance("aaapppp", "", 7)       = 7
	 * Strings.getLevenshteinDistance("aaapppp", "", 6))      = -1
	 * Strings.getLevenshteinDistance("elephant", "hippo", 7) = 7
	 * Strings.getLevenshteinDistance("elephant", "hippo", 6) = -1
	 * Strings.getLevenshteinDistance("hippo", "elephant", 7) = 7
	 * Strings.getLevenshteinDistance("hippo", "elephant", 6) = -1
	 * </pre>
	 * 
	 * @param s the first String, must not be null
	 * @param t the second String, must not be null
	 * @param threshold the target threshold, must not be negative
	 * @return result distance, or {@code -1} if the distance would be greater than the threshold
	 * @throws IllegalArgumentException if either String input {@code null} or negative threshold
	 */
	public static int getLevenshteinDistance(CharSequence s, CharSequence t, final int threshold) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}
		if (threshold < 0) {
			throw new IllegalArgumentException("Threshold must not be negative");
		}

		/*
		 * This implementation only computes the distance if it's less than or equal to the
		 * threshold value, returning -1 if it's greater. The advantage is performance: unbounded
		 * distance is O(nm), but a bound of k allows us to reduce it to O(km) time by only
		 * computing a diagonal stripe of width 2k + 1 of the cost table. It is also possible to use
		 * this to compute the unbounded Levenshtein distance by starting the threshold at 1 and
		 * doubling each time until the distance is found; this is O(dm), where d is the distance.
		 * One subtlety comes from needing to ignore entries on the border of our stripe eg. p[] =
		 * |#|#|#|* d[] = *|#|#|#| We must ignore the entry to the left of the leftmost member We
		 * must ignore the entry above the rightmost member Another subtlety comes from our stripe
		 * running off the matrix if the strings aren't of the same size. Since string s is always
		 * swapped to be the shorter of the two, the stripe will always run off to the upper right
		 * instead of the lower left of the matrix. As a concrete example, suppose s is of length 5,
		 * t is of length 7, and our threshold is 1. In this case we're going to walk a stripe of
		 * length 3. The matrix would look like so: 1 2 3 4 5 1 |#|#| | | | 2 |#|#|#| | | 3 |
		 * |#|#|#| | 4 | | |#|#|#| 5 | | | |#|#| 6 | | | | |#| 7 | | | | | | Note how the stripe
		 * leads off the table as there is no possible way to turn a string of length 5 into one of
		 * length 7 in edit distance of 1. Additionally, this implementation decreases memory usage
		 * by using two single-dimensional arrays and swapping them back and forth instead of
		 * allocating an entire n by m matrix. This requires a few minor changes, such as
		 * immediately returning when it's detected that the stripe has run off the matrix and
		 * initially filling the arrays with large values so that entries we don't compute are
		 * ignored. See Algorithms on Strings, Trees and Sequences by Dan Gusfield for some
		 * discussion.
		 */

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		// if one string is empty, the edit distance is necessarily the length of the other
		if (n == 0) {
			return m <= threshold ? m : -1;
		}
		else if (m == 0) {
			return n <= threshold ? n : -1;
		}

		if (n > m) {
			// swap the two strings to consume less memory
			final CharSequence tmp = s;
			s = t;
			t = tmp;
			n = m;
			m = t.length();
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// fill in starting table values
		final int boundary = Math.min(n, threshold) + 1;
		for (int i = 0; i < boundary; i++) {
			p[i] = i;
		}
		// these fills ensure that the value above the rightmost entry of our
		// stripe will be ignored in following loop iterations
		Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
		Arrays.fill(d, Integer.MAX_VALUE);

		// iterates through t
		for (int j = 1; j <= m; j++) {
			final char t_j = t.charAt(j - 1); // jth character of t
			d[0] = j;

			// compute stripe indices, constrain to array size
			final int min = Math.max(1, j - threshold);
			final int max = Math.min(n, j + threshold);

			// the stripe may lead off of the table if s and t are of different sizes
			if (min > max) {
				return -1;
			}

			// ignore entry left of leftmost
			if (min > 1) {
				d[min - 1] = Integer.MAX_VALUE;
			}

			// iterates through [min, max] in s
			for (int i = min; i <= max; i++) {
				if (s.charAt(i - 1) == t_j) {
					// diagonally left and up
					d[i] = p[i - 1];
				}
				else {
					// 1 + minimum of cell to the left, to the top, diagonally left and up
					d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
				}
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// if p[n] is greater than the threshold, there's no guarantee on it being the correct
		// distance
		if (p[n] <= threshold) {
			return p[n];
		}
		return -1;
	}

	// startsWith
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a CharSequence starts with a specified prefix.
	 * </p>
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to
	 * be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWith(null, null)      = true
	 * Strings.startsWith(null, "abc")     = false
	 * Strings.startsWith("abcdef", null)  = false
	 * Strings.startsWith("abcdef", "abc") = true
	 * Strings.startsWith("ABCDEF", "abc") = false
	 * </pre>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * @param str the CharSequence to check, may be null
	 * @param prefix the prefix to find, may be null
	 * @return {@code true} if the CharSequence starts with the prefix, case sensitive, or both
	 *         {@code null}
	 */
	public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
		return startsWith(str, prefix, false);
	}

	/**
	 * <p>
	 * Case insensitive check if a CharSequence starts with a specified prefix.
	 * </p>
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to
	 * be equal. The comparison is case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWithIgnoreCase(null, null)      = true
	 * Strings.startsWithIgnoreCase(null, "abc")     = false
	 * Strings.startsWithIgnoreCase("abcdef", null)  = false
	 * Strings.startsWithIgnoreCase("abcdef", "abc") = true
	 * Strings.startsWithIgnoreCase("ABCDEF", "abc") = true
	 * </pre>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * @param str the CharSequence to check, may be null
	 * @param prefix the prefix to find, may be null
	 * @return {@code true} if the CharSequence starts with the prefix, case insensitive, or both
	 *         {@code null}
	 */
	public static boolean startsWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
		return startsWith(str, prefix, true);
	}

	/**
	 * <p>
	 * Check if a CharSequence starts with a specified prefix (optionally case insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * @param str the CharSequence to check, may be null
	 * @param prefix the prefix to find, may be null
	 * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
	 * @return {@code true} if the CharSequence starts with the prefix or both {@code null}
	 */
	private static boolean startsWith(final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
		if (str == null || prefix == null) {
			return str == null && prefix == null;
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return CharSequences.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
	}

	/**
	 * <p>
	 * Check if a CharSequence starts with any of an array of specified strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWithAny(null, null)      = false
	 * Strings.startsWithAny(null, new String[] {"abc"})  = false
	 * Strings.startsWithAny("abcxyz", null)     = false
	 * Strings.startsWithAny("abcxyz", new String[] {""}) = false
	 * Strings.startsWithAny("abcxyz", new String[] {"abc"}) = true
	 * Strings.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param searchStrings the CharSequences to find, may be null or empty
	 * @return {@code true} if the CharSequence starts with any of the the prefixes, case
	 *         insensitive, or both {@code null}
	 */
	public static boolean startsWithAny(final CharSequence string, final CharSequence... searchStrings) {
		if (isEmpty(string) || Arrays.isEmpty(searchStrings)) {
			return false;
		}
		for (final CharSequence searchString : searchStrings) {
			if (Strings.startsWith(string, searchString)) {
				return true;
			}
		}
		return false;
	}

	// endsWith
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a CharSequence ends with a specified suffix.
	 * </p>
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to
	 * be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWith(null, null)      = true
	 * Strings.endsWith(null, "def")     = false
	 * Strings.endsWith("abcdef", null)  = false
	 * Strings.endsWith("abcdef", "def") = true
	 * Strings.endsWith("ABCDEF", "def") = false
	 * Strings.endsWith("ABCDEF", "cde") = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str the CharSequence to check, may be null
	 * @param suffix the suffix to find, may be null
	 * @return {@code true} if the CharSequence ends with the suffix, case sensitive, or both
	 *         {@code null}
	 */
	public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
		return endsWith(str, suffix, false);
	}

	/**
	 * <p>
	 * Case insensitive check if a CharSequence ends with a specified suffix.
	 * </p>
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to
	 * be equal. The comparison is case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWithIgnoreCase(null, null)      = true
	 * Strings.endsWithIgnoreCase(null, "def")     = false
	 * Strings.endsWithIgnoreCase("abcdef", null)  = false
	 * Strings.endsWithIgnoreCase("abcdef", "def") = true
	 * Strings.endsWithIgnoreCase("ABCDEF", "def") = true
	 * Strings.endsWithIgnoreCase("ABCDEF", "cde") = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str the CharSequence to check, may be null
	 * @param suffix the suffix to find, may be null
	 * @return {@code true} if the CharSequence ends with the suffix, case insensitive, or both
	 *         {@code null}
	 */
	public static boolean endsWithIgnoreCase(final CharSequence str, final CharSequence suffix) {
		return endsWith(str, suffix, true);
	}

	/**
	 * <p>
	 * Check if a CharSequence ends with a specified suffix (optionally case insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str the CharSequence to check, may be null
	 * @param suffix the suffix to find, may be null
	 * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
	 * @return {@code true} if the CharSequence starts with the prefix or both {@code null}
	 */
	private static boolean endsWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
		if (str == null || suffix == null) {
			return str == null && suffix == null;
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		final int strOffset = str.length() - suffix.length();
		return CharSequences.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
	}

	/**
	 * <p>
	 * Similar to <a
	 * href="http://www.w3.org/TR/xpath/#function-normalize-space">http://www.w3.org/TR
	 * /xpath/#function-normalize -space</a>
	 * </p>
	 * <p>
	 * The function returns the argument string with whitespace normalized by using
	 * <code>{@link #trim(String)}</code> to remove leading and trailing whitespace and then
	 * replacing sequences of whitespace characters by a single space.
	 * </p>
	 * In XML Whitespace characters are the same as those allowed by the <a
	 * href="http://www.w3.org/TR/REC-xml/#NT-S">S</a> production, which is S ::= (#x20 | #x9 | #xD
	 * | #xA)+
	 * <p>
	 * Java's regexp pattern \s defines whitespace as [ \t\n\x0B\f\r]
	 * <p>
	 * For reference:
	 * <ul>
	 * <li>\x0B = vertical tab</li>
	 * <li>\f = #xC = form feed</li>
	 * <li>#x20 = space</li>
	 * <li>#x9 = \t</li>
	 * <li>#xA = \n</li>
	 * <li>#xD = \r</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The difference is that Java's whitespace includes vertical tab and form feed, which this
	 * functional will also normalize. Additionally <code>{@link #trim(String)}</code> removes
	 * control characters (char &lt;= 32) from both ends of this String.
	 * </p>
	 * 
	 * @see Pattern
	 * @see #trim(String)
	 * @see <a
	 *      href="http://www.w3.org/TR/xpath/#function-normalize-space">http://www.w3.org/TR/xpath/#function-normalize-space</a>
	 * @param str the source String to normalize whitespaces from, may be null
	 * @return the modified string with whitespace normalized, {@code null} if null String input
	 */
	public static String normalizeSpace(final String str) {
		if (str == null) {
			return null;
		}
		return WHITESPACE_PATTERN.matcher(trim(str)).replaceAll(SPACE);
	}

	/**
	 * <p>
	 * Check if a CharSequence ends with any of an array of specified strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWithAny(null, null)      = false
	 * Strings.endsWithAny(null, new String[] {"abc"})  = false
	 * Strings.endsWithAny("abcxyz", null)     = false
	 * Strings.endsWithAny("abcxyz", new String[] {""}) = true
	 * Strings.endsWithAny("abcxyz", new String[] {"xyz"}) = true
	 * Strings.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param searchStrings the CharSequences to find, may be null or empty
	 * @return {@code true} if the CharSequence ends with any of the the prefixes, case insensitive,
	 *         or both {@code null}
	 */
	public static boolean endsWithAny(final CharSequence string, final CharSequence... searchStrings) {
		if (isEmpty(string) || Arrays.isEmpty(searchStrings)) {
			return false;
		}
		for (final CharSequence searchString : searchStrings) {
			if (Strings.endsWith(string, searchString)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Appends the suffix to the end of the string if the string does not already end in the suffix.
	 * 
	 * @param str The string.
	 * @param suffix The suffix to append to the end of the string.
	 * @param ignoreCase Indicates whether the compare should ignore case.
	 * @param suffixes Additional suffixes that are valid terminators (optional).
	 * @return A new String if suffix was appened, the same string otherwise.
	 */
	private static String appendIfMissing(final String str, final CharSequence suffix, final boolean ignoreCase,
			final CharSequence... suffixes) {
		if (str == null || isEmpty(suffix) || endsWith(str, suffix, ignoreCase)) {
			return str;
		}
		if (suffixes != null && suffixes.length > 0) {
			for (final CharSequence s : suffixes) {
				if (endsWith(str, s, ignoreCase)) {
					return str;
				}
			}
		}
		return str + suffix.toString();
	}

	/**
	 * Appends the suffix to the end of the string if the string does not already end with any the
	 * suffixes.
	 * 
	 * <pre>
	 * Strings.appendIfMissing(null, null) = null
	 * Strings.appendIfMissing("abc", null) = "abc"
	 * Strings.appendIfMissing("", "xyz") = "xyz"
	 * Strings.appendIfMissing("abc", "xyz") = "abcxyz"
	 * Strings.appendIfMissing("abcxyz", "xyz") = "abcxyz"
	 * Strings.appendIfMissing("abcXYZ", "xyz") = "abcXYZxyz"
	 * </pre>
	 * <p>
	 * With additional suffixes,
	 * </p>
	 * 
	 * <pre>
	 * Strings.appendIfMissing(null, null, null) = null
	 * Strings.appendIfMissing("abc", null, null) = "abc"
	 * Strings.appendIfMissing("", "xyz", null) = "xyz"
	 * Strings.appendIfMissing("abc", "xyz", new CharSequence[]{null}) = "abcxyz"
	 * Strings.appendIfMissing("abc", "xyz", "") = "abc"
	 * Strings.appendIfMissing("abc", "xyz", "mno") = "abcxyz"
	 * Strings.appendIfMissing("abcxyz", "xyz", "mno") = "abcxyz"
	 * Strings.appendIfMissing("abcmno", "xyz", "mno") = "abcmno"
	 * Strings.appendIfMissing("abcXYZ", "xyz", "mno") = "abcXYZxyz"
	 * Strings.appendIfMissing("abcMNO", "xyz", "mno") = "abcMNOxyz"
	 * </pre>
	 * 
	 * @param str The string.
	 * @param suffix The suffix to append to the end of the string.
	 * @param suffixes Additional suffixes that are valid terminators.
	 * @return A new String if suffix was appened, the same string otherwise.
	 */
	public static String appendIfMissing(final String str, final CharSequence suffix, final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, false, suffixes);
	}

	/**
	 * Appends the suffix to the end of the string if the string does not already end, case
	 * insensitive, with any of the suffixes.
	 * 
	 * <pre>
	 * Strings.appendIfMissingIgnoreCase(null, null) = null
	 * Strings.appendIfMissingIgnoreCase("abc", null) = "abc"
	 * Strings.appendIfMissingIgnoreCase("", "xyz") = "xyz"
	 * Strings.appendIfMissingIgnoreCase("abc", "xyz") = "abcxyz"
	 * Strings.appendIfMissingIgnoreCase("abcxyz", "xyz") = "abcxyz"
	 * Strings.appendIfMissingIgnoreCase("abcXYZ", "xyz") = "abcXYZ"
	 * </pre>
	 * <p>
	 * With additional suffixes,
	 * </p>
	 * 
	 * <pre>
	 * Strings.appendIfMissingIgnoreCase(null, null, null) = null
	 * Strings.appendIfMissingIgnoreCase("abc", null, null) = "abc"
	 * Strings.appendIfMissingIgnoreCase("", "xyz", null) = "xyz"
	 * Strings.appendIfMissingIgnoreCase("abc", "xyz", new CharSequence[]{null}) = "abcxyz"
	 * Strings.appendIfMissingIgnoreCase("abc", "xyz", "") = "abc"
	 * Strings.appendIfMissingIgnoreCase("abc", "xyz", "mno") = "axyz"
	 * Strings.appendIfMissingIgnoreCase("abcxyz", "xyz", "mno") = "abcxyz"
	 * Strings.appendIfMissingIgnoreCase("abcmno", "xyz", "mno") = "abcmno"
	 * Strings.appendIfMissingIgnoreCase("abcXYZ", "xyz", "mno") = "abcXYZ"
	 * Strings.appendIfMissingIgnoreCase("abcMNO", "xyz", "mno") = "abcMNO"
	 * </pre>
	 * 
	 * @param str The string.
	 * @param suffix The suffix to append to the end of the string.
	 * @param suffixes Additional suffixes that are valid terminators.
	 * @return A new String if suffix was appened, the same string otherwise.
	 */
	public static String appendIfMissingIgnoreCase(final String str, final CharSequence suffix,
			final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, true, suffixes);
	}

	/**
	 * Prepends the prefix to the start of the string if the string does not already start with any
	 * of the prefixes.
	 * 
	 * @param str The string.
	 * @param prefix The prefix to prepend to the start of the string.
	 * @param ignoreCase Indicates whether the compare should ignore case.
	 * @param prefixes Additional prefixes that are valid (optional).
	 * @return A new String if prefix was prepended, the same string otherwise.
	 */
	private static String prependIfMissing(final String str, final CharSequence prefix, final boolean ignoreCase,
			final CharSequence... prefixes) {
		if (str == null || isEmpty(prefix) || startsWith(str, prefix, ignoreCase)) {
			return str;
		}
		if (prefixes != null && prefixes.length > 0) {
			for (final CharSequence p : prefixes) {
				if (startsWith(str, p, ignoreCase)) {
					return str;
				}
			}
		}
		return prefix.toString() + str;
	}

	/**
	 * Prepends the prefix to the start of the string if the string does not already start with any
	 * of the prefixes.
	 * 
	 * <pre>
	 * Strings.prependIfMissing(null, null) = null
	 * Strings.prependIfMissing("abc", null) = "abc"
	 * Strings.prependIfMissing("", "xyz") = "xyz"
	 * Strings.prependIfMissing("abc", "xyz") = "xyzabc"
	 * Strings.prependIfMissing("xyzabc", "xyz") = "xyzabc"
	 * Strings.prependIfMissing("XYZabc", "xyz") = "xyzXYZabc"
	 * </pre>
	 * <p>
	 * With additional prefixes,
	 * </p>
	 * 
	 * <pre>
	 * Strings.prependIfMissing(null, null, null) = null
	 * Strings.prependIfMissing("abc", null, null) = "abc"
	 * Strings.prependIfMissing("", "xyz", null) = "xyz"
	 * Strings.prependIfMissing("abc", "xyz", new CharSequence[]{null}) = "xyzabc"
	 * Strings.prependIfMissing("abc", "xyz", "") = "abc"
	 * Strings.prependIfMissing("abc", "xyz", "mno") = "xyzabc"
	 * Strings.prependIfMissing("xyzabc", "xyz", "mno") = "xyzabc"
	 * Strings.prependIfMissing("mnoabc", "xyz", "mno") = "mnoabc"
	 * Strings.prependIfMissing("XYZabc", "xyz", "mno") = "xyzXYZabc"
	 * Strings.prependIfMissing("MNOabc", "xyz", "mno") = "xyzMNOabc"
	 * </pre>
	 * 
	 * @param str The string.
	 * @param prefix The prefix to prepend to the start of the string.
	 * @param prefixes Additional prefixes that are valid.
	 * @return A new String if prefix was prepended, the same string otherwise.
	 */
	public static String prependIfMissing(final String str, final CharSequence prefix, final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, false, prefixes);
	}

	/**
	 * Prepends the prefix to the start of the string if the string does not already start, case
	 * insensitive, with any of the prefixes.
	 * 
	 * <pre>
	 * Strings.prependIfMissingIgnoreCase(null, null) = null
	 * Strings.prependIfMissingIgnoreCase("abc", null) = "abc"
	 * Strings.prependIfMissingIgnoreCase("", "xyz") = "xyz"
	 * Strings.prependIfMissingIgnoreCase("abc", "xyz") = "xyzabc"
	 * Strings.prependIfMissingIgnoreCase("xyzabc", "xyz") = "xyzabc"
	 * Strings.prependIfMissingIgnoreCase("XYZabc", "xyz") = "XYZabc"
	 * </pre>
	 * <p>
	 * With additional prefixes,
	 * </p>
	 * 
	 * <pre>
	 * Strings.prependIfMissingIgnoreCase(null, null, null) = null
	 * Strings.prependIfMissingIgnoreCase("abc", null, null) = "abc"
	 * Strings.prependIfMissingIgnoreCase("", "xyz", null) = "xyz"
	 * Strings.prependIfMissingIgnoreCase("abc", "xyz", new CharSequence[]{null}) = "xyzabc"
	 * Strings.prependIfMissingIgnoreCase("abc", "xyz", "") = "abc"
	 * Strings.prependIfMissingIgnoreCase("abc", "xyz", "mno") = "xyzabc"
	 * Strings.prependIfMissingIgnoreCase("xyzabc", "xyz", "mno") = "xyzabc"
	 * Strings.prependIfMissingIgnoreCase("mnoabc", "xyz", "mno") = "mnoabc"
	 * Strings.prependIfMissingIgnoreCase("XYZabc", "xyz", "mno") = "XYZabc"
	 * Strings.prependIfMissingIgnoreCase("MNOabc", "xyz", "mno") = "MNOabc"
	 * </pre>
	 * 
	 * @param str The string.
	 * @param prefix The prefix to prepend to the start of the string.
	 * @param prefixes Additional prefixes that are valid (optional).
	 * @return A new String if prefix was prepended, the same string otherwise.
	 */
	public static String prependIfMissingIgnoreCase(final String str, final CharSequence prefix,
			final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, true, prefixes);
	}

	/**
	 * Converts a <code>byte[]</code> to a String using the specified character encoding.
	 * 
	 * @param bytes the byte array to read from
	 * @param charsetName the encoding to use, if null then use the platform default
	 * @return a new String
	 * @throws UnsupportedEncodingException If the named charset is not supported
	 * @throws NullPointerException if the input is null
	 */
	public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
		return charsetName == null ? new String(bytes) : new String(bytes, charsetName);
	}

	// ---------------------------------------------------------------------
	// General convenience methods for working with Strings
	// ---------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a String is a control character(c < 0x20 && c != '\t' && c != '\r' &&& c != '\n')
	 * text.
	 * </p>
	 * 
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is a control string
	 */
	public static boolean isControl(CharSequence str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			char c = str.charAt(i);
			if (c >= 0x20 || c == '\t' || c == '\r' || c == '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a String is a printable text.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isPrintable(null)      = false
	 * Strings.isPrintable("")        = false
	 * Strings.isPrintable(" ")       = false
	 * Strings.isPrintable("bob")     = true
	 * Strings.isPrintable("\u0003 \r\n") = false
	 * </pre>
	 * 
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is a printable string
	 */
	public static boolean isPrintable(CharSequence str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			char c = str.charAt(i);
			if (c > 0x20 && !Character.isWhitespace(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Trim all occurences of the leading/trailing space character from the given String[].
	 * 
	 * @param strs the String[] to trim
	 * @return the trimmed String[]
	 */
	public static String[] trimAll(String[] strs) {
		if (strs == null) {
			return null;
		}

		for (int i = 0; i < strs.length; i++) {
			strs[i] = trim(strs[i]);
		}

		return strs;
	}

	/**
	 * <p>
	 * Strips whitespace from the start of every String in an array. Whitespace is defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * <p>
	 * A new array is returned each time, except for length zero. A <code>null</code> array will
	 * return <code>null</code>. An empty array will return itself. A <code>null</code> array entry
	 * will be ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripAll(null)             = null
	 * Strings.stripAll([])               = []
	 * Strings.stripAll(["abc", "  abc"]) = ["abc", "abc"]
	 * Strings.stripAll(["  abc", null])  = ["abc", null]
	 * Strings.stripAll(["  abc  ", null])  = ["abc  ", null]
	 * </pre>
	 * 
	 * @param strs the array to remove whitespace from, may be null
	 * @return the stripped Strings, <code>null</code> if null array input
	 */
	public static String[] stripAllStart(CharSequence[] strs) {
		return stripAllStart(strs, null);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the start of every String in an array.
	 * </p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 * <p>
	 * A new array is returned each time, except for length zero. A <code>null</code> array will
	 * return <code>null</code>. An empty array will return itself. A <code>null</code> array entry
	 * will be ignored. A <code>null</code> stripChars will strip whitespace as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripAll(null, *)                = null
	 * Strings.stripAll([], *)                  = []
	 * Strings.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
	 * Strings.stripAll(["  abc", null], null)  = ["abc", null]
	 * Strings.stripAll(["  abc  ", null], null)  = ["  abc", null]
	 * Strings.stripAll(["  abc  ", null], "yz")= ["  abc  ", null]
	 * Strings.stripAll(["yabcz", null], "yz")  = ["abcz", null]
	 * </pre>
	 * 
	 * @param strs the array to remove characters from, may be null
	 * @param stripChars the characters to remove, null treated as whitespace
	 * @return the stripped Strings, <code>null</code> if null array input
	 */
	public static String[] stripAllStart(CharSequence[] strs, String stripChars) {
		if (strs == null) {
			return null;
		}

		int strsLen = strs.length;
		if (strsLen == 0) {
			return EMPTY_ARRAY;
		}

		String[] newArr = new String[strsLen];
		for (int i = 0; i < strsLen; i++) {
			newArr[i] = stripStart(strs[i], stripChars);
		}
		return newArr;
	}

	/**
	 * <p>
	 * Strips whitespace from the end of every String in an array. Whitespace is defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * <p>
	 * A new array is returned each time, except for length zero. A <code>null</code> array will
	 * return <code>null</code>. An empty array will return itself. A <code>null</code> array entry
	 * will be ignored.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripAll(null)             = null
	 * Strings.stripAll([])               = []
	 * Strings.stripAll(["abc", "  abc"]) = ["abc", "abc"]
	 * Strings.stripAll(["abc  ", null])  = ["abc", null]
	 * Strings.stripAll(["  abc  ", null])  = ["  abc", null]
	 * </pre>
	 * 
	 * @param strs the array to remove whitespace from, may be null
	 * @return the stripped Strings, <code>null</code> if null array input
	 */
	public static String[] stripAllEnd(CharSequence[] strs) {
		return stripAllEnd(strs, null);
	}

	/**
	 * <p>
	 * Strips any of a set of characters from the end of every String in an array.
	 * </p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 * <p>
	 * A new array is returned each time, except for length zero. A <code>null</code> array will
	 * return <code>null</code>. An empty array will return itself. A <code>null</code> array entry
	 * will be ignored. A <code>null</code> stripChars will strip whitespace as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.stripAll(null, *)                = null
	 * Strings.stripAll([], *)                  = []
	 * Strings.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
	 * Strings.stripAll(["abc  ", null], null)  = ["abc", null]
	 * Strings.stripAll(["  abc  ", null], null)  = ["  abc", null]
	 * Strings.stripAll(["  abc  ", null], "yz")= ["  abc  ", null]
	 * Strings.stripAll(["yabcz", null], "yz")  = ["yabc", null]
	 * </pre>
	 * 
	 * @param strs the array to remove characters from, may be null
	 * @param stripChars the characters to remove, null treated as whitespace
	 * @return the stripped Strings, <code>null</code> if null array input
	 */
	public static String[] stripAllEnd(CharSequence[] strs, String stripChars) {
		if (strs == null) {
			return null;
		}

		int strsLen = strs.length;
		if (strsLen == 0) {
			return EMPTY_ARRAY;
		}

		String[] newArr = new String[strsLen];
		for (int i = 0; i < strsLen; i++) {
			newArr[i] = stripEnd(strs[i], stripChars);
		}
		return newArr;
	}

	/**
	 * <p>
	 * Splits the provided text into an array.
	 * 
	 * <pre>
	 * Strings.splitChars(null)         = null
	 * Strings.splitChars("")           = []
	 * Strings.splitChars("a d")        = ["a", " ", "d"]
	 * </pre>
	 * 
	 * @param str the String to parse, may be null
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] splitChars(String str) {
		if (str == null) {
			return null;
		}

		int len = str.length();
		String[] ss = new String[len];
		for (int i = 0; i < len; i++) {
			ss[i] = str.substring(i, i + 1);
		}

		return ss;
	}

	/**
	 * <p>
	 * Check if a String starts with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWithChars(null, null)      = false
	 * Strings.startsWithChars(null, "abc")     = false
	 * Strings.startsWithChars("abcxyz", null)  = false
	 * Strings.startsWithChars("abcxyz", "")    = false
	 * Strings.startsWithChars("abcxyz", "abc") = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param chars the chars to find, may be null or empty
	 * @return <code>true</code> if the String starts with any of the chars
	 */
	public static boolean startsWithChars(CharSequence string, String chars) {
		return startsWithChars(string, chars, false);
	}

	/**
	 * <p>
	 * Check if a String starts with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWithChars(null, null)      = false
	 * Strings.startsWithChars(null, "abc")     = false
	 * Strings.startsWithChars("abcxyz", null)  = false
	 * Strings.startsWithChars("abcxyz", "")    = false
	 * Strings.startsWithChars("abcxyz", "abc") = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param chars the chars to find, may be null or empty
	 * @param ignoreCase inidicates whether the compare should ignore case (case insensitive) or
	 *            not.
	 * @return <code>true</code> if the String starts with any of the the prefixes
	 */
	public static boolean startsWithChars(CharSequence string, String chars, boolean ignoreCase) {
		if (isEmpty(string) || isEmpty(chars)) {
			return false;
		}

		char s = string.charAt(0);
		if (ignoreCase) {
			s = Character.toLowerCase(s);
		}
		for (int i = 0; i < chars.length(); i++) {
			char ch = chars.charAt(i);
			if (ignoreCase) {
				ch = Character.toLowerCase(ch);
			}
			if (s == ch) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Check if a String starts with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWithChar(null, 'a')     = false
	 * Strings.startsWithChar("abcxyz", 'a') = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param find the char to find, may be null
	 * @return <code>true</code> if the String starts with the char
	 */
	public static boolean startsWithChar(CharSequence string, char find) {
		return startsWithChar(string, find, false);
	}

	/**
	 * <p>
	 * Check if a String starts with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWithChar(null, 'a')     = false
	 * Strings.startsWithChar("abcxyz", 'a') = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param find the char to find, may be null
	 * @param ignoreCase inidicates whether the compare should ignore case (case insensitive) or
	 *            not.
	 * @return <code>true</code> if the String starts with the char
	 */
	public static boolean startsWithChar(CharSequence string, char find, boolean ignoreCase) {
		if (isEmpty(string)) {
			return false;
		}

		char s = string.charAt(0);
		if (ignoreCase) {
			s = Character.toLowerCase(s);
		}
		if (ignoreCase) {
			find = Character.toLowerCase(find);
		}
		return s == find;
	}

	/**
	 * <p>
	 * Check if a String starts with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWithChars(null, null)      = false
	 * Strings.endsWithChars(null, "abc")     = false
	 * Strings.endsWithChars("abcxyz", null)  = false
	 * Strings.endsWithChars("abcxyz", "")    = false
	 * Strings.endsWithChars("abcxyz", "abc") = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param chars the chars to find, may be null or empty
	 * @return <code>true</code> if the String ends with any of the chars
	 */
	public static boolean endsWithChars(String string, String chars) {
		return endsWithChars(string, chars, false);
	}

	/**
	 * <p>
	 * Check if a String starts with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWithChars(null, null)      = false
	 * Strings.endsWithChars(null, "abc")     = false
	 * Strings.endsWithChars("abcxyz", null)  = false
	 * Strings.endsWithChars("abcxyz", "")    = false
	 * Strings.endsWithChars("abcxyz", "abc") = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param chars the chars to find, may be null or empty
	 * @param ignoreCase inidicates whether the compare should ignore case (case insensitive) or
	 *            not.
	 * @return <code>true</code> if the String ends with any of the the prefixes
	 */
	public static boolean endsWithChars(String string, String chars, boolean ignoreCase) {
		if (isEmpty(string) || isEmpty(chars)) {
			return false;
		}

		char e = string.charAt(string.length() - 1);
		if (ignoreCase) {
			e = Character.toLowerCase(e);
		}
		for (int i = 0; i < chars.length(); i++) {
			char ch = chars.charAt(i);
			if (ignoreCase) {
				ch = Character.toLowerCase(ch);
			}
			if (e == ch) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Check if a String starts with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWithChar(null, 'a')     = false
	 * Strings.endsWithChar("abcxyz", 'a') = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param find the char to find, may be null
	 * @return <code>true</code> if the String starts with the char
	 */
	public static boolean endsWithChar(String string, char find) {
		return startsWithChar(string, find, false);
	}

	/**
	 * <p>
	 * Check if a String ends with any char of the specified chars.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWithChar(null, 'a')     = false
	 * Strings.endsWithChar("abcxyz", 'z') = true
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param find the char to find, may be null
	 * @param ignoreCase inidicates whether the compare should ignore case (case insensitive) or
	 *            not.
	 * @return <code>true</code> if the String ends with the char
	 */
	public static boolean endsWithChar(String string, char find, boolean ignoreCase) {
		if (isEmpty(string)) {
			return false;
		}

		char ch = string.charAt(string.length() - 1);
		if (ignoreCase) {
			ch = Character.toLowerCase(ch);
		}
		if (ignoreCase) {
			find = Character.toLowerCase(find);
		}
		return ch == find;
	}

	/**
	 * Test whether the given string matches the given substring at the given index.
	 * 
	 * @param str the original string (or StringBuffer)
	 * @param index the index in the original string to start matching against
	 * @param substring the substring to match at the given index
	 * @return true if the given string matches the given substring at the given index
	 */
	public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;
			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Count the occurrences of the substring in string s.
	 * 
	 * @param str string to search in. Return 0 if this is null.
	 * @param sub string to search for. Return 0 if this is null.
	 * @return count
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with formatted Strings
	// ---------------------------------------------------------------------

	/**
	 * Quote the given String with single quotes.
	 * 
	 * @param str the input String (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or
	 *         <code>null<code> if the input was <code>null</code>
	 */
	public static String quote(String str) {
		return (str != null ? "'" + str + "'" : null);
	}

	/**
	 * Turn the given Object into a String with single quotes if it is a String; keeping the Object
	 * as-is else.
	 * 
	 * @param obj the input Object (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or the input object as-is if not a String
	 */
	public static Object quoteIfString(Object obj) {
		return (obj instanceof String ? quote((String)obj) : obj);
	}

	/**
	 * Unqualify a string qualified by a '.' dot character. For example, "this.name.is.qualified",
	 * returns "qualified".
	 * 
	 * @param qualifiedName the qualified name
	 * @return unqualified string
	 */
	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	/**
	 * Unqualify a string qualified by a separator character. For example, "this:name:is:qualified"
	 * returns "qualified" if using a ':' separator.
	 * 
	 * @param qualifiedName the qualified name
	 * @param separator the separator
	 * @return unqualified string
	 */
	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with String arrays
	// ---------------------------------------------------------------------
	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param coll the Collection to display
	 * @return the delimited String
	 */
	public static String join(Collection<?> coll) {
		return join(coll, EMPTY);
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param coll the Collection to display
	 * @param delimiter the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String join(Collection<?> coll, String delimiter) {
		return join(coll, delimiter, EMPTY, EMPTY);
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param coll the Collection to display
	 * @param delimiter the delimiter to use (probably a ",")
	 * @param prefix the String to start each element with
	 * @param suffix the String to end each element with
	 * @return the delimited String
	 */
	public static String join(Collection<?> coll, String delimiter, String prefix, String suffix) {
		if (coll == null || coll.isEmpty()) {
			return "";
		}
		return join(coll.iterator(), delimiter, prefix, suffix);
	}

	/**
	 * Convenience method to return a Iterator as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param it the iterator to display
	 * @param delimiter the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String join(Iterator<?> it, String delimiter) {
		return join(it, delimiter, EMPTY, EMPTY);
	}

	/**
	 * Convenience method to return a Iterator as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param it the iterator to display
	 * @param delimiter the delimiter to use (probably a ",")
	 * @param prefix the String to start each element with
	 * @param suffix the String to end each element with
	 * @return the delimited String
	 */
	public static String join(Iterator<?> it, String delimiter, String prefix, String suffix) {
		if (it == null) {
			return null;
		}
		if (delimiter == null) {
			delimiter = EMPTY;
		}
		if (prefix == null) {
			prefix = EMPTY;
		}
		if (suffix == null) {
			suffix = EMPTY;
		}

		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(prefix).append(defaultString(it.next())).append(suffix);
			if (it.hasNext()) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(boolean[] array) {
		return join(array, EMPTY);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(boolean[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(boolean[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(byte[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(byte[] array) {
		return join(array, EMPTY);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(byte[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(char[] array) {
		return join(array);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(char[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(char[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(double[] array) {
		return join(array, EMPTY);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(double[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(double[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(float[] array) {
		return join(array, EMPTY);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(float[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(float[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(short[] array) {
		return join(array, EMPTY);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(short[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(short[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(int[] array) {
		return join(array, EMPTY);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(int[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(int[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(long[] array) {
		return join(array, EMPTY);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(long[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 * <p>
	 * No delimiter is added before or after the list. A <code>null</code> separator is the same as
	 * an empty String (""). Null objects or empty strings within the array are represented by empty
	 * strings.
	 * </p>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from. It is an error to pass in an end
	 *            index past the end of the array
	 * @param endIndex the index to stop joining from (exclusive). It is an error to pass in an end
	 *            index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(long[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) + len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				sb.append(separator);
			}
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * Copy the given Collection into a String array. The Collection must contain String elements
	 * only.
	 * 
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in Collection was <code>null</code>
	 *         )
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * Copy the given Enumeration into a String array. The Enumeration must contain String elements
	 * only.
	 * 
	 * @param enumeration the Enumeration to copy
	 * @return the String array (<code>null</code> if the passed-in Enumeration was
	 *         <code>null</code>)
	 */
	public static String[] toStringArray(Enumeration<String> enumeration) {
		if (enumeration == null) {
			return null;
		}
		List<String> list = Collections.list(enumeration);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Take an array Strings and split each element based on the given delimiter. A
	 * <code>Properties</code> instance is then generated, with the left of the delimiter providing
	 * the key, and the right of the delimiter providing the value.
	 * <p>
	 * Will trim both the key and value before adding them to the <code>Properties</code> instance.
	 * 
	 * @param array the array to process
	 * @param delimiter to split each element using (typically the equals symbol)
	 * @return a <code>Properties</code> instance representing the array contents, or
	 *         <code>null</code> if the array to process was null or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	/**
	 * Take an array Strings and split each element based on the given delimiter. A
	 * <code>Properties</code> instance is then generated, with the left of the delimiter providing
	 * the key, and the right of the delimiter providing the value.
	 * <p>
	 * Will trim both the key and value before adding them to the <code>Properties</code> instance.
	 * 
	 * @param array the array to process
	 * @param delimiter to split each element using (typically the equals symbol)
	 * @param charsToDelete one or more characters to remove from each element prior to attempting
	 *            the split operation (typically the quotation mark symbol), or <code>null</code> if
	 *            no removal should occur
	 * @return a <code>Properties</code> instance representing the array contents, or
	 *         <code>null</code> if the array to process was <code>null</code> or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

		if (Arrays.isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			if (charsToDelete != null) {
				element = remove(array[i], charsToDelete);
			}
			String[] splittedElement = split(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer. Trims tokens and omits
	 * empty tokens.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of delimiter characters.
	 * Each of those characters can be used to separate tokens. A delimiter is always a single
	 * character; for multi-character delimiters, consider using
	 * <code>delimitedListToStringArray</code>
	 * 
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String (each of those characters is
	 *            individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #toStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of delimiter characters.
	 * Each of those characters can be used to separate tokens. A delimiter is always a single
	 * character; for multi-character delimiters, consider using
	 * <code>delimitedListToStringArray</code>
	 * 
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String (each of those characters is
	 *            individually considered as delimiter)
	 * @param trimTokens trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to tokens that
	 *            are empty after trimming; StringTokenizer will not consider subsequent delimiters
	 *            as token in the first place).
	 * @return an array of the tokens (<code>null</code> if the input String was <code>null</code>)
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #toStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * getByteLength
	 * 
	 * @param str string
	 * @return byte length
	 */
	public static int getByteLength(String str) {
		if (isEmpty(str)) {
			return 0;
		}

		byte bytes[] = str.getBytes();

		return bytes != null ? bytes.length : 0;
	}

	/**
	 * getByteLength
	 * 
	 * @param str string
	 * @param encode encode
	 * @return string byte array length
	 * @throws UnsupportedEncodingException if the encode is not supported
	 */
	public static int getByteLength(String str, String encode) throws UnsupportedEncodingException {
		if (isEmpty(str)) {
			return 0;
		}

		byte bytes[] = null;
		if (isEmpty(encode)) {
			bytes = str.getBytes();
		}
		else {
			bytes = str.getBytes(encode);
		}

		return bytes != null ? bytes.length : 0;
	}

	// --------------------------------------------------------------------------------------
	// encodings
	//
	/**
	 * Encodes the given string into a sequence of bytes using the ISO-8859-1 charset, storing the
	 * result into a new byte array.
	 * 
	 * @param string the String to encode
	 * @return encoded bytes
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never
	 *             according the the Java specification.
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytes(String, String)
	 */
	public static byte[] getBytesIso8859_1(String string) {
		return getBytes(string, Charsets.ISO_8859_1);
	}

	/**
	 * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the
	 * result into a new byte array.
	 * 
	 * @param string the String to encode
	 * @return encoded bytes
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never
	 *             according the the Java specification.
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytes(String, String)
	 */
	public static byte[] getBytesUsAscii(String string) {
		return getBytes(string, Charsets.US_ASCII);
	}

	/**
	 * Encodes the given string into a sequence of bytes using the UTF-16 charset, storing the
	 * result into a new byte array.
	 * 
	 * @param string the String to encode
	 * @return encoded bytes
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never
	 *             according the the Java specification.
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytes(String, String)
	 */
	public static byte[] getBytesUtf16(String string) {
		return getBytes(string, Charsets.UTF_16);
	}

	/**
	 * Encodes the given string into a sequence of bytes using the UTF-16BE charset, storing the
	 * result into a new byte array.
	 * 
	 * @param string the String to encode
	 * @return encoded bytes
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never
	 *             according the the Java specification.
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytes(String, String)
	 */
	public static byte[] getBytesUtf16Be(String string) {
		return getBytes(string, Charsets.UTF_16BE);
	}

	/**
	 * Encodes the given string into a sequence of bytes using the UTF-16LE charset, storing the
	 * result into a new byte array.
	 * 
	 * @param string the String to encode
	 * @return encoded bytes
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never
	 *             according the the Java specification.
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytes(String, String)
	 */
	public static byte[] getBytesUtf16Le(String string) {
		return getBytes(string, Charsets.UTF_16LE);
	}

	/**
	 * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result
	 * into a new byte array.
	 * 
	 * @param string the String to encode
	 * @return encoded bytes
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never
	 *             according the the Java specification.
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytes(String, String)
	 */
	public static byte[] getBytesUtf8(String string) {
		return getBytes(string, Charsets.UTF_8);
	}

	/**
	 * Encodes the given string into a sequence of bytes using the named charset, storing the result
	 * into a new byte array.
	 * <p>
	 * This method catches {@link UnsupportedEncodingException} and rethrows it as
	 * {@link IllegalStateException}, which should never happen for a required charset name. Use
	 * this method when the encoding is required to be in the JRE.
	 * </p>
	 * 
	 * @param string the String to encode
	 * @param charsetName The name of a required {@link java.nio.charset.Charset}
	 * @return encoded bytes
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen for a required charset name.
	 * @see Charsets
	 * @see String#getBytes(String)
	 */
	public static byte[] getBytes(String string, String charsetName) {
		if (string == null) {
			return null;
		}
		try {
			return string.getBytes(charsetName);
		}
		catch (UnsupportedEncodingException e) {
			throw newIllegalStateException(charsetName, e);
		}
	}

	/**
	 * Encodes the given string into a sequence of bytes using the default charset, storing the result
	 * into a new byte array.
	 * 
	 * @param string the String to encode
	 * @return encoded bytes
	 * @see String#getBytes()
	 */
	public static byte[] getBytes(String string) {
		if (string == null) {
			return null;
		}
		return string.getBytes();
	}

	private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {
		return new IllegalStateException(charsetName + ": " + e);
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the given
	 * charset.
	 * <p>
	 * This method catches {@link UnsupportedEncodingException} and re-throws it as
	 * {@link IllegalStateException}, which should never happen for a required charset name. Use
	 * this method when the encoding is required to be in the JRE.
	 * </p>
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @param charsetName The name of a required {@link java.nio.charset.Charset}
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given
	 *         charset.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen for a required charset name.
	 * @see Charsets
	 * @see String#String(byte[], String)
	 */
	public static String newString(byte[] bytes, String charsetName) {
		if (bytes == null) {
			return null;
		}
		try {
			return new String(bytes, charsetName);
		}
		catch (UnsupportedEncodingException e) {
			throw newIllegalStateException(charsetName, e);
		}
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the
	 * ISO-8859-1 charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given
	 *         charset.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen since the charset is required.
	 */
	public static String newStringIso8859_1(byte[] bytes) {
		return newString(bytes, Charsets.ISO_8859_1);
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the
	 * US-ASCII charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given
	 *         charset.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen since the charset is required.
	 */
	public static String newStringUsAscii(byte[] bytes) {
		return newString(bytes, Charsets.US_ASCII);
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the
	 * UTF-16 charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given
	 *         charset.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen since the charset is required.
	 */
	public static String newStringUtf16(byte[] bytes) {
		return newString(bytes, Charsets.UTF_16);
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the
	 * UTF-16BE charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given
	 *         charset.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen since the charset is required.
	 */
	public static String newStringUtf16Be(byte[] bytes) {
		return newString(bytes, Charsets.UTF_16BE);
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the
	 * UTF-16LE charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given
	 *         charset.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen since the charset is required.
	 */
	public static String newStringUtf16Le(byte[] bytes) {
		return newString(bytes, Charsets.UTF_16LE);
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-8
	 * charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given
	 *         charset.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught,
	 *             which should never happen since the charset is required.
	 */
	public static String newStringUtf8(byte[] bytes) {
		return newString(bytes, Charsets.UTF_8);
	}

	/**
	 * Find the position of the first non-whitespace character
	 * 
	 * @param str string
	 * @return the position of the first non-whitespace character
	 */
	public static int firstNonWhitespace(CharSequence str) {
		int i = 0;
		while (Character.isWhitespace(str.charAt(i))) {
			i++;
		}
		return i;
	}
}
