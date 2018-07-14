package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

@IocBean
class NotPublicClass {

	@At("/here")
	@To(Views.SJSON)
	public String here() {
		return "asfdasdf";
	}

}
