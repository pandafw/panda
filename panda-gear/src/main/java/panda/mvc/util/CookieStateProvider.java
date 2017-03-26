package panda.mvc.util;

import javax.servlet.http.Cookie;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.crypto.Encrypts;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.servlet.HttpServlets;


/**
 * CookieStateProvider
 */
@IocBean(type=StateProvider.class, scope=Scope.REQUEST)
public class CookieStateProvider implements StateProvider {
	private final static String DOMAIN = "cookie-state-domain";
	private final static String PATH = "cookie-state-path";
	private final static String MAXAGE = "cookie-state-maxage";
	private final static String SECURE = "cookie-state-secure";
	
	@IocInject
	protected ActionContext context;

	/**
	 * encrypt key
	 */
	@IocInject(value=MvcConstants.COOKIE_SECRET_KEY, required=false)
	protected String secret = Encrypts.DEFAULT_KEY;
	
	/**
	 * encrypt cipher
	 */
	@IocInject(value=MvcConstants.COOKIE_SECRET_CIPHER, required=false)
	protected String cipher = Encrypts.DEFAULT_CIPHER;

	private String prefix;
	private String domain;
	private String path;
	private Integer maxAge;
	private Boolean secure;
	
	/**
	 * Constructor
	 */
	public CookieStateProvider() {
	}
	
	/**
	 * Constructor
	 * @param prefix prefix
	 */
	public CookieStateProvider(String prefix) {
		setPrefix(prefix);
	}
	
	/**
	 * @param textProvider text provider
	 */
	@IocInject
	public void setTextProvider(TextProvider textProvider) {
		domain = textProvider.getText(DOMAIN, (String)null);
		path = textProvider.getText(PATH, (String)null);
		maxAge = textProvider.getTextAsInt(MAXAGE, 0);
		secure = textProvider.getTextAsBoolean(SECURE);
	}
	
	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return maxAge;
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * get key name
	 * @param name state name
	 * @return key name
	 */
	private String getKey(String name) {
		return prefix == null ? name : prefix + name;
	}

	/**
	 * encode value 
	 * @param value value
	 * @return encoded value
	 */
	private String encodeValue(Object value) {
		try {
			return value == null ? Strings.EMPTY : Encrypts.encrypt(value.toString(), secret, cipher);
		}
		catch (Exception e) {
			return Strings.EMPTY;
		}
	}
	
	/**
	 * decode value 
	 * @param value value
	 * @return encoded value
	 */
	private String decodeValue(Object value) {
		try {
			return value == null ? Strings.EMPTY : Encrypts.decrypt(value.toString(), secret, cipher);
		}
		catch (Exception e) {
			return Strings.EMPTY;
		}
	}

	/**
	 * Save state
	 * @param name state name
	 * @param value state value
	 */
	public StateProvider saveState(String name, Object value) {
		String key = getKey(name);
		String val = encodeValue(value);

		Cookie c = new Cookie(key, val);

		if (domain != null) {
			c.setDomain(domain);
		}
		
		if (path == null) {
			path = context.getRequest().getRequestURI();
		}
		if (Strings.startsWithChar(path, '~')){
			path = context.getBase() + path.substring(1);
		}
		c.setPath(path);

		if (secure != null) {
			c.setSecure(secure);
		}

		if (value == null) {
			c.setMaxAge(0);
		}
		else if (maxAge != null) {
			c.setMaxAge(maxAge);
		}
		
		context.getResponse().addCookie(c);
		
		return this;
	}
	
	/**
	 * Load state
	 * @param name state name
	 * @return state value 
	 */
	public Object loadState(String name) {
		String key = getKey(name);
		Cookie c = HttpServlets.getCookie(context.getRequest(), key);
		if (c != null) {
			return decodeValue(c.getValue());
		}
		return null;
	}
}
