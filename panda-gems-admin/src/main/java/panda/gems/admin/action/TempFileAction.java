package panda.gems.admin.action;

import panda.mvc.annotation.At;
import panda.app.action.base.BaseFileAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;


@At("${!!super_path|||'/super'}/tmp")
@Auth(AUTH.SUPER)
public class TempFileAction extends BaseFileAction {
}
