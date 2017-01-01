package panda.wing.action;



public abstract class AbstractSyncAction extends AbstractAction {
	protected static final Object lock = new Object();
	
	/**
	 * get singleton instance
	 * @return instance
	 */
	protected AbstractSyncAction getSelf() {
		synchronized (lock) {
			return (AbstractSyncAction)getApp().get(getClass().getName());
		}
	}

	/**
	 * set singleton instance
	 * @param instance instance
	 */
	protected void setSelf(AbstractSyncAction instance) {
		synchronized (lock) {
			if (instance == null) {
				getApp().remove(getClass().getName());
			}
			else {
				getApp().put(getClass().getName(), instance);
			}
		}
	}
	
	protected Object execute() throws Exception {
		AbstractSyncAction aswa = getSelf();
		if (aswa == null) {
			setSelf(this);
			try {
				return doExecute();
			}
			finally {
				setSelf(null);
			}
		}
		else {
			return doRunning();
		}
	}

	protected Object doRunning() {
		addActionMessage(getClass().getSimpleName() + " is running ...");
		return null;
	}

	protected abstract Object doExecute();
}
