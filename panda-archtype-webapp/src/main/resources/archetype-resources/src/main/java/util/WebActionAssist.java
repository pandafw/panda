package ${package}.util;

import panda.app.util.AppActionAssist;
import panda.gems.users.entity.User;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.mvc.util.ActionAssist;


@IocBean(type=ActionAssist.class, scope=Scope.REQUEST)
public class WebActionAssist extends AppActionAssist {
	@Override
	public User getLoginUser() {
		return (User)super.getLoginUser();
	}
}
