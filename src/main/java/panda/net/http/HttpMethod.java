package panda.net.http;

import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public enum HttpMethod {
	GET, POST, OPTIONS, PUT, DELETE, TRACE, CONNECT;
	
	public static HttpMethod parse(String method) {
		return valueOf(Strings.upperCase(method));
	}
}
