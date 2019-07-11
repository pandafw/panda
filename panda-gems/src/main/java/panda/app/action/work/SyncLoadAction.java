package panda.app.action.work;

import panda.app.constant.RES;
import panda.lang.Strings;
import panda.lang.time.DateTimes;

/**
 */
public abstract class SyncLoadAction extends SyncWorkAction {
	private String appKey;
	private String msgKey;
	
	public SyncLoadAction() {
		this(null);
	}

	public SyncLoadAction(String appKey) {
		this(appKey, RES.MESSAGE_PROCESSED);
	}

	public SyncLoadAction(String appKey, String msgKey) {
		super();
		this.appKey = appKey;
		this.msgKey = msgKey;
	}

	protected void doExecute() throws Exception {
		boolean r = doLoad();
		if (!r) {
			return;
		}
		
		addActionMessage(getText(msgKey));
		if (Strings.isNotEmpty(appKey)) {
			getApp().put(appKey, DateTimes.getDate());
		}
	}

	protected abstract boolean doLoad() throws Exception;
}
