package panda.mvc.validation;

import panda.mvc.validation.annotation.Validates;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.validator.StringValidator;

public class ValidateObject {

	@Validates(@Validate(type=StringValidator.class, params="{type:'A'}", message="not A"))
	public String string;
	
	@Validates(@Validate(value="number", params="{min: -100, max: 100}", message="${top.length} / ${top.min} ~ ${top.max}"))
	public int number;
	
	@Validates(@Validate(value="email", message="${top.value} is not a email"))
	public String email;
}
