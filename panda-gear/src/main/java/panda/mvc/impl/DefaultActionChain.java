package panda.mvc.impl;

import java.util.List;

import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionContext;
import panda.mvc.ActionConfig;
import panda.mvc.ActionProcessor;

public class DefaultActionChain implements ActionChain {
	private static final Log log = Logs.getLog(DefaultActionChain.class);
	
	private ActionConfig config;
	private List<String> procs;
	
	public static class ProxyActionChain implements ActionChain {
		private ActionConfig config;
		private List<String> procs;
		private int current = -1;

		public ProxyActionChain(ActionConfig config, List<String> procs) {
			this.config = config;
			this.procs = procs;
		}

		@Override
		public ActionConfig getConfig() {
			return config;
		}

		@Override
		public void doChain(ActionContext ac) {
			throw new IllegalStateException("Illegal call for " + getClass().getSimpleName() + ".doChain()");
		}

		@Override
		public void doNext(ActionContext ac) {
			current++;
			if (current >= procs.size()) {
				return;
			}
			
			String name = procs.get(current);
			ActionProcessor processor = initProcessor(name, ac);
			processor.process(ac);
		}

		protected ActionProcessor initProcessor(String name, ActionContext ac) {
			try {
				ActionProcessor p;
				if (Strings.startsWithChar(name, IocValue.KIND_REF)) {
					p = ac.getIoc().get(ActionProcessor.class, name.substring(1));
				}
				else {
					p = (ActionProcessor)Classes.newInstance(name);
				}
				return p;
			}
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debugf("Failed to init processor(%s) for action chain %s/%s", name, config.getChainName(), config.getActionMethod());
				}
				throw Exceptions.wrapThrow(e);
			}
		}
	}

	public DefaultActionChain(ActionConfig ainfo, List<String> procs) {
		this.config = ainfo;
		this.procs = procs;
	}

	/**
	 * @return the action config
	 */
	@Override
	public ActionConfig getConfig() {
		return config;
	}

	@Override
	public void doChain(ActionContext ac) {
		ac.setAction(ac.getIoc().get(config.getActionType()));
		ac.setChain(new ProxyActionChain(config, procs));
		ac.getChain().doNext(ac);
	}

	@Override
	public void doNext(ActionContext ac) {
		throw new IllegalStateException("Illegal call for " + getClass().getSimpleName() + ".doNext()");
	}
}
