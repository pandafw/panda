package panda.net.http;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Iterators;
import panda.lang.Numbers;




/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html
 * @author yf.frank.wang@gmail.com
 */
public class HttpHeader implements Map<String, Object>, Cloneable, Serializable {
	private static final long serialVersionUID = 3L;
	
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
	public static final String X_REAL_IP = "X-Real-IP";
	
	// -------------------------------------------------------------
	public static final String USER_AGENT_PC = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.186 Safari/535.1";

	// -------------------------------------------------------------
	public static HttpHeader create() {
		HttpHeader header = new HttpHeader();
		return header;
	}

	// -------------------------------------------------------------
	private Map<String, Object> map = new TreeMap<String, Object>();

	public HttpHeader() {
	}

	public HttpHeader setDefault() {
		put(USER_AGENT, HttpClient.DEFAULT_USERAGENT);
		put(ACCEPT_ENCODING, "gzip,deflate");
		put(ACCEPT, "text/xml,application/xml,application/xhtml+xml,text/html;"
				+ "q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		put(ACCEPT_LANGUAGE, "en-US,en,ja,zh,zh-CN,zh-TW");
		put(ACCEPT_CHARSET, "ISO-8859-1,*,utf-8");
		put(CONNECTION, "keep-alive");
		return this;
	}

	public HttpHeader setUserAgent(String agent) {
		return set(USER_AGENT, agent);
	}

	//-------------------------------------------------
	public int getInt(String key) {
		String str = getString(key);
		return Numbers.toInt(str, -1);
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
		if (value == null) {
			return null;
		}
		
		if (value instanceof List) {
			return (List<String>)value;
		}
		return Arrays.toList(value.toString());
	}

	//-------------------------------------------------
	public HttpHeader setInt(String key, int value) {
		return set(key, value);
	}

	public HttpHeader setDate(String key, long value) {
		return set(key, new Date(value));
	}

	public HttpHeader setDate(String key, Date value) {
		return set(key, value);
	}
	
	public HttpHeader setString(String key, String value) {
		return set(key, value);
	}
	
	public HttpHeader set(String key, Object value) {
		put(key, value);
		return this;
	}

	public HttpHeader setAll(Map<? extends String, ? extends Object> m) {
		putAll(m);
		return this;
	}

	//-------------------------------------------------
	public HttpHeader addInt(String key, int value) {
		return add(key, value);
	}

	public HttpHeader addDate(String key, long value) {
		return add(key, new Date(value));
	}

	public HttpHeader addDate(String key, Date value) {
		return add(key, value);
	}
	
	public HttpHeader addString(String key, String value) {
		add(key, (Object)value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public HttpHeader add(String key, Object value) {
		if (key == null || value == null) {
			return this;
		}

		value = convertValue(value);

		key = (String)toCompareKey(key);
		Object object = map.get(key);
		if (object == null) {
			map.put(key, value);
			return this;
		}
		
		List vs = null;
		if (object instanceof List) {
			vs = (List)object;
		}
		else {
			vs = new ArrayList<String>();
			vs.add(object);
			map.put(key, vs);
		}
		
		if (value instanceof List) {
			vs.addAll((List)value);
		}
		else {
			vs.add(value);
		}

		return this;
	}
	
	public HttpHeader addAll(Map<? extends String, ? extends Object> m) {
		for (Entry<? extends String, ? extends Object> en : m.entrySet()) {
			add(en.getKey(), en.getValue());
		}
		return this;
	}

	//-------------------------------------------------
	private Object toCompareKey(Object key) {
		return key instanceof String ? ((String)key).toLowerCase() : key; 
	}

	@SuppressWarnings("unchecked")
	private Object convertValue(Object value) {
		if (value == null) {
			return null;
		}
		
		if (value instanceof Date) {
			return HttpDates.format((Date)value);
		}
		if (value instanceof Calendar) {
			return HttpDates.format(((Calendar)value).getTime());
		}

		if (Iterators.isIterable(value)) {
			List vs = new ArrayList();
			for (Object v : Iterators.asIterable(value)) {
				v = convertValue(v);
				if (v instanceof List) {
					for (Object c : (List)v) {
						vs.add(c);
					}
				}
				else {
					vs.add(v);
				}
			}
			return vs;
		}

		return value.toString();
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(toCompareKey(key));
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public Object get(Object key) {
		return map.get(toCompareKey(key));
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Object put(String key, Object value) {
		if (key == null) {
			return null;
		}
		
		key = (String)toCompareKey(key);
		if (value == null) {
			return map.remove(key);
		}

		value = convertValue(value);
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Entry<? extends String, ? extends Object> en : m.entrySet()) {
			put(en.getKey(), en.getValue());
		}
	}

	public Object remove(Object key) {
		return map.remove(toCompareKey(key));
	}

	public int size() {
		return map.size();
	}

	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public boolean equals(Object o) {
		return map.equals(o);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public Object clone() {
		HttpHeader hh = new HttpHeader();
		hh.map.putAll(map);
		return hh;
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

	public void toString(Appendable writer) throws IOException {
		for (Map.Entry<String, Object> en : entrySet()) {
			String key = en.getKey();
			writer.append(key).append(':').append(' ');

			Iterator it = Iterators.asIterator(en.getValue());
			while (it.hasNext()) {
				writer.append(it.next().toString());
				if (it.hasNext()) {
					writer.append(',');
				}
			}
			writer.append(Streams.LINE_SEPARATOR);
		}
	}
}
