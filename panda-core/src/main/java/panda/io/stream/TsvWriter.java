package panda.io.stream;

import panda.lang.Strings;

/**
 * TSV writer
 */
public class TsvWriter extends CsvWriter {

	/**
	 * Constructs TsvWriter using a comma for the separator.
	 *
	 * @param writer the writer to an underlying CSV source.
	 */
	public TsvWriter(Appendable writer) {
		this(writer, '\t');
	}

	/**
	 * Constructs TsvWriter with supplied separator.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries.
	 */
	public TsvWriter(Appendable writer, char separator) {
		this(writer, separator, '"');
	}

	/**
	 * Constructs TsvWriter with supplied separator and quote char.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 */
	public TsvWriter(Appendable writer, char separator, char quotechar) {
		this(writer, separator, quotechar, quotechar);
	}

	/**
	 * Constructs TsvWriter with supplied separator and quote char.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 * @param escapechar the character to use for escaping quotechars or escapechars
	 */
	public TsvWriter(Appendable writer, char separator, char quotechar, char escapechar) {
		this(writer, separator, quotechar, escapechar, Strings.CRLF);
	}

	/**
	 * Constructs TsvWriter with supplied separator and quote char.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 * @param lineEnd the line feed terminator to use
	 */
	public TsvWriter(Appendable writer, char separator, char quotechar, String lineEnd) {
		this(writer, separator, quotechar, quotechar, lineEnd);
	}

	/**
	 * Constructs TsvWriter with supplied separator, quote char, escape char and line ending.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 * @param escapechar the character to use for escaping quotechars or escapechars
	 * @param lineEnd the line feed terminator to use
	 */
	public TsvWriter(Appendable writer, char separator, char quotechar, char escapechar, String lineEnd) {
		super(writer, separator, quotechar, escapechar,lineEnd);
	}

}
