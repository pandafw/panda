package panda.mvc.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.MimeTypes;
import panda.io.Settings;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.SetConstants;
import panda.net.http.HttpDates;
import panda.net.http.HttpHeader;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServlets;

/**
 * static resource view
 * <p/>
 * <ul>
 * <li>'@To("file:/abc/cbc")' => /abc/cbc
 * <li>'@To("file:/abc/cbc.txt")' => /abc/cbc.txt
 * </ul>
 */
@IocBean(singleton=false)
public class ResourceView extends AbstractView {
	private static final Log log = Logs.getLog(ResourceView.class);

	public ResourceView() {
	}
	
	public ResourceView(String location) {
		setArgument(location);
	}
	
	public void render(ActionContext ac) {
		String path = argument;

		Object file = ac.getResult();
		if (file != null) {
			if (file instanceof File) {
				sendFile(ac, file);
				return;
			}
			if (file instanceof URL) {
				sendFile(ac, file);
				return;
			}
			if (file instanceof CharSequence) {
				path = file.toString();
			}
			else {
				throw new RuntimeException("Invalid result for " + getClass().getName() + ": " + file);
			}
		}

		file = findResource(ac, path);
		sendFile(ac, file);
	}

	protected File findFile(ActionContext ac, String path) {
		String rpath = ac.getServlet().getRealPath(path);
		File file = new File(rpath);
		return file.exists() ? file : null;
	}
	
	protected Object findResource(ActionContext ac, String path) {
		if (log.isDebugEnabled()) {
			log.debug("Find Resource: " + path);
		}

		File file = findFile(ac, path);
		if (file != null) {
			return file;
		}
		
		return ClassLoaders.getResourceAsURL(path);
	}

	protected void sendFile(ActionContext ac, Object file) {
		if (file == null || (file instanceof File && !((File)file).exists())) {
			if (log.isDebugEnabled()) {
				log.warn("[NOT FOUND]: " + file);
			}
			ac.getResponse().setStatus(HttpStatus.SC_NOT_FOUND);
			return;
		}
		
		forward(ac, file);
	}

	protected void forward(ActionContext ac, Object file) {
		forward(ac.getSettings(), ac.getRequest(), ac.getResponse(), file);
	}
	
	protected void forward(Settings settings, HttpServletRequest request, HttpServletResponse response, Object file) {
		boolean cache = settings.getPropertyAsBoolean(SetConstants.MVC_STATIC_BROWSER_CACHE, true);
		long expires = settings.getPropertyAsInt(SetConstants.MVC_STATIC_EXPIRE_DAYS, 30) * DateTimes.MS_DAY;
		String charset = settings.getProperty(SetConstants.MVC_STATIC_CHARSET, Charsets.UTF_8);
		
		// check for if-modified-since, prior to any other headers
		long ifModifiedSince = 0;
		try {
			ifModifiedSince = request.getDateHeader(HttpHeader.IF_MODIFIED_SINCE);
		}
		catch (Exception e) {
			log.warn("Invalid If-Modified-Since header value: '" + request.getHeader(HttpHeader.IF_MODIFIED_SINCE) + ", ignoring");
		}

		long mod = (file instanceof File) ? ((File)file).lastModified() : System.currentTimeMillis();
		long exp = mod + expires;

		if (ifModifiedSince > 0 && ifModifiedSince <= mod) {
			// not modified, content is not sent - only basic
			// headers and status SC_NOT_MODIFIED
			response.setHeader(HttpHeader.EXPIRES, HttpDates.format(exp));
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}

		// set the content-type header
		String contentType = MimeTypes.getMimeType(file.toString());
		if (Strings.isNotEmpty(charset)) {
			contentType += "; charset=" + charset;
		}
		response.setContentType(contentType);

		if (cache) {
			response.setHeader(HttpHeader.LAST_MODIFIED, HttpDates.format(mod));
			HttpServlets.setResponseCache(response, (int)(expires / 1000), "public");
		}
		else {
			HttpServlets.setResponseNoCache(response);
		}

		InputStream is = null;
		try {
			is = (file instanceof File ? new FileInputStream((File)file) : ((URL)file).openStream());
			Streams.copy(is, response.getOutputStream());
		}
		catch (IOException e) {
			log.warn("Failed to send resource: " + e.getMessage());
		}
		finally {
			Streams.safeClose(is);
		}
	}
}
