package panda.bind.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;


/**
 * A JSONTokener takes a source string and extracts characters and tokens from it.
 * 
 * @see <a href="http://JSON.org">JSON.org</a>
 * @author yf.frank.wang@gmail.com
 */
public class JsonTokener {
	private boolean ignoreComments = true;
	private long character;
	private boolean eof;
	private long index;
	private long line;
	private char previous;
	private Reader reader;
	private boolean usePrevious;

	/**
	 * Construct a JSONTokener from a Reader.
	 * 
	 * @param reader A reader.
	 */
	public JsonTokener(Reader reader) {
		this.reader = reader;
		this.eof = false;
		this.usePrevious = false;
		this.previous = 0;
		this.index = 0;
		this.character = 1;
		this.line = 1;
	}

	/**
	 * Construct a JSONTokener from an InputStream.
	 */
	public JsonTokener(InputStream inputStream) throws JsonException {
		this(new InputStreamReader(inputStream));
	}

	/**
	 * Construct a JSONTokener from a string.
	 * 
	 * @param s A source string.
	 */
	public JsonTokener(String s) {
		this(new StringReader(s));
	}

	/**
	 * set the ignore comments option
	 * @param ignoreComments set the ignore comments option
	 */
	public void setIgnoreComments(boolean ignoreComments) {
		this.ignoreComments = ignoreComments;
	}

	/**
	 * Back up one character. This provides a sort of lookahead capability, so that you can test for
	 * a digit or letter before attempting to parse the next number or identifier.
	 */
	public void back() throws JsonException {
		if (this.usePrevious || this.index <= 0) {
			throw new JsonException("Stepping back two steps is not supported");
		}
		this.index -= 1;
		this.character -= 1;
		this.usePrevious = true;
		this.eof = false;
	}

	public boolean end() {
		return this.eof && !this.usePrevious;
	}

	/**
	 * Determine if the source string still contains characters that next() can consume.
	 * 
	 * @return true if not yet at the end of the source.
	 */
	public boolean more() throws JsonException {
		this.next();
		if (this.end()) {
			return false;
		}
		this.back();
		return true;
	}

	/**
	 * Look at the next character in the source string.
	 * 
	 * @return The next character, or 0 if past the end of the source string.
	 */
	public char peek() {
		char c = this.next();
		if (!this.end()) {
			this.back();
		}
		return c;
	}

	/**
	 * Get the next character in the source string.
	 * 
	 * @return The next character, or 0 if past the end of the source string.
	 */
	public char next() throws JsonException {
		int c;
		if (this.usePrevious) {
			this.usePrevious = false;
			c = this.previous;
		}
		else {
			try {
				c = this.reader.read();
			}
			catch (IOException exception) {
				throw new JsonException(exception);
			}

			if (c <= 0) { // End of stream
				this.eof = true;
				c = 0;
			}
		}
		this.index += 1;
		if (this.previous == '\r') {
			this.line += 1;
			this.character = c == '\n' ? 0 : 1;
		}
		else if (c == '\n') {
			this.line += 1;
			this.character = 0;
		}
		else {
			this.character += 1;
		}
		this.previous = (char)c;
		return this.previous;
	}

	/**
	 * Consume the next character, and check that it matches a specified character.
	 * 
	 * @param c The character to match.
	 * @return The character.
	 * @throws JsonException if the character does not match.
	 */
	public char next(char c) throws JsonException {
		char n = this.next();
		if (n != c) {
			throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
		}
		return n;
	}

	/**
	 * Get the next n characters.
	 * 
	 * @param n The number of characters to take.
	 * @return A string of n characters.
	 * @throws JsonException Substring bounds error if there are not n characters remaining in the
	 *             source string.
	 */
	public String next(int n) throws JsonException {
		if (n == 0) {
			return "";
		}

		char[] chars = new char[n];
		int pos = 0;

		while (pos < n) {
			chars[pos] = this.next();
			if (this.end()) {
				throw this.syntaxError("Substring bounds error");
			}
			pos += 1;
		}
		return new String(chars);
	}

