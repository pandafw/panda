package panda.ioc.loader.xml.meta;

import panda.ioc.annotation.Bean;
import panda.lang.Exceptions;

@Bean(singleton=false, create="create", depose="depose")
public class SingletonService {
	
	public static int CreateCount;
	public static int DeposeCount;
	
	public void create() {
		CreateCount ++; 
	}

	public String depose() {
		DeposeCount++;
		throw Exceptions.impossible();
	}
}
