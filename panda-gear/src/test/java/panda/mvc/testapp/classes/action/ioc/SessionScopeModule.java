package panda.mvc.testapp.classes.action.ioc;

import panda.ioc.annotation.IocBean;
import panda.mvc.annotation.At;

@IocBean(scope = "session")
@At("/session")
public class SessionScopeModule {

	@At
	public void me() {
	}
}
