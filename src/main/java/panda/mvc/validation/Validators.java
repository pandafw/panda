package panda.mvc.validation;

import panda.mvc.ActionContext;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.validation.validator.Validator;


public interface Validators {
	// -------------------------------------------------------
	// validator name
	//
	public static final String CAST = "cast";
	public static final String REQUIRED = "required";
	public static final String EMPTY = "empty";

	public static final String EL = "el";
	public static final String REGEX = "regex";
	public static final String EMAIL = "email";
	public static final String FILENAME = "filename";
	public static final String CREDITCARDNO = "creditcardno";

	public static final String BINARY = "binary";
	public static final String DATE = "date";
	public static final String NUMBER = "number";
	public static final String STRING = "string";
	public static final String DECIMAL = "decimal";

	public static final String FILE = "file";
	public static final String IMAGE = "image";

	public static final String CONSTANT = "constant";
	public static final String PROHIBITED = "prohibited";

	public static final String VISIT = "visit";

	// -------------------------------------------------------
	// validation message id
	//
	public static final String MSGID_CAST_BOOLEAN = "validation-cast-boolean";
	public static final String MSGID_CAST_DATE = "validation-cast-date";
	public static final String MSGID_CAST_NUMBER = "validation-cast-number";
	public static final String MSGID_CONSTANT = "validation-constant";
	public static final String MSGID_DATE_RANGE_TO = "validation-date-range-to";
	public static final String MSGID_NUMBER_RANGE_TO = "validation-number-range-to";
	public static final String MSGID_NUMBER_MIN = "validation-number-min";
	public static final String MSGID_REQUIRED = "validation-required";
	
	/**
	 * @param ac action context
	 * @return true if no validation errors
	 */
	boolean validate(ActionContext ac) throws ValidateException;

	/**
	 * create validator
	 * @param ac action context
	 * @param v validator annotation
	 * @return validator
	 */
	Validator createValidator(ActionContext ac, Validate v);

	/**
	 * validate value
	 */
	boolean validate(ActionContext ac, Validator parent, String name, Object value, Validates vs);
	
}
