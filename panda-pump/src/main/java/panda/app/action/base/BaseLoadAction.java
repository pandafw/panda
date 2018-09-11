package panda.app.action.base;

import panda.app.constant.RES;
import panda.lang.Strings;
import panda.lang.time.DateTimes;

/**
 */
public abstract class BaseLoadAction extends BaseTaskAction {
	private String appKey;
	private String msgKey;
	
	public BaseLoadAction() {
		this(null);
	}

	public BaseLoadAction(String appKey) {
		this(appKey, RES.MESSAGE_PROCESSED);
	}

	public BaseLoadAction(String appKey, String msgKey) {
		super();
		this.appKey = appKey;
		this.msgKey = msgKey;
	}

	protected void doExecute() throws Exception {
		boolean r = doLoad();
		
		addActionMessage(getText(msgKey));
		if (r && Strings.isNotEmpty(appKey)) {
			getApp().put(appKey, DateTimes.getDate());
		}
	}

	protected abstract boolean doLoad() throws Exception;
}
