package panda.wing.action.tool;

import panda.io.FileNames;
import panda.lang.Numbers;
import panda.mvc.annotation.At;
import panda.mvc.view.FreemarkerView;
import panda.mvc.view.ServletErrorView;
import panda.mvc.view.SitemeshFreemarkerView;
import panda.servlet.HttpServlets;
import panda.wing.action.AbstractAction;

@At("/servlet-error")
public class ServletErrorAction extends AbstractAction {

	private static final String TPL = ServletErrorView.TPL;
	
	@At("(.*)$")
	public Object execute(String error) {
		Object sc = getRequest().getAttribute(HttpServlets.SERVLET_ERROR_STATUS_CODE);
		if (sc == null) {
			sc = Numbers.toInt(FileNames.getBaseName(error));
			if (sc != null) {
				getRequest().setAttribute(HttpServlets.SERVLET_ERROR_STATUS_CODE, sc);
			}
		}

		String ext = FileNames.getExtension(error);
		if ("sftl".equalsIgnoreCase(ext)) {
			return new SitemeshFreemarkerView(TPL);
		}
		return new FreemarkerView(TPL);
	}
}
