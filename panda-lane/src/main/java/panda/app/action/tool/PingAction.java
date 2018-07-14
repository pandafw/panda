package panda.app.action.tool;

import panda.app.action.AbstractAction;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

@At
@To(Views.SJSON)
public class PingAction extends AbstractAction {
	@At
	public String ping() {
		return "OK";
	}
}
