package panda.lang;


/**
 * utility class for Asia string
 * @author yf.frank.wang@gmail.com
 */
public abstract class AsiaStrings {

	/**
	 * isHankakuKatakanaString
	 * @param value string
	 * @return true if the value is HankakuKatakana string
	 */
	public static boolean isHankakuKatakanaString(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!AsiaChars.isHankakuKatakanaChar(value.charAt(i))) {
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
	public static boolean isHankakuString(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!AsiaChars.isHankakuChar(value.charAt(i))) {
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
	public static boolean isZenkakuString(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!AsiaChars.isZenkakuChar(value.charAt(i))) {
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
	public static boolean isZenkakuKatakanaString(String value) {
		if (Strings.isEmpty(value)) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!AsiaChars.isZenkakuKatakanaChar(value.charAt(i))) {
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

		Character zm = null;

		int len = value.length();
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);

			zm = getZenkakuChar(c);
			if (zm != null) {
				zenkaku.append(zm);
				continue;
			}
			if (i == len - 1) {
				zm = getZenkakuKasatahaChar(c);
				if (zm != null) {
					zenkaku.append(zm);
					continue;
				}
				if (c == '\uFF9C') {
					zenkaku.append("\u30EF");
					continue;
				}
				if (c == '\uFF66') {
					zenkaku.append("\u30F2");
				} 
				else {
					zenkaku.append(c);
				}
				continue;
			}

			char nc = value.charAt(i + 1);
			if (nc == '\uFF9E') {
				zm = getZenkakuDakuChar(c);
				if (zm != null) {
					zenkaku.append(zm);
					i++;
					continue;
				}
				if (c == '\uFF9C') {
					zenkaku.append("\u30F7");
					i++;
					continue;
				}
				if (c == '\uFF66') {
					zenkaku.append("\u30FA");
					i++;
				}
				else {
					zenkaku.append(c);
					zenkaku.append("\u309B");
					i++;
				}
				continue;
			}
			if (nc == '\uFF9F') {
				zm = getZenkakuHandakuChar(c);
				if (zm != null) {
					zenkaku.append(zm);
					i++;
				} 
				else {
					zm = getZenkakuKasatahaChar(c);
					zenkaku.append(zm);
					zenkaku.append("\u309C");
					i++;
				}
				continue;
			}

			zm = getZenkakuKasatahaChar(c);
			if (zm != null) {
				zenkaku.append(zm);
				continue;
			}
			if (c == '\uFF9C') {
				zenkaku.append("\u30EF");
				continue;
			}
			if (c == '\uFF66') {
				zenkaku.append("\u30F2");
			} 
			else {
				zenkaku.append(c);
			}
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
			if (AsiaChars.isHankakuChar(c)) {
				hankaku.append(c);
			}
			else {
				String zm = getHankakuMoji(c);
				hankaku.append(zm != null ? zm : c);
			}
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
	
	////////////////////////////////////////////////////////
	// protected
	///////////////////////////////////////////////////////

	/** 半角 */
	protected static final String HANKAKU = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\uFF61\uFF62\uFF63\uFF64\uFF65\uFF67\uFF68\uFF69\uFF6A\uFF6B\uFF6C\uFF6D\uFF6E\uFF6F\uFF70\uFF71\uFF72\uFF74\uFF75\uFF85\uFF86\uFF87\uFF88\uFF89\uFF8F\uFF90\uFF91\uFF92\uFF93\uFF94\uFF95\uFF96\uFF97\uFF98\uFF99\uFF9A\uFF9B\uFF9D\uFF9E\uFF9F ";
	/** 全角 */
	protected static final String ZENKAKU = "\uFF01\u201D\uFF03\uFF04\uFF05\uFF06\u2019\uFF08\uFF09\uFF0A\uFF0B\uFF0C\uFF0D\uFF0E\uFF0F\uFF10\uFF11\uFF12\uFF13\uFF14\uFF15\uFF16\uFF17\uFF18\uFF19\uFF1A\uFF1B\uFF1C\uFF1D\uFF1E\uFF1F\uFF20\uFF21\uFF22\uFF23\uFF24\uFF25\uFF26\uFF27\uFF28\uFF29\uFF2A\uFF2B\uFF2C\uFF2D\uFF2E\uFF2F\uFF30\uFF31\uFF32\uFF33\uFF34\uFF35\uFF36\uFF37\uFF38\uFF39\uFF3A\uFF3B\uFFE5\uFF3D\uFF3E\uFF3F\uFF40\uFF41\uFF42\uFF43\uFF44\uFF45\uFF46\uFF47\uFF48\uFF49\uFF4A\uFF4B\uFF4C\uFF4D\uFF4E\uFF4F\uFF50\uFF51\uFF52\uFF53\uFF54\uFF55\uFF56\uFF57\uFF58\uFF59\uFF5A\uFF5B\uFF5C\uFF5D\uFFE3\u3002\u300C\u300D\u3001\u30FB\u30A1\u30A3\u30A5\u30A7\u30A9\u30E3\u30E5\u30E7\u30C3\u30FC\u30A2\u30A4\u30A8\u30AA\u30CA\u30CB\u30CC\u30CD\u30CE\u30DE\u30DF\u30E0\u30E1\u30E2\u30E4\u30E6\u30E8\u30E9\u30EA\u30EB\u30EC\u30ED\u30F3\u309B\u309C\u3000";
	/** 半角　かさたは　行 */
	protected static final String HANKAKU_KASATAHA = "\uFF76\uFF77\uFF78\uFF79\uFF7A\uFF7B\uFF7C\uFF7D\uFF7E\uFF7F\uFF80\uFF81\uFF82\uFF83\uFF84\uFF8A\uFF8B\uFF8C\uFF8D\uFF8E\uFF73";
	/** 全角　かさたは　行 */
	protected static final String ZENKAKU_KASATAHA = "\u30AB\u30AD\u30AF\u30B1\u30B3\u30B5\u30B7\u30B9\u30BB\u30BD\u30BF\u30C1\u30C4\u30C6\u30C8\u30CF\u30D2\u30D5\u30D8\u30DB\u30A6";
	/** 全角　がざだば　行 */
	protected static final String ZENKAKU_GAZADABA = "\u30AC\u30AE\u30B0\u30B2\u30B4\u30B6\u30B8\u30BA\u30BC\u30BE\u30C0\u30C2\u30C5\u30C7\u30C9\u30D0\u30D3\u30D6\u30D9\u30DC\u30F4";
	/** 半角　半濁文字 */
	protected static final String HANKAKU_HANDAKU = "\uFF8A\uFF8B\uFF8C\uFF8D\uFF8E";
	/** 全角　半濁文字 */
	protected static final String ZENKAKU_HANDAKU = "\u30D1\u30D4\u30D7\u30DA\u30DD";

	protected static Character getZenkakuChar(char c) {
		int i = HANKAKU.indexOf(c);
		return i >= 0 ? ZENKAKU.charAt(i) : null;
	}

	protected static Character getZenkakuDakuChar(char c) {
		int i = HANKAKU_KASATAHA.indexOf(c);
		return i >= 0 ? ZENKAKU_GAZADABA.charAt(i) : null;
	}

	protected static Character getZenkakuHandakuChar(char c) {
		int i = HANKAKU_HANDAKU.indexOf(c);
		return i >= 0 ? ZENKAKU_HANDAKU.charAt(i) : null;
	}

	protected static Character getZenkakuKasatahaChar(char c) {
		int i = HANKAKU_KASATAHA.indexOf(c);
		return i >= 0 ? ZENKAKU_KASATAHA.charAt(i) : null;
	}

	protected static String getHankakuMoji(char c) {
		int i;

		i = ZENKAKU.indexOf(c);
		if (i >= 0) {
			return String.valueOf(HANKAKU.charAt(i));
		}

		i = ZENKAKU_KASATAHA.indexOf(c);
		if (i >= 0) {
			return String.valueOf(HANKAKU_KASATAHA.charAt(i));
		}

		i = ZENKAKU_GAZADABA.indexOf(c);
		if (i >= 0) {
			char hc = HANKAKU_KASATAHA.charAt(i);
			return String.valueOf(hc) + "\uFF9E";
		}

		i = ZENKAKU_HANDAKU.indexOf(c);
		if (i >= 0) {
			char hc = HANKAKU_HANDAKU.charAt(i);
			return String.valueOf(hc) + "\uFF9F";
		}

		switch (c) {
		case '\u30EF':
			return "\uFF9C";
		case '\u30F2':
			return "\uFF66";
		case '\u30F7':
			return "\uFF9C\uFF9E";
		case '\u30FA':
			return "\uFF66\uFF9E";
		default:
			return null;
		}
	}
}
