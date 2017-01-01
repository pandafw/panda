package panda.wing.action.tool;

import java.util.Map;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_context}/sysdump")
@Auth(AUTH.SUPER)
public class SysDumpAction extends AbstractAction {
	@At("")
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public void ftl(@Param Map m) {
	}

	@At
	@Ok(View.JSON)
	@Err(View.JSON)
	public void json(@Param Map m) {
	}

	@At
	@Ok(View.XML)
	@Err(View.XML)
	public void xml(@Param Map m) {
	}
}
