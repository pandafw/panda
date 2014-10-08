package panda.mvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.filepool.FilePool;
import panda.ioc.Ioc;
import panda.lang.Collections;
import panda.mvc.aware.ActionAware;
import panda.mvc.aware.ApplicationAware;
import panda.mvc.aware.ParamAware;
import panda.mvc.aware.SessionAware;
import panda.mvc.util.StateProvider;
import panda.mvc.util.TextProvider;
import panda.servlet.HttpSessionMap;
import panda.servlet.ServletContextMap;
import panda.servlet.ServletRequestAttrMap;
import panda.servlet.ServletRequestParamMap;

public class ActionContext {
	private Ioc ioc;
	
	private String path;
	private List<String> pathArgs;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servlet;

	private Object action;
	private Method method;
	private Object[] args;
	private Object params;
	private Object result;
	private Locale locale;

	private Throwable error;
	
	private List<Object> tops;

	private Map<String, Object> castErrors;
	
	//--------------------------
	// cached ioc bean
	//
	private FilePool filePool;
	private TextProvider textProvider;
	private StateProvider stateProvider;

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
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
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
	public TextProvider getTextProvider() {
		if (textProvider == null) {
			textProvider = ioc.get(TextProvider.class);
		}
		return textProvider;
	}

	/**
	 * @return the state provider
	 */
	public StateProvider getStateProvider() {
		if (stateProvider == null) {
			stateProvider = ioc.get(StateProvider.class);
		}
		return stateProvider;
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
	 * @return the request parameters map
	 */
	public Map<String, Object> getReqp() {
		return new ServletRequestParamMap(request);
	}
	
	/**
	 * @return the base context path
	 */
	public String getBase() {
		return servlet.getContextPath();
	}
	
	/**
	 * @return action aware
	 */
	public ActionAware getActionAware() {
		return ioc.get(ActionAware.class);
	}
	
	/**
	 * @return parameter aware
	 */
	public ParamAware getParamAware() {
		return ioc.get(ParamAware.class);
	}
	
	/**
	 * @return session aware
	 */
	public SessionAware getSessionAware() {
		return ioc.get(SessionAware.class);
	}
	
	/**
	 * @return application aware
	 */
	public ApplicationAware getApplicationAware() {
		return ioc.get(ApplicationAware.class);
	}

	//----------------------------------------------------
	// top
	//
	/**
	 * @return the top object
	 */
	public Object getTop() {
		return Collections.isEmpty(tops) ? null : tops.get(tops.size() - 1);
	}
	
	private List<Object> getTops() {
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
	// utility functions
	//
	public String text(String key, String def) {
		return getTextProvider().getText(key, def);
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
	 * @return the top object
	 * @see #getResult()
	 */
	public Object getT() {
		return getTop();
	}
}
