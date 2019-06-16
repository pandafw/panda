package panda.app.util;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import freemarker.template.TemplateException;
import panda.app.auth.IUser;
import panda.app.auth.UserAuthenticator;
import panda.app.constant.RES;
import panda.app.constant.VAL;
import panda.app.entity.ICreatedBy;
import panda.app.entity.IStatus;
import panda.app.entity.IUpdatedBy;
import panda.bind.json.JsonObject;
import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.cast.Castors;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Locales;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.DateTimes;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Sorter;
import panda.mvc.bind.adapter.SorterAdapter;
import panda.mvc.util.AccessHandler;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.MvcURLBuilder;
import panda.mvc.util.StateProvider;
import panda.mvc.view.ftl.FreemarkerHelper;


@IocBean(type=ActionAssist.class, scope=Scope.REQUEST)
public class AppActionAssist extends ActionAssist implements AccessHandler {
	@IocInject
	protected AppSettings settings;

	@IocInject
	protected FreemarkerHelper freemarker;
	
	@IocInject
	protected UserAuthenticator authenticator;

	//--------------------------------------------------------------------------	
	/**
	 * hasTemplate
	 * @param name template name
	 * @return true if template exists
	 */
	public boolean hasTemplate(String name) {
		return freemarker.hasTemplate(name);
	}
	
	/**
	 * process template
	 * @param name template name
	 * @param model model
	 * @return result
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public String execTemplate(String name, Object model) throws TemplateException, IOException {
		return freemarker.execTemplate(name, model);
	}
	
	//--------------------------------------------------------------------------	
	/**
	 * @return app version
	 */
	public String getAppVersion() {
		return settings.getAppVersion();
	}
	
	/**
	 * @return true if gae support
	 */
	public boolean isGaeSupport() {
		return Systems.IS_OS_APPENGINE;
	}

	//--------------------------------------------------------------------------	
	/**
	 * isValidLocale (used by Property, Template, Resource Action)
	 * @param language language
	 * @param country country
	 * @return true - if locale is valid
	 */
	public boolean isValidLocale(String language, String country) {
		if (Strings.isNotEmpty(language) && Strings.isNotEmpty(country)) {
			if (!VAL.LOCALE_ALL.equals(country)) {
				return Locales.isAvailableLocale(new Locale(language, country));
			}
		}
		return true;
	}

	//--------------------------------------------------------------------------	
	public String encrypt(String value) {
		return authenticator.encrypt(value);
	}
	
	public String decrypt(String value) {
		return authenticator.decrypt(value);
	}
	
	//--------------------------------------------------------------------------	
	/**
	 * @return true - if the login user is administrator or super
	 */
	public boolean isAdministrators() {
		return isAdministrators(getLoginUser());
	}

	/**
	 * @param u user
	 * @return true - if the user is administrator or super
	 */
	public boolean isAdministrators(IUser u) {
		return authenticator.isAdministrators(u);
	}

	/**
	 * @return true - if the login user is administrator
	 */
	public boolean isAdminUser() {
		return isAdminUser(getLoginUser());
	}

	/**
	 * @param u user
	 * @return true - if the user is administrator
	 */
	public boolean isAdminUser(IUser u) {
		return authenticator.isAdminUser(u);
	}

	/**
	 * @return true - if the login user is super
	 */
	public boolean isSuperUser() {
		return isSuperUser(getLoginUser());
	}

	/**
	 * @param u user
	 * @return true - if the user is super
	 */
	public boolean isSuperUser(IUser u) {
		return authenticator.isSuperUser(u);
	}

	//--------------------------------------------------------------------------
	public long getLoginUserId() {
		return authenticator.getLoginUserId(context);
	}

	public String getLoginUserName() {
		return authenticator.getLoginUserName(context);
	}

	/**
	 * getLoginUser
	 * @return user
	 */
	public IUser getLoginUser() {
		return authenticator.getLoginUser(context);
	}

	/**
	 * setLoginUser
	 * @param user user
	 * @param time login time
	 */
	public void setLoginUser(IUser user, Date time) {
		authenticator.saveUserToContext(context, user, time);
	}

	/**
	 * setLoginUser
	 * @param user user
	 */
	public void setLoginUser(IUser user) {
		authenticator.saveUserToContext(context, user, null);
	}

	/**
	 * save user object to client
	 * @param user user object
	 */
	public void saveUserToClient(IUser user) {
		authenticator.saveUserToClient(context, user);
	}

	/**
	 * removeLoginUser
	 */
	public void removeLoginUser() {
		authenticator.removeUserFromContext(context);
		authenticator.removeUserFromClient(context);
	}

	/**
	 * can access
	 * @param action action
	 * @return true if action can access
	 */
	@Override
	public boolean canAccess(String action) {
		if (authenticator == null) {
			return true;
		}

		String path = MvcURLBuilder.buildPath(context, action, false);
		return authenticator.canAccess(context, path);
	}

	/**
	 * canAccessData
	 * @param action action
	 * @param data data
	 * @return true if action can access the data
	 */
	@Override
	public boolean canAccessData(String action, Object data) {
		return canAccess(action);
	}

	//-------------------------------------------------------------
	/**
	 * set ICreatedBy fields of data
	 * @param data data
	 */
	public void setCreatedByFields(Object data) {
		if (data instanceof IStatus) {
			if (((IStatus)data).getStatus() == null) {
				((IStatus)data).setStatus(VAL.STATUS_ACTIVE);
			}
		}
		
		Date now = DateTimes.getDate();
		if (data instanceof ICreatedBy) {
			ICreatedBy cb = (ICreatedBy)data;
			cb.setCreatedAt(now);
			cb.setCreatedBy(getLoginUserId());
			cb.setCreatedByName(getLoginUserName());
		}
		
		if (data instanceof IUpdatedBy) {
			IUpdatedBy ub = (IUpdatedBy)data;
			ub.setUpdatedAt(now);
			ub.setUpdatedBy(getLoginUserId());
			ub.setUpdatedByName(getLoginUserName());
		}
	}

