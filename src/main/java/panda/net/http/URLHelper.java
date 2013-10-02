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

import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class URLHelper {
	public static final String AMP = "&";
	public static final String EAMP = "&amp;";
	
	/**
	 * get schema from url
	 * @param url url string
	 * @return schema
	 */
	public static String getUrlSchema(String url) {
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
	 * @param url url string
	 * @return domain
	 */
	public static String getUrlDomain(String url) {
		if (Strings.isEmpty(url)) {
			return url;
		}
		
		int i = url.indexOf("://");
		if (i >= 0) {
			url = Strings.stripStart(url.substring(i + 3), "/");
		}

		i = url.indexOf('/');
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
	 * get root from url
	 * @param url url string
	 * @return root
	 */
	public static String getUrlRoot(String url) {
		if (Strings.isEmpty(url)) {
			return url;
		}
		
		int i = url.indexOf("://");
		i = url.indexOf('/', i);
		if (i >= 0) {
			url = url.substring(0, i);
		}

		return url;
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
