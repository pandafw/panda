package panda.lang.escape;

import java.io.IOException;

/**
 * Helper subclass to CharSequenceTranslator to allow for translations that 
 * will replace up to one character at a time.
 */
public abstract class CodePointTranslator extends CharSequenceTranslator {

	/**
	 * Implementation of translate that maps onto the abstract translate(int, Writer) method.
	 * {@inheritDoc}
	 */
	@Override
	public final int translate(final CharSequence input, final int index, final Appendable out) throws IOException {
		final int codepoint = Character.codePointAt(input, index);
		final boolean consumed = translate(codepoint, out);
		if (consumed) {
			return 1;
		}
		else {
			return 0;
		}
	}

	/**
	 * Translate the specified codepoint into another.
	 * 
	 * @param codepoint int character input to translate
	 * @param out Writer to optionally push the translated output to
	 * @return boolean as to whether translation occurred or not
	 * @throws IOException if and only if the Writer produces an IOException
	 */
	public abstract boolean translate(int codepoint, Appendable out) throws IOException;

}
