package panda.gems.bundle.template.action;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.gems.bundle.template.entity.Template;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

public abstract class TemplateBulkDisableAction extends GenericBulkAction<Template> {

	/**
	 * Constructor
	 */
	public TemplateBulkDisableAction() {
		setType(Template.class);
		setDisplayFields(Template.ID, Template.NAME, Template.LOCALE, Template.STATUS, Template.UPDATED_AT, Template.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdisable
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object bdisable(@Param Map<String, String[]> args) {
		return super.bupdate(args);
	}

	/**
	 * bdisable_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~bdisable")
	@TokenProtect
	public Object bdisable_execute(@Param Map<String, String[]> args) {
		return super.bupdate_execute(args);
	}
	
}

