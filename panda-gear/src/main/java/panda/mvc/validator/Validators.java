package panda.mvc.validator;

import panda.mvc.ActionContext;
import panda.mvc.ValidateHandler;
import panda.mvc.impl.DefaultValidateHandler;

public class Validators {
	// -------------------------------------------------------
	// validation message id
	//
	public static final String MSGID_BOOLEAN = "validation-boolean";
	public static final String MSGID_CONSTANT = "validation-constant";
	public static final String MSGID_CIDR = "validation-cidr";
	public static final String MSGID_CREDITCARDNO = "validation-creditcardno";
	public static final String MSGID_DATE = "validation-date";
	public static final String MSGID_DATETIME = "validation-datetime";
	public static final String MSGID_DATEHHMM = "validation-datehhmm";
	public static final String MSGID_DATE_RANGE = "validation-date-range";
	public static final String MSGID_DATE_TO = "validation-date-to";
	public static final String MSGID_DECIMAL = "validation-decimal";
	public static final String MSGID_DECIMAL_PRECISION = "validation-decimal-precision";
	public static final String MSGID_EMAIL = "validation-email";
	public static final String MSGID_EMPTY = "validation-empty";
	public static final String MSGID_FILE = "validation-file";
	public static final String MSGID_FILENAME = "validation-filename";
	public static final String MSGID_IMAGE = "validation-image";
	public static final String MSGID_IMAIL = "validation-imail";
	public static final String MSGID_INCORRECT = "validation-incorrect";
	public static final String MSGID_INTEGER = "validation-integer";
	public static final String MSGID_LOCALE = "validation-locale";
	public static final String MSGID_NUMBER_TO = "validation-number-to";
	public static final String MSGID_NUMBER_RANGE = "validation-number-range";
	public static final String MSGID_PROHIBITED = "validation-prohibited";
	public static final String MSGID_REQUIRED = "validation-required";
	public static final String MSGID_STRING_LENTH = "validation-string-length";
	public static final String MSGID_TIME = "validation-time";
	public static final String MSGID_TIMEHHMM = "validation-timehhmm";
	public static final String MSGID_TIME_RANGE = "validation-time-range";
	public static final String MSGID_TIME_TO = "validation-time-to";
	public static final String MSGID_URL = "validation-url";
	
	// -------------------------------------------------------
	// string type validation message id
	//
	public static final String MSGID_ALPHA_STRING = "validation-alphastring";
	public static final String MSGID_ALPHA_NUMERIC_STRING = "validation-alphanumericstring";
	public static final String MSGID_NUMERIC_STRING = "validation-numericstring";
	public static final String MSGID_HANKAKU_STRING = "validation-hankakustring";
	public static final String MSGID_HANKAKU_KATAKANA_STRING = "validation-hankakukatakanastring";
	public static final String MSGID_ZENKAKU_STRING = "validation-zenkakustring";
	public static final String MSGID_ZENKAKU_KATAKANA_STRING = "validation-zenkakukatakanastring";
	public static final String MSGID_ZENKAKU_HIRAGANA_STRING = "validation-zenkakuhiraganastring";

	// -------------------------------------------------------
	// extend validation message id
	//
	public static final String MSGID_EMAIL_NOT_SAME = "validation-email-notsame";
	public static final String MSGID_PASSWORD = "validation-password";
	public static final String MSGID_PASSWORD_NOT_SAME = "validation-password-notsame";
	public static final String MSGID_PASSWORD_INCORRECT = "validation-password-incorrect";
	

	/**
	 * create validate handler
	 * @param context action context
	 * @return validate handler instance
	 */
	public static ValidateHandler getValidateHandler(ActionContext context) {
		ValidateHandler vh = context.getIoc().getIfExists(ValidateHandler.class);
		if (vh == null) {
			vh = new DefaultValidateHandler();
		}
		return vh;
	}

	/**
	 * Use validators to validate object
	 * @param context action context
	 * @param value validate value
	 * @return true if no validation error
	 */
	public static boolean validate(ActionContext context, Object value) {
		return validate(context, value, "");
	}

	/**
	 * Use validate handler to validate object
	 * @param context action context
	 * @param value validate value
	 * @param name object name
	 * @return true if no validation error
	 */
	public static boolean validate(ActionContext context, Object value, String name) {
		ValidateHandler vh = getValidateHandler(context);
		return vh.validate(context, name, value);
	}
}
