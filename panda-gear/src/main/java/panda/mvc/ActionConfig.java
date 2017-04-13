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

	private String okView;
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
		okView = null == okView ? parent.okView : okView;
		errorView = null == errorView ? parent.errorView : errorView;
		fatalView = null == fatalView ? parent.fatalView : fatalView;
		actionType = null == actionType ? parent.actionType : actionType;
		chainName = null == chainName ? parent.chainName : chainName;
		return this;
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

	public Method getActionMethod() {
		return actionMethod;
	}

	public void setActionMethod(Method method) {
		this.actionMethod = method;
	}

	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean noPath) {
		StringBuilder sb = new StringBuilder();

		if (!noPath) {
			sb.append(Strings.join(paths, ", "));
			sb.append(" >> ");
		}
		
		sb.append(Methods.toSimpleString(actionMethod));
		sb.append(": @To(");
		if (Strings.isNotEmpty(okView)) {
			sb.append("ok=").append(okView).append(", ");
		}
		if (Strings.isNotEmpty(errorView)) {
			sb.append("error=").append(errorView).append(", ");
		}
		if (Strings.isNotEmpty(fatalView)) {
			sb.append("fatal=").append(fatalView);
		}
		sb.append(')');
		return sb.toString();
	}
}
