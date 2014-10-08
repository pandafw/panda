package panda.mvc.processor;

import panda.bind.json.Jsons;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.validation.DefaultValidators;
import panda.mvc.validation.ValidateException;
import panda.mvc.validation.Validators;

public class ValidateProcessor extends ViewProcessor {

	private Validators validators;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		view = evalView(config, ai, ai.getErrorView());

		validators = config.getIoc().getIfExists(Validators.class);
		if (validators == null) {
			validators = new DefaultValidators();
		}
	}

	public void process(ActionContext ac) throws Throwable {
		if (validators.validate(ac)) {
			doNext(ac);
			return;
		}

		if (view == null) {
			StringBuilder sb = new StringBuilder();
			
			sb.append("Validation error occurs for " + ac.getPath() + ": \n");
			if (ac.getActionAware().hasErrors()) {
				sb.append("ActionErrors: ");
				Jsons.toJson(ac.getActionAware().getErrors(), sb, true);
			}
			if (ac.getParamAware().hasErrors()) {
				sb.append("ParamErrors: ");
				Jsons.toJson(ac.getParamAware().getErrors(), sb, true);
			}
			throw new ValidateException(sb.toString());
		}
			
		view.render(ac);
	}
}
