package panda.wing.action.tool;

import panda.io.FileNames;
import panda.lang.Numbers;
import panda.mvc.annotation.At;
import panda.mvc.view.FreemarkerView;
import panda.mvc.view.SitemeshFreemarkerView;
import panda.servlet.HttpServlets;
import panda.wing.action.AbstractAction;

@At("/servlet-error")
public class ServletErrorAction extends AbstractAction {

	private static final String TPL = "/panda/mvc/view/servlet-error.ftl";
	
	@At("*")
	public Object execute(String error) {
		Object sc = getRequest().getAttribute(HttpServlets.ERROR_STATUS_CODE_ATTRIBUTE);
		if (sc == null) {
			sc = Numbers.toInt(FileNames.getBaseName(error));
			if (sc != null) {
				getRequest().setAttribute(HttpServlets.ERROR_STATUS_CODE_ATTRIBUTE, sc);
			}
		}

		String ext = FileNames.getExtension(error);
		if ("sftl".equalsIgnoreCase(ext)) {
			return new SitemeshFreemarkerView(TPL);
		}
		return new FreemarkerView(TPL);
	}
}
