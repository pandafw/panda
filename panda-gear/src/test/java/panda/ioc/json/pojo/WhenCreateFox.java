package panda.ioc.json.pojo;

import panda.ioc.IocEventTrigger;

public class WhenCreateFox implements IocEventTrigger<Animal> {

	public void trigger(Animal obj) {
		obj.setName("$" + obj.getName());
	}

}
