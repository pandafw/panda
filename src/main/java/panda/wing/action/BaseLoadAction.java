package panda.wing.action;

import panda.wing.constant.RC;

/**
 */
public abstract class BaseLoadAction extends BaseTaskAction {
	private String msgKey;
	
	public BaseLoadAction() {
		this(RC.MESSAGE_PROCESSED);
	}

	public BaseLoadAction(String msgKey) {
		super();
		this.msgKey = msgKey;
	}

	protected void doExecute() throws Exception {
		doLoad();
		
		addActionMessage(getText(msgKey));
	}

	protected abstract void doLoad() throws Exception;
}
