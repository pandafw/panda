package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Ok;

@IocBean
class NotPublicClass {

	@At("/here")
	@Ok("json")
	public String here() {
		return "asfdasdf";
	}

}
