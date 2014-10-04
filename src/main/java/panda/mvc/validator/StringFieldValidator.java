package panda.mvc.validator;

import panda.lang.AsiaStrings;

public class StringFieldValidator extends AbstractStringFieldValidator {

	public static final char ANY = '*';
	public static final char ALPHA_NUMBER = 'A';
	public static final char HANKAKU = 'H';
	public static final char HANKAKU_KATAKANA = 'h';
	public static final char ZENKAKU = 'Z';
	public static final char ZENKAKU_KATAKANA = 'z';
	
	private char type = ANY;
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
		
		// only check for a minimum value if the min parameter is set
		if (minLength != null && length < minLength) {
			return false;
		}

		// only check for a maximum value if the max parameter is set
		if (maxLength != null && length > maxLength) {
			return false;
		}

		switch (type) {
		case HANKAKU: 
			return AsiaStrings.isHankakuString(value);
		case HANKAKU_KATAKANA:
			return AsiaStrings.isHankakuKatakanaString(value);
		case ZENKAKU:
			return AsiaStrings.isZenkakuString(value);
		case  ZENKAKU_KATAKANA:
			return AsiaStrings.isZenkakuKatakanaString(value);
		}
		
		return true;
	}
}
