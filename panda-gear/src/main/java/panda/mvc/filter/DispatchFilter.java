package panda.mvc.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Settings;
import panda.ioc.Ioc;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.impl.ComboIocContext;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.impl.SingletonObjectProxy;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Regexs;
import panda.lang.Strings;
import panda.lang.collection.CaseInsensitiveSet;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;
import panda.mvc.ServletChain;
import panda.mvc.ServletFilter;
import panda.mvc.SetConstants;
import panda.mvc.impl.ActionDispatcher;
import panda.mvc.ioc.IocSessionListener;
import panda.mvc.ioc.RequestIocContext;
import panda.mvc.ioc.RequestObjectProxy;
import panda.mvc.ioc.ResponseObjectProxy;
import panda.mvc.ioc.SessionIocContext;
import panda.servlet.HttpServlets;

@IocBean(create="initialize")
public class DispatchFilter implements ServletFilter {
	private static final Log log = Logs.getLog(DispatchFilter.class);

	@IocInject
	private Ioc ioc;

	@IocInject
	private ServletContext servlet;

	@IocInject
	private ActionMapping mapping;
	
	@IocInject
	private Settings settings;

	/**
	 * ActionContext class
	 */
	@IocInject(value=MvcConstants.MVC_ACTION_CONTEXT_TYPE, required=false)
	private Class<? extends ActionContext> acClass = ActionContext.class;

	@IocInject(value=MvcConstants.MVC_MAPPING_CASE_IGNORE, required=false)
	private boolean ignoreCase;

	/**
	 * ignore settings
	 */
	@IocInject(value=MvcConstants.MVC_MAPPING_IGNORES, required=false)
	private List<String> ignores;

	/**
	 * the regex patterns of the URI to exclude
	 */
	private List<Pattern> ignoreRegexs;

	/**
	 * the servlet paths to exclude
	 */
	private Set<String> ignorePaths;

	/**
	 * initialize on ioc bean created
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		ignores = (List<String>)settings.getPropertyAsList(SetConstants.MVC_MAPPING_IGNORES, ignores);

		if (Collections.isEmpty(ignores)) {
			return;
		}

		Set<String> regex = new HashSet<String>();
		Set<String> paths = ignoreCase ? new CaseInsensitiveSet<String>() : new HashSet<String>();
		for (String s : ignores) {
			s = Strings.strip(s);
			if (Strings.isEmpty(s)) {
				continue;
			}
			
			if (Strings.startsWithChar(s, '^') || Strings.endsWithChar(s, '&')) {
				regex.add(s);
				continue;
			}
			paths.add(s);
		}

		if (paths.size() > 0) {
			ignorePaths = paths;
			log.info("mvc ignore paths   = " + ignorePaths);
		}

		if (regex.size() > 0) {
			List<Pattern> patterns = new ArrayList<Pattern>();
			for (String s : regex) {
				patterns.add(ignoreCase ? Pattern.compile(s, Pattern.CASE_INSENSITIVE) : Pattern.compile(s));
			}
			ignoreRegexs = patterns;
			log.info("mvc ignore regexs  = " + Strings.join(regex, " "));
		}
	}

	/**
	 * determine the servlet path should exclude or not.
	 * order: ignorePaths, ignoreRegexs
	 * 
	 * @param path servlet path
	 * @return true if path should ignored
	 */
	protected boolean ignore(String path) {
		if (ignorePaths != null && ignorePaths.contains(path)) {
			return true;
		}
		if (ignoreRegexs != null && Regexs.matches(ignoreRegexs, path)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
		String path = HttpServlets.getServletPath(req);
		if (ignore(path)) {
			if (log.isDebugEnabled()) {
				log.debug("IGNORE: " + path);
			}
			return false;
		}

		Ioc ioc = this.ioc;
		RequestIocContext ric = null;

		if (ioc instanceof DefaultIoc) {
			// create a combo ioc context for request/session scope
			DefaultIoc di = ((DefaultIoc)ioc).clone();

			ComboIocContext ctx = new ComboIocContext();
			ric = new RequestIocContext();
			ctx.addContext(ric);
			
			if (IocSessionListener.isSessionScopeEnable) {
				ctx.addContext(SessionIocContext.get(req));
			}

			ctx.addContext(di.getContext());
			di.setContext(ctx);
			ioc = di;
		}
		
		try {
			ActionContext ac = acClass.newInstance();
			
			ac.setIoc(ioc);
			ac.setServlet(servlet);
			ac.setRequest(req);
			ac.setResponse(res);

			if (ric != null) {
				RequestObjectProxy opReq = new RequestObjectProxy(ac);
				ResponseObjectProxy opRes = new ResponseObjectProxy(ac);

				ric.save(Scope.REQUEST, ActionContext.class.getName(), new SingletonObjectProxy(ac));
				ric.save(Scope.REQUEST, ServletRequest.class.getName(), opReq);
				ric.save(Scope.REQUEST, ServletResponse.class.getName(), opRes);
				ric.save(Scope.REQUEST, HttpServletRequest.class.getName(), opReq);
				ric.save(Scope.REQUEST, HttpServletResponse.class.getName(), opRes);
			}

			// save action context to request
			Mvcs.setActionContext(req, ac);

			ActionDispatcher dispatcher = mapping.getActionDispatcher(ac);
			if (dispatcher == null) {
				if (log.isDebugEnabled()) {
					log.debug("SKIP: " + path);
				}
			}
			else {
				if (log.isDebugEnabled()) {
					log.debug("Dispatch: " + path);
				}
				if (dispatcher.dispatch(ac)) {
					return true;
				}
			}

			// do next
			return sc.doNext(req, res);
		}
		catch (Throwable e) {
			log.error("Failed to handle " + path, e);
			throw Exceptions.wrapThrow(e);
		}
		finally {
			if (ric != null) {
				ric.depose();
			}
			
			// remove action context from request
			Mvcs.setActionContext(req, null);
		}
	}
}
