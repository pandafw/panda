package panda.mvc.init.conf;

import panda.mvc.annotation.*;
import panda.mvc.annotation.method.DELETE;
import panda.mvc.annotation.method.GET;
import panda.mvc.annotation.method.POST;
import panda.mvc.annotation.method.PUT;

@Ok("raw")
@Fail("json")
public class RestModule {

	@At("/abc")
	@GET
	public String get() {
		return "get";
	}

	@At("/abc")
	@PUT
	public String put() {
		return "put";
	}

	@At("/abc")
	@POST
	public String post() {
		return "post";
	}

	@At("/abc")
	@DELETE
	public String delete() {
		return "delete";
	}

	@At("/xyz")
	@GET
	@POST
	public String getAndPost() {
		return "get&post";
	}

	@At("/a/?/b/?/c/*")
	public String pathArgs_01(int a, int b, String c) {
		return c + "?a=" + a + "&b=" + b;
	}

}
