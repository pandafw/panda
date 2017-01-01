package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.lang.JapanChars;
import panda.lang.JapanStrings;
import panda.lang.Strings;

@IocBean(singleton=false)
public class StringValidator extends AbstractStringValidator {

	public static final char ANY = '*';
	public static final char ALPHA = 'a';
	public static final char NUMBER = 'n';
	public static final char ALPHA_NUMBER = 'A';
	public static final char HANKAKU = 'H';
	public static final char ZENKAKU = 'Z';
	public static final char HANKAKU_KATAKANA = 'k';
	public static final char HANKAKU_KATAKANA_SPACE = 'ｶ';
	public static final char ZENKAKU_KATAKANA = 'K';
	public static final char ZENKAKU_KATAKANA_SPACE = 'カ';
	public static final char ZENKAKU_KATAKANA_SPACES = 'ガ';
	public static final char ZENKAKU_HIRAGANA = 'G';
	public static final char ZENKAKU_HIRAGANA_SPACE = 'か';
	public static final char ZENKAKU_HIRAGANA_SPACES = 'が';

	private char type = ANY;

	private int zenSize = 0;
	
	private Integer maxLength = null;
	private Integer minLength = null;
	
	/**
	 * string length
	 */
	private Integer length = null;

	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(char type) {
		this.type = type;
	}

	/**
	 * @return the zenSize
	 */
	public int getZenSize() {
		return zenSize;
	}

	/**
	 * @param zenSize the zenSize to set
	 */
	public void setZenSize(int zenSize) {
		this.zenSize = zenSize;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	protected boolean validateString(String value) {
		length = value.length();

		// change length for zen/han
		if (zenSize > 0) {
			int zlen = 0;
			for (int i = 0; i < length; i++) {
				char c = value.charAt(i);
				if (JapanChars.isZenkaku(c)) {
					zlen += zenSize;
				}
				else {
					zlen++;
				}
			}
			length = zlen;
		}
		
		// only check for a minimum value if the min parameter is set
		if (minLength != null && length < minLength) {
			return false;
		}

		// only check for a maximum value if the max parameter is set
		if (maxLength != null && length > maxLength) {
			return false;
		}

		switch (type) {
		case ALPHA:
			return Strings.isAlpha(value);
		case NUMBER:
			return Strings.isNumeric(value);
		case ALPHA_NUMBER:
			return Strings.isAlphanumeric(value);
		case HANKAKU: 
			return JapanStrings.isHankaku(value);
		case ZENKAKU:
			return JapanStrings.isZenkaku(value);
		case HANKAKU_KATAKANA:
			return JapanStrings.isHankakuKatakanaSpace(value);
		case ZENKAKU_KATAKANA:
			return JapanStrings.isZenkakuKatakana(value);
		case ZENKAKU_KATAKANA_SPACE:
			return JapanStrings.isZenkakuKatakanaSpace(value);
		case ZENKAKU_KATAKANA_SPACES:
			return JapanStrings.isZenkakuKatakanaSpaces(value);
		case ZENKAKU_HIRAGANA:
			return JapanStrings.isZenkakuHiragana(value);
		case ZENKAKU_HIRAGANA_SPACE:
			return JapanStrings.isZenkakuHiraganaSpace(value);
		case ZENKAKU_HIRAGANA_SPACES:
			return JapanStrings.isZenkakuHiraganaSpaces(value);
		}
		
		return true;
	}
}
