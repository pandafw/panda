package panda.net.http;

import java.io.Serializable;
import java.util.Date;

import panda.net.InternetHeader;

/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html
 */
public class HttpHeader extends InternetHeader implements Cloneable, Serializable {
	private static final long serialVersionUID = 5L;
	
	public static final String ACCEPT                = "Accept";
	public static final String ACCEPT_CHARSET        = "Accept-Charset";
	public static final String ACCEPT_ENCODING       = "Accept-Encoding";
	public static final String ACCEPT_LANGUAGE       = "Accept-Language";
	public static final String ACCEPT_RANGES         = "Accept-Ranges";
	public static final String AGE                   = "Age";
	public static final String ALLOW                 = "Allow";
	public static final String AUTHORIZATION         = "Authorization";
	public static final String CACHE_CONTROL         = "Cache-Control";
	public static final String CACHE_CONTROL_NOCACHE = "no-cache";
	public static final String CACHE_CONTROL_PUBLIC  = "public";
	public static final String CACHE_CONTROL_PRIVATE = "private";
	public static final String CONNECTION            = "Connection";
	public static final String CONTENT_DISPOSITION   = "Content-Disposition";
	public static final String CONTENT_ENCODING      = "Content-Encoding";
	public static final String CONTENT_LANGUAGE      = "Content-Language";
	public static final String CONTENT_LENGTH        = "Content-Length";
	public static final String CONTENT_LOCATION      = "Content-Location";
	public static final String CONTENT_MD5           = "Content-MD5";
	public static final String CONTENT_RANGE         = "Content-Range";
	public static final String CONTENT_TYPE          = "Content-Type";
	public static final String COOKIE                = "Cookie";
	public static final String DATE                  = "Date";
	public static final String ETAG                  = "ETag";
	public static final String EXPECT                = "Expect";
	public static final String EXPIRES               = "Expires";
	public static final String FROM                  = "From";
	public static final String HOST                  = "Host";
	public static final String IF_MATCH              = "If-Match";
	public static final String IF_MODIFIED_SINCE     = "If-Modified-Since";
	public static final String IF_NONE_MATCH         = "If-None-Match";
	public static final String IF_RANGE              = "If-Range";
	public static final String IF_UNMODIFIED_SINCE   = "If-Unmodified-Since";
	public static final String LAST_MODIFIED         = "Last-Modified";
	public static final String LOCATION              = "Location";
	public static final String MAX_FORWARDS          = "Max-Forwards";
	public static final String PRAGMA                = "Pragma";
	public static final String PROXY_AUTHENTICATE    = "Proxy-Authenticate";
	public static final String PROXY_AUTHORIZATION   = "Proxy-Authorization";
	public static final String RANGE                 = "Range";
	public static final String REFERER               = "Referer";
	public static final String RETRY_AFTER           = "Retry-After";
	public static final String SERVER                = "Server";
	public static final String TE                    = "TE";
	public static final String TRAILER               = "Trailer";
	public static final String TRANSFER_ENCODING     = "Transfer-Encoding";
	public static final String UPGRADE               = "Upgrade";
	public static final String USER_AGENT            = "User-Agent";
	public static final String VARY                  = "Vary";
	public static final String VIA                   = "Via";
	public static final String WARNING               = "Warning";
	public static final String WWW_AUTHENTICATE      = "WWW-Authenticate";

	public static final String SET_COOKIE = "Set-Cookie";

	// -------------------------------------------------------------
	public static final String CONTENT_ENCODING_GZIP            = "gzip";
	public static final String CONTENT_ENCODING_DEFLATE         = "deflate";
	public static final String CONTENT_DISPOSITION_ATTACHMENT   = "attachment";
	public static final String CONTENT_DISPOSITION_INLINE       = "inline";

	// -------------------------------------------------------------
	public static final String X_REAL_IP = "X-Real-IP";
	public static final String X_FORWARDED_PORT = "X-Forwarded-Port";
	public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
	
	public static final String X_ACCEL_BUFFERING = "X-Accel-Buffering";
	
	// -------------------------------------------------------------
	public static HttpHeader create() {
		HttpHeader header = new HttpHeader();
		return header;
	}

	// -------------------------------------------------------------
	public HttpHeader() {
	}

	public String getUserAgent() {
		return getString(USER_AGENT);
	}
	
	public HttpHeader setUserAgent(String agent) {
		return (HttpHeader)set(USER_AGENT, agent);
	}

	public String getAccept() {
		return getString(ACCEPT);
	}

	public HttpHeader setAccept(String accept) {
		return (HttpHeader)set(ACCEPT, accept);
	}

	public String getAcceptEncoding() {
		return getString(ACCEPT_ENCODING);
	}

	public HttpHeader setAcceptEncoding(String acceptEncoding) {
		return (HttpHeader)set(ACCEPT_ENCODING, acceptEncoding);
	}

	public String getAcceptLanguage() {
		return getString(ACCEPT_LANGUAGE);
	}

	public HttpHeader setAcceptLanguage(String acceptLanguage) {
		return (HttpHeader)set(ACCEPT_LANGUAGE, acceptLanguage);
	}

	public String getAcceptCharset() {
		return getString(ACCEPT_CHARSET);
	}

	public HttpHeader setAcceptCharset(String acceptCharset) {
		return (HttpHeader)set(ACCEPT_CHARSET, acceptCharset);
	}

	public String getContentEncoding() {
		return getString(CONTENT_ENCODING);
	}
	
	public HttpHeader setContentEncoding(String encoding) {
		return (HttpHeader)set(CONTENT_ENCODING, encoding);
	}

	public String getContentType() {
		return getString(CONTENT_TYPE);
	}
	
	public HttpHeader setContentType(String type) {
		return (HttpHeader)set(CONTENT_TYPE, type);
	}
	
	public String getAuthentication() {
		return getString(AUTHORIZATION);
	}
	
	public HttpHeader setAuthentication(String auth) {
		return (HttpHeader)set(AUTHORIZATION, auth);
	}
	
	public boolean isKeepAlive() {
		return "keep-alive".equalsIgnoreCase(getString(CONNECTION));
	}
	
	public HttpHeader setKeepAlive(boolean keep) {
		return (HttpHeader)set(CONNECTION, keep ? "keep-alive" : "close");
	}
	
	//-------------------------------------------------
	@Override
	protected Date parseDate(String value) {
		return HttpDates.safeParse(value);
	}
	
	@Override
	protected String formatDate(Date value) {
		return HttpDates.format(value);
	}
	
	@Override
	public HttpHeader clone() {
		HttpHeader hh = new HttpHeader();
		hh.putAll(this);
		return hh;
	}
}
