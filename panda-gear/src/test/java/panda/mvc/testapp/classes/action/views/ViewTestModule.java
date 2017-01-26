package panda.mvc.testapp.classes.action.views;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import panda.bind.json.Jsons;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.mvc.ActionContext;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;

@IocBean
@At("/views")
public class ViewTestModule {

	@IocInject
	protected ActionContext context;

	@At("jsp2")
	@To("jsp:jsp/views/jspView")
	public void jspView2() {
		context.getReq().put("obj", "2");
	}

	@At("jsp3")
	@To("jsp:/WEB-INF/jsp/views/jspView")
	public void jspView3() {
		context.getReq().put("obj", "3");
	}

	@At("jsp4")
	@To("jsp:/WEB-INF/jsp/views/jspView.jsp")
	public void jspView4() {
		context.getReq().put("obj", "4");
	}

	// -------------ServerRedirectView
	@At("red")
	@To(">>:/${reqp.to}.jsp")
	public void serverRedirectView() {
	}

	@At("red2")
	@To("RedirEct:/${reqp.to}.jsp")
	public void serverRedirectView2() {
	}

	@At("red3")
	@To("redirect:/${reqp.to}.jsp")
	public void serverRedirectView3() {
	}

	// -------------ForwardView
	@At("for")
	@To("->:/${reqp.to}.jsp")
	public void forwardView() {
	}

	@At("for2")
	@To("fOrWard:/${reqp.to}.jsp")
	public void forwardView2() {
	}

	@At("for3")
	@To("forward:/${reqp.to == null ? 'base' : 'base'}.jsp")
	public void forwardView3() {
	}

	// --------------Raw view
	@At("raw")
	@To("raw")
	public String raw() {
		return "ABC";
	}

	@At("raw2")
	@To("raw")
	public InputStream raw2() throws Throwable {
		return getClass().getResourceAsStream("abc.txt");
	}

	@At("raw3")
	@To("raw")
	public Reader raw3() throws Throwable {
		return Streams.toReader(getClass().getResourceAsStream("abc.txt"), Charsets.UTF_8);
	}

	@At("raw4")
	@To("raw")
	public void raw4() {
	}

	@At("raw5")
	@To("raw:json")
	public String raw5() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "wendal");
		return Jsons.toJson(map);
	}
}
