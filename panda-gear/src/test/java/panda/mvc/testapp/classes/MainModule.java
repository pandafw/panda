package panda.mvc.testapp.classes;

import panda.mvc.View;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.To;
import panda.mvc.ioc.provider.ComboIocProvider;

@To(all=View.JSON)
@IocBy(type=ComboIocProvider.class, args={"*default", "*json","panda/mvc/testapp/classes/ioc/test.js"})
@Modules(scan = true)
public class MainModule {

}
