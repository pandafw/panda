package panda.ioc.json.pojo;

import panda.ioc.aop.Aop;

public class Mammal extends Animal {

	@Aop({ "lst" })
	public void ohGod(Throwable e) throws Throwable {
		throw e;
	}

	@Aop("lst")
	public String getName() {
		// new Throwable().printStackTrace();
		return super.getName();
	}

	@Aop("lst")
	public void setAge(int age) {
		super.setAge(age);
	}

}
