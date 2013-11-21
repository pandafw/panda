package panda.net.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.io.FileNames;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
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
	 * @param base
	 * @param add
	 * @return url
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
	 * @return URL
	 */
	public static String buildURL(String uri, Map params, boolean escapeAmp, String encoding) {
		StringBuilder link = new StringBuilder();

		link.append(uri);

		if (Collections.isNotEmpty(params)) {
			appendParamSeparator(link, escapeAmp);
			buildParametersString(link, params, escapeAmp, encoding);
		}

		return link.toString();
	}

	public static void appendParamSeparator(StringBuilder link, boolean escapeAmp) {
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
	public static String buildParametersString(Map params) {
		return buildParametersString(params, Charsets.UTF_8);
	}

	/**
	 * @param link link
	 * @param params parameter map
	 */
	public static void buildParametersString(Appendable link, Map params) {
		buildParametersString(link, params, Charsets.UTF_8);
	}

	/**
	 * @param params parameter map
	 * @param encoding encoding
	 */
	public static String buildParametersString(Map params, String encoding) {
		return buildParametersString(params, false, encoding);
	}
	
	/**
	 * @param link link
	 * @param params parameter map
	 * @param encoding encoding
	 */
	public static void buildParametersString(Appendable link, Map params, String encoding) {
		buildParametersString(link, params, false, encoding);
	}
	
	/**
	 * @param params parameter map
	 * @param escapeAmp escape &
	 * @param encoding encoding
	 */
	public static String buildParametersString(Map params, boolean escapeAmp, String encoding) {
		return buildParametersString(params, escapeAmp ? EAMP : AMP, encoding);
	}

	/**
	 * @param link link
	 * @param params parameter map
	 * @param escapeAmp escape &
	 * @param encoding encoding
	 */
	public static void buildParametersString(Appendable link, Map params, boolean escapeAmp, String encoding) {
		buildParametersString(link, params, escapeAmp ? EAMP : AMP, encoding);
	}

	/**
	 * @param params parameter map
	 * @param encoding encoding
	 * @param paramSeparator parameter separator
	 */
	public static String buildParametersString(Map params, String paramSeparator, String encoding) {
		StringBuilder link = new StringBuilder();
		buildParametersString(link, params, paramSeparator, encoding);
		return link.toString();
	}
	
	/**
	 * @param link link
	 * @param params parameter map
	 * @param paramSeparator parameter separator
	 * @param encoding encoding
	 */
	public static void buildParametersString(Appendable link, Map params, String paramSeparator, String encoding) {
		if (Collections.isEmpty(params)) {
			return;
		}

		try {
			Iterator iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String name = (String) entry.getKey();
				Object value = entry.getValue();

				if (value != null && value.getClass().isArray()) {
					int len = Array.getLength(value);
					for (int i = 0; i < len; i++) {
						Object paramValue = Array.get(value, i);
						buildParameterSubstring(name, paramValue, encoding, link);

						if (i < len - 1) {
							link.append(paramSeparator);
						}
					}
				}
				else if (value instanceof Iterable) {
					for (Iterator iterator = ((Iterable) value).iterator(); iterator.hasNext();) {
						Object paramValue = iterator.next();
						buildParameterSubstring(name, paramValue, encoding, link);

						if (iterator.hasNext()) {
							link.append(paramSeparator);
						}
					}
				}
				else {
					buildParameterSubstring(name, value, encoding, link);
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

	private static void buildParameterSubstring(String name, Object value, String encoding, Appendable writer) throws IOException {
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
	public static Map parseQueryString(String queryString) {
		return parseQueryString(queryString, false, Charsets.UTF_8);
	}

	/**
	 * @param queryString query string
	 * @return parameter map
	 */
	public static Map parseQueryString(String queryString, String encoding) {
		return parseQueryString(queryString, false, encoding);
	}

	/**
	 * @param queryString query string
	 * @param forceValueArray if true each parameter is array
	 * @return parameter map
	 */
	@SuppressWarnings("unchecked")
	public static Map parseQueryString(String queryString, boolean forceValueArray, String encoding) {
		Map queryParams = new LinkedHashMap();
		if (queryString != null) {
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

						if (queryParams.containsKey(paramName) || forceValueArray) {
							Object currentParam = queryParams.get(paramName);
							if (currentParam instanceof String) {
								queryParams.put(paramName, new String[] { (String) currentParam,
										translatedParamValue });
							}
							else {
								String currentParamValues[] = (String[]) currentParam;
								if (currentParamValues != null) {
									List paramList = new ArrayList(Arrays.asList(currentParamValues));
									paramList.add(translatedParamValue);
									String newParamValues[] = new String[paramList.size()];
									queryParams.put(paramName, paramList.toArray(newParamValues));
								}
								else {
									queryParams.put(paramName,
										new String[] { translatedParamValue });
								}
							}
						}
						else {
							queryParams.put(paramName, translatedParamValue);
						}
					}
				}
			}
		}
		return queryParams;
	}
}
