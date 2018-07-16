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

import panda.io.Settings;
import panda.ioc.Ioc;
import panda.ioc.Scope;
import panda.ioc.impl.ComboIocContext;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.impl.SingletonObjectProxy;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Regexs;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.impl.ActionInvoker;
import panda.mvc.ioc.IocRequestListener;
import panda.mvc.ioc.IocSessionListener;
import panda.mvc.ioc.RequestIocContext;
import panda.mvc.ioc.RequestObjectProxy;
import panda.mvc.ioc.ResponseObjectProxy;
import panda.mvc.ioc.SessionIocContext;
import panda.servlet.HttpServlets;

public class MvcHandler {
	private static final Log log = Logs.getLog(MvcHandler.class);

	private boolean deposed;

	private MvcLoader loading;

	/**
	 * the regex patterns of the URI to exclude
	 */
	private List<Pattern> ignoreRegexs;

	/**
	 * the servlet paths to exclude
	 */
	private Set<String> ignorePaths;

	/**
	 * ActionContext class
	 */
	private Class<? extends ActionContext> acClass = ActionContext.class;
	
	/**
	 * @return the loading
	 */
	public MvcLoader getLoading() {
		return loading;
	}

	/**
	 * @return the ioc
	 */
	public Ioc getIoc() {
		return loading.getIoc();
	}
	
	/**
	 * @param loading MvcLoader
	 * @param ioc ioc
	 * @param mapping action mapping
	 * @throws ClassNotFoundException if action context class is not found
	 */
	@SuppressWarnings("unchecked")
	public MvcHandler(MvcLoader loading) throws ClassNotFoundException {
		this.loading = loading;

		Settings settings = getIoc().get(Settings.class);
		
		String cls = getIoc().getIfExists(String.class, MvcConstants.MVC_ACTION_CONTEXT_TYPE);
		cls = settings.getProperty(SetConstants.MVC_ACTION_CONTEXT_TYPE, cls);
		if (Strings.isNotEmpty(cls)) {
			acClass = (Class<? extends ActionContext>)Classes.getClass(cls);
		}
		
		String ignores = getIoc().getIfExists(String.class, MvcConstants.MVC_IGNORES);
		ignores = settings.getProperty(SetConstants.MVC_IGNORES, ignores);
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
	 * @return true if path should ignored
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
		String path = HttpServlets.getServletPath(req);
		if (ignore(path)) {
			return false;
		}

		req.setAttribute(Mvcs.REQUEST_TIME, System.currentTimeMillis());

		Ioc ioc = loading.getIoc();
		RequestIocContext ric = null;

		if (ioc instanceof DefaultIoc) {
			if (IocRequestListener.isRequestScopeEnable || IocSessionListener.isSessionScopeEnable) {
				DefaultIoc di = ((DefaultIoc)ioc).clone();

				ComboIocContext ctx = new ComboIocContext();
				if (IocRequestListener.isRequestScopeEnable) {
					ric = RequestIocContext.get(req);
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
		
		try {
			ActionContext ac = acClass.newInstance();
			
			ac.setIoc(ioc);
			ac.setServlet(loading.getMvcConfig().getServletContext());
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

			ActionInvoker invoker = loading.getActionMapping().getActionInvoker(ac);
			if (invoker == null) {
				return false;
			}

			return invoker.invoke(ac);
		}
		catch (Throwable e) {
			log.error("Failed to handle " + path, e);
			throw Exceptions.wrapThrow(e);
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
			loading.depose();
			deposed = true;
		}
	}

}
