package panda.mvc.testapp.classes.action.adaptor;

import java.util.Date;

import panda.mvc.annotation.param.Param;

public class Issue543 {

	@Param(value = "d", dfmt = "yyyyMMdd")
	public Date d;

}
