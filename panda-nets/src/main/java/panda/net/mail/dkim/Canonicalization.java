package panda.net.mail.dkim;

import panda.lang.Strings;

/**
 * Provides 'simple' and 'relaxed' canonicalization according to RFC 4871.
 */
public enum Canonicalization {
	/**
	 * The 'simple' canonicalization algorithm.
	 */
	SIMPLE {
		public String canonicalizeHeader(String name, String value) {
			return name + ": " + value;
		}

		public String canonicalizeBody(String body) {
			// Remove trailing empty lines ...
			body = Strings.stripEnd(body, "\r\n");
			return body + "\r\n";
		}
	},

	/**
	 * The 'relaxed' canonicalization algorithm.
	 */
	RELAXED {
		private String canonicalize(String s, boolean body) {
			if (Strings.isEmpty(s)) {
				return s;
			}
			
			StringBuilder sb = new StringBuilder();
			
			// Reduces all sequences of WSP within a line to a single SP character
			boolean sp = false;
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c == ' ' || c == '\t' || c == '\u000B' || c == '\u000C') {
					sp = true;
				}
				else if (sp) {
					sp = false;
					sb.append(' ').append(c);
				}
				else {
					sb.append(c);
				}
			}
			
			// Remove trailing empty lines
			int e = sb.length() - 1;
			for (; e >= 0; e--) {
				char c = sb.charAt(e);
				if (c != '\r' && c != '\n') {
					break;
				}
			}

			sb.setLength(e + 1);
			if (body) {
				sb.append("\r\n");
			}
			return sb.toString();
		}
		
		public String canonicalizeHeader(String name, String value) {
			name = name.trim().toLowerCase();
			value = canonicalize(value, false).trim();
			return name + ": " + value;
		}

		public String canonicalizeBody(String body) {
			if (Strings.isEmpty(body)) {
				return "\r\n";
			}

			body = canonicalize(body, true);
			return body;
		}
	};

	/**
	 * Returns a string representation of the canonicalization algorithm.
	 * 
	 * @return The string representation of the canonicalization algorithm.
	 */
	public final String getType() {
		return name().toLowerCase();
	}

	/**
	 * Performs header canonicalization.
	 * 
	 * @param name
	 *            The name of the header.
	 * @param value
	 *            The value of the header.
	 * @return The canonicalized header.
	 */
	public abstract String canonicalizeHeader(String name, String value);

	/**
	 * Performs body canonicalization.
	 * 
	 * @param body
	 *            The content of the body.
	 * @return The canonicalized body.
	 */
	public abstract String canonicalizeBody(String body);
}