	/**
	 * Get the next char in the string, skipping whitespace.
	 * 
	 * @return A character, or 0 if there are no more characters.
	 * @throws JsonException if a json syntax error occurs
	 */
	public char nextClean() throws JsonException {
		char c = this.skipBlank();
		if (ignoreComments) {
			while (true) {
				if (c == '/') {
					char n = this.peek();
					if (n == '/') {
						// inline comment
						c = this.skipTo('\n');
					}
					else if (n == '*') {
						// block comment
						c = this.skipBlockComment();
					}
					else {
						throw this.syntaxError("Comment syntax error");
					}
				}
				else if (c == '#') {
					// inline comment
					c = this.skipTo('\n');
				}
				else {
					break;
				}

				if (c == 0) {
					break;
				}
				if (c <= ' ') {
					c = this.skipBlank();
				}
			}
		}
		return c;
	}

	private char skipBlank() throws JsonException {
		for (;;) {
			char c = this.next();
			if (c == 0 || c > ' ') {
				return c;
			}
		}
	}

	private char skipBlockComment() throws JsonException {
		char c = this.next(); // skip '*'
		while (c > 0) {
			c = this.skipTo('*');
			if (c > 0) {
				c = this.next();
				if (c == '/') {
					c = this.next();
					break;
				}
			}
		}
		return c;
	}

	/**
	 * Return the characters up to the next close quote character. Backslash processing is done. The
	 * formal JSON format does not allow strings in single quotes, but an implementation is allowed
	 * to accept them.
	 * 
	 * @param quote The quoting character, either <code>"</code>&nbsp;<small>(double quote)</small>
	 *            or <code>'</code>&nbsp;<small>(single quote)</small>.
	 * @return A String.
	 * @throws JsonException Unterminated string.
	 */
	public String nextString(char quote) throws JsonException {
		char c;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			c = this.next();
			switch (c) {
			case 0:
			case '\n':
			case '\r':
				throw this.syntaxError("Unterminated string");
			case '\\':
				c = this.next();
				switch (c) {
				case 'b':
					sb.append('\b');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 'u':
					sb.append((char)Integer.parseInt(this.next(4), 16));
					break;
				case '"':
				case '\'':
				case '\\':
				case '/':
					sb.append(c);
					break;
				default:
					throw this.syntaxError("Illegal escape.");
				}
				break;
			default:
				if (c == quote) {
					return sb.toString();
				}
				sb.append(c);
			}
		}
	}

	/**
	 * Get the text up but not including the '='specified character or the end of line, whichever comes
	 * first.
	 * 
	 * @return A id string.
	 */
	public String nextId() throws JsonException {
		StringBuilder sb = new StringBuilder();
		for (;;) {
			char c = this.next();
			if (c == 0 || !Character.isJavaIdentifierPart(c)) {
				if (c != 0) {
					this.back();
				}
				return sb.toString().trim();
			}
			sb.append(c);
		}
	}

	/**
	 * Get the text up but not including the specified character or the end of line, whichever comes
	 * first.
	 * 
	 * @param delimiter A delimiter character.
	 * @return A string.
	 */
	public String nextTo(char delimiter) throws JsonException {
		StringBuilder sb = new StringBuilder();
		for (;;) {
			char c = this.next();
			if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
				if (c != 0) {
					this.back();
				}
				return sb.toString().trim();
			}
			sb.append(c);
		}
	}

	/**
	 * Get the text up but not including one of the specified delimiter characters or the end of
	 * line, whichever comes first.
	 * 
	 * @param delimiters A set of delimiter characters.
	 * @return A string, trimmed.
	 */
	public String nextTo(String delimiters) throws JsonException {
		char c;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			c = this.next();
			if (delimiters.indexOf(c) >= 0 || c == 0 || c == '\n' || c == '\r') {
				if (c != 0) {
					this.back();
				}
				return sb.toString().trim();
			}
			sb.append(c);
		}
	}

	/**
	 * Skip characters until the next character is the requested character. 
	 * 
	 * @param to A character to skip to.
	 * @return The requested character, or 0 if past the end of the source string.
	 */
	public char skipTo(char to) throws JsonException {
		char c;
		do {
			c = this.next();
			if (c == 0) {
				return c;
			}
		}
		while (c != to);
		return c;
	}

	/**
	 * Make a JSONException to signal a syntax error.
	 * 
	 * @param message The error message.
	 * @return A JSONException object, suitable for throwing
	 */
	public JsonException syntaxError(String message) {
		return new JsonException(message + this.toString());
	}

	/**
	 * Make a printable string of this JSONTokener.
	 * 
	 * @return " at {index} [character {character} line {line}]"
	 */
	public String toString() {
		return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
	}
}
