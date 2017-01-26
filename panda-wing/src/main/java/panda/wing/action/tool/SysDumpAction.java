package panda.wing.action.tool;

import java.util.Map;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_context}/sysdump")
@Auth(AUTH.SUPER)
public class SysDumpAction extends AbstractAction {
	@At("")
	@To(value=View.SFTL, error=View.SFTL)
	public void ftl(@Param Map m) {
	}

	@At
	@To(value=View.JSON, error=View.JSON)
	public void json(@Param Map m) {
	}

	@At
	@To(value=View.XML, error=View.XML)
	public void xml(@Param Map m) {
	}
}
