package panda.gems.users.action.admin;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.dao.query.DataQuery;
import panda.gems.users.action.UserBulkDisableAction;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/users")
@Auth(AUTH.SUPER)
public class UserBulkDisableExAction extends UserBulkDisableAction {

	public UserBulkDisableExAction() {
		super();
	}

	@Override
	protected void addQueryFilters(DataQuery<User> gq) {
		super.addQueryFilters(gq);
		
		User lu = (User)assist().getLoginUser();

		UserQuery q = new UserQuery(gq);
		q.status().in(VAL.STATUS_ACTIVE).id().ne(lu.getId());
	}

	@Override
	protected User getBulkUpdateSample(List<User> dataList, DataQuery<User> gq) {
		User d = new User();
		d.setStatus(VAL.STATUS_DISABLED);

		gq.excludeAll().include(User.STATUS);

		return d;
	}

}
