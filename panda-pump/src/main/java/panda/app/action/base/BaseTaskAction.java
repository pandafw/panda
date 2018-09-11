package panda.app.action.base;

import panda.app.action.work.GenericSyncWorkAction;
import panda.app.constant.RES;




public abstract class BaseTaskAction extends GenericSyncWorkAction {

	@Override
	protected void doRunning() {
		addActionMessage(getText(RES.MESSAGE_PROCESSING));
	}

}
