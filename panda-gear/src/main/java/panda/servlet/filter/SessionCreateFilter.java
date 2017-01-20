package panda.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Regexs;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.servlet.HttpServlets;


/**
 * SessionCreateFilter
 * 
 */
public class SessionCreateFilter implements Filter {
	private Log log = Logs.getLog(SessionCreateFilter.class);
	
	private List<Pattern> includes;
	private List<Pattern> excludes;

	/**
	 * Constructor.
	 */
	public SessionCreateFilter() {
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		String[] is = Strings.split(config.getInitParameter("includes"));
		if (Arrays.isNotEmpty(is)) {
			includes = new ArrayList<Pattern>();
			for (String s : is) {
				includes.add(Pattern.compile(s));
			}
		}

		String[] es = Strings.split(config.getInitParameter("excludes"));
		if (Arrays.isNotEmpty(es)) {
			excludes = new ArrayList<Pattern>();
			for (String s : es) {
				excludes.add(Pattern.compile(s));
			}
		}
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;

		if (shouldCreateSession(request)) {
			HttpSession hs = request.getSession(false);
			if (hs == null) {
				hs = request.getSession(true);
				if (log.isDebugEnabled()) {
					log.debug("Create HttpSession: " + hs.getId());
				}
			}
		}
		
		chain.doFilter(req, res);
	}

	private boolean shouldCreateSession(HttpServletRequest request) {
		String path = HttpServlets.getServletPath(request);
		if (Regexs.matches(excludes, path)) {
			return true;
		}
		
		if (Collections.isNotEmpty(includes)) {
			return Regexs.matches(includes, path);
		}
		
		return true;
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

}
