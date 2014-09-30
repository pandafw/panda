package panda.mvc.processor;

import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;

public class ActionProcessor extends AbstractProcessor {
	private ActionInfo ai;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		this.ai = ai;
	}

	public void process(ActionContext ac) throws Throwable {
		Object a = ac.getIoc().get(ai.getActionType());
		ac.setAction(a);
		
		ac.setMethod(ai.getMethod());
		doNext(ac);
	}

}
