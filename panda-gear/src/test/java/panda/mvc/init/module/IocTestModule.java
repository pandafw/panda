package panda.mvc.init.module;

import javax.servlet.ServletContext;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("/ioc")
@To(value=Views.RAW, fatal=Views.SJSON)
public class IocTestModule {

	@IocInject 
	protected ServletContext servlet;
	
	@IocInject(value="appvalue", required=false)
	protected String appv;
	
	@At
	public String getAppValue() {
		return String.valueOf(appv);
	}
	
	@At
	public String setAppValue(@Param("v") String v) {
		servlet.setAttribute("app.value", v);
		return String.valueOf(v);
	}
}
