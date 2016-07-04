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
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Locales;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.DateTimes;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Sorter;
import panda.mvc.bind.filter.SorterPropertyFilter;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.AccessControler;
import panda.mvc.util.ServletUrlBuilder;
import panda.mvc.util.StateProvider;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.net.mail.Email;
import panda.net.mail.EmailException;
import panda.net.mail.EmailClient;
import panda.wing.auth.AuthHelper;
import panda.wing.auth.IUser;
import panda.wing.auth.UserAuthenticator;
import panda.wing.constant.RC;
import panda.wing.constant.SC;
import panda.wing.constant.VC;
import panda.wing.entity.ICreate;
import panda.wing.entity.IStatus;
import panda.wing.entity.IUpdate;


@IocBean(type=ActionAssist.class, scope=Scope.REQUEST)
public class AppActionAssist extends ActionAssist implements AccessControler {
	@IocInject
	protected AppSettings settings;

	@IocInject
	protected FreemarkerHelper freemarker;
	
	@IocInject
	protected AppFreemarkerTemplateLoader ftlTemplateLoader;

	@IocInject
	protected AppResourceBundleLoader resBundleLoader;

	@IocInject(required=false)
	protected AuthHelper authHelper;
	
	@IocInject(required=false)
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
	public String encrypt(String value) {
		return authHelper.encrypt(value);
	}
	
	public String decrypt(String value) {
		return authHelper.decrypt(value);
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
	 * can access
	 * @param action action
	 * @return true if action can access
	 */
	@Override
	public boolean canAccess(String action) {
		if (authenticator == null) {
			return true;
		}

		String uri = ServletUrlBuilder.build(context, action, false);
		return authenticator.hasPermission(context, uri);
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
		setLimitToPager(pager, VC.DEFAULT_LIST_PAGE_ITEMS, VC.DEFAULT_LIST_MAX_ITEMS);
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
	
	//-------------------------------------------------------------
	// Template mail
	//
	/**
	 * send email
	 * @param email email
	 * @param name template name
	 * @param html html message
	 * @param model data
	 */
	public void sendTemplateMail(Email email, String name, boolean html, Object model) throws EmailException {
		String subject = "";
		String content;
		try {
			content = freemarker.execTemplate(name, model);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}

		int cr = content.indexOf('\n');
		if (cr > 0) {
			subject = Strings.strip(content.substring(0, cr));
			content = content.substring(cr + 1);
		}

		email.setSubject(subject);
		if (html) {
			email.setHtmlMsg(content);
		}
		else {
			email.setTextMsg(content);
		}

		sendMail(email);
	}

	/**
	 * send email
	 * @param email email
	 * @param name template name
	 * @param html html message
	 */
	public void sendTemplateMail(Email email, String name, boolean html) throws EmailException {
		sendTemplateMail(email, name, html, null);
	}
	
	/**
	 * send email
	 * @param email email
	 * @throws EmailException if an email error occurs
	 */
	public void sendMail(Email email) throws EmailException {
		if (email.getFrom() == null) {
			email.setFrom(getMailSetting(SC.MAIL_FROM_MAIL, null), getMailSetting(SC.MAIL_FROM_NAME, null));
		}

		String charset = getMailSetting(SC.MAIL_CHARSET, Charsets.UTF_8);
		if (Strings.isNotEmpty(charset)) {
			email.setCharset(charset);
		}

		EmailClient client = new EmailClient();
		String helo = getMailSetting(SC.MAIL_SMTP_HELO, null);
		if (Strings.isNotEmpty(helo)) {
			client.setHelo(helo);
		}
		
		String host = getMailSetting(SC.MAIL_SMTP_HOST, null);
		if (Strings.isNotEmpty(host)) {
			client.setHost(host);
		}
		
		int port = getMailSettingAsInt(SC.MAIL_SMTP_PORT, 0);
		if (port > 0) {
			client.setPort(port);
		}

		if (getMailSettingAsBoolean(SC.MAIL_SMTP_SSL, false)) {
			client.setSsl(true);
		}

		if (!getMailSettingAsBoolean(SC.MAIL_SMTP_TLS, true)) {
			client.setTls(false);
		}

		int timeout = getMailSettingAsInt(SC.MAIL_SMTP_CONN_TIMEOUT, 0);
		if (timeout > 0) {
			client.setConnectTimeout(timeout);
		}

		timeout = getMailSettingAsInt(SC.MAIL_SMTP_SEND_TIMEOUT, 0);
		if (timeout > 0) {
			client.setDefaultTimeout(timeout);
		}

//		client.setTLS(getMailSettingAsBoolean(SC.MAIL_SMTP_TLS, false));
		
		String username = getMailSetting(SC.MAIL_SMTP_USER, null);
		if (Strings.isNotEmpty(username)) {
			client.setUsername(username);
			client.setPassword(getMailSetting(SC.MAIL_SMTP_PASSWORD, ""));
		}
		
		String bounce = getMailSetting(SC.MAIL_SMTP_BOUNCE, null);
		if (Strings.isNotEmpty(bounce)) {
//			email.setBounceAddress(bounce);
		}

		client.send(email);
	}
	
	protected String getMailSetting(String key, String def) {
		String s = context.getText().getText(key, null);
		if (Strings.isNotEmpty(s)) {
			return s;
		}
		return settings.getProperty(key, def);
	}
	
	protected boolean getMailSettingAsBoolean(String key, boolean def) {
		String v = Strings.stripToNull(getMailSetting(key, null));
		if (Strings.isEmpty(v)) {
			return def;
		}
		return (Boolean)Castors.scast(v, boolean.class);
	}
	
	protected int getMailSettingAsInt(String key, int def) {
		String v = Strings.stripToNull(getMailSetting(key, null));
		if (Strings.isEmpty(v)) {
			return def;
		}
		return (Integer)Castors.scast(v, int.class);
	}
}
