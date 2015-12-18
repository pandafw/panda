package panda.ioc.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import panda.ioc.IocContext;
import panda.ioc.ObjectProxy;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * 自定义级别上下文对象
 */
public class ScopeIocContext implements IocContext {
	private static final Log log = Logs.getLog(ScopeIocContext.class);

	private String scope;
	private Map<String, ObjectProxy> objs;

	public ScopeIocContext(String scope) {
		this.scope = scope;
		objs = new ConcurrentHashMap<String, ObjectProxy>();
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
			if (log.isDebugEnabled()) {
				log.debugf("Save object '%s' to [%s] ", name, scope);
			}
			objs.put(name, obj);
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

			ObjectProxy op = objs.remove(name);
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
		if (objs == null) {
			if (log.isWarnEnabled()) {
				log.warnf("%s IocContext already deposed", scope);
			}
			return;
		}
		
		clear();
		objs = null;
	}

}
