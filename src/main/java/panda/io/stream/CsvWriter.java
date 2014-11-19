package panda.io.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import panda.lang.Collections;
import panda.lang.Systems;

/**
 * CSV writer
 * @author yf.frank.wang@gmail.com
 */
public class CsvWriter implements Closeable, Flushable {

	private Appendable writer;

	private char separator;

	private char quotechar;

	private char escapechar;

	private String lineEnd;

	/** The character used for escaping quotes. */
	public static final char DEFAULT_ESCAPE_CHARACTER = '"';

	/** The default separator to use if none is supplied to the constructor. */
	public static final char DEFAULT_SEPARATOR = ',';

	/**
	 * The default quote character to use if none is supplied to the
	 * constructor.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';

	/** The quote constant to use when you wish to suppress all quoting. */
	public static final char NO_QUOTE_CHARACTER = '\u0000';

	/** The escape constant to use when you wish to suppress all escaping. */
	public static final char NO_ESCAPE_CHARACTER = '\u0000';

	/** Default line terminator uses platform encoding. */
	public static final String DEFAULT_LINE_END = Systems.LINE_SEPARATOR;

	/**
	 * Constructs CSVWriter using a comma for the separator.
	 *
	 * @param writer the writer to an underlying CSV source.
	 */
	public CsvWriter(Appendable writer) {
		this(writer, DEFAULT_SEPARATOR);
	}

	/**
	 * Constructs CSVWriter with supplied separator.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries.
	 */
	public CsvWriter(Appendable writer, char separator) {
		this(writer, separator, DEFAULT_QUOTE_CHARACTER);
	}

	/**
	 * Constructs CSVWriter with supplied separator and quote char.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 */
	public CsvWriter(Appendable writer, char separator, char quotechar) {
		this(writer, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
	}

	/**
	 * Constructs CSVWriter with supplied separator and quote char.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 * @param escapechar the character to use for escaping quotechars or escapechars
	 */
	public CsvWriter(Appendable writer, char separator, char quotechar, char escapechar) {
		this(writer, separator, quotechar, escapechar, DEFAULT_LINE_END);
	}

	/**
	 * Constructs CSVWriter with supplied separator and quote char.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 * @param lineEnd the line feed terminator to use
	 */
	public CsvWriter(Appendable writer, char separator, char quotechar, String lineEnd) {
		this(writer, separator, quotechar, DEFAULT_ESCAPE_CHARACTER, lineEnd);
	}

	/**
	 * Constructs CSVWriter with supplied separator, quote char, escape char and line ending.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 * @param escapechar the character to use for escaping quotechars or escapechars
	 * @param lineEnd the line feed terminator to use
	 */
	public CsvWriter(Appendable writer, char separator, char quotechar, char escapechar, String lineEnd) {
		this.writer = writer;
		this.separator = separator;
		this.quotechar = quotechar;
		this.escapechar = escapechar;
		this.lineEnd = lineEnd;
	}

	/**
	 * Writes the entire list to a CSV file. The list is assumed to be a String[]
	 *
	 * @param allLines a List of String[], with each String[] representing a line of the file.
	 */
	public void writeAll(List allLines) throws IOException {
		for (Iterator iter = allLines.iterator(); iter.hasNext();) {
			Object nextLine = iter.next();
			if (nextLine == null){
				writeNext(Collections.EMPTY_LIST);
			}
			else if (nextLine instanceof Collection) {
				writeNext((Collection)nextLine);
			}
			else if (nextLine instanceof String[]) {
				writeNext((String[])nextLine);
			}
			else {
				throw new IllegalArgumentException("the element of list is not a instance of Collection or String[].");
			}
		}

	}

	/**
	 * Writes the next element to the string buffer.
	 *
	 * @param sb string buffer to write to
	 * @param nextElement a string to be write to string buffer
	 */
	private void writeNextElement(StringBuilder sb, String nextElement) {
		if (quotechar !=  NO_QUOTE_CHARACTER) {
			sb.append(quotechar);
		}
		for (int j = 0; j < nextElement.length(); j++) {
			char nextChar = nextElement.charAt(j);
			if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
				sb.append(escapechar).append(nextChar);
			}
			else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
				sb.append(escapechar).append(nextChar);
			}
			else {
				sb.append(nextChar);
			}
		}
		if (quotechar != NO_QUOTE_CHARACTER) {
			sb.append(quotechar);
		}
	}

	/**
	 * Writes the next line to the file.
	 *
	 * @param nextLine a collection with each comma-separated element as a separate entry.
	 */
	public void writeNext(Collection nextLine) throws IOException {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (Iterator it = nextLine.iterator(); it.hasNext();) {
			if (i != 0) {
				sb.append(separator);
			}

			Object nextElement = it.next();
			writeNextElement(sb, nextElement == null ? "" : nextElement.toString());

			i++;
		}

		sb.append(lineEnd);
		writer.append(sb);
	}

	/**
	 * Writes the next line to the file.
	 *
	 * @param nextLine a string array with each comma-separated element as a separate entry.
	 */
	public void writeNext(String[] nextLine) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nextLine.length; i++) {
			if (i != 0) {
				sb.append(separator);
			}

			String nextElement = nextLine[i];
			writeNextElement(sb, nextElement == null ? "" : nextElement);
		}

		sb.append(lineEnd);
		writer.append(sb);
	}

	/**
	 * Flush underlying stream to writer.
	 *
	 * @throws IOException if bad things happen
	 */
	public void flush() throws IOException {
		if (writer instanceof Flushable) {
			((Flushable)writer).flush();
		}
	}

	/**
	 * Close the underlying stream writer flushing any buffered content.
	 *
	 * @throws IOException if bad things happen
	 *
	 */
	public void close() throws IOException {
		if (writer instanceof Closeable) {
			((Closeable)writer).close();
		}
	}

	/**
	 * @return the separator
	 */
	public char getSeparator() {
		return separator;
	}

	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(char separator) {
		this.separator = separator;
	}

	/**
	 * @return the quotechar
	 */
	public char getQuotechar() {
		return quotechar;
	}

	/**
	 * @param quotechar the quotechar to set
	 */
	public void setQuotechar(char quotechar) {
		this.quotechar = quotechar;
	}

	/**
	 * @return the escapechar
	 */
	public char getEscapechar() {
		return escapechar;
	}

	/**
	 * @param escapechar the escapechar to set
	 */
	public void setEscapechar(char escapechar) {
		this.escapechar = escapechar;
	}

	/**
	 * @return the lineEnd
	 */
	public String getLineEnd() {
		return lineEnd;
	}

	/**
	 * @param lineEnd the lineEnd to set
	 */
	public void setLineEnd(String lineEnd) {
		this.lineEnd = lineEnd;
	}

}
