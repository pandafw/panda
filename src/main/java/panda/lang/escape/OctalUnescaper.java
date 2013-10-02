package panda.lang.escape;

import java.io.IOException;

/**
 * Translate escaped octal Strings back to their octal values.
 *
 * For example, "\45" should go back to being the specific value (a %).
 *
 * Note that this currently only supports the viable range of octal for Java; namely 
 * 1 to 377. This is both because parsing Java is the main use case and Integer.parseInt
 * throws an exception when values are larger than octal 377.
 * 
 */
public class OctalUnescaper extends CharSequenceTranslator {

	private static int OCTAL_MAX = 377;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int translate(final CharSequence input, final int index, final Appendable out) throws IOException {
		if (input.charAt(index) == '\\' && index < (input.length() - 1) && Character.isDigit(input.charAt(index + 1))) {
			final int start = index + 1;

			int end = index + 2;
			while (end < input.length() && Character.isDigit(input.charAt(end))) {
				end++;
				if (Integer.parseInt(input.subSequence(start, end).toString(), 10) > OCTAL_MAX) {
					end--; // rollback
					break;
				}
			}

			out.append((char)(Integer.parseInt(input.subSequence(start, end).toString(), 8)));
			return 1 + end - start;
		}
		return 0;
	}
}
