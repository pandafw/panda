package panda.gems.users.action.admin;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.dao.query.DataQuery;
import panda.gems.users.action.UserBulkEnableAction;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/users")
@Auth(AUTH.ADMIN)
public class UserBulkEnableExAction extends UserBulkEnableAction {
	public UserBulkEnableExAction() {
		super();
	}

	@Override
	protected void addQueryFilters(DataQuery<User> gq) {
		super.addQueryFilters(gq);
		
		UserQuery q = new UserQuery(gq);
		q.status().in(VAL.STATUS_DISABLED);
	}

	@Override
	protected User getBulkUpdateSample(List<User> dataList, DataQuery<User> gq) {
		User d = new User();
		d.setStatus(VAL.STATUS_ACTIVE);

		gq.excludeAll().include(User.STATUS);

		return d;
	}

}
