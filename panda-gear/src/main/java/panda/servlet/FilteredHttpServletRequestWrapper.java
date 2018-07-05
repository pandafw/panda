package panda.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.net.URLHelper;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;

public class FilteredHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private static final Log log = Logs.getLog(FilteredHttpServletRequestWrapper.class);

	private static final String CONTENT_ENCODING_FILTERED = "filtered";
	
	private String defaultEncoding = Charsets.UTF_8;

	private boolean decompress;
	private InputStream source;
	private BufferedReader reader;
	private ServletInputStream stream;
	private Map<String, String[]> params;

	public FilteredHttpServletRequestWrapper(HttpServletRequest req) {
		super(req);
	}

	
	/**
	 * @return the defaultEncoding
	 */
	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	/**
	 * @param defaultEncoding the defaultEncoding to set
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	//--------------------------------------------------------------------
	protected InputStream getSource() throws IOException {
		if (source == null) {
			HttpServletRequest req = (HttpServletRequest)getRequest();

			source = req.getInputStream();
			if (HttpMethod.POST.equalsIgnoreCase(req.getMethod())) {
				String encoding = req.getHeader(HttpHeader.CONTENT_ENCODING);
				if (encoding != null) {
					if (Strings.containsIgnoreCase(encoding, HttpHeader.CONTENT_ENCODING_GZIP)) {
						source = Streams.gzip(source);
						decompress = true;
					}
					else if (Strings.containsIgnoreCase(encoding, HttpHeader.CONTENT_ENCODING_DEFLATE)) {
						source = Streams.inflater(source);
						decompress = true;
					}
				}
			}
		}
		return source;
	}

	protected Map<String, String[]> getParams() {
		if (params == null) {
			HttpServletRequest req = (HttpServletRequest)getRequest();

			params = new HashMap<String, String[]>();

			String cs = Charsets.defaultEncoding(getCharacterEncoding(), defaultEncoding);
			Map<String, Object> ps = URLHelper.parseQueryString(req.getQueryString(), cs, false);
			addParams(ps);
			
			if (HttpServlets.isFormUrlEncoded(req)) {
				try {
					String qs = Streams.toString(getSource(), cs);
					ps = URLHelper.parseQueryString(qs, cs, false);
					addParams(ps);
				}
				catch (IOException e) {
					log.warn("Failed to decode urlencoded form", e);
				}
			}
		}
		
		return params;
	}

	@SuppressWarnings("unchecked")
	protected void addParams(Map<String, Object> ps) {
		for (Entry<String, Object> en : ps.entrySet()) {
			Object v = en.getValue();
			if (v instanceof String) {
				v = new String[] { (String)v };
			}
			else if (v instanceof List) {
				v = ((List<String>)v).toArray(new String[((List)v).size()]);
			}
			params.put(en.getKey(), (String[])v);
		}
	}

	//------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		if (reader != null) {
			return reader;
		}
		
		String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
		Reader r = new InputStreamReader(getSource(), cs);
		reader = new BufferedReader(r);
		return reader;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (stream != null) {
			return stream;
		}
		
		stream = new DelegateServletInputStream(getSource());
		return stream;
	}

	//------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHeader(String name) {
		if (decompress && HttpHeader.CONTENT_ENCODING.equalsIgnoreCase(name)) {
			return CONTENT_ENCODING_FILTERED;
		}
		return super.getHeader(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<String> getHeaders(String name) {
		if (decompress && HttpHeader.CONTENT_ENCODING.equalsIgnoreCase(name)) {
			return Collections.enumeration(Arrays.asList(CONTENT_ENCODING_FILTERED));
		}
		return super.getHeaders(name);
	}

	//------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParameter(String name) {
		String[] vs = getParams().get(name);
		if (vs == null) {
			return null;
		}
		return vs.length > 0 ? vs[0] : "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParams().keySet());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getParameterValues(String name) {
		return getParams().get(name);
	}

	/**
	 * Need to override getParametersMap because we initially read the whole input stream and
	 * servlet container won't have access to the input stream data.
	 *
	 * @return parsed parameters list. Parameters get parsed only when Content-Type
	 *         "application/x-www-form-urlencoded" is set.
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		return getParams();
	}
}
