package panda.mvc.processor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Locales;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.net.http.HttpHeader;
import panda.servlet.HttpServlets;

@IocBean
public class LocaleProcessor extends AbstractActionProcessor {

	/**
	 * DEFAULT_HEADER = "X-WW-LOCALE";
	 */
	public static final String DEFAULT_HEADER = "X-WW-LOCALE";

	/**
	 * DEFAULT_ATTRIBUTE = "WW_LOCALE";
	 */
	public static final String DEFAULT_ATTRIBUTE = "WW_LOCALE";

	/**
	 * DEFAULT_PARAMETER = "__locale";
	 */
	public static final String DEFAULT_PARAMETER = "__locale";

	/**
	 * DEFAULT_COOKIE = "WW_LOCALE";
	 */
	public static final String DEFAULT_COOKIE = DEFAULT_ATTRIBUTE;

	/**
	 * DEFAULT_COOKIE_MAXAGE = 60 * 60 * 24 * 30; //1M
	 */
	public static final int DEFAULT_COOKIE_MAXAGE = DateTimes.SEC_MONTH; //1M

	@IocInject(value=MvcConstants.LOCALE_HEADER_NAME, required=false)
	protected String headerName = DEFAULT_HEADER;

	@IocInject(value=MvcConstants.LOCALE_PARAMETER_NAME, required=false)
	protected String parameterName = DEFAULT_PARAMETER;

	@IocInject(value=MvcConstants.LOCALE_REQUEST_ATTR, required=false)
	protected String requestName = DEFAULT_ATTRIBUTE;

	@IocInject(value=MvcConstants.LOCALE_SESSION_ATTR, required=false)
	protected String sessionName = DEFAULT_ATTRIBUTE;

	@IocInject(value=MvcConstants.LOCALE_COOKIE_NAME, required=false)
	protected String cookieName = DEFAULT_COOKIE;

	@IocInject(value=MvcConstants.LOCALE_COOKIE_DOMAIN, required=false)
	protected String cookieDomain;

	@IocInject(value=MvcConstants.LOCALE_COOKIE_PATH, required=false)
	protected String cookiePath;

	@IocInject(value=MvcConstants.LOCALE_COOKIE_MAXAGE, required=false)
	protected int cookieMaxAge = DEFAULT_COOKIE_MAXAGE;

	protected Map<String, Locale> domainLocales;
	protected Map<Pattern, Locale> domainRegexLocales;
	
	@IocInject(value=MvcConstants.LOCALE_ACCEPTS, required=false)
	protected String[] acceptLocales;
	
	@IocInject(value=MvcConstants.LOCALE_SAVE_TO_SESSION, required=false)
	protected boolean saveToSession = false;

	@IocInject(value=MvcConstants.LOCALE_SAVE_TO_COOKIE, required=false)
	protected boolean saveToCookie = true;

	@IocInject(value=MvcConstants.LOCALE_FROM_ACCEPT_LANGUAGE, required=false)
	protected boolean fromAcceptLanguage = true;

	/**
	 * Constructor
	 */
	public LocaleProcessor() {
	}

	/**
	 * @param domainLocales the domainLocales to set
	 */
	@IocInject(value=MvcConstants.LOCALE_DOMAINS, required=false)
	public void setDomainLocales(Map<String, String> domainLocales) {
		if (Collections.isEmpty(domainLocales)) {
			return;
		}
		
		HashMap<String, Locale> domains = new HashMap<String, Locale>();
		HashMap<Pattern, Locale> regexs = new LinkedHashMap<Pattern, Locale>();
		for (Entry<String, String> en : domainLocales.entrySet()) {
			String key = en.getKey();
			Locale val = Locales.toLocale(en.getValue());
			if (Strings.endsWithChar(key, '$')) {
				regexs.put(Pattern.compile(key, Pattern.CASE_INSENSITIVE), val);
			}
			else {
				domains.put(key, val);
			}
		}
		
		if (Collections.isNotEmpty(domains)) {
			this.domainLocales = domains;
		}
		if (Collections.isNotEmpty(regexs)) {
			this.domainRegexLocales = regexs;
		}
	}

