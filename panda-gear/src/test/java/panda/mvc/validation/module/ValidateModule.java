package panda.mvc.validation.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.NumberValidate;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.ioc.provider.ComboIocProvider;
import panda.mvc.view.Views;

@At
@To(value=Views.RAW, error=Views.SJSON, fatal=Views.SJSON)
@IocBy(type=ComboIocProvider.class, args={ "*default", "*json","panda/mvc/validation/module/test.js" })
@Modules(scan = true)
public class ValidateModule {

	@At
	public Object zero() {
		return true;
	}

	@At
	public Object one(
			@Param("one")
			@NumberValidate(min="-100", max="100", message="min: ${top.min}, max: ${top.max}")
			int one) {

		return 1;
	}

	@At
	public Object oneCast(
			@Param("one")
			@NumberValidate(min="-100", max="100", message="min: ${top.min}, max: ${top.max}")
			@CastErrorValidate(message="int cast error")
			int one) {

		return 1;
	}

	@At
	public Object visitOne(
			@Param("one.*")
			@VisitValidate
			ValidateObject one) {

		return 1;
	}

	@At
	public Object reqirAny(
			@Param
			@RequiredValidate(refers="{ 'consts':'', 'el':'el2' }", message="required")
			ValidateObject one) {
		return one.consts + ' ' + one.el;
	}

	@At
	public Object reqirAny2(
			@Param
			@RequiredValidate(fields= { "consts", "el" }, message="required")
			ValidateObject one) {
		return one.consts + ' ' + one.el;
	}

	@At
	public Object reqirOne(
			@Param("one.*")
			@RequiredValidate(message="required")
			ValidateObject one) {

		return 1;
	}
}
