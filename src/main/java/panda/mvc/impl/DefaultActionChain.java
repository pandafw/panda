package panda.mvc.impl;

import java.util.Iterator;
import java.util.List;

import panda.lang.Exceptions;
import panda.mvc.ActionChain;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.Processor;

public class DefaultActionChain implements ActionChain {

	private ActionInfo info;
	private Processor head;

	public DefaultActionChain(ActionInfo ai, List<Processor> procs) {
		info = ai;
		if (null != procs) {
			Iterator<Processor> it = procs.iterator();
			if (it.hasNext()) {
				head = it.next();
				for (Processor p = head; it.hasNext(); ) {
					Processor next = it.next();
					p.setNext(next);
					p = next;
				}
			}
		}
	}

	public void doChain(ActionContext ac) {
		ac.setInfo(info);
		ac.setAction(ac.getIoc().get(ac.getInfo().getActionType()));

		if (null != head) {
			try {
				head.process(ac);
			}
			catch (Throwable e) {
				ac.setError(e);
				throw Exceptions.wrapThrow(e);
			}
		}
	}
}
