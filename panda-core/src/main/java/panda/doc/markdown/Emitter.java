package panda.doc.markdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.doc.html.HTML;
import panda.lang.Strings;

/**
 * Emitter class responsible for generating HTML output.
 */
class Emitter {
	/** Link references. */
	private final HashMap<String, LinkRef> linkRefs = new HashMap<String, LinkRef>();
	/** The configuration. */
	private final Configuration config;
	/** Extension flag. */
	public boolean useExtensions = false;
	/** Newline flag. */
	public boolean convertNewline2Br = false;
	/** Plugins references **/
	private Map<String, Plugin> plugins = new HashMap<String, Plugin>();

	/** 
	 * Constructor. 
	 * @param config the Configuration
	 */
	public Emitter(final Configuration config) {
		this.config = config;
		this.useExtensions = config.forceExtendedProfile;
		this.convertNewline2Br = config.convertNewline2Br;
		for (Plugin plugin : config.plugins) {
			register(plugin);
		}
	}

	public void register(Plugin plugin) {
		plugins.put(plugin.getIdPlugin(), plugin);
	}

	/**
	 * Adds a LinkRef to this set of LinkRefs.
	 * 
	 * @param key The key/id.
	 * @param linkRef The LinkRef.
	 */
	public void addLinkRef(final String key, final LinkRef linkRef) {
		this.linkRefs.put(key.toLowerCase(), linkRef);
	}

	/**
	 * Transforms the given block recursively into HTML.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param root The Block to process.
	 */
	@SuppressWarnings("incomplete-switch")
	public void emit(final StringBuilder out, final Block root) {
		root.removeSurroundingEmptyLines();

		switch (root.type) {
		case RULER:
			this.config.decorator.horizontalRuler(out);
			return;
		case NONE:
		case XML:
			break;
		case HEADLINE:
			this.config.decorator.openHeadline(out, root.hlDepth);
			if (this.useExtensions && root.id != null) {
				out.append(" id=\"");
				Utils.appendCode(out, root.id, 0, root.id.length());
				out.append('"');
			}
			out.append('>');
			break;
		case PARAGRAPH:
			this.config.decorator.openParagraph(out);
			break;
		case CODE:
		case FENCED_CODE:
			if (this.config.codeBlockEmitter == null)
				this.config.decorator.openCodeBlock(out);
			break;
		case BLOCKQUOTE:
			this.config.decorator.openBlockquote(out);
			break;
		case UNORDERED_LIST:
			this.config.decorator.openUnorderedList(out);
			break;
		case ORDERED_LIST:
			this.config.decorator.openOrderedList(out);
			break;
		case LIST_ITEM:
			this.config.decorator.openListItem(out);
			if (this.useExtensions && root.id != null) {
				out.append(" id=\"");
				Utils.appendCode(out, root.id, 0, root.id.length());
				out.append('"');
			}
			out.append('>');
			break;
		}

		if (root.hasLines()) {
			this.emitLines(out, root);
		}
		else {
			Block block = root.blocks;
			while (block != null) {
				this.emit(out, block);
				block = block.next;
			}
		}

		switch (root.type) {
		case RULER:
		case NONE:
		case XML:
			break;
		case HEADLINE:
			this.config.decorator.closeHeadline(out, root.hlDepth);
			break;
		case PARAGRAPH:
			this.config.decorator.closeParagraph(out);
			break;
		case CODE:
		case FENCED_CODE:
			if (this.config.codeBlockEmitter == null)
				this.config.decorator.closeCodeBlock(out);
			break;
		case BLOCKQUOTE:
			this.config.decorator.closeBlockquote(out);
			break;
		case UNORDERED_LIST:
			this.config.decorator.closeUnorderedList(out);
			break;
		case ORDERED_LIST:
			this.config.decorator.closeOrderedList(out);
			break;
		case LIST_ITEM:
			this.config.decorator.closeListItem(out);
			break;
		}
	}

	/**
	 * Transforms lines into HTML.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param block The Block to process.
	 */
	private void emitLines(final StringBuilder out, final Block block) {
		switch (block.type) {
		case CODE:
			this.emitCodeLines(out, block.lines, block.meta, true);
			break;
		case FENCED_CODE:
			this.emitCodeLines(out, block.lines, block.meta, false);
			break;
		case TABLE:
			this.emitTableLines(out, block.lines, block.meta);
			break;
		case TABLEB:
			this.emitTableBLines(out, block.lines, block.meta);
			break;
		case PLUGIN:
			this.emitPluginLines(out, block.lines, block.meta);
			break;
		case XML:
			this.emitRawLines(out, block.lines);
			break;
		case PARAGRAPH:
		default:
			this.emitMarkedLines(out, block.lines);
			break;
		}
	}

