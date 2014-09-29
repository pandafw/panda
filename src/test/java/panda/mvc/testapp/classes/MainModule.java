package panda.mvc.testapp.classes;

import panda.mvc.annotation.Fail;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.Ok;
import panda.mvc.ioc.provider.ComboIocProvider;

@Modules(scan=true)
@Ok("json")
@Fail("json")
@IocBy(type=ComboIocProvider.class,
        args={"*panda.ioc.loader.JsonIocLoader","panda/mvc/testapp/classes/ioc/test.js",
              "*panda.mvc.ioc.loader.MvcAnnotationIocLoader","panda.mvc.testapp.classes", "panda.filepool.dao", "panda.dao.sql"})
public class MainModule {

}
