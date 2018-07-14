package panda.app.action.template;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Template;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${super_path}/template")
@Auth(AUTH.SUPER)
public class TemplateBulkAction extends GenericBulkAction<Template> {

	/**
	 * Constructor
	 */
	public TemplateBulkAction() {
		setType(Template.class);
		addDisplayFields(Template.ID, Template.NAME, Template.LANGUAGE, Template.COUNTRY, Template.STATUS, Template.UUSID, Template.UTIME);
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
	@At
	@To(value=Views.SFTL, error="sftl:~bdelete")
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

