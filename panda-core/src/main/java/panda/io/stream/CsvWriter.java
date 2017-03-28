package panda.io.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import panda.lang.Arrays;
import panda.lang.Chars;
import panda.lang.Collections;
import panda.lang.Strings;

/**
 * CSV writer
 */
public class CsvWriter implements ListWriter, Closeable, Flushable {

	private Appendable writer;

	private char separator;

	private char quotechar;

	private char escapechar;

	private String lineEnd;

	private boolean forceQuote;
	
	/**
	 * Constructs CSVWriter using a comma for the separator.
	 *
	 * @param writer the writer to an underlying CSV source.
	 */
	public CsvWriter(Appendable writer) {
		this(writer, Chars.COMMA);
	}

	/**
	 * Constructs CSVWriter with supplied separator.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries.
	 */
	public CsvWriter(Appendable writer, char separator) {
		this(writer, separator, Chars.DOUBLE_QUOTE);
	}

	/**
	 * Constructs CSVWriter with supplied separator and quote char.
	 *
	 * @param writer the writer to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 */
	public CsvWriter(Appendable writer, char separator, char quotechar) {
		this(writer, separator, quotechar, quotechar);
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
		this(writer, separator, quotechar, escapechar, Strings.CRLF);
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
		this(writer, separator, quotechar, quotechar, lineEnd);
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
	 * @throws IOException if an IO error occurred
	 */
	public void writeAll(List<?> allLines) throws IOException {
		for (Iterator iter = allLines.iterator(); iter.hasNext();) {
			Object nextLine = iter.next();
			if (nextLine == null){
				writeList(Collections.EMPTY_LIST);
			}
			else if (nextLine instanceof Collection) {
				writeList((Collection)nextLine);
			}
			else if (nextLine instanceof Object[]) {
				writeArray((Object[])nextLine);
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
	 * @param str a string to be write to string buffer
	 * @throws IOException 
	 */
	private void writeItem(String str) throws IOException {
		if (forceQuote) {
			writer.append(quotechar);
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (c == quotechar || c == escapechar) {
					writer.append(escapechar).append(c);
				}
				else {
					writer.append(c);
				}
			}
			writer.append(quotechar);
		}
		else {
			boolean quoted = false;
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (c == quotechar || c == escapechar) {
					if (!quoted) {
						quoted = true;
						writer.append(quotechar);
						writer.append(str, 0, i);
					}
					writer.append(escapechar).append(c);
				}
				else if (c == Chars.CR || c == Chars.LF) {
					if (!quoted) {
						quoted = true;
						writer.append(quotechar);
						writer.append(str, 0, i);
					}
					writer.append(c);
				}
				else {
					if (quoted) {
						writer.append(c);
					}
				}
			}
			if (quoted) {
				writer.append(quotechar);
			}
			else {
				writer.append(str);
			}
		}
	}

	/**
	 * Writes the list to the file.
	 *
	 * @param list a collection with each comma-separated element as a separate entry.
	 */
	@Override
	public void writeList(Collection<?> list) throws IOException {
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			if (i != 0) {
				writer.append(separator);
			}

			Object e = it.next();
			writeItem(e == null ? "" : e.toString());

			i++;
		}

		writer.append(lineEnd);
	}

	/**
	 * Writes the next line to the file.
	 *
	 * @param array a string array with each comma-separated element as a separate entry.
	 * @throws IOException if an IO error occurred
	 */
	public void writeArray(Object[] array) throws IOException {
		writeList(Arrays.asList(array));
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

	/**
	 * @return the forceQuote
	 */
	public boolean isForceQuote() {
		return forceQuote;
	}

	/**
	 * @param forceQuote the forceQuote to set
	 */
	public void setForceQuote(boolean forceQuote) {
		this.forceQuote = forceQuote;
	}

}
