package panda.wing.action;

import panda.wing.action.work.AbstractSyncWorkAction;
import panda.wing.constant.RC;




public abstract class BaseTaskAction extends AbstractSyncWorkAction {

	@Override
	protected void doRunning() {
		addActionMessage(getText(RC.MESSAGE_PROCESSING));
	}

}
