package panda.mvc.validation;

import panda.mvc.annotation.At;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.validation.annotation.Validate;

@At
@Modules(scan = true)
@Ok("raw")
@Err("json")
@Fatal("json")
public class ValidateModule {

	@At
	public Object zero() {
		return true;
	}

	@At
	public Object one(
			@Param("one")
			@Validates(@Validate(value="number", params="{min: -100, max: 100}", message="min: ${top.min}, max: ${top.max}"))
			int one) {

		return 1;
	}

	@At
	public Object oneCast(
			@Param("one")
			@Validates({
				@Validate(value="number", params="{min: -100, max: 100}", message="min: ${top.min}, max: ${top.max}"),
				@Validate(value="cast", message="int cast error")
				})
			int one) {

		return 1;
	}

	@At
	public Object visitOne(
			@Param("one.*")
			@Validates(@Validate(value="visit"))
			ValidateObject one) {

		return 1;
	}
}