	/**
	 * Finds the position of the given Token in the given String.
	 * 
	 * @param in The String to search on.
	 * @param start The starting character position.
	 * @param token The token to find.
	 * @return The position of the token or -1 if none could be found.
	 */
	private int findToken(final String in, int start, MarkToken token) {
		int pos = start;
		while (pos < in.length()) {
			if (this.getToken(in, pos) == token)
				return pos;
			pos++;
		}
		return -1;
	}

	/**
	 * Checks if there is a valid markdown link definition.
	 * 
	 * @param out The StringBuilder containing the generated output.
	 * @param in Input String.
	 * @param start Starting position.
	 * @param token Either LINK or IMAGE.
	 * @return The new position or -1 if there is no valid markdown link.
	 */
	private int checkLink(final StringBuilder out, final String in, int start, MarkToken token) {
		boolean isAbbrev = false;
		int pos = start + (token == MarkToken.LINK ? 1 : 2);
		final StringBuilder temp = new StringBuilder();

		temp.setLength(0);
		pos = Utils.readMdLinkId(temp, in, pos);
		if (pos < start)
			return -1;

		String name = temp.toString(), link = null, comment = null;
		final int oldPos = pos++;
		pos = Utils.skipSpaces(in, pos);
		if (pos < start) {
			final LinkRef lr = this.linkRefs.get(name.toLowerCase());
			if (lr != null) {
				isAbbrev = lr.isAbbrev;
				link = lr.link;
				comment = lr.title;
				pos = oldPos;
			}
			else {
				return -1;
			}
		}
		else if (in.charAt(pos) == '(') {
			pos++;
			pos = Utils.skipSpaces(in, pos);
			if (pos < start)
				return -1;
			temp.setLength(0);
			boolean useLt = in.charAt(pos) == '<';
			pos = useLt ? Utils.readUntil(temp, in, pos + 1, '>') : Utils.readMdLink(temp, in, pos);
			if (pos < start)
				return -1;
			if (useLt)
				pos++;
			link = temp.toString();

			if (in.charAt(pos) == ' ') {
				pos = Utils.skipSpaces(in, pos);
				if (pos > start && in.charAt(pos) == '"') {
					pos++;
					temp.setLength(0);
					pos = Utils.readUntil(temp, in, pos, '"');
					if (pos < start)
						return -1;
					comment = temp.toString();
					pos++;
					pos = Utils.skipSpaces(in, pos);
					if (pos == -1)
						return -1;
				}
			}
			if (in.charAt(pos) != ')')
				return -1;
		}
		else if (in.charAt(pos) == '[') {
			pos++;
			temp.setLength(0);
			pos = Utils.readRawUntil(temp, in, pos, ']');
			if (pos < start)
				return -1;
			final String id = temp.length() > 0 ? temp.toString() : name;
			final LinkRef lr = this.linkRefs.get(id.toLowerCase());
			if (lr != null) {
				link = lr.link;
				comment = lr.title;
			}
			else if (temp.length() <= 0) {
				return -1;
			}
		}
		else {
			final LinkRef lr = this.linkRefs.get(name.toLowerCase());
			if (lr != null) {
				isAbbrev = lr.isAbbrev;
				link = lr.link;
				comment = lr.title;
				pos = oldPos;
			}
			else {
				return -1;
			}
		}

		if (token == MarkToken.LINK) {
			if (isAbbrev && comment != null) {
				if (!this.useExtensions)
					return -1;
				out.append("<abbr title=\"");
				Utils.appendValue(out, comment);
				out.append("\">");
				this.recursiveEmitLine(out, name, 0, name.length(), MarkToken.NONE);
				out.append("</abbr>");
			}
			else {
				this.config.decorator.openLink(out);
				out.append(" href=\"");
				Utils.appendValue(out, link);
				out.append('"');
				if (comment != null && comment.length() > 0) {
					out.append(" title=\"");
					Utils.appendValue(out, comment);
					out.append('"');
				}
				out.append('>');
				this.recursiveEmitLine(out, name, 0, name.length(), MarkToken.NONE);
				this.config.decorator.closeLink(out);
			}
		}
		else {
			this.config.decorator.openImage(out);
			out.append(" src=\"");
			Utils.appendValue(out, link);
			out.append('"');
			if (name != null && name.length() > 0) {
				out.append(" alt=\"");
				Utils.appendValue(out, name);
				out.append('"');
			}
			if (comment != null && comment.length() > 0) {
				out.append(" title=\"");
				Utils.appendValue(out, comment);
				out.append('"');
			}
			this.config.decorator.closeImage(out);
		}

		return pos;
	}

