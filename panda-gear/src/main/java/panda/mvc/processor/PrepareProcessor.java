package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.lang.Prepareable;
import panda.mvc.ActionContext;

@IocBean
public class PrepareProcessor extends AbstractProcessor {
	@Override
	public void process(ActionContext ac) {
		Object action = ac.getAction();
		
		if (action instanceof Prepareable) {
			((Prepareable)action).prepare();
		}
		doNext(ac);
	}
}
