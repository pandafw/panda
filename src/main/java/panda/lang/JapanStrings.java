package panda.lang;


/**
 * utility class for Japanese string
 */
public abstract class JapanStrings {
	/**
	 * isHankakuKatakana
	 * @param value string
	 * @return true if the value is HankakuKatakana string
	 */
	public static boolean isHankakuKatakana(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!JapanChars.isHankakuKatakana(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isHankakuKatakana
	 * @param value string
	 * @return true if the value is HankakuKatakana string
	 */
	public static boolean isHankakuKatakanaSpace(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c != ' ' && !JapanChars.isHankakuKatakana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isHankakuString
	 * @param value string
	 * @return true if the value is Hankaku string
	 */
	public static boolean isHankaku(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!JapanChars.isHankaku(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isZenkakuString
	 * @param value string
	 * @return true if the value is Zenkaku string
	 */
	public static boolean isZenkaku(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!JapanChars.isZenkaku(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isZenkakuKatakanaString
	 * @param value string
	 * @return true if the value is ZenkakuKatakana string
	 */
	public static boolean isZenkakuKatakana(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!JapanChars.isZenkakuKatakana(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isZenkakuKatakanaSpace
	 * @param value string
	 * @return true if the value is Zenkaku Katakana or SPACE string
	 */
	public static boolean isZenkakuKatakanaSpace(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c != '　' && !JapanChars.isZenkakuKatakana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isZenkakuHirakanaString
	 * @param value string
	 * @return true if the value is ZenkakuHirakana string
	 */
	public static boolean isZenkakuHirakana(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!JapanChars.isZenkakuHirakana(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isZenkakuHirakanaSpace
	 * @param value string
	 * @return true if the value is Zenkaku Hirakana or SPACE string
	 */
	public static boolean isZenkakuHirakanaSpace(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c != '　' && !JapanChars.isZenkakuHirakana(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * convert the string from hankaku to Zenkaku
	 * @param value string
	 * @return converted zenkaku string
	 */
	public static String hankakuToZenkaku(String value) {
		if (Strings.isEmpty(value)) {
			return value;
		}

		StringBuilder zenkaku = new StringBuilder(value.length());

		int len = value.length();
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);

			if (i < len - 1) {
				char nc = value.charAt(i + 1);
				if (nc == '\uFF9E') { // ﾞ
					char z = JapanChars.toZenkakuDaku(c);
					if (z != 0) {
						zenkaku.append(z);
						i++;
						continue;
					}
				}
				if (nc == '\uFF9F') { // ﾟ
					char z = JapanChars.toZenkakuHandaku(c);
					if (z != 0) {
						zenkaku.append(z);
						i++;
						continue;
					}
				}
			}
			
			char z = JapanChars.toZenkaku(c);
			if (z != 0) {
				zenkaku.append(z);
				continue;
			}

			zenkaku.append(c);
		}

		return zenkaku.toString();
	}


	/**
	 * convert the string from zenkaku to hankaku
	 * @param value string
	 * @return converted hankaku string
	 */
	public static String zenkakuToHankaku(String value) {
		if (Strings.isEmpty(value)) {
			return value;
		}

		StringBuilder hankaku = new StringBuilder(value.length());
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (JapanChars.isHankaku(c)) {
				hankaku.append(c);
				continue;
			}

			JapanChars.zenkakuToHankaku(hankaku, c);
		}

		return hankaku.toString();
	}
	
	/**
	 * convert the string from hankaku to Zenkaku
	 * @param value string
	 * @return converted zenkaku string
	 */
	public static String stripHankakuToZenkaku(String value) {
		return hankakuToZenkaku(Strings.strip(value));
	}
	
	/**
	 * convert the string from hankaku to Zenkaku
	 * @param value string
	 * @return converted zenkaku string
	 */
	public static String stripHankakuToZenkakuEmpty(String value) {
		return hankakuToZenkaku(Strings.stripToEmpty(value));
	}
	
	/**
	 * convert the string from hankaku to Zenkaku
	 * @param value string
	 * @return converted zenkaku string
	 */
	public static String stripHankakuToZenkakuNull(String value) {
		return hankakuToZenkaku(Strings.stripToNull(value));
	}
	
	/**
	 * convert the string from zenkaku to hankaku
	 * @param value string
	 * @return converted hankaku string
	 */
	public static String stripZenkakuToHankaku(String value) {
		return zenkakuToHankaku(Strings.strip(value));
	}
	
	/**
	 * convert the string from zenkaku to hankaku
	 * @param value string
	 * @return converted hankaku string
	 */
	public static String stripZenkakuToHankakuEmtpy(String value) {
		return zenkakuToHankaku(Strings.stripToEmpty(value));
	}
	
	/**
	 * convert the string from zenkaku to hankaku
	 * @param value string
	 * @return converted hankaku string
	 */
	public static String stripZenkakuToHankakuNull(String value) {
		return zenkakuToHankaku(Strings.stripToEmpty(value));
	}

}
