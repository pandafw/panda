package panda.wing.action.tool;

import panda.el.El;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

/**
 */
@At("/admin/el")
@Auth(AUTH.SYSADMIN)
public class ElEvaluateAction extends AbstractAction {

	@IocInject
	protected ActionContext ac;

	@At("")
	@Ok(View.FREEMARKER)
	public void input() {
	}

	@At("json")
	@Ok(View.JSON)
	public Object json(@Param("expr") String expr) throws Exception {
		return exec(expr);
	}
	
	@At("xml")
	@Ok(View.XML)
	public Object xml(@Param("expr") String expr) throws Exception {
		return exec(expr);
	}

	protected Object exec(String expr) throws Exception {
		if (Strings.isEmpty(expr)) {
			return null;
		}
		
		return El.eval(expr, ac);
	}
}
