package panda.mvc.ioc;

import panda.ioc.IocMaking;
import panda.ioc.ObjectProxy;
import panda.mvc.ActionContext;

/**
 * ObjectProxy for request bean
 */
public class RequestObjectProxy implements ObjectProxy {
	/**
	 * action context
	 */
	private ActionContext context;

	public RequestObjectProxy(ActionContext ac) {
		context = ac;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, IocMaking imak) {
		return (T)context.getRequest();
	}

	@Override
	public void depose() {
	}
}
