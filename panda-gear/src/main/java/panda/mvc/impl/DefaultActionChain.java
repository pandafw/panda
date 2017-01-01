package panda.mvc.impl;

import java.util.List;

import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionChain;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.Processor;

public class DefaultActionChain implements ActionChain {
	private static final Log log = Logs.getLog(DefaultActionChain.class);
	
	public static final String IOC_PREFIX = "#";
	
	private ActionInfo ainfo;
	private List<String> procs;
	
	public static class ProxyActionChain implements ActionChain {
		private ActionInfo ainfo;
		private List<String> procs;
		private int current = -1;

		public ProxyActionChain(ActionInfo ainfo, List<String> procs) {
			this.ainfo = ainfo;
			this.procs = procs;
		}

		@Override
		public ActionInfo getInfo() {
			return ainfo;
		}

		@Override
		public void doChain(ActionContext ac) {
			throw new IllegalStateException("Illegal call for ProxyActionChain.doChain()");
		}

		@Override
		public void doNext(ActionContext ac) {
			current++;
			if (current >= procs.size()) {
				return;
			}
			
			String name = procs.get(current);
			Processor processor = initProcessor(name, ac);
			processor.process(ac);
		}

		protected Processor initProcessor(String name, ActionContext ac) {
			try {
				Processor p;
				if (name.startsWith(IOC_PREFIX)) {
					p = ac.getIoc().get(Processor.class, name.substring(1));
				}
				else {
					p = (Processor)Classes.born(name);
				}
				return p;
			}
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debugf("Failed to init processor(%s) for action chain %s/%s", name, ainfo.getChainName(), ainfo.getMethod());
				}
				throw Exceptions.wrapThrow(e);
			}
		}
	}

	public DefaultActionChain(ActionInfo ainfo, List<String> procs) {
		this.ainfo = ainfo;
		this.procs = procs;
	}

	/**
	 * @return the info
	 */
	public ActionInfo getInfo() {
		return ainfo;
	}

	public void doChain(ActionContext ac) {
		ac.setAction(ac.getIoc().get(ainfo.getActionType()));
		ac.setChain(new ProxyActionChain(ainfo, procs));
		ac.getChain().doNext(ac);
	}

	public void doNext(ActionContext ac) {
		throw new IllegalStateException("Illegal call for DefaultActionChain.doNext()");
	}
}
