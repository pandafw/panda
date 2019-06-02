package panda.app.util;

import panda.dao.DaoClient;
import panda.ioc.annotation.IocBean;

@IocBean(type=DaoClient.class, factory="#panda.app.util.AppDaoClientFactory.getDaoClient")
public interface AppDaoClient {
}
