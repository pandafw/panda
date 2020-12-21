package panda.net;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.codec.net.URLCodec;
import panda.io.FileNames;
import panda.lang.Charsets;
import panda.lang.Strings;

/**
 */
public class URLHelper {
	public static final char SEPARATOR = '/';
	public static final String AMP = "&";
	public static final String EAMP = "&amp;";
	
	public static final int SUPPRESS_NONE = 0;
	public static final int SUPPRESS_NULL = 1;
	public static final int SUPPRESS_EMPTY = 2;
	
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
	 * resolve url to base
	 * <pre>
	 *   null                    + *             = *
	 *   ""                      + *             = *
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
	 *   /d/e                    + x             = /d/x
	 *   /d/e                    + ../x          = /x
	 * </pre>
	 * @param base base url
	 * @param add append url
	 * @return url url
	 */
	public static String resolveURL(String base, String add) {
		if (Strings.isEmpty(add)) {
			return base;
		}
		if (Strings.isEmpty(base)) {
			return add;
		}
		
		if (getURLRootLength(add) >= 0) {
			return add;
		}

		int prefix = getURLRootLength(base);
		if (prefix < 0) {
			prefix = 0;
		}

		String root = base.substring(0, prefix);
		String path = getURLParent(base.substring(prefix));
		String uri;
		
		if (add.charAt(0) == SEPARATOR) {
			uri = normalize(add);
		}
		else {
			uri = normalize(path + SEPARATOR + add);
		}
		
		if (uri == null) {
			return uri;
		}

		return root + uri;
	}
	
	public static String appendPath(String base, String add) {
		return normalize(base + SEPARATOR + add);
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
		if (Strings.isEmpty(url) || Strings.isEmpty(encoding)) {
			return url;
		}
		
		return URLCodec.encodeUrl(url, encoding);
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
		
		return URLCodec.safeDecodeUrl(input, encoding);
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
	 * @param encoding the encoding
	 * @return parameter map
	 */
	public static Map<String, Object> parseQueryString(String queryString, String encoding) {
		return parseQueryString(queryString, encoding, false);
	}
	
	/**
	 * @param queryString query string
	 * @param removeEmpty remove empty parameter
	 * @return parameter map
	 */
	public static Map<String, Object> parseQueryString(String queryString, boolean removeEmpty) {
		return parseQueryString(queryString, Charsets.UTF_8, removeEmpty);
	}

	/**
	 * @param queryString query string
	 * @param encoding the encoding
	 * @param removeEmpty remove empty parameter
	 * @return parameter map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseQueryString(String queryString, String encoding, boolean removeEmpty) {
		Map<String, Object> qparams = new LinkedHashMap<String, Object>();
		if (Strings.isEmpty(queryString)) {
			return qparams;
		}
		
		String[] params = Strings.split(queryString, '&');
		for (String a : params) {
			a = Strings.trim(a);
			if (Strings.isEmpty(a)) {
				continue;
			}

			String k, v;
			int d = a.indexOf('=');
			if (d < 0) {
				k = a;
				v = null;
			}
			else {
				k = Strings.trim(a.substring(0, d));
				v = Strings.trim(a.substring(d + 1));
			}

			if (removeEmpty && Strings.isEmpty(v)) {
				continue;
			}

			k = decodeURL(k, encoding);
			v = decodeURL(v, encoding);
			if (qparams.containsKey(k)) {
				Object cv = qparams.get(k);
				if (cv instanceof List) {
					List<String> ss = (List<String>)cv;
					ss.add(v);
				}
				else {
					List<String> ss = new ArrayList<String>();
					ss.add((String)cv);
					ss.add(v);
					qparams.put(k, ss);
				}
			}
			else {
				qparams.put(k, v);
			}
		}
		return qparams;
	}
}
