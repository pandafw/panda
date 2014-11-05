package panda.wing.action.task;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.wing.action.BaseLoadAction;
import panda.wing.util.AppFreemarkerTemplateLoader;

/**
 */
@At("/admin/task/templateload")
public class TemplateLoadAction extends BaseLoadAction {
	@IocInject
	protected AppFreemarkerTemplateLoader aftLoader;
	
	@Override
	protected void doExecute() throws Exception {
		if (assist().isDatabaseTemplateLoader()) {
			super.doExecute();
		}
	}
	
	@Override
	protected void doLoad() {
		aftLoader.reload();
	}
}
