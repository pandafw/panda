package panda.lang;


/**
 * utility class for Japanese string
 */
public abstract class JapanStrings {
	/**
	 * @return true if s is HankakuKatakana string
	 */
	public static boolean isHankakuKatakana(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			if (!JapanChars.isHankakuKatakana(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is HankakuKatakana or space string
	 */
	public static boolean isHankakuKatakanaSpace(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != ' ' && !JapanChars.isHankakuKatakana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is Hankaku string
	 */
	public static boolean isHankaku(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!JapanChars.isHankaku(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is Zenkaku string
	 */
	public static boolean isZenkaku(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!JapanChars.isZenkaku(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is ZenkakuKatakana string
	 */
	public static boolean isZenkakuKatakana(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!JapanChars.isZenkakuKatakana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is Zenkaku Katakana or Space string
	 */
	public static boolean isZenkakuKatakanaSpace(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '　' && !JapanChars.isZenkakuKatakana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is Zenkaku Katakana or Space (Zenkaku or Hankaku) string
	 */
	public static boolean isZenkakuKatakanaSpaces(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != ' ' && c != '　' && !JapanChars.isZenkakuKatakana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is ZenkakuHiragana string
	 */
	public static boolean isZenkakuHiragana(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!JapanChars.isZenkakuHiragana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is Zenkaku Hiragana or Space string
	 */
	public static boolean isZenkakuHiraganaSpace(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '　' && !JapanChars.isZenkakuHiragana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if s is Zenkaku Hiragana or Space(Zenkaku or Hankaku) string
	 */
	public static boolean isZenkakuHiraganaSpaces(String s) {
		if (Strings.isEmpty(s)) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != ' ' && c != '　' && !JapanChars.isZenkakuHiragana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * convert the string from hankaku to Zenkaku
	 * @param s string
	 * @return converted zenkaku string
	 */
	public static String toZenkaku(String s) {
		if (Strings.isEmpty(s)) {
			return s;
		}

		int len = s.length();

		StringBuilder zs = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);

			if (i < len - 1) {
				char nc = s.charAt(i + 1);
				if (nc == '\uFF9E') { // ﾞ
					char z = JapanChars.toZenkakuDaku(c);
					if (z != c) {
						zs.append(z);
						i++;
						continue;
					}
				}
				if (nc == '\uFF9F') { // ﾟ
					char z = JapanChars.toZenkakuHandaku(c);
					if (z != c) {
						zs.append(z);
						i++;
						continue;
					}
				}
			}
			
			c = JapanChars.toZenkaku(c);
			zs.append(c);
		}

		return zs.toString();
	}

	/**
	 * convert the string from zenkaku to hankaku
	 * @param s string
	 * @return converted hankaku string
	 */
	public static String toHankaku(String s) {
		if (Strings.isEmpty(s)) {
			return s;
		}

		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (JapanChars.isHankaku(c)) {
				sb.append(c);
				continue;
			}

			char r = JapanChars.toHankakuDaku(c);
			if (r != c) {
				sb.append(r);
				sb.append('\uFF9E'); // ﾞ
				continue;
			}

			r = JapanChars.toHankakuHandaku(c);
			if (r != c) {
				sb.append(r);
				sb.append('\uFF9F'); // ﾟ
				continue;
			}

			r = JapanChars.toHankaku(c);
			sb.append(r);
		}

		return sb.toString();
	}

	/**
	 * convert the string from hankaku to Zenkaku
	 * @param s string
	 * @return converted zenkaku string
	 */
	public static String stripHankakuToZenkaku(String s) {
		return toZenkaku(Strings.strip(s));
	}
	
	/**
	 * convert the string from hankaku to Zenkaku
	 * @param s string
	 * @return converted zenkaku string
	 */
	public static String stripHankakuToZenkakuEmpty(String s) {
		return toZenkaku(Strings.stripToEmpty(s));
	}
	
	/**
	 * convert the string from hankaku to Zenkaku
	 * @param s string
	 * @return converted zenkaku string
	 */
	public static String stripHankakuToZenkakuNull(String s) {
		return toZenkaku(Strings.stripToNull(s));
	}
	
	/**
	 * convert the string from zenkaku to hankaku
	 * @param s string
	 * @return converted hankaku string
	 */
	public static String stripZenkakuToHankaku(String s) {
		return toHankaku(Strings.strip(s));
	}
	
	/**
	 * convert the string from zenkaku to hankaku
	 * @param s string
	 * @return converted hankaku string
	 */
	public static String stripZenkakuToHankakuEmtpy(String s) {
		return toHankaku(Strings.stripToEmpty(s));
	}
	
	/**
	 * convert the string from zenkaku to hankaku
	 * @param s string
	 * @return converted hankaku string
	 */
	public static String stripZenkakuToHankakuNull(String s) {
		return toHankaku(Strings.stripToEmpty(s));
	}
}
