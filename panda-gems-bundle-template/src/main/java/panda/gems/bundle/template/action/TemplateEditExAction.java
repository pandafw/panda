package panda.gems.bundle.template.action;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.bundle.template.entity.Template;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.view.ftl.FreemarkerHelper;

@At("${!!super_path|||'/super'}/template")
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
