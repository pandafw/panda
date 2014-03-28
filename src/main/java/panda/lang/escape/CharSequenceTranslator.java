package panda.lang.escape;

import panda.io.stream.StringBuilderWriter;

import java.io.IOException;
import java.util.Locale;

/**
 * An API for translating text. 
 * Its core use is to escape and unescape text. Because escaping and unescaping 
 * is completely contextual, the API does not present two separate signatures.
 */
public abstract class CharSequenceTranslator {

	/**
	 * Translate a set of codepoints, represented by an int index into a CharSequence, into another
	 * set of codepoints. The number of codepoints consumed must be returned, and the only
	 * IOExceptions thrown must be from interacting with the Writer so that the top level API may
	 * reliable ignore StringBuilderWriter IOExceptions.
	 * 
	 * @param input CharSequence that is being translated
	 * @param index int representing the current point of translation
	 * @param out Writer to translate the text to
	 * @return int count of codepoints consumed
	 * @throws IOException if and only if the Writer produces an IOException
	 */
	public abstract int translateChar(CharSequence input, int index, Appendable out) throws IOException;

	/**
	 * Helper for non-Writer usage.
	 * 
	 * @param input CharSequence to be translated
	 * @return String output of translation
	 */
	public final String translate(final CharSequence input) {
		if (input == null) {
			return null;
		}
		try {
			final StringBuilderWriter writer = new StringBuilderWriter(input.length() * 2);
			translate(input, writer);
			return writer.toString();
		}
		catch (final IOException ioe) {
			// this should never ever happen while writing to a StringBuilderWriter
			throw new RuntimeException(ioe);
		}
	}

	/**
	 * Translate an input onto a Writer. This is intentionally final as its algorithm is tightly
	 * coupled with the abstract method of this class.
	 * 
	 * @param input CharSequence that is being translated
	 * @param out Writer to translate the text to
	 * @throws IOException if and only if the Writer produces an IOException
	 */
	public final void translate(final CharSequence input, final Appendable out) throws IOException {
		translate(input, 0, input == null ? 0 : input.length(), out);
	}

	/**
	 * Translate an input onto a Writer. This is intentionally final as its algorithm is tightly
	 * coupled with the abstract method of this class.
	 * 
	 * @param input CharSequence that is being translated
	 * @param start start position
	 * @param out Writer to translate the text to
	 * @throws IOException if and only if the Writer produces an IOException
	 */
	public final void translate(final CharSequence input, final int start, final Appendable out) throws IOException {
		translate(input, start, input == null ? 0 : input.length(), out);
	}
	
	/**
	 * Translate an input onto a Writer. This is intentionally final as its algorithm is tightly
	 * coupled with the abstract method of this class.
	 * 
	 * @param input CharSequence that is being translated
	 * @param start start position
	 * @param end end position
	 * @param out Writer to translate the text to
	 * @throws IOException if and only if the Writer produces an IOException
	 */
	public final void translate(final CharSequence input, final int start, final int end, final Appendable out) throws IOException {
		if (out == null) {
			throw new IllegalArgumentException("The Writer must not be null");
		}
		if (input == null) {
			return;
		}
		if (start < 0) {
			throw new StringIndexOutOfBoundsException(start);
		}
		if (end > input.length()) {
			throw new StringIndexOutOfBoundsException(end);
		}

		int pos = start;
		final int len = end;
		while (pos < len) {
			final int consumed = translateChar(input, pos, out);
			if (consumed == 0) {
				final char[] cs = Character.toChars(Character.codePointAt(input, pos));
				for (char c : cs) {
					out.append(c);
				}
				pos += cs.length;
				continue;
			}
			// // contract with translators is that they have to understand codepoints
			// // and they just took care of a surrogate pair
			for (int pt = 0; pt < consumed; pt++) {
				pos += Character.charCount(Character.codePointAt(input, pt));
			}
		}
	}

	/**
	 * Helper method to create a merger of this translator with another set of translators. Useful
	 * in customizing the standard functionality.
	 * 
	 * @param translators CharSequenceTranslator array of translators to merge with this one
	 * @return CharSequenceTranslator merging this translator with the others
	 */
	public final CharSequenceTranslator with(final CharSequenceTranslator... translators) {
		final CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
		newArray[0] = this;
		System.arraycopy(translators, 0, newArray, 1, translators.length);
		return new AggregateTranslator(newArray);
	}

	/**
	 * <p>
	 * Returns an upper case hexadecimal <code>String</code> for the given character.
	 * </p>
	 * 
	 * @param codepoint The codepoint to convert.
	 * @return An upper case hexadecimal <code>String</code>
	 */
	public static String hex(final int codepoint) {
		return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
	}

}
