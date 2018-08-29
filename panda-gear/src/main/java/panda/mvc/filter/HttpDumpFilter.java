package panda.mvc.filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Files;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;
import panda.mvc.util.MvcSettings;
import panda.net.http.HttpHeader;
import panda.servlet.HttpServletRequestCapturer;
import panda.servlet.HttpServletResponseCapturer;
import panda.servlet.HttpServlets;
import panda.servlet.ServletRequestHeaderMap;

@IocBean
public class HttpDumpFilter implements ServletFilter {
	private final static Log log = Logs.getLog(HttpDumpFilter.class);

	private AtomicInteger serial;

	@IocInject
	private MvcSettings settings;
	
	public HttpDumpFilter() {
		serial = new AtomicInteger();
	}

	@Override
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
		String path = settings.getProperty("http.dump.path");
		boolean dreq = settings.getPropertyAsBoolean("http.dump.request");
		boolean dres = settings.getPropertyAsBoolean("http.dump.response");

		if (Strings.isEmpty(path) || (!dreq && !dres)) {
			return sc.doNext(req, res);
		}
		
		try {
			Files.makeDirs(path);
		}
		catch (Throwable e) {
			log.warn("Failed to create dump folder: " + e);
			return sc.doNext(req, res);
		}

		long time = System.currentTimeMillis();
		int serial = this.serial.incrementAndGet();

		if (dreq) {
			req = new HttpServletRequestCapturer(req);
			dumpRequest((HttpServletRequestCapturer)req, path, time, serial);
		}

		if (dres) {
			res = new HttpServletResponseCapturer(res);
		}

		try {
			return sc.doNext(req, res);
		}
		catch (Throwable e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			if (dres) {
				dumpResponse(req, (HttpServletResponseCapturer)res, path, time, serial);
			}
		}
	}

	private void dumpRequest(HttpServletRequestCapturer req, String path, long time, int serial) {
		File dir = new File(path, DateTimes.isoDateFormat().format(time));
		File file = new File(dir, DateTimes.timestampLogFormat().format(time) + "." + serial + "-req.log");
		
		OutputStream fos = null;
		try {
			Files.makeDirs(dir);

			fos = new FileOutputStream(file);
			
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
			
			if (log.isDebugEnabled()) {
				log.debug("Request dumpped to " + file);
			}
		}
		catch (Throwable e) {
			log.warn("Failed to dump request to " + file, e);
		}
		finally {
			Streams.safeClose(fos);
		}
	}
	
	private void dumpResponse(HttpServletRequest req, HttpServletResponseCapturer cres, String path, long time, int serial) {
		File dir = new File(path, DateTimes.isoDateFormat().format(time));
		File file = new File(dir, DateTimes.timestampLogFormat().format(time) + "." + serial + "-res.log");
		
		OutputStream fos = null;
		try {
			// flush wrapper
			cres.flushBuffer();
			
			Files.makeDirs(dir);

			fos = new FileOutputStream(file);
			
			StringBuilder sb = new StringBuilder();
			sb.append(req.getProtocol()).append(' ')
				.append(cres.getStatusCode()).append(' ')
				.append(cres.getStatusMsg()).append('\n');
			
			cres.getHead().write(sb);

			fos.write(sb.toString().getBytes(Charsets.CS_UTF_8));
			fos.write('\n');
			Streams.copy(cres.getBodyStream(), fos);

			if (log.isDebugEnabled()) {
				log.debug("Response dumpped to " + file);
			}
		}
		catch (Throwable e) {
			log.warn("Failed to dump response to " + file, e);
		}
		finally {
			Streams.safeClose(fos);
		}
	}
}
