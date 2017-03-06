package panda.mvc.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import panda.io.Streams;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionContext;

public class ActionInvoker {
	private static final Log log = Logs.getLog(ActionInvoker.class);

	private ActionChain defaultChain;

	private Map<String, ActionChain> chainMap;

	public ActionInvoker() {
	}

	/**
	 * add a ActionChain
	 * 
	 * @param method HTTP method
	 * @param chain action chain
	 */
	public void addChain(String method, ActionChain chain) {
		if (chainMap == null) {
			chainMap = new HashMap<String, ActionChain>();
		}
		chainMap.put(method, chain);
	}

	/**
	 * has ActionChain
	 * 
	 * @param method HTTP method
	 * @return true if has the specified method chain
	 */
	public boolean hasChain(String method) {
		if (chainMap == null) {
			return false;
		}
		return chainMap.containsKey(method);
	}

	/**
	 * get ActionChain
	 * 
	 * @param method HTTP method
	 * @return chain
	 */
	public ActionChain getChain(String method) {
		if (chainMap == null) {
			return null;
		}
		return chainMap.get(method);
	}

	/**
	 * @return the defaultChain
	 */
	public ActionChain getDefaultChain() {
		return defaultChain;
	}

	/**
	 * @param defaultChain the defaultChain to set
	 */
	public void setDefaultChain(ActionChain defaultChain) {
		this.defaultChain = defaultChain;
	}

	/**
	 * invoke action
	 * 
	 * @param ac action context
	 * @return true if a action is found and execute successfully
	 */
	public boolean invoke(ActionContext ac) {
		ActionChain chain = getActionChain(ac);
		if (chain == null) {
			if (log.isDebugEnabled()) {
				log.debugf("No chain for request (path=%s, method=%s)", ac.getPath(), ac.getRequest().getMethod());
			}
			return false;
		}
		chain.doChain(ac);
		return true;
	}

	public ActionChain getActionChain(ActionContext ac) {
		HttpServletRequest req = ac.getRequest();
		String method = Strings.upperCase(req.getMethod());
		ActionChain chain = getChain(method);
		if (chain != null) {
			return chain;
		}
		
		return defaultChain;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (defaultChain != null) {
			sb.append(defaultChain.getConfig().toString(true));
		}
		if (Collections.isNotEmpty(chainMap)) {
			for (Entry<String, ActionChain> en : chainMap.entrySet()) {
				if (sb.length() > 0) {
					sb.append(Streams.LINE_SEPARATOR).append("   - ");
				}
				sb.append(en.getKey()).append(": ").append(en.getValue().getConfig());
			}
		}
		return sb.toString();
	}

}
