package panda.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.Ioc;
import panda.ioc.Scope;
import panda.ioc.impl.ComboIocContext;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.impl.DefaultObjectProxy;
import panda.lang.Classes;
import panda.lang.Regexs;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.config.AbstractMvcConfig;
import panda.mvc.impl.ActionInvoker;
import panda.mvc.impl.DefaultMvcLoading;
import panda.mvc.ioc.IocRequestListener;
import panda.mvc.ioc.IocSessionListener;
import panda.mvc.ioc.RequestIocContext;
import panda.mvc.ioc.SessionIocContext;
import panda.servlet.DelegateHttpServletResponseWrapper;
import panda.servlet.HttpServlets;

public class ActionHandler {
	private static final Log log = Logs.getLog(ActionHandler.class);

	private boolean deposed;
	
	private Loading loading;

	private ActionMapping mapping;

	private MvcConfig config;

	/**
	 * the regex patterns of the URI to exclude
	 */
	private List<Pattern> ignoreRegexs;

	/**
	 * the servlet paths to exclude
	 */
	private Set<String> ignorePaths;

	/**
	 * @return the loading
	 */
	public Loading getLoading() {
		return loading;
	}

	/**
	 * @return the mapping
	 */
	public ActionMapping getMapping() {
		return mapping;
	}

	/**
	 * @return the config
	 */
	public MvcConfig getConfig() {
		return config;
	}

	/**
	 * init
	 * @param config MvcConfig
	 */
	public ActionHandler(AbstractMvcConfig config) {
		this.config = config;
		this.loading = new DefaultMvcLoading();
		this.mapping = loading.load(config);

		String ignores = config.getInitParameter("ignores");
		if (ignores != null) {
			String[] es = Strings.split(ignores);
			Set<String> regex = new HashSet<String>();
			Set<String> paths = new HashSet<String>();
			for (String s : es) {
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
			if (regex.size() > 0) {
				ignoreRegexs = new ArrayList<Pattern>();
				for (String s : regex) {
					ignoreRegexs.add(Pattern.compile(s));
				}
				log.info("ignore regexs  = " + Strings.join(regex, " "));
			}
			if (paths.size() > 0) {
				ignorePaths = paths;
				log.info("ignore paths   = " + ignorePaths);
			}
		}
	}

	/**
	 * determine the servlet path should exclude or not.
	 * order: ignorePaths, ignoreRegexs
	 * 
	 * @param path servlet path
	 */
	protected boolean ignore(String path) {
		if (ignorePaths != null && ignorePaths.contains(path)) {
			return true;
		}
		if (Regexs.matches(ignoreRegexs, path)) {
			return true;
		}
		return false;
	}

	public boolean handle(HttpServletRequest req, HttpServletResponse res) {
		if (log.isTraceEnabled()) {
			log.trace(config.getAppName() + " handle:\n" + HttpServlets.dumpRequestProperties(req));
		}
		
		String path = HttpServlets.getServletPath(req);
		if (ignore(path)) {
			return false;
		}

		ActionContext ac = Classes.born(config.getContextClass());
		RequestIocContext ric = null;

		Ioc ioc = config.getIoc();
		if (ioc instanceof DefaultIoc) {
			if (IocRequestListener.isRequestScopeEnable || IocSessionListener.isSessionScopeEnable) {
				DefaultIoc di = ((DefaultIoc)ioc).clone();

				ComboIocContext ctx = new ComboIocContext();
				if (IocRequestListener.isRequestScopeEnable) {
					ric = RequestIocContext.get(req);

					ric.save(Scope.REQUEST, ActionContext.class.getName(), new DefaultObjectProxy(ac));
					ric.save(Scope.REQUEST, ServletRequest.class.getName(), new DefaultObjectProxy(req));
					ric.save(Scope.REQUEST, ServletResponse.class.getName(), new DefaultObjectProxy(res));
					ric.save(Scope.REQUEST, HttpServletRequest.class.getName(), new DefaultObjectProxy(req));
					ric.save(Scope.REQUEST, HttpServletResponse.class.getName(), new DefaultObjectProxy(res));
					
					ctx.addContext(ric);
				}
				
				if (IocSessionListener.isSessionScopeEnable) {
					SessionIocContext sic = SessionIocContext.get(req);
					ctx.addContext(sic);
				}
				
				ctx.addContext(di.getContext());
				di.setContext(ctx);
				
				ioc = di;
			}
		}

		ac.setIoc(ioc);
		ac.setServlet(config.getServletContext());
		ac.setRequest(req);
		ac.setResponse(new DelegateHttpServletResponseWrapper(res));

		// save action context to request
		Mvcs.setActionContext(req, ac);
		try {
			ActionInvoker invoker = mapping.getActionInvoker(ac);
			if (invoker == null) {
				return false;
			}

			return invoker.invoke(ac);
		}
		finally {
			if (ric != null) {
				RequestIocContext.depose(req);
			}
			
			// remove action context from request
			Mvcs.setActionContext(req, null);
		}
	}

	public void depose() {
		if (!deposed) {
			loading.depose(config);
			deposed = true;
		}
	}

}
