package panda.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * utility class for Japanese character
 */
public abstract class JapanChars {
	// z2hMark 全角: 。「」、・゛゜
	private final static Map<Character, Character> z2hMark = Arrays.toMap(
		'\u3002', '\uFF61', // 。 => ｡
		'\u300C', '\uFF62', // 「 => ｢
		'\u300D', '\uFF63', // 」 => ｣
		'\u3001', '\uFF64', // 、 => ､
		'\u30FB', '\uFF65', // ・ => ･
		'\u309B', '\uFF9E', // ゛ => ﾞ
		'\u309C', '\uFF9F'  // ゜ => ﾟ
	);

	// h2zMark 半角: ｡｢｣､･ﾞﾟ
	private final static Map<Character, Character> h2zMark = Collections.toReverse(z2hMark);

	// z2hAyatu 全角: ァィゥェォャュョッー
	private final static Map<Character, Character> z2hAyatu = Arrays.toMap(
		'\u30A1', '\uFF67', // ァ => ｧ
		'\u30A3', '\uFF68', // ィ => ｨ
		'\u30A5', '\uFF69', // ゥ => ｩ
		'\u30A7', '\uFF6A', // ェ => ｪ
		'\u30A9', '\uFF6B', // ォ => ｫ
		'\u30E3', '\uFF6C', // ャ => ｬ
		'\u30E5', '\uFF6D', // ュ => ｭ
		'\u30E7', '\uFF6E', // ョ => ｮ
		'\u30C3', '\uFF6F', // ッ => ｯ
		'\u30FC', '\uFF70'  // ー => ｰ
	);

	// h2zAyatu 半角: ｧｨｩｪｫｬｭｮｯｰ
	private final static Map<Character, Character> h2zAyatu = Collections.toReverse(z2hAyatu);

	// z2hAnamayara 全角: アイエオナニヌネノマミムメモヤユヨラリルレロン
	private final static Map<Character, Character> z2hAnamayara = Arrays.toMap(
		'\u30A2', '\uFF71', // ア => ｱ
		'\u30A4', '\uFF72', // イ => ｲ
		'\u30A8', '\uFF74', // エ => ｴ
		'\u30AA', '\uFF75', // オ => ｵ
		'\u30CA', '\uFF85', // ナ => ﾅ
		'\u30CB', '\uFF86', // ニ => ﾆ
		'\u30CC', '\uFF87', // ヌ => ﾇ
		'\u30CD', '\uFF88', // ネ => ﾈ
		'\u30CE', '\uFF89', // ノ => ﾉ
		'\u30DE', '\uFF8F', // マ => ﾏ
		'\u30DF', '\uFF90', // ミ => ﾐ
		'\u30E0', '\uFF91', // ム => ﾑ
		'\u30E1', '\uFF92', // メ => ﾒ
		'\u30E2', '\uFF93', // モ => ﾓ
		'\u30E4', '\uFF94', // ヤ => ﾔ
		'\u30E6', '\uFF95', // ユ => ﾕ
		'\u30E8', '\uFF96', // ヨ => ﾖ
		'\u30E9', '\uFF97', // ラ => ﾗ
		'\u30EA', '\uFF98', // リ => ﾘ
		'\u30EB', '\uFF99', // ル => ﾙ
		'\u30EC', '\uFF9A', // レ => ﾚ
		'\u30ED', '\uFF9B', // ロ => ﾛ
		'\u30F3', '\uFF9D' // ン => ﾝ
	);

	// h2zAnamayara 半角: ｱｲｴｵﾅﾆﾇﾈﾉﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝ
	private final static Map<Character, Character> h2zAnamayara = Collections.toReverse(z2hAnamayara);

