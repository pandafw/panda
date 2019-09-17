package ${package}.action;

import panda.app.action.BaseAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;


@At("/")
@To(Views.SFTL)
public class IndexAction extends BaseAction {
	@At({ "", "index"})
	public void index() {
	}
}
