package panda.ioc.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import panda.ioc.IocContext;
import panda.ioc.ObjectProxy;
import panda.log.Log;
import panda.log.Logs;

public class ScopeIocContext implements IocContext {
	private static final Log log = Logs.getLog(ScopeIocContext.class);

	private String scope;
	private Map<String, ObjectProxy> proxys;

	public ScopeIocContext(String scope) {
		this.scope = scope;
		proxys = new ConcurrentHashMap<String, ObjectProxy>();
	}

	private void checkBuffer() {
		if (proxys == null) {
			throw new IllegalStateException("IocContext '" + scope + "' had been deposed!");
		}
	}

	public Map<String, ObjectProxy> getProxys() {
		return proxys;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public ObjectProxy fetch(String name) {
		checkBuffer();
		return proxys.get(name);
	}

	public boolean save(String scope, String name, ObjectProxy op) {
		if (accept(scope)) {
			checkBuffer();
			if (log.isDebugEnabled()) {
				log.debugf("Save object '%s' to [%s] ", name, scope);
			}
			proxys.put(name, op);
			return true;
		}
		return false;
	}

	protected boolean accept(String scope) {
		return this.scope.equals(scope);
	}

	public boolean remove(String scope, String name) {
		if (accept(scope)) {
			checkBuffer();

			ObjectProxy op = proxys.remove(name);
			if (op != null) {
				if (log.isDebugEnabled()) {
					log.debugf("Depose object '%s' ...", name);
				}
				op.depose();
				return true;
			}
		}
		return false;
	}

	public synchronized void clear() {
		checkBuffer();
		for (Entry<String, ObjectProxy> en : proxys.entrySet()) {
			if (log.isDebugEnabled()) {
				log.debugf("Depose object '%s' ...", en.getKey());
			}
			en.getValue().depose();
		}
		proxys.clear();
	}

	public void depose() {
		if (proxys == null) {
			if (log.isWarnEnabled()) {
				log.warn(scope + " IocContext already deposed");
			}
			return;
		}
		
		clear();
		proxys = null;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + scope + '[' + proxys.size() + "]: " + proxys;
	}

}
