package panda.ioc.a;

import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean(singleton=false, create="onCreate", fetch="onFetch", depose="onDespose")
public class D {
	@IocInject("$ioc")
	public Ioc ioc;

	@IocInject("$name")
	public String name;

	@IocInject("!{'a': 0, 'b': 1}")
	public String jo;

	@IocInject("![ 0, 1 ]")
	public String ja;
	
	public void onCreate() {
		System.out.println("onCreate(): " + this);
	}
	
	public void onFetch() {
		System.out.println("onFetch(): " + this);
	}
	
	public void onDepose() {
		System.out.println("onDepose(): " + this);
	}
}
