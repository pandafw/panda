package panda.wing.action.tool;

import javax.servlet.http.HttpServletResponse;

import panda.exts.struts2.views.freemarker.Freemarkers;
import panda.mvc.annotation.view.Ok;
import panda.mvc.view.VoidView;
import panda.servlet.HttpServlets;
import panda.wing.mvc.AbstractAction;

public class FreemarkerAction extends AbstractAction {

	@Ok("ftl:${result}")
	public Object execute() throws Exception {
		String location = HttpServlets.getRelativeURI(getRequest());
		if (!Freemarkers.hasTemplate(location)) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, location);
			return VoidView.INSTANCE;
		}
		return location;
	}

}
