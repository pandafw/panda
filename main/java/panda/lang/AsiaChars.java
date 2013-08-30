package panda.lang;


/**
 * utility class for Asia character
 * @author yf.frank.wang@gmail.com
 */
public abstract class AsiaChars {
	/**
	 * isHankakuKatakanaChar
	 * @param c char
	 * @return true if the char is Hankaku Katakana
	 */
	public static boolean isHankakuKatakanaChar(char c) {
		return c >= '\uFF61' && c <= '\uFF9F';
	}

	/**
	 * isZenkakuKatakanaChar
	 * @param c char
	 * @return true if the char is Zenkaku Katakana
	 */
	public static boolean isZenkakuKatakanaChar(char c) {
		return (c >= '\u30A1' && c <= '\u30df')
				|| (c >= '\u30E0' && c <= '\u30F6');
	}

	/**
	 * isHankakuChar
	 * @param c char
	 * @return true if the char is Hankaku
	 */
	public static boolean isHankakuChar(char c) {
		if (c <= '\377') {
			switch (c) {
			case '\247':
			case '\250':
			case '\260':
			case '\261':
			case '\264':
			case '\266':
			case '\327':
			case '\367':
				return false;
			default:
				return true;
			}
		}

		return isHankakuKatakanaChar(c);
	}

	/**
	 * isZenkakuChar
	 * @param c char
	 * @return true if the char is Zenkaku
	 */
	public static boolean isZenkakuChar(char c) {
		return !isHankakuChar(c);
	}
}
