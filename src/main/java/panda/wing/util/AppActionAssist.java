package panda.wing.util;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import panda.bind.json.JsonObject;
import panda.bind.json.JsonSerializer;
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
import panda.mvc.bind.filter.SorterPropertyFilter;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.PermissionProvider;
import panda.mvc.util.StateProvider;
import panda.wing.auth.AuthHelper;
import panda.wing.auth.IUser;
import panda.wing.constant.RC;
import panda.wing.constant.SC;
import panda.wing.constant.VC;
import panda.wing.entity.ICreate;
import panda.wing.entity.IStatus;
import panda.wing.entity.IUpdate;


@IocBean(type=ActionAssist.class, scope=Scope.REQUEST)
public class AppActionAssist extends ActionAssist implements PermissionProvider {
	@IocInject
	protected AppSettings settings;

	@IocInject
	protected AppFreemarkerTemplateLoader ftlTemplateLoader;

	@IocInject
	protected AppResourceBundleLoader resBundleLoader;

	@IocInject(required=false)
	protected AuthHelper authHelper;
	
	//--------------------------------------------------------------------------	
	/**
	 * @return app version
	 */
	public String getAppVersion() {
		return settings.getAppVersion();
	}
	
	/**
	 * @return true if database resource loader is activated
	 */
	public boolean isDatabaseResourceLoader() {
		return resBundleLoader.getDatabaseResourceLoader() != null;
	}
	
	/**
	 * @return true if database template loader is activated
	 */
	public boolean isDatabaseTemplateLoader() {
		return ftlTemplateLoader.getDatabaseTemplateLoader() != null;
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
			if (!VC.LOCALE_ALL.equals(country)) {
				return Locales.isAvailableLocale(new Locale(language, country));
			}
		}
		return true;
	}

	//--------------------------------------------------------------------------	
	/**
	 * @return true if remote host is local network host
	 */
	@Override
	public boolean isDebugEnabled() {
		return isIntranetHost() || isSuperUser();
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
		return authHelper.isAdminUser(u);
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
		return authHelper.isSuperUser(u);
	}

	/**
	* @param username username
	* @param password password
	* @return true if username & password equals properties setting
	*/
	public boolean isSuperUser(String username, String password) {
		return authHelper.isSuperUser(username, password);
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

	//--------------------------------------------------------------------------
	public long getLoginUserId() {
		return authHelper.getLoginUserId(context);
	}

	/**
	 * getLoginUser
	 * @return user
	 */
	public IUser getLoginUser() {
		return authHelper.getLoginUser(context);
	}

	/**
	 * setLoginUser
	 * @param user user
	 */
	public void setLoginUser(IUser user) {
		authHelper.setLoginUser(context, user);
	}

	/**
	 * removeLoginUser
	 */
	public void removeLoginUser() {
		authHelper.removeLoginUser(context);
	}

	/**
	 * hasPermission
	 * @param action action
	 * @return true if action has access permit
	 */
	@Override
	public boolean hasPermission(String action) {
		return authHelper.hasPermission(context, action);
	}

	/**
	 * hasDataPermission
	 * @param path path
	 * @return true if action has access permit
	 */
	@Override
	public boolean hasDataPermission(Object data, String path) {
		return true;
	}

	//-------------------------------------------------------------
	/**
	 * initialize common fields of data
	 * @param data data
	 */
	public void initCommonFields(Object data) {
		if (data instanceof IStatus) {
			if (((IStatus)data).getStatus() == null) {
				((IStatus)data).setStatus(VC.STATUS_ACTIVE);
			}
		}
		
		Date now = DateTimes.getDate();
		if (data instanceof ICreate) {
			ICreate cb = (ICreate)data;
			cb.setCusid(getLoginUserId());
			cb.setCtime(now);
		}
		
		if (data instanceof IUpdate) {
			IUpdate ub = (IUpdate)data;
	
			ub.setUusid(getLoginUserId());
			ub.setUtime(now);
		}
	}

	
	/**
	 * initialize update fields of data
	 * @param data data
	 */
	public void initUpdateFields(Object data, Object srcData) {
		if (data instanceof IStatus) {
			if (((IStatus)data).getStatus() == null) {
				((IStatus)data).setStatus(VC.STATUS_ACTIVE);
			}
		}
		
		if (data instanceof ICreate) {
			ICreate cb = (ICreate)data;
			ICreate sb = (ICreate)srcData;
	
			cb.setCusid(sb.getCusid());
			cb.setCtime(sb.getCtime());
		}
		
		if (data instanceof IUpdate) {
			IUpdate ub = (IUpdate)data;

			ub.setUusid(getLoginUserId());
			ub.setUtime(DateTimes.getDate());
		}
	}
	
	//-------------------------------------------------------------
	/**
	 * load sorter parameters from stateProvider
	 * @param sorter sorter
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
			sc = getText(getMethodName() + RC.SORTER_SUFFIX, null);
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
		JsonSerializer js = new JsonSerializer();
		js.registerPropertyFilter(Sorter.class, new SorterPropertyFilter(true));
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
		setLimitToPager(pager, VC.DEFAULT_LIST_PAGE_ITEMS, VC.DEFAULT_MAX_PAGE_ITEMS);
	}
	
	/**
	 * if pager.limit > maxLimit then set pager.limit = maxLimit
	 * @param pager pager
	 * @param def default limit
	 * @param max maximum limit
	 */
	public void setLimitToPager(Pager pager, long def, long max) {
		if (!pager.hasLimit()) {
			long l = getTextAsLong(getMethodName() + RC.PAGE_ITEMS_DEFAULT_SUFFIX, def);
			pager.setLimit(l);
		}

		long m = getTextAsLong(getMethodName() + RC.PAGE_ITEMS_MAXIMUM_SUFFIX, max);
		if (m > 0 && (!pager.hasLimit() || pager.getLimit() > m)) {
			pager.setLimit(m);
		}
	}
	
	/**
	 * save pager limit parameters to stateProvider
	 * @param pager pager
	 */
	public void saveLimitParams(Pager pager) {
		getState().saveState("limit", pager.getLimit());
	}
	
}
