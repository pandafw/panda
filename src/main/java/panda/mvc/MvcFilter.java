package panda.mvc;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.config.FilterMvcConfig;

/**
 * 同 JSP/Serlvet 容器的挂接点
 */
public class MvcFilter implements Filter {

	private static final Log log = Logs.getLog(MvcFilter.class);

	protected ActionHandler handler;

	private static final String IGNORE = "^.+\\.(jsp|png|gif|jpg|js|css|jspx|jpeg|swf|ico)$";

	protected Pattern ignorePtn;

	/**
	 * 需要排除的路径前缀
	 */
	protected Pattern exclusionsPrefix;
	/**
	 * 需要排除的后缀名
	 */
	protected Pattern exclusionsSuffix;
	/**
	 * 需要排除的固定路径
	 */
	protected Set<String> exclusionPaths;

	public void init(FilterConfig conf) throws ServletException {
		log.infof("MvcFilter[%s] starting ...", conf.getFilterName());

		FilterMvcConfig config = new FilterMvcConfig(conf);
		
		handler = new ActionHandler(config);
		String regx = Strings.defaultString(config.getInitParameter("ignore"), IGNORE);
		if (!"null".equalsIgnoreCase(regx)) {
			ignorePtn = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		}
		String exclusions = config.getInitParameter("exclusions");
		if (exclusions != null) {
			String[] tmps = Strings.split(exclusions);
			Set<String> prefix = new HashSet<String>();
			Set<String> suffix = new HashSet<String>();
			Set<String> paths = new HashSet<String>();
			for (String tmp : tmps) {
				tmp = tmp.trim().intern();
				if (tmp.length() > 1) {
					if (tmp.startsWith("*")) {
						prefix.add(tmp.substring(1));
						continue;
					}
					else if (tmp.endsWith("*")) {
						suffix.add(tmp.substring(0, tmp.length() - 1));
						continue;
					}
				}
				paths.add(tmp);
			}
			if (prefix.size() > 0) {
				exclusionsPrefix = Pattern.compile("^(" + Strings.join(prefix, '|') + ")", Pattern.CASE_INSENSITIVE);
				log.info("exclusionsPrefix  = " + exclusionsPrefix);
			}
			if (suffix.size() > 0) {
				exclusionsSuffix = Pattern.compile("^(" + Strings.join(suffix, '|') + ")", Pattern.CASE_INSENSITIVE);
				log.info("exclusionsSuffix = " + exclusionsSuffix);
			}
			if (paths.size() > 0) {
				exclusionPaths = paths;
				log.info("exclusionsPath   = " + exclusionPaths);
			}
		}
	}

	public void destroy() {
		if (handler != null) {
			handler.depose();
		}
	}

	/**
	 * 过滤请求. 过滤顺序(ignorePtn,exclusionsSuffix,exclusionsPrefix,exclusionPaths)
	 * 
	 * @param matchUrl
	 * @throws IOException
	 * @throws ServletException
	 */
	protected boolean isExclusion(String matchUrl) throws IOException, ServletException {
		if (ignorePtn != null && ignorePtn.matcher(matchUrl).find()) {
			return true;
		}
		if (exclusionsSuffix != null) {
			if (exclusionsSuffix.matcher(matchUrl).find()) {
				return true;
			}
		}
		if (exclusionsPrefix != null) {
			if (exclusionsPrefix.matcher(matchUrl).find()) {
				return true;
			}
		}
		if (exclusionPaths != null) {
			for (String exclusionPath : exclusionPaths) {
				if (exclusionPath.equals(matchUrl)) {
					return true;
				}
			}
		}
		return false;
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		RequestPath path = RequestPath.getRequestPathObject(request);
		String matchUrl = path.getUrl();

		if (!isExclusion(matchUrl)) {
			if (handler.handle(request, response)) {
				return;
			}
		}
		nextChain(request, response, chain);
	}

	protected void nextChain(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		chain.doFilter(req, resp);
	}
}
