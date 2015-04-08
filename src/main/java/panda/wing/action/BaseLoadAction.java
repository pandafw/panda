package panda.wing.action;

import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.wing.constant.RC;

/**
 */
public abstract class BaseLoadAction extends BaseTaskAction {
	private String appKey;
	private String msgKey;
	
	public BaseLoadAction() {
		this(null);
	}

	public BaseLoadAction(String appKey) {
		this(appKey, RC.MESSAGE_PROCESSED);
	}

	public BaseLoadAction(String appKey, String msgKey) {
		super();
		this.appKey = appKey;
		this.msgKey = msgKey;
	}

	protected void doExecute() throws Exception {
		doLoad();
		
		addActionMessage(getText(msgKey));
		if (Strings.isNotEmpty(appKey)) {
			getApp().put(appKey, DateTimes.getDate());
		}
	}

	protected abstract void doLoad() throws Exception;
}
