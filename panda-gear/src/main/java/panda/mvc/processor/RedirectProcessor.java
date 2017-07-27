package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.annotation.Redirect;
import panda.mvc.util.MvcURLBuilder;
import panda.net.URLHelper;
import panda.servlet.HttpServlets;


/**
 * Redirect Processor
 * <p>
 * Samples：
 * <p>
 * By Annotation: '@Redirect("/pet/list")'
 * <p>
 * 
 * <p>
 * @Redirect("//anotherContext/some") -> redirect to: /anotherContext/some
 * @Redirect("//some") -> redirect to: /thisContext/some
 * @Redirect("+/some") -> redirect to: /thisContext + context.getPath() + /some
 * @Redirect("~/some") -> redirect to: /thisContext + context.getPath() + /../some
 * 
 * <p>
 * By Parameter: 
 * /a.do?__redir=/pdf.do&__param=url&b=1&c=2 -> redirect to: /thisContext/pdf?url=escape(/thisContext/a.do?b=1&c=2)
 */
@IocBean
public class RedirectProcessor extends AbstractProcessor {
	private static final Log log = Logs.getLog(RedirectProcessor.class);

	/**
	 * DEFAULT_PARAMETER = "__redir";
	 */
	public static final String DEFAULT_PARAMETER = "__redir";

	/**
	 * DEFAULT_QUERY = "__query";
	 */
	public static final String DEFAULT_QUERY = "__query";

	@IocInject(value=MvcConstants.REDIRECT_PARAMETER, required=false)
	protected String parameName = DEFAULT_PARAMETER;

	@IocInject(value=MvcConstants.REDIRECT_QUERY_NAME, required=false)
	protected String queryName = DEFAULT_QUERY;

	public void process(ActionContext ac) {
		if (redirectByParameter(ac)) {
			return;
		}
		if (redirectByAnnotation(ac)) {
			return;
		}
		doNext(ac);
	}
	
	protected boolean redirectByParameter(ActionContext ac) {
		String red = ac.getRequest().getParameter(parameName);
		if (Strings.isNotEmpty(red)) {
			// redirect action (ex: PDF Action)
			String qs = null;
			String xqn = ac.getRequest().getParameter(queryName);
			if (Strings.isNotEmpty(xqn)) {
				MvcURLBuilder ub = ac.getIoc().get(MvcURLBuilder.class);
				ub = ac.getIoc().get(MvcURLBuilder.class);
				ub.setForceAddSchemeHostAndPort(true);
				ub.setIncludeContext(true);
				ub.setIncludeParams(MvcURLBuilder.INCLUDE_ALL);
				ub.setExcludeParams(Arrays.asList(parameName, queryName));
				qs = xqn + "=" + URLHelper.encodeURL(ub.build());
			}
			
			MvcURLBuilder ub = ac.getIoc().get(MvcURLBuilder.class);
			ub.setAction(red);
			ub.setForceAddSchemeHostAndPort(true);
			ub.setIncludeContext(true);
			ub.setIncludeParams(MvcURLBuilder.INCLUDE_NONE);
			ub.setQuery(qs);

			String url = ub.build();
			HttpServlets.sendRedirect(ac.getResponse(), url);
			return true;
		}
		return false;
	}
	
	protected boolean redirectByAnnotation(ActionContext ac) {
		Redirect r = ac.getMethod().getAnnotation(Redirect.class);
		if (r != null) {
			if (Strings.isNotEmpty(r.value())) {
				String url = MvcURLBuilder.buildPath(ac, r.value());
				if (log.isDebugEnabled()) {
					log.debug("Redirect to: " + url);
				}
				HttpServlets.sendRedirect(ac.getResponse(), url);
				return true;
			}

			if (r.toslash()) {
				if (!Strings.endsWithChar(ac.getPath(), '/')) {
					String url = ac.getBase() + ac.getPath() + '/';
					if (log.isDebugEnabled()) {
						log.debug("Redirect to: " + url);
					}
					HttpServlets.sendRedirect(ac.getResponse(), url);
					return true;
				}
			}
		}
		return false;
	}
}
