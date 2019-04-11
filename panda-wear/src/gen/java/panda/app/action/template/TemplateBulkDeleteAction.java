package panda.app.action.template;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Template;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

@At("${!!super_path|||'/super'}/template")
@Auth(AUTH.SUPER)
public class TemplateBulkDeleteAction extends GenericBulkAction<Template> {

	/**
	 * Constructor
	 */
	public TemplateBulkDeleteAction() {
		setType(Template.class);
		addDisplayFields(Template.ID, Template.NAME, Template.LOCALE, Template.STATUS, Template.UPDATED_AT, Template.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdelete
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object bdelete(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * bdelete_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~bdelete")
	@TokenProtect
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

