package panda.mvc.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.MvcConstants;
import panda.net.http.URLHelper;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLHelper;

/**
 * Implemntations of this interface can be used to build a URL
 */
@IocBean(type = UrlBuilder.class, singleton = false)
public class ServletUrlBuilder implements UrlBuilder {
	public static final String SUPPRESS_NONE = "none";
	public static final String SUPPRESS_EMPTY = "empty";
	public static final String SUPPRESS_NULL = "null";
	
	@IocInject
	protected HttpServletRequest request;

	protected String scheme;

	protected int port;
	
	protected String action;

	protected String query;
	
	protected Map params;

	protected String includeParams;

	@IocInject(value = MvcConstants.UI_URL_INCLUDE_CONTEXT, required = false)
	protected boolean includeContext = true;

	protected String anchor;

	protected boolean escapeAmp;

	protected String suppressParam;
	
	@IocInject(value = MvcConstants.UI_URL_ENCODE, required = false)
	protected String encoding = Charsets.UTF_8;

	protected boolean forceAddSchemeHostAndPort;

	@SuppressWarnings("unchecked")
	@Override
	public String build() {
		String uri;
		if (Strings.isNotEmpty(action)) {
			uri = action;
			char c = action.charAt(0);
			
			if (c == '~') {
				// resolve URL
				String base = HttpServlets.getRequestURI(request);
				String path = Strings.stripStart(action.substring(1), '/');
				uri = URLHelper.resolveURL(base, path);
			}
			else if (c == '+') {
				// append path
				String base = HttpServlets.getRequestURI(request);
				uri = URLHelper.appendPath(base, action.substring(1));
			}
			else if (c == '/') {
				if (includeContext) {
					uri = request.getContextPath() + action;
				}
			}
		}
		else {
			// Go to "same page"
			uri = HttpServlets.getRequestURI(request);
		}

		Map ps = params;
		if (GET.equalsIgnoreCase(includeParams)) {
			String qs = HttpServlets.getRequestQueryString(request);
			Map<String, Object> qsm = URLHelper.parseQueryString(qs);
			if (Collections.isNotEmpty(qsm)) {
				if (ps == null) {
					ps = new LinkedHashMap();
				}
				else {
					ps = new LinkedHashMap(ps);
				}
				for (Entry<String, Object> en : qsm.entrySet()) {
					if (!ps.containsKey(en.getKey())) {
						ps.put(en.getKey(), en.getValue());
					}
				}
			}
		}
		else if (ALL.equalsIgnoreCase(includeParams)) {
			if (ps == null) {
				ps = request.getParameterMap();
			}
			else {
				ps = new LinkedHashMap(ps);
				Map<String, String[]> qm = request.getParameterMap();
				for (Entry<String, String[]> en : qm.entrySet()) {
					if (!ps.containsKey(en.getKey())) {
						ps.put(en.getKey(), en.getValue());
					}
				}
			}
		}

		StringBuilder link = new StringBuilder();
		link.append(ServletURLHelper.buildURL(request, scheme, port, uri, query, ps, forceAddSchemeHostAndPort,
			escapeAmp, getSuppressOption(), encoding));

		if (Strings.isNotEmpty(anchor)) {
			link.append('#').append(anchor);
		}
		return link.toString();
	}

	// --------------------
	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the params
	 */
	public Map getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map params) {
		this.params = params;
	}

	/**
	 * @return the includeParams
	 */
	public String getIncludeParams() {
		return includeParams;
	}

	/**
	 * @param includeParams the includeParams to set
	 */
	public void setIncludeParams(String includeParams) {
		this.includeParams = includeParams;
	}

	/**
	 * @return the includeContext
	 */
	public boolean isIncludeContext() {
		return includeContext;
	}

	/**
	 * @param includeContext the includeContext to set
	 */
	public void setIncludeContext(boolean includeContext) {
		this.includeContext = includeContext;
	}

	/**
	 * @return the anchor
	 */
	public String getAnchor() {
		return anchor;
	}

	/**
	 * @param anchor the anchor to set
	 */
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	/**
	 * @return the escapeAmp
	 */
	public boolean isEscapeAmp() {
		return escapeAmp;
	}

	private int getSuppressOption() {
		if (SUPPRESS_EMPTY.equalsIgnoreCase(suppressParam)) {
			return URLHelper.SUPPRESS_EMPTY;
		}
		if (SUPPRESS_NONE.equalsIgnoreCase(suppressParam)) {
			return URLHelper.SUPPRESS_NONE;
		}
		return URLHelper.SUPPRESS_NULL;
	}
	
	/**
	 * @return the suppress
	 */
	public String getSuppressParam() {
		return suppressParam;
	}

	/**
	 * @param suppress the suppress to set
	 */
	public void setSuppressParam(String suppress) {
		this.suppressParam = suppress;
	}

	/**
	 * @param escapeAmp the escapeAmp to set
	 */
	public void setEscapeAmp(boolean escapeAmp) {
		this.escapeAmp = escapeAmp;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the forceAddSchemeHostAndPort
	 */
	public boolean isForceAddSchemeHostAndPort() {
		return forceAddSchemeHostAndPort;
	}

	/**
	 * @param forceAddSchemeHostAndPort the forceAddSchemeHostAndPort to set
	 */
	public void setForceAddSchemeHostAndPort(boolean forceAddSchemeHostAndPort) {
		this.forceAddSchemeHostAndPort = forceAddSchemeHostAndPort;
	}
}
