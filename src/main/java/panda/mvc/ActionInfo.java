package panda.mvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import panda.net.http.HttpMethod;

public class ActionInfo {

	private String[] paths;

	private String chainName;

	private Class<? extends ParamAdaptor> adaptor;

	private String okView;
	private String errorView;
	private String fatalView;

	private List<HttpMethod> httpMethods;

	private Class<?> actionType;

	private Method method;

	public ActionInfo() {
		httpMethods = new ArrayList<HttpMethod>(4);
	}

	public ActionInfo mergeWith(ActionInfo parent) {
		// 组合路径 - 与父路径做一个笛卡尔积
		if (null != paths && null != parent.paths && parent.paths.length > 0) {
			List<String> myPaths = new ArrayList<String>(paths.length * parent.paths.length);
			for (int i = 0; i < parent.paths.length; i++) {
				String pp = parent.paths[i];
				for (int x = 0; x < paths.length; x++) {
					myPaths.add(pp + paths[x]);
				}
			}
			paths = myPaths.toArray(new String[myPaths.size()]);
		}

		// 填充默认值
		adaptor = null == adaptor ? parent.adaptor : adaptor;
		okView = null == okView ? parent.okView : okView;
		errorView = null == errorView ? parent.errorView : errorView;
		fatalView = null == fatalView ? parent.fatalView : fatalView;
		actionType = null == actionType ? parent.actionType : actionType;
		chainName = null == chainName ? parent.chainName : chainName;
		return this;
	}

	/**
	 * @return 这个入口函数是不是只匹配特殊的 http 方法。
	 */
	public boolean hasHttpMethod() {
		return httpMethods.size() > 0;
	}

	/**
	 * 只能接受如下字符串
	 * <ul>
	 * <li>GET
	 * <li>PUT
	 * <li>POST
	 * <li>DELETE
	 * </ul>
	 * 
	 * @return 特殊的 HTTP 方法列表
	 */
	public List<HttpMethod> getHttpMethods() {
		return httpMethods;
	}

	public String[] getPaths() {
		return paths;
	}

	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	public Class<? extends ParamAdaptor> getAdaptor() {
		return adaptor;
	}

	public void setAdaptor(Class<? extends ParamAdaptor> adaptor) {
		this.adaptor = adaptor;
	}

	public String getChainName() {
		return chainName;
	}

	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	public String getOkView() {
		return okView;
	}

	public void setOkView(String okView) {
		this.okView = okView;
	}

	public String getErrorView() {
		return errorView;
	}

	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	public String getFatalView() {
		return fatalView;
	}

	public void setFatalView(String fatalView) {
		this.fatalView = fatalView;
	}

	public Class<?> getActionType() {
		return actionType;
	}

	public void setActionType(Class<?> actionType) {
		this.actionType = actionType;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
