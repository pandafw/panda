package panda.lang;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.el.El;
import panda.el.Evaluator;
import panda.io.Streams;
import panda.io.stream.CharSequenceReader;
import panda.io.stream.CsvReader;
import panda.io.stream.CsvWriter;
import panda.io.stream.StringBuilderWriter;


/**
 * Utility class for Text.
 * 
 */
public abstract class Texts {
	public final static String ELLIPSIS = "...";

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

	// -----------------------------------------------------------------------
	public static class BeanEvaluator implements Evaluator {
		private Object context;
		private BeanHandler beanh;
		
		public BeanEvaluator(Object context) {
			this.context = context;
			this.beanh = Beans.i().getBeanHandler(context.getClass());
		}
		
		@SuppressWarnings("unchecked")
		public Object evaluate(String expression) {
			return beanh.getBeanValue(context, expression);
		}
	}

	public static class ElEvaluator implements Evaluator {
		private Object context;
		
		public ElEvaluator(Object context) {
			this.context = context;
		}
		
		public Object evaluate(String expression) {
			return new El(expression).eval(context);
		}
	}
	
	/**
	 * translate "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param expression expression
	 * @param wrapper object wrapper
	 * @return translated string
	 */
	public static String translate(String expression, Object wrapper) {
		return translate(expression, wrapper, '$');
	}
	

	/**
	 * translate "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param expression expression 
	 * @param wrapper object wrapper
	 * @param prefix prefix char 
	 * @return translated string
	 */
	public static String translate(String expression, Object wrapper, char prefix) {
		if (Strings.isEmpty(expression) || wrapper == null) {
			return expression;
		}
		
		Evaluator eva = new BeanEvaluator(wrapper);
		return translate(expression, eva, prefix, '{', '}');
	}
	
	/**
	 * translate "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param expression expression
	 * @param wrapper object wrapper
	 * @return translated string
	 */
	public static String elTranslate(String expression, Object wrapper) {
		return elTranslate(expression, wrapper, Marks.DOLLAR);
	}
	
	/**
	 * translate "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param expression expression 
	 * @param wrapper object wrapper
	 * @param prefix prefix char 
	 * @return translated string
	 */
	public static String elTranslate(String expression, Object wrapper, char prefix) {
		if (Strings.isEmpty(expression) || wrapper == null) {
			return expression;
		}
		
		Evaluator eva = new ElEvaluator(wrapper);
		return translate(expression, eva, prefix, Marks.BRACES_LEFT, Marks.BRACES_RIGHT);
	}

	/**
	 * translate "${a}-${b}" with Map { "a": 1, "b": 2 } -> "1-2".
	 * 
	 * @param prefix prefix char $ or %
	 * @param open open char ( or {
	 * @param close close char ) or } 
	 * @param expression expression string
	 * @param eval object evaluator
	 * @return translated string
	 */
	public static String translate(String expression, Evaluator eval, char prefix, char open, char close) {
		if (Strings.isEmpty(expression) || eval == null) {
			return expression;
		}
		
		int x = 0;
		int len = expression.length();
		StringBuilder sb = null;
		for (int i = 0; i < len; i++) {
			char c = expression.charAt(i);
			if (c == prefix && i < len - 1 && expression.charAt(i + 1) == open) {
				String pn = null;
				int j = i + 2;
				for (; j < len; j++) {
					if (expression.charAt(j) == close) {
						pn = expression.substring(i + 2, j);
						break;
					}
				}
				if (pn == null) {
					throw new IllegalArgumentException("Illegal statement (" + i + "): unexpected end of tag reached.");
				}
				else if (pn.length() < 1) {
					throw new IllegalArgumentException("Illegal statement (" + i + "): the paramenter can not be empty.");
				}
				
				Object v = eval.evaluate(pn);
				if (v != null) {
					if (sb == null) {
						sb = new StringBuilder();
					}
					sb.append(expression.substring(x, i));
					sb.append(v);
					x = j + 1;
				}
				i = j;
			}
		}
		
		if (sb != null && x < len) {
			sb.append(expression.substring(x, len));
		}
		
		return sb == null ? expression : sb.toString();
	}

