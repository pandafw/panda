package panda.mvc.impl;

import java.lang.reflect.Method;
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
import panda.mvc.ActionChainCreator;
import panda.mvc.ActionConfig;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.servlet.HttpServlets;

public abstract class AbstractActionMapping implements ActionMapping {
	private static final Log log = Logs.getLog(AbstractActionMapping.class);

	protected abstract void addInvoker(String path, ActionInvoker invoker);
	
	protected abstract ActionInvoker getInvoker(String path, List<String> args);

	private Map<String, ActionInvoker> ainks = new HashMap<String, ActionInvoker>();
	private Map<String, ActionConfig> acfgs = new HashMap<String, ActionConfig>();

	@Override
	public void add(ActionChainCreator acc, ActionConfig acfg) {
		// add method
		String mn = acfg.getActionType().getName() + '.' + acfg.getActionMethod().getName();
		if (acfgs.put(mn, acfg) != null) {
			throw new IllegalArgumentException(mn + " is already mapped.");
		}
		
		// normalize paths
		acfg.normalizePaths();

		// check path
		String[] paths = acfg.getPaths();
		if (Arrays.isEmpty(paths)) {
			throw new IllegalArgumentException(String.format("Empty @At of %s.%s", acfg.getActionType().getName(), acfg.getActionMethod().getName()));
		}

		ActionChain chain = acc.create(acfg);

		// mapping path
		for (String path : paths) {
			ActionInvoker invoker = ainks.get(path);
			if (invoker == null) {
				invoker = new ActionInvoker();
				ainks.put(path, invoker);
			}
			if (acfg.hasAtMethod()) {
				for (String hm : acfg.getAtMethods()) {
					if (invoker.hasChain(hm)) {
						throw new IllegalArgumentException(String.format("%s.%s @At(%s, %s) is already mapped for %s.%s().", 
							acfg.getActionType().getName(), acfg.getActionMethod().getName(), path, hm, 
							invoker.getChain(hm).getConfig().getActionType().getName(), invoker.getChain(hm).getConfig().getActionMethod().getName()));
					}
					invoker.addChain(hm, chain);
				}
			}
			else {
				if (invoker.getDefaultChain() != null) {
					throw new IllegalArgumentException(String.format("%s.%s @At(%s) is already mapped for %s.%s().", 
						acfg.getActionType().getName(), acfg.getActionMethod().getName(), path, 
						invoker.getDefaultChain().getConfig().getActionType().getName(), invoker.getDefaultChain().getConfig().getActionMethod().getName()));
				}
				invoker.setDefaultChain(chain);
			}
			addInvoker(path, invoker);
		}

		if (log.isDebugEnabled()) {
			log.debug("Add: " + acfg.toString());
		}
	}

	@Override
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

	@Override
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


	/**
	 * find action config by method
	 * 
	 * @param clazz action class
	 * @param method action method
	 * @return action config
	 */
	@Override
	public ActionConfig getActionConfig(Class clazz, Method method) {
		return acfgs.get(clazz.getName() + '.' + method.getName());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass());
		for (Entry<String, ActionInvoker> en : (new TreeMap<String, ActionInvoker>(ainks)).entrySet()) {
			sb.append(Streams.LINE_SEPARATOR)
				.append(" - ")
				.append(Strings.rightPad(en.getKey(), 50))
				.append(" >> ")
				.append(en.getValue().toString());
		}
		return sb.toString();
	}
	
}
