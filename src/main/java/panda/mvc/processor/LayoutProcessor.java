package panda.mvc.processor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.net.http.UserAgent;
import panda.servlet.HttpServlets;

public class LayoutProcessor extends AbstractProcessor {

	/**
	 * DEFAULT_ATTRIBUTE = "WW_LAYOUT";
	 */
	public static final String DEFAULT_ATTRIBUTE = "WW_LAYOUT";

	/**
	 * DEFAULT_PARAMETER = "__layout";
	 */
	public static final String DEFAULT_PARAMETER = "__layout";

	/**
	 * DEFAULT_COOKIE_MAXAGE = 60 * 60 * 24 * 30; //1M
	 */
	public static final Integer DEFAULT_COOKIE_MAXAGE = 60 * 60 * 24 * 30; //1M

	/**
	 * PC_LAYOUT = "pc";
	 */
	public static final String PC_LAYOUT = "pc";

	/**
	 * MOBILE_LAYOUT = "pc";
	 */
	public static final String MOBILE_LAYOUT = "mb";

	/**
	 * DEFAULT_LAYOUT = "pc";
	 */
	public static final String DEFAULT_LAYOUT = PC_LAYOUT;

	protected String parameterName = DEFAULT_PARAMETER;
	protected String requestName = DEFAULT_ATTRIBUTE;
	protected String sessionName = DEFAULT_ATTRIBUTE;
	protected String cookieName = DEFAULT_ATTRIBUTE;
	protected String cookieDomain;
	protected String cookiePath;
	protected Integer cookieMaxAge = DEFAULT_COOKIE_MAXAGE;
	protected String defaultLayout = PC_LAYOUT;

	protected boolean saveToSession = false;
	protected boolean saveToCookie = true;

	/**
	 * Constructor
	 */
	public LayoutProcessor() {
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setSessionName(String attributeName) {
		this.sessionName = attributeName;
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

	/**
	 * @param defaultLayout the defaultLayout to set
	 */
	public void setDefaultLayout(String defaultLayout) {
		this.defaultLayout = defaultLayout;
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

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
	}

	public void process(ActionContext ac) throws Throwable {
		HttpSession session = ac.getRequest().getSession(false);

		String layout = null;

		layout = getLayoutFromParameter(ac, parameterName);
		if (Strings.isEmpty(layout)) {
			layout = (String)ac.getRequest().getAttribute(requestName);
			if (Strings.isNotEmpty(layout)) {
				saveToSession = false;
				saveToCookie = false;
			}
		}

		if (Strings.isEmpty(layout) && session != null) {
			Object obj = session.getAttribute(sessionName);
			if (obj != null) {
				layout = (String)obj;
				saveToSession = false;
				saveToCookie = false;
			}
		}
		
		if (Strings.isEmpty(layout) && Strings.isNotEmpty(cookieName)) {
			layout = getLayoutFromCookie(ac);
			saveToCookie = false;
		}
		
		if (Strings.isEmpty(layout)) {
			layout = getLayoutFromUserAgent(ac);
			saveToCookie = false;
		}
		
		// save layout
		if (Strings.isNotEmpty(layout)) {
			saveLayout(ac, layout);
			
			if (saveToSession && session != null) {
				session.setAttribute(sessionName, layout);
			}
			if (saveToCookie && Strings.isNotEmpty(cookieName)) {
				saveLayoutToCookie(ac, layout);
			}
		}
		
		doNext(ac);
	}

	/**
	 * Save the given layout to the ActionInvocation.
	 * 
	 * @param layout The layout to save.
	 */
	protected void saveLayout(ActionContext ac, String layout) {
		ac.getRequest().setAttribute(DEFAULT_ATTRIBUTE, layout);
	}
	
	/**
	 * get context layout
	 * @return layout
	 */
	public static String getLayout(ActionContext ac) {
		String layout = (String)ac.getRequest().getAttribute(DEFAULT_ATTRIBUTE);

		if (layout == null) {
			layout = DEFAULT_LAYOUT;
		}
		return layout;
	}

	protected String getLayoutFromCookie(ActionContext ac) {
		String layout = HttpServlets.getCookieValue(ac.getRequest(), cookieName);
		return layout;
	}

	protected String getLayoutFromParameter(ActionContext ac, String parameterName) {
		Object layout = ac.getRequest().getParameter(parameterName);
		if (layout != null && layout.getClass().isArray() && ((Object[])layout).length == 1) {
			layout = ((Object[])layout)[0];
		}

		return layout == null ? null : layout.toString();
	}

	protected String getLayoutFromUserAgent(ActionContext ac) {
		UserAgent ua = HttpServlets.getUserAgent(ac.getRequest());
		if (ua.isMobile() || ua.isRobot()) {
			return MOBILE_LAYOUT;
		}
		else {
			return defaultLayout;
		}
	}

	protected void saveLayoutToCookie(ActionContext ac, String layout) {
		Cookie c = new Cookie(cookieName, layout);
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
}
