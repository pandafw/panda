package panda.mvc.processor;

import panda.ioc.IocContext;
import panda.ioc.impl.ComboContext;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.IocRequestListener;
import panda.mvc.IocSessionListener;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.RequestIocContext;
import panda.mvc.ioc.SessionIocContext;

public class ActionProcessor extends AbstractProcessor {
	private ActionInfo ai;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		this.ai = ai;
	}

	public void process(ActionContext ac) throws Throwable {
		Object obj;
		IocContext ictx = null;
		
		if (IocRequestListener.isRequestScopeEnable && IocSessionListener.isSessionScopeEnable) {
			ictx = new ComboContext(RequestIocContext.get(ac.getRequest()), SessionIocContext.get(ac.getRequest().getSession()));
		}
		else if (IocRequestListener.isRequestScopeEnable) {
			ictx = RequestIocContext.get(ac.getRequest());
		}
		else if (IocSessionListener.isSessionScopeEnable) {
			ictx = SessionIocContext.get(ac.getRequest().getSession());
		}
		
		obj = ac.getIoc().get(ai.getActionType(), ictx);
		ac.setAction(obj);
		
		ac.setMethod(ai.getMethod());
		doNext(ac);
	}

}
