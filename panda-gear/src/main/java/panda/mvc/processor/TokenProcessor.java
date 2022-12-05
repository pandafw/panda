package panda.mvc.processor;

import java.lang.reflect.Method;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.View;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.view.Views;
import panda.servlet.HttpServlets;
import panda.util.crypto.Cryptor;
import panda.util.crypto.Token;

@IocBean
public class TokenProcessor extends AbstractActionProcessor {
	private static final Log log = Logs.getLog(TokenProcessor.class);

	/**
	 * DEFAULT_HEADER = "X-WW-TOKEN";
	 */
	public static final String DEFAULT_HEADER = "X-WW-TOKEN";

	/**
	 * DEFAULT_ATTRIBUTE = "WW_TOKEN";
	 */
	public static final String DEFAULT_ATTRIBUTE = "WW_TOKEN";

	/**
	 * DEFAULT_PARAMETER = "__token";
	 */
	public static final String DEFAULT_PARAMETER = "__token";

	/**
	 * DEFAULT_COOKIE = "WW_TOKEN";
	 */
	public static final String DEFAULT_COOKIE = DEFAULT_ATTRIBUTE;

	/**
	 * DEFAULT_COOKIE_MAXAGE = 60 * 60 * 24 * 30; //1M
	 */
	public static final int DEFAULT_COOKIE_MAXAGE = DateTimes.SEC_MONTH; // 1M

	@IocInject(value = MvcConstants.TOKEN_HEADER_NAME, required = false)
	protected String headerName = DEFAULT_HEADER;

	@IocInject(value = MvcConstants.TOKEN_PARAMETER_NAME, required = false)
	protected String parameterName = DEFAULT_PARAMETER;

	@IocInject(value = MvcConstants.TOKEN_REQUEST_ATTR, required = false)
	protected String requestName = DEFAULT_ATTRIBUTE;

	@IocInject(value = MvcConstants.TOKEN_SESSION_ATTR, required = false)
	protected String sessionName = DEFAULT_ATTRIBUTE;

	@IocInject(value = MvcConstants.TOKEN_COOKIE_NAME, required = false)
	protected String cookieName = DEFAULT_COOKIE;

	@IocInject(value = MvcConstants.TOKEN_COOKIE_DOMAIN, required = false)
	protected String cookieDomain;

	@IocInject(value = MvcConstants.TOKEN_COOKIE_PATH, required = false)
	protected String cookiePath;

	@IocInject(value = MvcConstants.TOKEN_COOKIE_MAXAGE, required = false)
	protected int cookieMaxAge = DEFAULT_COOKIE_MAXAGE;

	@IocInject(value = MvcConstants.TOKEN_SAVE_TO_SESSION, required = false)
	protected boolean saveToSession = false;

	@IocInject(value = MvcConstants.TOKEN_SAVE_TO_COOKIE, required = false)
	protected boolean saveToCookie = true;

	@IocInject
	protected Cryptor cryptor;

	/**
	 * Constructor
	 */
	public TokenProcessor() {
	}

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public void process(ActionContext ac) {
		if (!validate(ac)) {
			ac.getActionAlert().addError(ac.getText().getText("servlet-error-message-400", "Invalid Request Token."));
			doErrorView(ac);
			return;
		}

		doNext(ac);
		return;
	}

	private boolean isProtectMethod(ActionContext ac, TokenProtect tp) {
		if (tp.value().length == 0) {
			return false;
		}

		if (tp.value().length == 1 && TokenProtect.ALL.equals(tp.value()[0])) {
			return true;
		}

		String method = Strings.upperCase(ac.getRequest().getMethod());
		for (String m : tp.value()) {
			if (Strings.equals(m, method)) {
				return true;
			}
		}

		return false;
	}

	protected boolean validate(ActionContext ac) {
		Method method = ac.getMethod();

		TokenProtect tp = method.getAnnotation(TokenProtect.class);
		if (tp == null) {
			tp = ac.getAction().getClass().getAnnotation(TokenProtect.class);
		}

		if (tp == null) {
			return true;
		}

		if (!isProtectMethod(ac, tp)) {
			return true;
		}

		Token stoken = getSourceToken(ac);
		if (stoken == null) {
			log.warn("Missing cookie or session token: " + ac.getPath());
			return false;
		}

		Token rtoken = getRequestToken(ac);
		if (rtoken == null) {
			return false;
		}

		if (rtoken.getTimestamp() <= 0) {
			log.warn("Request token (" + rtoken + ") has invalid timestamp: " + ac.getPath());
			return false;
		}

		if (!Token.isSameSecret(stoken, rtoken)) {
			log.warn("Request token (" + rtoken + ") is not same as (" + stoken + "): " + ac.getPath());
			return false;
		}

		if (tp.expire() > 0) {
			long now = System.currentTimeMillis();
			if (rtoken.getTimestamp() + tp.expire() < now) {
				log.warn("Request token (" + rtoken + ") expired " + tp.expire() + " ms: " + ac.getPath());
				return false;
			}
		}

		return true;
	}

