package panda.mvc.ioc;

import panda.ioc.IocMaking;
import panda.ioc.ObjectProxy;
import panda.mvc.ActionContext;

/**
 * ObjectProxy for response bean
 */
public class ResponseObjectProxy implements ObjectProxy {
	/**
	 * action context
	 */
	private ActionContext context;

	public ResponseObjectProxy(ActionContext ac) {
		context = ac;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, IocMaking imak) {
		return (T)context.getResponse();
	}

	@Override
	public void depose() {
	}
}