	@Override
	public void process(ActionContext ac) {
		boolean saveToSession = this.saveToSession;
		boolean saveToCookie = this.saveToCookie;

		Locale locale = (Locale)ac.getRequest().getAttribute(requestName);
		if (locale != null) {
			saveToSession = false;
			saveToCookie = false;
		}

		if (locale == null) {
			locale = getLocaleFromParameter(ac);
		}

		if (locale == null) {
			locale = getLocaleFromHeader(ac);
		}

		if (locale == null) {
			locale = getLocaleFromSession(ac);
			if (locale != null) {
				saveToSession = false;
				saveToCookie = false;
			}
		}
		
		if (locale == null && Strings.isNotEmpty(cookieName)) {
			locale = getLocaleFromCookie(ac);
			saveToCookie = false;
		}
		
		if (locale == null) {
			locale = getLocaleFromDomain(ac);
			if (locale != null) {
				saveToCookie = false;
			}
		}
		
		if (locale == null && fromAcceptLanguage) {
			locale = getLocaleFromAcceptLanguage(ac);
			saveToCookie = false;
		}

		if (locale == null) {
			if (Arrays.isEmpty(acceptLocales)) {
				locale = Locale.getDefault();
			}
			else {
				locale = Locales.parseLocale(acceptLocales[0]);
			}
			saveToCookie = false;
		}
		
		// save locale
		if (locale != null) {
			ac.setLocale(locale);

			if (saveToSession) {
				saveLocaleToSession(ac, locale);
			}
			if (saveToCookie && Strings.isNotEmpty(cookieName)) {
				saveLocaleToCookie(ac, locale);
			}
		}
		
		doNext(ac);
	}

	protected Locale getLocaleFromDomain(ActionContext ac) {
		if (Collections.isNotEmpty(domainLocales)) {
			String sn = ac.getRequest().getServerName();
			Locale locale = domainLocales.get(sn);
			if (locale != null) {
				return locale;
			}
		}
		
		if (Collections.isNotEmpty(domainRegexLocales)) {
			String sn = ac.getRequest().getServerName();
			for (Entry<Pattern, Locale> en : domainRegexLocales.entrySet()) {
				Pattern p = en.getKey();
				Matcher m = p.matcher(sn);
				if (m.matches()) {
					return en.getValue();
				}
			}
		}
		
		return null;
	}

	protected Locale getLocaleFromCookie(ActionContext ac) {
		String cv = HttpServlets.getCookieValue(ac.getRequest(), cookieName);
		return parseLocale(cv);
	}

	protected Locale getLocaleFromParameter(ActionContext ac) {
		String v = ac.getRequest().getParameter(parameterName);

		if (Strings.isEmpty(v)) {
			return null;
		}
		
		Locale locale = Locales.toLocale(v, null);
		return isAcceptableLocale(locale) ? locale : null;
	}

	protected Locale getLocaleFromHeader(ActionContext ac) {
		String v = ac.getRequest().getHeader(headerName);

		if (Strings.isEmpty(v)) {
			return null;
		}
		
		Locale locale = Locales.toLocale(v, null);
		return isAcceptableLocale(locale) ? locale : null;
	}

	protected Locale getLocaleFromSession(ActionContext ac) {
		return (Locale)ac.getSes().get(sessionName);
	}

	protected Locale getLocaleFromAcceptLanguage(ActionContext ac) {
		String al = ac.getRequest().getHeader(HttpHeader.ACCEPT_LANGUAGE);
		if (Strings.isNotEmpty(al)) {
			String[] als = Strings.split(Strings.replaceChars(al, '-', '_'), ",; ");
			for (String str : als) {
				Locale locale = parseLocale(str);
				if (locale != null) {
					return locale;
				}
			}
		}
		return null;
	}
	
	protected void saveLocaleToSession(ActionContext ac, Locale locale) {
		ac.getSes().put(sessionName, locale);
	}
	
	protected void saveLocaleToCookie(ActionContext ac, Locale locale) {
		Cookie c = new Cookie(cookieName, locale.toString());
		if (Strings.isNotEmpty(cookieDomain)) {
			c.setDomain(cookieDomain);
		}
		
		if (Strings.isNotEmpty(cookiePath)) {
			c.setPath(cookiePath);
		}
		else {
			c.setPath(ac.getRequest().getContextPath());
			if (Strings.isEmpty(c.getPath())) {
				c.setPath("/");
			}
		}
		
		c.setMaxAge(cookieMaxAge);
		
		ac.getResponse().addCookie(c);
	}

	protected boolean isAcceptableLocale(Locale locale) {
		if (locale != null) {
			if (acceptLocales == null) {
				return true;
			}
			
			String l = locale.toString();
			for (String p : acceptLocales) {
				if (l.startsWith(p)) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected Locale parseLocale(String str) {
		if (Strings.isEmpty(str)) {
			return null;
		}
		
		Locale locale = Locales.toLocale(str);
		if (isAcceptableLocale(locale)) {
			return locale;
		}
		
		int i = str.indexOf('_');
		if (i > 0) {
			String s2 = str.substring(0, i);
			locale = Locales.toLocale(s2);
			if (isAcceptableLocale(locale)) {
				return locale;
			}
		}
		
		return null;
	}
}
