package panda.wing.action.tool;

import panda.mvc.annotation.At;
import panda.wing.action.AbstractAction;

@At("/")
public class PingAction extends AbstractAction {
	@At
	public void ping() {
	}
}
