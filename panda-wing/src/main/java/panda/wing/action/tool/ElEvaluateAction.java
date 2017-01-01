package panda.wing.action.tool;

import panda.el.El;
import panda.lang.Strings;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_context}/el")
@Auth(AUTH.SUPER)
public class ElEvaluateAction extends AbstractAction {

	@At("")
	@Ok(View.SFTL)
	public void input() {
	}

	@At
	@Ok(View.JSON)
	@Err(View.JSON)
	@Fatal(View.JSON)
	public Object json(@Param("expr") String expr) {
		return exec(expr);
	}
	
	@At
	@Ok(View.XML)
	@Err(View.XML)
	@Fatal(View.XML)
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
