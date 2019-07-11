package panda.gems.admin.action;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.el.EL;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/el")
@Auth(AUTH.SUPER)
public class ELEvaluateAction extends BaseAction {

	@At("")
	@To(Views.SFTL)
	public void input() {
	}

	@At
	@TokenProtect
	@To(Views.SJSON)
	public Object json(@Param("expr") String expr) {
		return exec(expr);
	}
	
	@At
	@TokenProtect
	@To(Views.SXML)
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
