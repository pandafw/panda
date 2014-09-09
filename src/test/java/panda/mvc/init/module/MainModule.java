package panda.mvc.init.module;

import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;
import panda.mvc.annotation.UrlMappingBy;
import panda.mvc.ioc.provider.ComboIocProvider;

@Modules(scan = true)
@IocBy(type = ComboIocProvider.class, args = { "*json",
		"panda/mvc/init/module/base.js", "*anno",
		"apnda.mvc.init.module" })
@UrlMappingBy(args = { "ioc:myUrlMappingImpl" })
public class MainModule {
}
