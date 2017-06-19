package panda.app.action.template;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Template;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

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
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object bdelete(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * bdelete_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:~bdelete")
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

