package panda.mvc.impl;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import panda.lang.Exceptions;
import panda.mvc.ActionChain;
import panda.mvc.ActionContext;
import panda.mvc.Processor;

public class DefaultActionChain implements ActionChain {

	private Processor head;

	private Processor error;

	private Method method;

	public DefaultActionChain(List<Processor> procs, Processor error, Method method) {
		if (null != procs) {
			Iterator<Processor> it = procs.iterator();
			if (it.hasNext()) {
				head = it.next();
				Processor p = head;
				while (it.hasNext()) {
					Processor next = it.next();
					p.setNext(next);
					p = next;
				}
			}
		}
		this.error = error;
		this.method = method;
	}

	public void doChain(ActionContext ac) {
		if (null != head) {
			try {
				head.process(ac);
			}
			catch (Throwable e) {
				ac.setError(e);
				try {
					error.process(ac);
				}
				catch (Throwable ee) {
					throw Exceptions.wrapThrow(ee);
				}
			}
		}
	}

	public String toString() {
		return method.toString();
	}
}
