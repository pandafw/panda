package panda.mvc.init.module;

import panda.ioc.annotation.IocBean;
import panda.mvc.impl.DefaultUrlMapping;

@IocBean
public class MyUrlMappingImpl extends DefaultUrlMapping {

	public MyUrlMappingImpl() {
		System.out.println("I am Here");
	}

}
