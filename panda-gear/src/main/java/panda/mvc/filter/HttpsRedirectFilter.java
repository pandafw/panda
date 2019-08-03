package panda.mvc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;
import panda.mvc.SetConstants;
import panda.mvc.util.MvcSettings;
import panda.net.Scheme;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLBuilder;


@IocBean
public class HttpsRedirectFilter implements ServletFilter {
	@IocInject(value=MvcConstants.MVC_HTTPS_REDIRECT, required=false)
	private boolean redirect = true;

	@IocInject
	private MvcSettings settings;
	
	@Override
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
		boolean enabled = settings.getPropertyAsBoolean(SetConstants.MVC_HTTPS_REDIRECT, redirect);
		if (enabled) {
			String schema = HttpServlets.getScheme(req);
			if (Scheme.HTTP.equals(schema)) {
				ServletURLBuilder sub = new ServletURLBuilder();
				sub.setRequest(req);
				sub.setScheme(Scheme.HTTPS);
				sub.setHost(req.getServerName());
				sub.setParams(req.getParameterMap());
				String url = sub.build();
				HttpServlets.sendRedirect(res, url);

				// stop filter chain
				return true;
			}
		}

		return sc.doNext(req, res);
	}
}

