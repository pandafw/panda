package panda.app.action.filepool;

import panda.app.action.base.BaseTempFileAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${super_path}/filepool")
@Auth(AUTH.SUPER)
public class FilePoolViewAction extends BaseTempFileAction {
	/**
	 * Constructor
	 */
	public FilePoolViewAction() {
	}

	/**
	 * view
	 * @param id id
	 * @return view
	 * 
	 * @throws Exception if an error occurs
	 */
	@At
	@To(Views.RAW)
	public Object view(@Param("id") String id) throws Exception {
		return download(id);
	}
}
