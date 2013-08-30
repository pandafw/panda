package panda.lang;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import panda.bean.Beans;
import panda.log.Log;
import panda.log.Logs;


/**
 * Utility class for Text.
 * 
 * @author yf.frank.wang@gmail.com
 */
public abstract class Texts {
	private static Log log = Logs.getLog(Texts.class);

	// Wrapping
	// --------------------------------------------------------------------------
	/**
	 * <p>
	 * Wraps a single line of text, identifying words by <code>' '</code>.
	 * </p>
	 * <p>
	 * New lines will be separated by the system property line separator. Very long words, such as
	 * URLs will <i>not</i> be wrapped.
	 * </p>
	 * <p>
	 * Leading spaces on a new line are stripped. Trailing spaces are not stripped.
	 * </p>
	 * 
	 * <pre>
	 * wrap(null, *) = null
	 * wrap("", *) = ""
	 * </pre>
	 * 
	 * @param str the String to be word wrapped, may be null
	 * @param wrapLength the column to wrap the words at, less than 1 is treated as 1
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength) {
		return wrap(str, wrapLength, null, false);
	}

	/**
	 * <p>
	 * Wraps a single line of text, identifying words by <code>' '</code>.
	 * </p>
	 * <p>
	 * Leading spaces on a new line are stripped. Trailing spaces are not stripped.
	 * </p>
	 * 
	 * <pre>
	 * wrap(null, *, *, *) = null
	 * wrap("", *, *, *) = ""
	 * </pre>
	 * 
	 * @param str the String to be word wrapped, may be null
	 * @param wrapLength the column to wrap the words at, less than 1 is treated as 1
	 * @param newLineStr the string to insert for a new line, <code>null</code> uses the system
	 *            property line separator
	 * @param wrapLongWords true if long words (such as URLs) should be wrapped
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
		if (str == null) {
			return null;
		}
		if (newLineStr == null) {
			newLineStr = Systems.LINE_SEPARATOR;
		}
		if (wrapLength < 1) {
			wrapLength = 1;
		}
		int inputLineLength = str.length();
		int offset = 0;
		StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

		while (inputLineLength - offset > wrapLength) {
			if (str.charAt(offset) == ' ') {
				offset++;
				continue;
			}
			int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

			if (spaceToWrapAt >= offset) {
				// normal case
				wrappedLine.append(str.substring(offset, spaceToWrapAt));
				wrappedLine.append(newLineStr);
				offset = spaceToWrapAt + 1;

			}
			else {
				// really long word or URL
				if (wrapLongWords) {
					// wrap really long word one line at a time
					wrappedLine.append(str.substring(offset, wrapLength + offset));
					wrappedLine.append(newLineStr);
					offset += wrapLength;
				}
				else {
					// do not wrap really long word, just extend beyond limit
					spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
					if (spaceToWrapAt >= 0) {
						wrappedLine.append(str.substring(offset, spaceToWrapAt));
						wrappedLine.append(newLineStr);
						offset = spaceToWrapAt + 1;
					}
					else {
						wrappedLine.append(str.substring(offset));
						offset = inputLineLength;
					}
				}
			}
		}

		// Whatever is left in line is short enough to just pass through
		wrappedLine.append(str.substring(offset));

		return wrappedLine.toString();
	}

	// Capitalizing
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Capitalizes all the whitespace separated words in a String. Only the first letter of each
	 * word is changed. To convert the rest of each word to lowercase at the same time, use
	 * {@link #capitalizeFully(String)}.
	 * </p>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A <code>null</code> input
	 * String returns <code>null</code>. Capitalization uses the Unicode title case, normally
	 * equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * capitalize(null)        = null
	 * capitalize("")          = ""
	 * capitalize("i am FINE") = "I Am FINE"
	 * </pre>
	 * 
	 * @param str the String to capitalize, may be null
	 * @return capitalized String, <code>null</code> if null String input
	 * @see #uncapitalize(String)
	 * @see #capitalizeFully(String)
	 */
	public static String capitalize(String str) {
		return capitalize(str, null);
	}

