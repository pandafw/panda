package panda.ioc.impl;

import panda.ioc.IocEventTrigger;
import panda.ioc.IocMaking;
import panda.ioc.ObjectProxy;
import panda.ioc.ObjectWeaver;

/**
 * dynamic bean object proxy
 */
public class DynamicObjectProxy implements ObjectProxy {
	private ThreadLocal<Object> local = new ThreadLocal<Object>();
	
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
		Object o = local.get();

		if (o == null) {
			o = weaver.born(im);
			
			// avoid reference loop
			local.set(o);

			try {
				weaver.fill(im, o);
				
				weaver.onCreate(o);
			}
			finally {
				local.remove();
			}
		}
		
		if (fetch != null) {
			fetch.trigger(o);
		}
		
		return (T)o;
	}

	@Override
	public void depose() {
	}
}
