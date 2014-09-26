package panda.wing.action.tool;

import panda.el.El;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;
import panda.wing.mvc.AbstractAction;

/**
 */
@At("/admin/el")
public class ElEvaluateAction extends AbstractAction {

	@IocInject
	protected ActionContext ac;

	@At("input")
	@Ok("ftl")
	public void input() {
	}

	@At("json")
	@Ok("json")
	public Object json(@Param("expr") String expr) throws Exception {
		return exec(expr);
	}
	
	@At("xml")
	@Ok("xml")
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
