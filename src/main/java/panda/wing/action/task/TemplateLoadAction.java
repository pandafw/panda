package panda.wing.action.task;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.wing.action.BaseLoadAction;
import panda.wing.auth.Auth;
import panda.wing.constant.APP;
import panda.wing.constant.AUTH;
import panda.wing.util.AppFreemarkerTemplateLoader;

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