	/**
	 * Check if there is a valid HTML tag here. This method also transforms auto links and mailto
	 * auto links.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Starting position.
	 * @return The new position or -1 if nothing valid has been found.
	 */
	private int checkHtml(final StringBuilder out, final String in, int start) {
		final StringBuilder temp = new StringBuilder();
		int pos;

		// Check for auto links
		temp.setLength(0);
		pos = Utils.readUntil(temp, in, start + 1, ':', ' ', '>', '\n');
		if (pos != -1 && in.charAt(pos) == ':' && HTML.isLinkPrefix(temp.toString())) {
			pos = Utils.readUntil(temp, in, pos, '>');
			if (pos != -1) {
				final String link = temp.toString();
				this.config.decorator.openLink(out);
				out.append(" href=\"");
				Utils.appendValue(out, link, 0, link.length());
				out.append("\">");
				Utils.appendValue(out, link, 0, link.length());
				this.config.decorator.closeLink(out);
				return pos;
			}
		}

		// Check for mailto or adress auto link
		temp.setLength(0);
		pos = Utils.readUntil(temp, in, start + 1, '@', ' ', '>', '\n');
		if (pos != -1 && in.charAt(pos) == '@') {
			pos = Utils.readUntil(temp, in, pos, '>');
			if (pos != -1) {
				final String link = temp.toString();
				this.config.decorator.openLink(out);
				out.append(" href=\"");

				// address auto links
				if (link.startsWith("@")) {
					String slink = link.substring(1);
					String url = "https://maps.google.com/maps?q=" + slink.replace(' ', '+');
					out.append(url);
					out.append("\">");
					out.append(slink);
				}
				// mailto auto links
				else {
					Utils.appendMailto(out, "mailto:", 0, 7);
					Utils.appendMailto(out, link, 0, link.length());
					out.append("\">");
					Utils.appendMailto(out, link, 0, link.length());
				}
				this.config.decorator.closeLink(out);
				return pos;
			}
		}

		// Check for inline html
		if (start + 2 < in.length()) {
			temp.setLength(0);
			return Utils.readXML(out, in, start, this.config.safeMode);
		}

		return -1;
	}

