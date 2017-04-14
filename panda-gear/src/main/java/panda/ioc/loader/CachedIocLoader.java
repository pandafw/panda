package panda.ioc.loader;

import panda.ioc.IocLoader;

/**
 * IocLoader with cache
 */
public interface CachedIocLoader extends IocLoader {

	void clear();

}
