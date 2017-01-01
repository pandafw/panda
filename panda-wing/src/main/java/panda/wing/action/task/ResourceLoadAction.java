package panda.wing.action.task;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.util.DefaultTextProvider;
import panda.wing.action.base.BaseLoadAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.util.AppResourceBundleLoader;

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
		((DefaultTextProvider)getText()).clearResourceBundlesCache();

		arbLoader.reload();
		
		return true;
	}
}
