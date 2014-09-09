package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.impl.UrlMappingImpl;

@IocBean
public class MyUrlMappingImpl extends UrlMappingImpl {

	public MyUrlMappingImpl() {
		System.out.println("I am Here");
	}

}
