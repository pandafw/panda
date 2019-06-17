package panda.gems.users.util;

import panda.gems.users.S;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.util.crypto.Digests;

@IocBean(create="initialize")
public class PasswordHelper {
	private int passwordHashLength;
	
	@IocInject
	private Settings settings;

	public void initialize() {
		passwordHashLength = hashPassword(getDefaultPassword()).length();
	}
	
	public String hashPassword(String password) {
		return Digests.sha256Hex(password);
	}

	public String getDefaultPassword() {
		return settings.getProperty(S.DEFAULT_PASSWORD, "trustme");
	}
	
	public int getPasswordHashLength() {
		return passwordHashLength;
	}
}
