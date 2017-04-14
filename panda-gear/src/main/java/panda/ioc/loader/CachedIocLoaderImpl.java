package panda.ioc.loader;

import panda.ioc.IocLoadException;
import panda.ioc.IocLoader;
import panda.ioc.meta.IocObject;

/**
 * A simple IocLoader with cache
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
			beans.put(n, NULL);
		}
	}

	@Override
	public void clear() {
		beans.clear();
	}

	public IocObject load(String name) throws IocLoadException {
		IocObject io = beans.get(name);
		if (io == NULL) {
			io = loader.load(name);
			if (io != null) {
				beans.put(name, io);
			}
		}
		return io;
	}

	@Override
	public String toString() {
		return loader.toString();
	}
}
