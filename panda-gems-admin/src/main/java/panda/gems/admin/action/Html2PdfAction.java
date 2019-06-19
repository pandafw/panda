package panda.gems.admin.action;

import panda.app.action.base.BaseHtml2PdfAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/html2pdf")
@Auth(AUTH.SUPER)
public class Html2PdfAction extends BaseHtml2PdfAction {
}
