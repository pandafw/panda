package panda.gems.bundle.template.action;

import panda.app.action.work.SyncLoadAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.bundle.template.TemplateBundleLoader;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;

@At("${!!task_path|||'/task'}/template/load")
@Auth({ AUTH.LOCAL, AUTH.TOKEN, AUTH.SUPER })
public class TemplateBundleLoadAction extends SyncLoadAction {
	@IocInject
	protected TemplateBundleLoader templateLoader;
	
	public TemplateBundleLoadAction() {
		super("bundle.template.load.date");
	}
	
	@Override
	protected boolean doLoad() {
		if (templateLoader.getDatabaseTemplateLoader() == null) {
			return false;
		}

		templateLoader.reload();
		return true;
	}
}
