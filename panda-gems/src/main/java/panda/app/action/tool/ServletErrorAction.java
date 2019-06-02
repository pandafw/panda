package panda.app.action.tool;

import panda.app.action.AbstractAction;
import panda.io.FileNames;
import panda.lang.Numbers;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.ServletErrorView;
import panda.mvc.view.Views;
import panda.servlet.HttpServlets;

@At("/servlet-error")
public class ServletErrorAction extends AbstractAction {

	private static final String TPL = ServletErrorView.TPL_SERVLET_ERROR;
	
	@At("(.*)$")
	public Object execute(@PathArg String error) {
		Object sc = getRequest().getAttribute(HttpServlets.SERVLET_ERROR_STATUS_CODE);
		if (sc == null) {
			sc = Numbers.toInt(FileNames.getBaseName(error));
			if (sc != null) {
				getRequest().setAttribute(HttpServlets.SERVLET_ERROR_STATUS_CODE, sc);
			}
		}

		String ext = FileNames.getExtension(error);
		if ("sftl".equalsIgnoreCase(ext)) {
			return Views.sftl(context, TPL);
		}

		return Views.ftl(context, TPL);
	}
}
