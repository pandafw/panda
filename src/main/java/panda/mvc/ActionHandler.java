package panda.mvc;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.Ioc;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.ioc.impl.ComboIocContext;
import panda.ioc.impl.DefaultIoc;
import panda.lang.Classes;
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
import panda.servlet.HttpServlets;

public class ActionHandler {
	private static final Log log = Logs.getLog(ActionHandler.class);

	private boolean deposed;
	
	private Loading loading;

	private UrlMapping mapping;

	private MvcConfig config;

	private Pattern ignorePtn;

	/**
	 * 需要排除的路径前缀
	 */
	private Pattern exclusionsPrefix;

	/**
	 * 需要排除的后缀名
	 */
	private Pattern exclusionsSuffix;

	/**
	 * 需要排除的固定路径
	 */
	private Set<String> exclusionPaths;

	/**
	 * @return the loading
	 */
	public Loading getLoading() {
		return loading;
	}

	/**
	 * @return the mapping
	 */
	public UrlMapping getMapping() {
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

		String regx = config.getInitParameter("ignore");
		if (Strings.isNotEmpty(regx)) {
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

	/**
	 * 过滤请求. 过滤顺序(ignorePtn,exclusionsSuffix,exclusionsPrefix,exclusionPaths)
	 * 
	 * @param matchUrl
	 */
	protected boolean isExclusion(String matchUrl) {
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

	public boolean handle(HttpServletRequest req, HttpServletResponse res) {
		String url = HttpServlets.getServletPath(req);
		if (isExclusion(url)) {
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

					ric.save(Scope.REQUEST, ActionContext.class.getName(), new ObjectProxy(ac));
					ric.save(Scope.REQUEST, ServletRequest.class.getName(), new ObjectProxy(req));
					ric.save(Scope.REQUEST, ServletResponse.class.getName(), new ObjectProxy(res));
					ric.save(Scope.REQUEST, HttpServletRequest.class.getName(), new ObjectProxy(req));
					ric.save(Scope.REQUEST, HttpServletResponse.class.getName(), new ObjectProxy(res));
					
					ctx.addContext(ric);
				}
				
				if (IocSessionListener.isSessionScopeEnable) {
					SessionIocContext sic = SessionIocContext.get(req.getSession());
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
		ac.setResponse(res);

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
		}
	}

	public void depose() {
		if (!deposed) {
			loading.depose(config);
			deposed = true;
		}
	}

}
