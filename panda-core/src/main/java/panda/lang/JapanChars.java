package panda.lang;


/**
 * utility class for Japanese character
 */
public abstract class JapanChars {
	/** 
	 * 半角数字: 0123456789
	 */
	public static final String HANKAKU_DIGIT = "0123456789";

	/**
	 * 全角数字: ０１２３４５６７８９
	 */
	public static final String ZENKAKU_DIGIT = "０１２３４５６７８９";

	/**
	 * 半角英字: ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
	 */
	public static final String HANKAKU_LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * 全角英字: ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ
	 */
	public static final String ZENKAKU_LETTER = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";
	
	/**
	 * 半角記号: !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
	 */
	public static final String HANKAKU_SYMBOL = " !\"\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

	/**
	 * 全角記号: ！“”＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝～
	 */
	public static final String ZENKAKU_SYMBOL = "　！”“＃＄％＆’（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝～";

	/** 
	 * 半角: !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
	 */
	public static final String HANKAKU_ASCII = HANKAKU_DIGIT + HANKAKU_LETTER + HANKAKU_SYMBOL;

	/** 
	 * 全角: ！“”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［￥］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～
	 */
	public static final String ZENKAKU_ASCII = ZENKAKU_DIGIT + ZENKAKU_LETTER + ZENKAKU_SYMBOL;

	/**
	 * 半角: ｡｢｣､･ﾞﾟ
	 */
	public static final String HANKAKU_MARK = "｡｢｣､･ﾞﾟ";

	/**
	 * 全角: 。「」、・゛゜
	 */
	public static final String ZENKAKU_MARK = "。「」、・゛゜";

	/**
	 * 半角: ｧｨｩｪｫｬｭｮｯｰ
	 */
	public static final String HANKAKU_AYATU = "ｧｨｩｪｫｬｭｮｯｰ";

	/**
	 * 全角: ァィゥェォャュョッー
	 */
	public static final String ZENKAKU_AYATU = "ァィゥェォャュョッー";

	/**
	 * 半角: ｱｲｴｵﾅﾆﾇﾈﾉﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝ
	 */
	public static final String HANKAKU_ANAMAYARA = "ｱｲｴｵﾅﾆﾇﾈﾉﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝ";

	/**
	 * 全角: アイエオナニヌネノマミムメモヤユヨラリルレロン
	 */
	public static final String ZENKAKU_ANAMAYARA = "アイエオナニヌネノマミムメモヤユヨラリルレロン";

	/** 
	 * 半角　かさたは　行: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
	 */
	public static final String HANKAKU_KASATAHA = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ";

	/** 
	 * 全角　かさたは　行: カキクケコサシスセソタチツテトハヒフヘホウ
	 */
	public static final String ZENKAKU_KASATAHA = "カキクケコサシスセソタチツテトハヒフヘホウ";
	
	/** 
	 * 全角　がざだば　行: ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ
	 */
	public static final String ZENKAKU_GAZADABA = "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ";

	/** 
	 * 半角　わ　行: ﾜｦ
	 */
	public static final String HANKAKU_WAOU = "ﾜｦ";

	/** 
	 * 全角　わ　行: ワヲ
	 */
	public static final String ZENKAKU_WAOU = "ワヲ";
	
	/** 
	 * 全角　わ　行: ヷヺ
	 */
	public static final String ZENKAKU_VAVO = "ヷヺ";

	/** 
	 * 半角　濁文字: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳﾜｦ
	 */
	public static final String HANKAKU_DAKU = HANKAKU_KASATAHA + HANKAKU_WAOU;
	
	/**
	 * 全角　濁文字: ガギグゲゴザジズゼゾダヂヅデドバビブベボヴヷヺ
	 */
	public static final String ZENKAKU_DAKU = ZENKAKU_GAZADABA + ZENKAKU_VAVO;

	/** 
	 * 半角　半濁文字: ﾊﾋﾌﾍﾎ
	 */
	public static final String HANKAKU_HANDAKU = "ﾊﾋﾌﾍﾎ";
	
	/**
	 * 全角　半濁文字: パピプペポ
	 */
	public static final String ZENKAKU_HANDAKU = "パピプペポ";

	/** 
	 * 半角
	 */
	public static final String HANKAKU = HANKAKU_ASCII + HANKAKU_MARK + HANKAKU_AYATU + HANKAKU_ANAMAYARA + HANKAKU_KASATAHA + HANKAKU_WAOU;

	/** 
	 * 全角
	 */
	public static final String ZENKAKU = ZENKAKU_ASCII + ZENKAKU_MARK + ZENKAKU_AYATU + ZENKAKU_ANAMAYARA + ZENKAKU_KASATAHA + ZENKAKU_WAOU;

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

	public static void asciiZenkakuToHankaku(StringBuilder sb, char c) {
		int i;

		i = ZENKAKU_ASCII.indexOf(c);
		if (i >= 0) {
			sb.append(HANKAKU_ASCII.charAt(i));
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
	 * isZenkakuHiragana
	 * @param c char
	 * @return true if the char is Zenkaku Hiragana
	 */
	public static boolean isZenkakuHiragana(char c) {
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
