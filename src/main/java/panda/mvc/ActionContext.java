package panda.mvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.bean.Beans;
import panda.filepool.FilePool;
import panda.io.Settings;
import panda.ioc.Ioc;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.mvc.alert.ActionAlert;
import panda.mvc.alert.ApplicationAlert;
import panda.mvc.alert.ParamAlert;
import panda.mvc.alert.SessionAlert;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.ActionConsts;
import panda.mvc.util.StateProvider;
import panda.mvc.util.TextProvider;
import panda.servlet.HttpSessionMap;
import panda.servlet.ServletContextMap;
import panda.servlet.ServletRequestAttrMap;
import panda.servlet.ServletRequestHeaderMap;
import panda.servlet.ServletRequestParamMap;

public class ActionContext {
	private Ioc ioc;
	
	private String path;
	private List<String> pathArgs;

	private ServletContext servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ActionChain chain;
	
	private ActionAssist assist;
	private ActionConsts consts;

	private Locale locale;
	private Object action;
	private Object result;

	private Object[] args;
	private Object params;

	private Throwable error;
	
	private List<Object> tops;
	private Map<String, Object> vars;

	private Map<String, Object> castErrors;
	
	//--------------------------
	// cached ioc bean
	//
	private Settings settings;
	private FilePool filePool;
	private TextProvider text;
	private StateProvider state;

	private ActionAlert actionAlert;
	private ParamAlert paramAlert;
	private ApplicationAlert applicationAlert;
	private SessionAlert sessionAlert;
	

	/**
	 * Constructor
	 */
	public ActionContext() {
	}

	/**
	 * @return the ioc
	 */
	public Ioc getIoc() {
		return ioc;
	}

	/**
	 * @param ioc the ioc to set
	 */
	protected void setIoc(Ioc ioc) {
		this.ioc = ioc;
	}

	/**
	 * @return the info
	 */
	public ActionInfo getInfo() {
		return chain.getInfo();
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the pathArgs
	 */
	public List<String> getPathArgs() {
		return pathArgs;
	}

	/**
	 * @param pathArgs the pathArgs to set
	 */
	public void setPathArgs(List<String> pathArgs) {
		this.pathArgs = pathArgs;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @return the servlet
	 */
	public ServletContext getServlet() {
		return servlet;
	}

	/**
	 * @param servlet the servlet to set
	 */
	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	/**
	 * @return the chain
	 */
	public ActionChain getChain() {
		return chain;
	}

	/**
	 * @param chain the chain to set
	 */
	public void setChain(ActionChain chain) {
		this.chain = chain;
	}

	/**
	 * @return the action
	 */
	public Object getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Object action) {
		this.action = action;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return chain == null ? null : getInfo().getMethod();
	}

	/**
	 * @return the method name
	 */
	public String getMethodName() {
		return chain == null ? null : getInfo().getMethod().getName();
	}

	/**
	 * @return the adapter type
	 */
	public Class<? extends ParamAdaptor> getAdaptorType() {
		return chain == null ? null : getInfo().getAdaptor();
	}
	
	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param args the args to set
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}

	/**
	 * @return the params
	 */
	public Object getParams() {
		return params;
	}

	/**
	 * @param name parameter name
	 * @return parameter
	 */
	public Object getParameter(String name) {
		if (params == null) {
			return null;
		}
		
		if (Classes.isImmutable(params.getClass())) {
			return null;
		}
		
		return Beans.i().getBeanValue(params, name);
	}
	
	/**
	 * @param params the params to set
	 */
	public void setParams(Object params) {
		this.params = params;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the error
	 */
	public Throwable getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Throwable error) {
		this.error = error;
	}

