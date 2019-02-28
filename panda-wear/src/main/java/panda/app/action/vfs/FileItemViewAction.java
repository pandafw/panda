package panda.app.action.vfs;

import panda.app.action.base.BaseTempFileAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.vfs.FileItem;

@At("${super_path}/fileitem")
@Auth(AUTH.SUPER)
public class FileItemViewAction extends BaseTempFileAction {
	/**
	 * Constructor
	 */
	public FileItemViewAction() {
	}

	/**
	 * view
	 * @param name name
	 * @return view
	 * 
	 * @throws Exception if an error occurs
	 */
	@At
	@To(Views.RAW)
	public Object view(@Param(FileItem.NAME) String name) throws Exception {
		return download(name);
	}
}
