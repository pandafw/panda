package panda.wing.action.work;

import java.util.HashMap;
import java.util.Map;

import panda.lang.Strings;


public abstract class GenericMultiWorkAction extends GenericSyncWorkAction {
	protected String key;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = Strings.stripToNull(key);
	}

	protected Map getActionMap(boolean create) {
		Map m = (Map)getApp().get(getActionKey());
		if (m == null && create) {
			m = new HashMap();
			getApp().put(getActionKey(), m);
		}
		return m;
	}
	
	/**
	 * get singleton instance
	 * @return instance
	 */
	@Override
	protected GenericSyncWorkAction getSelf() {
		synchronized (lock) {
			Map m = getActionMap(false);
			if (m == null) {
				return null;
			}
		
			return (GenericSyncWorkAction)m.get(key);
		}
	}

	/**
	 * set singleton instance
	 * @param instance instance
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void setSelf(GenericSyncWorkAction instance) {
		synchronized (lock) {
			if (instance == null) {
				Map m = getActionMap(false);
				if (m != null) {
					m.remove(key);
				}
			}
			else {
				Map m = getActionMap(true);
				m.put(key, instance);
			}
		}
	}
}
