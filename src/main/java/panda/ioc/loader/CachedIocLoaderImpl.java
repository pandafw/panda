package panda.ioc.loader;

import panda.ioc.IocLoadException;
import panda.ioc.IocLoader;
import panda.ioc.meta.IocObject;

/**
 * 简单的带缓存的IocLoader <b/>仅对singleton == true的IocObject对象进行缓存,
 */
public class CachedIocLoaderImpl extends AbstractIocLoader implements CachedIocLoader {

	private static final IocObject NULL = new IocObject();
	
	public static CachedIocLoaderImpl create(IocLoader proxyIocLoader) {
		return new CachedIocLoaderImpl(proxyIocLoader);
	}

	private IocLoader loader;

	private CachedIocLoaderImpl(IocLoader loader) {
		super();
		
		this.loader = loader;

		// cache names
		for (String n : loader.getNames()) {
			beans.put(n, null);
		}
	}

	@Override
	public void clear() {
		beans.clear();
	}

	public IocObject load(String name) throws IocLoadException {
		IocObject io = beans.get(name);
		if (io == null) {
			io = loader.load(name);
			if (io == null) {
				io = NULL;
			}
			beans.put(name, io);
		}
		return io == NULL ? null : io;
	}

	@Override
	public String toString() {
		return loader.toString();
	}
}
