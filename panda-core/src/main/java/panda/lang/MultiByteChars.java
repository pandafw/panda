package panda.lang;

import java.util.Map;

/**
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 */
public abstract class MultiByteChars {
	// s2m single-byte char to multi-byte char
	private final static Map<Character, Character> s2m = Arrays.toMap(
		// space
		'\u0020', '\u3000', //   =>
		// Symbol
		'\u0021', '\uFF01', // ! => ！
		'\u0022', '\uFF02', // " => ＂
		'\u0023', '\uFF03', // # => ＃
		'\u0024', '\uFF04', // $ => ＄
		'\u0025', '\uFF05', // % => ％
		'\u0026', '\uFF06', // & => ＆
		'\'', '\uFF07', // ' => ＇
		'\u0028', '\uFF08', // ( => （
		'\u0029', '\uFF09', // ) => ）
		'\u002A', '\uFF0A', // * => ＊
		'\u002B', '\uFF0B', // + => ＋
		'\u002C', '\uFF0C', // , => ，
		'\u002D', '\uFF0D', // - => －
		'\u002E', '\uFF0E', // . => ．
		'\u002F', '\uFF0F', // / => ／
		// Number
		'\u0030', '\uFF10', // 0 => ０
		'\u0031', '\uFF11', // 1 => １
		'\u0032', '\uFF12', // 2 => ２
		'\u0033', '\uFF13', // 3 => ３
		'\u0034', '\uFF14', // 4 => ４
		'\u0035', '\uFF15', // 5 => ５
		'\u0036', '\uFF16', // 6 => ６
		'\u0037', '\uFF17', // 7 => ７
		'\u0038', '\uFF18', // 8 => ８
		'\u0039', '\uFF19', // 9 => ９
		// Symbol
		'\u003A', '\uFF1A', // : => ：
		'\u003B', '\uFF1B', // ; => ；
		'\u003C', '\uFF1C', // < => ＜
		'\u003D', '\uFF1D', // = => ＝
		'\u003E', '\uFF1E', // > => ＞
		'\u003F', '\uFF1F', // ? => ？
		'\u0040', '\uFF20', // @ => ＠
		// Letter
		'\u0041', '\uFF21', // A => Ａ
		'\u0042', '\uFF22', // B => Ｂ
		'\u0043', '\uFF23', // C => Ｃ
		'\u0044', '\uFF24', // D => Ｄ
		'\u0045', '\uFF25', // E => Ｅ
		'\u0046', '\uFF26', // F => Ｆ
		'\u0047', '\uFF27', // G => Ｇ
		'\u0048', '\uFF28', // H => Ｈ
		'\u0049', '\uFF29', // I => Ｉ
		'\u004A', '\uFF2A', // J => Ｊ
		'\u004B', '\uFF2B', // K => Ｋ
		'\u004C', '\uFF2C', // L => Ｌ
		'\u004D', '\uFF2D', // M => Ｍ
		'\u004E', '\uFF2E', // N => Ｎ
		'\u004F', '\uFF2F', // O => Ｏ
		'\u0050', '\uFF30', // P => Ｐ
		'\u0051', '\uFF31', // Q => Ｑ
		'\u0052', '\uFF32', // R => Ｒ
		'\u0053', '\uFF33', // S => Ｓ
		'\u0054', '\uFF34', // T => Ｔ
		'\u0055', '\uFF35', // U => Ｕ
		'\u0056', '\uFF36', // V => Ｖ
		'\u0057', '\uFF37', // W => Ｗ
		'\u0058', '\uFF38', // X => Ｘ
		'\u0059', '\uFF39', // Y => Ｙ
		'\u005A', '\uFF3A', // Z => Ｚ
		// Symbol
		'\u005B', '\uFF3B', // [ => ［
		'\\', '\uFF3C', // \ => ＼
		'\u005D', '\uFF3D', // ] => ］
		'\u005E', '\uFF3E', // ^ => ＾
		'\u005F', '\uFF3F', // _ => ＿
		'\u0060', '\uFF40', // ` => ｀
		// Letter
		'\u0061', '\uFF41', // a => ａ
		'\u0062', '\uFF42', // b => ｂ
		'\u0063', '\uFF43', // c => ｃ
		'\u0064', '\uFF44', // d => ｄ
		'\u0065', '\uFF45', // e => ｅ
		'\u0066', '\uFF46', // f => ｆ
		'\u0067', '\uFF47', // g => ｇ
		'\u0068', '\uFF48', // h => ｈ
		'\u0069', '\uFF49', // i => ｉ
		'\u006A', '\uFF4A', // j => ｊ
		'\u006B', '\uFF4B', // k => ｋ
		'\u006C', '\uFF4C', // l => ｌ
		'\u006D', '\uFF4D', // m => ｍ
		'\u006E', '\uFF4E', // n => ｎ
		'\u006F', '\uFF4F', // o => ｏ
		'\u0070', '\uFF50', // p => ｐ
		'\u0071', '\uFF51', // q => ｑ
		'\u0072', '\uFF52', // r => ｒ
		'\u0073', '\uFF53', // s => ｓ
		'\u0074', '\uFF54', // t => ｔ
		'\u0075', '\uFF55', // u => ｕ
		'\u0076', '\uFF56', // v => ｖ
		'\u0077', '\uFF57', // w => ｗ
		'\u0078', '\uFF58', // x => ｘ
		'\u0079', '\uFF59', // y => ｙ
		'\u007A', '\uFF5A', // z => ｚ
		// Symbol
		'\u007B', '\uFF5B', // { => ｛
		'\u007C', '\uFF5C', // | => ｜
		'\u007D', '\uFF5D', // } => ｝
		'\u007E', '\uFF5E' // ~ => ～
	);

	// m2s multi-byte char to single-byte char
	private final static Map<Character, Character> m2s = Collections.toReverse(s2m);
	
	static {
		m2s.putAll(Arrays.toMap(
			// Symbol
			'\uFFE5', '\\', // ￥ => \
			'\uFF61', '\u002E', // ｡ => .
			'\uFF62', '\u005B', // ｢ => [
			'\uFF63', '\u005D', // ｣ => ]
			'\uFF64', '\u002C', // ､ => ,
			// Symbol
			'\u2010', '\u002D', // ‐ => -
			'\u2019', '\'', // ’ => '
			'\u201C', '\u0022', // “ => "
			'\u201D', '\u0022', // ” => "
			'\u2033', '\u0022', // ″ => "
			'\u2212', '\u002D', // − => -
			'\u301C', '\u007E', // 〜 => ~
			'\u30FC', '\u002D' // ー => -
		));
	}
	/**
	 * convert multi ascii char c to single ascii char
	 */
	public static char toHalfChar(char c) {
		if (c < 0x80) {
			return c;
		}

		Character s = m2s.get(c);
		if (s != null) {
			return s;
		}
		return c;
	}

	/**
	 * convert single ascii char c to multi ascii char
	 */
	public static char toFullChar(char c) {
		if (c < 0x80) {
			Character m = s2m.get(c);
			if (m != null) {
				return m;
			}
		}
		return c;
	}
}
