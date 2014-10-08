package panda.mvc.init.module;

import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;

@At("/two")
public class NemberTwoModule {

	@At("/abc")
	@Ok("raw")
	public String say() {
		System.out.println("java");
		return "haha";
	}

	@At
	@Ok("raw")
	@Fatal("json")
	public boolean login(@Param("username") String userName, @Param("password") String password,
			@Param("authCode") Long authCode) {
		return !(userName == null || password == null || authCode == null);
	}

	//TODO
//	@At("/need")
//	@Ok("json")
//	@Fail("json")
//	@Filters(@By(type = CheckSession.class, args = { "AUTH", "/two/abc" }))
//	public String needLogin() {
//		System.out.println("abc");
//		return "ABC";
//	}

	@At("/pathme/*")
	@Ok("raw")
	public String pathme(int abc, long xyz) {
		return abc + "+" + xyz;
	}

	@At("/pathtwo/?/?")
	@Ok("raw")
	public String pathtwo(int abc, long xyz) {
		return abc + "+" + xyz;
	}
}
