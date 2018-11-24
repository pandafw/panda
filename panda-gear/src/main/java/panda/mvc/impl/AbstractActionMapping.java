package panda.mvc.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import panda.io.Streams;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.collection.CaseInsensitiveMap;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainCreator;
import panda.mvc.ActionConfig;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.mvc.MvcConstants;
import panda.servlet.HttpServlets;

public abstract class AbstractActionMapping implements ActionMapping {
	private static final Log log = Logs.getLog(AbstractActionMapping.class);

	@IocInject(value=MvcConstants.MVC_ACTION_MAPPING_CASE_SENSITIVE, required=false)
	protected boolean caseSensitive;
	
	protected abstract void addDispatcher(String path, ActionDispatcher dispatcher);
	
	protected abstract ActionDispatcher getDispatcher(String path, List<String> args);

	private Map<String, ActionDispatcher> dispatchers;
	private Map<String, ActionConfig> configs;

	public void initialize() {
		dispatchers = caseSensitive ? new HashMap<String, ActionDispatcher>() : new CaseInsensitiveMap<String, ActionDispatcher>();
		configs = caseSensitive ? new HashMap<String, ActionConfig>() : new CaseInsensitiveMap<String, ActionConfig>();
	}
	
	@Override
	public void add(ActionChainCreator acc, ActionConfig acfg) {
		// add method
		String mn = acfg.getActionType().getName() + '.' + acfg.getActionMethod().getName();
		if (configs.put(mn, acfg) != null) {
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
			ActionDispatcher dispatcher = dispatchers.get(path);
			if (dispatcher == null) {
				dispatcher = new ActionDispatcher();
				dispatchers.put(path, dispatcher);
			}
			if (acfg.hasAtMethod()) {
				for (String hm : acfg.getAtMethods()) {
					if (dispatcher.hasChain(hm)) {
						throw new IllegalArgumentException(String.format("%s.%s @At(%s, %s) is already mapped for %s.%s().", 
							acfg.getActionType().getName(), acfg.getActionMethod().getName(), path, hm, 
							dispatcher.getChain(hm).getConfig().getActionType().getName(), dispatcher.getChain(hm).getConfig().getActionMethod().getName()));
					}
					dispatcher.addChain(hm, chain);
				}
			}
			else {
				if (dispatcher.getDefaultChain() != null) {
					throw new IllegalArgumentException(String.format("%s.%s @At(%s) is already mapped for %s.%s().", 
						acfg.getActionType().getName(), acfg.getActionMethod().getName(), path, 
						dispatcher.getDefaultChain().getConfig().getActionType().getName(), dispatcher.getDefaultChain().getConfig().getActionMethod().getName()));
				}
				dispatcher.setDefaultChain(chain);
			}
			addDispatcher(path, dispatcher);
		}

		if (log.isDebugEnabled()) {
			log.debug("Add: " + acfg.toString());
		}
	}

	@Override
	public ActionDispatcher getActionDispatcher(ActionContext ac) {
		String path = HttpServlets.getServletPath(ac.getRequest());

		ac.setPath(path);
		ac.setPathArgs(new ArrayList<String>());

		ActionDispatcher dispatcher = getDispatcher(path, ac.getPathArgs());
		if (dispatcher != null) {
			ActionChain chain = dispatcher.getActionChain(ac);
			if (chain != null) {
				if (log.isDebugEnabled()) {
					log.debugf("Found mapping for [%s] path=%s : %s", ac.getRequest().getMethod(), path, chain);
				}
				return dispatcher;
			}
		}
		if (log.isDebugEnabled()) {
			log.debugf("Search mapping for path=%s : No action match", path);
		}
		return null;
	}

	@Override
	public ActionConfig getActionConfig(String path) {
		ActionDispatcher dispatcher = getDispatcher(path, null);
		if (dispatcher != null) {
			ActionChain chain = dispatcher.getDefaultChain();
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
		return configs.get(clazz.getName() + '.' + method.getName());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass());
		for (Entry<String, ActionDispatcher> en : (new TreeMap<String, ActionDispatcher>(dispatchers)).entrySet()) {
			sb.append(Streams.EOL)
				.append(" - ")
				.append(Strings.rightPad(en.getKey(), 50))
				.append(" >> ")
				.append(en.getValue().toString());
		}
		return sb.toString();
	}
	
}
