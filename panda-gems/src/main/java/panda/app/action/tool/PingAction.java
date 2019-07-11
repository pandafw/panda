package panda.app.action.tool;

import panda.app.action.BaseAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

@At
@To(Views.SJSON)
public class PingAction extends BaseAction {
	@At
	public String ping() {
		return "OK";
	}
}
