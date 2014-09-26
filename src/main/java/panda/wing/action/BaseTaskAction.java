package panda.wing.action;

import panda.wing.action.work.AbstractSyncWorkAction;
import panda.wing.constant.RC;




public abstract class BaseTaskAction extends AbstractSyncWorkAction {
	/**
	 * @return the assist
	 */
	protected BaseActionAssist assist() {
		return (BaseActionAssist)super.getAssist();
	}

	/**
	 * @return the consts
	 */
	protected BaseActionConsts consts() {
		return (BaseActionConsts)super.getConsts();
	}
	

	@Override
	protected void doRunning() {
		addActionMessage(getText(RC.MESSAGE_PROCESSING));
	}

}