	/**
	 * set IUpdatedBy fields of data
	 * @param data input data
	 */
	public void setUpdatedByFields(Object data) {
		if (data instanceof IUpdatedBy) {
			IUpdatedBy ub = (IUpdatedBy)data;
			ub.setUpdatedAt(DateTimes.getDate());
			ub.setUpdatedBy(getLoginUserId());
			ub.setUpdatedByName(getLoginUserName());
		}
	}
	
	/**
	 * set IUpdatedBy fields of data, and copy ICreatedBy fields of source data
	 * @param data input data
	 * @param sdat source data
	 */
	public void setUpdatedByFields(Object data, Object sdat) {
		if (data instanceof ICreatedBy && sdat instanceof ICreatedBy) {
			ICreatedBy cb = (ICreatedBy)data;
			ICreatedBy sb = (ICreatedBy)sdat;
	
			cb.setCreatedAt(sb.getCreatedAt());
			cb.setCreatedBy(sb.getCreatedBy());
			cb.setCreatedByName(sb.getCreatedByName());
		}
		
		setUpdatedByFields(data);
	}

	/**
	 * clear ICreatedBy fields of data
	 * @param data ICreateBy data
	 */
	public void clearCreateByFields(Object data) {
		if (data instanceof ICreatedBy) {
			ICreatedBy cb = (ICreatedBy)data;
			cb.setCreatedAt(null);
			cb.setCreatedBy(null);
			cb.setCreatedByName(null);
		}
	}
	
	/**
	 * clear IUpdatedBy fields of data
	 * @param data IUpdateBy data
	 */
	public void clearUpdateByFields(Object data) {
		if (data instanceof IUpdatedBy) {
			IUpdatedBy ub = (IUpdatedBy)data;
			ub.setUpdatedAt(null);
			ub.setUpdatedBy(null);
			ub.setUpdatedByName(null);
		}
	}
	
	//-------------------------------------------------------------
	/**
	 * load sorter parameters from stateProvider
	 * @param sorter sorter
	 * @param orders orders
	 */
	public void loadSorterParams(Sorter sorter, Map<String, String> orders) {
		StateProvider sp = getState();
		if (sp == null) {
			return;
		}
		
		// first check
		if (Strings.isNotEmpty(sorter.getColumn()) && orders.containsKey(sorter.getColumn())) {
			return;
		}
		
		String sc = (String)sp.loadState("sorter");
		if (Strings.isEmpty(sc)) {
			sc = getText(getMethodName() + RES.SORTER_SUFFIX, null);
		}
		castToSorter(sorter, sc);

		// second check
		if (Strings.isNotEmpty(sorter.getColumn()) && orders.containsKey(sorter.getColumn())) {
			return;
		}

		// set default
		for (Entry<String, String> en : orders.entrySet()) {
			sorter.setColumn(en.getKey());
			sorter.setDirection(en.getValue());
			return;
		}
	}

	public void castToSorter(Sorter sorter, String sc) {
		if (Strings.isEmpty(sc)) {
			return;
		}
		
		try {
			JsonObject jo = JsonObject.fromJson(sc);
			Castors.scastTo(jo, sorter);
		}
		catch (Exception e) {
			getLog().debug("Invalid JSON sorter: " + sc, e);
		}
	}

	/**
	 * save sorter parameters to stateProvider
	 * @param sorter sorter
	 */
	public void saveSorterParams(Sorter sorter) {
		JsonSerializer js = Jsons.newJsonSerializer();
		js.registerAdapter(Sorter.class, SorterAdapter.s());

		String ss = js.serialize(sorter);
		getState().saveState("sorter", ss);
	}
	
	/**
	 * load pager limit parameters from stateProvider
	 * @param pager pager
	 */
	public void loadLimitParams(Pager pager) {
		if (!pager.hasLimit()) {
			StateProvider sp = getState();
			if (sp != null) {
				pager.setLimit(Numbers.toLong((String)sp.loadState("limit"), 0L));
			}
		}
		setLimitToPager(pager);
	}

	/**
	 * if pager.limit > maxLimit then set pager.limit = maxLimit
	 * @param pager pager
	 */
	public void setLimitToPager(Pager pager) {
		setLimitToPager(pager, VAL.DEFAULT_LIST_PAGESIZE, VAL.MAXIMUM_LIST_PAGESIZE);
	}
	
	/**
	 * if pager.limit > maxLimit then set pager.limit = maxLimit
	 * @param pager pager
	 * @param def default limit
	 * @param max maximum limit
	 */
	public void setLimitToPager(Pager pager, long def, long max) {
		if (!pager.hasLimit()) {
			long l = getTextAsLong(getMethodName() + RES.PAGESIZE_DEFAULT_SUFFIX, def);
			pager.setLimit(l);
		}

		long m = getTextAsLong(getMethodName() + RES.PAGESIZE_MAXIMUM_SUFFIX, max);
		if (m > 0 && (!pager.hasLimit() || pager.getLimit() > m)) {
			pager.setLimit(m);
		}
	}
	
	/**
	 * save pager limit parameters to stateProvider
	 * @param pager pager
	 */
	public void saveLimitParams(Pager pager) {
		if (pager.getLimit() != null) {
			getState().saveState("limit", pager.getLimit().toString());
		}
	}
}
