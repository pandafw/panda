package panda.app.action.tool;

import panda.app.action.AbstractAction;
import panda.mvc.annotation.At;

@At("/")
public class PingAction extends AbstractAction {
	@At
	public void ping() {
	}
}
