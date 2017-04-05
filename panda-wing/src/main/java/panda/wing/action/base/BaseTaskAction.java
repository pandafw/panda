package panda.wing.action.base;

import panda.wing.action.work.GenericSyncWorkAction;
import panda.wing.constant.RES;




public abstract class BaseTaskAction extends GenericSyncWorkAction {

	@Override
	protected void doRunning() {
		addActionMessage(getText(RES.MESSAGE_PROCESSING));
	}

}
