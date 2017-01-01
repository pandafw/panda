package panda.lang.escape;

import java.io.IOException;

/**
 * Translates escaped Unicode values of the form \\u+\d\d\d\d back to 
 * Unicode. It supports multiple 'u' characters and will work with or 
 * without the +.
 * 
 */
public class UnicodeUnescaper extends CharSequenceTranslator {

	@Override
	public int translateChar(final CharSequence input, final int index, final Appendable out) throws IOException {
		if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u') {
			// consume optional additional 'u' chars
			int i = 2;
			while (index + i < input.length() && input.charAt(index + i) == 'u') {
				i++;
			}

			if (index + i < input.length() && input.charAt(index + i) == '+') {
				i++;
			}

			if (index + i + 4 <= input.length()) {
				// Get 4 hex digits
				final CharSequence unicode = input.subSequence(index + i, index + i + 4);

				try {
					final int value = Integer.parseInt(unicode.toString(), 16);
					out.append((char)value);
				}
				catch (final NumberFormatException nfe) {
					throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
				}
				return i + 4;
			}
			else {
				throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '"
						+ input.subSequence(index, input.length()) + "' due to end of CharSequence");
			}
		}
		return 0;
	}
}
