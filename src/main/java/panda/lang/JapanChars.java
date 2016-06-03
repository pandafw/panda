package panda.lang;


/**
 * utility class for Japanese character
 */
public abstract class JapanChars {
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

	public static char toZenkaku(char c) {
		int i = HANKAKU.indexOf(c);
		return i >= 0 ? ZENKAKU.charAt(i) : 0;
	}

	public static char toZenkakuDaku(char c) {
		int i = HANKAKU_DAKU.indexOf(c);
		return i >= 0 ? ZENKAKU_DAKU.charAt(i) : 0;
	}

	public static char toZenkakuHandaku(char c) {
		int i = HANKAKU_HANDAKU.indexOf(c);
		return i >= 0 ? ZENKAKU_HANDAKU.charAt(i) : 0;
	}

	public static void zenkakuToHankaku(StringBuilder sb, char c) {
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
	//------------------------------------------------------------------------------

	/**
	 * isHankakuKatakanaChar
	 * @param c char
	 * @return true if the char is Hankaku Katakana
	 */
	public static boolean isHankakuKatakana(char c) {
		return c >= '\uFF61' && c <= '\uFF9F';
	}

	/**
	 * isZenkakuKatakanaChar
	 * @param c char
	 * @return true if the char is Zenkaku Katakana
	 */
	public static boolean isZenkakuKatakana(char c) {
		return c >= '\u30A1' && c <= '\u30F6';
	}

	/**
	 * isZenkakuHirakana
	 * @param c char
	 * @return true if the char is Zenkaku Hirakana
	 */
	public static boolean isZenkakuHirakana(char c) {
		return c >= '\u3041' && c <= '\u309F';
	}

	/**
	 * isHankakuChar
	 * @param c char
	 * @return true if the char is Hankaku
	 */
	public static boolean isHankaku(char c) {
		if (c <= '\u007F') {
			return true;
		}

		return isHankakuKatakana(c);
	}

	/**
	 * isZenkakuChar
	 * @param c char
	 * @return true if the char is Zenkaku
	 */
	public static boolean isZenkaku(char c) {
		return !isHankaku(c);
	}
}
