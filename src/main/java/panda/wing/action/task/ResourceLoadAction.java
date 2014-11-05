package panda.wing.action.task;

import panda.io.resource.ResourceBundleLoader;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.util.DefaultTextProvider;
import panda.wing.action.BaseLoadAction;
import panda.wing.util.AppResourceBundleLoader;

/**
 */
@At("/admin/task/resourceload")
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
	protected void doLoad() throws Exception {
		((DefaultTextProvider)getText()).clearResourceBundlesCache();

		((AppResourceBundleLoader)arbLoader).reload();
	}
}
