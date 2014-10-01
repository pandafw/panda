package panda.mvc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.filepool.FilePool;
import panda.ioc.Ioc;
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
	private Object result;
	private Locale locale;

	private Throwable error;

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
		return ioc.get(FilePool.class);
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
	public Map<String, Object> getParam() {
		return new ServletRequestParamMap(request);
	}
	
	/**
	 * @return the base context path
	 */
	public String getBase() {
		return servlet.getContextPath();
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
	 * @return the request parameters map
	 * @see #getParam()
	 */
	public Map<String, Object> getP() {
		return getParam();
	}

	/**
	 * @return the result object
	 * @see #getResult()
	 */
	public Object getR() {
		return getResult();
	}

}
