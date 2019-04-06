package panda.app.action.base;

import panda.app.action.AbstractAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;

public abstract class BaseHtmlAction extends AbstractAction {
	@At("(.*)\\.htm$")
	@To(Views.RES)
	public Object htm(@PathArg String path) throws Exception {
		return html(path, ".htm");
	}

	@At("(.*)\\.shtm$")
	@To(Views.SRES)
	public Object shtm(@PathArg String path) throws Exception {
		return html(path, ".htm");
	}

	@At("(.*)\\.html$")
	@To(Views.RES)
	public Object html(@PathArg String path) throws Exception {
		return html(path, ".html");
	}

	@At("(.*)\\.shtml$")
	@To(Views.SRES)
	public Object shtml(@PathArg String path) {
		return html(path, ".html");
	}
	
	protected Object html(String path, String ext) {
		return "/" + path + ext;
	}
}
