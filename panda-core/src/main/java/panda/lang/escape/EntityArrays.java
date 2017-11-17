package panda.lang.escape;

/**
 * Class holding various entity data for HTML and XML - generally for use with
 * the LookupTranslator.
 * All arrays are of length [*][2].
 */
public class EntityArrays {
	/**
	 * Mapping to escape the Java control characters. Namely: {@code \b \n \t \f \r}
	 * 
	 * @return the mapping table
	 */
	public static String[][] JAVA_CTRL_CHARS_ESCAPE() {
		return JAVA_CTRL_CHARS_ESCAPE;
	}

	private static final String[][] JAVA_CTRL_CHARS_ESCAPE = { 
			{ "\b", "\\b" }, 
			{ "\n", "\\n" }, 
			{ "\t", "\\t" },
			{ "\f", "\\f" }, 
			{ "\r", "\\r" } 
			};

	/**
	 * Reverse of {@link #JAVA_CTRL_CHARS_ESCAPE()} for unescaping purposes.
	 * 
	 * @return the mapping table
	 */
	public static String[][] JAVA_CTRL_CHARS_UNESCAPE() {
		return JAVA_CTRL_CHARS_UNESCAPE;
	}

	private static final String[][] JAVA_CTRL_CHARS_UNESCAPE = invert(JAVA_CTRL_CHARS_ESCAPE);

	/**
	 * Used to invert an escape array into an unescape array
	 * 
	 * @param array String[][] to be inverted
	 * @return String[][] inverted array
	 */
	public static String[][] invert(final String[][] array) {
		final String[][] newarray = new String[array.length][2];
		for (int i = 0; i < array.length; i++) {
			newarray[i][0] = array[i][1];
			newarray[i][1] = array[i][0];
		}
		return newarray;
	}

}
