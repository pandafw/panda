package panda.mvc.validation.module;

import java.util.Date;

import panda.mvc.annotation.validate.BinaryValidate;
import panda.mvc.annotation.validate.ConstantValidate;
import panda.mvc.annotation.validate.CreditCardNoValidate;
import panda.mvc.annotation.validate.DateValidate;
import panda.mvc.annotation.validate.ELValidate;
import panda.mvc.annotation.validate.EmailValidate;
import panda.mvc.annotation.validate.FilenameValidate;
import panda.mvc.annotation.validate.NumberValidate;
import panda.mvc.annotation.validate.ProhibitedValidate;
import panda.mvc.annotation.validate.RegexValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.annotation.validate.URLValidate;

public class ValidateObject {
	@BinaryValidate(minLength=5, maxLength=100, message="${top.length}, ${top.minLength} ~ ${top.maxLength}")
	public byte[] bin;

	@ConstantValidate(list="[ 'a', 'b' ]}", message="${top.value}, ${top.consts}")
	public String consts;
	
	@CreditCardNoValidate(message="'${top.value}' is not a card no.")
	public String cardno;
	
	@DateValidate(min="2010-01-01 00:00:00", max="2010-12-12 12:12:12", message="'${top.value.getTime()}', ${top.min.getTime()} ~ ${top.max.getTime()}")
	public Date date;

	@ELValidate(el="top.value == \"ok\"", message="'${top.value}', '${top.el}'")
	public String el;
	
	@ELValidate(el="top.value == top.parent.value.el", message="'${top.value}', '${top.el}'")
	public String el2;
	
	@EmailValidate(message="'${top.value}' is not a email.")
	public String email;

	@URLValidate(message="'${top.value}' is not a url.")
	public String url;

	@FilenameValidate(message="'${top.value}' is not a filename.")
	public String filename;
	
	@NumberValidate(min="-100", max="100", message="${top.value}, ${top.min} ~ ${top.max}")
	public int number;

	@ProhibitedValidate(list="[ 'a', 'b' ]", message="${top.value}, ${top.consts}")
	public String prohibited;
	
	@RegexValidate(regex="ok", message="'${top.value}', '${top.regex}'")
	public String regex;

	@RegexValidate(regex="#(regex-telno)", message="not a telephone number.")
	public String telno;

	@StringValidate(type='A', message="'${top.value}' is not A.")
	public String string;

	@StringValidate(minLength=5, maxLength=100, message="${top.length}, ${top.minLength} ~ ${top.maxLength}")
	public String strlen;
	
	@StringValidate(shortCircuit=true, minLength=5, maxLength=100, message="${top.length}, ${top.minLength} ~ ${top.maxLength}")
	@RegexValidate(regex="[a-zA-Z]*", message="'${top.value}' is not A.")
	public String shortCircuitTrue;
	
	@StringValidate(minLength=5, maxLength=100, message="${top.length}, ${top.minLength} ~ ${top.maxLength}")
	@RegexValidate(regex="[a-zA-Z]*", message="'${top.value}' is not A.")
	public String shortCircuitFalse;
}
