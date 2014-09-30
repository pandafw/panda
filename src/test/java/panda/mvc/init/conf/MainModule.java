package panda.mvc.init.conf;

import java.util.ArrayList;
import java.util.List;

import panda.bind.json.Jsons;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Fail;
import panda.mvc.annotation.Ok;
import panda.mvc.annotation.param.Param;

@At
@Fail("json")
public class MainModule {

	@At("/param/a")
	@Ok("json")
	public List<String> f_A(@Param("ids") long[] ids) {
		List<String> ls = new ArrayList<String>();
		for (long n : ids) {
			ls.add(String.valueOf(n));
		}
		return ls;
	}

	@At("/param/b")
	@Ok("raw")
	public String f_B(@Param("ids") long[] ids) {
		return Jsons.toJson(ids);
	}

}
