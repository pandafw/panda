package panda.app.util;

import java.util.Map;

import panda.app.constant.MVC;
import panda.ioc.annotation.IocBean;

@IocBean(name=MVC.CACHE_IOCBEAN, type=Map.class, factory="#panda.app.util.AppCacheFactory.getCache")
public interface AppCache {
}