	/**
	 * <p>
	 * Capitalizes all the delimiter separated words in a String. Only the first letter of each word
	 * is changed. To convert the rest of each word to lowercase at the same time, use
	 * {@link #capitalizeFully(String, char[])}.
	 * </p>
	 * <p>
	 * The delimiters represent a set of characters understood to separate words. The first string
	 * character and the first non-delimiter character after a delimiter will be capitalized.
	 * </p>
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. Capitalization uses the Unicode
	 * title case, normally equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * capitalize(null, *)            = null
	 * capitalize("", *)              = ""
	 * capitalize(*, new char[0])     = *
	 * capitalize("i am fine", null)  = "I Am Fine"
	 * capitalize("i aM.fine", {'.'}) = "I aM.Fine"
	 * </pre>
	 * 
	 * @param str the String to capitalize, may be null
	 * @param delimiters set of characters to determine capitalization, null means whitespace
	 * @return capitalized String, <code>null</code> if null String input
	 * @see #uncapitalize(String)
	 * @see #capitalizeFully(String)
	 * @since 2.1
	 */
	public static String capitalize(String str, char... delimiters) {
		int delimLen = delimiters == null ? -1 : delimiters.length;
		if (Strings.isEmpty(str) || delimLen == 0) {
			return str;
		}
		char[] buffer = str.toCharArray();
		boolean capitalizeNext = true;
		for (int i = 0; i < buffer.length; i++) {
			char ch = buffer[i];
			if (isDelimiter(ch, delimiters)) {
				capitalizeNext = true;
			}
			else if (capitalizeNext) {
				buffer[i] = Character.toTitleCase(ch);
				capitalizeNext = false;
			}
		}
		return new String(buffer);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts all the whitespace separated words in a String into capitalized words, that is each
	 * word is made up of a titlecase character and then a series of lowercase characters.
	 * </p>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A <code>null</code> input
	 * String returns <code>null</code>. Capitalization uses the Unicode title case, normally
	 * equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * capitalizeFully(null)        = null
	 * capitalizeFully("")          = ""
	 * capitalizeFully("i am FINE") = "I Am Fine"
	 * </pre>
	 * 
	 * @param str the String to capitalize, may be null
	 * @return capitalized String, <code>null</code> if null String input
	 */
	public static String capitalizeFully(String str) {
		return capitalizeFully(str, null);
	}

	/**
	 * <p>
	 * Converts all the delimiter separated words in a String into capitalized words, that is each
	 * word is made up of a titlecase character and then a series of lowercase characters.
	 * </p>
	 * <p>
	 * The delimiters represent a set of characters understood to separate words. The first string
	 * character and the first non-delimiter character after a delimiter will be capitalized.
	 * </p>
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. Capitalization uses the Unicode
	 * title case, normally equivalent to upper case.
	 * </p>
	 * 
	 * <pre>
	 * capitalizeFully(null, *)            = null
	 * capitalizeFully("", *)              = ""
	 * capitalizeFully(*, null)            = *
	 * capitalizeFully(*, new char[0])     = *
	 * capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
	 * </pre>
	 * 
	 * @param str the String to capitalize, may be null
	 * @param delimiters set of characters to determine capitalization, null means whitespace
	 * @return capitalized String, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String capitalizeFully(String str, char... delimiters) {
		int delimLen = delimiters == null ? -1 : delimiters.length;
		if (Strings.isEmpty(str) || delimLen == 0) {
			return str;
		}
		str = str.toLowerCase();
		return capitalize(str, delimiters);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Uncapitalizes all the whitespace separated words in a String. Only the first letter of each
	 * word is changed.
	 * </p>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A <code>null</code> input
	 * String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * uncapitalize(null)        = null
	 * uncapitalize("")          = ""
	 * uncapitalize("I Am FINE") = "i am fINE"
	 * </pre>
	 * 
	 * @param str the String to uncapitalize, may be null
	 * @return uncapitalized String, <code>null</code> if null String input
	 * @see #capitalize(String)
	 */
	public static String uncapitalize(String str) {
		return uncapitalize(str, null);
	}

	/**
	 * <p>
	 * Uncapitalizes all the whitespace separated words in a String. Only the first letter of each
	 * word is changed.
	 * </p>
	 * <p>
	 * The delimiters represent a set of characters understood to separate words. The first string
	 * character and the first non-delimiter character after a delimiter will be uncapitalized.
	 * </p>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A <code>null</code> input
	 * String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * uncapitalize(null, *)            = null
	 * uncapitalize("", *)              = ""
	 * uncapitalize(*, null)            = *
	 * uncapitalize(*, new char[0])     = *
	 * uncapitalize("I AM.FINE", {'.'}) = "i AM.fINE"
	 * </pre>
	 * 
	 * @param str the String to uncapitalize, may be null
	 * @param delimiters set of characters to determine uncapitalization, null means whitespace
	 * @return uncapitalized String, <code>null</code> if null String input
	 * @see #capitalize(String)
	 * @since 2.1
	 */
	public static String uncapitalize(String str, char... delimiters) {
		int delimLen = delimiters == null ? -1 : delimiters.length;
		if (Strings.isEmpty(str) || delimLen == 0) {
			return str;
		}
		char[] buffer = str.toCharArray();
		boolean uncapitalizeNext = true;
		for (int i = 0; i < buffer.length; i++) {
			char ch = buffer[i];
			if (isDelimiter(ch, delimiters)) {
				uncapitalizeNext = true;
			}
			else if (uncapitalizeNext) {
				buffer[i] = Character.toLowerCase(ch);
				uncapitalizeNext = false;
			}
		}
		return new String(buffer);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Swaps the case of a String using a word based algorithm.
	 * </p>
	 * <ul>
	 * <li>Upper case character converts to Lower case</li>
	 * <li>Title case character converts to Lower case</li>
	 * <li>Lower case character after Whitespace or at start converts to Title case</li>
	 * <li>Other Lower case character converts to Upper case</li>
	 * </ul>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A <code>null</code> input
	 * String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * Strings.swapCase(null)                 = null
	 * Strings.swapCase("")                   = ""
	 * Strings.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 * 
	 * @param str the String to swap case, may be null
	 * @return the changed String, <code>null</code> if null String input
	 */
	public static String swapCase(String str) {
		if (Strings.isEmpty(str)) {
			return str;
		}
		char[] buffer = str.toCharArray();

		boolean whitespace = true;

		for (int i = 0; i < buffer.length; i++) {
			char ch = buffer[i];
			if (Character.isUpperCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
				whitespace = false;
			}
			else if (Character.isTitleCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
				whitespace = false;
			}
			else if (Character.isLowerCase(ch)) {
				if (whitespace) {
					buffer[i] = Character.toTitleCase(ch);
					whitespace = false;
				}
				else {
					buffer[i] = Character.toUpperCase(ch);
				}
			}
			else {
				whitespace = Character.isWhitespace(ch);
			}
		}
		return new String(buffer);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Extracts the initial letters from each word in the String.
	 * </p>
	 * <p>
	 * The first letter of the string and all first letters after whitespace are returned as a new
	 * string. Their case is not changed.
	 * </p>
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A <code>null</code> input
	 * String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * initials(null)             = null
	 * initials("")               = ""
	 * initials("Ben John Lee")   = "BJL"
	 * initials("Ben J.Lee")      = "BJ"
	 * </pre>
	 * 
	 * @param str the String to get initials from, may be null
	 * @return String of initial letters, <code>null</code> if null String input
	 * @see #initials(String,char[])
	 * @since 2.2
	 */
	public static String initials(String str) {
		return initials(str, null);
	}

	/**
	 * <p>
	 * Extracts the initial letters from each word in the String.
	 * </p>
	 * <p>
	 * The first letter of the string and all first letters after the defined delimiters are
	 * returned as a new string. Their case is not changed.
	 * </p>
	 * <p>
	 * If the delimiters array is null, then Whitespace is used. Whitespace is defined by
	 * {@link Character#isWhitespace(char)}. A <code>null</code> input String returns
	 * <code>null</code>. An empty delimiter array returns an empty String.
	 * </p>
	 * 
	 * <pre>
	 * initials(null, *)                = null
	 * initials("", *)                  = ""
	 * initials("Ben John Lee", null)   = "BJL"
	 * initials("Ben J.Lee", null)      = "BJ"
	 * initials("Ben J.Lee", [' ','.']) = "BJL"
	 * initials(*, new char[0])         = ""
	 * </pre>
	 * 
	 * @param str the String to get initials from, may be null
	 * @param delimiters set of characters to determine words, null means whitespace
	 * @return String of initial letters, <code>null</code> if null String input
	 * @see #initials(String)
	 * @since 2.2
	 */
	public static String initials(String str, char... delimiters) {
		if (Strings.isEmpty(str)) {
			return str;
		}
		if (delimiters != null && delimiters.length == 0) {
			return "";
		}
		int strLen = str.length();
		char[] buf = new char[strLen / 2 + 1];
		int count = 0;
		boolean lastWasGap = true;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);

			if (isDelimiter(ch, delimiters)) {
				lastWasGap = true;
			}
			else if (lastWasGap) {
				buf[count++] = ch;
				lastWasGap = false;
			}
			else {
				continue; // ignore ch
			}
		}
		return new String(buf, 0, count);
	}

	// -----------------------------------------------------------------------
	/**
	 * Is the character a delimiter.
	 * 
	 * @param ch the character to check
	 * @param delimiters the delimiters
	 * @return true if it is a delimiter
	 */
	private static boolean isDelimiter(char ch, char[] delimiters) {
		if (delimiters == null) {
			return Character.isWhitespace(ch);
		}
		for (char delimiter : delimiters) {
			if (ch == delimiter) {
				return true;
			}
		}
		return false;
	}

	/**
	 * transform "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param expression expression
	 * @param wrapper object wrapper
	 * @return translated string
	 */
	public static String transform(String expression, Object wrapper) {
		return transform(expression, wrapper, '$');
	}
	

	/**
	 * transform "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param expression expression 
	 * @param wrapper object wrapper
	 * @param prefix prefix char 
	 * @return translated string
	 */
	public static String transform(String expression, Object wrapper, char prefix) {
		return transform(expression, wrapper, prefix, '{', '}');
	}
	/**
	 * transform "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param prefix prefix char $ or %
	 * @param open open char ( or {
	 * @param close close char ) or } 
	 * @param expression expression string
	 * @param wrapper object wrapper
	 * @return translated string
	 */
	public static String transform(String expression, Object wrapper, char prefix, char open, char close) {
		if (Strings.isEmpty(expression)) {
			return expression;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);
			if (c == prefix && i < expression.length() - 1 && expression.charAt(i + 1) == open) {
				String n = null;
				int j = i + 2;
				for (; j < expression.length(); j++) {
					if (expression.charAt(j) == close) {
						n = expression.substring(i + 2, j);
						break;
					}
				}
				if (n == null) {
					throw new IllegalArgumentException("Illegal statement (" + i + "): unexpected end of tag reached.");
				}
				else if (n.length() < 1) {
					throw new IllegalArgumentException("Illegal statement (" + i + "): the paramenter can not be empty.");
				}
				
				Object v = null;
				if (wrapper instanceof Map) {
					v = ((Map<?, ?>)wrapper).get(n);
				}
				else {
					try {
						v = Beans.getBean(wrapper, n);
					}
					catch (Exception e) {
						log.warn("Failed to get property: " + wrapper.getClass() + " - " + n);
					}
				}
				if (v == null) {
					sb.append(prefix);
					sb.append(open);
					sb.append(n);
					sb.append(close);
				}
				else {
					sb.append(v);
				}
				i = j;
			}
			else {
				sb.append(c);
			}
		}
		
		return sb.toString();
	}

	/**
	 * 
	 * @param text text
	 * @return keyword set
	 */
	public static Set<String> parseKeywords(String text) {
		return parseKeywords(text, 1, 20);
	}
	
	/**
	 * 
	 * @param text text
	 * @param minLength min length of keyword
	 * @param maxLength max length of keyword 
	 * @return keyword set
	 */
	public static Set<String> parseKeywords(String text, int minLength, int maxLength) {
		Set<String> kws = new HashSet<String>();

		if (Strings.isBlank(text)) {
			return kws;
		}

		int start = 0;
		int i = 0;
		int len = text.length();
		for (; i < len; i++) {
			char ch = text.charAt(i);
			if (!Character.isLetterOrDigit(ch)) {
				int cl = i - start;
				if (cl >= minLength && i <= maxLength) {
					kws.add(text.substring(start, i));
				}
				start = i + 1;
			}
		}
		int cl = i - start;
		if (cl >= minLength && i <= maxLength) {
			kws.add(text.substring(start, i));
		}

		return kws;
	}
}
