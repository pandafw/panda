package panda.app.action.tool;

import java.util.Map;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

@At("${super_path}/sysdump")
@Auth(AUTH.SUPER)
public class SysDumpAction extends AbstractAction {
	@At("")
	@To(value=View.SFTL, error=View.SFTL)
	public void ftl(@Param Map m) {
	}

	@At
	@To(value=View.SJSON, error=View.SJSON)
	public void json(@Param Map m) {
	}

	@At
	@To(value=View.SXML, error=View.SXML)
	public void xml(@Param Map m) {
	}
}
