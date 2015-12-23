package panda.mvc.init.errmodule;

import panda.mvc.annotation.At;

@At
public class SimpleErrorModule {

	@At({ "/check", "" })
	public void check() {
	}

	@At({ "/check" })
	public void check2() {
	}

}
