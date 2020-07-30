package panda.gems.users.action.admin;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.users.action.UserImportAction;
import panda.gems.users.entity.User;
import panda.gems.users.util.PasswordHelper;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/users")
@Auth(AUTH.ADMIN)
public class UserImportExAction extends UserImportAction {

	@IocInject
	private PasswordHelper pwHelper;
	
	private int pwHashLength = -1;
	
	public UserImportExAction() {
		super();
	}

	protected void checkNotNulls(User data) {
		// disable password null check
		String pw = data.getPassword();
		data.setPassword("-");
		try {
			super.checkNotNulls(data);
		}
		finally {
			data.setPassword(pw);
		}
	}

	protected int getPasswordHashLength() {
		if (pwHashLength < 0) {
			pwHashLength = pwHelper.getPasswordHashLength();
		}
		return pwHashLength;
	}
	
	@Override
	protected void insertData(User data) {
		if (Strings.isEmpty(data.getPassword())) {
			data.setPassword(pwHelper.hashPassword(pwHelper.getDefaultPassword()));
		}
		else if (data.getPassword().length() != getPasswordHashLength()) {
			data.setPassword(pwHelper.hashPassword(data.getPassword()));
		}

		super.insertData(data);
	}
}
