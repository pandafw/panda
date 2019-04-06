package panda.mvc.init.conf;

import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

@At
@To(value=Views.RAW, fatal=Views.SJSON)
public class RestModule {

	@At(value="/abc", method=HttpMethod.GET)
	public String get() {
		return "get";
	}

	@At(value="/abc", method=HttpMethod.PUT)
	public String put() {
		return "put";
	}

	@At(value="/abc", method=HttpMethod.POST)
	public String post() {
		return "post";
	}

	@At(value="/abc", method=HttpMethod.DELETE)
	public String delete() {
		return "delete";
	}

	@At(value="/xyz", method={ HttpMethod.GET, HttpMethod.POST })
	public String getAndPost() {
		return "get&post";
	}

	@At("/a/(.*)/b/(.+?)/c/(.*)$")
	public String pathArgs_01(@PathArg int a, @PathArg int b, @PathArg String c) {
		return c + "?a=" + a + "&b=" + b;
	}

}
