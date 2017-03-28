package panda.net.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import panda.lang.Strings;
import panda.net.Mimes;

/**
 * A simple parser intended to parse sequences of name/value pairs. Parameter values are expected to
 * be enclosed in quotes if they contain unsafe characters, such as '=' characters or separators.
 * Parameter values are optional and can be omitted.
 * <p>
 * <code>param1 = value; param2 = "anything goes; really"; param3</code>
 * </p>
 */
public class ParameterParser {

	/**
	 * String to be parsed.
	 */
	private char[] chars = null;

	/**
	 * Current position in the string.
	 */
	private int pos = 0;

	/**
	 * Maximum position in the string.
	 */
	private int len = 0;

	/**
	 * Start of a token.
	 */
	private int i1 = 0;

	/**
	 * End of a token.
	 */
	private int i2 = 0;

	/**
	 * Whether names stored in the map should be converted to lower case.
	 */
	private boolean lowerCaseNames = false;

	/**
	 * Default ParameterParser constructor.
	 */
	public ParameterParser() {
		super();
	}

	/**
	 * ParameterParser constructor.
	 * 
	 * @param lowerCaseNames set parameter names to lowercase or not
	 */
	public ParameterParser(boolean lowerCaseNames) {
		this.lowerCaseNames = lowerCaseNames;
	}

	/**
	 * Are there any characters left to parse?
	 * 
	 * @return <tt>true</tt> if there are unparsed characters, <tt>false</tt> otherwise.
	 */
	private boolean hasChar() {
		return this.pos < this.len;
	}

	/**
	 * A helper method to process the parsed token. This method removes leading and trailing blanks
	 * as well as enclosing quotation marks, when necessary.
	 * 
	 * @param quoted <tt>true</tt> if quotation marks are expected, <tt>false</tt> otherwise.
	 * @return the token
	 */
	private String getToken(boolean quoted) {
		// Trim leading white spaces
		while ((i1 < i2) && (Character.isWhitespace(chars[i1]))) {
			i1++;
		}
		// Trim trailing white spaces
		while ((i2 > i1) && (Character.isWhitespace(chars[i2 - 1]))) {
			i2--;
		}
		// Strip away quotation marks if necessary
		if (quoted && ((i2 - i1) >= 2) && (chars[i1] == '"') && (chars[i2 - 1] == '"')) {
			i1++;
			i2--;
		}
		String result = null;
		if (i2 > i1) {
			result = new String(chars, i1, i2 - i1);
		}
		return result;
	}

