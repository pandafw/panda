package panda.app.action.tool;

import panda.app.action.AbstractAction;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;

@At
@To(all=View.JSON)
public class PingAction extends AbstractAction {
	@At
	public String ping() {
		return "OK";
	}
}
