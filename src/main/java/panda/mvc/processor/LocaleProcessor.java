package panda.mvc.processor;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.lang.Locales;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.servlet.HttpServlets;

public class LocaleProcessor extends AbstractProcessor {

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
	public static final Integer DEFAULT_COOKIE_MAXAGE = 60 * 60 * 24 * 30; //1M

	protected String parameterName = DEFAULT_PARAMETER;
	protected String requestName = DEFAULT_ATTRIBUTE;
	protected String sessionName = DEFAULT_ATTRIBUTE;
	protected String cookieName = DEFAULT_COOKIE;
	protected String cookieDomain;
	protected String cookiePath;
	protected Integer cookieMaxAge = DEFAULT_COOKIE_MAXAGE;
	protected String[] validLocales;
	protected boolean fromAcceptLanguage = true;

	protected boolean saveToSession = false;
	protected boolean saveToCookie = true;

	/**
	 * Constructor
	 */
	public LocaleProcessor() {
	}

	/**
	 * @param sessionName the sessionName to set
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * @param cookieName the cookieName to set
	 */
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	/**
	 * @param cookieDomain the cookieDomain to set
	 */
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	/**
	 * @param cookiePath the cookiePath to set
	 */
	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	/**
	 * @param cookieMaxAge the cookieMaxAge to set
	 */
	public void setCookieMaxAge(Integer cookieMaxAge) {
		this.cookieMaxAge = cookieMaxAge;
	}

	public void setValidLocale(String validLocale) {
		this.validLocales = Strings.split(validLocale, ',');
	}

	/**
	 * @param fromAcceptLanguage the fromAcceptLanguage to set
	 */
	public void setFromAcceptLanguage(String fromAcceptLanguage) {
		this.fromAcceptLanguage = Boolean.parseBoolean(fromAcceptLanguage);
	}

	/**
	 * @param saveToSession the saveToSession to set
	 */
	public void setSaveToSession(boolean saveToSession) {
		this.saveToSession = saveToSession;
	}

	/**
	 * @param saveToCookie the saveToCookie to set
	 */
	public void setSaveToCookie(boolean saveToCookie) {
		this.saveToCookie = saveToCookie;
	}

	public void process(ActionContext ac) throws Throwable {
		HttpSession session = ac.getRequest().getSession();

		Locale locale = null;

		locale = getLocaleFromParameter(ac, parameterName);
		if (locale == null) {
			locale = (Locale)ac.getRequest().getAttribute(requestName);
			if (locale != null) {
				saveToSession = false;
				saveToCookie = false;
			}
		}

		if (locale == null && session != null) {
			Object obj = session.getAttribute(sessionName);
			if (obj != null && obj instanceof Locale) {
				locale = (Locale)obj;
				saveToSession = false;
				saveToCookie = false;
			}
		}
		
		if (locale == null && Strings.isNotEmpty(cookieName)) {
			locale = getLocaleFromCookie(ac);
			saveToCookie = false;
		}
		
		if (locale == null && fromAcceptLanguage) {
			locale = getLocaleFromAcceptLanguage(ac);
			saveToCookie = false;
		}

		// save locale
		if (locale != null) {
			saveLocale(ac, locale);

			if (saveToSession && session != null) {
				session.setAttribute(sessionName, locale);
			}
			if (saveToCookie && Strings.isNotEmpty(cookieName)) {
				saveLocaleToCookie(ac, locale);
			}
		}
		
		doNext(ac);
	}

	protected Locale getLocaleFromCookie(ActionContext ac) {
		Locale locale = null;
		HttpServletRequest request = ac.getRequest();
		String cv = HttpServlets.getCookieValue(request, cookieName);
		if (Strings.isNotEmpty(cv)) {
			locale = Locales.toLocale(cv, null);
		}
		return isValidLocale(locale) ? locale : null;
	}

	protected Locale getLocaleFromParameter(ActionContext ac, String parameterName) {
		Object locale = ac.getRequest().getParameter(parameterName);
		if (locale != null && locale.getClass().isArray() && ((Object[])locale).length == 1) {
			locale = ((Object[])locale)[0];
		}

		if (locale != null && !(locale instanceof Locale)) {
			locale = Locales.toLocale(locale.toString(), null);
		}
		return isValidLocale((Locale)locale) ? (Locale)locale : null;
	}

	protected Locale getLocaleFromAcceptLanguage(ActionContext ac) {
		String al = ac.getRequest().getHeader("Accept-Language");
		if (Strings.isNotEmpty(al)) {
			String[] als = Strings.split(Strings.replaceChars(al, '-', '_'), ",;");
			for (String str : als) {
				try {
					Locale locale = Locales.toLocale(str);
					if (isValidLocale(locale)) {
						return locale;
					}
				}
				catch (Exception e) {
					
				}
			}
		}
		return null;
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
		}
		
		c.setMaxAge(cookieMaxAge);
		
		ac.getResponse().addCookie(c);
	}

	/**
	 * Save the given locale to the ActionInvocation.
	 * 
	 * @param ac The ActionContext.
	 * @param locale The locale to save.
	 */
	protected void saveLocale(ActionContext ac, Locale locale) {
		ac.setLocale(locale);
	}

	protected boolean isValidLocale(Locale locale) {
		if (locale != null) {
			if (validLocales == null) {
				return true;
			}
			String l = locale.toString();
			for (String p : validLocales) {
				if (l.startsWith(p)) {
					return true;
				}
			}
		}
		return false;
	}
}
