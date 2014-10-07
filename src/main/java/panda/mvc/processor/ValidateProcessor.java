package panda.mvc.processor;

import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.validation.DefaultValidators;
import panda.mvc.validation.Validators;

public class ValidateProcessor extends AbstractProcessor {

	private Validators validators;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		if (config.getIoc().has(Validators.class)) {
			validators = config.getIoc().get(Validators.class);
		}
		else {
			validators = new DefaultValidators();
		}
	}

	public void process(ActionContext ac) throws Throwable {
		validators.validate(ac);
		doNext(ac);
	}
}
