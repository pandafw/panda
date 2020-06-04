package panda.ioc.a;

import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;

@IocBean(singleton=false, create="onCreate", fetch="onFetch", depose="onDespose")
public class D1 {
	@IocInject("$ioc")
	public Ioc ioc;

	@IocInject("$name")
	public String name;

	@IocInject("!{'a': 0, 'b': 1}")
	public String jo;

	@IocInject("![ 0, 1 ]")
	public String ja;
	
	@IocInject
	public D1 self;
	
	@IocInject
	public D2 d2;
	
	public void onCreate() {
//		System.out.println("onCreate(): " + this);
	}
	
	public void onFetch() {
//		System.out.println("onFetch(): " + this);
	}
	
	public void onDepose() {
		throw Exceptions.impossible();
	}
}
