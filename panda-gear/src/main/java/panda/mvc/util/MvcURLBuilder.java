package panda.mvc.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;
import panda.net.URLHelper;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLBuilder;

/**
 * Implementations of this interface can be used to build a URL
 */
@IocBean(singleton = false)
public class MvcURLBuilder extends ServletURLBuilder {
	/**
	 * The includeParams attribute may have the value 'none', 'get' or 'all'. 
	 * none - include no parameters in the URL 
	 * get - include only GET parameters in the URL (default) 
	 * all - include both GET and POST parameters in the URL
	 */
	public static final String INCLUDE_NONE = "none";
	public static final String INCLUDE_GET = "get";
	public static final String INCLUDE_ALL = "all";

	public static final String SUPPRESS_NONE = "none";
	public static final String SUPPRESS_EMPTY = "empty";
	public static final String SUPPRESS_NULL = "null";
	
	protected ActionContext context;

	protected String action;

	protected String includeParams;
	protected List<String> excludeParams;

	protected boolean includeContext = true;

	// --------------------
	/**
	 * @return the context
	 */
	public ActionContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	@IocInject
	public void setContext(ActionContext context) {
		this.context = context;
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the includeParams
	 */
	public String getIncludeParams() {
		return includeParams;
	}

	/**
	 * @param includeParams the includeParams to set
	 */
	public void setIncludeParams(String includeParams) {
		this.includeParams = includeParams;
	}

	/**
	 * @return the excludeParams
	 */
	public List<String> getExcludeParams() {
		return excludeParams;
	}

	/**
	 * @param excludeParams the excludeParams to set
	 */
	public void setExcludeParams(List<String> excludeParams) {
		this.excludeParams = excludeParams;
	}

	/**
	 * @return the includeContext
	 */
	public boolean isIncludeContext() {
		return includeContext;
	}

	/**
	 * @param includeContext the includeContext to set
	 */
	@IocInject(value = MvcConstants.URL_INCLUDE_CONTEXT, required = false)
	public void setIncludeContext(boolean includeContext) {
		this.includeContext = includeContext;
	}

	/**
	 * @return the suppress
	 */
	public String getSuppressParam() {
		switch (suppress) {
		case URLHelper.SUPPRESS_EMPTY:
			return SUPPRESS_EMPTY;
		case URLHelper.SUPPRESS_NONE:
			return SUPPRESS_NONE;
		default:
			return SUPPRESS_NULL;
		}
	}

	/**
	 * @param suppress the suppress to set
	 */
	public void setSuppressParam(String suppress) {
		if (SUPPRESS_EMPTY.equalsIgnoreCase(suppress)) {
			setSuppress(URLHelper.SUPPRESS_EMPTY);
		}
		else if (SUPPRESS_NONE.equalsIgnoreCase(suppress)) {
			setSuppress(URLHelper.SUPPRESS_NONE);
		}
		else {
			setSuppress(URLHelper.SUPPRESS_NULL);
		}
	}

	/**
	 * @param encoding the encoding to set
	 */
	@IocInject(value = MvcConstants.URL_ENCODING, required = false)
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@SuppressWarnings("unchecked")
	private Map getParamsMap() {
		Map ps = null;
		if (params == null) {
			ps = new LinkedHashMap();
		}
		else if (params instanceof Map) {
			ps = new LinkedHashMap((Map)params);
		}
		else {
			ps = castors.cast(params, LinkedHashMap.class);
		}
		return ps;
	}

	//-------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public void addParams(Map ps) {
		if (Collections.isEmpty(ps)) {
			return;
		}
		
		params = getParamsMap();
		((Map)params).putAll(ps);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String build() {
		setBeans(Mvcs.getBeans());
		setCastors(Mvcs.getCastors());
		setRequest(context.getRequest());

		// split action and query string
		if (Strings.isNotEmpty(action)) {
			int q = action.indexOf('?');
			if (q >= 0) {
				String qs = action.substring(q + 1);
				action = action.substring(0, q);
				if (Strings.isEmpty(query)) {
					query = qs;
				}
				else {
					query += '&' + qs;
				}
			}
		}
		
		if (path == null) {
			path = buildPath(context, action, includeContext);
		}

		if (INCLUDE_GET.equalsIgnoreCase(includeParams)) {
			String qs = HttpServlets.getRequestQueryString(request);
			Map<String, Object> qsm = URLHelper.parseQueryString(qs);
			if (Collections.isNotEmpty(qsm)) {
				Map ps = getParamsMap();
				for (Entry<String, Object> en : qsm.entrySet()) {
					if (ps.containsKey(en.getKey()) || Collections.contains(excludeParams, en.getKey())) {
						continue;
					}
					ps.put(en.getKey(), en.getValue());
				}
				params = ps;
			}
		}
		else if (INCLUDE_ALL.equalsIgnoreCase(includeParams)) {
			Map ps = getParamsMap();
			Map<String, String[]> qm = request.getParameterMap();
			for (Entry<String, String[]> en : qm.entrySet()) {
				if (ps.containsKey(en.getKey()) || Collections.contains(excludeParams, en.getKey())) {
					continue;
				}
				ps.put(en.getKey(), en.getValue());
			}
			params = ps;
		}

		return super.build();
	}

	@Override
	protected String castString(Object value) {
		return Mvcs.castString(context, value);
	}

	//-------------------------------------------------------------------
	public static String buildPath(ActionContext context, String action) {
		return buildPath(context, action, true);
	}
	
	public static String buildPath(ActionContext context, String action, boolean includeContext) {
		String uri;

		if (Strings.isNotEmpty(action)) {
			if (action.startsWith("//")) {
				// absolute site path
				uri = action.substring(1);
			}
			else {
				uri = action;

				char c = action.charAt(0);
				if (c == '.') {
					// relative path
					String self = context.getPath();
					uri = URLHelper.resolveURL(self, action);
					if (includeContext) {
						uri = context.getBase() + uri;
					}
				}
				else if (c == '~') {
					// resolve URL ( ~/xxx = ./xxx)
					String self = context.getPath();
					String path = Strings.stripStart(action.substring(1), '/');
					uri = URLHelper.resolveURL(self, path);
					if (includeContext) {
						uri = context.getBase() + uri;
					}
				}
				else if (c == '+') {
					// append path
					String self = context.getPath();
					uri = URLHelper.appendPath(self, action.substring(1));
					if (includeContext) {
						uri = context.getBase() + uri;
					}
				}
				else if (c == '/') {
					if (includeContext) {
						uri = context.getBase() + uri;
					}
				}
			}
		}
		else {
			// Go to "same page"
			uri = context.getPath();
			if (includeContext) {
				uri = context.getBase() + uri;
			}
		}
		
		return uri;
	}
	
	public static String buildURL(ActionContext context, Object params) {
		MvcURLBuilder ub = context.getIoc().get(MvcURLBuilder.class);
		ub.setParams(params);
		return ub.build();
	}
	
	public static String buildQueryString(ActionContext context, Object params) {
		MvcURLBuilder ub = context.getIoc().get(MvcURLBuilder.class);
		ub.setPath("");
		ub.setParams(params);
		return ub.build();
	}
}
