package panda.gems.admin.action;

import java.util.Map;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/sysdump")
@Auth(AUTH.SUPER)
public class SysDumpAction extends AbstractAction {
	@At("")
	@To(value=Views.SFTL, error=Views.SFTL)
	public void ftl(@Param Map m) {
	}

	@At
	@To(value=Views.SJSON, error=Views.SJSON)
	public void json(@Param Map m) {
	}

	@At
	@To(value=Views.SXML, error=Views.SXML)
	public void xml(@Param Map m) {
	}
}
