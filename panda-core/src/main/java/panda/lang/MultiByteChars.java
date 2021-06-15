package panda.lang;

import java.util.Map;

/**
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 */
public abstract class MultiByteChars {
	// m2s multi-byte char to single-byte char
	private final static Map<Character, Character> m2s = Arrays.toMap(
		// Number
		'\uFF10', '\u0030', // ０ => 0
		'\uFF11', '\u0031', // １ => 1
		'\uFF12', '\u0032', // ２ => 2
		'\uFF13', '\u0033', // ３ => 3
		'\uFF14', '\u0034', // ４ => 4
		'\uFF15', '\u0035', // ５ => 5
		'\uFF16', '\u0036', // ６ => 6
		'\uFF17', '\u0037', // ７ => 7
		'\uFF18', '\u0038', // ８ => 8
		'\uFF19', '\u0039', // ９ => 9
		// Letter
		'\uFF21', '\u0041', // Ａ => A
		'\uFF22', '\u0042', // Ｂ => B
		'\uFF23', '\u0043', // Ｃ => C
		'\uFF24', '\u0044', // Ｄ => D
		'\uFF25', '\u0045', // Ｅ => E
		'\uFF26', '\u0046', // Ｆ => F
		'\uFF27', '\u0047', // Ｇ => G
		'\uFF28', '\u0048', // Ｈ => H
		'\uFF29', '\u0049', // Ｉ => I
		'\uFF2A', '\u004A', // Ｊ => J
		'\uFF2B', '\u004B', // Ｋ => K
		'\uFF2C', '\u004C', // Ｌ => L
		'\uFF2D', '\u004D', // Ｍ => M
		'\uFF2E', '\u004E', // Ｎ => N
		'\uFF2F', '\u004F', // Ｏ => O
		'\uFF30', '\u0050', // Ｐ => P
		'\uFF31', '\u0051', // Ｑ => Q
		'\uFF32', '\u0052', // Ｒ => R
		'\uFF33', '\u0053', // Ｓ => S
		'\uFF34', '\u0054', // Ｔ => T
		'\uFF35', '\u0055', // Ｕ => U
		'\uFF36', '\u0056', // Ｖ => V
		'\uFF37', '\u0057', // Ｗ => W
		'\uFF38', '\u0058', // Ｘ => X
		'\uFF39', '\u0059', // Ｙ => Y
		'\uFF3A', '\u005A', // Ｚ => Z
		'\uFF41', '\u0061', // ａ => a
		'\uFF42', '\u0062', // ｂ => b
		'\uFF43', '\u0063', // ｃ => c
		'\uFF44', '\u0064', // ｄ => d
		'\uFF45', '\u0065', // ｅ => e
		'\uFF46', '\u0066', // ｆ => f
		'\uFF47', '\u0067', // ｇ => g
		'\uFF48', '\u0068', // ｈ => h
		'\uFF49', '\u0069', // ｉ => i
		'\uFF4A', '\u006A', // ｊ => j
		'\uFF4B', '\u006B', // ｋ => k
		'\uFF4C', '\u006C', // ｌ => l
		'\uFF4D', '\u006D', // ｍ => m
		'\uFF4E', '\u006E', // ｎ => n
		'\uFF4F', '\u006F', // ｏ => o
		'\uFF50', '\u0070', // ｐ => p
		'\uFF51', '\u0071', // ｑ => q
		'\uFF52', '\u0072', // ｒ => r
		'\uFF53', '\u0073', // ｓ => s
		'\uFF54', '\u0074', // ｔ => t
		'\uFF55', '\u0075', // ｕ => u
		'\uFF56', '\u0076', // ｖ => v
		'\uFF57', '\u0077', // ｗ => w
		'\uFF58', '\u0078', // ｘ => x
		'\uFF59', '\u0079', // ｙ => y
		'\uFF5A', '\u007A', // ｚ => z
		// Symbol
		'\u3000', '\u0020', // 　 =>
		'\uFF01', '\u0021', // ！ => !
		'\u2033', '\u0022', // " => "
		'\u201C', '\u0022', // “ => "
		'\u201D', '\u0022', // ” => "
		'\uFF03', '\u0023', // ＃ => #
		'\uFF04', '\u0024', // ＄ => $
		'\uFF05', '\u0025', // ％ => %
		'\uFF06', '\u0026', // ＆ => &
		'\u2019', '\'',     // ’ => '
		'\uFF08', '\u0028', // （ => (
		'\uFF09', '\u0029', // ） => )
		'\uFF0A', '\u002A', // ＊ => *
		'\uFF0B', '\u002B', // ＋ => +
		'\uFF0C', '\u002C', // ， => ,
		'\uFF0D', '\u002D', // － => -
		'\u2010', '\u002D', // ‐ => -
		'\u2212', '\u002D', // − => -
		'\u30FC', '\u002D', // ー => -
		'\uFF0E', '\u002E', // ． => .
		'\uFF0F', '\u002F', // ／ => /
		'\uFF1A', '\u003A', // ： => :
		'\uFF1B', '\u003B', // ； => ;
		'\uFF1C', '\u003C', // ＜ => <
		'\uFF1D', '\u003D', // ＝ => =
		'\uFF1E', '\u003E', // ＞ => >
		'\uFF1F', '\u003F', // ？ => ?
		'\uFF20', '\u0040', // ＠ => @
		'\uFF3B', '\u005B', // ［ => [
		'\uFFE5', '\\',     // ￥ => \
		'\uFF3D', '\u005D', // ］ => ]
		'\uFF3E', '\u005E', // ＾ => ^
		'\uFF3F', '\u005F', // ＿ => _
		'\uFF40', '\u0060', // ｀ => `
		'\uFF5B', '\u007B', // ｛ => {
		'\uFF5C', '\u007C', // ｜ => |
		'\uFF5D', '\u007D', // ｝ => }
		'\uFF5E', '\u007E', // ～ => ~
		'\u301C', '\u007E' // 〜 => ~
	);

	// s2m single-byte char to multi-byte char
	private final static Map<Character, Character> s2m = initSM();
	
	private final static Map<Character, Character> initSM() {
		Map<Character, Character> s2m = Collections.toReverse(m2s);
		s2m.put('\u002D', '\uFF0D'); // - => －
		s2m.put('\u0022', '\u2033'); // ″ => "
		s2m.put('\u007E', '\uFF5E'); // ~ => ～
		return s2m;
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
