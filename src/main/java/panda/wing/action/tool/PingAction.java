package panda.wing.action.tool;

import panda.mvc.annotation.At;
import panda.wing.mvc.AbstractAction;

@At("/admin")
public class PingAction extends AbstractAction {
	@At
	public void ping() {
	}
}