	protected void doErrorView(ActionContext ac) {
		View view = Views.createErrorView(ac);
		if (view == null) {
			view = Views.seBadRequest(ac);
		}
		view.render(ac);
	}

	protected String encrypt(String s) {
		return cryptor.encrypt(s);
	}

	protected String decrypt(String s) {
		return cryptor.decrypt(s);
	}

	protected Token parseToken(String s) {
		try {
			if (Strings.isEmpty(s)) {
				return null;
			}

			String token = decrypt(s);
			return Token.parse(token);
		} catch (Throwable e) {
			return null;
		}
	}

	protected Token getSourceToken(ActionContext ac) {
		Token token = (Token)ac.getReq().get(requestName);
		if (token == null) {
			token = getTokenFromSession(ac);
			if (token == null) {
				token = getTokenFromCookie(ac);
			}
			if (token != null) {
				ac.getReq().put(requestName, token);
			}
		}

		return token;
	}

	protected Token getTokenFromSession(ActionContext ac) {
		HttpSession session = ac.getRequest().getSession(false);
		if (session != null && Strings.isNotEmpty(sessionName)) {
			Token token = (Token)session.getAttribute(sessionName);
			if (token != null && log.isDebugEnabled()) {
				log.debug("Get Session Token " + sessionName + ": " + token);
			}
			return token;
		}

		return null;
	}

	protected Token getTokenFromCookie(ActionContext ac) {
		Token token = parseToken(HttpServlets.getCookieValue(ac.getRequest(), cookieName));
		if (token != null && log.isDebugEnabled()) {
			log.debug("Get Cookie Token " + cookieName + ": " + token);
		}
		return token;
	}

	protected Token getRequestToken(ActionContext ac) {
		Token token = getTokenFromParameter(ac);
		if (token == null) {
			token = getTokenFromHeader(ac);
		}
		return token;
	}

	protected Token getTokenFromParameter(ActionContext ac) {
		Token token = parseToken(ac.getRequest().getParameter(parameterName));
		if (token != null && log.isDebugEnabled()) {
			log.debug("Get Parameter Token " + parameterName + ": " + token);
		}
		return token;
	}

	protected Token getTokenFromHeader(ActionContext ac) {
		Token token = parseToken(ac.getRequest().getHeader(headerName));
		if (token != null && log.isDebugEnabled()) {
			log.debug("Get Header Token " + headerName + ": " + token);
		}
		return token;
	}

	protected void saveToken(ActionContext ac, Token token) {
		ac.getReq().put(requestName, token);

		if (saveToSession) {
			ac.getSes().put(sessionName, token);
			if (log.isDebugEnabled()) {
				log.debug("Save Session Token " + sessionName + ": " + token);
			}
		}
		if (saveToCookie && Strings.isNotEmpty(cookieName)) {
			saveTokenToCookie(ac, token);
		}
	}

	protected void saveTokenToCookie(ActionContext ac, Token token) {
		String et = encrypt(token.getToken());

		Cookie c = new Cookie(cookieName, et);
		if (Strings.isNotEmpty(cookieDomain)) {
			c.setDomain(cookieDomain);
		}

		if (Strings.isNotEmpty(cookiePath)) {
			c.setPath(cookiePath);
		} else {
			c.setPath(ac.getRequest().getContextPath());
			if (Strings.isEmpty(c.getPath())) {
				c.setPath("/");
			}
		}

		c.setMaxAge(cookieMaxAge);

		ac.getResponse().addCookie(c);

		if (log.isDebugEnabled()) {
			log.debug("Save Cookie Token " + cookieName + ": " + token);
		}
	}

	public String refreshToken(ActionContext ac) {
		Token token = getSourceToken(ac);
		if (token == null) {
			token = new Token();
			saveToken(ac, token);
		} else {
			// refresh salt and timestamp
			token.refresh();
		}
		return encrypt(token.getToken());
	}
}
