package panda.mvc.testapp.classes.action.ioc;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.mvc.annotation.At;

@IocBean(scope = Scope.REQUEST)
@At("/req")
public class ReqModule {

	@At
	public void me() {
	}
}
