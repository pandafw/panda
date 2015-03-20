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
	
	private ActionInfo info;
	private List<String> procs;
	private int current;

	public DefaultActionChain(ActionInfo ai, List<String> procs) {
		info = ai;
		this.procs = procs;
	}

	/**
	 * @return the info
	 */
	public ActionInfo getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(ActionInfo info) {
		this.info = info;
	}

	public void doChain(ActionContext ac) {
		ac.setChain(this);
		ac.setInfo(info);
		ac.setAction(ac.getIoc().get(ac.getInfo().getActionType()));
		current = -1;
		doNext(ac);
	}

	public void doNext(ActionContext ac) {
		current++;
		if (current >= procs.size()) {
			return;
		}
		
		String name = procs.get(current);
		Processor processor = initProcessor(name, ac);
		try {
			processor.process(ac);
		}
		catch (Throwable e) {
			ac.setError(e);
			throw Exceptions.wrapThrow(e);
		}
	}

	protected Processor initProcessor(String name, ActionContext ac) {
		try {
			Processor p;
			if (name.startsWith("#")) {
				p = ac.getIoc().get(Processor.class, name.substring(1));
			}
			else {
				p = (Processor)Classes.born(name);
			}
			return p;
		}
		catch (Throwable e) {
			if (log.isDebugEnabled()) {
				log.debugf("Failed to init processor(%s) for action chain %s/%s", name, info.getChainName(), info.getMethod());
			}
			throw Exceptions.wrapThrow(e);
		}
	}
}
