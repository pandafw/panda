package panda.ioc.impl;

import java.util.ArrayList;
import java.util.List;

import panda.ioc.IocContext;
import panda.ioc.ObjectProxy;

/**
 * 组合了一组 IocContext。每当保存（save）时，会存入所有的Context。
 * <p>
 * 每当获取时 按照构造Context的顺序，依次获取。 只要有一个 Context 返回了非 null 对象，就立即返回
 * 
 */
public class ComboIocContext implements IocContext {

	private List<IocContext> contexts = new ArrayList<IocContext>();

	/**
	 * Context 的获取优先级，以数组的顺序来决定
	 * 
	 * @param contexts
	 */
	public ComboIocContext(IocContext... contexts) {
		if (contexts != null) {
			for (IocContext ic : contexts) {
				addContext(ic);
			}
		}
	}

	public boolean isEmpty() {
		return contexts.isEmpty();
	}
	
	public void addContext(IocContext ic) {
		if (ic instanceof ComboIocContext) {
			ComboIocContext cc = (ComboIocContext)ic;
			for (IocContext ic2 : cc.contexts) {
				addContext(ic2);
			}
		}
		else {
			contexts.remove(ic);
			contexts.add(ic);
		}
	}

	public ObjectProxy fetch(String key) {
		for (IocContext c : contexts) {
			ObjectProxy re = c.fetch(key);
			if (null != re) {
				return re;
			}
		}
		return null;
	}

	public boolean save(String scope, String name, ObjectProxy obj) {
		for (IocContext c : contexts) {
			if (c.save(scope, name, obj)) {
				return true;
			}
		}
		return false;
	}

	public boolean remove(String scope, String name) {
		boolean re = false;
		for (IocContext c : contexts) {
			if (c.remove(scope, name)) {
				re = true;
			}
		}
		return re;
	}

	public void clear() {
		for (IocContext c : contexts) {
			c.clear();
		}
	}

	public void depose() {
		for (IocContext c : contexts) {
			c.depose();
		}
	}
}
