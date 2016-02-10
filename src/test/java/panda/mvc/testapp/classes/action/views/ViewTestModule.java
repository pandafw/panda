package panda.mvc.testapp.classes.action.views;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import panda.bind.json.Jsons;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.lang.Charsets;
import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;

@IocBean
@At("/views")
public class ViewTestModule {

	// ---------------JSP View


	@At("jsp2")
	@Ok("jsp:jsp/views/jspView")
	public void jspView2() {
	}

	@At("jsp3")
	@Ok("jsp:/WEB-INF/jsp/views/jspView")
	public void jspView3() {
	}

	@At("jsp4")
	@Ok("jsp:/WEB-INF/jsp/views/jspView.jsp")
	public void jspView4() {
	}

	// -------------ServerRedirectView
	@At("red")
	@Ok(">>:/${reqp.to}.jsp")
	public void serverRedirectView() {
	}

	@At("red2")
	@Ok("RedirEct:/${reqp.to}.jsp")
	public void serverRedirectView2() {
	}

	@At("red3")
	@Ok("redirect:/${reqp.to}.jsp")
	public void serverRedirectView3() {
	}

	// -------------ForwardView
	@At("for")
	@Ok("->:/${reqp.to}.jsp")
	public void forwardView() {
	}

	@At("for2")
	@Ok("fOrWard:/${reqp.to}.jsp")
	public void forwardView2() {
	}

	@At("for3")
	@Ok("forward:/${reqp.to == null ? 'base' : 'base'}.jsp")
	public void forwardView3() {
	}

	// --------------Raw view
	@At("raw")
	@Ok("raw")
	public String raw() {
		return "ABC";
	}

	@At("raw2")
	@Ok("raw")
	public InputStream raw2() throws Throwable {
		return getClass().getResourceAsStream("abc.txt");
	}

	@At("raw3")
	@Ok("raw")
	public Reader raw3() throws Throwable {
		return Streams.toReader(getClass().getResourceAsStream("abc.txt"), Charsets.UTF_8);
	}

	@At("raw4")
	@Ok("raw")
	public void raw4() {
	}

	@At("raw5")
	@Ok("raw:json")
	public String raw5() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "wendal");
		return Jsons.toJson(map);
	}
}
