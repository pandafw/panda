package panda.ioc.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.ioc.IocContext;
import panda.ioc.ObjectProxy;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * 自定义级别上下文对象
 */
public class ScopeContext implements IocContext {

	private static final Log log = Logs.getLog(ScopeContext.class);

	private String scope;
	private Map<String, ObjectProxy> objs;

	public ScopeContext(String scope) {
		this.scope = scope;
		objs = new HashMap<String, ObjectProxy>();
	}

	private void checkBuffer() {
		if (null == objs) {
			throw Exceptions.makeThrow("Context '%s' had been deposed!", scope);
		}
	}

	public Map<String, ObjectProxy> getObjs() {
		return objs;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public ObjectProxy fetch(String name) {
		checkBuffer();
		return objs.get(name);
	}

	public boolean save(String scope, String name, ObjectProxy obj) {
		if (accept(scope)) {
			checkBuffer();
			synchronized (this) {
				if (!objs.containsKey(name)) {
					if (log.isDebugEnabled()) {
						log.debugf("Save object '%s' to [%s] ", name, scope);
					}
					return null != objs.put(name, obj);
				}
			}
		}
		return false;
	}

	protected boolean accept(String scope) {
		return null != scope && this.scope.equals(scope);
	}

	public boolean remove(String scope, String name) {
		if (accept(scope)) {
			checkBuffer();

			synchronized (this) {
				if (objs.containsKey(name)) {
					if (log.isDebugEnabled()) {
						log.debugf("Remove object '%s' from [%s] ", name, scope);
					}
					return null != objs.remove(name);
				}
			}
		}
		return false;
	}

	public void clear() {
		checkBuffer();
		for (Entry<String, ObjectProxy> en : objs.entrySet()) {
			if (log.isDebugEnabled()) {
				log.debugf("Depose object '%s' ...", en.getKey());
			}
			en.getValue().depose();
		}
		objs.clear();
	}

	public void depose() {
		if (objs != null) {
			clear();
			objs = null;
		}
		else {
			if (log.isWarnEnabled()) {
				log.warn("can't depose twice , skip");
			}
		}
	}

}
