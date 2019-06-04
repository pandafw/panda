package panda.gems.bundle.template.action;

import panda.app.action.crud.GenericImportAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.bundle.template.entity.Template;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/template")
@Auth(AUTH.SUPER)
public class TemplateImportAction extends GenericImportAction<Template> {

	/**
	 * Constructor
	 */
	public TemplateImportAction() {
		setType(Template.class);
		setDisplayFields(Template.ID, Template.NAME, Template.LOCALE, Template.STATUS, Template.UPDATED_AT, Template.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * importx
	 * @param arg argument
	 * @return result or view
	 */
	@At("import")
	@To(value=Views.SFTL, error=Views.SFTL)
	@TokenProtect
	public Object importx(@Param Arg arg) {
		return super.importx(arg);
	}
	
}

