package panda.wing.action.tool;

import panda.el.El;
import panda.lang.Strings;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_context}/el")
@Auth(AUTH.SUPER)
public class ElEvaluateAction extends AbstractAction {

	@At("")
	@To(View.SFTL)
	public void input() {
	}

	@At
	@To(all=View.JSON)
	public Object json(@Param("expr") String expr) {
		return exec(expr);
	}
	
	@At
	@To(all=View.XML)
	public Object xml(@Param("expr") String expr) {
		return exec(expr);
	}

	protected Object exec(String expr) {
		if (Strings.isEmpty(expr)) {
			return null;
		}
		
		try {
			return El.eval(expr, getContext());
		}
		catch (Exception e) {
			getContext().setError(e);
			return null;
		}
	}
}