	/**
	 * Tests if the given character is present in the array of characters.
	 * 
	 * @param ch the character to test for presense in the array of characters
	 * @param charray the array of characters to test against
	 * @return <tt>true</tt> if the character is present in the array of characters, <tt>false</tt>
	 *         otherwise.
	 */
	private boolean isOneOf(char ch, final char[] charray) {
		boolean result = false;
		for (char element : charray) {
			if (ch == element) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Parses out a token until any of the given terminators is encountered.
	 * 
	 * @param terminators the array of terminating characters. Any of these characters when
	 *            encountered signify the end of the token
	 * @return the token
	 */
	private String parseToken(final char[] terminators) {
		char ch;
		i1 = pos;
		i2 = pos;
		while (hasChar()) {
			ch = chars[pos];
			if (isOneOf(ch, terminators)) {
				break;
			}
			i2++;
			pos++;
		}
		return getToken(false);
	}

	/**
	 * Parses out a token until any of the given terminators is encountered outside the quotation
	 * marks.
	 * 
	 * @param terminators the array of terminating characters. Any of these characters when
	 *            encountered outside the quotation marks signify the end of the token
	 * @return the token
	 */
	private String parseQuotedToken(final char[] terminators) {
		char ch;
		i1 = pos;
		i2 = pos;
		boolean quoted = false;
		boolean charEscaped = false;
		while (hasChar()) {
			ch = chars[pos];
			if (!quoted && isOneOf(ch, terminators)) {
				break;
			}
			if (!charEscaped && ch == '"') {
				quoted = !quoted;
			}
			charEscaped = (!charEscaped && ch == '\\');
			i2++;
			pos++;

		}
		return getToken(true);
	}

	/**
	 * Returns <tt>true</tt> if parameter names are to be converted to lower case when name/value
	 * pairs are parsed.
	 * 
	 * @return <tt>true</tt> if parameter names are to be converted to lower case when name/value
	 *         pairs are parsed. Otherwise returns <tt>false</tt>
	 */
	public boolean isLowerCaseNames() {
		return this.lowerCaseNames;
	}

	/**
	 * Sets the flag if parameter names are to be converted to lower case when name/value pairs are
	 * parsed.
	 * 
	 * @param b <tt>true</tt> if parameter names are to be converted to lower case when name/value
	 *            pairs are parsed. <tt>false</tt> otherwise.
	 */
	public void setLowerCaseNames(boolean b) {
		this.lowerCaseNames = b;
	}

	private char getSeparator(final String str, final String separators) {
		char separator = separators.charAt(0);
		if (str != null) {
			int idx = str.length();
			for (int i = 0; i < separators.length(); i++) {
				char separator2 = separators.charAt(i);
				int tmp = str.indexOf(separator2);
				if (tmp != -1 && tmp < idx) {
					idx = tmp;
					separator = separator2;
				}
			}
		}
		return separator;
	}

	/**
	 * Extracts a map of name/value pairs from the given string. Names are expected to be unique.
	 * Multiple separators may be specified and the earliest found in the input string is used.
	 * 
	 * @param str the string that contains a sequence of name/value pairs
	 * @param separators the name/value pairs separators
	 * @return a map of name/value pairs
	 */
	public Map<String, String> parse(final String str, final String separators) {
		if (separators == null || separators.length() == 0) {
			return new HashMap<String, String>();
		}
		char separator = getSeparator(str, separators);
		return parse(str, separator);
	}

	/**
	 * Extracts a map of name/value pairs from the given string. Names are expected to be unique.
	 * Multiple separators may be specified and the earliest found in the input string is used.
	 * 
	 * @param str the string that contains a sequence of name/value pairs
	 * @param key the parameter key
	 * @param separators the name/value pairs separators
	 * @return a map of name/value pairs
	 */
	public String get(final String str, String key, String separators) {
		if (separators == null || separators.length() == 0) {
			return null;
		}

		char separator = getSeparator(str, separators);
		return get(str, key, separator);
	}

	/**
	 * Extracts a map of name/value pairs from the given string. Names are expected to be unique.
	 * 
	 * @param str the string that contains a sequence of name/value pairs
	 * @param separator the name/value pairs separator
	 * @return a map of name/value pairs
	 */
	public Map<String, String> parse(final String str, final char separator) {
		if (str == null) {
			return new HashMap<String, String>();
		}
		return parse(str.toCharArray(), separator);
	}

	/**
	 * Extracts a map of name/value pairs from the given string. Names are expected to be unique.
	 * 
	 * @param str the string that contains a sequence of name/value pairs
	 * @param key the parameter key
	 * @param separator the name/value pairs separator
	 * @return a map of name/value pairs
	 */
	public String get(final String str, final String key, final char separator) {
		if (str == null) {
			return null;
		}
		return get(str.toCharArray(), key, separator);
	}

	/**
	 * Extracts a map of name/value pairs from the given array of characters. Names are expected to
	 * be unique.
	 * 
	 * @param charArray the array of characters that contains a sequence of name/value pairs
	 * @param separator the name/value pairs separator
	 * @return a map of name/value pairs
	 */
	public Map<String, String> parse(final char[] charArray, final char separator) {
		if (charArray == null) {
			return new HashMap<String, String>();
		}
		return parse(charArray, 0, charArray.length, separator);
	}

	/**
	 * Extracts a map of name/value pairs from the given array of characters. Names are expected to
	 * be unique.
	 * 
	 * @param charArray the array of characters that contains a sequence of name/value pairs
	 * @param key the parameter key
	 * @param separator the name/value pairs separator
	 * @return a map of name/value pairs
	 */
	public String get(final char[] charArray, final String key, final char separator) {
		if (charArray == null) {
			return null;
		}
		return get(charArray, 0, charArray.length, key, separator);
	}

	/**
	 * Extracts a map of name/value pairs from the given array of characters. Names are expected to
	 * be unique.
	 * 
	 * @param charArray the array of characters that contains a sequence of name/value pairs
	 * @param offset - the initial offset.
	 * @param length - the length.
	 * @param separator the name/value pairs separator
	 * @return a map of name/value pairs
	 */
	public Map<String, String> parse(final char[] charArray, int offset, int length, char separator) {
		if (charArray == null) {
			return new HashMap<String, String>();
		}
		HashMap<String, String> params = new HashMap<String, String>();
		this.chars = charArray;
		this.pos = offset;
		this.len = length;

		String paramName = null;
		String paramValue = null;
		while (hasChar()) {
			paramName = parseToken(new char[] { '=', separator });
			paramValue = null;
			if (hasChar() && (charArray[pos] == '=')) {
				pos++; // skip '='
				paramValue = parseQuotedToken(new char[] { separator });

				if (paramValue != null) {
					try {
						paramValue = Mimes.decodeText(paramValue);
					}
					catch (UnsupportedEncodingException e) {
						// let's keep the original value in this case
					}
				}
				else {
					paramValue = Strings.EMPTY;
				}
			}
			if (hasChar() && (charArray[pos] == separator)) {
				pos++; // skip separator
			}
			if ((paramName != null) && (paramName.length() > 0)) {
				if (this.lowerCaseNames) {
					paramName = paramName.toLowerCase(Locale.ENGLISH);
				}

				params.put(paramName, paramValue);
			}
		}
		return params;
	}


	/**
	 * Extracts a map of name/value pairs from the given array of characters. Names are expected to
	 * be unique.
	 * 
	 * @param charArray the array of characters that contains a sequence of name/value pairs
	 * @param offset - the initial offset.
	 * @param length - the length.
	 * @param key the parameter key
	 * @param separator the name/value pairs separator
	 * @return value
	 */
	public String get(final char[] charArray, int offset, int length, final String key, final char separator) {
		if (charArray == null) {
			return null;
		}

		this.chars = charArray;
		this.pos = offset;
		this.len = length;

		String paramName = null;
		String paramValue = null;
		while (hasChar()) {
			paramName = parseToken(new char[] { '=', separator });
			paramValue = null;
			if (hasChar() && (charArray[pos] == '=')) {
				pos++; // skip '='
				paramValue = parseQuotedToken(new char[] { separator });

				if (paramValue != null) {
					try {
						paramValue = Mimes.decodeText(paramValue);
					}
					catch (UnsupportedEncodingException e) {
						// let's keep the original value in this case
					}
				}
				else {
					paramValue = Strings.EMPTY;
				}
			}
			if (hasChar() && (charArray[pos] == separator)) {
				pos++; // skip separator
			}
			if ((paramName != null) && (paramName.length() > 0)) {
				if (this.lowerCaseNames) {
					if (paramName.equalsIgnoreCase(key)) {
						return paramValue;
					}
				}
				else {
					if (paramName.equals(key)) {
						return paramValue;
					}
				}
			}
		}
		return null;
	}
}
