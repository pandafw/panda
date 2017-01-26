package panda.mvc.init.module;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

@At("/two")
public class NemberTwoModule {

	@At("abc")
	@To(View.RAW)
	public String say() {
		System.out.println("java");
		return "haha";
	}

	@At
	@To(value=View.RAW, fatal=View.JSON)
	public boolean login(@Param("username") String userName, @Param("password") String password,
			@Param("authCode") Long authCode) {
		return !(userName == null || password == null || authCode == null);
	}

	@At("pathme/(.*)$")
	@To(View.RAW)
	public String pathme(String abc) {
		return abc;
	}

	@At("pathtwo/(.+?)/(.*)$")
	@To(View.RAW)
	public String pathtwo(int abc, long xyz) {
		return abc + "+" + xyz;
	}
}
