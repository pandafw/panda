package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;

@IocBean
public class AuthenticateProcessor extends AbstractProcessor {
	@Override
	public void process(ActionContext ac) {
		doNext(ac);
	}
}
