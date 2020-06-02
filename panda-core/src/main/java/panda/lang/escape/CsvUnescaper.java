package panda.lang.escape;

import java.io.IOException;

import panda.lang.Strings;

// TODO: Create a parent class - 'SinglePassTranslator' ?
// It would handle the index checking + length returning,
// and could also have an optimization check method.
public class CsvUnescaper extends CharSequenceTranslator {
	private static final char[] CSV_SEARCH_CHARS = CsvEscaper.CSV_SEARCH_CHARS;

	@Override
	public int translateChar(CharSequence input, int index, Appendable out) throws IOException {
		if (index != 0) {
			throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
		}

		if (input.charAt(0) != '"' || input.charAt(input.length() - 1) != '"') {
			out.append(input);
			return input.length();
		}

		// strip quotes
		String quoteless = input.subSequence(1, input.length() - 1).toString();

		if (Strings.containsAny(quoteless, CSV_SEARCH_CHARS)) {
			// deal with escaped quotes; ie) ""
			out.append(Strings.replace(quoteless, "\"\"", "\""));
		}
		else {
			out.append(input);
		}
		return input.length();
	}
}
