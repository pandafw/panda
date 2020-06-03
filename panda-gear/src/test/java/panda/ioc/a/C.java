package panda.ioc.a;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class C {
	@IocInject
	public A a;

	public B b;

	@IocInject
	public void setB(B b) {
		this.b = b;
	}
}
