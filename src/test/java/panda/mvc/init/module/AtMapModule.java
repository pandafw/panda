package panda.mvc.init.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.Fail;
import panda.mvc.annotation.Ok;

@At("/atmap")
@Ok("json")
@Fail("json")
public class AtMapModule {

	@At(key = "at.abc", value = "/ABC")
	public String abc() {
		return ">>abc";
	}

	@At(key = "at.xyz")
	public String xyz() {
		return ">>xyz";
	}

}
