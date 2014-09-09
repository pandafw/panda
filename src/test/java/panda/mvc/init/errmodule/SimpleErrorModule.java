package panda.mvc.init.errmodule;

import panda.mvc.annotation.At;

public class SimpleErrorModule {

	@At({ "/check", "" })
	public void check() {
	}

}
