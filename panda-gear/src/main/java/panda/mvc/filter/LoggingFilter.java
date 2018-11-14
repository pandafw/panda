package panda.mvc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;
import panda.mvc.SetConstants;
import panda.mvc.util.MvcSettings;
import panda.servlet.FilteredHttpServletResponseWrapper;
import panda.servlet.HttpServlets;

/**
 * 
 * Access Log Format:
 * <ul>
 *     <li><b>%t</b> - Date and time, in Common Log Format</li>
 *     <li><b>%a</b> - Remote IP address</li>
 *     <li><b>%h</b> - Remote host name</li>
 *     <li><b>%p</b> - Request protocol</li>
 *     <li><b>%m</b> - Request method (GET, POST, etc.)</li>
 *     <li><b>%q</b> - Query string (prepended with a '?' if it exists)</li>
 *     <li><b>%s</b> - User session ID</li>
 *     <li><b>%u</b> - Remote user that was authenticated (if any), else '-'</li>
 *     <li><b>%A</b> - Local IP address</li>
 *     <li><b>%V</b> - Local server name</li>
 *     <li><b>%P</b> - Local port on which this request was received</li>
 *     <li><b>%S</b> - HTTP status code of the response</li>
 *     <li><b>%T</b> - Time taken to process the request, in milliseconds</li>
 *     <li><b>%I</b> - current request thread name (can compare later with stacktraces)</li>
 *     <li><b>%u</b> - Requested URL path</li>
 * </ul> 
 *
 */
@IocBean
public class LoggingFilter implements ServletFilter {
	private static final String DEFAULT_FORMAT = "%t %a %h %p %m %s %A %V %P %S %T %I %u";

	private static Log log = Logs.getLog(LoggingFilter.class);

	@IocInject
	private MvcSettings settings;

	public LoggingFilter() {
	}
	
	@Override
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
		StopWatch sw = new StopWatch();

		logRequest(req);

		try {
			return sc.doNext(req, res);
		}
		catch (Throwable e) {
			try {
				// log exception
				Log elog = Logs.getLog(e.getClass());
				if (HttpServlets.isClientAbortError(e)) {
					if (elog.isWarnEnabled()) {
						String s = HttpServlets.dumpException(req, e, false);
						elog.warn(s);
					}
					return true;
				}

				if (elog.isErrorEnabled()) {
					String s = HttpServlets.dumpException(req, e, true);
					elog.error(s, e);
				}

				HttpServlets.sendError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			catch (Throwable e2) {
				if (log.isWarnEnabled()) {
					log.warn(e.getMessage(), e);
				}
			}
			return true;
		}
		finally {
			logAccess(req, res, sw);
		}
	}

	private void logRequest(HttpServletRequest request) {
		try {
			if (log.isTraceEnabled()) {
				log.trace(HttpServlets.dumpRequestTrace(request));
				return;
			}
			if (log.isDebugEnabled()) {
				log.debug(HttpServlets.dumpRequestDebug(request));
				return;
			}
			if (log.isInfoEnabled()) {
				log.info(HttpServlets.dumpRequestInfo(request));
			}
		}
		catch (Throwable e) {
			//pass
		}
	}

	private void logAccess(HttpServletRequest request, HttpServletResponse response, StopWatch sw) {
		try {
			String name = settings.getProperty(SetConstants.MVC_ACCESS_LOG_NAME);
			if (Strings.isEmpty(name)) {
				return;
			}
			
			Log logger = Logs.getLog(name);
			if (!logger.isInfoEnabled()) {
				return;
			}

			sw.stop();

			String[] format = parseFormat(settings.getProperty(SetConstants.MVC_ACCESS_LOG_FORMAT, DEFAULT_FORMAT));

			StringBuilder msg = new StringBuilder();
			for (String s : format) {
				char f = s.charAt(0);
				switch (f) {
				case 't': // Date and time, in Common Log Format
					append(msg, DateTimes.timestampFormat().format(sw.getStartTime()));
					break;
				case 'a': // Remote IP address
					append(msg, request.getRemoteAddr());
					break;
				case 'h': // Remote host name
					append(msg, request.getRemoteHost());
					break;
				case 'p': // Request protocol
					append(msg, request.getProtocol());
					break;
				case 'm': // Request method (GET, POST, etc.)
					append(msg, request.getMethod());
					break;
				case 'q': // Query string (prepended with a '?' if it exists)
					append(msg, request.getQueryString());
					break;
				case 's': // Requested session ID
					append(msg, request.getRequestedSessionId());
					break;
				case 'A': // Local IP address
					append(msg, request.getLocalAddr());
					break;
				case 'V': // Local server name
					append(msg, request.getLocalName());
					break;
				case 'P': // Local port on which this request was received
					msg.append(request.getLocalPort());
					break;
				case 'S': // HTTP status code of the response
					if (response instanceof FilteredHttpServletResponseWrapper) {
						msg.append(((FilteredHttpServletResponseWrapper)response).getStatus());
					}
					else {
						msg.append('-');
					}
					break;
				case 'T': // Time taken to process the request, in milliseconds
					msg.append(sw.getTime());
					break;
				case 'I': // current request thread name (can compare later with stacktraces)
					append(msg, Thread.currentThread().getName());
					break;
				case 'u': // Requested URL path
					msg.append(request.getRequestURL());
					break;
				default:
					msg.append('-');
					break;
				}
				msg.append('\t');
			}
			
			logger.info(msg.toString());
		}
		catch (Throwable e) {
			//pass
		}
	}

	protected String[] parseFormat(String format) {
		return Strings.split(Strings.remove(format, '%'));
	}

	private void append(StringBuilder msg, String value) {
		append(msg, value, "");
	}
	
	private void append(StringBuilder msg, String value, String defv) {
		msg.append(Strings.isEmpty(value) ? defv : value);
	}
}
