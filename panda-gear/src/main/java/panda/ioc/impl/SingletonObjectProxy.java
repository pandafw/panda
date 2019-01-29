package panda.ioc.impl;

import panda.ioc.IocEventTrigger;
import panda.ioc.IocMaking;
import panda.ioc.ObjectProxy;
import panda.lang.Exceptions;

/**
 * ObjectProxy for singleton bean
 */
public class SingletonObjectProxy implements ObjectProxy {
	/**
	 * bean instance
	 */
	private Object object;

	/**
	 * fetch event
	 */
	private IocEventTrigger<Object> fetch;

	/**
	 * depose event
	 */
	private IocEventTrigger<Object> depose;

	public SingletonObjectProxy() {
	}

	public SingletonObjectProxy(Object obj) {
		this.object = obj;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @param fetch the fetch to set
	 */
	public void setFetch(IocEventTrigger<Object> fetch) {
		this.fetch = fetch;
	}

	/**
	 * @param depose the depose to set
	 */
	public void setDepose(IocEventTrigger<Object> depose) {
		this.depose = depose;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, IocMaking imak) {
		if (object == null) {
			throw Exceptions.makeThrow("Null '%s' Object for SingletonObjectProxy", imak.getName());
		}

		if (fetch != null) {
			fetch.trigger(object);
		}
		
		return (T)object;
	}

	@Override
	public void depose() {
		if (depose != null && object != null) {
			depose.trigger(object);
		}
	}
}
