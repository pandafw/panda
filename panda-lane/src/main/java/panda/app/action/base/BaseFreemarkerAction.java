package panda.app.action.base;

import panda.app.action.AbstractAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

public abstract class BaseFreemarkerAction extends AbstractAction {
	@At("(.*)\\.ftl$")
	@To("ftl:${result}")
	public Object ftl(String path) throws Exception {
		String location = "/" + path + ".ftl";
		if (!assist().hasTemplate(location)) {
			return Views.notFound(context);
		}
		return location;
	}

	@At("(.*)\\.sftl$")
	@To("sftl:${result}")
	public Object sftl(String path) throws Exception {
		return ftl(path);
	}
}
