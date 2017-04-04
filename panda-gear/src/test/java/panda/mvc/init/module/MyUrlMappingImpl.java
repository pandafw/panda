package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.impl.RegexActionMapping;

@IocBean
public class MyUrlMappingImpl extends RegexActionMapping {

	public MyUrlMappingImpl() {
		System.out.println("I am Here");
	}

}
