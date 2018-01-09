package panda.mvc.processor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.net.http.UserAgent;
import panda.servlet.HttpServlets;

@IocBean
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
	 * DEFAULT_COOKIE = "WW_LAYOUT";
	 */
	public static final String DEFAULT_COOKIE = DEFAULT_ATTRIBUTE;

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

	@IocInject(value=MvcConstants.LAYOUT_PARAMETER_NAME, required=false)
	protected String parameterName = DEFAULT_PARAMETER;

	@IocInject(value=MvcConstants.LAYOUT_REQUEST_ATTR, required=false)
	protected String requestName = DEFAULT_ATTRIBUTE;

	@IocInject(value=MvcConstants.LAYOUT_SESSION_ATTR, required=false)
	protected String sessionName = DEFAULT_ATTRIBUTE;

	@IocInject(value=MvcConstants.LAYOUT_COOKIE_NAME, required=false)
	protected String cookieName = DEFAULT_COOKIE;

	@IocInject(value=MvcConstants.LAYOUT_COOKIE_DOMAIN, required=false)
	protected String cookieDomain;

	@IocInject(value=MvcConstants.LAYOUT_COOKIE_PATH, required=false)
	protected String cookiePath;

	@IocInject(value=MvcConstants.LAYOUT_COOKIE_MAXAGE, required=false)
	protected int cookieMaxAge = DEFAULT_COOKIE_MAXAGE;

	protected String defaultLayout = PC_LAYOUT;

	@IocInject(value=MvcConstants.LAYOUT_SAVE_TO_SESSION, required=false)
	protected boolean saveToSession = false;

	@IocInject(value=MvcConstants.LAYOUT_SAVE_TO_COOKIE, required=false)
	protected boolean saveToCookie = true;

	@IocInject(value=MvcConstants.LAYOUT_FROM_USER_AGENT, required=false)
	protected boolean fromUserAgent = true;

	/**
	 * Constructor
	 */
	public LayoutProcessor() {
	}

	public void process(ActionContext ac) {
		boolean saveToSession = this.saveToSession;
		boolean saveToCookie = this.saveToCookie;

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
		
		if (Strings.isEmpty(layout) && fromUserAgent) {
			layout = getLayoutFromUserAgent(ac);
			saveToCookie = false;
		}

		if (layout == null) {
			layout = defaultLayout;
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
	 * @param ac action context
	 * @param layout The layout to save.
	 */
	protected void saveLayout(ActionContext ac, String layout) {
		ac.getRequest().setAttribute(DEFAULT_ATTRIBUTE, layout);
	}
	
	/**
	 * get context layout
	 * @param ac action context
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
		return null;
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
			if (Strings.isEmpty(c.getPath())) {
				c.setPath("/");
			}
		}
		
		c.setMaxAge(cookieMaxAge);
		
		ac.getResponse().addCookie(c);
	}
}
