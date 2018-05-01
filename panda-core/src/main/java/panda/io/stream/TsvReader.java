package panda.io.stream;

import java.io.Reader;

import panda.lang.Chars;

/**
 * TSV reader.
 */
public class TsvReader extends CsvReader {
	/**
	 * Constructs TsvReader using a comma for the separator.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 */
	public TsvReader(Reader reader) {
		this(reader, Chars.TAB);
	}

	/**
	 * Constructs TsvReader with supplied separator.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries.
	 */
	public TsvReader(Reader reader, char separator) {
		super(reader, separator);
	}
	
	
	/**
	 * Constructs TsvReader with supplied separator and quote char.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 */
	public TsvReader(Reader reader, char separator, char quotechar) {
		super(reader, separator, quotechar);
	}
	
}