	// -----------------------------------------------------------------------
	/**
	 * convert a string to a camel style word
	 * 
	 * <pre>
	 *  camelWord("hello-world", '-') => "helloWorld"
	 * </pre>
	 * 
	 * @param cs the input string
	 * @param c separator
	 * @return camel style word
	 */
	public static String camelWord(CharSequence cs, char c) {
		if (cs == null) {
			return null;
		}
		if (cs.length() < 1) {
			return Strings.EMPTY;
		}
		
		StringBuilder sb = new StringBuilder();
		int len = cs.length();
		for (int i = 0; i < len; i++) {
			char ch = cs.charAt(i);
			if (ch == c) {
				do {
					i++;
					if (i >= len) {
						return sb.toString();
					}
					ch = cs.charAt(i);
				}
				while (ch == c);
				sb.append(Character.toUpperCase(ch));
			}
			else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * convert a camel style word to a string joined by sepecified separator
	 * 
	 * <pre>
	 *  uncamelWord("helloWorld", '-') => "hello-world"
	 * </pre>
	 * 
	 * @param cs the input string
	 * @param c separator
	 * @return converted string
	 */
	public static String uncamelWord(CharSequence cs, char c) {
		if (cs == null) {
			return null;
		}
		if (cs.length() < 1) {
			return Strings.EMPTY;
		}

		StringBuilder sb = new StringBuilder();
		int len = cs.length();
		for (int i = 0; i < len; i++) {
			char ch = cs.charAt(i);
			if (Character.isUpperCase(ch)) {
				if (i > 0) {
					sb.append(c);
				}
				sb.append(Character.toLowerCase(ch));
			}
			else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	// -----------------------------------------------------------------------
	/**
	 * toCsv
	 * 
	 * @param lines lines
	 * @return csv string
	 */
	public static String toCsv(List<String[]> lines) {
		if (lines == null) {
			return null;
		}

		StringBuilderWriter sw = new StringBuilderWriter();
		CsvWriter cw = new CsvWriter(sw);
		try {
			cw.writeAll(lines);
			return sw.toString();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			Streams.safeClose(cw);
		}
	}

	/**
	 * parse csv string
	 * 
	 * @param str string
	 * @return string list
	 */
	public static List<String> parseCsv(String str) {
		return parseCsv(str, Chars.COMMA);
	}

	/**
	 * parse csv string
	 * 
	 * @param str string
	 * @param separator the delimiter to use for separating entries.
	 * @return string list
	 */
	public static List<String> parseCsv(String str, char separator) {
		return parseCsv(str, Chars.COMMA, Chars.DOUBLE_QUOTE);
	}

	/**
	 * parse csv string
	 * 
	 * @param str string
	 * @param separator the delimiter to use for separating entries.
	 * @param quotechar the character to use for quoted elements
	 * @return string list
	 */
	public static List<String> parseCsv(CharSequence str, char separator, char quotechar) {
		if (str == null) {
			return null;
		}

		CsvReader cr = new CsvReader(new CharSequenceReader(str), separator, quotechar);
		try {
			return cr.readList();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			Streams.safeClose(cr);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Truncate a string and add an ellipsis ('...') to the end if it exceeds the specified length
	 * 
	 * @param str value The string to truncate
	 * @param len length The maximum length to allow before truncating
	 * @return The converted text
	 */
	public static String ellipsis(String str, int len) {
		return ellipsis(str, len, ELLIPSIS);
	}

	/**
	 * Truncate a string and add an ellipsis ('...') to the end if it exceeds the specified length
	 * 
	 * @param str value The string to truncate
	 * @param len length The maximum length to allow before truncating
	 * @return The converted textELLIPSIS
	 */
	public static String ellipsis(String str, int len, String ellipsis) {
		if (str == null) {
			return null;
		}

		if (str.length() > len) {
			if (len <= ellipsis.length()) {
				return ellipsis.substring(0, len);
			}
			return str.substring(0, len - ellipsis.length()) + ellipsis;
		}
		return str;
	}

	/**
	 * Truncate a string and add an ellipsis ('...') to the end if it exceeds the specified length
	 * the length of charCodeAt(i) > 0xFF will be treated as 2.
	 * 
	 * @param str value The string to truncate
	 * @param len length The maximum length to allow before truncating
	 * @return The converted text
	 */
	public static String ellipsiz(String str, int len) {
		return ellipsiz(str, len, ELLIPSIS);
	}
	
	/**
	 * Truncate a string and add an ellipsis ('...') to the end if it exceeds the specified length
	 * the length of charCodeAt(i) > 0xFF will be treated as 2.
	 * 
	 * @param str value The string to truncate
	 * @param len length The maximum length to allow before truncating
	 * @return The converted text
	 */
	public static String ellipsiz(String str, int len, String ellipsis) {
		if (str == null) {
			return null;
		}

		int max = len - ellipsis.length();
		int sz = 0, j = 0;
		for (int i = 0; i < str.length(); i++) {
			sz++;
			if (str.charAt(i) > 0xFF) {
				sz++;
			}
			if (sz > max && j == 0) {
				j = i;
			}
			if (sz > len) {
				if (len <= ellipsis.length()) {
					return ellipsis.substring(0, len);
				}
				return str.substring(0, j) + ellipsis;
			}
		}
		return str;
	}
	
	// -----------------------------------------------------------------------
	private static class XmlPattern {
		static Pattern p0 = Pattern.compile(".+</\\w[^>]*>$");
		static Pattern p1 = Pattern.compile("^</\\w*>$");
		static Pattern p2 = Pattern.compile("^<\\w[^>]*[^/]>.*$");
		static Pattern p3 = Pattern.compile("(>)(<)(/*)");
	}
	
	/**
	 * prettify xml
	 * 
	 * @param xml xml string
	 * @return prettified xml
	 */
	public static String prettifyXml(String xml) {
		StringBuilder fmt = new StringBuilder();

		xml = XmlPattern.p3.matcher(xml).replaceAll("$1\n$2$3");

		String[] xs = Strings.split(xml, Strings.CRLF);

		int pad = 0;
		for (int i = 0; i < xs.length; i++) {
			String node = xs[i];
			int indent = 0;
			if (XmlPattern.p0.matcher(node).matches()) {
				indent = 0;
			}
			else if (XmlPattern.p1.matcher(node).matches()) {
				if (pad != 0) {
					pad -= 1;
				}
			}
			else if (XmlPattern.p2.matcher(node).matches()) {
				indent = 1;
			}
			else {
				indent = 0;
			}

			fmt.append(Strings.leftPad(Strings.EMPTY, pad * 2, ' ')).append(node).append(Strings.CRLF);
			pad += indent;
		}

		return fmt.toString();
	}

	// -----------------------------------------------------------------------
	/**
	 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
	 */
	public static int computeLevenshteinDistance(String s0, String s1) {
		int len0 = s0.length() + 1;
		int len1 = s1.length() + 1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for (int i = 0; i < len0; i++)
			cost[i] = i;

		// dynamicaly computing the array of distances

		// transformation cost for each letter in s1
		for (int j = 1; j < len1; j++) {

			// initial cost of skipping prefix in String s1
			newcost[0] = j - 1;

			// transformation cost for each letter in s0
			for (int i = 1; i < len0; i++) {

				// matching current letters in both strings
				int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

				// computing cost for each transformation
				int cost_replace = cost[i - 1] + match;
				int cost_insert = cost[i] + 1;
				int cost_delete = newcost[i - 1] + 1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
			}

			// swap cost/newcost arrays
			int[] swap = cost;
			cost = newcost;
			newcost = swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0 - 1];
	}

	public static String maskPassword(String pwd) {
		if (Strings.isEmpty(pwd)) {
			return pwd;
		}
		
		StringBuilder sb = new StringBuilder(pwd.length());
		for (int i = 0; i < pwd.length(); i++) {
			if (i > 1 && i < pwd.length() - 1) {
				sb.append('*');
			}
			else {
				sb.append(pwd.charAt(i));
			}
		}
		return sb.toString();
	}
}
