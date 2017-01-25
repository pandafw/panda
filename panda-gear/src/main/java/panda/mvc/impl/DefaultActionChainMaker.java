package panda.mvc.impl;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionConfig;
import panda.mvc.MvcConfig;
import panda.mvc.MvcConstants;
import panda.mvc.processor.AdaptProcessor;
import panda.mvc.processor.FatalProcessor;
import panda.mvc.processor.InvokeProcessor;
import panda.mvc.processor.LayoutProcessor;
import panda.mvc.processor.LocaleProcessor;
import panda.mvc.processor.PrepareProcessor;
import panda.mvc.processor.ValidateProcessor;
import panda.mvc.processor.ViewProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IocBean(type=ActionChainMaker.class, create="initialize")
public class DefaultActionChainMaker implements ActionChainMaker {

	private static final String DEFAULT_CHAIN = "default";
	
	@IocInject(value=MvcConstants.MVC_CHAINS, required=false)
	protected Map<String, List<String>> map;

	public void initialize() {
		if (Collections.isEmpty(map)) {
			map = new HashMap<String, List<String>>();
		}
		if (!map.containsKey(DEFAULT_CHAIN)) {
			List<String> defs = Arrays.toList(
				DefaultActionChain.IOC_PREFIX + FatalProcessor.class.getName(),
				DefaultActionChain.IOC_PREFIX + LocaleProcessor.class.getName(),
				DefaultActionChain.IOC_PREFIX + LayoutProcessor.class.getName(),
				DefaultActionChain.IOC_PREFIX + AdaptProcessor.class.getName(),
				DefaultActionChain.IOC_PREFIX + PrepareProcessor.class.getName(),
				DefaultActionChain.IOC_PREFIX + ValidateProcessor.class.getName(),
				DefaultActionChain.IOC_PREFIX + InvokeProcessor.class.getName(),
				DefaultActionChain.IOC_PREFIX + ViewProcessor.class.getName()
			);
			map.put(DEFAULT_CHAIN, defs);
		}
	}

	private List<String> getProcessors(String key) {
		if (Strings.isEmpty(key)) {
			key = DEFAULT_CHAIN;
		}

		return map.get(key);
	}
	
	public ActionChain eval(MvcConfig config, ActionConfig ai) {
		List<String> procs = getProcessors(ai.getChainName());
		if (procs == null) {
			throw new IllegalArgumentException("Failed to find chain [" + ai.getChainName() + "] for " + ai.getActionType());
		}
		ActionChain chain = new DefaultActionChain(ai, procs);
		return chain;
	}
}
