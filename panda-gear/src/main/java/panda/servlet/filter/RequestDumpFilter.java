package panda.servlet.filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Files;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpHeader;
import panda.servlet.BufferedHttpServletRequestWrapper;
import panda.servlet.BufferedHttpServletResponseWrapper;
import panda.servlet.HttpServlets;
import panda.servlet.ServletRequestHeaderMap;


/**
 * RequestDumpFilter
 * 
 * <pre>
 * Dump request and response to file
 *
 * &lt;filter&gt;
 *  &lt;filter-name&gt;dump-filter&lt;/filter-name&gt;
 *  &lt;filter-class&gt;panda.servlet.filters.RequestDumpFilter&lt;/filter-class&gt;
 *  &lt;init-param&gt;            
 *    &lt;param-name&gt;dumpRequest&lt;/param-name&gt;            
 *    &lt;param-value&gt;true&lt;/param-value&gt;        
 *  &lt;/init-param&gt;
 *  &lt;init-param&gt;            
 *    &lt;param-name&gt;dumpResponse&lt;/param-name&gt;            
 *    &lt;param-value&gt;true&lt;/param-value&gt;        
 *  &lt;/init-param&gt;
 *  &lt;init-param&gt;            
 *    &lt;param-name&gt;dumpFolder&lt;/param-name&gt;            
 *    &lt;param-value&gt;/dump&lt;/param-value&gt;        
 *  &lt;/init-param&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *  &lt;filter-name&gt;dump-filter&lt;/filter-name&gt;
 *  &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 */
public class RequestDumpFilter implements Filter {
	private final static Log log = Logs.getLog(RequestDumpFilter.class);

	private AtomicInteger serial;
	private File dumpPath;
	private boolean dumpRequest;
	private boolean dumpResponse;
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		String dumpFolder = config.getInitParameter("dumpPath");
		if (Strings.isEmpty(dumpFolder)) {
			log.info("RequestDumpFilter is disabled due to empty dump path");
			return;
		}

		dumpRequest = Boolean.valueOf(config.getInitParameter("dumpRequest"));
		dumpResponse = Boolean.valueOf(config.getInitParameter("dumpResponse"));

		if (!dumpRequest && !dumpResponse) {
			return;
		}
		
		if (dumpFolder.startsWith("web://")) {
			dumpFolder = config.getServletContext().getRealPath(dumpFolder.substring(6));
		}
		dumpPath = new File(dumpFolder);
		
		try {
			Files.makeDirs(dumpPath);
		}
		catch (Throwable e) {
			log.info("RequestDumpFilter failed to create dump folder: " + e);

			dumpRequest = false;
			dumpResponse = false;
			return;
		}

		serial = new AtomicInteger();
		log.info("RequestDumpFilter is enabled to dump " 
				+ ((dumpRequest && dumpResponse) ? "request & response" : (dumpRequest ? "request" : "response")) 
				+ " to " + dumpPath.getAbsolutePath());
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		long time = 0;
		int serial = 0;
		if (dumpRequest || dumpResponse) {
			time = System.currentTimeMillis();
			serial = this.serial.incrementAndGet();
		}

		if (dumpRequest) {
			BufferedHttpServletRequestWrapper reqWrapper = new BufferedHttpServletRequestWrapper(request);
			req = reqWrapper;
			dumpRequest(reqWrapper, time, serial);
		}

		BufferedHttpServletResponseWrapper resWrapper = null;
		if (dumpResponse) {
			resWrapper = new BufferedHttpServletResponseWrapper(response);
			res = resWrapper;
		}

		try {
			chain.doFilter(req, res);
		}
		catch (Throwable e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			if (dumpResponse) {
				dumpResponse(request, response, resWrapper, time, serial);
			}
		}
	}

	private void dumpRequest(BufferedHttpServletRequestWrapper req, long time, int serial) {
		String date = DateTimes.isoDateFormat().format(time);
		String fn = DateTimes.timestampLogFormat().format(time);
		File dumpFolder = new File(dumpPath, date);
		File dumpFile = new File(dumpFolder, fn + "." + serial + "-req.log");
		
		OutputStream fos = null;
		try {
			Files.makeDirs(dumpFolder);

			fos = new FileOutputStream(dumpFile);
			
			StringBuilder sb = new StringBuilder();
			sb.append(req.getMethod()).append(' ');
			sb.append(HttpServlets.getRequestLink(req)).append(' ');
			sb.append(req.getProtocol()).append('\n');
			
			HttpHeader hh = new HttpHeader();
			hh.add("#remote-addr", req.getRemoteAddr());
			hh.add("#remote-user", req.getRemoteUser());
			hh.putAll(new ServletRequestHeaderMap(req));
			hh.write(sb);

			fos.write(sb.toString().getBytes(Charsets.CS_UTF_8));
			fos.write('\n');
			Streams.copy(req.getBodyStream(), fos);
		}
		catch (Throwable e) {
			log.warn("Failed to dump request to " + dumpFile.getPath(), e);
		}
		finally {
			Streams.safeClose(fos);
		}
	}
	
	private void dumpResponse(HttpServletRequest request, HttpServletResponse response,
			BufferedHttpServletResponseWrapper wrapper, long time, int serial) throws IOException {

		// flush wrapper
		wrapper.flushBuffer();
		
		String date = DateTimes.isoDateFormat().format(time);
		String fn = DateTimes.timestampLogFormat().format(time);
		File dumpFolder = new File(dumpPath, date);
		File dumpFile = new File(dumpFolder, fn + "." + serial + "-res.log");
		
		OutputStream fos = null;
		try {
			Files.makeDirs(dumpFolder);

			fos = new FileOutputStream(dumpFile);
			
			StringBuilder sb = new StringBuilder();
			sb.append(request.getProtocol()).append(' ')
				.append(wrapper.getStatusCode()).append(' ')
				.append(wrapper.getStatusMsg()).append('\n');
			
			wrapper.getHead().write(sb);

			fos.write(sb.toString().getBytes(Charsets.CS_UTF_8));
			fos.write('\n');
			Streams.copy(wrapper.getBodyStream(), fos);
		}
		catch (Throwable e) {
			log.warn("Failed to dump response to " + dumpFile.getPath(), e);
		}
		finally {
			Streams.safeClose(fos);
		}
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}

