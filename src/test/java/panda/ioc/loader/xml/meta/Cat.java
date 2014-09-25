package panda.ioc.loader.xml.meta;

import java.util.Calendar;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class Cat {

	@IocInject
	public Calendar dummy;
	
}
