package panda.ioc.loader.annotation.meta;

import java.io.InputStream;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class ClassB {

	@IocInject
	public ClassA a;

	@IocInject
	public ClassC c;

	@IocInject(type=ClassC.class)
	public ClassCC cc;

	@IocInject
	public ClassCC cc2;
	
	@IocInject(value="xxx", required=false)
	public String xxx = "default";

	@IocInject(type=InputStream.class, required=false)
	public InputStream yyy;

}