	//----------------------------------------------------
	// ioc object
	//
	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		if (settings == null) {
			settings = ioc.get(Settings.class);
		}
		return settings;
	}

	/**
	 * @return the filePool
	 */
	public FilePool getFilePool() {
		if (filePool == null) {
			filePool = ioc.get(FilePool.class);
		}
		return filePool;
	}

	/**
	 * @return the text provider
	 */
	public TextProvider getText() {
		if (text == null) {
			text = ioc.get(TextProvider.class);
		}
		return text;
	}

	/**
	 * @return the state provider
	 */
	public StateProvider getState() {
		if (state == null) {
			state = ioc.get(StateProvider.class);
		}
		return state;
	}
	
	/**
	 * @return action alert
	 */
	public ActionAlert getActionAlert() {
		if (actionAlert == null) {
			actionAlert = ioc.get(ActionAlert.class);
		}
		return actionAlert;
	}
	
	public Collection<String> getActionErrors() {
		return getActionAlert().getErrors();
	}
	
	public Collection<String> getActionWarnings() {
		return getActionAlert().getWarnings();
	}
	
	public Collection<String> getActionConfirms() {
		return getActionAlert().getConfirms();
	}
	
	public Collection<String> getActionMessages() {
		return getActionAlert().getMessages();
	}
	
	/**
	 * @return parameter alert
	 */
	public ParamAlert getParamAlert() {
		if (paramAlert == null) {
			paramAlert = ioc.get(ParamAlert.class);
		}
		return paramAlert;
	}
	
	/**
	 * @return parameter errors
	 */
	public Map<String, List<String>> getParamErrors() {
		return getParamAlert().getErrors();
	}

	/**
	 * @return session alert
	 */
	public SessionAlert getSessionAlert() {
		if (sessionAlert == null) {
			sessionAlert = ioc.get(SessionAlert.class);
		}
		return sessionAlert;
	}
	
	public Collection<String> getSessionErrors() {
		return getSessionAlert().getErrors();
	}
	
	public Collection<String> getSessionWarnings() {
		return getSessionAlert().getWarnings();
	}
	
	public Collection<String> getSessionConfirms() {
		return getSessionAlert().getConfirms();
	}
	
	public Collection<String> getSessionMessages() {
		return getSessionAlert().getMessages();
	}
	
	/**
	 * @return application alert
	 */
	public ApplicationAlert getApplicationAlert() {
		if (applicationAlert == null) {
			applicationAlert = ioc.get(ApplicationAlert.class);
		}
		return applicationAlert;
	}
	
	public Collection<String> getApplicationErrors() {
		return getApplicationAlert().getErrors();
	}
	
	public Collection<String> getApplicationWarnings() {
		return getApplicationAlert().getWarnings();
	}
	
	public Collection<String> getApplicationConfirms() {
		return getApplicationAlert().getConfirms();
	}
	
	public Collection<String> getApplicationMessages() {
		return getApplicationAlert().getMessages();
	}

	/**
	 * @return the assist
	 */
	public ActionAssist getAssist() {
		if (assist == null) {
			assist = getIoc().get(ActionAssist.class);
		}
		return assist;
	}

	/**
	 * @return the consts
	 */
	public ActionConsts getConsts() {
		if (consts == null) {
			consts = getIoc().get(ActionConsts.class);
		}
		return consts;
	}
	
	//----------------------------------------------------
	/**
	 * @return the servlet context attributes map
	 */
	public Map<String, Object> getApp() {
		return new ServletContextMap(servlet);
	}

	/**
	 * @return the session attributes map
	 */
	public Map<String, Object> getSes() {
		return new HttpSessionMap(request);
	}

	/**
	 * @return the request attributes map
	 */
	public Map<String, Object> getReq() {
		return new ServletRequestAttrMap(request);
	}

	/**
	 * @return the request parameter map
	 */
	public Map<String, Object> getReqp() {
		return new ServletRequestParamMap(request);
	}

	/**
	 * @return the request header map
	 */
	public Map<String, Object> getReqh() {
		return new ServletRequestHeaderMap(request);
	}
	
	/**
	 * @return the base context path
	 */
	public String getBase() {
		return servlet.getContextPath();
	}

	//----------------------------------------------------
	// top stack
	//
	/**
	 * @return the top object
	 */
	public Object getTop() {
		return Collections.isEmpty(tops) ? null : tops.get(tops.size() - 1);
	}
	
	public List<Object> getTops() {
		if (tops == null) {
			tops = new ArrayList<Object>();
		}
		return tops;
	}
	
	public void push(Object top) {
		getTops().add(top);
	}
	
	public Object pop() {
		return Collections.isEmpty(tops) ? null : tops.remove(tops.size() - 1);
	}

	//----------------------------------------------------
	// vars map
	//
	public Map<String, Object> getVars() {
		if (vars == null) {
			vars = new HashMap<String, Object>();
		}
		return vars;
	}

	//----------------------------------------------------
	// text functions
	//
	public String text(String key) {
		return getText().getText(key);
	}

	public String text(String key, String def) {
		return getText().getText(key, def);
	}

	public String text(String key, String def, Object arg) {
		return getText().getText(key, def, arg);
	}


	//----------------------------------------------------
	/**
	 * @return the castErrors
	 */
	public Map<String, Object> getCastErrors() {
		if (castErrors == null) {
			castErrors = new HashMap<String, Object>();
		}
		return castErrors;
	}
	
	public void addCastErrors(Map<String, Object> errors) {
		getCastErrors().putAll(errors);
	}

	//----------------------------------------------------
	// shortcut alias
	//
	/**
	 * @return the action object
	 * @see #getAction()
	 */
	public Object getA() {
		return getAction();
	}

	/**
	 * @return the request parameters
	 * @see #getParams()
	 */
	public Object getP() {
		return getParams();
	}

	/**
	 * @return the result object
	 * @see #getResult()
	 */
	public Object getR() {
		return getResult();
	}

	/**
	 * @return the settings
	 * @see #getSettings()
	 */
	public Settings getS() {
		return getSettings();
	}

	/**
	 * @return the top object
	 * @see #getResult()
	 */
	public Object getT() {
		return getTop();
	}

	/**
	 * @return the vars object
	 * @see #getVars()
	 */
	public Map<String, Object> getV() {
		return getVars();
	}
}
