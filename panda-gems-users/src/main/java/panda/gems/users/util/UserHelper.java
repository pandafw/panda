package panda.gems.users.util;

import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.app.entity.ICreatedBy;
import panda.app.entity.IUpdatedBy;
import panda.dao.DaoClient;
import panda.dao.entity.EntityDao;
import panda.gems.users.S;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;

@IocBean
public class UserHelper {
	private static final Log log = Logs.getLog(UserHelper.class);
	
	public static final String SET_DEFAULT_PWD = "users-default-password";

	@IocInject
	private DaoClient daoClient;
	
	@IocInject
	private Settings settings;

	@IocInject
	private PasswordHelper pwHelper;
	
	public void initSystemUser() {
		try {
			EntityDao<User> udao = daoClient.getEntityDao(User.class);
			if (udao.fetch(VAL.SYSTEM_UID) != null) {
				return;
			}
			
			User u = new User();
			u.setId(VAL.SYSTEM_UID);
			u.setEmail("SYSTEM");
			u.setName("SYSTEM");
			u.setPassword(pwHelper.hashPassword(Randoms.randString(10)));
			u.setRole(AUTH.NONE);
			u.setStatus(VAL.STATUS_DISABLED);
			if (u instanceof ICreatedBy) {
				ICreatedBy c = (ICreatedBy)u;
				c.setCreatedBy(VAL.SYSTEM_UID);
				c.setCreatedAt(DateTimes.getDate());
			}
			if (u instanceof IUpdatedBy) {
				IUpdatedBy c = (IUpdatedBy)u;
				c.setUpdatedBy(VAL.SYSTEM_UID);
				c.setUpdatedAt(DateTimes.getDate());
			}
			
			log.info("Create default system user");
			udao.insert(u);
		}
		catch (Exception e) {
			log.error("Failed to init system user", e);
		}
	}

	public void initSuperUser() {
		try {
			if (Strings.isEmpty(settings.getProperty(S.SUPER_EMAIL))) {
				return;
			}
			
			EntityDao<User> udao = daoClient.getEntityDao(User.class);
			UserQuery uq = new UserQuery();
			uq.role().eq(AUTH.SUPER).limit(1);
			if (udao.fetch(uq) != null) {
				return;
			}
			
			User u = new User();
			u.setEmail(settings.getProperty(S.SUPER_EMAIL));
			u.setName(settings.getProperty(S.SUPER_USERNAME, "SUPER"));
			u.setPassword(pwHelper.hashPassword(settings.getProperty(S.SUPER_PASSWORD, "trustme")));
			u.setRole(AUTH.SUPER);
			u.setStatus(VAL.STATUS_ACTIVE);
			if (u instanceof ICreatedBy) {
				ICreatedBy c = (ICreatedBy)u;
				c.setCreatedBy(VAL.SYSTEM_UID);
				c.setCreatedAt(DateTimes.getDate());
			}
			if (u instanceof IUpdatedBy) {
				IUpdatedBy c = (IUpdatedBy)u;
				c.setUpdatedBy(VAL.SYSTEM_UID);
				c.setUpdatedAt(DateTimes.getDate());
			}
			
			log.info("Create default super user: " + u.getEmail() + "/" + u.getPassword());
			udao.insert(u);
		}
		catch (Exception e) {
			log.error("Failed to init super user", e);
		}
	}

}
