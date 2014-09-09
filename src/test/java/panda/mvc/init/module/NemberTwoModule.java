package panda.mvc.init.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.Fail;
import panda.mvc.annotation.Ok;
import panda.mvc.annotation.param.Param;

@At("/two")
public class NemberTwoModule {

	@At("/abc")
	@Ok("json")
	public String say() {
		System.out.println("java");
		return "haha";
	}

	@At
	@Ok("json")
	@Fail("json")
	public boolean login(@Param("username") String userName, @Param("password") String password,
			@Param("authCode") Long authCode) {
		return !(userName == null || password == null);
	}

	@At("/need")
	@Ok("json")
	@Fail("json")
	@Filters(@By(type = CheckSession.class, args = { "AUTH", "/two/abc" }))
	public String needLogin() {
		System.out.println("abc");
		return "ABC";
	}

	@At("/pathme/*")
	@Ok("json")
	@Fail("json")
	public String pathme(int abc, long xyz) {
		return "" + abc;
	}
}
