package panda.doc.markdown;

import panda.doc.html.HTML;

/**
 * Utilities.
 * 
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
class Utils {
	/** Random number generator value. */
	private static int RND = (int)System.nanoTime();

	/**
	 * LCG random number generator.
	 * 
	 * @return A pseudo random number between 0 and 1023
	 */
	public final static int rnd() {
		return (RND = RND * 1664525 + 1013904223) >>> 22;
	}

	/**
	 * Skips spaces in the given String.
	 * 
	 * @param in Input String.
	 * @param start Starting position.
	 * @return The new position or -1 if EOL has been reached.
	 */
	public final static int skipSpaces(final String in, final int start) {
		int pos = start;
		while (pos < in.length() && (in.charAt(pos) == ' ' || in.charAt(pos) == '\n'))
			pos++;
		return pos < in.length() ? pos : -1;
	}

	/**
	 * Processed the given escape sequence.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param ch The character.
	 * @param pos Current parsing position.
	 * @return The new position.
	 */
	public final static int escape(final StringBuilder out, final char ch, final int pos) {
		switch (ch) {
		case '\\':
		case '[':
		case ']':
		case '(':
		case ')':
		case '{':
		case '}':
		case '#':
		case '"':
		case '\'':
		case '.':
		case '>':
		case '<':
		case '*':
		case '+':
		case '-':
		case '_':
		case '!':
		case '`':
		case '^':
			out.append(ch);
			return pos + 1;
		default:
			out.append('\\');
			return pos;
		}
	}

	/**
	 * Reads characters until any 'end' character is encountered.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in The Input String.
	 * @param start Starting position.
	 * @param end End characters.
	 * @return The new position or -1 if no 'end' char was found.
	 */
	public final static int readUntil(final StringBuilder out, final String in, final int start, final char... end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == '\\' && pos + 1 < in.length()) {
				pos = escape(out, in.charAt(pos + 1), pos);
			}
			else {
				boolean endReached = false;
				for (int n = 0; n < end.length; n++) {
					if (ch == end[n]) {
						endReached = true;
						break;
					}
				}
				if (endReached)
					break;
				out.append(ch);
			}
			pos++;
		}

		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until the 'end' character is encountered.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in The Input String.
	 * @param start Starting position.
	 * @param end End characters.
	 * @return The new position or -1 if no 'end' char was found.
	 */
	public final static int readUntil(final StringBuilder out, final String in, final int start, final char end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == '\\' && pos + 1 < in.length()) {
				pos = escape(out, in.charAt(pos + 1), pos);
			}
			else {
				if (ch == end)
					break;
				out.append(ch);
			}
			pos++;
		}

		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads a markdown link.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Starting position.
	 * @return The new position or -1 if this is no valid markdown link.
	 */
	public final static int readMdLink(final StringBuilder out, final String in, final int start) {
		int pos = start;
		int counter = 1;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == '\\' && pos + 1 < in.length()) {
				pos = escape(out, in.charAt(pos + 1), pos);
			}
			else {
				boolean endReached = false;
				switch (ch) {
				case '(':
					counter++;
					break;
				case ' ':
					if (counter == 1)
						endReached = true;
					break;
				case ')':
					counter--;
					if (counter == 0)
						endReached = true;
					break;
				}
				if (endReached)
					break;
				out.append(ch);
			}
			pos++;
		}

		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads a markdown link ID.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Starting position.
	 * @return The new position or -1 if this is no valid markdown link ID.
	 */
	public final static int readMdLinkId(final StringBuilder out, final String in, final int start) {
		int pos = start;
		int counter = 1;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			boolean endReached = false;
			switch (ch) {
			case '\n':
				out.append(' ');
				break;
			case '[':
				counter++;
				out.append(ch);
				break;
			case ']':
				counter--;
				if (counter == 0)
					endReached = true;
				else
					out.append(ch);
				break;
			default:
				out.append(ch);
				break;
			}
			if (endReached)
				break;
			pos++;
		}

		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until any 'end' character is encountered, ignoring escape sequences.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in The Input String.
	 * @param start Starting position.
	 * @param end End characters.
	 * @return The new position or -1 if no 'end' char was found.
	 */
	public final static int readRawUntil(final StringBuilder out, final String in, final int start, final char... end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			boolean endReached = false;
			for (int n = 0; n < end.length; n++) {
				if (ch == end[n]) {
					endReached = true;
					break;
				}
			}
			if (endReached)
				break;
			out.append(ch);
			pos++;
		}

		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until the end character is encountered, ignoring escape sequences.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in The Input String.
	 * @param start Starting position.
	 * @param end End characters.
	 * @return The new position or -1 if no 'end' char was found.
	 */
	public final static int readRawUntil(final StringBuilder out, final String in, final int start, final char end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == end)
				break;
			out.append(ch);
			pos++;
		}

		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until any 'end' character is encountered, ignoring escape sequences.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in The Input String.
	 * @param start Starting position.
	 * @param end End characters.
	 * @return The new position or -1 if no 'end' char was found.
	 */
	public final static int readXMLUntil(final StringBuilder out, final String in, final int start, final char... end) {
		int pos = start;
		boolean inString = false;
		char stringChar = 0;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (inString) {
				if (ch == '\\') {
					out.append(ch);
					pos++;
					if (pos < in.length()) {
						out.append(ch);
						pos++;
					}
					continue;
				}
				if (ch == stringChar) {
					inString = false;
					out.append(ch);
					pos++;
					continue;
				}
			}
			switch (ch) {
			case '"':
			case '\'':
				inString = true;
				stringChar = ch;
				break;
			}
			if (!inString) {
				boolean endReached = false;
				for (int n = 0; n < end.length; n++) {
					if (ch == end[n]) {
						endReached = true;
						break;
					}
				}
				if (endReached)
					break;
			}
			out.append(ch);
			pos++;
		}

		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Appends the given string encoding special HTML characters.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Input String starting position.
	 * @param end Input String end position.
	 */
	public final static void appendCode(final StringBuilder out, final String in, final int start, final int end) {
		for (int i = start; i < end; i++) {
			final char c;
			switch (c = in.charAt(i)) {
			case '&':
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			default:
				out.append(c);
				break;
			}
		}
	}

	public final static void appendValue(final StringBuilder out, final String in) {
		if (in == null || in.length() <= 0) {
			return;
		}
		appendValue(out, in, 0, in.length());
	}
	
	/**
	 * Appends the given string encoding special HTML characters (used in HTML attribute values).
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Input String starting position.
	 * @param end Input String end position.
	 */
	public final static void appendValue(final StringBuilder out, final String in, final int start, final int end) {
		for (int i = start; i < end; i++) {
			final char c;
			switch (c = in.charAt(i)) {
			case '&':
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			case '"':
				out.append("&quot;");
				break;
			case '\'':
				out.append("&apos;");
				break;
			default:
				out.append(c);
				break;
			}
		}
	}

	/**
	 * Append the given char as a decimal HTML entity.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param value The character.
	 */
	public final static void appendDecEntity(final StringBuilder out, final char value) {
		out.append("&#");
		out.append((int)value);
		out.append(';');
	}

	/**
	 * Append the given char as a hexadecimal HTML entity.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param value The character.
	 */
	public final static void appendHexEntity(final StringBuilder out, final char value) {
		out.append("&#x");
		out.append(Integer.toHexString(value));
		out.append(';');
	}

	/**
	 * Appends the given mailto link using obfuscation.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Input String starting position.
	 * @param end Input String end position.
	 */
	public final static void appendMailto(final StringBuilder out, final String in, final int start, final int end) {
		for (int i = start; i < end; i++) {
			final char c;
			final int r = rnd();
			switch (c = in.charAt(i)) {
			case '&':
			case '<':
			case '>':
			case '"':
			case '\'':
			case '@':
				if (r < 512)
					appendDecEntity(out, c);
				else
					appendHexEntity(out, c);
				break;
			default:
				if (r < 32)
					out.append(c);
				else if (r < 520)
					appendDecEntity(out, c);
				else
					appendHexEntity(out, c);
				break;
			}
		}
	}

	/**
	 * Extracts the tag from an XML element.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input StringBuilder.
	 */
	public final static void getXMLTag(final StringBuilder out, final StringBuilder in) {
		int pos = 1;
		if (in.charAt(1) == '/')
			pos++;
		while (Character.isLetterOrDigit(in.charAt(pos))) {
			out.append(in.charAt(pos++));
		}
	}

	/**
	 * Extracts the tag from an XML element.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 */
	public final static void getXMLTag(final StringBuilder out, final String in) {
		int pos = 1;
		if (in.charAt(1) == '/')
			pos++;
		while (Character.isLetterOrDigit(in.charAt(pos))) {
			out.append(in.charAt(pos++));
		}
	}

	/**
	 * Reads an XML element.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Starting position.
	 * @param safeMode Whether to escape unsafe HTML tags or not
	 * @return The new position or -1 if this is no valid XML element.
	 */
	public final static int readXML(final StringBuilder out, final String in, final int start, final boolean safeMode) {
		int pos;
		final boolean isCloseTag;
		try {
			if (in.charAt(start + 1) == '/') {
				isCloseTag = true;
				pos = start + 2;
			}
			else if (in.charAt(start + 1) == '!') {
				out.append("<!");
				return start + 1;
			}
			else {
				isCloseTag = false;
				pos = start + 1;
			}
			if (safeMode) {
				final StringBuilder temp = new StringBuilder();
				pos = readRawUntil(temp, in, pos, ' ', '/', '>');
				if (pos == -1)
					return -1;
				final String tag = temp.toString().trim().toLowerCase();
				if (HTML.isUnsafeElement(tag)) {
					out.append("&lt;");
					if (isCloseTag)
						out.append('/');
					out.append(temp);
				}
			}
			else {
				out.append('<');
				if (isCloseTag)
					out.append('/');
				pos = readRawUntil(out, in, pos, ' ', '/', '>');
			}
			if (pos == -1)
				return -1;
			pos = readRawUntil(out, in, pos, '/', '>');
			if (in.charAt(pos) == '/') {
				if (out.length() < 1 || out.charAt(out.length() - 1) != ' ') {
					out.append(' ');
				}
				out.append('/');
				pos = readRawUntil(out, in, pos + 1, '>');
				if (pos == -1)
					return -1;
			}
			if (in.charAt(pos) == '>') {
				out.append('>');
				return pos;
			}
		}
		catch (StringIndexOutOfBoundsException e) {
			return -1;
		}
		return -1;
	}

	/**
	 * Removes trailing <code>`</code> and trims spaces.
	 * 
	 * @param fenceLine Fenced code block starting line
	 * @return Rest of the line after trimming and backtick removal
	 */
	public final static String getMetaFromFence(String fenceLine) {
		char f = ' ';
		for (int i = 0; i < fenceLine.length(); i++) {
			final char c = fenceLine.charAt(i);
			if (Character.isWhitespace(c)) {
				continue;
			}
			if (f == ' ') {
				f = c;
				continue;
			}
			if (f != c) {
				return fenceLine.substring(i).trim();
			}
		}
		return "";
	}
}
