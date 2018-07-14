package panda.mvc.testapp.classes;

import panda.mvc.View;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.To;
import panda.mvc.ioc.provider.ComboIocProvider;

@IocBy(type=ComboIocProvider.class, args={ "*json","panda/mvc/testapp/classes/ioc/test.js", "*default" })
@Modules(scan = true)
@To(View.SJSON)
public class MainModule {

}