	/**
	 * Check if this is a valid XML/HTML entity.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Starting position
	 * @return The new position or -1 if this entity in invalid.
	 */
	private static int checkEntity(final StringBuilder out, final String in, int start) {
		int pos = Utils.readUntil(out, in, start, ';');
		if (pos < 0 || out.length() < 3)
			return -1;
		if (out.charAt(1) == '#') {
			if (out.charAt(2) == 'x' || out.charAt(2) == 'X') {
				if (out.length() < 4)
					return -1;
				for (int i = 3; i < out.length(); i++) {
					final char c = out.charAt(i);
					if ((c < '0' || c > '9') && ((c < 'a' || c > 'f') && (c < 'A' || c > 'F')))
						return -1;
				}
			}
			else {
				for (int i = 2; i < out.length(); i++) {
					final char c = out.charAt(i);
					if (c < '0' || c > '9')
						return -1;
				}
			}
			out.append(';');
		}
		else {
			for (int i = 1; i < out.length(); i++) {
				final char c = out.charAt(i);
				if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))
					return -1;
			}
			out.append(';');
			return HTML.isEntity(out.toString()) ? pos : -1;
		}

		return pos;
	}

	/**
	 * Recursively scans through the given line, taking care of any markdown stuff.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param in Input String.
	 * @param start Start position.
	 * @param end End position.
	 * @param token The matching Token (for e.g. '*')
	 * @return The position of the matching Token or -1 if token was NONE or no Token could be
	 *         found.
	 */
	private int recursiveEmitLine(final StringBuilder out, final String in, final int start, final int end, MarkToken token) {
		int pos = start, a, b;
		final StringBuilder temp = new StringBuilder();
		while (pos < end) {
			final MarkToken mt = this.getToken(in, pos);
			if (token != MarkToken.NONE
					&& (mt == token || token == MarkToken.EM_STAR && mt == MarkToken.STRONG_STAR || token == MarkToken.EM_UNDERSCORE
							&& mt == MarkToken.STRONG_UNDERSCORE))
				return pos;

			switch (mt) {
			case IMAGE:
			case LINK:
				temp.setLength(0);
				b = this.checkLink(temp, in, pos, mt);
				if (b > 0) {
					out.append(temp);
					pos = b;
				}
				else {
					out.append(in.charAt(pos));
				}
				break;
			case EM_STAR:
			case EM_UNDERSCORE:
				temp.setLength(0);
				b = this.recursiveEmitLine(temp, in, pos + 1, end, mt);
				if (b > 0) {
					this.config.decorator.openEmphasis(out);
					out.append(temp);
					this.config.decorator.closeEmphasis(out);
					pos = b;
				}
				else {
					out.append(in.charAt(pos));
				}
				break;
			case STRONG_STAR:
			case STRONG_UNDERSCORE:
				temp.setLength(0);
				b = this.recursiveEmitLine(temp, in, pos + 2, end, mt);
				if (b > 0) {
					this.config.decorator.openStrong(out);
					out.append(temp);
					this.config.decorator.closeStrong(out);
					pos = b + 1;
				}
				else {
					out.append(in.charAt(pos));
				}
				break;
			case STRIKE:
				temp.setLength(0);
				b = this.recursiveEmitLine(temp, in, pos + 2, end, mt);
				if (b > 0) {
					this.config.decorator.openStrike(out);
					out.append(temp);
					this.config.decorator.closeStrike(out);
					pos = b + 1;
				}
				else {
					out.append(in.charAt(pos));
				}
				break;
			case SUPER:
				temp.setLength(0);
				b = this.recursiveEmitLine(temp, in, pos + 1, end, mt);
				if (b > 0) {
					this.config.decorator.openSuper(out);
					out.append(temp);
					this.config.decorator.closeSuper(out);
					pos = b;
				}
				else {
					out.append(in.charAt(pos));
				}
				break;
			case CODE_SINGLE:
			case CODE_DOUBLE:
				a = pos + (mt == MarkToken.CODE_DOUBLE ? 2 : 1);
				b = this.findToken(in, a, mt);
				if (b > 0) {
					pos = b + (mt == MarkToken.CODE_DOUBLE ? 1 : 0);
					while (a < b && in.charAt(a) == ' ')
						a++;
					if (a < b) {
						while (in.charAt(b - 1) == ' ')
							b--;
						this.config.decorator.openCodeSpan(out);
						Utils.appendCode(out, in, a, b);
						this.config.decorator.closeCodeSpan(out);
					}
				}
				else {
					out.append(in.charAt(pos));
				}
				break;
			case HTML:
				temp.setLength(0);
				b = this.checkHtml(temp, in, pos);
				if (b > 0) {
					out.append(temp);
					pos = b;
				}
				else {
					out.append("&lt;");
				}
				break;
			case ENTITY:
				temp.setLength(0);
				b = checkEntity(temp, in, pos);
				if (b > 0) {
					out.append(temp);
					pos = b;
				}
				else {
					out.append("&amp;");
				}
				break;
			case X_LINK_OPEN:
				temp.setLength(0);
				b = this.recursiveEmitLine(temp, in, pos + 2, end, MarkToken.X_LINK_CLOSE);
				if (b > 0 && this.config.specialLinkEmitter != null) {
					this.config.specialLinkEmitter.emitSpan(out, temp.toString());
					pos = b + 1;
				}
				else {
					out.append(in.charAt(pos));
				}
				break;
			case X_COPY:
				out.append("&copy;");
				pos += 2;
				break;
			case X_REG:
				out.append("&reg;");
				pos += 2;
				break;
			case X_TRADE:
				out.append("&trade;");
				pos += 3;
				break;
			case X_NDASH:
				out.append("&ndash;");
				pos++;
				break;
			case X_MDASH:
				out.append("&mdash;");
				pos += 2;
				break;
			case X_HELLIP:
				out.append("&hellip;");
				pos += 2;
				break;
			case X_LAQUO:
				out.append("&laquo;");
				pos++;
				break;
			case X_RAQUO:
				out.append("&raquo;");
				pos++;
				break;
			case X_RDQUO:
				out.append("&rdquo;");
				break;
			case X_LDQUO:
				out.append("&ldquo;");
				break;
			case ESCAPE:
				pos++;
				//$FALL-THROUGH$
			default:
				out.append(in.charAt(pos));
				break;
			}
			pos++;
		}
		return -1;
	}

	/**
	 * Turns every whitespace character into a space character.
	 * 
	 * @param c Character to check
	 * @return 32 is c was a whitespace, c otherwise
	 */
	private static char whitespaceToSpace(char c) {
		return Character.isWhitespace(c) ? ' ' : c;
	}

	/**
	 * Check if there is any markdown Token.
	 * 
	 * @param in Input String.
	 * @param pos Starting position.
	 * @return The Token.
	 */
	private MarkToken getToken(final String in, final int pos) {
		final char c0 = pos > 0 ? whitespaceToSpace(in.charAt(pos - 1)) : ' ';
		final char c = whitespaceToSpace(in.charAt(pos));
		final char c1 = pos + 1 < in.length() ? whitespaceToSpace(in.charAt(pos + 1)) : ' ';
		final char c2 = pos + 2 < in.length() ? whitespaceToSpace(in.charAt(pos + 2)) : ' ';
		final char c3 = pos + 3 < in.length() ? whitespaceToSpace(in.charAt(pos + 3)) : ' ';

		switch (c) {
		case '*':
			if (c1 == '*') {
				return c0 != ' ' || c2 != ' ' ? MarkToken.STRONG_STAR : MarkToken.EM_STAR;
			}
			return c0 != ' ' || c1 != ' ' ? MarkToken.EM_STAR : MarkToken.NONE;
		case '_':
			if (c1 == '_') {
				return c0 != ' ' || c2 != ' ' ? MarkToken.STRONG_UNDERSCORE : MarkToken.EM_UNDERSCORE;
			}
			if (this.useExtensions) {
				return Character.isLetterOrDigit(c0) && c0 != '_' && Character.isLetterOrDigit(c1) ? MarkToken.NONE : MarkToken.EM_UNDERSCORE;
			}
			return c0 != ' ' || c1 != ' ' ? MarkToken.EM_UNDERSCORE : MarkToken.NONE;
		case '~':
			if (this.useExtensions && c1 == '~') {
				return MarkToken.STRIKE;
			}
			return MarkToken.NONE;
		case '!':
			if (c1 == '[')
				return MarkToken.IMAGE;
			return MarkToken.NONE;
		case '[':
			if (this.useExtensions && c1 == '[')
				return MarkToken.X_LINK_OPEN;
			return MarkToken.LINK;
		case ']':
			if (this.useExtensions && c1 == ']')
				return MarkToken.X_LINK_CLOSE;
			return MarkToken.NONE;
		case '`':
			return c1 == '`' ? MarkToken.CODE_DOUBLE : MarkToken.CODE_SINGLE;
		case '\\':
			switch (c1) {
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
				return MarkToken.ESCAPE;
			default:
				return MarkToken.NONE;
			}
		case '<':
			if (this.useExtensions && c1 == '<')
				return MarkToken.X_LAQUO;
			return MarkToken.HTML;
		case '&':
			return MarkToken.ENTITY;
		default:
			if (this.useExtensions) {
				switch (c) {
				case '-':
					if (c1 == '-')
						return c2 == '-' ? MarkToken.X_MDASH : MarkToken.X_NDASH;
					break;
				case '^':
					return c0 == '^' || c1 == '^' ? MarkToken.NONE : MarkToken.SUPER;
				case '>':
					if (c1 == '>')
						return MarkToken.X_RAQUO;
					break;
				case '.':
					if (c1 == '.' && c2 == '.')
						return MarkToken.X_HELLIP;
					break;
				case '(':
					if (c1 == 'C' && c2 == ')')
						return MarkToken.X_COPY;
					if (c1 == 'R' && c2 == ')')
						return MarkToken.X_REG;
					if (c1 == 'T' & c2 == 'M' & c3 == ')')
						return MarkToken.X_TRADE;
					break;
				case '"':
					if (!Character.isLetterOrDigit(c0) && c1 != ' ')
						return MarkToken.X_LDQUO;
					if (c0 != ' ' && !Character.isLetterOrDigit(c1))
						return MarkToken.X_RDQUO;
					break;
				}
			}
			return MarkToken.NONE;
		}
	}

	/**
	 * Writes a set of markdown lines into the StringBuilder.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param line The lines to write.
	 */
	private void emitMarkedLines(final StringBuilder out, Line line) {
		final StringBuilder in = new StringBuilder();
		while (line != null) {
			if (!line.isEmpty) {
				in.append(line.value.substring(line.leading, line.value.length() - line.trailing));
				if (line.trailing >= 2 && !convertNewline2Br) {
					in.append("<br />");
				}
			}
			if (line.next != null) {
				in.append('\n');
				if (convertNewline2Br) {
					in.append("<br />");
				}
			}
			line = line.next;
		}

		this.recursiveEmitLine(out, in.toString(), 0, in.length(), MarkToken.NONE);
	}

	/**
	 * Writes a set of raw lines into the StringBuilder.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param line The lines to write.
	 */
	private void emitRawLines(final StringBuilder out, Line line) {
		if (this.config.safeMode) {
			final StringBuilder temp = new StringBuilder();
			while (line != null) {
				if (!line.isEmpty) {
					temp.append(line.value);
				}
				temp.append('\n');
				line = line.next;
			}
			final String in = temp.toString();
			for (int pos = 0; pos < in.length(); pos++) {
				if (in.charAt(pos) == '<') {
					temp.setLength(0);
					final int t = Utils.readXML(temp, in, pos, this.config.safeMode);
					if (t != -1) {
						out.append(temp);
						pos = t;
					}
					else {
						out.append(in.charAt(pos));
					}
				}
				else {
					out.append(in.charAt(pos));
				}
			}
		}
		else {
			while (line != null) {
				if (!line.isEmpty) {
					out.append(line.value);
				}
				out.append('\n');
				line = line.next;
			}
		}
	}

	/**
	 * Writes a code block into the StringBuilder.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param line The lines to write.
	 * @param meta Meta information.
	 */
	private void emitCodeLines(final StringBuilder out, Line line, final String meta, final boolean removeIndent) {
		if (this.config.codeBlockEmitter != null) {
			final ArrayList<String> list = new ArrayList<String>();
			while (line != null) {
				if (line.isEmpty)
					list.add("");
				else
					list.add(removeIndent ? line.value.substring(4) : line.value);
				line = line.next;
			}
			this.config.codeBlockEmitter.emitBlock(out, list, meta);
		}
		else {
			while (line != null) {
				if (!line.isEmpty) {
					for (int i = 4; i < line.value.length(); i++) {
						final char c;
						switch (c = line.value.charAt(i)) {
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
				out.append('\n');
				line = line.next;
			}
		}
	}

	/**
	 * interprets a table block into the StringBuilder.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param lines The line to write.
	 * @param meta Meta information.
	 */
	protected void emitTableLines(final StringBuilder out, final Line lines, final String meta) {
		if (lines == null || lines.next == null) {
			return;
		}
		

		String[] aligns = splitTableLine(lines.next);
		int cols = aligns.length;
		for (int i = 0; i < cols; i++) {
			String v = Strings.strip(aligns[i]);
			String a = null;
			if (v.length() > 1) {
				if (v.charAt(v.length() - 1) == ':') {
					a =  (v.charAt(0) == ':' ? "center" : "right");
				}
			}
			aligns[i] = a;
		}
		

		out.append("<table>\n");
		out.append("<thead>\n");
		String[] tds = splitTableLine(lines);
		renderTableLine(out, true, tds, aligns);
		out.append("</thead>\n");

		out.append("<tbody>\n");
		for (Line line = lines.next.next; line != null; line = line.next) {
			if (line.isEmpty) {
				continue;
			}
			
			tds = splitTableLine(line);
			renderTableLine(out, false, tds, aligns);
		}
		out.append("</tbody>\n");

		out.append("</table>\n");
	}

	private void renderTableLine(StringBuilder out, boolean th, String[] tds, String[] aligns) {
		out.append("<tr>");
		for (int i = 0; i < tds.length; i++) {
			String v = Strings.strip(tds[i]);
			String a = i < aligns.length ? aligns[i] : null;

			out.append(th ? "<th" : "<td");
			if (a != null) {
				out.append(" align=\"").append(a).append("\"");
			}
			out.append(">");
			this.recursiveEmitLine(out, v, 0, v.length(), MarkToken.NONE);
			out.append(th ? "</th>" : "</td>");
		}

		int n = tds.length;
		while (n < aligns.length) {
			out.append(th ? "<th></th>" : "<td></td>");
			n++;
		}
		out.append("</tr>\n");
	}

	private String[] splitTableLine(final Line line) {
		int l = line.value.length();
		int s = line.leading;
		while (s < l && line.value.charAt(s) == '|') {
			s++;
		}
		int e = l - line.trailing - 1;
		while (e >= 0 && line.value.charAt(e) == '|') {
			e--;
		}

		return Strings.split(line.value.substring(s, e + 1), '|');
	}
	
	/**
	 * interprets a table block into the StringBuilder.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param lines The line to write.
	 * @param meta Meta information.
	 */
	protected void emitTableBLines(final StringBuilder out, final Line lines, final String meta) {
		boolean thead = false;
		boolean tfoot = false;
		int cols = 0;
		
		Line line = lines;
		while (line != null) {
			if (!line.isEmpty) {
				if (line.isAllChars('-', 4)) {
					thead = true;
					cols = Strings.split(line.value).length;
				}
				else if (line.isAllChars('~', 4)) {
					tfoot = true;
					if (cols <= 0) {
						cols = Strings.split(line.value).length;
					}
				}
			}
			line = line.next;
		}

		out.append("<table>\n");
		out.append(thead ? "<thead>\n" : "<tbody>\n");

		line = lines;
		for (line = lines; line != null; line = line.next) {
			if (line.isEmpty) {
				continue;
			}
			
			if (line.isAllChars('-', 4)) {
				out.append("</thead>\n<tbody>\n");
				continue;
			}
			
			if (line.isAllChars('~', 4)) {
				out.append("</tbody>\n<tfoot>\n");
				continue;
			}

			out.append("<tr>");
			int n = 0;
			int s = line.leading, e = line.value.length() - line.trailing;
			while (s < e) {
				out.append("<td>");
				int d = line.value.indexOf("    ", s);
				if (d > 0) {
					this.recursiveEmitLine(out, line.value, s, d, MarkToken.NONE);
					s = d;
					while (s < e && line.value.charAt(s) == ' ') {
						s++;
					}
				}
				else {
					this.recursiveEmitLine(out, line.value, s, e, MarkToken.NONE);
					s = e;
				}
				out.append("</td>");
				n++;
			}
			while (n < cols) {
				out.append("<td></td>");
				n++;
			}
			out.append("</tr>\n");
		}

		out.append(tfoot ? "</tfoot>\n" : "<tbody>\n");
		out.append("</table>\n");
	}


	/**
	 * interprets a plugin block into the StringBuilder.
	 * 
	 * @param out The StringBuilder to write to.
	 * @param line The line to write.
	 * @param meta Meta information.
	 */
	protected void emitPluginLines(final StringBuilder out, Line line, final String meta) {
		String idPlugin = meta;
		String sparams = null;
		Map<String, String> params = null;
		int iow = meta.indexOf(' ');
		if (iow != -1) {
			idPlugin = meta.substring(0, iow);
			sparams = meta.substring(iow + 1);
			if (sparams != null) {
				params = parsePluginParams(sparams);
			}
		}

		if (params == null) {
			params = new HashMap<String, String>();
		}
		final ArrayList<String> list = new ArrayList<String>();
		while (line != null) {
			if (line.isEmpty)
				list.add("");
			else
				list.add(line.value);
			line = line.next;
		}

		Plugin plugin = plugins.get(idPlugin);
		if (plugin != null) {
			plugin.emit(out, list, params);
		}
	}

	protected Map<String, String> parsePluginParams(String s) {
		Map<String, String> params = new HashMap<String, String>();
		Pattern p = Pattern.compile("(\\w+)=\"*((?<=\")[^\"]+(?=\")|([^\\s]+))\"*");

		Matcher m = p.matcher(s);

		while (m.find()) {
			params.put(m.group(1), m.group(2));
		}

		return params;
	}

}
