package panda.mvc.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import panda.bind.json.Jsons;
import panda.ioc.annotation.IocBean;
import panda.lang.Arrays;
import panda.mvc.ActionContext;
import panda.mvc.ValidateException;
import panda.mvc.ValidateHandler;
import panda.mvc.View;
import panda.mvc.adaptor.DefaultParamAdaptor;
import panda.mvc.annotation.param.Param;
import panda.mvc.validator.Validators;
import panda.mvc.view.Views;

@IocBean
public class ValidateProcessor extends AbstractActionProcessor {
	@Override
	public void process(ActionContext ac) {
		ValidateHandler vh = Validators.getValidateHandler(ac);

		if (validate(ac, vh)) {
			doNext(ac);
			return;
		}

		doErrorView(ac);
	}

	protected boolean validate(ActionContext ac, ValidateHandler vh) {
		validateParams(ac, vh);
		return !(ac.getActionAlert().hasErrors() || ac.getParamAlert().hasErrors());
	}
	
	protected void validateParams(ActionContext ac, ValidateHandler vh) {
		if (Arrays.isEmpty(ac.getArgs())) {
			return;
		}

		Method method = ac.getMethod();
		// TODO: plain method validate

		Annotation[][] pass = method.getParameterAnnotations();
		for (int i = 0; i < pass.length; i++) {
			Param param = null;

			Annotation[] pas = pass[i];
			for (Annotation pa : pas) {
				if (pa instanceof Param) {
					param = (Param)pa;
					break;
				}
			}

			String name = DefaultParamAdaptor.indexedName(i, param);
			Object obj = ac.getArgs()[i];

			vh.validate(ac, null, name, obj, pas);
		}
	}

	protected void doErrorView(ActionContext ac) {
		View view = Views.createErrorView(ac);
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
