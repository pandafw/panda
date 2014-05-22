package panda.ioc.loader.xml.meta;

import panda.ioc.annotation.Inject;
import panda.ioc.annotation.Bean;

@Bean
public class DogMaster {

	@Inject
	public Dog dog;
}
