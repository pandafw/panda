package panda.app.action.base;

import panda.app.action.AbstractAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;

public abstract class BaseFreemarkerAction extends AbstractAction {
	@At("(.*)\\.ftl$")
	@To("ftl:${result}")
	public Object ftl(@PathArg String path) {
		String location = "/" + path + ".ftl";
		if (!assist().hasTemplate(location)) {
			return Views.seNotFound(context);
		}
		return location;
	}

	@At("(.*)\\.sftl$")
	@To("sftl:${result}")
	public Object sftl(@PathArg String path) {
		return ftl(path);
	}
}
