package panda.wing.auth;

import panda.io.Settings;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.wing.constant.AUTH;
import panda.wing.constant.SC;

public class AuthHelper {
	@IocInject
	protected Settings settings;

	/**
	 * @param u user
	 * @return true - if the user is super
	 */
	public boolean isSuperUser(IUser u) {
		return u != null && Collections.contains(u.getPermits(), AUTH.ALL);
	}

	/**
	 * @return the super user name
	 */
	public String getSuperUsername() {
		return settings.getProperty(SC.SUPER_USERNAME);
	}

	/**
	 * @return the user user password
	 */
	public String getSuperPassword() {
		return settings.getProperty(SC.SUPER_PASSWORD);
	}

	/**
	* @param username username
	* @param password password
	* @return true if username & password equals properties setting
	*/
	public boolean isSuperUser(String username, String password) {
		return (getSuperUsername().equals(username) && getSuperPassword().equals(password));
	}
}
