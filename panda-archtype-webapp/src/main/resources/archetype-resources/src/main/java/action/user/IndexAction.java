package ${package}.action.user;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

@At("/user")
@Auth(AUTH.SIGNIN)
@To(Views.SFTL)
public class IndexAction extends BaseAction {

	@At({ "", "index"})
	public void index() {
	}
}
