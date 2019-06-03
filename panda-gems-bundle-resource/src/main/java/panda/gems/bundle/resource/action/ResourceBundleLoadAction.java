package panda.gems.bundle.resource.action;

import panda.app.action.base.BaseLoadAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.bundle.resource.ResourceBundleLoader;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.util.ActionTextProvider;

@At("${!!task_path|||'/task'}/resource/load")
@Auth({ AUTH.LOCAL, AUTH.TOKEN, AUTH.SUPER })
public class ResourceBundleLoadAction extends BaseLoadAction {
	@IocInject
	protected ResourceBundleLoader resourceLoader;

	public ResourceBundleLoadAction() {
		super("bundle.resource.load.date");
	}

	@Override
	protected boolean doLoad() throws Exception {
		if (resourceLoader.getDatabaseResourceLoader() == null) {
			return false;
		}
		
		if (!resourceLoader.reload()) {
			return false;
		}

		((ActionTextProvider)getText()).clearResourceBundlesCache();
		return true;
	}
}
