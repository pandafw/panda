package panda.wing.auth;

import panda.io.Settings;
import panda.ioc.annotation.IocInject;
import panda.wing.constant.SC;
import panda.wing.constant.VC;

public class AuthHelper {
	@IocInject
	protected Settings settings;

	/**
	 * @param u user
	 * @return true - if the user is super
	 */
	public boolean isSuperUser(IUser u) {
		return u != null && VC.SUPER_LEVEL == u.getGroupLevel();
	}

	/**
	 * @param u user
	 * @return true - if the user is admin
	 */
	public boolean isAdminUser(IUser u) {
		return u != null && VC.ADMIN_LEVEL >= u.getGroupLevel();
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
