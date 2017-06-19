package panda.app.action.task;

import panda.app.action.base.BaseLoadAction;
import panda.app.auth.Auth;
import panda.app.constant.APP;
import panda.app.constant.AUTH;
import panda.app.util.AppFreemarkerTemplateLoader;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;

@At("/task/template/load")
@Auth({ AUTH.LOCAL, AUTH.SUPER })
public class TemplateLoadAction extends BaseLoadAction {
	@IocInject
	protected AppFreemarkerTemplateLoader aftLoader;
	
	public TemplateLoadAction() {
		super(APP.TEMPLATE_LOAD_DATE);
	}
	
	@Override
	protected void doExecute() throws Exception {
		if (assist().isDatabaseTemplateLoader()) {
			super.doExecute();
		}
	}
	
	@Override
	protected boolean doLoad() {
		aftLoader.reload();
		return true;
	}
}
