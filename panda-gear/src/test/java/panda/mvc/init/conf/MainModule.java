package panda.mvc.init.conf;

import java.util.ArrayList;
import java.util.List;

import panda.bind.json.Jsons;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

@At
@To(all=View.SJSON)
public class MainModule {

	@At("/param/a")
	@To(View.SJSON)
	public List<String> f_A(@Param("ids") long[] ids) {
		List<String> ls = new ArrayList<String>();
		for (long n : ids) {
			ls.add(String.valueOf(n));
		}
		return ls;
	}

	@At("/param/b")
	@To(View.RAW)
	public String f_B(@Param("ids") long[] ids) {
		return Jsons.toJson(ids);
	}

}
