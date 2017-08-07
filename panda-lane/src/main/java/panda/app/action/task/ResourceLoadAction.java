package panda.app.action.task;

import panda.app.action.base.BaseLoadAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.util.AppResourceBundleLoader;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.util.ActionTextProvider;

@At("/task/resource/load")
@Auth({ AUTH.LOCAL, AUTH.SUPER })
public class ResourceLoadAction extends BaseLoadAction {
	@IocInject
	protected AppResourceBundleLoader arbLoader;

	@Override
	protected void doExecute() throws Exception {
		if (assist().isDatabaseResourceLoader()) {
			super.doExecute();
		}
	}
	
	@Override
	protected boolean doLoad() throws Exception {
		((ActionTextProvider)getText()).clearResourceBundlesCache();

		arbLoader.reload();
		
		return true;
	}
}
