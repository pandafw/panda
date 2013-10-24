package panda.lang;


/**
 * utility class for Asia string
 * @author yf.frank.wang@gmail.com
 */
public abstract class AsiaStrings {
	/** 
	 * 半角: !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
	 */
	protected static final String HANKAKU_ASCII = " !\"\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

	/** 
	 * 全角: ！“”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［￥］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～
	 */
	protected static final String ZENKAKU_ASCII = "　！”“＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［￥］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～";

	/** 
	 * 半角: ｡｢｣､･ｧｨｩｪｫｬｭｮｯｰｱｲｴｵﾅﾆﾇﾈﾉﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝﾞﾟ  
	 */
	protected static final String HANKAKU_NORMAL = "｡｢｣､･ｧｨｩｪｫｬｭｮｯｰｱｲｴｵﾅﾆﾇﾈﾉﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝﾞﾟ";

	/** 
	 * 全角: 。「」、・ァィゥェォャュョッーアイエオナニヌネノマミムメモヤユヨラリルレロン゛゜　
	 */
	protected static final String ZENKAKU_NORMAL = "。「」、・ァィゥェォャュョッーアイエオナニヌネノマミムメモヤユヨラリルレロン゛゜";

	/** 
	 * 半角　かさたは　行: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
	 */
	protected static final String HANKAKU_KASATAHA = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ";

	/** 
	 * 全角　かさたは　行: カキクケコサシスセソタチツテトハヒフヘホウ
	 */
	protected static final String ZENKAKU_KASATAHA = "カキクケコサシスセソタチツテトハヒフヘホウ";
	
	/** 
	 * 全角　がざだば　行: ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ
	 */
	protected static final String ZENKAKU_GAZADABA = "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ";

	/** 
	 * 半角　わ　行: ﾜｦ
	 */
	protected static final String HANKAKU_WAOU = "ﾜｦ";

	/** 
	 * 全角　わ　行: ワヲ
	 */
	protected static final String ZENKAKU_WAOU = "ワヲ";
	
	/** 
	 * 全角　わ　行: ヷヺ
	 */
	protected static final String ZENKAKU_VAVO = "ヷヺ";

	/** 
	 * 半角　濁文字: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳﾜｦ
	 */
	protected static final String HANKAKU_DAKU = HANKAKU_KASATAHA + HANKAKU_WAOU;
	
	/**
	 * 全角　濁文字: ガギグゲゴザジズゼゾダヂヅデドバビブベボヴヷヺ
	 */
	protected static final String ZENKAKU_DAKU = ZENKAKU_GAZADABA + ZENKAKU_VAVO;

	/** 
	 * 半角　半濁文字: ﾊﾋﾌﾍﾎ
	 */
	protected static final String HANKAKU_HANDAKU = "ﾊﾋﾌﾍﾎ";
	
	/**
	 * 全角　半濁文字: パピプペポ
	 */
	protected static final String ZENKAKU_HANDAKU = "パピプペポ";

	/** 
	 * 半角
	 */
	protected static final String HANKAKU = HANKAKU_ASCII + HANKAKU_NORMAL + HANKAKU_KASATAHA + HANKAKU_WAOU;

	/** 
	 * 全角
	 */
	protected static final String ZENKAKU = ZENKAKU_ASCII + ZENKAKU_NORMAL + ZENKAKU_KASATAHA + ZENKAKU_WAOU;

	protected static char getZenkakuChar(char c) {
		int i = HANKAKU.indexOf(c);
		return i >= 0 ? ZENKAKU.charAt(i) : 0;
	}

	protected static char getZenkakuDakuChar(char c) {
		int i = HANKAKU_DAKU.indexOf(c);
		return i >= 0 ? ZENKAKU_DAKU.charAt(i) : 0;
	}

	protected static char getZenkakuHandakuChar(char c) {
		int i = HANKAKU_HANDAKU.indexOf(c);
		return i >= 0 ? ZENKAKU_HANDAKU.charAt(i) : 0;
	}

	//------------------------------------------------------------------------------
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

		int len = value.length();
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);

			if (i < len - 1) {
				char nc = value.charAt(i + 1);
				if (nc == '\uFF9E') { // ﾞ
					char z = getZenkakuDakuChar(c);
					if (z != 0) {
						zenkaku.append(z);
						i++;
						continue;
					}
				}
				if (nc == '\uFF9F') { // ﾟ
					char z = getZenkakuHandakuChar(c);
					if (z != 0) {
						zenkaku.append(z);
						i++;
						continue;
					}
				}
			}
			
			char z = getZenkakuChar(c);
			if (z != 0) {
				zenkaku.append(z);
				continue;
			}

			zenkaku.append(c);
		}

		return zenkaku.toString();
	}

	protected static void zenkakuToHankaku(StringBuilder sb, char c) {
		int i;

		i = ZENKAKU_HANDAKU.indexOf(c);
		if (i >= 0) {
			char hc = HANKAKU_HANDAKU.charAt(i);
			sb.append(hc).append('\uFF9F'); // ﾟ
			return;
		}
		
		i = ZENKAKU_DAKU.indexOf(c);
		if (i >= 0) {
			char hc = HANKAKU_DAKU.charAt(i);
			sb.append(hc).append('\uFF9E'); // ﾞ
			return;
		}

		i = ZENKAKU.indexOf(c);
		if (i >= 0) {
			sb.append(HANKAKU.charAt(i));
			return;
		}

		sb.append(c);
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
				continue;
			}

			zenkakuToHankaku(hankaku, c);
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
