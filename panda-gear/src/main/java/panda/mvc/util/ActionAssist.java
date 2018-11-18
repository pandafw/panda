package panda.mvc.util;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import panda.bind.json.Jsons;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.Objects;
import panda.lang.Order;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.mvc.processor.LayoutProcessor;
import panda.net.URLHelper;
import panda.net.http.HttpStatus;
import panda.net.http.UserAgent;
import panda.servlet.HttpServlets;


@IocBean(scope=Scope.REQUEST)
public class ActionAssist extends ActionSupport {
	/**
	 * @return log
	 */
	public Log getLog() {
		return Logs.getLog(context.getAction().getClass());
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
	 * @return remoteAddr
	 */
	public String getRemoteAddr() {
		return HttpServlets.getRemoteAddr(getRequest());
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
		Long start = (Long)req.getAttribute(Mvcs.REQUEST_TIME);
		if (start != null) {
			long end = System.currentTimeMillis();
			long elapse = end - start;
			if (elapse < 1000) {
				return elapse + "ms";
			}
			return (elapse / 1000) + "s";
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
		return sn;
	}

	/**
	 * @return system year
	 */
	public int getSystemYear() {
		return DateTimes.getYear();
	}

	/**
	 * @return system date
	 */
	public Date getSystemDate() {
		return DateTimes.getDate();
	}

	/**
	 * @return system calendar
	 */
	public Calendar getSystemCalendar() {
		return DateTimes.getCalendar();
	}

	/**
	 * @return userAgent
	 */
	public UserAgent getUserAgent() {
		return context.getUserAgent();
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
	 * @param order the order to reverse
	 * @return reverse order
	 */
	public String reverseOrder(String order) {
		Order o = Order.parse(order);
		return Order.reverse(o).toString().toLowerCase();
	}

	/**
	 * @param o the object to escape
	 * @return escaped html string
	 * @see StringEscapes#escapeHtml(CharSequence)
	 */
	public String escapeHtml(Object o) {
		return StringEscapes.escapeHtml(Objects.toString(o));
	}

	/**
	 * @param o the object to escape
	 * @return escaped pre formatted html string
	 * @see StringEscapes#escapePhtml(CharSequence)
	 */
	public String escapePhtml(Object o) {
		return StringEscapes.escapePhtml(Objects.toString(o));
	}

	/**
	 * @param o the object to escape
	 * @return escaped java string
	 * @see StringEscapes#escapeJava(CharSequence)
	 */
	public String escapeJava(Object o) {
		return StringEscapes.escapeJava(Objects.toString(o));
	}

	/**
	 * @param o the object to escape
	 * @return escaped javascript string
	 * @see StringEscapes#escapeJavaScript(CharSequence)
	 */
	public String escapeJavaScript(Object o) {
		return StringEscapes.escapeJavaScript(Objects.toString(o));
	}

	/**
	 * @param o the object to escape
	 * @return escaped xml string
	 * @see StringEscapes#escapeXml(CharSequence)
	 */
	public String escapeXml(Object o) {
		return StringEscapes.escapeXml(Objects.toString(o));
	}
	
	/**
	 * @param o the object to escape
	 * @return escaped csv string
	 * @see StringEscapes#escapeCsv(CharSequence)
	 */
	public String escapeCsv(Object o) {
		return StringEscapes.escapeCsv(Objects.toString(o));
	}
	
	/**
	 * @param size size
	 * @return formatted string
	 */
	public String formatSize(Number size) {
		return Numbers.formatSize(size);
	}
	
	/**
	 * Truncate a string and add an ellipsis ('...') to the end if it exceeds the specified length
	 * 
	 * @param str value The string to truncate
	 * @param len length The maximum length to allow before truncating
	 * @return The converted text
	 * @see Texts#ellipsis(String, int)
	 */
	public String ellipsis(String str, int len) {
		return Texts.ellipsis(str, len);
	}
	
	/**
	 * Truncate a string and add an ellipsis ('...') to the end if it exceeds the specified length
	 * the length of charCodeAt(i) > 0xFF will be treated as 2.
	 * 
	 * @param str value The string to truncate
	 * @param len length The maximum length to allow before truncating
	 * @return The converted text
	 * @see Texts#ellipsiz(String, int)
	 */
	public String ellipsiz(String str, int len) {
		return Texts.ellipsiz(str, len);
	}

	/**
	 * convert to object to a json string
	 * @param o object
	 * @return json string
	 */
	public String toJson(Object o) {
		return Jsons.toJson(o);
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
	 * @param expr expression
	 * @return value
	 * @see Mvcs#findValue(panda.mvc.ActionContext, String)
	 */
	public Object findValue(String expr) {
		return Mvcs.findValue(getContext(), expr);
	}
	
	/**
	 * find value in context with argument
	 * @param expr expression
	 * @param arg argument
	 * @return value
	 * @see Mvcs#findValue(panda.mvc.ActionContext, String, Object)
	 */
	public Object findValue(String expr, Object arg) {
		return Mvcs.findValue(getContext(), expr, arg);
	}
}
