package panda.mvc.processor;

import panda.bind.json.Jsons;
import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.validation.DefaultValidators;
import panda.mvc.validation.ValidateException;
import panda.mvc.validation.Validators;

@IocBean
public class ValidateProcessor extends ViewProcessor {
	@Override
	public void process(ActionContext ac) {
		View view = evalView(ac.getIoc(), ac.getInfo().getErrorView());

		Validators validators = ac.getIoc().getIfExists(Validators.class);
		if (validators == null) {
			validators = new DefaultValidators();
		}

		if (validators.validate(ac)) {
			doNext(ac);
			return;
		}

		if (view == null) {
			StringBuilder sb = new StringBuilder();
			
			sb.append("Validation error occurs for " + ac.getPath() + ": \n");
			if (ac.getActionAlert().hasErrors()) {
				sb.append("ActionErrors: ");
				Jsons.toJson(ac.getActionAlert().getErrors(), sb, true);
			}
			if (ac.getParamAlert().hasErrors()) {
				sb.append("ParamErrors: ");
				Jsons.toJson(ac.getParamAlert().getErrors(), sb, true);
			}
			throw new ValidateException(sb.toString());
		}
			
		view.render(ac);
	}
}
