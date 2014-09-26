package panda.wing.mvc;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import panda.dao.query.Order;
import panda.io.Files;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Sorter;
import panda.mvc.processor.LayoutProcessor;
import panda.mvc.util.ActionSupport;
import panda.mvc.util.StateProvider;
import panda.net.Inets;
import panda.net.http.HttpStatus;
import panda.net.http.URLHelper;
import panda.net.http.UserAgent;
import panda.servlet.HttpServlets;
import panda.servlet.filter.RequestLoggingFilter;


/**
 */
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
	
	/**
	 * @return servlet exception stack trace
	 */
	public String getServletExceptionStackTrace() {
		HttpServletRequest req = getRequest();
		Throwable ex = (Throwable)req.getAttribute(HttpServlets.ERROR_EXCEPTION_ATTRIBUTE);
		if (ex != null) {
			return Exceptions.getStackTrace(ex);
		}
		else {
			return Strings.EMPTY;
		}
	}

	/**
	 * @return contextPath
	 */
	public String getContextPath() {
		return getRequest().getContextPath();
	}

	/**
	 * @return relativeURI
	 */
	public String getRelativeURI() {
		return HttpServlets.getRelativeURI(getRequest());
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
		return Calendar.getInstance().getTime();
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
	 * load sorter parameters from stateProvider
	 * @param sorter sorter
	 * @throws Exception if an error occurs
	 */
	public void loadSorterParams(Sorter sorter) throws Exception {
		StateProvider sp = getState();
		if (sp == null) {
			return;
		}
				
		if (Strings.isEmpty(sorter.getColumn())) {
			String sc = (String)sp.loadState("list.sc");
			if (Strings.isEmpty(sc)) {
				String tx = getMethodName() + ActionRC.SORTER_COLUMN_SUFFIX;
				sc = getText(tx, (String)null);
				if (sc == null && !ActionRC.LIST_SORTER_COLUMN.equals(tx)) {
					sc = getText(ActionRC.LIST_SORTER_COLUMN, (String)null);
				}
			}
			if (Strings.isNotEmpty(sc)) {
				sorter.setColumn(sc);
			}
		}
		if (Strings.isNotEmpty(sorter.getColumn())) {
			if (Strings.isEmpty(sorter.getDirection())) {
				String sd = (String)sp.loadState("list.sd");
				if (Strings.isEmpty(sd)) {
					String tx = getMethodName() + ActionRC.SORTER_DIRECTION_SUFFIX;
					sd = getText(tx, (String)null);
					if (sd == null && !ActionRC.LIST_SORTER_DIRECTION.equals(tx)) {
						sd = getText(ActionRC.LIST_SORTER_DIRECTION, (String)null);
					}
					if (sd == null) {
						sd = Sorter.ASC;
					}
				}
				sorter.setDirection(sd);
			}
		}
	}
	
	/**
	 * load pager limit parameters from stateProvider
	 * @param pager pager
	 * @throws Exception if an error occurs
	 */
	public void loadLimitParams(Pager pager) throws Exception {
		if (pager.getLimit() == null || pager.getLimit() < 1) {
			pager.setLimit(Numbers.toLong((String)getState().loadState("list.pl"), 0L));
		}
		if (pager.getLimit() == null || pager.getLimit() < 1) {
			String tx = getMethodName() + ActionRC.PAGER_LIMIT_SUFFIX;
			Long l = getTextAsLong(tx);
			if (l == null && !ActionRC.LIST_PAGER_LIMIT.equals(tx)) {
				l = getTextAsLong(ActionRC.LIST_PAGER_LIMIT);
			}
			if (l == null) {
				l = ActionRC.DEFAULT_LIST_PAGER_LIMIT;
			}
			pager.setLimit(l);
		}
		limitPage(pager);
	}
	
	/**
	 * save sorter parameters to stateProvider
	 * @param sorter sorter
	 * @throws Exception if an error occurs
	 */
	public void saveSorterParams(Sorter sorter) throws Exception {
		getState().saveState("list.sc", sorter.getColumn());
		getState().saveState("list.sd", sorter.getDirection());
	}
	
	/**
	 * save pager limit parameters to stateProvider
	 * @param pager pager
	 * @throws Exception if an error occurs
	 */
	public void saveLimitParams(Pager pager) throws Exception {
		getState().saveState("list.pl", pager.getLimit());
	}
	
	/**
	 * if pager.limit > maxLimit then set pager.limit = maxLimit
	 * @param pager pager
	 */
	public void limitPage(Pager pager) {
		long maxLimit = getTextAsLong(ActionRC.PAGER_MAX_LIMIT, 100L);
		if (pager.getLimit() == null 
				|| pager.getLimit() < 1 
				|| pager.getLimit() > maxLimit) {
			pager.setLimit(maxLimit);
		}
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
}
