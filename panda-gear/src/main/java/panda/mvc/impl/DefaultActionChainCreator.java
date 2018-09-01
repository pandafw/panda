package panda.mvc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.meta.IocValue;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainCreator;
import panda.mvc.ActionConfig;
import panda.mvc.MvcConstants;
import panda.mvc.processor.AdaptProcessor;
import panda.mvc.processor.AuthenticateProcessor;
import panda.mvc.processor.FatalProcessor;
import panda.mvc.processor.InvokeProcessor;
import panda.mvc.processor.LayoutProcessor;
import panda.mvc.processor.LocaleProcessor;
import panda.mvc.processor.PrepareProcessor;
import panda.mvc.processor.RedirectProcessor;
import panda.mvc.processor.ValidateProcessor;
import panda.mvc.processor.ViewProcessor;

@IocBean(type=ActionChainCreator.class, create="initialize")
public class DefaultActionChainCreator implements ActionChainCreator {

	protected static final String DEFAULT_CHAIN = "default";
	
	@IocInject(value=MvcConstants.MVC_CHAINS, required=false)
	protected Map<String, List<String>> map;

	protected ArrayList<String> defaultChain() {
		return Arrays.toList(
			IocValue.TYPE_REF + FatalProcessor.class.getName(),
			IocValue.TYPE_REF + RedirectProcessor.class.getName(),
			IocValue.TYPE_REF + LocaleProcessor.class.getName(),
			IocValue.TYPE_REF + LayoutProcessor.class.getName(),
			IocValue.TYPE_REF + AuthenticateProcessor.class.getName(),
			IocValue.TYPE_REF + AdaptProcessor.class.getName(),
			IocValue.TYPE_REF + PrepareProcessor.class.getName(),
			IocValue.TYPE_REF + ValidateProcessor.class.getName(),
			IocValue.TYPE_REF + InvokeProcessor.class.getName(),
			IocValue.TYPE_REF + ViewProcessor.class.getName()
		);
	}
	
	public void initialize() {
		if (Collections.isEmpty(map)) {
			map = new HashMap<String, List<String>>();
		}
		if (!map.containsKey(DEFAULT_CHAIN)) {
			List<String> chain = defaultChain();
			map.put(DEFAULT_CHAIN, chain);
		}
	}

	private List<String> getProcessors(String key) {
		if (Strings.isEmpty(key)) {
			key = DEFAULT_CHAIN;
		}

		return map.get(key);
	}
	
	@Override
	public ActionChain create(ActionConfig acfg) {
		List<String> procs = getProcessors(acfg.getChainName());
		if (procs == null) {
			throw new IllegalArgumentException("Failed to find chain [" + acfg.getChainName() + "] for " + acfg.getActionType());
		}
		ActionChain chain = new DefaultActionChain(acfg, procs);
		return chain;
	}
}
