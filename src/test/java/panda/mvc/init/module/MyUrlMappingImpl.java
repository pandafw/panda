package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.impl.RegexUrlMapping;

@IocBean
public class MyUrlMappingImpl extends RegexUrlMapping {

	public MyUrlMappingImpl() {
		System.out.println("I am Here");
	}

}
