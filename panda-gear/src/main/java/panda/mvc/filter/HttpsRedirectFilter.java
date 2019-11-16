package panda.mvc.filter;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Regexs;
import panda.mvc.MvcConstants;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;
import panda.mvc.SetConstants;
import panda.mvc.util.MvcSettings;
import panda.net.Scheme;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLBuilder;


@IocBean(create="initialize")
public class HttpsRedirectFilter implements ServletFilter {
	@IocInject(value=MvcConstants.MVC_HTTPS_REDIRECT, required=false)
	private boolean redirect = true;

	@IocInject(value=MvcConstants.MVC_HTTPS_PORT, required=false)
	private int httpsport = 443;

	@IocInject(value=MvcConstants.MVC_MAPPING_CASE_IGNORE, required=false)
	protected boolean ignoreCase;
	
	@IocInject(value=MvcConstants.MVC_HTTPS_REDIRECT_INCLUDES, required=false)
	private List<String> includes;

	@IocInject(value=MvcConstants.MVC_HTTPS_REDIRECT_EXCLUDES, required=false)
	private List<String> excludes;

	@IocInject
	private MvcSettings settings;

	private List<Pattern> incPatterns;
	private List<Pattern> excPatterns;
	
	@SuppressWarnings("unchecked")
	public void initialize() {
		includes = (List<String>)settings.getPropertyAsList(SetConstants.MVC_HTTPS_REDIRECT_INCLUDES, includes);
		excludes = (List<String>)settings.getPropertyAsList(SetConstants.MVC_HTTPS_REDIRECT_EXCLUDES, excludes);

		int flags = ignoreCase ? Pattern.CASE_INSENSITIVE : 0;
		incPatterns = Regexs.compiles(includes, flags);
		excPatterns = Regexs.compiles(excludes, flags);
	}

	@Override
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
		boolean enabled = settings.getPropertyAsBoolean(SetConstants.MVC_HTTPS_REDIRECT, redirect);
		if (enabled) {
			String schema = HttpServlets.getScheme(req);
			if (Scheme.HTTP.equals(schema)) {
				String uri = req.getRequestURI();

				if (Regexs.matches(excPatterns, uri)) {
					return sc.doNext(req, res);
				}

				if (Collections.isEmpty(incPatterns) || Regexs.matches(incPatterns, uri)) {
					int port = settings.getPropertyAsInt(SetConstants.MVC_HTTPS_PORT, httpsport);

					ServletURLBuilder sub = new ServletURLBuilder();
					sub.setRequest(req);
					sub.setScheme(Scheme.HTTPS);
					sub.setHost(req.getServerName());
					sub.setPort(port);
					sub.setParams(req.getParameterMap());
		
					String url = sub.build();
		
					HttpServlets.sendRedirect(res, url);

					// stop filter chain
					return true;
				}
			}
		}

		return sc.doNext(req, res);
	}
}

