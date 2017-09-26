package panda.app.util;

import java.util.Map;

import panda.app.AppConstants;
import panda.ioc.annotation.IocBean;

@IocBean(name=AppConstants.CACHE_IOCBEAN, type=Map.class, factory="#panda.app.util.AppCacheFactory.getCache")
public interface AppCache {
}
