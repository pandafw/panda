package panda.wing.action.template;

import java.util.Map;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.wing.action.crud.GenericBulkAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Template;

@At("${super_context}/template")
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

