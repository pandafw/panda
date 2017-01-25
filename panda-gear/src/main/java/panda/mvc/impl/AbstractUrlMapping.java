package panda.mvc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionContext;
import panda.mvc.ActionConfig;
import panda.mvc.MvcConfig;
import panda.mvc.UrlMapping;
import panda.servlet.HttpServlets;

public abstract class AbstractUrlMapping implements UrlMapping {
	private static final Log log = Logs.getLog(AbstractUrlMapping.class);

	protected abstract void addInvoker(String path, ActionInvoker invoker);
	
	protected abstract ActionInvoker getInvoker(String path, List<String> args);

	private Map<String, ActionInvoker> map = new HashMap<String, ActionInvoker>();
	
	public void add(ActionChainMaker acm, ActionConfig ac, MvcConfig mc) {
		// check path
		String[] paths = ac.getPaths();
		if (Arrays.isEmpty(paths)) {
			throw new IllegalArgumentException(String.format("Empty @At of %s.%s", ac.getActionType().getName(), ac.getActionMethod().getName()));
		}

		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (Strings.isEmpty(path)) {
				paths[i] = "/";
			}
			else if (path.charAt(0) != '/') {
				paths[i] = '/' + path;
			}
		}

		ActionChain chain = acm.eval(mc, ac);

		// mapping path
		for (String path : paths) {
			ActionInvoker invoker = map.get(path);
			if (invoker == null) {
				invoker = new ActionInvoker();
				map.put(path, invoker);
			}
			if (ac.hasAtMethod()) {
				for (String hm : ac.getAtMethods()) {
					if (invoker.hasChain(hm)) {
						throw new IllegalArgumentException(String.format("%s.%s @At(%s, %s) is already mapped for %s.%s().", 
							ac.getActionType().getName(), ac.getActionMethod().getName(), path, hm, 
							invoker.getChain(hm).getConfig().getActionType().getName(), invoker.getChain(hm).getConfig().getActionMethod().getName()));
					}
					invoker.addChain(hm, chain);
				}
			}
			else {
				if (invoker.getDefaultChain() != null) {
					throw new IllegalArgumentException(String.format("%s.%s @At(%s) is already mapped for %s.%s().", 
						ac.getActionType().getName(), ac.getActionMethod().getName(), path, 
						invoker.getDefaultChain().getConfig().getActionType().getName(), invoker.getDefaultChain().getConfig().getActionMethod().getName()));
				}
				invoker.setDefaultChain(chain);
			}
			addInvoker(path, invoker);
		}

		if (log.isDebugEnabled()) {
			log.debug("Add: " + ac.toString());
		}
	}

	public ActionInvoker getActionInvoker(ActionContext ac) {
		String path = HttpServlets.getServletPath(ac.getRequest());

		ac.setPath(path);
		ac.setPathArgs(new ArrayList<String>());

		ActionInvoker invoker = getInvoker(path, ac.getPathArgs());
		if (invoker != null) {
			ActionChain chain = invoker.getActionChain(ac);
			if (chain != null) {
				if (log.isDebugEnabled()) {
					log.debugf("Found mapping for [%s] path=%s : %s", ac.getRequest().getMethod(), path, chain);
				}
				return invoker;
			}
		}
		if (log.isDebugEnabled()) {
			log.debugf("Search mapping for path=%s : No action match", path);
		}
		return null;
	}

	public ActionConfig getActionConfig(String path) {
		ActionInvoker invoker = getInvoker(path, null);
		if (invoker != null) {
			ActionChain chain = invoker.getDefaultChain();
			if (chain != null) {
				return chain.getConfig();
			}
		}
		if (log.isDebugEnabled()) {
			log.debugf("Search mapping for path=%s : No action match", path);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass());
		for (Entry<String, ActionInvoker> en : (new TreeMap<String, ActionInvoker>(map)).entrySet()) {
			sb.append(Streams.LINE_SEPARATOR).append(" - ").append(en.getValue().toString());
		}
		return sb.toString();
	}
	
}
