package panda.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.filepool.FilePool;
import panda.ioc.Ioc;

public class ActionContext extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	private static final String IOC = "ioc";
	
	private static final String PATH = "path";
	private static final String PATH_ARGS = "pathArgs";

	private static final String REQUEST = "request";
	private static final String RESPONSE = "response";
	private static final String SERVLET = "servlet";

	private static final String ACTION = "action";
	private static final String METHOD = "method";
	private static final String ARGS = "args";
	private static final String RESULT = "result";

	private static final String LOCALE = "locale";

	private static final String FILE_POOL = "filepool";
	
	private static final String ERROR = "error";

	public ActionContext() {
		
	}
	
	/**
	 * 获取全局的Ioc对象
	 * 
	 * @return 如果定义了IocBy注解,则肯定返回非空对象
	 */
	public Ioc getIoc() {
		return (Ioc)get(IOC);
	}
	
	public ActionContext setIoc(Ioc ioc) {
		put(IOC, ioc);
		return this;
	}

	/**
	 * 获取异常对象
	 */
	public Throwable getError() {
		return (Throwable)get(ERROR);
	}

	/**
	 * 设置异常对象,一般由ActionChain捕捉到异常后调用
	 * 
	 * @param error 异常对象
	 * @return 当前上下文,即被调用者本身
	 */
	public ActionContext setError(Throwable error) {
		put(ERROR, error);
		return this;
	}

	/**
	 * 获取当前请求的path,经过去后缀处理
	 * 
	 * @return 当前请求的path,经过去后缀处理
	 */
	public String getPath() {
		return (String)get(PATH);
	}

	/**
	 * 设置当前请求的path,经过去后缀处理
	 * 
	 * @param ph 请求的path,,经过去后缀处理
	 * @return 当前上下文,即被调用者本身
	 */
	public ActionContext setPath(String ph) {
		put(PATH, ph);
		return this;
	}

	/**
	 * 获取路径参数
	 * 
	 * @return 路径参数
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPathArgs() {
		return (List)get(PATH_ARGS);
	}

	public ActionContext setPathArgs(List<String> args) {
		put(PATH_ARGS, args);
		return this;
	}

	/**
	 * 获取这个Action对应的Method
	 */
	public Method getMethod() {
		return (Method)get(METHOD);
	}

	/**
	 * 设置这个Action对应的Method
	 * 
	 * @param m 这个Action对应的Method
	 * @return 当前上下文,即被调用者本身
	 */
	public ActionContext setMethod(Method m) {
		put(METHOD, m);
		return this;
	}

	/**
	 * 获取将要执行Method的对象
	 * 
	 * @return 执行对象,即模块类的实例
	 */
	public Object getAction() {
		return get(ACTION);
	}

	public ActionContext setAction(Object obj) {
		put(ACTION, obj);
		return this;
	}

	/**
	 * 获取将要执行Method的参数
	 * 
	 * @return method的参数
	 */
	public Object[] getArguments() {
		return (Object[])get(ARGS);
	}

	public ActionContext setArgumens(Object[] args) {
		put(ARGS, args);
		return this;
	}

	/**
	 * 获取method返回值
	 */
	public Object getResult() {
		return this.get(RESULT);
	}

	public ActionContext setResult(Object re) {
		put(RESULT, re);
		return this;
	}

	/**
	 * 获取请求的HttpServletRequest
	 * 
	 * @return 请求的HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return (HttpServletRequest)get(REQUEST);
	}

	public ActionContext setRequest(HttpServletRequest req) {
		put(REQUEST, req);
		return this;
	}

	/**
	 * 获取请求的HttpServletResponse
	 * 
	 * @return 请求的HttpServletResponse
	 */
	public HttpServletResponse getResponse() {
		return (HttpServletResponse)get(RESPONSE);
	}

	public ActionContext setResponse(HttpServletResponse resp) {
		put(RESPONSE, resp);
		return this;
	}

	/**
	 * 获取ServletContext
	 * 
	 * @return ServletContext
	 */
	public ServletContext getServlet() {
		return (ServletContext)get(SERVLET);
	}

	public ActionContext setServletContext(ServletContext sc) {
		put(SERVLET, sc);
		return this;
	}

	public Locale getLocale() {
		return (Locale)get(LOCALE);
	}

	public ActionContext setLocale(Locale locale) {
		put(LOCALE, locale);
		return this;
	}

	public FilePool getFilePool() {
		return (FilePool)get(FILE_POOL);
	}

	public ActionContext setFilePool(FilePool fp) {
		put(FILE_POOL, fp);
		return this;
	}
}
