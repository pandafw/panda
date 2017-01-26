package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;

@IocBean
class NotPublicClass {

	@At("/here")
	@To(View.JSON)
	public String here() {
		return "asfdasdf";
	}

}
