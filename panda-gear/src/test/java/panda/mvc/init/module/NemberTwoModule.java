package panda.mvc.init.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;

@At("/two")
public class NemberTwoModule {

	@At("abc")
	@To(Views.RAW)
	public String say() {
		System.out.println("java");
		return "haha";
	}

	@At
	@To(value=Views.RAW, fatal=Views.SJSON)
	public boolean login(@Param("username") String userName, @Param("password") String password,
			@Param("authCode") Long authCode) {
		return !(userName == null || password == null || authCode == null);
	}

	@At("pathme/(.*)$")
	@To(Views.RAW)
	public String pathme(@PathArg String abc) {
		return abc;
	}

	@At("pathtwo/(.+?)/(.*)$")
	@To(Views.RAW)
	public String pathtwo(@PathArg int abc, @PathArg long xyz) {
		return abc + "+" + xyz;
	}
}
