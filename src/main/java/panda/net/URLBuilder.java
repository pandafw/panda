package panda.net;

import java.util.Iterator;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.cast.Castors;
import panda.lang.Charsets;
import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * Implementations of this interface can be used to build a URL
 */
public class URLBuilder {
	public static final char SEPARATOR = '/';
	public static final String AMP = "&";
	public static final String EAMP = "&amp;";
	
	public static final int SUPPRESS_NONE = 0;
	public static final int SUPPRESS_NULL = 1;
	public static final int SUPPRESS_EMPTY = 2;

	protected Beans beans = Beans.i();
	
	protected Castors castors = Castors.i();
	
	protected String scheme;

	protected String host;

	protected int port;
	
	protected String path;

	protected String query;
	
	protected Object params;

	protected String anchor;

	protected boolean escapeAmp;

	protected int suppress = SUPPRESS_NULL;
	
	protected String encoding = Charsets.UTF_8;


	/**
	 * @return the beans
	 */
	public Beans getBeans() {
		return beans;
	}

	/**
	 * @param beans the beans to set
	 */
	public void setBeans(Beans beans) {
		this.beans = beans;
	}

	/**
	 * @return the castors
	 */
	public Castors getCastors() {
		return castors;
	}

	/**
	 * @param castors the castors to set
	 */
	public void setCastors(Castors castors) {
		this.castors = castors;
	}

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
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
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
	public Object getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Object params) {
		this.params = params;
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
	 * @return the suppress
	 */
	public int getSuppress() {
		return suppress;
	}

	/**
	 * @param suppress the suppress to set
	 */
	public void setSuppress(int suppress) {
		this.suppress = suppress;
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
	 * build url
	 * @return url
	 */
	public String build() {
		StringBuilder url = new StringBuilder();

		if (Strings.isNotEmpty(scheme)) {
			url.append(scheme).append("://");
		}

		if (Strings.isNotEmpty(host)) {
			url.append(host);
			appendPort(url, scheme, port);
		}
		
		if (Strings.isNotEmpty(path)) {
			url.append(path);
		}

		if (Strings.isNotEmpty(query)) {
			appendQuerySeparator(url);

			Map<String, Object> qs = URLHelper.parseQueryString(query);
			appendQueryParameters(url, qs);
		}
		
		if (params != null) {
			appendQuerySeparator(url);
			appendQueryParameters(url, params);
		}

		if (Strings.isNotEmpty(anchor)) {
			url.append('#').append(anchor);
		}
		return url.toString();
	}

	protected void appendPort(StringBuilder link, String scheme, int port) {
		if (port > 0) {
			if ((scheme.equals("http") && (port != 80))
					|| (scheme.equals("https") && port != 443)) {
				link.append(":").append(port);
			}
		}
	}

	protected void appendQuerySeparator(StringBuilder link) {
		if (link.length() <= 0) {
			return;
		}
		
		if (Strings.contains(link, '?')) {
			if (link.charAt(link.length() - 1) != '?') {
				link.append(escapeAmp ? EAMP : AMP);
			}
		}
		else {
			if (link.charAt(link.length() - 1) != '?') {
				link.append('?');
			}
		}
	}
	
	/**
	 * @param link link
	 * @param params parameter map
	 * @param paramSeparator parameter separator
	 * @param encoding encoding
	 */
	@SuppressWarnings("unchecked")
	protected void appendQueryParameters(StringBuilder link, Object params) {
		if (params == null) {
			return;
		}

		boolean next = false;
		boolean noClass = !(params instanceof Map);

		BeanHandler bh = beans.getBeanHandler(params.getClass());
		String[] pns = bh.getReadPropertyNames(params);
		
		Iterator iter = Iterators.asIterator(pns);
		while (iter.hasNext()) {
			String name = (String) iter.next();
			if (noClass && "class".equals(name)) {
				continue;
			}

			Object value = bh.getPropertyValue(params, name);
			if (Iterators.isIterable(value)) {
				for (Iterator it = Iterators.asIterator(value); it.hasNext();) {
					Object pv = it.next();
					if (pv == null && (suppress & SUPPRESS_NULL) != 0) {
						continue;
					}
					if (Objects.isEmpty(pv) && (suppress & SUPPRESS_EMPTY) != 0) {
						continue;
					}

					if (next) {
						link.append(escapeAmp ? EAMP : AMP);
					}
					else {
						next = true;
					}
					appendQueryParameter(link, name, pv);
				}
			}
			else {
				if (value == null && (suppress & SUPPRESS_NULL) != 0) {
					continue;
				}
				if (Objects.isEmpty(value) && (suppress & SUPPRESS_EMPTY) != 0) {
					continue;
				}

				if (next) {
					link.append(escapeAmp ? EAMP : AMP);
				}
				else {
					next = true;
				}
				appendQueryParameter(link, name, value);
			}
		}
	}

	protected void appendQueryParameter(StringBuilder url, String name, Object value) {
		url.append(name);
		url.append('=');
		String s = castString(value);
		url.append(value == null ? "" : URLHelper.encodeURL(s, encoding));
	}
	
	protected String castString(Object value) {
		return castors.cast(value, String.class);
	}
	
	//------------------------------------------------------------------------
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param path request path
	 * @param params parameters
	 * @return URL
	 */
	public static String buildURL(String path, Object params) {
		URLBuilder ub = new URLBuilder();
		ub.setPath(path);
		ub.setParams(params);
		return ub.build();
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param path request path
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(String path, Object params, boolean escapeAmp) {
		URLBuilder ub = new URLBuilder();
		ub.setPath(path);
		ub.setParams(params);
		ub.setEscapeAmp(escapeAmp);
		return ub.build();
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param path request path
	 * @param params parameters
	 * @param encoding url encoding
	 * @return URL
	 */
	public static String buildURL(String path, Object params, String encoding) {
		URLBuilder ub = new URLBuilder();
		ub.setPath(path);
		ub.setParams(params);
		ub.setEncoding(encoding);
		return ub.build();
	}
}
