package panda.ioc.bean;

import java.util.Set;

import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.IocException;

public class IocProxy implements Ioc {
	private Ioc ioc;
	
	/**
	 * @param ioc the IOC
	 */
	public IocProxy(Ioc ioc) {
		this.ioc = ioc;
	}

	/**
	 * @return the ioc
	 */
	public Ioc getIoc() {
		return ioc;
	}

	/**
	 * @param ioc the ioc to set
	 */
	public void setIoc(Ioc ioc) {
		this.ioc = ioc;
	}

	/**
	 * @return the context
	 */
	@Override
	public IocContext getContext() {
		return ioc.getContext();
	}

	@Override
	public <T> T get(Class<T> type) throws IocException {
		return ioc.get(type);
	}

	@Override
	public <T> T get(Class<T> type, String name) throws IocException {
		return ioc.get(type, name);
	}

	@Override
	public <T> T getIfExists(Class<T> type) throws IocException {
		return ioc.getIfExists(type);
	}

	@Override
	public <T> T getIfExists(Class<T> type, String name) throws IocException {
		return ioc.getIfExists(type, name);
	}

	@Override
	public boolean has(Class<?> type, String name) throws IocException {
		return ioc.has(type, name);
	}

	@Override
	public boolean has(Class<?> type) throws IocException {
		return ioc.has(type);
	}

	@Override
	public boolean has(String name) throws IocException {
		return ioc.has(name);
	}

	@Override
	public Set<String> getNames() {
		return ioc.getNames();
	}

	@Override
	public void reset() {
		ioc.reset();
	}

	@Override
	public void depose() {
		ioc.depose();
	}

}
