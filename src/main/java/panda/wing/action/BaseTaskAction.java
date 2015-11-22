package panda.wing.action;

import panda.wing.action.work.GenericSyncWorkAction;
import panda.wing.constant.RC;




public abstract class BaseTaskAction extends GenericSyncWorkAction {

	@Override
	protected void doRunning() {
		addActionMessage(getText(RC.MESSAGE_PROCESSING));
	}

}
