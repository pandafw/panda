package panda.mvc.testapp.classes;

import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;
import panda.mvc.ioc.provider.ComboIocProvider;

@Ok("json")
@Fatal("json")
@IocBy(type=ComboIocProvider.class,
        args={"*default", "*json","panda/mvc/testapp/classes/ioc/test.js"})
@Modules(packages={ "panda.dao.sql" }, scan = true)
public class MainModule {

}
