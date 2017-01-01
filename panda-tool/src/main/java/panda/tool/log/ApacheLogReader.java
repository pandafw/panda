package panda.tool.log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ApacheLogReader implements Closeable {
	private BufferedReader br;

	private char separator;
	private String leftQuotes;
	private String rightQuotes;

	/**
	 * The left quote characters
	 */
	public static final String LEFT_QUOTES = "[\"";

	/**
	 * The right quote characters
	 */
	public static final String RIGHT_QUOTES = "]\"";

	/** 
	 * space separator 
	 */
	public static final char SPACE_SEPARATOR = ' ';
	
	/**
	 * Constructs using default format.
	 * 
	 * @param reader the reader to an underlying apache log source.
	 */
	public ApacheLogReader(Reader reader) {
		this(reader, SPACE_SEPARATOR);
	}

	/**
	 * Constructs CSVReader with supplied separator.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries.
	 */
	public ApacheLogReader(Reader reader, char separator) {
		this(reader, separator, LEFT_QUOTES, RIGHT_QUOTES);
	}
	
	
	/**
	 * Constructs CSVReader with supplied separator and quote char.
	 * 
	 * @param reader the reader to an underlying CSV source.
	 * @param separator the delimiter to use for separating entries
	 * @param leftQuotes the character to use for quoted elements
	 * @param rightQuotes the character to use for quoted elements
	 */
	public ApacheLogReader(Reader reader, char separator, String leftQuotes, String rightQuotes) {
		this.br = new BufferedReader(reader);
		this.separator = separator;
		this.leftQuotes = leftQuotes;
		this.rightQuotes = rightQuotes;
	}
	
	/**
	 * Reads the entire file into a List with each element being a String[] of tokens.
	 * 
	 * @return a List of String[], with each String[] representing a line of the file.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<List<String>> readAll() throws IOException {
		List<List<String>> all = new ArrayList<List<String>>();
		while (true) {
			List<String> items = readNext();
			if (items == null) {
				break;
			}
			all.add(items);
		}
		return all;

	}

	/**
	 * Reads the next line from the buffer and converts to a string array.
	 * 
	 * @return a string array with each comma-separated element as a separate entry.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<String> readNext() throws IOException {
		String line = getNextLine();
		if (line != null) {
			return parseLine(line);
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
	 * @param line the string to parse
	 * @return the comma-tokenized list of elements, or null if nextLine is null
	 * @throws IOException if bad things happen during the read
	 */
	private List<String> parseLine(String line) throws IOException {
		List<String> items = new ArrayList<String>();

		for (int i = 0; i < line.length(); ) {
			char c = line.charAt(i);
			if (c == separator) {
				i++;
				continue;
			}
			
			int quote = leftQuotes.indexOf(c);
			if (quote >= 0) {
				int rq = rightQuotes.charAt(quote);
				int j = i + 1;
				for (; j < line.length(); j++) {
					int nc = line.charAt(j);
					if (nc == '\\') {
						j++;
					}
					else if (nc == rq) {
						break;
					}
				}
				if (j > i) {
					items.add(line.substring(i + 1, j));
					i = j + 1;
				}
				else {
					// not found right quote
					items.add(line.substring(i + 1));
					break;
				}
			}
			else {
				int j = line.indexOf(separator, i + 1);
				if (j > i) {
					items.add(line.substring(i, j));
					i = j + 1;
				}
				else {
					// not found right seperator
					items.add(line.substring(i));
					break;
				}
			}
		}

		return items;
	}

	/**
	 * Closes the underlying reader.
	 * 
	 * @throws IOException if the close fails
	 */
	public void close() throws IOException{
		br.close();
	}
}
