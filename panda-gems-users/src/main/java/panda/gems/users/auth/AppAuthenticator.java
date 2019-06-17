package panda.gems.users.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.app.auth.UserAuthenticator;
import panda.app.constant.VAL;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.gems.users.util.PasswordHelper;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;

@IocBean(type=UserAuthenticator.class)
public class AppAuthenticator extends UserAuthenticator {
	@IocInject
	private DaoClient daoClient;

	@IocInject
	private PasswordHelper pwHelper;

	/**
	 * get Login User From Request Attributes
	 * @param ac action context
	 * @return user object
	 */
	public User getLoginUser(ActionContext ac) {
		return (User)super.getLoginUser(ac);
	}

	public User findUser(String usr, String pwd) {
		Dao dao = daoClient.getDao();

		// find user
		UserQuery uq = new UserQuery();

		pwd = pwHelper.hashPassword(pwd);
		uq.email().eq(usr);
		uq.password().eq(pwd);
		uq.status().eq(VAL.STATUS_ACTIVE);

		return dao.fetch(uq);
	}
	
	@Override
	protected Object getUserFromParameter(ActionContext ac) {
		User u = (User)super.getUserFromParameter(ac);
		if (u != null) {
			return u;
		}
		
		HttpServletRequest req = ac.getRequest();
		
		String usr = Strings.strip(req.getParameter("_usr_"));
		String pwd = Strings.strip(req.getParameter("_pwd_"));
		if (Strings.isEmpty(usr) || Strings.isEmpty(pwd)) {
			return null;
		}
		
		return findUser(usr, pwd);
	}

	@Override
	protected String serializeUser(Object user) {
		User u = (User)user;

		Map<String, Object> cu = new HashMap<String, Object>(2);
		cu.put(User.ID, u.getId());
		cu.put(User.PASSWORD, u.getPassword());

		return super.serializeUser(cu);
	}

	@Override
	protected Object deserializeUser(String ticket) {
		User u = deserializeUser(ticket, User.class);
		if (u == null || u.getId() == null) {
			return null;
		}
		
		Dao dao = daoClient.getDao();
		UserQuery q = new UserQuery();
		q.id().eq(u.getId()).password().eq(u.getPassword()).status().eq(VAL.STATUS_ACTIVE);
		
		return dao.fetch(q);
	}
}
