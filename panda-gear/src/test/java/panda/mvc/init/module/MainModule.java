package panda.mvc.init.module;

import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.ioc.provider.ComboIocProvider;

@Modules(scan = true)
@IocBy(type = ComboIocProvider.class, args = {
	"*anno", "panda.mvc.init.module",
	"*json", "panda/mvc/init/module/base.js", 
	"*default"})
public class MainModule {
}
