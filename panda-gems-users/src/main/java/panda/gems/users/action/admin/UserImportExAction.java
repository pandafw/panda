package panda.gems.users.action.admin;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.users.action.UserImportAction;
import panda.gems.users.entity.User;
import panda.gems.users.util.PasswordHelper;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/user")
@Auth(AUTH.SUPER)
public class UserImportExAction extends UserImportAction {

	@IocInject
	private PasswordHelper pwHelper;
	
	private int pwdhashLength = -1;
	
	public UserImportExAction() {
		super();
	}

	protected void checkNotNulls(User data) {
		// disable pwd null check
		String pwd = data.getPassword();
		data.setPassword("-");
		try {
			super.checkNotNulls(data);
		}
		finally {
			data.setPassword(pwd);
		}
	}

	protected int getPasswordHashLength() {
		if (pwdhashLength < 0) {
			pwdhashLength = pwHelper.getPasswordHashLength();
		}
		return pwdhashLength;
	}
	
	@Override
	protected void insertData(User data) {
		if (Strings.isEmpty(data.getPassword())) {
			data.setPassword(pwHelper.hashPassword(pwHelper.getDefaultPassword()));
		}
		else if (data.getPassword().length() != pwdhashLength) {
			data.setPassword(pwHelper.hashPassword(data.getPassword()));
		}

		super.insertData(data);
	}
}
