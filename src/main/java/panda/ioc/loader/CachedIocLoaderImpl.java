package panda.ioc.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import panda.ioc.IocLoader;
import panda.ioc.IocLoading;
import panda.ioc.IocLoadException;
import panda.ioc.meta.IocObject;

/**
 * 简单的带缓存的IocLoader <b/>仅对singleton == true的IocObject对象进行缓存,
 */
public class CachedIocLoaderImpl implements CachedIocLoader {

	public static CachedIocLoaderImpl create(IocLoader proxyIocLoader) {
		return new CachedIocLoaderImpl(proxyIocLoader);
	}

	private IocLoader loader;

	private Map<String, IocObject> cache;

	private CachedIocLoaderImpl(IocLoader loader) {
		this.loader = loader;
		this.cache = new HashMap<String, IocObject>();
	}

	public void clear() {
		cache.clear();
	}

	public Set<String> getNames() {
		return loader.getNames();
	}

	public boolean has(String name) {
		return loader.has(name);
	}

	public IocObject load(IocLoading loading, String name) throws IocLoadException {
		IocObject iocObject = cache.get(name);
		if (iocObject == null) {
			iocObject = loader.load(loading, name);
			if (iocObject == null) {
				return null;
			}
			if (iocObject.isSingleton() && iocObject.getType() != null) {
				cache.put(name, iocObject);
			}
		}
		return iocObject;
	}

	@Override
	public String toString() {
		return loader.toString();
	}
}
