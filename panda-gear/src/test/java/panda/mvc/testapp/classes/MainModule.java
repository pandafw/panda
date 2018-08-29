package panda.mvc.testapp.classes;

import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.To;
import panda.mvc.ioc.provider.ComboIocProvider;
import panda.mvc.view.Views;

@IocBy(type=ComboIocProvider.class, args={ "*default", "*json","panda/mvc/testapp/classes/ioc/test.js" })
@Modules(scan = true)
@To(Views.SJSON)
public class MainModule {

}
