package panda.mvc.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import panda.bind.json.Jsons;
import panda.ioc.annotation.IocBean;
import panda.lang.Arrays;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.View;
import panda.mvc.adaptor.DefaultParamAdaptor;
import panda.mvc.annotation.param.Param;
import panda.mvc.validation.ValidateException;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.view.Views;

@IocBean
public class ValidateProcessor extends AbstractProcessor {
	@Override
	public void process(ActionContext ac) {
		Validators validators = Mvcs.getValidators(ac);

		if (validate(ac, validators)) {
			doNext(ac);
			return;
		}

		doErrorView(ac);
	}

	protected boolean validate(ActionContext ac, Validators vts) {
		validateParams(ac, vts);
		return !(ac.getActionAlert().hasErrors() || ac.getParamAlert().hasErrors());
	}
	
	protected void validateParams(ActionContext ac, Validators vts) {
		if (Arrays.isEmpty(ac.getArgs())) {
			return;
		}

		Method method = ac.getMethod();
		Validates ma = method.getAnnotation(Validates.class);
		if (ma != null) {
			// TODO: plain method validate
		}

		Annotation[][] pass = method.getParameterAnnotations();
		for (int i = 0; i < pass.length; i++) {
			Param param = null;
			Validates vs = null;

			Annotation[] pas = pass[i];
			for (Annotation pa : pas) {
				if (pa instanceof Param) {
					param = (Param)pa;
				}
				if (pa instanceof Validates) {
					vs = (Validates)pa;
				}
			}

			if (vs == null) {
				continue;
			}

			Object obj = ac.getArgs()[i];
			String name = DefaultParamAdaptor.indexedName(i, param);

			if (!vts.validate(ac, null, name, obj, vs.value())) {
				if (vs.shortCircuit()) {
					break;
				}
			}
		}
	}

	protected void doErrorView(ActionContext ac) {
		View view = Views.evalView(ac.getIoc(), ac.getConfig().getErrorView());
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
