package panda.mvc.validation;

import panda.mvc.validation.annotation.Validation;
import panda.mvc.validation.annotation.Validator;
import panda.mvc.validation.validator.StringFieldValidator;

public class Sample {

	@Validation(@Validator(type=StringFieldValidator.class))
	public String str;
	
}
