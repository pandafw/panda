package panda.ioc.impl;

import panda.ioc.IocEventTrigger;
import panda.ioc.IocMaking;
import panda.ioc.ObjectProxy;
import panda.ioc.ObjectWeaver;

/**
 * dynamic bean object proxy
 */
public class DynamicObjectProxy implements ObjectProxy {
	/**
	 * object weaver
	 */
	private ObjectWeaver weaver;

	/**
	 * fetch event
	 */
	private IocEventTrigger<Object> fetch;

	public DynamicObjectProxy(ObjectWeaver weaver) {
		this.weaver = weaver;
	}

	public void setFetch(IocEventTrigger<Object> fetch) {
		this.fetch = fetch;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, IocMaking im) {
		Object r = weaver.onCreate(weaver.fill(im, weaver.born(im)));
		
		if (fetch != null) {
			fetch.trigger(r);
		}
		
		return (T)r;
	}

	@Override
	public void depose() {
	}
}
