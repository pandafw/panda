package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.util.ActionPreprocessor;

@IocBean
public class PreProcessor extends AbstractActionProcessor {
	@Override
	public void process(ActionContext ac) {
		Object action = ac.getAction();
		
		if (action instanceof ActionPreprocessor) {
			if (!((ActionPreprocessor)action).preprocess()) {
				return;
			}
		}
		doNext(ac);
	}
}
