package panda.lang;

/**
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 */
public abstract class MultiByteStrings {
	/**
	 * convert the string from multi ascii to single ascii
	 */
	public static String toHalfWidth(String s) {
		if (Strings.isEmpty(s)) {
			return s;
		}

		StringBuilder sb = null;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			char r = MultiByteChars.toHalfChar(c);
			if (r != c) {
				if (sb == null) {
					sb = new StringBuilder(s.length());
					sb.append(s.substring(0, i));
				}
				sb.append(r);
				continue;
			}

			if (sb != null) {
				sb.append(c);
			}
		}

		if (sb != null) {
			return sb.toString();
		}

		return s;
	}

	/**
	 * convert the string from single ascii to multi ascii
	 */
	public static String toFullWidth(String s) {
		if (Strings.isEmpty(s)) {
			return s;
		}

		StringBuilder sb = null;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			char r = MultiByteChars.toFullChar(c);
			if (r != c) {
				if (sb == null) {
					sb = new StringBuilder(s.length());
					sb.append(s.substring(0, i));
				}
				sb.append(r);
				continue;
			}

			if (sb != null) {
				sb.append(c);
			}
		}

		if (sb != null) {
			return sb.toString();
		}

		return s;
	}
}
