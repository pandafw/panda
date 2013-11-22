package panda.net.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;




/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html
 * @author yf.frank.wang@gmail.com
 */
public class HttpHeader extends HashMap<String, Object> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
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
	public static final String USER_AGENT_PC = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.186 Safari/535.1";
	
	// -------------------------------------------------------------
	public static HttpHeader create() {
		HttpHeader header = new HttpHeader();
		return header;
	}

	// -------------------------------------------------------------
	public HttpHeader() {
	}

	public HttpHeader setDefault() {
		put(USER_AGENT, "Panda.Robot");
		put(ACCEPT_ENCODING, "gzip,deflate");
		put(ACCEPT, "text/xml,application/xml,application/xhtml+xml,text/html;"
				+ "q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		put(ACCEPT_LANGUAGE, "en-US,en,ja,zh,zh-CN");
		put(ACCEPT_CHARSET, "ISO-8859-1,*,utf-8");
		put(CONNECTION, "keep-alive");
		//put(CACHE_CONTROL, "max-age=0");
		return this;
	}

	public HttpHeader setUserAgent(String agent) {
		return set(USER_AGENT, agent);
	}

	public HttpHeader setDate(String key, Date value) {
		return set(key, HttpDates.format(value));
	}
	
	public HttpHeader set(String key, String value) {
		put(key, value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public HttpHeader add(String key, String value) {
		Object object = get(key);
		if (object == null) {
			put(key, value);
		}
		else if (object instanceof List) {
			((List)object).add(value);
		}
		else {
			List vs = new ArrayList<String>();
			vs.add(object);
			vs.add(value);
			put(key, value);
		}
		return this;
	}
	
	public Date getDate(String key) {
		String str = getString(key);
		return HttpDates.safeParse(str);
	}
	
	public String getString(String key) {
		return getString(key, null);
	}
	
	public String getString(String key, String defaultValue) {
		Object value = get(key);
		if (value == null)
			return defaultValue;
		
		if (value instanceof String) {
			return (String)value;
		}
		if (value instanceof List) {
			List vs = (List)value;
			return vs.size() > 0 ? vs.get(0).toString() : defaultValue;
		}

		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public List<String> getStrings(String key) {
		Object value = get(key);
		if (value == null)
			return null;
		
		if (value instanceof List) {
			return (List<String>)value;
		}
		return Arrays.toList(value.toString());
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object put(String key, Object value) {
		if (value == null) {
			return remove(key);
		}
		
		if (value instanceof String) {
			return super.put(key, value);
		}
		
		if (value instanceof String[]) {
			List<String> vs = Arrays.toList(((String[])value));
			return super.put(key, vs);
		}
		
		if (value instanceof Enumeration) {
			List vs = new ArrayList();
			Collections.addAll(vs, (Enumeration)value);
			return super.put(key, vs);
		}

		if (value instanceof List) {
			return super.put(key, value);
		}

		throw new IllegalArgumentException("Invalid http header: key=" + key + ", value=" + value);
	}

	public void toString(Appendable writer) throws IOException {
		for (Map.Entry<String, Object> en : entrySet()) {
			String key = en.getKey();
			Object value = en.getValue();
			if (value instanceof List) {
				for (Object v : ((List)value)) {
					if (Strings.isNotEmpty(key)) {
						writer.append(key).append(": ");
					}
					writer.append(String.valueOf(v)).append(Strings.CRLF);
				}
			}
			else if (value != null) {
				if (Strings.isNotEmpty(key)) {
					writer.append(key).append(": ");
				}
				writer.append(String.valueOf(value)).append(Strings.CRLF);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			toString(sb);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return sb.toString();
	}
}
