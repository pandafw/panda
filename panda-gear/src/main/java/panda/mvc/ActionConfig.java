package panda.mvc;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.reflect.Methods;

public class ActionConfig {

	private String[] paths;

	private String chainName;

	private Class<? extends ParamAdaptor> adaptor;

	private String defaultView;
	private String errorView;
	private String fatalView;

	private String[] atMethods;

	private Class<?> actionType;

	private Method actionMethod;

	public ActionConfig() {
	}

	public ActionConfig mergeWith(ActionConfig parent) {
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
						
						// @At("") -> [ '/parent', '/parent/' ]
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
		defaultView = null == defaultView ? parent.defaultView : defaultView;
		errorView = null == errorView ? parent.errorView : errorView;
		fatalView = null == fatalView ? parent.fatalView : fatalView;
		actionType = null == actionType ? parent.actionType : actionType;
		chainName = null == chainName ? parent.chainName : chainName;
		return this;
	}

	/**
	 * normalize paths
	 */
	public void normalizePaths() {
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (Strings.isEmpty(path)) {
				paths[i] = "/";
			}
			else {
				paths[i] = '/' + Strings.stripStart(path, '/');
			}
		}

	}

	/**
	 * @return true if some atMethod exists
	 */
	public boolean hasAtMethod() {
		return Arrays.isNotEmpty(atMethods);
	}

	/**
	 * @return at method list
	 */
	public String[] getAtMethods() {
		return atMethods;
	}

	public void setAtMethods(String[] methods) {
		atMethods = methods;
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

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
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

	public Method getActionMethod() {
		return actionMethod;
	}

	public void setActionMethod(Method method) {
		this.actionMethod = method;
	}

	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean path) {
		StringBuilder sb = new StringBuilder();

		if (path) {
			sb.append(Strings.join(paths, ", "));
			sb.append(" >> ");
		}
		
		sb.append(Methods.toSimpleString(actionMethod));
		sb.append(": @To(");
		sb.append(getViewInfo());
		sb.append(')');
		return sb.toString();
	}
	
	private String getViewInfo() {
		StringBuilder sb = new StringBuilder();
		if (Strings.isNotEmpty(defaultView)) {
			sb.append(defaultView);
		}
		if (Strings.isNotEmpty(errorView)) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("error=").append(errorView);
		}
		if (Strings.isNotEmpty(fatalView)) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("fatal=").append(fatalView);
		}
		return sb.toString();
	}
}
