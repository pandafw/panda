package panda.app.action.base;

import java.io.File;

import panda.app.action.BaseAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;

public abstract class BaseJspAction extends BaseAction {
	@At("(.*)\\.jsp$")
	@To("jsp:${result}")
	public Object jsp(@PathArg String path) {
		String location = "/" + path + ".jsp";
		
		File file = new File(context.getServlet().getRealPath(location));
		return file.exists() ? location : Views.seNotFound(context);
	}

	@At("(.*)\\.sjsp$")
	@To("sjsp:${result}")
	public Object sjsp(@PathArg String path) {
		return jsp(path);
	}
}
