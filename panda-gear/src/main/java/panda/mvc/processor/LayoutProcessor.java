package panda.mvc.processor;

import javax.servlet.http.Cookie;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.net.http.UserAgent;
import panda.servlet.HttpServlets;

@IocBean
public class LayoutProcessor extends AbstractActionProcessor {

	/**
	 * DEFAULT_HEADER = "X-WW-LAYOUT";
	 */
	public static final String DEFAULT_HEADER = "X-WW-LAYOUT";

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
	public static final Integer DEFAULT_COOKIE_MAXAGE = DateTimes.SEC_MONTH; //1M

	/**
	 * PC_LAYOUT = "pc";
	 */
	public static final String PC_LAYOUT = "pc";

	/**
	 * MOBILE_LAYOUT = "mb";
	 */
	public static final String MOBILE_LAYOUT = "mb";

	/**
	 * DEFAULT_LAYOUT = "pc";
	 */
	public static final String DEFAULT_LAYOUT = PC_LAYOUT;

	@IocInject(value=MvcConstants.LAYOUT_HEADER_NAME, required=false)
	protected String headerName = DEFAULT_HEADER;

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

	@IocInject(value=MvcConstants.LAYOUT_DEFAULT, required=false)
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

	@Override
	public void process(ActionContext ac) {
		boolean saveToSession = this.saveToSession;
		boolean saveToCookie = this.saveToCookie;

		String layout = null;

		layout = getLayoutFromRequest(ac);
		if (Strings.isNotEmpty(layout)) {
			saveToSession = false;
			saveToCookie = false;
		}
		
		if (Strings.isEmpty(layout)) {
			layout = getLayoutFromParameter(ac);
		}

		if (Strings.isEmpty(layout)) {
			layout = getLayoutFromHeader(ac);
		}

		if (Strings.isEmpty(layout)) {
			layout = getLayoutFromSession(ac);
			if (Strings.isNotEmpty(layout)) {
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
			saveLayoutToRequest(ac, layout);
			
			if (saveToSession) {
				saveLayoutToSession(ac, layout);
			}
			if (saveToCookie && Strings.isNotEmpty(cookieName)) {
				saveLayoutToCookie(ac, layout);
			}
		}
		
		doNext(ac);
	}
	
	/**
	 * get context layout
	 * @param ac action context
	 * @return layout
	 */
	public static String getLayout(ActionContext ac) {
		String layout = null;
		
		LayoutProcessor lp = ac.getIoc().getIfExists(LayoutProcessor.class);
		if (lp != null) {
			layout = (String)ac.getRequest().getAttribute(lp.requestName);
		}

		if (Strings.isEmpty(layout)) {
			layout = DEFAULT_LAYOUT;
		}
		return layout;
	}

	protected String getLayoutFromCookie(ActionContext ac) {
		return HttpServlets.getCookieValue(ac.getRequest(), cookieName);
	}

	protected String getLayoutFromRequest(ActionContext ac) {
		return (String)ac.getRequest().getAttribute(requestName);
	}

	protected String getLayoutFromParameter(ActionContext ac) {
		return ac.getRequest().getParameter(parameterName);
	}

	protected String getLayoutFromHeader(ActionContext ac) {
		return ac.getRequest().getHeader(headerName);
	}

	protected String getLayoutFromUserAgent(ActionContext ac) {
		UserAgent ua = ac.getUserAgent();
		if (ua.isMobile()) {
			return MOBILE_LAYOUT;
		}
		return null;
	}

	protected String getLayoutFromSession(ActionContext ac) {
		return (String)ac.getSes().get(sessionName);
	}

	/**
	 * Save the given layout to the request context.
	 * @param ac action context
	 * @param layout The layout to save.
	 */
	protected void saveLayoutToRequest(ActionContext ac, String layout) {
		ac.getRequest().setAttribute(DEFAULT_ATTRIBUTE, layout);
	}

	protected void saveLayoutToSession(ActionContext ac, String layout) {
		ac.getSes().put(sessionName, layout);
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
