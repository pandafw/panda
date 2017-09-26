package panda.app.util;

import panda.dao.Dao;
import panda.ioc.annotation.IocBean;

@IocBean(type=Dao.class, singleton=false, factory="#panda.dao.DaoClient.getDao")
public interface AppDao {
}
