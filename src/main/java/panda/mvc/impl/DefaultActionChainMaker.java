package panda.mvc.impl;

import java.util.ArrayList;
import java.util.List;

import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionChainMaker;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.Processor;
import panda.mvc.impl.chainconfig.ActionChainMakerConfiguration;
import panda.mvc.impl.chainconfig.JsonActionChainMakerConfiguration;

public class DefaultActionChainMaker implements ActionChainMaker {

	private static final Log logger = Logs.getLog(DefaultActionChainMaker.class);

	ActionChainMakerConfiguration conf;

	public DefaultActionChainMaker(String... args) {
		conf = new JsonActionChainMakerConfiguration(args);
	}

	public ActionChain eval(MvcConfig config, ActionInfo ai) {
		try {
			List<Processor> list = new ArrayList<Processor>();
			for (String name : conf.getProcessors(ai.getChainName())) {
				Processor processor = getProcessorByName(config, name);
				processor.init(config, ai);
				list.add(processor);
			}

			Processor errp = getProcessorByName(config, conf.getErrorProcessor(ai.getChainName()));
			errp.init(config, ai);

			/*
			 * 返回动作链实例
			 */
			ActionChain chain = new DefaultActionChain(list, errp, ai.getMethod());
			return chain;
		}
		catch (Throwable e) {
			if (logger.isDebugEnabled()) {
				logger.debugf("Failed to eval action chain for %s/%s", ai.getChainName(), ai.getMethod());
			}
			throw Exceptions.wrapThrow(e);
		}
	}

	protected static Processor getProcessorByName(MvcConfig config, String name) throws Exception {
		if (name.startsWith("ioc:") && name.length() > 4) {
			return config.getIoc().get(Processor.class, name.substring(4));
		}
		return (Processor)Classes.born(name);
	}
}
