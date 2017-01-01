package panda.mvc.validation.module;

import java.util.Date;

import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.validation.validator.StringValidator;

public class ValidateObject {
	@Validates(@Validate(value=Validators.BINARY, params="{minLength: 5, maxLength: 100}", message="${top.length}, ${top.minLength} ~ ${top.maxLength}"))
	public byte[] bin;

	@Validates(@Validate(value=Validators.CONSTANT, params="{list: [ 'a', 'b' ]}", message="${top.value}, ${top.consts}"))
	public String consts;
	
	@Validates(@Validate(value=Validators.CREDITCARDNO, message="'${top.value}' is not a card no."))
	public String cardno;
	
	@Validates(@Validate(value=Validators.DATE, params="{min: '2010-01-01 00:00:00', max: '2010-12-12 12:12:12'}", message="'${top.value.getTime()}', ${top.min.getTime()} ~ ${top.max.getTime()}"))
	public Date date;

	@Validates(@Validate(value=Validators.EL, params="{el: 'top.value == \"ok\"'}", message="'${top.value}', '${top.el}'"))
	public String el;
	
	@Validates(@Validate(value=Validators.EL, params="{el: 'top.value == top.parent.value.el'}", message="'${top.value}', '${top.el}'"))
	public String el2;
	
	@Validates(@Validate(value=Validators.EMAIL, message="'${top.value}' is not a email."))
	public String email;

	@Validates(@Validate(value=Validators.URL, message="'${top.value}' is not a url."))
	public String url;

	@Validates(@Validate(value=Validators.FILENAME, message="'${top.value}' is not a filename."))
	public String filename;
	
	@Validates(@Validate(value=Validators.NUMBER, params="{min: -100, max: 100}", message="${top.value}, ${top.min} ~ ${top.max}"))
	public int number;

	@Validates(@Validate(value=Validators.PROHIBITED, params="{list: [ 'a', 'b' ]}", message="${top.value}, ${top.consts}"))
	public String prohibited;
	
	@Validates(@Validate(value=Validators.REGEX, params="{regex: 'ok'}", message="'${top.value}', '${top.regex}'"))
	public String regex;

	@Validates(@Validate(value=Validators.REGEX, params="{ regex: '#(regex-telnumber)' }", message="not a telephone number."))
	public String telno;

	@Validates(@Validate(type=StringValidator.class, params="{type:'A'}", message="'${top.value}' is not A."))
	public String string;

	@Validates(@Validate(type=StringValidator.class, params="{minLength: 5, maxLength: 100}", message="${top.length}, ${top.minLength} ~ ${top.maxLength}"))
	public String strlen;
	
	@Validates({
		@Validate(type=StringValidator.class, shortCircuit=true, params="{minLength: 5, maxLength: 100}", message="${top.length}, ${top.minLength} ~ ${top.maxLength}"),
		@Validate(type=StringValidator.class, params="{type: 'A'}", message="'${top.value}' is not A.")
	})
	public String shortCircuitTrue;
	
	@Validates({
		@Validate(type=StringValidator.class, params="{minLength: 5, maxLength: 100}", message="${top.length}, ${top.minLength} ~ ${top.maxLength}"),
		@Validate(type=StringValidator.class, params="{type: 'A'}", message="'${top.value}' is not A.")
	})
	public String shortCircuitFalse;
}
