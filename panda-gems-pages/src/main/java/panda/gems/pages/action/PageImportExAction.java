package panda.gems.pages.action;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;

@At("${!!admin_path|||'/admin'}/pages")
@Auth(AUTH.ADMIN)
public class PageImportExAction extends PageImportAction {

	/**
	 * Constructor
	 */
	public PageImportExAction() {
		super();
	}
}

