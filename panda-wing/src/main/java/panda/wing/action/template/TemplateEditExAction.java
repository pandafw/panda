package panda.wing.action.template;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Template;

@At("${super_path}/template")
@Auth(AUTH.SUPER)
public class TemplateEditExAction extends TemplateEditAction {

	@IocInject
	private FreemarkerHelper freemarker;
	
	@Override
	protected boolean checkOnInput(Template data, Template srcData) {
		try {
			freemarker.newTemplate(data.getSource());
			return true;
		}
		catch (Exception e) {
			addFieldError(Template.SOURCE, e.getMessage());
			return false;
		}
	}

}
