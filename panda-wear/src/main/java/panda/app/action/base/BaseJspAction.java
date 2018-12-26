package panda.app.action.base;

import java.io.File;

import panda.app.action.AbstractAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

public abstract class BaseJspAction extends AbstractAction {
	@At("(.*)\\.jsp$")
	@To("jsp:${result}")
	public Object jsp(String path) {
		String location = "/" + path + ".jsp";
		
		File file = new File(context.getServlet().getRealPath(location));
		return file.exists() ? location : Views.seNotFound(context);
	}

	@At("(.*)\\.sjsp$")
	@To("sjsp:${result}")
	public Object sjsp(String path) {
		return jsp(path);
	}
}
