package panda.mvc.util;

import javax.servlet.http.Cookie;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Encrypts;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.servlet.HttpServlets;


/**
 * CookieStateProvider
 */
@IocBean(type=StateProvider.class, scope=Scope.REQUEST)
public class CookieStateProvider implements StateProvider {
	private final static String DOMAIN = "cookie-state-domain";
	private final static String PATH = "cookie-state-path";
	private final static String EXPIRE = "cookie-state-expire";
	private final static String SECURE = "cookie-state-secure";
	
	private ActionContext context;

	private String prefix;
	private String domain;
	private String path;
	private Integer expire;
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
	 * @param context the context to set
	 */
	@IocInject
	public void setContext(ActionContext context) {
		this.context = context;
	}

	/**
	 * @param textProvider text provider
	 */
	@IocInject
	public void setTextProvider(TextProvider textProvider) {
		domain = textProvider.getText(DOMAIN, (String)null);
		path = textProvider.getText(PATH, (String)null);
		expire = textProvider.getTextAsInt(EXPIRE, 0);
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
	 * @return the expire
	 */
	public int getExpire() {
		return expire;
	}

	/**
	 * @param expire the expire to set
	 */
	public void setExpire(int expire) {
		this.expire = expire;
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
			return value == null ? Strings.EMPTY : Encrypts.encrypt(value.toString());
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
			return value == null ? Strings.EMPTY : Encrypts.decrypt(value.toString());
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
		c.setPath(path);

		if (secure != null) {
			c.setSecure(secure);
		}

		if (value == null) {
			c.setMaxAge(0);
		}
		else if (expire != null) {
			c.setMaxAge(expire);
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