	// z2hKasataha 全角　かさたは　行: カキクケコサシスセソタチツテトハヒフヘホウ
	private final static Map<Character, Character> z2hKasataha = Arrays.toMap(
		'\u30AB', '\uFF76', // カ => ｶ
		'\u30AD', '\uFF77', // キ => ｷ
		'\u30AF', '\uFF78', // ク => ｸ
		'\u30B1', '\uFF79', // ケ => ｹ
		'\u30B3', '\uFF7A', // コ => ｺ
		'\u30B5', '\uFF7B', // サ => ｻ
		'\u30B7', '\uFF7C', // シ => ｼ
		'\u30B9', '\uFF7D', // ス => ｽ
		'\u30BB', '\uFF7E', // セ => ｾ
		'\u30BD', '\uFF7F', // ソ => ｿ
		'\u30BF', '\uFF80', // タ => ﾀ
		'\u30C1', '\uFF81', // チ => ﾁ
		'\u30C4', '\uFF82', // ツ => ﾂ
		'\u30C6', '\uFF83', // テ => ﾃ
		'\u30C8', '\uFF84', // ト => ﾄ
		'\u30CF', '\uFF8A', // ハ => ﾊ
		'\u30D2', '\uFF8B', // ヒ => ﾋ
		'\u30D5', '\uFF8C', // フ => ﾌ
		'\u30D8', '\uFF8D', // ヘ => ﾍ
		'\u30DB', '\uFF8E', // ホ => ﾎ
		'\u30A6', '\uFF73' // ウ => ｳ
	);

	// h2zKasataha 半角　かさたは　行: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
	private final static Map<Character, Character> h2zKasataha = Collections.toReverse(z2hKasataha);

	// z2hWaou 全角　わ　行: ワヲ
	private final static Map<Character, Character> z2hWaou = Arrays.toMap(
		'\u30EF', '\uFF9C', // ワ => ﾜ
		'\u30F2', '\uFF66' // ヲ => ｦ
	);

	// h2zWaou 半角　わ　行: ﾜｦ
	private final static Map<Character, Character> h2zWaou = Collections.toReverse(z2hWaou);

	// z2h 全角
	private final static Map<Character, Character> z2h = merge(z2hMark, z2hAyatu, z2hAnamayara, z2hKasataha, z2hWaou);

	// h2z 半角
	private final static Map<Character, Character> h2z = merge(h2zMark, h2zAyatu, h2zAnamayara, h2zKasataha, h2zWaou);

	// z2hDaku 全角　濁文字: ガギグゲゴザジズゼゾダヂヅデドバビブベボヴヷヸヹヺ
	private final static Map<Character, Character> z2hDaku = Arrays.toMap(
		'\u30AC', '\uFF76', // ガ => ｶ
		'\u30AE', '\uFF77', // ギ => ｷ
		'\u30B0', '\uFF78', // グ => ｸ
		'\u30B2', '\uFF79', // ゲ => ｹ
		'\u30B4', '\uFF7A', // ゴ => ｺ
		'\u30B6', '\uFF7B', // ザ => ｻ
		'\u30B8', '\uFF7C', // ジ => ｼ
		'\u30BA', '\uFF7D', // ズ => ｽ
		'\u30BC', '\uFF7E', // ゼ => ｾ
		'\u30BE', '\uFF7F', // ゾ => ｿ
		'\u30C0', '\uFF80', // ダ => ﾀ
		'\u30C2', '\uFF81', // ヂ => ﾁ
		'\u30C5', '\uFF82', // ヅ => ﾂ
		'\u30C7', '\uFF83', // デ => ﾃ
		'\u30C9', '\uFF84', // ド => ﾄ
		'\u30D0', '\uFF8A', // バ => ﾊ
		'\u30D3', '\uFF8B', // ビ => ﾋ
		'\u30D6', '\uFF8C', // ブ => ﾌ
		'\u30D9', '\uFF8D', // ベ => ﾍ
		'\u30DC', '\uFF8E', // ボ => ﾎ
		'\u30F4', '\uFF73', // ヴ => ｳ
		'\u30F7', '\uFF9C', // ヷ => ﾜ
		'\u30F8', '\uFF72', // ヸ => ｲ
		'\u30F9', '\uFF74', // ヹ => ｴ
		'\u30FA', '\uFF66'  // ヺ => ｦ
	);

