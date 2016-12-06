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
			if (Strings.isEmpty(body)) {
				return "\r\n";
			}

			return body;
		}
	},

	/**
	 * The 'relaxed' canonicalization algorithm.
	 */
	RELAXED {
		public String canonicalizeHeader(String name, String value) {
			name = name.trim().toLowerCase();
			value = value.replaceAll("\\s+", " ").trim();
			return name + ": " + value;
		}

		public String canonicalizeBody(String body) {
			if (Strings.isEmpty(body)) {
				return "\r\n";
			}

			body = body.replaceAll("[ \\t\\x0B\\f]+", " ");
			body = body.replaceAll(" \r\n", "\r\n");

			// Remove trailing empty lines ...
			body = Strings.stripEnd(body, "\r\n");
			if (Strings.isEmpty(body)) {
				return "\r\n";
			}

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
