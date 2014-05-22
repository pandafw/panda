package panda.ioc.loader.xml.meta;

import panda.ioc.annotation.Bean;

@Bean
public class Dog {

	public Dog() {
		// 总是抛出异常,这个bean就总无法被创建
		throw new RuntimeException();
	}
}
