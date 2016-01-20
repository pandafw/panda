package panda.mvc.util;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import panda.dao.query.Order;
import panda.io.Files;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.mvc.processor.LayoutProcessor;
import panda.net.Inets;
import panda.net.URLHelper;
import panda.net.http.HttpStatus;
import panda.net.http.UserAgent;
import panda.servlet.HttpServlets;
import panda.servlet.filter.RequestLoggingFilter;


@IocBean(scope=Scope.REQUEST)
public class ActionAssist extends ActionSupport {
	private UserAgent userAgent;

	/**
	 * @return log
	 */
	public Log getLog() {
		return Logs.getLog(context.getAction().getClass());
	}
	
	/**
	 * @return true if remote host is local network host
	 */
	public boolean isDebugEnabled() {
		return isIntranetHost();
	}

	
	/**
	 * @return true if remote host is local network host
	 */
	public boolean isIntranetHost() {
		return Inets.isIntranetAddr(HttpServlets.getRemoteAddr(getRequest()));
	}

	public String getServletErrorReason(int sc) {
		return HttpStatus.getStatusReason(sc);
	}
	
	public Throwable getServletException() {
		return HttpServlets.getServletException(getRequest());
	}
	
	public Integer getServletErrorCode() {
		return HttpServlets.getServletErrorCode(getRequest());
	}
	
	public String getServletErrorMessage() {
		return HttpServlets.getServletErrorMessage(getRequest());
	}
	
	/**
	 * @return servlet exception stack trace
	 */
	public String getServletExceptionStackTrace() {
		Throwable ex = getServletException();
		if (ex != null) {
			return Exceptions.getStackTrace(ex);
		}
		return Strings.EMPTY;
	}

	/**
	 * @return contextPath
	 */
	public String getContextPath() {
		return getRequest().getContextPath();
	}

	/**
	 * @return servletPath
	 */
	public String getServletPath() {
		return HttpServlets.getServletPath(getRequest());
	}

	/**
	 * @return requestURI
	 */
	public String getRequestURI() {
		return HttpServlets.getRequestURI(getRequest());
	}
	
	/**
	 * @return requestLink
	 */
	public String getRequestLink() {
		return HttpServlets.getRequestLink(getRequest());
	}
	
	/**
	 * @return requestURL
	 */
	public String getRequestURL() {
		return HttpServlets.getRequestURL(getRequest());
	}
	
	/**
	 * @return requestElapsedTime
	 */
	public String getRequestElapsedTime() {
		HttpServletRequest req = getRequest();
		Long start = (Long)req.getAttribute(RequestLoggingFilter.REQUEST_TIME);
		if (start != null) {
			long end = System.currentTimeMillis();
			long elapse = end - start;
			if (elapse < 1000) {
				return elapse + "ms";
			}
			else {
				return (elapse / 1000) + "s";
			}
		}
		return "";
	}
	
	/**
	 * @return csv file time string
	 */
	public String getCsvFileTime() {
		String pattern = getText("date-format-file", "_(yyyyMMdd-HHmmss)");
		Format format = FastDateFormat.getInstance(pattern);
		return format.format(DateTimes.getDate());
	}

	/**
	 * @return server domain
	 */
	public String getServerDomain() {
		String sn = getRequest().getServerName();
		String[] ss = Strings.split(sn, ".");
		if (ss.length > 2) {
			return ss[ss.length - 2] + "." + ss[ss.length - 1];
		}
		else {
			return sn;
		}
	}

	/**
	 * @return system date
	 */
	public Date getSystemDate() {
		return DateTimes.getDate();
	}

	/**
	 * @return system year
	 */
	public String getSystemYear() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	/**
	 * @return userAgent
	 */
	public UserAgent getUserAgent() {
		if (userAgent == null) {
			HttpServletRequest req = getRequest();
			userAgent = HttpServlets.getUserAgent(req);
		}
		return userAgent;
	}

	/**
	 * @return browser string
	 */
	public String getBrowser() {
		String b = getUserAgent().toSimpleString();
		String l = getLayout();
		if (Strings.isNotEmpty(l)) {
			b += " layout-" + l;
		}
		return b;
	}

	/**
	 * @return layout
	 */
	public String getLayout() {
		return LayoutProcessor.getLayout(getContext());
	}

	/**
	 * @return mobile layout
	 */
	public boolean isMobileLayout() {
		return LayoutProcessor.MOBILE_LAYOUT.equals(getLayout());
	}
	
	/**
	 * @return reverse order
	 */
	public String reverseOrder(String order) {
		Order o = Order.parse(order);
		return Order.reverse(o).toString().toLowerCase();
	}

	/**
	 * @see StringEscapes#escapeHtml(CharSequence)
	 */
	public String escapeHtml(CharSequence str) {
		return StringEscapes.escapeHtml(str);
	}
	
	/**
	 * @see StringEscapes#escapePhtml(CharSequence)
	 */
	public String escapePhtml(CharSequence str) {
		return StringEscapes.escapePhtml(str);
	}
	
	/**
	 * @see StringEscapes#escapeJava(CharSequence)
	 */
	public String escapeJava(CharSequence str) {
		return StringEscapes.escapeJava(str);
	}
	
	/**
	 * @see StringEscapes#escapeJavaScript(CharSequence)
	 */
	public String escapeJavaScript(CharSequence str) {
		return StringEscapes.escapeJavaScript(str);
	}
	
	/**
	 * @see StringEscapes#escapeXml(CharSequence)
	 */
	public String escapeXml(CharSequence str) {
		return StringEscapes.escapeXml(str);
	}
	
	/**
	 * @see StringEscapes#escapeCsv(CharSequence)
	 */
	public String escapeCsv(CharSequence str) {
		return StringEscapes.escapeCsv(str);
	}
	
	/**
	 * @see Files#toDisplaySize(Long)
	 */
	public String formatFileSize(long size) {
		return Files.toDisplaySize(size);
	}
	
	/**
	 * @see Texts#ellipsis(String, int)
	 */
	public String ellipsis(String str, int len) {
		return Texts.ellipsis(str, len);
	}
	
	/**
	 * @see Texts#ellipsiz(String, int)
	 */
	public String ellipsiz(String str, int len) {
		return Texts.ellipsiz(str, len);
	}
	
	/**
	 * @param name cookie name
	 * @return cookie value
	 */
	public String getCookie(String name) {
		Cookie c = HttpServlets.getCookie(getRequest(), name);
		return c != null ? c.getValue() : null;
	}

	/**
	 * @param str string
	 * @return array
	 */
	public String[] split(String str) {
		return Strings.split(str);
	}

	/**
	 * @return random id
	 */
	public String getRandomId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @param url the url to encode
	 * @return encoded url
	 */
	public String encodeURL(String url) {
		return URLHelper.encodeURL(url);
	}

	/**
	 * @param url the url to decode
	 * @return decoded url
	 */
	public String decodeURL(String url) {
		return URLHelper.decodeURL(url);
	}

	/**
	 * find value in context
	 */
	public Object findValue(String expr) {
		return Mvcs.findValue(expr, getContext());
	}
	
	/**
	 * find value in context with argument
	 */
	public Object findValue(String expr, Object arg) {
		return Mvcs.findValue(expr, getContext(), arg);
	}
}
