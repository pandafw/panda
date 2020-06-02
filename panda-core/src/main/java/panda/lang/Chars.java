package panda.lang;

/**
 * <p>
 * Operations on char primitives and Character objects.
 * </p>
 * <p>
 * This class tries to handle {@code null} input gracefully. An exception will not be thrown for a
 * {@code null} input. Each method documents its behaviour in more detail.
 * </p>
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 */
public abstract class Chars {
	/**
	 * BOM character
	 */
	public static final char BOM = '\uFEFF';

	private static final String[] CHAR_STRING_ARRAY = new String[128];
	static {
		for (char c = 0; c < CHAR_STRING_ARRAY.length; c++) {
			CHAR_STRING_ARRAY[c] = String.valueOf(c);
		}
	}

	/**
	 * <p>
	 * {@code Chars} instances should NOT be constructed in standard programming. Instead, the
	 * class should be used as {@code Chars.toString('c');}.
	 * </p>
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean instance to operate.
	 * </p>
	 */
	public Chars() {
		super();
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts the String to a Character using the first character, returning null for empty
	 * Strings.
	 * </p>
	 * <p>
	 * For ASCII 7 bit characters, this uses a cache that will return the same Character object each
	 * time.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toCharacterObject(null) = null
	 *   Chars.toCharacterObject("")   = null
	 *   Chars.toCharacterObject("A")  = 'A'
	 *   Chars.toCharacterObject("BA") = 'B'
	 * </pre>
	 * 
	 * @param str the character to convert
	 * @return the Character value of the first letter of the String
	 */
	public static Character toCharacterObject(String str) {
		if (Strings.isEmpty(str)) {
			return null;
		}
		return Character.valueOf(str.charAt(0));
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts the Character to a char throwing an exception for {@code null}.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toChar(' ')  = ' '
	 *   Chars.toChar('A')  = 'A'
	 *   Chars.toChar(null) throws IllegalArgumentException
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @return the char value of the Character
	 * @throws IllegalArgumentException if the Character is null
	 */
	public static char toChar(Character ch) {
		if (ch == null) {
			throw new IllegalArgumentException("The Character must not be null");
		}
		return ch.charValue();
	}

	/**
	 * <p>
	 * Converts the Character to a char handling {@code null}.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toChar(null, 'X') = 'X'
	 *   Chars.toChar(' ', 'X')  = ' '
	 *   Chars.toChar('A', 'X')  = 'A'
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @param defaultValue the value to use if the Character is null
	 * @return the char value of the Character or the default if null
	 */
	public static char toChar(Character ch, char defaultValue) {
		if (ch == null) {
			return defaultValue;
		}
		return ch.charValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts the String to a char using the first character, throwing an exception on empty
	 * Strings.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toChar("A")  = 'A'
	 *   Chars.toChar("BA") = 'B'
	 *   Chars.toChar(null) throws IllegalArgumentException
	 *   Chars.toChar("")   throws IllegalArgumentException
	 * </pre>
	 * 
	 * @param str the character to convert
	 * @return the char value of the first letter of the String
	 * @throws IllegalArgumentException if the String is empty
	 */
	public static char toChar(String str) {
		if (Strings.isEmpty(str)) {
			throw new IllegalArgumentException("The String must not be empty");
		}
		return str.charAt(0);
	}

	/**
	 * <p>
	 * Converts the String to a char using the first character, defaulting the value on empty
	 * Strings.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toChar(null, 'X') = 'X'
	 *   Chars.toChar("", 'X')   = 'X'
	 *   Chars.toChar("A", 'X')  = 'A'
	 *   Chars.toChar("BA", 'X') = 'B'
	 * </pre>
	 * 
	 * @param str the character to convert
	 * @param defaultValue the value to use if the Character is null
	 * @return the char value of the first letter of the String or the default if null
	 */
	public static char toChar(String str, char defaultValue) {
		if (Strings.isEmpty(str)) {
			return defaultValue;
		}
		return str.charAt(0);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts the character to the Integer it represents, throwing an exception if the character
	 * is not numeric.
	 * </p>
	 * <p>
	 * This method coverts the char '1' to the int 1 and so on.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toIntValue('3')  = 3
	 *   Chars.toIntValue('A')  throws IllegalArgumentException
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @return the int value of the character
	 * @throws IllegalArgumentException if the character is not ASCII numeric
	 */
	public static int toIntValue(char ch) {
		if (isAsciiNumeric(ch) == false) {
			throw new IllegalArgumentException("The character " + ch
					+ " is not in the range '0' - '9'");
		}
		return ch - 48;
	}

	/**
	 * <p>
	 * Converts the character to the Integer it represents, throwing an exception if the character
	 * is not numeric.
	 * </p>
	 * <p>
	 * This method coverts the char '1' to the int 1 and so on.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toIntValue('3', -1)  = 3
	 *   Chars.toIntValue('A', -1)  = -1
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @param defaultValue the default value to use if the character is not numeric
	 * @return the int value of the character
	 */
	public static int toIntValue(char ch, int defaultValue) {
		if (isAsciiNumeric(ch) == false) {
			return defaultValue;
		}
		return ch - 48;
	}

	/**
	 * <p>
	 * Converts the character to the Integer it represents, throwing an exception if the character
	 * is not numeric.
	 * </p>
	 * <p>
	 * This method coverts the char '1' to the int 1 and so on.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toIntValue('3')  = 3
	 *   Chars.toIntValue(null) throws IllegalArgumentException
	 *   Chars.toIntValue('A')  throws IllegalArgumentException
	 * </pre>
	 * 
	 * @param ch the character to convert, not null
	 * @return the int value of the character
	 * @throws IllegalArgumentException if the Character is not ASCII numeric or is null
	 */
	public static int toIntValue(Character ch) {
		if (ch == null) {
			throw new IllegalArgumentException("The character must not be null");
		}
		return toIntValue(ch.charValue());
	}

	/**
	 * <p>
	 * Converts the character to the Integer it represents, throwing an exception if the character
	 * is not numeric.
	 * </p>
	 * <p>
	 * This method coverts the char '1' to the int 1 and so on.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toIntValue(null, -1) = -1
	 *   Chars.toIntValue('3', -1)  = 3
	 *   Chars.toIntValue('A', -1)  = -1
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @param defaultValue the default value to use if the character is not numeric
	 * @return the int value of the character
	 */
	public static int toIntValue(Character ch, int defaultValue) {
		if (ch == null) {
			return defaultValue;
		}
		return toIntValue(ch.charValue(), defaultValue);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts the character to a String that contains the one character.
	 * </p>
	 * <p>
	 * For ASCII 7 bit characters, this uses a cache that will return the same String object each
	 * time.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toString(' ')  = " "
	 *   Chars.toString('A')  = "A"
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @return a String containing the one specified character
	 */
	public static String toString(char ch) {
		if (ch < 128) {
			return CHAR_STRING_ARRAY[ch];
		}
		return new String(new char[] { ch });
	}

	/**
	 * <p>
	 * Converts the character to a String that contains the one character.
	 * </p>
	 * <p>
	 * For ASCII 7 bit characters, this uses a cache that will return the same String object each
	 * time.
	 * </p>
	 * <p>
	 * If {@code null} is passed in, {@code null} will be returned.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.toString(null) = null
	 *   Chars.toString(' ')  = " "
	 *   Chars.toString('A')  = "A"
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @return a String containing the one specified character
	 */
	public static String toString(Character ch) {
		if (ch == null) {
			return null;
		}
		return toString(ch.charValue());
	}

	// --------------------------------------------------------------------------
	/**
	 * <p>
	 * Converts the string to the Unicode format '\u0020'.
	 * </p>
	 * <p>
	 * This format is the Java source code format.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.unicodeEscaped(' ') = "\u0020"
	 *   Chars.unicodeEscaped('A') = "\u0041"
	 * </pre>
	 * 
	 * @param ch the character to convert
	 * @return the escaped Unicode string
	 */
	public static String unicodeEscaped(char ch) {
		if (ch < 0x10) {
			return "\\u000" + Integer.toHexString(ch);
		}
		else if (ch < 0x100) {
			return "\\u00" + Integer.toHexString(ch);
		}
		else if (ch < 0x1000) {
			return "\\u0" + Integer.toHexString(ch);
		}
		return "\\u" + Integer.toHexString(ch);
	}

	/**
	 * <p>
	 * Converts the string to the Unicode format '\u0020'.
	 * </p>
	 * <p>
	 * This format is the Java source code format.
	 * </p>
	 * <p>
	 * If {@code null} is passed in, {@code null} will be returned.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.unicodeEscaped(null) = null
	 *   Chars.unicodeEscaped(' ')  = "\u0020"
	 *   Chars.unicodeEscaped('A')  = "\u0041"
	 * </pre>
	 * 
	 * @param ch the character to convert, may be null
	 * @return the escaped Unicode string, null if null input
	 */
	public static String unicodeEscaped(Character ch) {
		if (ch == null) {
			return null;
		}
		return unicodeEscaped(ch.charValue());
	}

	// --------------------------------------------------------------------------
	/**
	 * Checkes whether the character is non-breaking space ({@code '\u005Cu00A0'},
	 *      {@code '\u005Cu2007'}, {@code '\u005Cu202F'}) or whitespace is stripped as defined by
	 * {@link Character#isWhitespace(char)}.
	 * @param ch char
	 * @return is space
	 */
	public static boolean isSpace(char ch) {
		return Character.isWhitespace(ch) || isNonBreakingSpace(ch);
	}
	
	/**
	 * Checkes whether the character is non-breaking space ({@code '\u005Cu00A0'},
	 *      {@code '\u005Cu2007'}, {@code '\u005Cu202F'})
	 * @param ch char
	 * @return is non breaking space
	 */
	public static boolean isNonBreakingSpace(char ch) {
		return ch == '\u00A0' || ch == '\uu2007' || ch == '\u202F';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAscii('a')  = true
	 *   Chars.isAscii('A')  = true
	 *   Chars.isAscii('3')  = true
	 *   Chars.isAscii('-')  = true
	 *   Chars.isAscii('\n') = true
	 *   Chars.isAscii('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if less than 128
	 */
	public static boolean isAscii(char ch) {
		return ch < 128;
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit printable.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAsciiPrintable('a')  = true
	 *   Chars.isAsciiPrintable('A')  = true
	 *   Chars.isAsciiPrintable('3')  = true
	 *   Chars.isAsciiPrintable('-')  = true
	 *   Chars.isAsciiPrintable('\n') = false
	 *   Chars.isAsciiPrintable('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 32 and 126 inclusive
	 */
	public static boolean isAsciiPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit control.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAsciiControl('a')  = false
	 *   Chars.isAsciiControl('A')  = false
	 *   Chars.isAsciiControl('3')  = false
	 *   Chars.isAsciiControl('-')  = false
	 *   Chars.isAsciiControl('\n') = true
	 *   Chars.isAsciiControl('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if less than 32 or equals 127
	 */
	public static boolean isAsciiControl(char ch) {
		return ch < 32 || ch == 127;
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit alphabetic.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAsciiAlpha('a')  = true
	 *   Chars.isAsciiAlpha('A')  = true
	 *   Chars.isAsciiAlpha('3')  = false
	 *   Chars.isAsciiAlpha('-')  = false
	 *   Chars.isAsciiAlpha('\n') = false
	 *   Chars.isAsciiAlpha('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 65 and 90 or 97 and 122 inclusive
	 */
	public static boolean isAsciiAlpha(char ch) {
		return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit alphabetic upper case.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAsciiAlphaUpper('a')  = false
	 *   Chars.isAsciiAlphaUpper('A')  = true
	 *   Chars.isAsciiAlphaUpper('3')  = false
	 *   Chars.isAsciiAlphaUpper('-')  = false
	 *   Chars.isAsciiAlphaUpper('\n') = false
	 *   Chars.isAsciiAlphaUpper('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 65 and 90 inclusive
	 */
	public static boolean isAsciiAlphaUpper(char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit alphabetic lower case.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAsciiAlphaLower('a')  = true
	 *   Chars.isAsciiAlphaLower('A')  = false
	 *   Chars.isAsciiAlphaLower('3')  = false
	 *   Chars.isAsciiAlphaLower('-')  = false
	 *   Chars.isAsciiAlphaLower('\n') = false
	 *   Chars.isAsciiAlphaLower('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 97 and 122 inclusive
	 */
	public static boolean isAsciiAlphaLower(char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit numeric.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAsciiNumeric('a')  = false
	 *   Chars.isAsciiNumeric('A')  = false
	 *   Chars.isAsciiNumeric('3')  = true
	 *   Chars.isAsciiNumeric('-')  = false
	 *   Chars.isAsciiNumeric('\n') = false
	 *   Chars.isAsciiNumeric('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 48 and 57 inclusive
	 */
	public static boolean isAsciiNumeric(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit numeric.
	 * </p>
	 * 
	 * <pre>
	 *   Chars.isAsciiAlphanumeric('a')  = true
	 *   Chars.isAsciiAlphanumeric('A')  = true
	 *   Chars.isAsciiAlphanumeric('3')  = true
	 *   Chars.isAsciiAlphanumeric('-')  = false
	 *   Chars.isAsciiAlphanumeric('\n') = false
	 *   Chars.isAsciiAlphanumeric('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 48 and 57 or 65 and 90 or 97 and 122 inclusive
	 */
	public static boolean isAsciiAlphanumeric(char ch) {
		return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
	}

}
