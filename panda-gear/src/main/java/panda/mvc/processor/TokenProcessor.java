package panda.mvc.processor;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.View;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;
import panda.servlet.HttpServlets;
import panda.util.crypto.Token;

@IocBean
public class TokenProcessor extends AbstractProcessor {
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
	public static final int DEFAULT_COOKIE_MAXAGE = DateTimes.SEC_MONTH; //1M

	@IocInject(value=MvcConstants.TOKEN_HEADER_NAME, required=false)
	protected String headerName = DEFAULT_HEADER;

	@IocInject(value=MvcConstants.TOKEN_PARAMETER_NAME, required=false)
	protected String parameterName = DEFAULT_PARAMETER;

	@IocInject(value=MvcConstants.TOKEN_REQUEST_ATTR, required=false)
	protected String requestName = DEFAULT_ATTRIBUTE;

	@IocInject(value=MvcConstants.TOKEN_SESSION_ATTR, required=false)
	protected String sessionName = DEFAULT_ATTRIBUTE;

	@IocInject(value=MvcConstants.TOKEN_COOKIE_NAME, required=false)
	protected String cookieName = DEFAULT_COOKIE;

	@IocInject(value=MvcConstants.TOKEN_COOKIE_DOMAIN, required=false)
	protected String cookieDomain;

	@IocInject(value=MvcConstants.TOKEN_COOKIE_PATH, required=false)
	protected String cookiePath;

	@IocInject(value=MvcConstants.TOKEN_COOKIE_MAXAGE, required=false)
	protected int cookieMaxAge = DEFAULT_COOKIE_MAXAGE;

	@IocInject(value=MvcConstants.TOKEN_SAVE_TO_SESSION, required=false)
	protected boolean saveToSession = false;

	@IocInject(value=MvcConstants.TOKEN_SAVE_TO_COOKIE, required=false)
	protected boolean saveToCookie = true;

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
		Token token = null;

		HttpSession session = ac.getRequest().getSession(false);
		if (session != null) {
			token = (Token)session.getAttribute(sessionName);
		}

		if (token == null && Strings.isNotEmpty(cookieName)) {
			token = getTokenFromCookie(ac);
		}

		if (validate(ac, token)) {
			saveToken(ac, token);
			doNext(ac);
			return;
		}

		ac.getActionAlert().addError(ac.getText().getText("servlet-error-message-400", "Invalid Request Token."));
		saveToken(ac, token);
		doErrorView(ac);
	}

	public static final Set<String> UNPROTECT_METHODS = Arrays.toSet(
		HttpMethod.GET, HttpMethod.HEAD, HttpMethod.OPTIONS, HttpMethod.TRACE);
	
	public static boolean isUnProtectMethod(String method) {
		method = Strings.upperCase(method);
		return UNPROTECT_METHODS.contains(method);
	}
	
	protected boolean validate(ActionContext ac, Token token) {
		if (isUnProtectMethod(ac.getRequest().getMethod())) {
			return true;
		}
		
		Method method = ac.getMethod();

		TokenProtect tp = method.getAnnotation(TokenProtect.class);
		if (tp == null) {
			tp = ac.getAction().getClass().getAnnotation(TokenProtect.class);
		}
		
		if (tp != null) {
			if (tp.value()) {
				if (token == null) {
					log.warn("Missing cookie or session token!");
					return false;
				}
				
				Token rtoken = getRequestToken(ac);
				if (rtoken == null) {
					log.warn("Missing request token!");
					return false;
				}
				
				if (!Token.isSameSecret(token, rtoken)) {
					log.warn("Request token (" + rtoken + ") is not same as (" + token + ")!");
					return false;
				}
				
				if (tp.expire() > 0) {
					if (token.getTimestamp() + tp.expire() < rtoken.getTimestamp()) {
						log.warn("Request token (" + rtoken + ") expired " + tp.expire() + " ms than (" + token + ")!"); 
						return false;
					}
				}
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

	protected Token getTokenFromCookie(ActionContext ac) {
		return Token.parse(HttpServlets.getCookieValue(ac.getRequest(), cookieName));
	}

	protected Token getRequestToken(ActionContext ac) {
		Token token = getTokenFromParameter(ac);
		if (token == null) {
			token = getTokenFromHeader(ac);
		}
		return token;
	}
	
	protected Token getTokenFromParameter(ActionContext ac) {
		return Token.parse(ac.getRequest().getParameter(parameterName));
	}

	protected Token getTokenFromHeader(ActionContext ac) {
		return Token.parse(ac.getRequest().getHeader(headerName));
	}

	protected void saveToken(ActionContext ac, Token token) {
		// refresh salt and timestamp of token
		token = new Token(token);

		ac.getReq().put(requestName, token);
		
		if (saveToSession) {
			ac.getSes().put(sessionName, token);
		}
		if (saveToCookie && Strings.isNotEmpty(cookieName)) {
			saveTokenToCookie(ac, token);
		}
	}

	protected void saveTokenToCookie(ActionContext ac, Token token) {
		Cookie c = new Cookie(cookieName, token.getToken());
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
	
	public String getTokenString(ActionContext ac) {
		Token token = (Token)ac.getReq().get(requestName);
		if (token == null) {
			token = new Token();
		}
		return token.getToken();
	}
}
