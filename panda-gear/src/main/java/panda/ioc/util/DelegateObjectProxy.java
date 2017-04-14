package panda.ioc.util;

import panda.ioc.Ioc;
import panda.ioc.IocMaking;
import panda.ioc.ObjectProxy;

public class DelegateObjectProxy implements ObjectProxy {
	private Ioc ioc;
	private Class<? extends ObjectProvider> provider;

	/**
	 * @param ioc IOC
	 * @param provider provider type
	 */
	public DelegateObjectProxy(Ioc ioc, Class<? extends ObjectProvider> provider) {
		super();
		this.ioc = ioc;
		this.provider = provider;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, IocMaking ing) {
		ObjectProvider<T> op = ioc.get(provider);
		return op.getObject();
	}

	@Override
	public void depose() {
	}
	
}
