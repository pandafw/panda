package panda.mvc.validation.module;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.To;
import panda.mvc.annotation.Validate;
import panda.mvc.annotation.Validates;
import panda.mvc.annotation.param.Param;
import panda.mvc.ioc.provider.ComboIocProvider;
import panda.mvc.validator.Validators;

@At
@To(value=View.RAW, error=View.JSON, fatal=View.JSON)
@IocBy(type=ComboIocProvider.class, args={ "*json","panda/mvc/validation/module/test.js", "*default" })
@Modules(scan = true)
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
			@Validates(@Validate(value=Validators.REQUIRED, params="refers: { 'consts':'', 'el':'el2' }", message="required"))
			ValidateObject one) {
		return one.consts + ' ' + one.el;
	}

	@At
	public Object reqirAny2(
			@Param
			@Validates(@Validate(value=Validators.REQUIRED, params="fields: [ 'consts', 'el' ]", message="required"))
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
