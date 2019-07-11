package panda.gems.bundle.property.action;

import panda.app.action.work.SyncLoadAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.bundle.PropertyBundleLoader;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.util.ActionTextProvider;

@At("${!!task_path|||'/task'}/property/load")
@Auth({ AUTH.LOCAL, AUTH.TOKEN, AUTH.SUPER })
public class PropertyBundleLoadAction extends SyncLoadAction {
	@IocInject
	protected PropertyBundleLoader propertyLoader;

	public PropertyBundleLoadAction() {
		super("bundle.property.load.date");
	}

	@Override
	protected boolean doLoad() throws Exception {
		if (propertyLoader.getDatabaseResourceLoader() == null) {
			return false;
		}

		if (!propertyLoader.reload()) {
			return false;
		}

		((ActionTextProvider)getText()).clearResourceBundlesCache();
		return true;
	}
}
