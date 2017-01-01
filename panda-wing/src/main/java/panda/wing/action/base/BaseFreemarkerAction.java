package panda.wing.action.base;

import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;
import panda.mvc.view.VoidView;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.wing.action.AbstractAction;

public abstract class BaseFreemarkerAction extends AbstractAction {

	@IocInject
	FreemarkerHelper freemarker;
	
	@At("(.*)\\.ftl$")
	@Ok("ftl:${result}")
	public Object ftl(String path) throws Exception {
		String location = "/" + path + ".ftl";
		if (!freemarker.hasTemplate(location)) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, location);
			return VoidView.INSTANCE;
		}
		return location;
	}

	@At("(.*)\\.sftl$")
	@Ok("sftl:${result}")
	public Object sftl(String path) throws Exception {
		return ftl(path);
	}
}
