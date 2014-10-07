package panda.mvc.validation;

import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.validation.annotation.Validation;
import panda.mvc.validation.annotation.Validator;

@At
public class SampleModule {

	@At
	public Object zero() {
		return true;
	}

	@At
	public Object one(
			@Param("one")
			@Validation(@Validator(value="int", message="min: ${top.min}, max: ${top.max}"))
			int one) {

		return 1;
	}

	@At
	public Object visitOne(
			@Param("^one.")
			@Validation(@Validator(value="visit"))
			SampleParam one) {

		return 1;
	}
}
