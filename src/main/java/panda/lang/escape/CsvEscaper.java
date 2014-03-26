package panda.lang.escape;

import java.io.IOException;

import panda.lang.Chars;
import panda.lang.Strings;

// TODO: Create a parent class - 'SinglePassTranslator' ?
// It would handle the index checking + length returning,
// and could also have an optimization check method.
public class CsvEscaper extends CharSequenceTranslator {
	private static final char CSV_DELIMITER = ',';
	private static final char CSV_QUOTE = '"';
	private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);
	private static final char[] CSV_SEARCH_CHARS = new char[] { CSV_DELIMITER, CSV_QUOTE,
			Chars.CR, Chars.LF };

	@Override
	public int translateChar(CharSequence input, int index, Appendable out) throws IOException {

		if (index != 0) {
			throw new IllegalStateException("CsvEscaper should never reach the [1] index");
		}

		if (Strings.containsNone(input.toString(), CSV_SEARCH_CHARS)) {
			out.append(input);
		}
		else {
			out.append(CSV_QUOTE);
			out.append(Strings.replace(input.toString(), CSV_QUOTE_STR, CSV_QUOTE_STR
					+ CSV_QUOTE_STR));
			out.append(CSV_QUOTE);
		}
		return input.length();
	}
}
