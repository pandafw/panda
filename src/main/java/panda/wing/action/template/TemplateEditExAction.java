package panda.wing.action.template;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Template;

@At("${super_context}/template")
@Auth(AUTH.SUPER)
public class TemplateEditExAction extends TemplateEditAction {

	@IocInject
	private FreemarkerHelper fh;
	
	@Override
	protected boolean checkOnInput(Template data, Template srcData) {
		try {
			fh.initTemplate(data.getSource());
			return true;
		}
		catch (Exception e) {
			addFieldError(Template.SOURCE, e.getMessage());
			return false;
		}
	}

}
