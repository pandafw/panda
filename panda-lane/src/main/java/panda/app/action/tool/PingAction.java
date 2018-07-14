package panda.app.action.tool;

import panda.app.action.AbstractAction;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;

@At
@To(View.SJSON)
public class PingAction extends AbstractAction {
	@At
	public String ping() {
		return "OK";
	}
}
