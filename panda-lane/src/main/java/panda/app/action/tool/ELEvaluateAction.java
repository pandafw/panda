package panda.app.action.tool;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.el.EL;
import panda.lang.Strings;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

@At("${super_path}/el")
@Auth(AUTH.SUPER)
public class ELEvaluateAction extends AbstractAction {

	@At("")
	@To(View.SFTL)
	public void input() {
	}

	@At
	@To(View.SJSON)
	public Object json(@Param("expr") String expr) {
		return exec(expr);
	}
	
	@At
	@To(View.SXML)
	public Object xml(@Param("expr") String expr) {
		return exec(expr);
	}

	protected Object exec(String expr) {
		if (Strings.isEmpty(expr)) {
			return null;
		}
		
		try {
			return EL.eval(expr, getContext());
		}
		catch (Exception e) {
			getContext().setError(e);
			return null;
		}
	}
}
