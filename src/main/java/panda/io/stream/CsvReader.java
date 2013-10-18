package panda.io.stream;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV reader.
 * @author yf.frank.wang@gmail.com
 */
public class CsvReader implements Closeable {
	private BufferedReader br;

	private char separator;

	private char quotechar;
	
	/** comma separator */
	public static final char COMMA_SEPARATOR = ',';

	/** tab separator */
	public static final char TAB_SEPARATOR = '\t';

	/**
	 * The default quote character to use if none is supplied to the
	 * constructor.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';
	
	/**
	 * Constructs CSVReader using a comma for the separator.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 */
	public CsvReader(Reader reader) {
		this(reader, COMMA_SEPARATOR);
	}

	/**
	 * Constructs CSVReader with supplied separator.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries.
	 */
	public CsvReader(Reader reader, char separator) {
		this(reader, separator, DEFAULT_QUOTE_CHARACTER);
	}
	
	
	/**
	 * Constructs CSVReader with supplied separator and quote char.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param quotechar the character to use for quoted elements
	 */
	public CsvReader(Reader reader, char separator, char quotechar) {
		this.br = new BufferedReader(reader);
		this.separator = separator;
		this.quotechar = quotechar;
	}
	
	/**
	 * Reads the entire file into a List with each element being a String[] of tokens.
	 * 
	 * @return a List of String[], with each String[] representing a line of the file.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<List<String>> readAll() throws IOException {
		List<List<String>> allElements = new ArrayList<List<String>>();
		while (true) {
			List<String> nextLineAsTokens = readNext();
			if (nextLineAsTokens == null) {
				break;
			}
			allElements.add(nextLineAsTokens);
		}
		return allElements;

	}

	/**
	 * Reads the next line from the buffer and converts to a string array.
	 * 
	 * @return a string array with each comma-separated element as a separate entry.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<String> readNext() throws IOException {
		String nextLine = getNextLine();
		if (nextLine != null) {
			return parseLine(nextLine);
		}
		return null;
	}

	/**
	 * Skip 1 line
	 * @return skiped lines
	 * @throws IOException if bad things happen during the read
	 */
	public int skipLine() throws IOException {
		String line = br.readLine();
		return line == null ? 0 : 1;
	}

	/**
	 * Skip lines
	 * @param skipLines the number of lines to skip
	 * @return skiped lines
	 * @throws IOException if bad things happen during the read
	 */
	public int skipLines(int skipLines) throws IOException {
		int i = 0;
		for (; i < skipLines; i++) {
			if (br.readLine() == null) {
				break;
			}
		}
		return i;
	}
	
	/**
	 * Reads the next line from the file.
	 * 
	 * @return the next line from the file without trailing newline
	 * @throws IOException if bad things happen during the read
	 */
	private String getNextLine() throws IOException {
		return br.readLine();
	}

	/**
	 * Parses an incoming String and returns an array of elements.
	 * 
	 * @param nextLine the string to parse
	 * @return the comma-tokenized list of elements, or null if nextLine is null
	 * @throws IOException if bad things happen during the read
	 */
	private List<String> parseLine(String nextLine) throws IOException {
		List<String> tokensOnThisLine = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		boolean inQuotes = false;
		do {
			if (inQuotes) {
				// continuing a quoted section, reappend newline
				sb.append("\n");
				nextLine = getNextLine();
				if (nextLine == null)
					break;
			}
			for (int i = 0; i < nextLine.length(); i++) {
				char c = nextLine.charAt(i);
				if (c == quotechar) {
					// this gets complex... the quote may end a quoted block, or escape another quote.
					// do a 1-char lookahead:
					if( inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
						&& nextLine.length() > (i+1)  // there is indeed another character to check.
						&& nextLine.charAt(i+1) == quotechar ){ // ..and that char. is a quote also.
						// we have two quote chars in a row == one quote char, so consume them both and
						// put one on the token. we do *not* exit the quoted text.
						sb.append(nextLine.charAt(i+1));
						i++;
					}
					else {
						inQuotes = !inQuotes;
						// the tricky case of an embedded quote in the middle: a,bc"d"ef,g
						if (i > 2 //not on the begining of the line
								&& nextLine.charAt(i - 1) != separator //not at the begining of an escape sequence 
								&& nextLine.length() > (i + 1) 
								&& nextLine.charAt(i + 1) != separator //not at the	end of an escape sequence 
							) {
							sb.append(c);
						}
					}
				}
				else if (c == separator && !inQuotes) {
					tokensOnThisLine.add(sb.toString());
					sb = new StringBuilder(); // start work on next token
				}
				else {
					sb.append(c);
				}
			}
		} while (inQuotes);

		tokensOnThisLine.add(sb.toString());
		return tokensOnThisLine;
	}

	/**
	 * Closes the underlying reader.
	 * 
	 * @throws IOException if the close fails
	 */
	public void close() throws IOException{
		br.close();
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
}
