package panda.mvc.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import panda.Panda;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.net.http.HttpDates;
import panda.net.http.HttpHeader;
import panda.servlet.HttpServlets;

public class StaticAction extends ActionSupport {
	private static final Log log = Logs.getLog(StaticAction.class);

	protected static final String BASE = "panda/html/";
	protected static final String VERSION = Panda.VERSION + '/';
	
	@IocInject(value = MvcConstants.STATIC_BROWSER_CACHE, required = false)
	protected boolean cache = true;
	
	protected String version = VERSION;

	protected long expires = DateTimes.MS_MONTH;

	protected String charset = Charsets.UTF_8;
	
	/**
	 * @param version the version to set
	 */
	@IocInject(value = MvcConstants.STATIC_VERSION, required = false)
	public void setVersion(String version) {
		this.version = Strings.stripToNull(version);
		if (!Strings.endsWithChar(this.version, '/')) {
			this.version += '/';
		}
	}

	/**
	 * @param expires the expires to set
	 */
	@IocInject(value = MvcConstants.STATIC_EXPIRE_DAYS, required = false)
	public void setExpires(long expires) {
		this.expires = expires * DateTimes.MS_DAY;
	}

	@At("${!!static_path|||'/static'}/(.*)$")
	@To(View.NONE)
	public void execute(String path) {
		URL url = findResource(path);
		if (url == null) {
			if (log.isDebugEnabled()) {
				log.warn("[NOT FOUND]: " + path);
			}
			getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// check for if-modified-since, prior to any other headers
		long ifModifiedSince = 0;
		try {
			ifModifiedSince = getRequest().getDateHeader(HttpHeader.IF_MODIFIED_SINCE);
		}
		catch (Exception e) {
			log.warn("Invalid If-Modified-Since header value: '" + getRequest().getHeader(HttpHeader.IF_MODIFIED_SINCE) + ", ignoring");
		}

		long now = System.currentTimeMillis();
		long exp = now + expires;

		if (ifModifiedSince > 0 && ifModifiedSince <= now) {
			// not modified, content is not sent - only basic
			// headers and status SC_NOT_MODIFIED
			getResponse().setHeader(HttpHeader.EXPIRES, HttpDates.format(exp));
			getResponse().setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}

		// set the content-type header
		String contentType = MimeTypes.getMimeType(path);
		if (Strings.isNotEmpty(charset)) {
			contentType += "; charset=" + charset;
		}
		getResponse().setContentType(contentType);

		if (cache) {
			getResponse().setHeader(HttpHeader.LAST_MODIFIED, HttpDates.format(now));
			HttpServlets.setResponseCache(getResponse(), (int)(expires / 1000), "public");
		}
		else {
			HttpServlets.setResponseNoCache(getResponse());
		}

		InputStream is = null;
		try {
			is = url.openStream();
			Streams.copy(is, getResponse().getOutputStream());
		}
		catch (IOException e) {
			log.warn("Failed to send resource: " + e.getMessage());
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	protected URL findFile(String path) {
		try {
			String rpath = getServlet().getRealPath(path);
			File file = new File(rpath);
			if (file.exists()) {
				return file.toURI().toURL();
			}
		}
		catch (MalformedURLException e) {
		}
		return null;
	}
	
	protected URL findResource(String name) {
		String path = BASE + name;
		
		if (log.isDebugEnabled()) {
			log.debug("Find File: " + path);
		}
		URL url = findFile(path);
		if (url != null) {
			return url;
		}
		
		String npath = null;
		if (Strings.isNotEmpty(version) && Strings.startsWith(name, version)) {
			npath = BASE + Strings.substring(name, version.length());
		}

		if (npath != null) {
			if (log.isDebugEnabled()) {
				log.debug("Find File: " + npath);
			}
			url = findFile(npath);
			if (url != null) {
				return url;
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Find Resource: " + path);
		}
		url = ClassLoaders.getResourceAsURL(path);
		if (url != null) {
			return url;
		}
		
		if (npath != null) {
			if (log.isDebugEnabled()) {
				log.debug("Find Resource: " + npath);
			}
			url = ClassLoaders.getResourceAsURL(npath);
			if (url != null) {
				return url;
			}
		}
		
		return null;
	}
}
