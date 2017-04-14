package panda.ioc.a;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class C {
	@IocInject
	public void setB(B b) {
		System.out.println(b.getClass());
	}
	
	@IocInject
	public void setA(A a) {
		System.out.println(a.getClass());
	}
}
