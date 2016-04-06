package panda.mvc.validation.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.validation.annotation.Validate;

@Modules(scan = true)
@At
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
			@Validates(@Validate(value=Validators.NUMBER, params="{min: -100, max: 100}", message="min: ${top.min}, max: ${top.max}"))
			int one) {

		return 1;
	}

	@At
	public Object oneCast(
			@Param("one")
			@Validates({
				@Validate(value=Validators.NUMBER, params="{min: -100, max: 100}", message="min: ${top.min}, max: ${top.max}"),
				@Validate(value=Validators.CAST, message="int cast error")
				})
			int one) {

		return 1;
	}

	@At
	public Object visitOne(
			@Param("one.*")
			@Validates(@Validate(value=Validators.VISIT))
			ValidateObject one) {

		return 1;
	}

	@At
	public Object reqirAny(
			@Param
			@Validates(@Validate(value=Validators.REQUIRED, params="fields: { 'consts':'', 'el':'el2' }", message="required"))
			ValidateObject one) {
		return one.consts + ' ' + one.el;
	}

	@At
	public Object reqirOne(
			@Param("one.*")
			@Validates(@Validate(value=Validators.REQUIRED, message="required"))
			ValidateObject one) {

		return 1;
	}
}
