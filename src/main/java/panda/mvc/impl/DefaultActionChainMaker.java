package panda.mvc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.MvcConstants;
import panda.mvc.processor.AdaptProcessor;
import panda.mvc.processor.FatalProcessor;
import panda.mvc.processor.InvokeProcessor;
import panda.mvc.processor.LayoutProcessor;
import panda.mvc.processor.LocaleProcessor;
import panda.mvc.processor.ValidateProcessor;
import panda.mvc.processor.ViewProcessor;

@IocBean(type=ActionChainMaker.class, create="initialize")
public class DefaultActionChainMaker implements ActionChainMaker {

	private static final String DEFAULT_CHAIN = "default";
	
	@IocInject(value=MvcConstants.MVC_CHAINS, required=false)
	protected Map<String, List<String>> map;

	public void initialize() {
		if (Collections.isEmpty(map)) {
			map = new HashMap<String, List<String>>();
		}
		if (map.containsKey(DEFAULT_CHAIN)) {
			List<String> defs = Arrays.toList(
				FatalProcessor.class.getName(),
				AdaptProcessor.class.getName(),
				LocaleProcessor.class.getName(),
				LayoutProcessor.class.getName(),
				ValidateProcessor.class.getName(),
				InvokeProcessor.class.getName(),
				ViewProcessor.class.getName()
			);
			map.put(DEFAULT_CHAIN, defs);
		}
	}

	private List<String> getProcessors(String key) {
		if (Strings.isEmpty(key)) {
			key = DEFAULT_CHAIN;
		}

		return (List<String>)map.get(key);
	}
	
	public ActionChain eval(MvcConfig config, ActionInfo ai) {
		List<String> procs = getProcessors(ai.getChainName());
		ActionChain chain = new DefaultActionChain(ai, procs);
		return chain;
	}
}