	// h2zDaku 半角　濁文字: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳﾜｦ
	private final static Map<Character, Character> h2zDaku = Collections.toReverse(z2hDaku);

	// jaZenkakuHandakuChars 全角　半濁文字: パピプペポ
	private final static Map<Character, Character> z2hHandaku = Arrays.toMap(
		'\u30D1', '\uFF8A', // パ => ﾊ
		'\u30D4', '\uFF8B', // ピ => ﾋ
		'\u30D7', '\uFF8C', // プ => ﾌ
		'\u30DA', '\uFF8D', // ペ => ﾍ
		'\u30DD', '\uFF8E' // ポ => ﾎ
	);

	// h2zHandaku 半角　半濁文字: ﾊﾋﾌﾍﾎ
	private final static Map<Character, Character> h2zHandaku = Collections.toReverse(z2hHandaku);
	
	@SafeVarargs
	private static <K, V> Map<K, V> merge(Map<K,V>...ms) {
		Map<K,V> nm = new HashMap<K,V>();
		for (Map<K,V> m : ms) {
			nm.putAll(m);
		}
		return nm;
	}

	/**
	 * convert the char c to zenkaku
	 */
	public static char toZenkaku(char c) {
		Character r = h2z.get(c);
		if (r != null) {
			return r;
		}
		return MultiByteChars.toFullChar(c);
	}

	/**
	 * convert the char c to hankaku
	 */
	public static char toHankaku(char c) {
		Character r = z2h.get(c);
		if (r != null) {
			return r;
		}
		return MultiByteChars.toHalfChar(c);
	}

	/** 
	 * convert the char c to zenkaku Daku
	 */
	public static char toZenkakuDaku(char c) {
		Character r = h2zDaku.get(c);
		if (r != null) {
			return r;
		}
		
		return c;
	}

	// ToHankakuDakuChar convert the char c to hankaku Daku
	public static char toHankakuDaku(char c) {
		Character r = z2hDaku.get(c);
		if (r != null) {
			return r;
		}
		
		return c;
	}

	// ToZenkakuHandakuChar convert the char c to zenkaku Handaku
	public static char toZenkakuHandaku(char c) {
		Character r = h2zHandaku.get(c);
		if (r != null) {
			return r;
		}
		
		return c;
	}

	// ToHankakuHandakuChar convert the char c to hankaku Handaku
	public static char toHankakuHandaku(char c) {
		Character r = z2hHandaku.get(c);
		if (r != null) {
			return r;
		}
		
		return c;
	}

	/** 
	 * http://charset.7jp.net/jis0201.html
	 * @return true if c is Hankaku Katakana char
	 */
	public static boolean isHankakuKatakana(char c) {
		return c >= '\uFF61' && c <= '\uFF9F';
	}

	/**
	 * https://ja.wikipedia.org/wiki/片仮名_(Unicodeのブロック)
	 * @return true if c is Zenkaku Katakana char
	 */
	public static boolean isZenkakuKatakana(char c) {
		return c >= '\u30A1' && c <= '\u30FC';
	}

	/**
	 * https://ja.wikipedia.org/wiki/平仮名_(Unicodeのブロック)
	 * @return true if c is Zenkaku Hiragana char
	 */
	public static boolean isZenkakuHiragana(char c) {
		return c >= '\u3041' && c <= '\u309F';
	}

	/**
	 * @return true if c is Hankaku char
	 */
	public static boolean isHankaku(char c) {
		if (c < 0x80) {
			return true;
		}

		return isHankakuKatakana(c);
	}

	/**
	 * @return true if c is Zenkaku
	 */
	public static boolean isZenkaku(char c) {
		return !isHankaku(c);
	}
}
