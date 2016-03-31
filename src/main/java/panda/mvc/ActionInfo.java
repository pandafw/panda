package panda.mvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.reflect.Methods;
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
		if (paths != null && Arrays.isNotEmpty(parent.paths)) {
			Set<String> myPaths = new HashSet<String>(paths.length * parent.paths.length);
			for (String p : paths) {
				// absolute path
				if (p.length() > 0 && p.charAt(0) == '/') {
					myPaths.add(p);
					continue;
				}
				
				// relative path
				for (String pp : parent.paths) {
					if (Strings.isEmpty(pp)) {
						myPaths.add('/' + p);
					}
					else {
						if (Strings.endsWithChar(pp, '/')) {
							myPaths.add(pp + p);
						}
						else {
							myPaths.add(pp + '/' + p);
						}
						if (Strings.isEmpty(p)) {
							myPaths.add(pp);
						}
					}
				}
			}
			paths = myPaths.toArray(new String[myPaths.size()]);
		}

		// set defaults
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(Strings.rightPad(Strings.join(paths, ", "), 50));
		sb.append(" >> ");
		sb.append(Methods.toSimpleString(method));
		sb.append(":");
		sb.append(" @Ok(").append(okView).append(")");
		sb.append(" @Err(").append(errorView).append(")");
		sb.append(" @Fatal(").append(fatalView).append(")");
		return sb.toString();
	}
}
