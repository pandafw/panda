package panda.lang.chardet;

import panda.lang.Strings;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public enum LangHint {
	ALL,
	JAPANESE,
	CHINESE,
	SIMPLIFIED_CHINESE,
	TRADITIONAL_CHINESE,
	KOREAN;
	
	public static LangHint parse(String hint) {
		if (Strings.isNotEmpty(hint)) {
			char c = Character.toUpperCase(hint.charAt(0));
			switch (c) {
			case 'J':
				return JAPANESE;
			case 'C':
			case 'Z':
				return CHINESE;
			case 'S':
				return SIMPLIFIED_CHINESE;
			case 'T':
				return TRADITIONAL_CHINESE;
			case 'K':
				return KOREAN;
			}
		}
		return ALL;
	}
}
