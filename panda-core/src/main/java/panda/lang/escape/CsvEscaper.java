package panda.lang.escape;

import java.io.IOException;

import panda.lang.Strings;

// TODO: Create a parent class - 'SinglePassTranslator' ?
// It would handle the index checking + length returning,
// and could also have an optimization check method.
public class CsvEscaper extends CharSequenceTranslator {
	protected static final char[] CSV_SEARCH_CHARS = new char[] { ',', '"', '\r', '\n' };

	@Override
	public int translateChar(CharSequence input, int index, Appendable out) throws IOException {

		if (index != 0) {
			throw new IllegalStateException("CsvEscaper should never reach the [1] index");
		}

		if (Strings.containsNone(input.toString(), CSV_SEARCH_CHARS)) {
			out.append(input);
		}
		else {
			out.append('"');
			out.append(Strings.replace(input.toString(), "\"", "\"\""));
			out.append('"');
		}
		return input.length();
	}
}
