package panda.wing.action.tool;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.wing.action.AbstractAction;

@At
public class ServletErrorAction extends AbstractAction {

	@IocInject
	FreemarkerHelper freemarker;
	
	@At("/panda/mvc/view/servlet-error.ftl")
	@Ok("ftl:/panda/mvc/view/servlet-error.ftl")
	public void ftl() {
	}

	@At("/panda/mvc/view/servlet-error.sftl")
	@Ok("sftl:/panda/mvc/view/servlet-error.ftl")
	public void sftl() {
	}
}
