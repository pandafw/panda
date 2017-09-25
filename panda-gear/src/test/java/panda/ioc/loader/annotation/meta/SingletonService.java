package panda.ioc.loader.annotation.meta;

import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;

@IocBean(singleton=false, create="create", depose="depose")
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
