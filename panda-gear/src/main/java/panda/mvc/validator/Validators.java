package panda.mvc.validator;

import panda.mvc.ActionContext;
import panda.mvc.ValidateHandler;
import panda.mvc.impl.DefaultValidateHandler;

public class Validators {
	// -------------------------------------------------------
	// validator name
	//
	public static final String CAST = "cast";
	public static final String REQUIRED = "required";
	public static final String EMPTY = "empty";

	public static final String EL = "el";
	public static final String REGEX = "regex";
	public static final String EMAIL = "email";
	public static final String IMAIL = "imail";
	public static final String FILENAME = "filename";
	public static final String CREDITCARDNO = "creditcardno";

	public static final String BINARY = "binary";
	public static final String CIDR = "cidr";
	public static final String DATE = "date";
	public static final String NUMBER = "number";
	public static final String STRING = "string";
	public static final String DECIMAL = "decimal";

	public static final String FILE = "file";
	public static final String IMAGE = "image";

	public static final String CONSTANT = "constant";
	public static final String PROHIBITED = "prohibited";

	public static final String URL = "url";
	public static final String VISIT = "visit";

	// -------------------------------------------------------
	// validation message id
	//
	public static final String MSGID_CAST_BOOLEAN = "validation-cast-boolean";
	public static final String MSGID_CAST_CHAR = "validation-cast-char";
	public static final String MSGID_CAST_DATE = "validation-cast-date";
	public static final String MSGID_CAST_DATETIME = "validation-cast-datetime";
	public static final String MSGID_CAST_DATEHHMM = "validation-cast-datehhmm";
	public static final String MSGID_CAST_DECIMAL = "validation-cast-decimal";
	public static final String MSGID_CAST_FILE = "validation-cast-file";
	public static final String MSGID_CAST_NUMBER = "validation-cast-number";
	public static final String MSGID_CAST_TIME = "validation-cast-time";
	public static final String MSGID_CAST_HHMM = "validation-cast-hhmm";
	public static final String MSGID_CAST_URL = "validation-cast-url";
	public static final String MSGID_CONSTANT = "validation-constant";
	public static final String MSGID_PROHIBITED = "validation-prohibited";
	public static final String MSGID_CIDR = "validation-cidr";
	public static final String MSGID_DATETIME_RANGE = "validation-datetime-range";
	public static final String MSGID_DATE_RANGE = "validation-date-range";
	public static final String MSGID_TIME_RANGE = "validation-time-range";
	public static final String MSGID_DATETIME_TO = "validation-datetime-to";
	public static final String MSGID_DATE_TO = "validation-date-to";
	public static final String MSGID_TIME_TO = "validation-time-to";
	public static final String MSGID_NUMBER_TO = "validation-number-to";
	public static final String MSGID_NUMBER_RANGE = "validation-number-range";
	public static final String MSGID_REQUIRED = "validation-required";
	public static final String MSGID_STRING_LENTH = "validation-string-length";
	public static final String MSGID_FILE = "validation-file";
	public static final String MSGID_IMAGE = "validation-image";
	public static final String MSGID_EMAIL = "validation-email";
	public static final String MSGID_IMAIL = "validation-imail";
	public static final String MSGID_URL = "validation-url";
	
	// -------------------------------------------------------
	// extend validation message id
	//
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
