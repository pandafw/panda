package panda.mvc.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
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
	@IocInject
	protected HttpServletRequest request;

	protected String scheme;

	protected int port;
	
	protected String action;

	protected String query;
	
	protected Map params;

	protected boolean includeParams;

	protected boolean includeContext;

	protected String anchor;

	protected boolean escapeAmp;

	@IocInject(value = MvcConstants.PANDA_URL_ENCODE, required = false)
	protected String encoding = Charsets.UTF_8;

	protected boolean forceAddSchemeHostAndPort;

	@SuppressWarnings("unchecked")
	@Override
	public String build() {
		String uri;
		if (action != null) {
			uri = action;
			
			// Check if context path needs to be added
			if (action.startsWith("/")) {
				if (includeContext) {
					String base = request.getContextPath();
					if (!base.equals("/")) {
						uri = base + action;
					}
				}
			}
			else {
				String base = HttpServlets.getRequestURI(request);
				uri = URLHelper.concatURL(base, action);
			}
		}
		else {
			// Go to "same page"
			uri = HttpServlets.getRequestURI(request);
		}

		String qs = query;
		Map ps = params;
		if (GET.equals(includeParams)) {
			if (qs == null) {
				qs = HttpServlets.getRequestQueryString(request);
			}
			else {
				qs += '&' + HttpServlets.getRequestQueryString(request);
			}
		}
		else if (ALL.equals(includeParams)) {
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
		link.append(ServletURLHelper.buildURL(request, scheme, port, uri, qs, ps, forceAddSchemeHostAndPort,
			escapeAmp, encoding));

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
	public boolean isIncludeParams() {
		return includeParams;
	}

	/**
	 * @param includeParams the includeParams to set
	 */
	public void setIncludeParams(boolean includeParams) {
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
