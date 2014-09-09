package panda.mvc.testapp.classes;

import panda.mvc.annotation.Fail;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Localization;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.Ok;
import panda.mvc.ioc.provider.ComboIocProvider;

@Modules(scan=true)
@Ok("json")
@Fail("json")
@IocBy(type=ComboIocProvider.class,
        args={"*org.nutz.ioc.loader.json.JsonLoader","org/nutz/mvc/testapp/classes/ioc",
              "*org.nutz.ioc.loader.annotation.AnnotationIocLoader","org.nutz.mvc.testapp.classes"})
@Localization("org/nutz/mvc/testapp/classes/message/")
public class MainModule {

}
