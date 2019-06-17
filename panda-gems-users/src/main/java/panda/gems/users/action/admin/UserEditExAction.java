package panda.gems.users.action.admin;

import java.util.HashSet;
import java.util.Set;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.gems.users.ROLE;
import panda.gems.users.action.UserEditAction;
import panda.gems.users.entity.User;
import panda.gems.users.util.PasswordHelper;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/user")
@Auth(AUTH.SUPER)
public class UserEditExAction extends UserEditAction {

	@IocInject
	private PasswordHelper pwHelper;
	
	public UserEditExAction() {
		super();
	}

	public String getDefaultPassword() {
		return pwHelper.getDefaultPassword();
	}
	
	@Override
	protected User prepareData(User data) {
		User pu = super.prepareData(data);
		
		if (Strings.isEmpty(pu.getRole())) {
			pu.setRole(ROLE.USER);
		}
		if (pu.getStatus() == null) {
			pu.setStatus(VAL.STATUS_ACTIVE);
		}

		return pu;
	}

	@Override
	protected User trimData(User d) {
		d.setPassword(null);
		return super.trimData(d);
	}

	@Override
	protected User startInsert(User data) {
		data = super.startInsert(data);

		if (Strings.isEmpty(data.getPassword())) {
			data.setPassword(pwHelper.getDefaultPassword());
		}

		User iu = data.clone();
		iu.setPassword(pwHelper.hashPassword(iu.getPassword()));
		return iu;
	}

	@Override
	protected User startUpdate(User data, User sd) {
		data = super.startUpdate(data, sd);

		User ud = data;
		if (Strings.isNotEmpty(data.getPassword())) {
			ud = data.clone();
			ud.setPassword(pwHelper.hashPassword(ud.getPassword()));
		}
		return ud;
	}

	@Override
	protected Set<String> getUpdateFields(User data, User sd) {
		Set<String> ufs = super.getUpdateFields(data, sd);
		if (Strings.isEmpty(data.getPassword())) {
			ufs = new HashSet<String>(ufs);
			ufs.remove(User.PASSWORD);
		}

		return ufs;
	}
}
