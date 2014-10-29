package panda.net.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.io.FileNames;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Iterators;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class URLHelper {
	public static final char SEPARATOR = '/';
	public static final String AMP = "&";
	public static final String EAMP = "&amp;";
	
	/**
	 * get schema from url
	 * <pre>
	 *   "http://www.test.com/app" -> "http"
	 *   "https://www.test.com/app" -> "https"
	 * </pre>
	 * @param url url string
	 * @return schema
	 */
	public static String getURLSchema(String url) {
		if (Strings.isEmpty(url)) {
			return url;
		}

		String schema = null;
		int i = url.indexOf("://");
		if (i >= 0) {
			schema = url.substring(0, i);
		}
		
		return schema;
	}
	
	/**
	 * get domain from url
	 * <pre>
	 *   "http://www.test.com/app" -> "www.test.com"
	 * </pre>
	 * @param url url string
	 * @return domain
	 */
	public static String getURLDomain(String url) {
		if (Strings.isEmpty(url)) {
			return url;
		}
		
		int i = url.indexOf("://");
		if (i >= 0) {
			url = Strings.stripStart(url.substring(i + 3), '/');
		}

		i = url.indexOf(SEPARATOR);
		if (i >= 0) {
			url = url.substring(0, i);
		}
		
		i = url.indexOf(':');
		if (i >= 0) {
			url = url.substring(0, i);
		}

		return url;
	}
	
	/**
	 * get root length from url
	 * <pre>
	 *   http://www.test.com     = [http://www.test.com]
	 *   http://www.test.com/    = [http://www.test.com]
	 *   http://www.test.com/app = [http://www.test.com]
	 *   null                    = -1
	 *   ""                      = -1
	 *   /app                    = -1
	 *   app                     = -1
	 * </pre>
	 * @param url url string
	 * @return root length
	 */
	public static int getURLRootLength(String url) {
		if (Strings.isEmpty(url)) {
			return -1;
		}
		
		int i = url.indexOf("://");
		if (i < 0) {
			return -1;
		}

		i = url.indexOf('/', i + 3);
		if (i < 0) {
			return url.length();
		}

		return i;
	}
	
	/**
	 * get root from url
	 * <pre>
	 *   "http://www.test.com/app" -> "http://www.test.com"
	 * </pre>
	 * @param url url string
	 * @return root
	 */
	public static String getURLRoot(String url) {
		int len = getURLRootLength(url);
		return len < 0 ? null : url.substring(0, len);
	}

	/**
	 * Gets the parent path of URL.
	 * <pre>
	 * a/b/c.txt --> a/b
	 * a.txt     --> ""
	 * a/b/c     --> a/b
	 * a/b/c/    --> a/b/c
	 * </pre>
	 * <p>
	 * 
	 * @param url the url to query, null returns null
	 * @return the parent path of the url, or an empty string if none exists
	 */
	public static String getURLParent(String url) {
		if (url == null) {
			return null;
		}
		
		int index = url.lastIndexOf(SEPARATOR);
		return index > 0 ? url.substring(0, index) : Strings.EMPTY;
	}
	
	/**
	 * concat url to base
	 * <pre>
	 *   null                    + *             = null
	 *   ""                      + *             = null
	 *   http://a.b.c            + null          = http://a.b.c
	 *   http://a.b.c            + ""            = http://a.b.c
	 *   http://a.b.c            + http://x.y.z  = http://x.y.z
	 *   http://a.b.c            + /x            = http://a.b.c/x
	 *   http://a.b.c/           + /x            = http://a.b.c/x
	 *   http://a.b.c/d          + /x            = null
	 *   http://a.b.c/d/         + /x            = http://a.b.c/x
	 *   http://a.b.c/d/e        + /x            = http://a.b.c/x
	 *   http://a.b.c            + ../x          = null
	 *   http://a.b.c/           + ../x          = null
	 *   http://a.b.c/d          + ../x          = http://a.b.c/x
	 *   http://a.b.c/d/e        + ../x          = http://a.b.c/x
	 *   http://a.b.c/d/e/       + ../x          = http://a.b.c/d/x
	 *   http://a.b.c/d/e/f      + ../x          = http://a.b.c/d/x
	 *   http://a.b.c            + x             = http://a.b.c/x
	 *   http://a.b.c/           + x             = http://a.b.c/x
	 *   http://a.b.c/d          + x             = http://a.b.c/x
	 *   http://a.b.c/d/         + x             = http://a.b.c/d/x
	 *   http://a.b.c/d/e        + x             = http://a.b.c/d/x
	 * </pre>
	 * @param base base url
	 * @param add append url
	 * @return url url
	 */
	public static String concatURL(String base, String add) {
		if (Strings.isEmpty(add)) {
			return base;
		}
		
		if (getURLRootLength(add) >= 0) {
			return add;
		}

		int prefix = getURLRootLength(base);
		if (prefix < 0) {
			return null;
		}

		String root = base.substring(0, prefix);
		String path = getURLParent(base.substring(prefix));
		String uri;
		
		if (add.charAt(0) == SEPARATOR) {
			uri = normalize(add);
		}
		else {
			int len = path.length();
			if (len > 0 && path.charAt(len - 1) == SEPARATOR) {
				uri = normalize(path + add);
			}
			else {
				uri = normalize(path + SEPARATOR + add);
			}
		}

		if (uri == null) {
			return null;
		}
		
		return root + uri;
	}
	
	public static String normalize(String url) {
		if (url == null) {
			return null;
		}
		
		int size = url.length();
		if (size == 0) {
			return url;
		}
		
		int prefix = getURLRootLength(url);
		if (prefix < 0) {
			prefix = url.charAt(0) == SEPARATOR ? 1 : 0;
		}

		char[] array = new char[size + 2]; // +1 for possible extra slash, +2 for arraycopy
		url.getChars(0, url.length(), array, 0);

		return FileNames.normalize(array, size, prefix, SEPARATOR, true);
	}

	/**
	 * remove '#' anchor from url
	 * @param url url string
	 * @return url
	 */
	public static String removeURLAnchor(String url) {
		return Strings.substringBefore(url, '#');
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param uri request uri
	 * @param params parameters
	 * @return URL
	 */
	public static String buildURL(String uri, Map params) {
		return buildURL(uri, params, false);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param uri request uri
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(String uri, Map params, boolean escapeAmp) {
		return buildURL(uri, params, escapeAmp, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param uri request uri
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @param encoding url encoding
	 * @return URL
	 */
	public static String buildURL(String uri, Map params, boolean escapeAmp, String encoding) {
		return buildURL(null, null, 0, uri, null, params, escapeAmp, encoding);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param scheme scheme
	 * @param host host
	 * @param port port
	 * @param uri request uri
	 * @param params parameters
	 * @return URL
	 */
	public static String buildURL(String scheme, String host, int port, String uri, Map params) {
		return buildURL(scheme, host, port, uri, null, params, false, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param scheme scheme
	 * @param host host
	 * @param port port
	 * @param uri request uri
	 * @param query query string
	 * @return URL
	 */
	public static String buildURL(String scheme, String host, int port, String uri, String query) {
		return buildURL(scheme, host, port, uri, query, null, false, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param scheme scheme
	 * @param host host
	 * @param port port
	 * @param uri request uri
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(String scheme, String host, int port, String uri, Map params, boolean escapeAmp) {
		return buildURL(scheme, host, port, uri, null, params, escapeAmp, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param scheme scheme
	 * @param host host
	 * @param port port
	 * @param uri request uri
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @param encoding url encoding
	 * @return URL
	 */
	public static String buildURL(String scheme, String host, int port, 
			String uri, Map params, boolean escapeAmp, String encoding) {
		return buildURL(scheme, host, port, uri, null, params, escapeAmp, encoding);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param scheme scheme
	 * @param host host
	 * @param port port
	 * @param uri request uri
	 * @param query query string
	 * @param params parameters
	 * @return URL
	 */
	public static String buildURL(String scheme, String host, int port, String uri, String query, Map params) {
		return buildURL(scheme, host, port, uri, query, params, false, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param scheme scheme
	 * @param host host
	 * @param port port
	 * @param uri request uri
	 * @param query query string
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(String scheme, String host, int port, 
			String uri, String query, Map params, boolean escapeAmp) {
		return buildURL(scheme, host, port, uri, query, params, escapeAmp, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param scheme scheme
	 * @param host host
	 * @param port port
	 * @param uri request uri
	 * @param query query string
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @param encoding url encoding
	 * @return URL
	 */
	public static String buildURL(String scheme, String host, int port, 
			String uri, String query, Map params, 
			boolean escapeAmp, String encoding) {
		StringBuilder url = new StringBuilder();

		if (Strings.isNotEmpty(host)) {
			if (Strings.isNotEmpty(scheme)) {
				url.append(scheme).append("://");
			}
			url.append(host);
			appendPort(url, scheme, port);
		}
		
		url.append(uri);

		if (Strings.isNotEmpty(query)) {
			appendQuerySeparator(url, escapeAmp);
			url.append(query);
		}
		
		if (Collections.isNotEmpty(params)) {
			appendQuerySeparator(url, escapeAmp);
			appendQueryString(url, params, escapeAmp, encoding);
		}

		return url.toString();
	}

	public static void appendPort(StringBuilder link, String scheme, int port) {
		if (port > 0) {
			if ((scheme.equals("http") && (port != 80))
					|| (scheme.equals("https") && port != 443)) {
				link.append(":").append(port);
			}
		}
	}

	public static void appendQuerySeparator(StringBuilder link, boolean escapeAmp) {
		if (Strings.contains(link, '?')) {
			if (link.length() > 0 && link.charAt(link.length() - 1) != '?') {
				link.append(escapeAmp ? EAMP : AMP);
			}
		}
		else {
			if (link.length() > 0 && link.charAt(link.length() - 1) != '?') {
				link.append('?');
			}
		}
	}
	
	/**
	 * @param params parameter map
	 */
	public static String buildQueryString(Map params) {
		return buildQueryString(params, Charsets.UTF_8);
	}

	/**
	 * @param link link
	 * @param params parameter map
	 */
	public static void appendQueryString(Appendable link, Map params) {
		appendQueryString(link, params, Charsets.UTF_8);
	}

	/**
	 * @param params parameter map
	 * @param encoding encoding
	 */
	public static String buildQueryString(Map params, String encoding) {
		return buildQueryString(params, false, encoding);
	}
	
	/**
	 * @param link link
	 * @param params parameter map
	 * @param encoding encoding
	 */
	public static void appendQueryString(Appendable link, Map params, String encoding) {
		appendQueryString(link, params, false, encoding);
	}
	
	/**
	 * @param params parameter map
	 * @param escapeAmp escape &
	 * @param encoding encoding
	 */
	public static String buildQueryString(Map params, boolean escapeAmp, String encoding) {
		return buildQueryString(params, escapeAmp ? EAMP : AMP, encoding);
	}

	/**
	 * @param link link
	 * @param params parameter map
	 * @param escapeAmp escape &
	 * @param encoding encoding
	 */
	public static void appendQueryString(Appendable link, Map params, boolean escapeAmp, String encoding) {
		appendQueryString(link, params, escapeAmp ? EAMP : AMP, encoding);
	}

	/**
	 * @param params parameter map
	 * @param encoding encoding
	 * @param paramSeparator parameter separator
	 */
	public static String buildQueryString(Map params, String paramSeparator, String encoding) {
		StringBuilder link = new StringBuilder();
		appendQueryString(link, params, paramSeparator, encoding);
		return link.toString();
	}
	
	/**
	 * @param link link
	 * @param params parameter map
	 * @param paramSeparator parameter separator
	 * @param encoding encoding
	 */
	public static void appendQueryString(Appendable link, Map params, String paramSeparator, String encoding) {
		if (Collections.isEmpty(params)) {
			return;
		}

		try {
			Iterator iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String name = (String) entry.getKey();
				Object value = entry.getValue();

				if (Iterators.isIterable(value)) {
					for (Iterator it = Iterators.asIterator(value); it.hasNext();) {
						Object pv = it.next();
						buildQuerySubstring(name, pv, encoding, link);

						if (it.hasNext()) {
							link.append(paramSeparator);
						}
					}
				}
				else {
					buildQuerySubstring(name, value, encoding, link);
				}

				if (iter.hasNext()) {
					link.append(paramSeparator);
				}
			}
		}
		catch (IOException e) {
			Exceptions.wrapThrow(e);
		}
	}

	private static void buildQuerySubstring(String name, Object value, String encoding, Appendable writer) throws IOException {
		writer.append(name);
		writer.append('=');
		writer.append(value == null ? "" : encodeURL(value.toString(), encoding));
	}

	/**
	 * encodes the URL (UTF-8)
	 * using {@link java.net.URLEncoder#encode}.
	 * 
	 * @param url url to encode
	 * @return the encoded string
	 */
	public static String encodeURL(String url) {
		return encodeURL(url, Charsets.UTF_8);
	}

	/**
	 * encodes the URL
	 * using {@link java.net.URLEncoder#encode}.
	 * 
	 * @param url url to encode
	 * @param encoding encoding
	 * @return the encoded string
	 */
	public static String encodeURL(String url, String encoding) {
		if (Strings.isEmpty(url)) {
			return url;
		}
		
		try {
			return URLEncoder.encode(url, encoding);
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * decode url use UTF-8 encoding
	 * @param input input string
	 * @return decoded string
	 */
	public static String decodeURL(String input) {
		return decodeURL(input, Charsets.UTF_8);
	}

	/**
	 * decode url use the specified encoding
	 * @param input input string
	 * @param encoding encoding
	 * @return decoded string
	 */
	public static String decodeURL(String input, String encoding) {
		if (Strings.isEmpty(input)) {
			return input;
		}
		
		try {
			return URLDecoder.decode(input, encoding);
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * @param queryString query string
	 * @return parameter map
	 */
	public static Map<String, Object> parseQueryString(String queryString) {
		return parseQueryString(queryString, Charsets.UTF_8);
	}

	/**
	 * @param queryString query string
	 * @return parameter map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseQueryString(String queryString, String encoding) {
		Map<String, Object> qparams = new LinkedHashMap<String, Object>();
		if (queryString == null) {
			return qparams;
		}
		
		String[] params = queryString.split("&");
		for (int a = 0; a < params.length; a++) {
			if (params[a].trim().length() > 0) {
				String[] tmpParams = params[a].split("=");
				String paramName = null;
				String paramValue = "";
				if (tmpParams.length > 0) {
					paramName = tmpParams[0];
				}
				if (tmpParams.length > 1) {
					paramValue = tmpParams[1];
				}
				if (paramName != null) {
					String translatedParamValue = decodeURL(paramValue, encoding);

					if (qparams.containsKey(paramName)) {
						Object currentParam = qparams.get(paramName);
						if (currentParam instanceof String) {
							List<String> ss = new ArrayList<String>();
							ss.add((String)currentParam);
							ss.add(translatedParamValue);
							qparams.put(paramName, ss);
						}
						else {
							List<String> ss = (List<String>)currentParam;
							ss.add(translatedParamValue);
							qparams.put(paramName, ss);
						}
					}
					else {
						qparams.put(paramName, translatedParamValue);
					}
				}
			}
		}
		return qparams;
	}
}
